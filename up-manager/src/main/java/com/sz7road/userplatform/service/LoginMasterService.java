package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.web.masterdata.Command;
import com.sz7road.web.masterdata.NetConnector;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-9
 * Time: 上午10:37
 * 登录主数据平台的服务类
 */
@Singleton
public class LoginMasterService {

    @Inject
    private Provider<Command> commandProvider;

    private Map<String, Map<String, Object>> masterDataObject = null;

    private Command command = null;

    private Command getCommand(String userName, String password) {

//        boolean flag = (command == null || !command.getUserName().equals(userName) || !command.getPassword().equals(password));
//        if (flag) {
        command = commandProvider.get();
        command.setUserName(userName);
        command.setPassword(password);
        NetConnector.getInstance().setCommand(command);
//        }
        return command;
    }

    /**
     * 拿到用户信息并转换为map
     *
     * @return
     */
    public Map<String, Map<String, Object>> getAccountInfo(String userName, String password) {
        getCommand(userName, password);
        masterDataObject = command.getMasterDataObject();

        if (masterDataObject != null) {
        } else {
            getCommand(userName, password);
            masterDataObject = command.getMasterDataObject();
        }
        return masterDataObject;
    }


}
