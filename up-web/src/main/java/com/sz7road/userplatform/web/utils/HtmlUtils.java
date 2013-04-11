package com.sz7road.userplatform.web.utils;

import com.google.common.base.Strings;
import com.sz7road.userplatform.pojos.FormItem;
import com.sz7road.userplatform.utils.DataUtil;

import java.util.*;

/**
 * @author jiangfan.zhou
 */

public class HtmlUtils {

    public static String html2Entity(String html) {
        if (Strings.isNullOrEmpty(html) || html.trim().equals(""))
            return html;
        html = html.trim();
        html = html.replaceAll("&", "&amp;");
        html = html.replaceAll("\"", "&quot;");
        html = html.replaceAll("'", "&apos;");
        html = html.replaceAll("<", "&lt;");
        html = html.replaceAll(">", "&gt;");
        return html;
    }

    public static String html2FullWidthChar(String html) {
        if (html == null || html.trim().equals(""))
            return html;
        html = html.replaceAll("&", "＆");
        html = html.replaceAll("\"", "＼");
        html = html.replaceAll("'", "＇");
        html = html.replaceAll("<", "＜");
        html = html.replaceAll(">", "＞");
        html = html.replaceAll("=", "＝");
        //html = html.replaceAll("\\.","。");
        html = html.replaceAll("javascript", "ｊａｖａｓｃｒｉｐｔ");
        return html;
    }

    public static String entity2Html(String html) {
        if (html == null || html.trim().equals(""))
            return html;
        html = html.replaceAll("&quot;", "\"");
        html = html.replaceAll("&apos;", "'");
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        html = html.replaceAll("&amp;", "&");
        return html;
    }

    /**
     * 转义之后判断最大长度
     *
     * @param item
     * @param maxLength
     * @return
     */
    public static String html2EntityAndCheckMaxLength(String item, int maxLength) {
        String userName = HtmlUtils.html2Entity(item);
        if (!Strings.isNullOrEmpty(userName)&& userName.length() > maxLength) {
            userName = userName.trim().substring(0, maxLength);
        }
        return userName;
    }

    /**
     * 14，判断非空并返回消息和状态码!
     */

    public static Map<String, Object> checkItemAndReturnMsg(List<FormItem> items) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msg = "没有为空的项，填写完整!";
        int code = 200;
        boolean checkResult = false;
        if (items != null && !items.isEmpty()) {
            for (FormItem item : items) {
                String value = item.getItemValue();
                if (Strings.isNullOrEmpty(value)) {
                    msg = "请填写" + item.getMsg() + "!";
                    code = item.getCode();
                    checkResult = true;
                    break;
                }
            }
            result.put("result", checkResult);
            result.put("msg", msg);
            result.put("code", code);
        }
        return result;
    }

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }


    public static boolean isChinese(String strName) {
        boolean flag = false;
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c) == true) {
                flag = isChinese(c);
            } else {
                flag = false;
                break;
            }

        }
        return flag;

    }

    public static boolean isNotChinese(String strName) {
        boolean flag = false;
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c) == false) {
                flag = true;
                break;
            } else {
                flag = false;
            }

        }
        return flag;

    }


    public static void main(String[] args) {
//       List<FormItem> items=new LinkedList<FormItem>();
//
//       items.add(new FormItem("userName","sfsdfsdfd","用户名",201));
//        items.add(new FormItem("psw","","密码",202));
//        items.add(new FormItem("pswc","sfsdfsdfd","确认密码",203));
//        items.add(new FormItem("email","sfsdfsdfd","邮箱",204));
//
//        Map<String,Object> checkResult= checkItemAndReturnMsg(items);
//        int code=(Integer)checkResult.get("code");
//        String msg=(String)checkResult.get("msg");
//        boolean result=(Boolean)checkResult.get("result");
//        System.out.println("状态码："+code+" 消息： "+msg+" 结果："+result);

        String name = "aaaa你好";

        System.out.println(name + " 是中文名字吗？" + isChinese(name));
        System.out.println(name + " 不是中文名字吗？" + isNotChinese(name));

        System.out.println(DataUtil.getHandledEmail(""));
    }
}
