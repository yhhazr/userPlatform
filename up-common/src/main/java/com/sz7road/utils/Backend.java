/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * BackEnd(后端)，The specified http-base client for backend server.<p/>
 * 后端服务器的HTTP协议请求端，比如GET、POST到后端的服务器作登录验证，内容获取等。<p/>
 * 对于所有未指定字符编码的处理上均以UTF-8编码处理。
 *
 * @author jeremy
 */
public class Backend {

    /**
     * 8秒连接超时
     */
    public static final int CONNECTION_TIMEOUT = 8 * 1000;

    /**
     * 15秒读取超时
     */
    public static final int READ_TIMEOUT = 15 * 1000;

    /**
     * 8KB输入缓冲
     */
    public static final int BUFFER_SIZE = 8 * 1024;

    /**
     * 默认字符集UTF-8
     */
    public static Charset DEFAULT_CHARSET = Charsets.UTF_8;

    private static final Logger log = LoggerFactory.getLogger(Backend.class);

    /**
     * 后端响应信息。
     */
    public static interface BackendResponse {

        /**
         * 返回响应状态值。
         *
         * @return response code
         */
        int getResponseCode();

        /**
         * 返回响应数据，仅在返回响应状态值为200时才有数据。
         *
         * @return response content if the response code is 200, null otherwise.
         */
        String getResponseContent();

        /**
         * 返回响应数据, 仅在返回响应状态值为200时才有数据。
         *
         * @return response data if response code is 200, null otherwise.
         */
        byte[] getResponseData();

        /**
         * 获取返回的字符编码。
         *
         * @return response encoding of content.
         */
        Charset getResponseEncoding();

        /**
         * 获取返回的内容类型。
         *
         * @return typeof response content
         */
        String getResponseContentType();

    }

    private Backend() {
        super();
    }

    public static BackendResponse get(final URL url) {
        if (null == url) {
            throw new NullPointerException("url");
        }
        try {
            final URLConnection conn = url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDefaultUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setDoInput(true);
            conn.setDoOutput(false);

            conn.connect();

            final byte[] data = read(conn.getInputStream());
            return new BackendResponse() {
                @Override
                public final int getResponseCode() {
                    try {
                        return ((HttpURLConnection) conn).getResponseCode();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    return -1;
                }

                @Override
                public final String getResponseContent() {
                    return new String(getResponseData(), getResponseEncoding());
                }

                @Override
                public final byte[] getResponseData() {
                    return data;
                }

                @Override
                public Charset getResponseEncoding() {
                    return Backend.getResponseEncoding(conn);
                }

                @Override
                public String getResponseContentType() {
                    return conn.getContentType();
                }
            };
        } catch (final IOException ioe) {
            log.error("Backend IOException caught at BackendClient: {}", ioe.getMessage());
        } catch (final Exception e) {
            log.error("Backend Exception caught at BackendClient: {}", e.getMessage());
        } finally {
        }

        return null;
    }

    public static BackendResponse get(final String dest) {
        try {
            return get(new URL(dest));
        } catch (MalformedURLException e) {
            log.error("无效后端GET地址：{} - {}", dest, e.getMessage());
        }
        return null;
    }

    public static BackendResponse get(String dest, final Map<String, Object> parameters) {
        if (Strings.isNullOrEmpty(dest)) {
            throw new IllegalArgumentException("Illegal dest");
        }

        try {
            if (null != parameters && !parameters.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();

                    if (sb.length() > 0) {
                        sb.append('&');
                    }
                    sb.append(key).append('=').append(URLEncoder.encode(value, Charsets.UTF_8.name()));
                }

                if (sb.length() > 0) {
                    dest = dest.trim();
                    int i = dest.indexOf("?");
                    if (i > 0 && i < dest.length() - 1) {
                        dest = dest.concat(sb.insert(0, '&').toString());
                    } else if (i == dest.length() - 1 || i == -1) {
                        dest = dest.concat("?").concat(sb.toString());
                    }
                }
            }
            return get(dest);
        } catch (final IOException e) {
        }
        return null;
    }

    public static BackendResponse post(final String dest, final Map<String, Object> parameters) {
        return post(dest, parameters, null);
    }

    public static BackendResponse post(final String dest, final Map<String, Object> parameters, final Map<String, String> headers) {
        try {
            if(!Strings.isNullOrEmpty(dest))
            {
            final URL url = new URL(dest);
            byte[] postData = null;
            if (null != parameters) {
                final StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();

                    if (!Strings.isNullOrEmpty(key) && !Strings.isNullOrEmpty(value)) {
                        if (sb.length() > 0) {
                            sb.append('&');
                        }
                        sb.append(key).append('=').append(value);
                    }
                }

                if (sb.length() > 0) {
                    postData = sb.toString().getBytes(Charsets.UTF_8);
                }
            }
            return post(url, postData, headers);
            }
        } catch (MalformedURLException e) {
            log.error("无效后端POST地址：{} - {}", dest, e.getMessage());
        }
        return null;
    }

    public static BackendResponse post(final URL url, byte[] postData) {
        return post(url, postData, null);
    }

    public static BackendResponse post(final URL url, byte[] postData, Map<String, String> headers) {
        if (null == url) throw new NullPointerException("url");
        if (null == headers) headers = Maps.newHashMap();
        if (null == postData) postData = new byte[0];

        if (!headers.containsKey("Content-Type"))
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        if (!headers.containsKey("Content-Encoding"))
            headers.put("Content-Encoding", "UTF-8");

        try {
            final URLConnection conn = url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDefaultUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            for (final Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry<String, String> entry = it.next();
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            conn.connect();
            conn.getOutputStream().write(postData);
            conn.getOutputStream().flush();

            final byte[] data = read(conn.getInputStream());
            return new BackendResponse() {
                @Override
                public final int getResponseCode() {
                    try {
                        return ((HttpURLConnection) conn).getResponseCode();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    return -1;
                }

                @Override
                public final String getResponseContent() {
                    return new String(getResponseData(), getResponseEncoding());
                }

                @Override
                public final byte[] getResponseData() {
                    return data;
                }

                @Override
                public Charset getResponseEncoding() {
                    return Backend.getResponseEncoding(conn);
                }

                @Override
                public String getResponseContentType() {
                    return conn.getContentType();
                }
            };
        } catch (final IOException ioe) {
            log.error("Backend IOException caught at BackendClient: {}", ioe.getMessage());
        } catch (final Exception e) {
            log.error("Backend Exception caught at BackendClient: {}", e.getMessage());
        } finally {
        }

        return null;
    }

    static byte[] read(final InputStream stream) throws IOException {
        if (null != stream) {
            //final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            //return IOUtils.toByteArray(reader);
            return IOUtils.toByteArray(stream);
        }
        return null;
    }

//    static byte[] read(final InputStream stream) throws IOException {
//        boolean done = false;
//        int off = 0, length;
//        // initialize with 8K buffer.
//        int capacity = BUFFER_SIZE, remaining = capacity;
//        byte[] dst = new byte[0], buf = new byte[capacity];
//
//        while (!done) {
//            length = stream.read(buf, off, remaining);
//            off += length;
//            remaining = capacity - off;
//
//            if (remaining == 0 || length == -1) {
//                // full with buffer or the last time access.
//                byte[] newBuf = new byte[dst.length + off];
//                System.arraycopy(dst, 0, newBuf, 0, dst.length);
//                System.arraycopy(buf, 0, newBuf, dst.length, off);
//                dst = newBuf;
//
//                if (length != -1) {
//                    off = 0;
//                    remaining = (capacity <<= 1);
//                    buf = new byte[capacity];
//                } else {
//                    done = true;
//                }
//            }
//        }
//        return dst;
//    }

    static Charset getResponseEncoding(URLConnection conn) {
        String encoding = conn.getContentEncoding();
        if (Strings.isNullOrEmpty(encoding)) {
            final String contentType = conn.getContentType();
            if (contentType != null && contentType.matches(".*[;]charset[=](\\w+)$")) {
                encoding = contentType.split(";", 2)[1].split("=", 2)[1];
            }
        }
        if (!Strings.isNullOrEmpty(encoding)) {
            return Charset.forName(encoding);
        }
        return DEFAULT_CHARSET;
    }
}
