package com.sz7road.userplatform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.utils.Base64;
import com.sz7road.utils.DesUtils;
import com.sz7road.utils.MD5Utils;
import org.apache.http.impl.cookie.DateUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: leo.liao
 * Date: 12-6-8
 * Time: 下午3:36
 */
public class Test {
    public static void main(String[] args){
        try {
            String uname = "jeremy";
            int uid = 8;
            long time = System.currentTimeMillis() / 1000;
            final Map<String, Object> param = Maps.newHashMap();
            param.put("uname", uname);
            param.put("uid", String.valueOf(uid));
            param.put("time", String.valueOf(time));

            String password = UUID.randomUUID().toString();

            String _time = Long.toString(System.currentTimeMillis());
            String loginKey = "SQONE-S67roadfLogin-777yxd33goon-06578-69b8FN-AD11-interf-111SGEEN";
                   loginKey = "SQONE-S67roadfLogin-777yxd33goon-06578-69b8FN-AD11-interf-111SGEEN";
            StringBuilder content = new StringBuilder();
            content.append(uid);
            content.append('|');
            content.append(password);
            content.append('|');
            content.append(_time);
            content.append('|');
            //签名 user+password+time + key
            String _sign = MD5Utils.digestAsHex(uid + password + time + loginKey);
            content.append(_sign);
            
            System.out.println("content="+content.toString());
            
            List<String> list = Lists.newArrayList(param.keySet());
            Collections.sort(list);

            final StringBuilder sb = new StringBuilder();
            for (String p : list) {
                sb.append(p).append("=").append(param.get(p));
            }
            int sid = 5;
            final String loginUrl = "http://assist"+sid+".shenquol.com/login";
            final String key = "SQONE-S67roadfLogin-777yxd33goon-06578-69b8FN-AD11-interf-111SGEEN";
            final String sign = MD5Utils.digestAsHex(sb.toString() + key);

            param.put("sign", sign);
            param.put("sid", sid);
            String str = "?uname=" + uname + "&uid=" + String.valueOf(uid)+
                    "&time=" + String.valueOf(time) + "&sign=" + sign+ "&sid=" + sid;
            String url = loginUrl + str;
            System.out.println(url);
//            Headend.redirectForm(response, loginUrl, "GET", param);
            String md5key = "fc02cf7b-bc85-53bb-807b-a33b0544873c";
            String name = "admin777";
            String id = "237";
            String time1 = "1339753841279";
            String signSrc = id + name + time1 + md5key;
            String sign1 = MD5Utils.digestAsHex(signSrc).toLowerCase();
            System.out.println(sign1);
            String userInfo = id + "," + name + "," + time1 + "," + sign1;
            String cookie = URLEncoder.encode(Base64.encode(userInfo), "utf-8");
            System.out.println(cookie);
            String dd = Base64.decode2Str(URLDecoder.decode("MTQ2LDdyb2FkYWRtaW4sMTM0MDAyMzIyNjA2Myw0YTdhMWYxN2ExZTBlM2JhOWVmYWJkN2VjZGRjOTJiNQ%3D%3D","utf-8"));
            String[] dds = dd.split(",");
            String ss = dds[0]+dds[1]+dds[2]+md5key;
            String sign11 = MD5Utils.digestAsHex(ss);
            System.out.println(sign11);
            System.out.println(dd);
            
            String jdbc_url = "SPmS+WcH7TnWVhkdstPdSGdSVI6Wm9LLXW+8pMt8qhzDivbpcoiiGKKCVvZSM9Gt7CbMDbbdb7HrSAOkTefRuTIRitzruxGY";
            String jdbcUrl = DesUtils.decrypt(jdbc_url);
            System.out.println(jdbcUrl);

            String jdbc_url2 = "jdbc:mysql://192.168.1.11:2433/db_userplatform?characterEncoding=utf8";
            String jdbcUrl2 = DesUtils.encrypt(jdbc_url2);
            System.out.println(jdbcUrl2);
            System.out.println(DesUtils.encrypt("leo"));
            System.out.println(DesUtils.encrypt("leoLEO123"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
