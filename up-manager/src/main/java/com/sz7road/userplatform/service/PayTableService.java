package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.PayTableDao;
import com.sz7road.userplatform.dao.UserManagerDao;
import com.sz7road.userplatform.pojo.UserInfoObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.PayTable;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.pojos.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Singleton
public class PayTableService {

    @Inject
    private Provider<PayTableDao> payTableDaoProvider;

    public List<PayTable.PayEntry> list () {
        try {
            return payTableDaoProvider.get().listAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
