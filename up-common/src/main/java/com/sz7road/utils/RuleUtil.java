package com.sz7road.utils;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-14
 * Time: 上午9:48
 * 通用规则类
 */
public class RuleUtil {
    /**
     * 得到密码的强度等级
     * 2.7 字母，数字，特殊字符都有 等级为7
     * 2.6 字母和特殊字符 等级为6
     * 2.5 数字和特殊字符 等级为5
     * 2.4 数字和字母 等级为4
     * 2.3 纯特殊字符 等级为3
     * 2.2 纯字母 等级为2
     * 2.1 纯数字 等级为1
     * 空的       -1
     * 长度不符合要求或者9位以下的纯数字 0
     *
     * @param psw 密码不含空格
     * @return
     */
    public static int getPswStrength(String psw) {
        int pswStrength = 0, countNum = 0, countChar = 0, countSpacialChar = 0;
        if (Strings.isNullOrEmpty(psw)) {
            pswStrength = -1;
        } else {
            char[] pswCharArray = psw.toCharArray();
            int pswLength = pswCharArray.length;
            if (pswLength < 6 || pswLength > 20) {
                pswStrength = 0;
            } else {
                for (char ch : pswCharArray) {
                    if (Character.isDigit(ch)) {
                        countNum++;
                    } else if (Character.isLetter(ch)) {
                        countChar++;
                    } else {
                        countSpacialChar++;
                    }
                }
                if (countNum == 0) {
                    if (countChar > 0 && countSpacialChar == 0) {
                        pswStrength = 2;
                    }
                    if (countChar == 0 && countSpacialChar > 0) {
                        pswStrength = 3;
                    }
                    if (countChar > 0 && countSpacialChar > 0) {
                        pswStrength = 6;
                    }
                }
                if (countNum > 0) {
                    if (countNum >=6 && countChar == 0 && countSpacialChar == 0) {
                        pswStrength = 1;
                    }
                    if (countChar > 0 && countSpacialChar == 0) {
                        pswStrength = 4;
                    }
                    if (countChar == 0 && countSpacialChar > 0) {
                        pswStrength = 5;
                    }
                    if (countChar > 0 && countSpacialChar > 0) {
                        pswStrength = 7;
                    }
                }
            }
        }
        return pswStrength;
    }

    /**
     * 获得文件的扩展名
     * @param imgName
     * @return
     */
    public static String getImgExt(String imgName)
    {
      return  imgName.substring(imgName.lastIndexOf(".") + 1, imgName.length());
    }


    /**
     * 过滤特殊文字
     * @param imgName  图片文件路径
     * @return
     */
    public static boolean isContainStrangeWord(String imgName)
    {
        boolean flag=false;
        Map<String,String> strangeWords=new HashMap<String, String>();
        strangeWords.put("php","php");
        strangeWords.put("js","js");
        strangeWords.put("conf","conf");
        strangeWords.put("exe","exe");
        strangeWords.put("asp","asp");
        strangeWords.put("..","..");
        strangeWords.put("/","/");
        strangeWords.put("dll","dll");

        imgName= imgName.toLowerCase();
        for(String str:strangeWords.values())
        {
           if(imgName.contains(str))
           {
             flag=true;
               break;
           }
        }
        return  flag;
    }

    public static void main(String[] args) {
        System.out.println("9位的数字：" + RuleUtil.getPswStrength("123456789"));
        System.out.println("9位以下的数字：" + RuleUtil.getPswStrength("111111"));
        System.out.println("字母：" + RuleUtil.getPswStrength("aaaaaaa"));
        System.out.println("特殊字符：" + RuleUtil.getPswStrength("!!!!@@#!"));
        System.out.println("数字+字母：" + RuleUtil.getPswStrength("1234abc"));
        System.out.println("数字+特殊字符：" + RuleUtil.getPswStrength("123!@##@!"));
        System.out.println("字母+特殊字符：" + RuleUtil.getPswStrength("abc!@#!@#"));
        System.out.println("数字+字母+特殊字符：" + RuleUtil.getPswStrength("12abc!@#"));
        System.out.println("空：" + RuleUtil.getPswStrength(""));
        System.out.println("[6,20]范围之外：" + RuleUtil.getPswStrength("12"));

        System.out.println("含有特殊字符吗：" + RuleUtil.isContainStrangeWord("./../p.jpg"));
    }


}
