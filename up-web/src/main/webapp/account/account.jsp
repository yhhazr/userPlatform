<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>个人中心</title>
<link href="${imageDomainUrl}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${imageDomainUrl}/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
<link rel="shortcut icon" href="http://account.7road.com/img/favicon.ico"/>
<style type="text/css">
.info {
    height: 12px;
    line-height: 12px;
    margin-left: 5px;
    padding: 7px 0 7px 0;
    display: inline-block;
    color: #c5c6cc;
}

.infoError {
    padding-left: 25px;
    color: #ED1C24;
    background: url('/img/icons4.gif') no-repeat 0 7px;
}

.infoOk {
    background: url('/img/icons4.gif') no-repeat 0 -18px;
    padding-left: 25px;
}

* {
    margin: 0;
    padding: 0;
}

.password_strangth_level {
    width: 16px;
    height: 12px;
}

html {
    overflow-y: scroll;
}

ul, li {
    list-style: none;
}

img {
    border: 0;
}

a {
    color: #08C;
}

.blank, .blank20 {
    height: 0;
    font-size: 0;
    height: 0;
    overflow: hidden;
    clear: both;
}

.blank18 {
    height: 17px;
}

.none {
    display: none;
}

cite {
    float: left;
    font-style: normal;
}

body {
    font: normal 14px / 22px 微 软 雅 黑, 宋 体;
    background-color: #FFF;
    color: #333;
}

.head {
    height: 74px;
    width: 100%;
    background: url(${imageDomainUrl}/img/headbg_nie.png) repeat-x;
}

.navbox {
    width: 1010px;
    margin: 0 auto;
}

.userlogo {
    float: left;
    width: 203px;
}

.logo2 {
    float: left;
    background: url(${imageDomainUrl}/img/kh.jpg) no-repeat;
    width: 187px;
    height: 74px;
}

.nav {
    float: left;
}

.nav a {
    float: left;
    height: 74px;
}

.m1 {
    width: 108px;
    background: url(${imageDomainUrl}/img/menu_01.jpg) no-repeat;
}

.m2 {
    width: 134px;
    background: url(${imageDomainUrl}/img/menu_02_on.jpg) no-repeat;
}

.m3 {
    width: 130px;
    background: url(${imageDomainUrl}/img/menu_03.jpg) no-repeat;
}

.m4 {
    width: 130px;
    background: url(${imageDomainUrl}/img/menu_04.jpg) no-repeat;
}

.m5 {
    width: 108px;
    background: url(${imageDomainUrl}/img/menu_05.jpg) no-repeat;
}

.m1:hover {
    background: url(${imageDomainUrl}/img/menu_01_on.jpg) no-repeat;
}

.m2:hover {
    background: url(${imageDomainUrl}/img/menu_02_on.jpg) no-repeat;
}

.m3:hover {
    background: url(${imageDomainUrl}/img/menu_03_on.jpg) no-repeat;
}

.m4:hover {
    background: url(${imageDomainUrl}/img/menu_04_on.jpg) no-repeat;
}

.m5:hover {
    background: url(${imageDomainUrl}/img/menu_05_on.jpg) no-repeat;
}

.usernav {
    width: 998px;
    border: 1px solid #DADADA;
    height: 32px;
    margin: 0 auto;
    background: url(${imageDomainUrl}/img/usernavbg.png) repeat-x;
}

.content {
    width: 1000px;
    overflow: auto;
    zoom: 1;
    margin: 0 auto;
}

.userleft {
    float: left;
    width: 162px;
    height: 274px;
    background: url(${imageDomainUrl}/img/userleftbg.png) no-repeat;
}

.userleft ul {
    float: left;
    margin: 37px 0 0 10px;
    display: inline;
}

.userleft ul li {
    float: left;
    width: 110px;
    height: 36px;
    line-height: 36px;
    color: #08C;
    padding-left: 42px;
    margin-top: 5px;
    cursor: pointer;
}

.userleft ul li.a {
    background: url(${imageDomainUrl}/img/t1.png) no-repeat 10px -36px;
}

.userleft ul li.b {
    background: url(${imageDomainUrl}/img/t2.png) no-repeat 10px -36px;
}

.userleft ul li.c {
    background: url(${imageDomainUrl}/img/t3.png) no-repeat 10px -36px;
}

.userleft ul li.d {
    background: url(${imageDomainUrl}/img/t4.png) no-repeat 10px -36px;
}

.userleft ul li.e {
    background: url(${imageDomainUrl}/img/t5.png) no-repeat 10px -36px;
}

.userleft ul li.select {
    background-color: #fff;
    color: #333;
    border: 1px solid #ddd;
    border-right: 0;
    width: 109px;
}

.userright {
    float: left;
    width: 838px;
}

.tabbox h2 {
    height: 43px;
    background: url(${imageDomainUrl}/img/usernavbg2.png) repeat-x;
    font: normal 14px / 42px 微 软 雅 黑, 宋 体;
    padding-left: 20px;
}

.tabbox h2 span {
    color: #08C;
}

.userdata {
    float: left;
    margin: 18px 0 0 18px;
    display: inline;
}

.userdata img {
    float: left;
}

.userdatadiv {
    float: left;
    margin-left: 65px;
}

.zhanghao {
    margin: 10px 0 0 18px;
}

.zhanghao p {
    line-height: 26px;
}

.zhanghao p.a {
    border-bottom: 1px solid #ddd;
}

.zhanghao span {
    display: inline-block;
    overflow: hidden;
    background-color: orange;
    width: 100px;
    height: 10px;
    float: left;
    margin: 10px 0 0 5px;
}

.zhanghao span.org {
    background-color: orange;
}

.zhanghao span.sli {
    background-color: silver;
}
</style>
</head>

<body>
<div class="head">
    <div class="navbox">
        <a href="http://sq.7road.com" class="userlogo"><img src="${imageDomainUrl}/img/logo.jpg"/></a>

        <div class="logo2"></div>
        <div class="nav">
            <a href="http://sq.7road.com/" class="m1"></a>
            <a href="${imageDomainUrl}" class="m2"></a>
            <a href="http://pay.7road.com/" class="m3"></a>
            <a href="${imageDomainUrl}/service/" class="m4"></a>
            <a href="http://bbs.7road.com/" class="m5"></a>
        </div>
    </div>
</div>
<div class="blank18"></div>

<div class="usernav">
    <img src="img/gerenbg.png"/>
</div>
<div class="content">
<div class="userleft">
    <ul>
        <li class="a select" style="background-position:10px 0">我的资料</li>
        <li class="b">修改资料</li>
        <li class="c">邮箱绑定</li>
        <li class="d">修改密码</li>
        <li class="e">密保设置</li>
        <%--
        --%>
    </ul>
</div>
<div class="userright">
<div class="tabbox zoom">
    <h2><span>个人中心</span>&nbsp;/&nbsp;我的资料</h2>

    <div class="userdata">
        <img id="userHeadImg" src="${userObject.headDir}">

        <div class="userdatadiv">
            <p>${userObject.userName},欢迎你！</p>

            <p>本次登陆时间:${userObject.lastLoginTime}</p>

            <p>新游戏:《${gameEntry.gameName}》${serverEntry.serverName} <a href="javascript:void(0)"
                                                                       onclick="javascript:enterGame1('/PlayGame2?game=S&subGame=0&g=${gameEntry.id}&z=${serverEntry.id}');">进入游戏</a>
                << </p>
            <br>
            <c:if test="${fn:length(userIPLogs)>=1}">
                <table>
                    <tr style="width: 100%">
                        <th>IP</th>
                        <th>地址</th>
                        <th>时间</th>
                    </tr>
                    <c:forEach var="IPlog" items="${userIPLogs}" begin="0" end="5" step="1">
                        <tr>
                            <td><c:out value="${IPlog.content}"></c:out></td>
                            <td><c:out value="${IPlog.ext1}"></c:out></td>
                            <td><c:out value="${IPlog.log_time}"></c:out></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <c:if test="${fn:length(userIPLogs)<1}">
                <c:out value="没有登录异常!"></c:out>
            </c:if>
        </div>


    </div>
    <div class="blank"></div>
    <div class="zhanghao">
        <p class="a">账号安全:</p>

        <p class="b">
            <cite>当前安全级别:</cite>
            <c:forEach begin="1" end="${userObject.safeLevel}"><span class="org"></span></c:forEach>
            <c:forEach begin="1" end="${4-userObject.safeLevel}"><span class="sli"></span></c:forEach>
            <cite>&nbsp;&nbsp;${safeLevel}</cite>
        </p>

        <div class="blank"></div>
        <p>为保障您的帐户安全，请尽快完善保密 <a href="javascript:void(0)" class="zhengqiang">我要增强保护</a></p>
    </div>
</div>
<div class="tabbox zoom none">
    <h2><span>个人中心</span>&nbsp;/&nbsp;修改资料</h2>

    <div style="height:10px" class="blank"></div>
    <form class="form-horizontal">
        <fieldset>
            <div class="control-group">
                <label class="control-label" for="prependedInput">昵称</label>

                <div class="controls">
                    <p class="help-block" id="userName">${userObject.userName}</p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="prependedInput">性别</label>

                <div class="controls">
                    <div class="btn-group" data-toggle="buttons-radio">
                        <button class="btn" value="0" onclick="javascript:return false;">男</button>
                        <button class="btn" value="1" onclick="javascript:return false;">女</button>
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="prependedInput">生日</label>

                <div class="controls">
                    <select class="span1" id="year" style="width: 65px"></select>年
                    <select class="span1" id="month" onchange="monthChange()"></select>月
                    <select class="span1" id="day"></select>日
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="prependedInput">所在城市</label>

                <div class="controls">
                    <select class="span1" id="province"></select>
                    <select class="span1" id="city"></select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="eduLevel">学历</label>

                <div class="controls">
                    <select name="eduLevel" id="eduLevel" style="width: 80px">
                        <option value="empty" selected="selected">请选择</option>
                        <option value="本科">本科</option>
                        <option value="大专">大专</option>
                        <option value="中专">中专</option>
                        <option value="中技">中技</option>
                        <option value="博士">博士</option>
                        <option value="硕士">硕士</option>
                        <option value="高中">高中</option>
                        <option value="初中">初中</option>
                        <option value="其他">其他</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="prependedInput">职业</label>

                <div class="controls">
                    <input type="text" class="input-xlarge" id="career"
                           style="line-height:25px;height:25px;padding:2px 4px;"
                           value="${userObject.career}"/>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary" onclick="return modifyAccountInfo();">保
                    存
                </button>
            </div>
        </fieldset>
    </form>
</div>
<div class="tabbox zoom none">
    <h2><span>个人中心</span>&nbsp;/&nbsp;邮箱绑定</h2>

    <div style=" margin:15px 0 0 15px;" class="zoom">
        <form class="well" style="display:none;" id="emailVerifySendForm">
            <label>邮 箱：</label>

            <div class="input-prepend">
                    <span class="add-on" style="line-height:28px;height:28px;padding:0 4px;"><i
                            class="icon-envelope"></i></span><input type="text" class="span3"
                                                                    placeholder="your email address."
                                                                    style="line-height:28px;height:28px;padding:0 4px;"
                                                                    id="email"/>
            </div>
            <p>
                <button type="submit" class="btn btn-primary" id="emailBindBtn"
                        onclick="return bindEmail('${userObject.userName}');">发送验证邮件
                </button>
            </p>
        </form>
        <form class="well" id="emailModifyForm" style="display:none;">
            <label>邮 箱：</label>

            <div class="input-prepend">
                    <span class="add-on" style="line-height:28px;height:28px;padding:0 4px;"><i
                            class="icon-envelope"></i></span><input type="text" class="span3"
                                                                    placeholder="your email address."
                                                                    style="line-height:28px;height:28px;padding:0 4px;"
                                                                    disabled="true"
                                                                    value="${userObject.account.email}"/>
                <button type="submit" class="btn btn-primary" id="emailUnbindBtn"
                        onclick="return unBindEmail('${userObject.userName}','${userObject.account.email}');">解除绑定
                </button>
            </div>
        </form>
    </div>
</div>
<div class="tabbox zoom none">
    <h2><span>个人中心</span>&nbsp;/&nbsp;修改密码</h2>

    <div style=" margin:15px 0 0 15px;" class="zoom">
        <form class="form-horizontal">
            <fieldset>

                <div class="control-group">
                    <label class="control-label" for="old_pwd">原密码:</label>

                    <div class="controls">
                        <input type="password" class="input-xlarge" id="old_pwd"
                               style="line-height:25px;height:25px;"> &nbsp;&nbsp;&nbsp;&nbsp;<span
                            id="old_password_info" class="info"
                            style="display: none; ">请输入正在使用的密码</span>
                    </div>
                </div>
                <div class="control-group" style="position:relative;">
                    <label class="control-label" for="new_pwd">新密码:</label>

                    <div style="position:absolute;left:370px;top:7px;">
                        <span id="pswLevel1" style="background-color: silver; "> &nbsp;&nbsp;</span>
                        <span id="pswLevel2" style="background-color: silver; "> &nbsp;&nbsp;</span>
                        <span id="pswLevel3" style="background-color: silver; "> &nbsp;&nbsp;</span>
                    </div>
                    <div class="controls">
                        <input type="password" class="input-xlarge" maxlength="32" id="new_pwd"
                               style="line-height:25px;height:25px;">&nbsp;&nbsp;&nbsp;&nbsp;
                        <span id="password_info" class="info" style="display: none;"></span></div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="confirm_pwd">确认新密码:</label>

                    <div class="controls">
                        <input type="password" class="input-xlarge" id="confirm_pwd" size="32"
                               style="line-height:25px;height:25px;">&nbsp;&nbsp;&nbsp;&nbsp;
                        <span
                                id="confirm_password_info" class="info" style="display: none; ">两次密码不一致</span></div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary"
                            onclick="return modifyPassword('${userObject.userName}');">确定
                    </button>

                </div>
            </fieldset>
        </form>
    </div>
</div>
<div class="tabbox zoom none">
<h2><span>个人中心</span>&nbsp;/&nbsp;密保设置</h2>

<div style=" margin:15px 0 0 15px;" class="zoom">
密保问题:<c:if test="${!questionStatus}"><a href="javascript:;"
                                        onclick="javascript:showQuestionForm('#questionStep1')">设置密保</a></c:if>
<c:if test="${questionStatus}"><a href="javascript:;"
                                  onclick="javascript:findQuestion('${userObject.userName}');">重新设置</a></c:if>
<br>
密保手机:<c:if test="${empty userObject.mobile || userObject.mobile == ''}"><a href="javascript:;"
                                                                           onclick="javascript:bindMobile();">立即绑定</a></c:if>
<c:if test="${userObject.mobile != null && userObject.mobile != ''}"><a href="javascript:;"
                                                                        onclick="javascript:unBindMobile();">取消绑定</a></c:if>

<form class="form-horizontal" id="questionStep1" style="display: none;">
    <fieldset>
        <div class="control-group">
            <label class="control-label">密码问题一</label>

            <div class="controls">
                <select id="q1" style="width:280px;">
                    <option value="-1">请选择密保问题</option>
                    <option value="您母亲的姓名是？">您母亲的姓名是？</option>
                    <option value="您配偶的生日是？">您配偶的生日是？</option>
                    <option value="您的学号（或工号）是？">您的学号（或工号）是？</option>
                    <option value="您母亲的生日是？">您母亲的生日是？</option>
                    <option value="您高中班主任的名字是？">您高中班主任的名字是？</option>
                    <option value="您父亲的姓名是？">您父亲的姓名是？</option>
                    <option value="您小学班主任的名字是？">您小学班主任的名字是？</option>
                    <option value="您父亲的生日是？">您父亲的生日是？</option>
                    <option value="您配偶的姓名是？">您配偶的姓名是？</option>
                    <option value="您初中班主任的名字是？">您初中班主任的名字是？</option>
                    <option value="您最熟悉的童年好友名字是？">您最熟悉的童年好友名字是？</option>
                    <option value="您最熟悉的学校宿舍室友名字是？">您最熟悉的学校宿舍室友名字是？</option>
                    <option value="对您影响最大的人名字是？">对您影响最大的人名字是？</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="a1"
                       style="line-height:25px;height:25px;padding:2px 4px;" maxlength="30"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">密码问题二</label>

            <div class="controls">
                <select id="q2" style="width:280px;">
                    <option value="-1">请选择密保问题</option>
                    <option value="您母亲的姓名是？">您母亲的姓名是？</option>
                    <option value="您配偶的生日是？">您配偶的生日是？</option>
                    <option value="您的学号（或工号）是？">您的学号（或工号）是？</option>
                    <option value="您母亲的生日是？">您母亲的生日是？</option>
                    <option value="您高中班主任的名字是？">您高中班主任的名字是？</option>
                    <option value="您父亲的姓名是？">您父亲的姓名是？</option>
                    <option value="您小学班主任的名字是？">您小学班主任的名字是？</option>
                    <option value="您父亲的生日是？">您父亲的生日是？</option>
                    <option value="您配偶的姓名是？">您配偶的姓名是？</option>
                    <option value="您初中班主任的名字是？">您初中班主任的名字是？</option>
                    <option value="您最熟悉的童年好友名字是？">您最熟悉的童年好友名字是？</option>
                    <option value="您最熟悉的学校宿舍室友名字是？">您最熟悉的学校宿舍室友名字是？</option>
                    <option value="对您影响最大的人名字是？">对您影响最大的人名字是？</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="a2"
                       style="line-height:25px;height:25px;padding:2px 4px;" maxlength="30"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">密码问题三</label>

            <div class="controls">
                <select id="q3" style="width:280px;">
                    <option value="-1">请选择密保问题</option>
                    <option value="您母亲的姓名是？">您母亲的姓名是？</option>
                    <option value="您配偶的生日是？">您配偶的生日是？</option>
                    <option value="您的学号（或工号）是？">您的学号（或工号）是？</option>
                    <option value="您母亲的生日是？">您母亲的生日是？</option>
                    <option value="您高中班主任的名字是？">您高中班主任的名字是？</option>
                    <option value="您父亲的姓名是？">您父亲的姓名是？</option>
                    <option value="您小学班主任的名字是？">您小学班主任的名字是？</option>
                    <option value="您父亲的生日是？">您父亲的生日是？</option>
                    <option value="您配偶的姓名是？">您配偶的姓名是？</option>
                    <option value="您初中班主任的名字是？">您初中班主任的名字是？</option>
                    <option value="您最熟悉的童年好友名字是？">您最熟悉的童年好友名字是？</option>
                    <option value="您最熟悉的学校宿舍室友名字是？">您最熟悉的学校宿舍室友名字是？</option>
                    <option value="对您影响最大的人名字是？">对您影响最大的人名字是？</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="a3"
                       style="line-height:25px;height:25px;padding:2px 4px;" maxlength="30"/>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary" onclick="return questionNext('${userObject.userName}');">下一步
            </button>
        </div>
    </fieldset>
</form>

<form class="form-horizontal" id="questionStep2" style="display: none;">
    <fieldset>
        <div class="control-group">
            <label class="control-label">密码问题一</label>

            <div class="controls">
                <span id="span_a1"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="ca1" style="line-height:25px;height:25px;padding:2px 4px;"
                       maxlength="30"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">密码问题二</label>

            <div class="controls">
                <span id="span_a2"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="ca2" style="line-height:25px;height:25px;padding:2px 4px;"
                       maxlength="30"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">密码问题三</label>

            <div class="controls">
                <span id="span_a3"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="ca3" style="line-height:25px;height:25px;padding:2px 4px;"
                       maxlength="30"/>
            </div>
        </div>

        <div class="form-actions">
            <input type="hidden" id="a11">
            <input type="hidden" id="a12">
            <input type="hidden" id="a13">
            <button type="submit" class="btn btn-primary" onclick="return updateQuestion('${userObject.userName}');">
                下一步
            </button>
        </div>

    </fieldset>
</form>

<form class="form-horizontal" id="questionR1" style="display: none;">
    <fieldset>
        <div class="control-group">
            <label class="control-label">密码问题一</label>

            <div class="controls">
                <span id="span_q1"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="ra1" style="line-height:25px;height:25px;padding:2px 4px;"
                       maxlength="30"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">密码问题二</label>

            <div class="controls">
                <span id="span_q2"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="ra2" style="line-height:25px;height:25px;padding:2px 4px;"
                       maxlength="30"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">密码问题三</label>

            <div class="controls">
                <span id="span_q3"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">答案</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="ra3" style="line-height:25px;height:25px;padding:2px 4px;"
                       maxlength="30"/>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary" onclick="return checkFindQuestion('${userObject.userName}');">
                确定
            </button>
        </div>

    </fieldset>
</form>

<form class="form-horizontal" id="questionMobile" style="display: none;">
    <fieldset>
        <div class="control-group">
            <label class="control-label">手机号</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="mobile" value="${userObject.mobile}"
                       style="line-height:25px;height:25px;padding:2px 4px;" maxlength="11"/>
                <a href="javascript:;" onclick="javascript:sendMobileCode('${userObject.userName}')">发送验证码</a>
                <span id="mobileCodeMsg" style="display: none;"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">验证码</label>

            <div class="controls">
                <input type="text" class="input-xlarge" id="m_code"
                       style="line-height:25px;height:25px;padding:2px 4px;" maxlength="6"/>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary" onclick="return mobileCodeSubmit('${userObject.userName}');"
                    disabled="true">确定
            </button>
        </div>

    </fieldset>
</form>

</div>
</div>
</div>
</div>

<div class="blank18"></div>
<hr/>
<jsp:include page="/common/bootom.jsp" flush="true"/>
<script type="text/javascript" src="${staticDomainUrl}/scripts/accountcenter.js"></script>

</body>

<script type="text/javascript">
$(function () {
    $(".userleft li").click(function () {
        var index = $(".userleft li").index(this);
        $(".tabbox").eq(index).show().siblings(".tabbox").hide();
        $(this).css("background-position", "10px 0").siblings("li").removeAttr("style");
        $(this).addClass("select").siblings("li").removeClass("select")
    });
    $(".zhengqiang").click(function () {
        $(".userleft li").eq(2).click();
    });
    $(".btn-group .btn").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
    })
    $("#old_pwd").focus(function () {
        $("#old_password_info").empty().append('请输入原密码(非空格)').removeClass("infoOk").removeClass("infoError").addClass("info").show();
    }).blur(function () {
                if ($("#old_pwd").val().trim().length == 0) {
                    $("#old_password_info").empty().append('请输入原密码（非空格）').removeClass("infoOk").addClass("infoError").show();
                }
                else {
                    $("#old_password_info").empty().append('已经输入原密码').removeClass("infoError").addClass("infoOk").show();
                }
            });

    $("#new_pwd").focus(function () {
        $("#password_info").empty().append('至少6位字符(字母,数字,符号)').removeClass("infoOk").removeClass("infoError").addClass("info").show();
        pswLenStrengh(3, 'silver');
    }).blur(function () {
                if ($("#new_pwd").val().length < 6) {
                    $("#password_info").empty().append('请输入新密码,至少6位字符(字母,数字,符号)').removeClass("infoOk").addClass("infoError").show();
                }
                else {
                    $("#password_info").empty().append('已经输入新密码').removeClass("infoError").addClass("infoOk").show();

                    var $psw = $("#new_pwd").val();
                    pswLenStrengh(checkStrong($psw), 'green');
                }
            });

    $("#confirm_pwd").focus(function () {
        $("#confirm_password_info").removeClass("infoOk").removeClass("infoError").addClass("info").show();
    }).blur(function () {
                var $confirm_pwd = $("#confirm_pwd").val().trim().length;
                if ($confirm_pwd == 0) {
                    $("#confirm_password_info").empty().append('请输入确认密码(不含空格)').removeClass("infoOk").addClass("infoError").show();
                }
                else {
                    if ($("#confirm_pwd").val().trim() != $("#new_pwd").val().trim()) {
                        $("#confirm_password_info").empty().append('两次输入密码不一致').addClass("infoError").show();
                    }
                    else {
                        $("#confirm_password_info").empty().append('两次输入密码一致').removeClass("infoError").addClass("infoOk").show();
                    }
                }
            });


})

function pswLenStrengh(level, color) {
    for (var i = 1; i <= parseInt(level); i++) {
        if (level < 3)
            $("#pswLevel" + i).css("background-color", "red");
        else {
            $("#pswLevel" + i).css("background-color", color);
        }
    }

}


var provinceArr = [];
provinceArr[0] = ['北京市'];
provinceArr[1] = ['天津市'];
provinceArr[2] = ['上海市'];
provinceArr[3] = ['重庆市'];
provinceArr[4] = ['河北省'];
provinceArr[5] = ['河南省'];
provinceArr[6] = ['云南省'];
provinceArr[7] = ['辽宁省'];
provinceArr[8] = ['黑龙江省'];
provinceArr[9] = ['湖南省'];
provinceArr[10] = ['安徽省'];
provinceArr[11] = ['山东省'];
provinceArr[12] = ['新疆维吾尔自治区'];
provinceArr[13] = ['江苏省'];
provinceArr[14] = ['浙江省'];
provinceArr[15] = ['江西省'];
provinceArr[16] = ['湖北省'];
provinceArr[17] = ['广西壮族'];
provinceArr[18] = ['甘肃省'];
provinceArr[19] = ['山西省'];
provinceArr[20] = ['内蒙古自治区'];
provinceArr[21] = ['陕西省'];
provinceArr[22] = ['吉林省'];
provinceArr[23] = ['福建省'];
provinceArr[24] = ['贵州省'];
provinceArr[25] = ['广东省'];
provinceArr[26] = ['青海省'];
provinceArr[27] = ['西藏'];
provinceArr[28] = ['四川省'];
provinceArr[29] = ['宁夏回族'];
provinceArr[30] = ['海南省'];
provinceArr[31] = ['台湾省'];
provinceArr[32] = ['香港特别行政区'];
provinceArr[33] = ['澳门特别行政区'];
var cityArr = [];
cityArr[0] = ['北京市', '东城区', '西城区', '崇文区', '宣武区', '朝阳区', '丰台区', '石景山区', '海淀区', '门头沟区', '房山区', '通州区', '顺义区', '昌平区', '大兴 区', '怀柔区', '平谷区', '密云县', '延庆县'];
cityArr[1] = ['天津市', '和平区', '河东区', '河西区', '南开区', '河北区', '红桥区', '塘沽区', '汉沽区', '大港区', '东丽区', '西青区', '津南区', '北辰区', '武清区', '宝坻区', '宁河县', '静海县', '蓟县'];
cityArr[2] = ['上海市', '黄浦区', '卢湾区', '徐汇区', '长宁区', '静安区', '普陀区', '闸北区', '虹口区', '杨浦区', '闵行区', '宝山区', '嘉定区', '浦东新区', '金山区', '松江区', '青浦区', '南汇区', '奉贤区', '崇明县'];
cityArr[3] = ['重庆市', '万州区', '涪陵区', '渝中区', '大渡口区', '江北区', '沙坪坝区', '九龙坡区', '南岸区', '北碚区', '万盛区', '双桥区', '渝北区', '巴南区', '黔江区', '长寿区', '江津区', '合川区', '永川区', '南川区', '綦江县', '潼南县', '铜梁县', '大足县', '荣昌县', '璧山县', '梁平县', '城口县', '丰都县', '垫江县', '武隆县', '忠县', '开县', '云阳县', '奉节县', '巫山县', '巫溪县', '石柱土家族自治县', '秀山土家族苗族自治县', '酉阳土家族苗族自治县', '彭水苗族土家族自治县'];
cityArr[4] = ['河北省', '石家庄市', '唐山市', '秦皇岛市', '邯郸市', '邢台市', '保定市', '张家口市', '承德市', '沧州市', '廊坊市', '衡水市'];
cityArr[5] = ['河南省', '郑州市', '开封市', '洛阳市', '平顶山市', '安阳市', '鹤壁市', '新乡市', '焦作市', '济源市', '濮阳市', '许昌市', '漯河市', '三门峡市', '南阳市', '商丘市', '信阳市', '周口市', '驻马店市'];
cityArr[6] = ['云南省', '昆明市', ' 曲靖市', '玉溪市', '保山市', '昭通市', '丽江市', '思茅市', '临沧市', '楚雄彝族自治州', '红河哈尼族彝族自治州', '文山壮族苗族自治州', '西双版纳傣族自治州', '大理白族自治州', '德宏傣族景颇族自治州', '怒江傈僳族自治州', '迪庆藏族自治州'];
cityArr[7] = ['辽宁省', '沈阳市' , '大连市' , '鞍山市' , '抚顺市' , '本溪市' , '丹东市' , '锦州市' , '营口市' , '阜新市' , '辽阳市' , '盘锦市' , '铁岭市' , '朝阳市' , '葫芦岛市'];
cityArr[8] = ['黑龙江省', '哈尔滨市', '齐齐哈尔市', '鸡西市', '鹤岗市', '双鸭山市', '大庆市', '伊春市', '佳木斯市', '七台河市', '牡丹江市', '黑河市', '绥化市', '大兴安岭地区'];
cityArr[9] = ['湖南省', '长沙市', '株洲市', '湘潭市', '衡阳市', '邵阳市', '岳阳市', '常德市', '张家界市', '益阳市', '郴州市', '永州市', '怀化市', '娄底市', '湘西土家族苗族自治州'];
cityArr[10] = ['安徽省', '合肥市', '芜湖市', '蚌埠市', '淮南市', '马鞍山市', '淮北市', '铜陵市', '安庆市', '黄山市', '滁州市', '阜阳市', '宿州市', '巢湖市', '六安市', '亳州市', '池州', '宣城市'];
cityArr[11] = ['山东省', '济南市', '青岛市', '淄博市', '枣庄市', '东营市', '烟台市', '潍坊市', '济宁市', '泰安市', '威海市', '日照市', '莱芜市', '临沂市', '德州市', '聊城市', '滨州市', '菏泽市'];
cityArr[12] = ['新疆维吾尔自治区', '乌鲁木齐市', '克拉玛依市', '吐鲁番地区', '哈密地区', '昌吉回族自治州 ', '博尔塔拉蒙古自治州 ', '巴音郭楞蒙古自治州 ', '阿克苏地区', '克孜勒苏柯尔克孜自治州 ', '喀什地区', '和田地区', '伊犁哈萨克自治州', '塔城地区', '阿勒泰地区', '石河子市', '阿拉尔市', '图木舒克市', '五家渠市' ];
cityArr[13] = ['江苏省', '南京市', '无锡市', '徐州市', '常州市', '苏州市', '南通市', '连云港市', '淮安市', '盐城市', '扬州市', '镇江市', '泰州市', '宿迁市' ];
cityArr[14] = ['浙江省', '杭州市', '宁波市', '温州市', '嘉兴市', '湖州市', '绍兴市', '金华市', '衢州市', '舟山市', '台州市', '丽水市'];
cityArr[15] = ['江西省', '南昌市', '景德镇市', '萍乡市', '九江市', '新余市', '鹰潭市', '赣州市', '吉安市', '宜春市', '抚州市', '上饶市'];
cityArr[16] = ['湖北省', '武汉市', '黄石市', '十堰市', '宜昌市', '襄樊市', '鄂州市', '荆门市', '孝感市', '荆州市', '黄冈市', '咸宁市', '随州市', '恩施土家族苗族自治州', '仙桃市', '潜江市', '天门市', '神农架林区'];
cityArr[17] = ['广西壮族', '南宁市', '柳州市', '桂林市', '梧州市', '北海市', '防城港市', '钦州市', '贵港市', '玉林市', '百色市', '贺州市', '河池市', '来宾市', '崇左市'];
cityArr[18] = ['甘肃省', '兰州市', '嘉峪关市', '金昌市', '白银市', '天水市', '武威市', '张掖市', '平凉市', '酒泉市', '庆阳市', '定西市', '陇南市', '临夏回族自治州', '甘南藏族自治州'];
cityArr[19] = ['山西省', '太原市', '大同市', '阳泉市', '长治市', '晋城市', '朔州市', '晋中市', '运城市', '忻州市', '临汾市', '吕梁市' ];
cityArr[20] = ['内蒙古自治区', '呼和浩特市', '包头市', '乌海市', '赤峰市', '通辽市', '鄂尔多斯市', '呼伦贝尔市', '巴彦淖尔市', '乌兰察布市', '兴安盟', '锡林郭勒盟', '阿拉善盟' ];
cityArr[21] = ['陕西省', '西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '延安市', '汉中市', '榆林市', '安康市', '商洛市' ];
cityArr[22] = ['吉林省', '长春市', '吉林市', '四平市', '辽源市', '通化市', '白山市', '松原市', '白城市', '延边朝鲜族自治州'];
cityArr[23] = ['福建省', '福州市', '厦门市', '莆田市', '三明市', '泉州市', '漳州市', '南平市', '龙岩市', '宁德市' ];
cityArr[24] = ['贵州省', '贵阳市', '六盘水市', '遵义市', '安顺市', '铜仁地区', '黔西南布依族苗族自治州', '毕节地区', '黔东南苗族侗族自治州', '黔南布依族苗族自治州'];
cityArr[25] = ['广东省', '广州市', '韶关市', '深圳市', '珠海市', '汕头市', '佛山市', '江门市', '湛江市', '茂名市', '肇庆市', '惠州市', '梅州市', '汕尾市', '河源市', '阳江市', '清远市', '东莞市', '中山市', '潮州市', '揭阳市', '云浮市'];
cityArr[26] = ['青海省', '西宁市' , '海东地区', '海北藏族自治州', '黄南藏族自治州', '海南藏族自治州', '果洛藏族自治州', '玉树藏族自治州', '海西蒙古族藏族自治州'];
cityArr[27] = ['西藏', '拉萨市', '昌都地区', '山南地区', '日喀则地市', '那曲地区', '阿里地区', '林芝地区' ];
cityArr[28] = ['四川省', '成都市', '自贡市', '攀枝花市', '泸州市', '德阳市', '绵阳市', '广元市', '遂宁市', '内江市', '乐山市', '南充市', '眉山市', '宜宾市', '广安市', '达州市', '雅安市', '巴中市', '资阳市', '阿坝藏族羌族自治州', '甘孜藏族自治州', '凉山彝族自治州'];
cityArr[29] = ['宁夏回族', '银川市', '石嘴山市', '吴忠市', '固原市', '中卫市'];
cityArr[30] = ['海南省', '海口市', '三亚市', '五指山市', '琼海市', '儋州市', '文昌市', '万宁市', '东方市', '定安县', '屯昌县', '澄迈县', '临高县', '白沙黎族自治县', '昌江黎族自治县', '乐东黎族自治县', '陵水黎族自治县', '保亭黎族苗族自治县', '琼中黎族苗族自治县' ];
cityArr[31] = ['台湾省', '台北市', '高雄市', '基隆市', '台中市', '台南市', '新竹市', '嘉义市'];
cityArr[32] = ['香港特别行政区', '中西区', '湾仔区', '东区', '南区', '油尖旺区', '深水埗区', '九龙城区', '黄大仙区', '观塘区', '荃湾区', '葵青区', '沙田区', '西贡区', '大埔区', '北区', '元朗区', '屯门区', '离岛区' ];
cityArr[33] = ['澳门特别行政区', '澳门'];
var ccCity = null;
$('document').ready(function () {
    var gender = '${userObject.gender}';
    if (gender == 0) {
        $("div button[value='0']").attr('class', 'btn active');
    } else {
        $("div button[value='1']").attr('class', 'btn active');
    }
    var email = '${userObject.account.email}';
    if (email != "") {
        $("#emailModifyForm").show();
    } else {
        $("#emailVerifySendForm").show();
    }
    var year = 0, month = 0, day = 0;
    var birthday = '${userObject.birthday}';
    var START_YEAR = 1900;
    if (document.all) {
        START_YEAR = 0;
    }
    var date;
    if (birthday != null && birthday != "") {
        if (document.all) {
            var tearDate = birthday.split("-");
            date = new Date(tearDate[0], tearDate[1] - 1, tearDate[2]);
        } else {
            date = new Date(birthday);
        }
        year = START_YEAR + date.getYear();
        month = date.getMonth() + 1;
        day = date.getDate();
    }
    var cCity = '${userObject.city}';
    var ccP = null;
    if (cCity != null) {
        var c = cCity.split(",");
        ccP = c[0];
        ccCity = c[1];
    }
    var currDate = new Date();
    var cYear = START_YEAR + currDate.getYear();
    var yearSelect = $("#year");
    for (var i = 0; i < 100; i++) {
        var ccYear = cYear - i;
        var str = "";
        if (ccYear == year) {
            str = "<option selected=\"selected\">" + ccYear + "</option>";
        } else {
            str = "<option>" + ccYear + "</option>";
        }
        yearSelect.append(str);
    }
    var monthSelect = $("#month");
    for (var i = 1; i < 13; i++) {
        if (month == i) {
            monthSelect.append("<option selected = \"selected\">" + i + "</option>");
        } else {
            monthSelect.append("<option>" + i + "</option>");
        }
    }
    if (day == 0)day = 1;
    //初始化日期
    var nDate = new Date(year, month);
    var days = (new Date(nDate.getTime() - 1000 * 60 * 60 * 24)).getDate();
    var daySelect = $("#day");
    for (var i = 1; i <= days; i++) {
        if (day == i) {
            daySelect.append("<option selected=\"selected\">" + i + "</option>");
        } else {
            daySelect.append("<option>" + i + "</option>");
        }
    }
    var provinceSelect = $("#province");
    if (ccP == null) {
        ccP = provinceArr[0];
    }
    for (var i = 0; i < provinceArr.length; i++) {
        if (ccP != null && ccP == provinceArr[i]) {
            provinceSelect.append("<option selected=\"selected\">" + provinceArr[i] + "</option>");
        } else {
            provinceSelect.append("<option>" + provinceArr[i] + "</option>");
        }
    }
    if (ccCity == null) {
        ccCity = cityArr[0];
    }
    var citySelect = $("#city");
    citySelect.empty();
    var currProvince = provinceSelect.val();
    var count = cityArr.length;
    for (var i = 0; i < count; i++) {
        if (currProvince == provinceArr[i][0]) {
            for (var j = 1; j < cityArr[i].length; j++) {
                var city = cityArr[i][j];
                if (ccCity != null && ccCity == city) {
                    citySelect.append("<option selected=\"selected\">" + city + "</option>");
                } else {
                    citySelect.append("<option>" + city + "</option>");
                }
            }
        }
    }
    provinceSelect.change(function () {
        var citySelect = $("#city");
        citySelect.empty();
        var currProvince = provinceSelect.val();
        var count = cityArr.length;
        for (var i = 0; i < count; i++) {
            if (currProvince == provinceArr[i][0]) {
                for (var j = 1; j < cityArr[i].length; j++) {
                    var city = cityArr[i][j];
                    citySelect.append("<option>" + city + "</option>");
                }
            }
        }
    });
});
function monthChange() {
    var year = $("#year").val();
    var month = $("#month").val();
    var nDate = new Date(year, month);
    var days = (new Date(nDate.getTime() - 1000 * 60 * 60 * 24)).getDate();
    var daySelect = $("#day");
    daySelect.empty();
    for (var i = 1; i <= days; i++) {
        daySelect.append("<option>" + i + "</option>");
    }
}
</script>

</html>
