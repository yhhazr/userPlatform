package com.sz7road.web.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-16
 * Time: 下午12:01
 * 参数的一些实用类
 */
public class ParamUtil {

    private static final Logger log = LoggerFactory.getLogger(ParamUtil.class);

    /**
     * 验证参数是不是空的
     *
     * @param params 参数列表
     */
    public static void checkParamsNull(List<String> params) {
        for (String param : params) {
            if (Strings.isNullOrEmpty(param)) {
                log.info(param.getClass().getSimpleName() + "为空!");
                return;
            }
        }
    }

    /**
     * 判断数组中是不是含有element
     *
     * @param element
     * @param array
     * @return
     */
    public static boolean containInt(int element, int[] array) {
        boolean flag = false;
        for (int id : array) {
            if (element == id) {
                flag = true;
                break;
            }
        }
        return flag;
    }


}
