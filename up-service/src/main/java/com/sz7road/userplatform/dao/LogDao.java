package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.Log;

import java.util.List;

/**
 * @author leo.liao
 */


public interface LogDao extends Dao<Log> {

    /**
     * 拿到登录异常的日志
     *
     * @param userName 用户名
     * @return
     */
    public List<Log> getExceptionLog(String userName);


    /**
     * 增加异常登入的日志信息
     *
     * @param log
     * @return
     */
    public int addExceptionLog(Log log);

    public int addInfoLog(Log log);

    /**
     * 影藏该用户名的IP登录日志，当前时间以前的。
     * @param userName
     * @return
     */
    public int  hideExceptionLog(String userName);

}
