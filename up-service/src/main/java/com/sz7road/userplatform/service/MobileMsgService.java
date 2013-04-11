package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.utils.ConfigurationUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangfan.zhou
 */

@Singleton
public class MobileMsgService  extends Injection {

    private final static Logger log = LoggerFactory.getLogger(MobileMsgService.class.getName());

    public String send(String mobile, String content) {
        String sn = ConfigurationUtils.get("sendMsg.sn");
        String key = ConfigurationUtils.get("sendMsg.key");
        String url = ConfigurationUtils.get("sendMsg.url");
        //String content = ConfigurationUtils.get("sendMsg.content");

        sn = Strings.isNullOrEmpty(sn) ? "SDK-666-010-01745" : sn;
        key = Strings.isNullOrEmpty(key) ? "399524" : key;
        url = Strings.isNullOrEmpty(url) ? "http://sdk2.entinfo.cn/z_mdsmssend.aspx" : url;

        String tmp = sn + key.toUpperCase();
        String pwd = DigestUtils.md5Hex(tmp).toUpperCase();

        try{
            url = url + "?sn=" + sn + "&pwd=" + pwd + "&mobile=" + mobile + "&content=" + URLEncoder.encode(content, "gb2312");
        } catch (Exception e) {
            throw new IllegalArgumentException("message content invidal content");
        }
        return getContent(url, new ArrayList<NameValuePair>());
    }

    public static String getContent(String url, List<NameValuePair> qparams) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try {
            post.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() != 200){
                return "" + statusLine.getStatusCode();
            }
            HttpEntity entity = new BufferedHttpEntity(response.getEntity());
            if (entity != null) {
                entity = new BufferedHttpEntity(entity);
                return EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            log.error("请求URL：{}出现异常{}",url,e.getMessage());
        }
        return null;
    }

}
