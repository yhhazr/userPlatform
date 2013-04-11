/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

/**
 * @author jeremy
 */
public class TableLocator {

    /**
     * Constructor.
     */
    private TableLocator() {
        super();
    }

    public static String getUserTable(int userId) {
        return "dt_user_" + (userId % 128 + 1);
    }

    public static String getChargeOrderLogTable() {
//        return "log_charge_order_" + (new SimpleDateFormat("yyyyMM").format(new Date()));
        return "log_charge_order";
    }

    public static String getOrderTable() {
//        return "dt_order_" + (new SimpleDateFormat("yyyyMM").format(new Date()));
        return "dt_order";
    }

    public static String getProductOrderTable() {
//      return "dt_order_" + (new SimpleDateFormat("yyyyMM").format(new Date()));
      return "dt_product_order";
  }

}

