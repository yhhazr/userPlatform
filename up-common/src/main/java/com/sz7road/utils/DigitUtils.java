/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangfan.zhou
 */

public class DigitUtils {

    public static int parserInt(String strInt){
        int r = 0;
        try {
            r = Integer.parseInt(strInt);
        } catch (IllegalArgumentException e) {}
        return r;
    }

    private static AtomicInteger ai = new AtomicInteger(1);
    public static String getRandomStrDigit(){
        return  System.currentTimeMillis() + "" + ai.getAndIncrement();
    }

    public static String getRandomDigit(int len){
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
