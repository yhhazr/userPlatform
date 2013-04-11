package com.sz7road.userplatform.utils;

import com.sz7road.userplatform.pojos.SimpleResponseResult;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * User: leo.liao
 * Date: 12-6-6
 * Time: 上午11:32
 */
public final class Helper {

    public static String RandomCode(int len) {
        java.util.Random rand = new Random(); // 设置随机种子
        // 设置备选验证码:包括"a-zA-Z"和数字"0-9"
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int size = base.length();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int start = rand.nextInt(size);
            String tmpStr = base.substring(start, start + 1);
            str.append(tmpStr);
        }
        return str.toString();
    }

    /**
     * 发送回客户端告知该请求执行结果。
     *
     * @throws java.io.IOException
     */
    public static void sendResponse(final HeadlessServletResponse response, final Object object) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String rsp = mapper.writeValueAsString(object);
            response.getWriter().print(rsp);
        } catch (Exception ex) {
//            log.error("写回response失败！");
        } finally {
                response.getWriter().flush();
                response.getWriter().close();
        }
    }
}
