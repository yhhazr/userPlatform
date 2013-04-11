/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform;

import com.google.common.collect.ImmutableList;

/**
 * 一些常量定义。
 *
 * @author jeremy
 */
public final class Constant {
    /**
     * The default size for buffer allocating.
     */
    public static final int DEFAULT_BUFFER_BLOCK_SIZE = 500 * 1024; // 500 kb.

    /**
     * Current version.
     */
    public static final String VERSION = "1.0";

    public static final String UTF_8 = "UTF-8";

    public static final long TIME_7_DAYS = 604800000L;

    public static final long TIME_24_HOURS = 86400000L;

    public static final long TIME_30_MINUTES = 1800000L;

    public static final long TIME_15_MINUTES = 900000L;

    public static ImmutableList<String> MBK_CHAR_LIST=ImmutableList.of("A","B","C","D","E","F","G","H","I","J");
}
