package com.sz7road.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-11
 * Time: 上午9:47
 * 定义一些常量数据，可以快速的通过编码找到名称
 */
public class DropDownDataUtil {
    /**
     * 得到学校类型的Map
     * @return
     */
    public static Map<Integer,String> getSchoolTypeMap()
    {
       Map<Integer,String> schoolTypes= new HashMap<Integer, String>();
        schoolTypes.put(0,"100");
        schoolTypes.put(1,"小学");
        schoolTypes.put(2,"初中");
        schoolTypes.put(3,"高中");
        schoolTypes.put(4,"大学");
        schoolTypes.put(100,"其它");
        return schoolTypes;
    }

    /**
     * 根据编号得到学校类型
     * @param schoolTypeKey
     * @return
     */
    public static String getSchoolType(int schoolTypeKey)
    {
        String schoolTypeText=getSchoolTypeMap().get(schoolTypeKey);
        if(Strings.isNullOrEmpty(schoolTypeText))
        {
            schoolTypeText="其它";
        }
        return schoolTypeText;
    }

    /**
     * 得到教育程度的Map
     * @return
     */
    public static Map<Integer,String> getEduLevMap()
    {
        Map<Integer,String> eduLevels= new HashMap<Integer, String>();
        eduLevels.put(0,"请选择");
        eduLevels.put(1,"初中");
        eduLevels.put(2,"高中");
        eduLevels.put(3,"中技");
        eduLevels.put(4,"中专");
        eduLevels.put(5,"大专");
        eduLevels.put(6,"本科");
        eduLevels.put(7,"硕士");
        eduLevels.put(8,"博士");
        eduLevels.put(100,"其它");

        return eduLevels;
    }

    /**
     * 根据编号得到教育程度
     * @param eduLevKey
     * @return
     */
    public static String getEduLev(int eduLevKey)
    {
        String eduLevText=getEduLevMap().get(eduLevKey);
        if(Strings.isNullOrEmpty(eduLevText))
        {
            eduLevText="其它";
        }
        return eduLevText;
    }

    /**
     * 得到婚姻状态的Map
     * @return
     */
    public static Map<Integer,String> getMarryStatusMap()
    {
        Map<Integer,String> MarryStatus= new HashMap<Integer, String>();
        MarryStatus.put(0,"未婚");
        MarryStatus.put(1,"已婚");
        return MarryStatus;
    }

    /**
     * 根据编号得到婚姻状态
     * @param marryStatusKey
     * @return
     */
    public static String getMarryStatus(int marryStatusKey)
    {
        String marryStatusText=getMarryStatusMap().get(marryStatusKey);
        if(Strings.isNullOrEmpty(marryStatusText))
        {
            marryStatusText="未婚";
        }
        return marryStatusText;
    }

    /**
     * 得到允许上传的文件的后缀名
     * @return
     */
    public static Map<String,String> getEnableUploadExtMap()
    {
        Map<String,String> enableUploadExts=new HashMap<String, String>();
        enableUploadExts.put("png","png");
        enableUploadExts.put("jpg","jpg");
        enableUploadExts.put("gif","gif");
        return enableUploadExts;
    }

    /**
     * 得到允许上传的文件的后缀名的编号
     * @return
     */
    public static String getEnableUploadExtValue(String text)
    {
        String keyStr=null;
        Map<String,String> enableUploadExts=new HashMap<String, String>();
        enableUploadExts.put("1","png");
        enableUploadExts.put("2","jpg");
        enableUploadExts.put("3","gif");

        for(String key:enableUploadExts.keySet())
        {
            if(text.equalsIgnoreCase(enableUploadExts.get(key)))
            {
              keyStr=key;
            }
        }
        return keyStr;
    }

    /**
     * 得到允许上传的文件的后缀名的名称
     * @return
     */
    public static String getEnableUploadExtText(String key)
    {
        Map<String,String> enableUploadExts=new HashMap<String, String>();
        enableUploadExts.put("1","png");
        enableUploadExts.put("2","jpg");
        enableUploadExts.put("3","gif");


        return enableUploadExts.get(key);
    }
    public static String getEnableUploadExt(String key)
    {
        Map<String,String> enableUploadExts=new HashMap<String, String>();
        enableUploadExts.put("1","png");
        enableUploadExts.put("2","jpg");
        enableUploadExts.put("3","gif");
        if(enableUploadExts.containsValue(key.toLowerCase()))
        return key;
        return null;
    }

    public static void main(String[] args)
    {
        String oldImgName="df443c91-a558-4165-afec-551be7996584.jpg";
        System.out.println(oldImgName.substring(oldImgName.lastIndexOf('.') + 1, oldImgName.length()));
    }

}
