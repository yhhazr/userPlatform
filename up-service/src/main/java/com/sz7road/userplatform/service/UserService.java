/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.dao.*;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.utils.AntiAddictionRegisterUtil;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.utils.Helper;
import com.sz7road.utils.*;
import com.sz7road.web.pojos.AddictionParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jeremy
 */
@Singleton
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    @Inject
    private ExecutorService asyncExecutor;

    final long MAX_TIME = 604800000;

    final long RESETPWD_URL_EXPIRYTIME = 86400000;

    //    private final Pattern namedPattern = Pattern.compile("^[0-9a-zA-Z_]{4,20}");
    private final Pattern namedPattern = Pattern.compile("^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){5,19}$");
    private final Pattern namedCanRegPattern = Pattern.compile("([a-zA-Z0-9])\\1{4,}");
    private final Pattern emailPattern = Pattern.compile("^([a-z0-9A-Z]+[_|\\-|.]?)+[a-z0-9A-Z]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?)\\.[a-zA-Z]{2,}$");

    @Inject
    private Provider<UserDao> userDaoProvider;
    @Inject
    private Provider<AccountDao> accountDaoProvider;
    @Inject
    private Provider<QuestionDao> questionDaoProvider;
    @Inject
    private com.google.inject.Provider<QueryRunner> runner;
    @Inject
    private Provider<LogDao> logDaoProvider;
    @Inject
    private Provider<LoginGameService> loginGameServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeServiceProvider;

    @com.google.inject.Inject
    private com.google.inject.Provider<AntiAddictionRegisterUtil> antiAddictionRegisterUtilProvider;

    private UserDao getUserDao() {
        final UserDao userDao = userDaoProvider.get();
        if (null == userDao) {
            throw new NullPointerException("null userdao provided!");
        }
        return userDao;
    }

    private LogDao getLogDao() {
        final LogDao logDao = logDaoProvider.get();
        if (null == logDao) {
            throw new NullPointerException("null logDao provided!");
        }
        return logDao;
    }

    private AccountDao getAccountDao() {
        final AccountDao dao = accountDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null accountdao provided!");
        }
        return dao;
    }

    private QuestionDao getQuestionDao() {
        final QuestionDao dao = questionDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null questiondao provided!");
        }
        return dao;
    }

    private LoginGameService getLoginGameService() {
        final LoginGameService service = loginGameServiceProvider.get();
        if (null == service) {
            throw new NullPointerException("null LoginGameService provided!");
        }
        return service;
    }

    /**
     * 验证指定用户账号是否为有效数据。
     *
     * @param userName 用户名
     * @param passWord 密码
     * @return 验证成功返回该用户的数据对象， 否则返回Null
     */
    public UserAccount authenticated(final String userName, final String passWord) {
        if (Strings.isNullOrEmpty(userName)) {
            throw new IllegalArgumentException("Illegal userName");
        }
        if (Strings.isNullOrEmpty(passWord)) {
            throw new IllegalArgumentException("Illegal passWord");
        }

        final UserAccount account = getAccountDao().get(userName, MD5Utils.password(passWord));
        if (null != account) {
            return account;
        }
        return null;
    }

    public UserAccount authenticatedByName(final String userName) {
        if (Strings.isNullOrEmpty(userName)) {
            throw new IllegalArgumentException("Illegal userName");
        }
        final UserAccount account = getAccountDao().get(userName);
        if (null != account) {
            return account;
        }
        return null;
    }

    public UserAccount findAccountById(final int userId) {
        if (userId > 0) {
            try {
                return getAccountDao().getById(userId);
            } catch (final Exception e) {
                logger.error("获取用户账号异常：{}", e.getMessage());
            }
        }
        return null;
    }

    public UserAccount findAccountByUserName(final String userName) {
        if (Strings.isNullOrEmpty(userName)) {
            throw new IllegalArgumentException("Illegal userName");
        }

        return getAccountDao().get(userName);
    }

    public int findUserIdByUserName(final String userName) {
        if (Strings.isNullOrEmpty(userName)) {
            throw new IllegalArgumentException("Illegal userName");
        }
        final UserAccount account = getAccountDao().get(userName);
        if (account == null) {
            return 0;
        } else {
            return account.getId();
        }

    }

    public UserAccount findByEmail(final String email) {
        if (Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("Illegal email");
        }

        try {
            final List<UserAccount> list = getAccountDao().getByEmail(email);
            if (null != list && !list.isEmpty()) {
                return list.get(0);
            }
        } catch (final Exception e) {
            logger.error("通过Email查找用户账号异常：{}", e.getMessage());
        }
        return null;
    }

    public UserAccount signUp(final String userName, final String passWord, final String email) {
        UserAccount account = new UserAccount();
        account.setUserName(userName);
        account.setEmail(email);
        account.setPassWord(MD5Utils.password(passWord));
        final int id = getAccountDao().add(account);
        if (id > 0) {
            account.setId(id);
            return account;
        }
        return null;
    }

    public UserAccount signUpReWrite(final String userName, final String passWord) {
        UserAccount account = new UserAccount();
        account.setUserName(userName);
        account.setPassWord(MD5Utils.password(passWord));
        final int id = getAccountDao().addReWrite(account);
        if (id > 0) {
            account.setId(id);
            return account;
        }
        return null;
    }


    /**
     * 保存用户数据。
     *
     * @param userObject 用户数据对象
     * @return true or false
     */
    public boolean saveData(final UserObject userObject) {
        try {
            return getUserDao().add(userObject) == 1;
        } catch (final Exception e) {
            logger.error("保存用户数据异常：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 保存用户数据。
     *
     * @param userObject 用户数据对象
     * @return true or false
     */
    public Map<String, Integer> saveData_Register(final UserObject userObject) {
        Map<String, Integer> map = null;
        try {
            map = getUserDao().add_register(userObject);
        } catch (final Exception e) {
            logger.error("保存用户数据异常：{}", e.getMessage());
        }
        return map;
    }


    /**
     * 保存用户数据。
     *
     * @param userObject 用户数据对象
     * @return true or false
     */
    public Map<String, Integer> saveData_Register_fcm(UserObject userObject) {
        Map<String, Integer> map = null;
        try {
            //增加防沉迷资料
            map = getUserDao().register_fcm(userObject);
            String AddictionSwitch = ConfigurationUtils.get("addictionSwitch");
            if ("open".equals(AddictionSwitch)) {
                AddictionParam param = new AddictionParam();
                param.setCardId(userObject.getIcn());
                param.setCardName(userObject.getRealName());
                param.setUserName(userObject.getUserName());
                param.setUserid(map.get("id"));
                param.setSite("7road_0001");
                int rel = antiAddictionRegisterUtilProvider.get().addAntiAddictionItem(param);
                logger.info((rel == 0) ? "插入防沉迷信息成功!" : "插入防沉迷信息失败!");
            }
        } catch (final Exception e) {
            logger.error("保存用户数据异常：{}", e.getMessage());
        }
        return map;
    }


    /**
     * 通过用户账号获取用户的数据对象。
     *
     * @param account 用户账号
     * @return 用户数据对象
     */
    public UserObject findByAccount(final UserAccount account) {
        if (null == account) {
            throw new NullPointerException("null account");
        }
        final UserObject userObject = getUserDao().get(account.getId());

        if (null != userObject) {
            userObject.setAccount(account);
        }
        return userObject;
    }

    public void runLoginAsyncHooks(final UserObject user) {
        asyncExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getUserDao().updateLoginData(user);
                } catch (final Exception e) {
                    logger.error("记录用户登录日志事件异常：{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

//    public void runLoginAsyncHooks(final UserObject user, final String ipDataPath) {
//        asyncExecutor.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //更新登录信息
//                    getUserDao().updateLoginData(user);
//                } catch (final Exception e) {
//                    logger.error("记录用户登录日志事件异常：{}", e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


    public int updateLoginDataAndLog() {
        return 0;
    }


    public UserObject getUserObject(String userName) {
        UserAccount userAccount = findAccountByUserName(userName);
        UserObject userObject = null;
        if (userAccount != null) {
            userObject = findByAccount(userAccount);
        }
        return userObject;
    }

    /**
     * 得到用户登录异常IP日志列表
     *
     * @param userName 用户名
     * @return
     */
    public List<Log> getUserIPLogs(String userName) {
        List<Log> userIPLogs = null;
        if (!Strings.isNullOrEmpty(userName)) {
            Map<String, List<Log>> map = new HashMap<String, List<Log>>();
            userIPLogs = getLogDao().getExceptionLog(userName);
            if (userIPLogs != null && !userIPLogs.isEmpty()) {
                for (Log log : userIPLogs) {
                    String ip = log.getContent();
                    if (map.containsKey(ip)) {
                        map.get(ip).add(log);
                    } else {
                        List<Log> logs = new LinkedList<Log>();
                        logs.add(log);
                        map.put(ip, logs);
                    }
                }
            }
            if (map.size() >= 2) {//如果有两个以上的IP，去掉次数最多的，剩下的是异常的IP登录信息
                userIPLogs.removeAll(map.get(DataUtil.getMaxNumIPLogs(map)));
            } else {//如果只有一个IP,没有异常情况
                userIPLogs = null;
            }

        }
        if (userIPLogs != null && !userIPLogs.isEmpty()) {
            for (Log userLog : userIPLogs) {//隐藏掉最后两位的IP数字
                userLog.setContent(DataUtil.getHandledIP(userLog.getContent()));
            }
        }
        return userIPLogs;
    }

    /**
     * 生成邮箱验证URL
     *
     * @param userAccount 用户账号
     * @return 邮箱验证URL
     */
    public String generateEmailVerifyURL(UserAccount userAccount) {
        if (null == userAccount) throw new NullPointerException("此账户已经不存在");
        if (Strings.isNullOrEmpty(userAccount.getEmail())) {
            throw new IllegalArgumentException("此帐号没绑定任何邮箱");
        }
        StringBuilder url = new StringBuilder();
        try {
            String ui = URLEncoder.encode(DesUtils.encrypt(userAccount.getId() + ""), Constant.UTF_8);
            String un = URLEncoder.encode(DesUtils.encrypt(userAccount.getUserName()), Constant.UTF_8);
            String ue = URLEncoder.encode(DesUtils.encrypt(userAccount.getEmail()), Constant.UTF_8);
            long timestamp = System.currentTimeMillis() + MAX_TIME;
            String code = Helper.RandomCode(5);
            VerifyCode verifyCode = new VerifyCode("verify_" + timestamp + "_" + userAccount.getUserName(), code, timestamp);
            VerifyCodeProvider verifyCodeService = verifyCodeServiceProvider.get();
            verifyCodeService.add(verifyCode);
            String _c = DesUtils.encrypt(code);
            String c = URLEncoder.encode(_c, Constant.UTF_8);
            String ut = URLEncoder.encode(DesUtils.encrypt(timestamp + ""), Constant.UTF_8);
            url.append("?ui=").append(ui).append("&un=").append(un).append("&ue=").append(ue).append("&ut=").append(ut).append("&c=").append(c);
        } catch (Exception e) {
            logger.error("编码出错", e.getMessage());
        }
        return url.toString();
    }

    public int updateEmail(UserAccount account) {
        if (null == account) throw new NullPointerException("此账户不存在");
        UserAccount oldAccount = getAccountDao().get(account.getUserName());
        int ret = getAccountDao().updateEmail(account);
        if (ret > 0 && (oldAccount.getEmail() == null || oldAccount.getEmail().equals(""))) {
            UserObject userObject = getUserDao().get(account.getId());
            byte safeLevel = userObject.getSafeLevel();
            safeLevel += 1;
            getUserDao().updateSafeLevel(account.getId(), safeLevel);
        }
        if (ret > 0 && (oldAccount.getEmail() != null && !oldAccount.getEmail().equals(""))) {
            UserObject userObject = getUserDao().get(account.getId());
            byte safeLevel = userObject.getSafeLevel();
            safeLevel -= 1;
            getUserDao().updateSafeLevel(account.getId(), safeLevel);
        }
        return ret;
    }

    public int modifyPwd(String userName, String oldPwd, String newPwd) {
        UserAccount account = getAccountDao().get(userName, MD5Utils.password(oldPwd));
        if (account == null) {
            throw new IllegalArgumentException("原密码错误");
        }
        return modifyPwdCommon(account.getId(), account.getUserName(), newPwd);
    }

    public int modifyPwdCommon(int id, String userName, String newPwd) {
        return getUserDao().modifyPasswordCommon(id, userName, newPwd);
    }


    /**
     * 更新密码强度
     *
     * @param id
     * @param pswStrength
     * @return
     */
    public int updatePswStrengthLevel(int id, int pswStrength) {
        return getUserDao().updatePswStrengthLevel(id, pswStrength);
    }

    public int updateBaseInfo(UserObject userObject) {
        if (null == userObject) {
            throw new NullPointerException("null userObject");
        }
        return getUserDao().updateBaseInfo(userObject);
    }

    public String generateGetBackPasswordURL(UserAccount userAccount) {
        StringBuilder url = new StringBuilder();
        try {
            String ui = URLEncoder.encode(DesUtils.encrypt(userAccount.getId() + ""), Constant.UTF_8);
            String un = URLEncoder.encode(DesUtils.encrypt(userAccount.getUserName()), Constant.UTF_8);
            String ue = URLEncoder.encode(DesUtils.encrypt(userAccount.getEmail()), Constant.UTF_8);
            long timestamp = System.currentTimeMillis() + RESETPWD_URL_EXPIRYTIME;
            String ut = URLEncoder.encode(DesUtils.encrypt(timestamp + ""), Constant.UTF_8);
            String code = Helper.RandomCode(5);
            VerifyCode verifyCode = new VerifyCode("verify_" + timestamp + "_" + userAccount.getUserName(), code, timestamp);
            VerifyCodeProvider verifyCodeService = verifyCodeServiceProvider.get();
            verifyCodeService.add(verifyCode);
            String _c = DesUtils.encrypt(code);
            String c = URLEncoder.encode(_c, Constant.UTF_8);
            url.append("?ui=").append(ui).append("&un=").append(un).append("&ue=").append(ue).append("&ut=").append(ut).append("&_c=").append(c);
        } catch (Exception e) {
            logger.error("生成找回密码URL连接编码时出错", e.getMessage());
        }
        return url.toString();
    }

    public int resetPwd(UserAccount account, String pwd) {
        if (account == null) {
            throw new IllegalArgumentException("帐号不存在");
        }
        return modifyPwdCommon(account.getId(), account.getUserName(), pwd);
    }

    public int updateGameData(final UserObject userObject) {
        asyncExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getUserDao().updateGameData(userObject);

                    LoginGame entity = new LoginGame();
                    entity.setUserId(userObject.getId());
                    entity.setGameId(userObject.getLastGameId());
                    entity.setServerId(userObject.getLastGameZoneId());
                    entity.setLoginTime(userObject.getLastLoginTime());
                    getLoginGameService().insertOrUpdate(entity);
                } catch (final Exception e) {
                    logger.error("更新用户[{}]游戏区数据出现异常：{}", userObject.getUserName(), e.getMessage());
                }
            }
        });
        return 0;
    }

    /**
     * 添加新的密保问题
     *
     * @param questions
     * @return
     */
    public int updateQuestion(int userId, int status, Question... questions) {
        if (null == questions) throw new IllegalArgumentException("Illegal questions");
        /*getQuestionDao().updateStatus(userId, status);
        return getQuestionDao().add(questions);*/
        return getQuestionDao().addAndUpdateStatus(userId, status, questions);
    }

    public List<Question> getLastQuestions(int userId, int n) {
        try {
            return getQuestionDao().getByUserId(userId, QuestionDao.STATUS_USE, n);
        } catch (Exception e) {
            logger.error("获取用户ID[{}]密保出现异常：{}", userId, e.getMessage());
        }
        return null;
    }

    /**
     * 根据用户名拿到密保信息
     *
     * @param userName
     * @return
     */
    public List<Question> getLastQuestionsByUserName(String userName) {
        try {
            UserAccount userAccount = findAccountByUserName(userName);
            return getQuestionDao().getByUserId(userAccount.getId(), QuestionDao.STATUS_USE, 3);
        } catch (Exception e) {
            logger.error("获取用户ID[{}]密保出现异常：{}", userName);
            e.printStackTrace();
        }
        return null;
    }


    public List<Question> getLastQuestionsByUserName(int id) {
        try {
            return getQuestionDao().getByUserId(id, QuestionDao.STATUS_USE, 3);
        } catch (Exception e) {
            logger.error("获取用户ID[{}]密保出现异常：{}", id);
            e.printStackTrace();
        }
        return null;
    }

    public UserObject findByMobile(int id, String mobile) {
        if (Strings.isNullOrEmpty(mobile)) {
            throw new IllegalArgumentException("Illegal mobile");
        }

        try {
            final List<UserObject> list = getUserDao().getByMobile(id, mobile);
            if (null != list && !list.isEmpty()) {
                return list.get(0);
            }
        } catch (final Exception e) {
            logger.error("通过Mobile查找用户账号异常：{}", e.getMessage());
        }
        return null;
    }

    public int updateMobile(final UserObject userObject) throws SQLException {
        if (null == userObject) throw new NullPointerException("此账户不存在");
        return getUserDao().updateMobile(userObject);
    }

    public int updateMessageCount(final UserObject userObject) throws SQLException {
        if (null == userObject) throw new NullPointerException("此账户不存在");

        String strCount = ConfigurationUtils.get("sendMsg.Count");
        int count = Strings.isNullOrEmpty(strCount) ? 5 : Integer.parseInt(strCount);

        if (userObject.getLastMessageTime() != null) {
            long lastMessageTime = userObject.getLastMessageTime().getTime();
            long firstMomentTimeOfToday = CommonDateUtils.getOneDayFirstMoment(new Date());

            if (firstMomentTimeOfToday > lastMessageTime) {
                userObject.setMessageCount((byte) 1);
            } else {
                userObject.setMessageCount((byte) (userObject.getMessageCount() + 1));
            }
        } else {
            userObject.setMessageCount((byte) 1);
        }


        userObject.setLastMessageTime(new Timestamp(System.currentTimeMillis()));
        return getUserDao().updateMessageCount(userObject);
    }

    public boolean isLegalNaming(final String userName) {
        return !Strings.isNullOrEmpty(userName) && namedPattern.matcher(userName).matches();
    }

    public boolean isNameCanReg(final String userName) {
        return !(!Strings.isNullOrEmpty(userName) && namedCanRegPattern.matcher(userName).find());
    }

    public boolean isLegalPassword(String passWord) {
        return !Strings.isNullOrEmpty(passWord) && passWord.length() >= 6;
    }

    public boolean isLegalEmail(String email) {
        return Strings.isNullOrEmpty(email) || emailPattern.matcher(email).matches();
    }

    //更改用户头像
    public int updateUserAvatar(int id, String userName, String headDir) {
        return getUserDao().updateUserAvatar(id, userName, headDir);
    }


    public int updateUserBasicInfo(UserObject userObject) {
        //更新用户信息的时候，插入到防沉迷系统中
        final String icn = userObject.getIcn();
        if (!Strings.isNullOrEmpty(icn)) {
            final String userName = userObject.getUserName();
            final String addictionServerUrl = ConfigurationUtils.get("addictionServerUrl");
            final String addictionKey = ConfigurationUtils.get("addictionKey");
            Map<String, Object> param = Maps.newHashMap();
            param.put("userName", userName);
            param.put("gameAlias", "sq");
            param.put("cardId", icn);
            param.put("proxyName", "7road");
            final String sign = MD5Utils.digestAsHex(userName + addictionKey).toUpperCase();
            param.put("sign", sign);
            Backend.BackendResponse back = Backend.post(addictionServerUrl, param);
            if (back == null) {
                logger.info("连接防沉迷服务器失败");
            } else {
                logger.info("【" + userName + "】的防沉迷注册结果是：" + back.getResponseContent());
            }
        }
        return getUserDao().updateUserBasicInfo(userObject);
    }

    /**
     * 更新用户的详细信息
     *
     * @param userObject
     * @return
     */
    public int updateUserDetailInfo(UserObject userObject) {
        return getUserDao().updateUserDetailInfo(userObject);
    }

    /**
     * 更新用户的教育信息
     *
     * @param userObject
     * @return
     */
    public int updateUserEduInfo(UserObject userObject) {
        return getUserDao().updateUserEduInfo(userObject);
    }

    /**
     * 更新用户的工作信息
     *
     * @param userObject
     * @return
     */
    public int updateUserWorkInfo(UserObject userObject) {
        return getUserDao().updateUserWorkInfo(userObject);
    }


    /**
     * 得到整体的安全信息
     *
     * @param userObject
     * @return
     */
    public Map<String, String> getWholeSafeInfo(UserObject userObject) {
        Map<String, String> safeInfo = new HashMap<String, String>();
        int safeStrength = 0;
        List<Question> questions = getLastQuestionsByUserName(userObject.getId());
        /* 1, 安全邮箱，绑定手机，证件信息，个人资料，密保信息权重一分
       密码安全度大于3的2分，小于3的1分 */
        if (userObject != null) {
            String email = userObject.getAccount().getEmail();
            if (!Strings.isNullOrEmpty(email)) { //安全邮箱，有安全度+1
                safeStrength += 1;
                safeInfo.put("email", DataUtil.getHandledEmail(email));
            } else {
                safeInfo.put("email", "empty");
            }
            String phone = userObject.getMobile();
            if (!Strings.isNullOrEmpty(phone)) { //绑定手机，有安全度+1
                safeStrength += 1;
                safeInfo.put("phone", DataUtil.getHandledPhoneNum(phone));
            } else {
                safeInfo.put("phone", "empty");
            }

            if (!Strings.isNullOrEmpty(userObject.getIcn()) && !Strings.isNullOrEmpty(userObject.getRealName())) { //真实姓名和身份证号码，都有，安全度+1
                safeStrength += 1;
                safeInfo.put("certify", "ok");
            } else {
                safeInfo.put("certify", "empty");
            }
            if (!Strings.isNullOrEmpty(userObject.getNickName()) && !Strings.isNullOrEmpty(userObject.getLinkPhone()) && (userObject.getGender() == 0 || userObject.getGender() == 1) && !Strings.isNullOrEmpty(userObject.getBirthday().toString()) && !Strings.isNullOrEmpty(userObject.getQq()) && !Strings.isNullOrEmpty(userObject.getMsn()) && !Strings.isNullOrEmpty(userObject.getSelfIntroduction())) {//个人资料完整度
                safeStrength += 1;
                safeInfo.put("userInfo", "ok");
            } else {
                safeInfo.put("userInfo", "empty");
            }
            //密码强度
            int pswStength = userObject.getPswStrength();
            if (pswStength > 3) {
                safeStrength += 2;
            }
            if (pswStength <= 3 && pswStength > 0) {
                safeStrength += 1;
            }
            safeInfo.put("pswStrength", String.valueOf(pswStength));
        }


        //密保问题
        if (questions != null && questions.size() > 0) {
            safeStrength += 1;
            safeInfo.put("question", "ok");
        } else {
            safeInfo.put("question", "empty");
        }
        safeInfo.put("safeStrength", String.valueOf(safeStrength));
        return safeInfo;
    }

    private static String sqDest = "http://assist%s.shenquol.com/usertransfer/updatename";

    /**
     * 重新注册；其他代理商的游戏数据重新注册
     *
     * @param
     * @return strGameResult
     *         -2	IP限制
     *         -1	参数为空
     *         0	更新成功
     *         1	更新失败
     *         2	用户名已存在
     *         3	该户名不存在
     *         4	无效IP
     *         5	代理商错误
     *         6	加密不正确
     *         7	其他错误
     *         8	未知错误
     */
    public String reRegister(String userName, String userPass, String oldname, String oldsite, String sourceSite, String newsite) {
        String strGameResult = "1";
        Connection conn = null;
        try {
            conn = runner.get().getDataSource().getConnection();
            conn.setAutoCommit(false);
            UserAccount account = new UserAccount();
            account.setUserName(userName);
            account.setPassWord(MD5Utils.password(userPass));
            account.setEmail("");
            int id = getAccountDao().add(conn, account);

            account.setId(id);
            UserObject user = new UserObject();
            user.setAccount(account);
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            user.setIcn("");
            user.setRealName("");
            user.setSite(sourceSite);
            user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
            user.setPswStrength(RuleUtil.getPswStrength(userPass));
            int result = getUserDao().add(conn, user);

            String newname = "" + id;
            String loginKey = "qykdsutkds-lksdf45-wand-6sdfdsd-dfsl-sfd94745-87lovdse";
            String sign = DigestUtils.md5Hex(oldname + newname + oldsite + newsite + loginKey);

            Map<String, Object> param = Maps.newHashMap();
            param.put("oldname", oldname);
            param.put("newname", newname);
            param.put("oldsite", oldsite);
            param.put("newsite", newsite);
            param.put("sign", sign);

            String dest = "";
            Matcher matcherSite = Pattern.compile("7road_(\\d+)").matcher(newsite);
            if (matcherSite.find() && matcherSite.groupCount() == 1) {
                dest = String.format(sqDest, Integer.parseInt(matcherSite.group(1)));

                Backend.BackendResponse response1 = Backend.post(dest, param);
                strGameResult = response1.getResponseContent();
            }

            if (strGameResult != null && strGameResult.trim().equals("0")) {
                conn.commit();
            } else {
                conn.rollback();
            }
            logger.info("重新注册:username={},oldname={},oldsite={},newname={},newsite={},result={}",
                    new Object[]{userName, oldname, oldsite, newname, newsite, strGameResult});
        } catch (Exception ex) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
            }
            logger.error("转移游戏数据注册信息异常：{}", ex.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
            DbUtils.closeQuietly(conn);
        }
        return strGameResult.trim();
    }

    /**
     * 判断是不是合法的身份证号码
     *
     * @param icn
     * @return
     */
    public boolean isLegalIcn(String icn) {
        return IcnConfirmUtil.validateCard(icn);
    }

    /**
     * 更新平台用户的防沉迷信息
     *
     * @param userId   用户Id
     * @param realName 真实姓名
     * @param icn      身份证号码
     * @return
     */
    public boolean updateFcmInfo(int userId, String realName, String icn) {
        return getUserDao().updateFcmInfo(userId, realName, icn);
    }

    /**
     * 20130329 11:20
     * 通过用户id得到sites
     *
     * @return
     */
    public ImmutableList<String> getRoleInfoByUserId(int userId) {
        return getUserDao().getRoleInfoByUserId(userId);
    }
}
