package util;

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

import java.util.List;

public class HttpClientUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class.getName());

    public static String getContent(String url, List<NameValuePair> qparams) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);

        try {
            httpost.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);
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
            return "500";
        }
        return null;
    }


}
