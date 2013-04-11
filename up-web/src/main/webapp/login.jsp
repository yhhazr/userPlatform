<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-17
  Time: 下午4:54
  登录页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>个人中心_第七大道</title>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link href="css/commonsq.css" rel="stylesheet" type="text/css"/>
    <link href="css/home.css" rel="stylesheet" type="text/css"/>
    <link href="css/showcss.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<jsp:include page="common/topClient2.jsp" flush="false"></jsp:include>

<div class="blank20" ></div>

<div class="register">
    <h2>登录第七大道游戏</h2>

    <div class="formbox">
        <div class="formlist">
            <div class="formtext">用户名：</div>
            <div class="forminput"><input id="userName" maxlength="20" type="text" class="textinput"/></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>6-20个中英文字符、数字或下划线组成</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>请输入帐号</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4 warning"><span class="error"></span>帐号不正确！</div>
        </div>
        <div class="formlist">
            <div class="formtext">密码：</div>
            <div class="forminput"><input id="psw" maxlength="20" type="password" class="textinput"/></div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>请输入密码</div>
            </div>
            <div class="formerr formerr1">
                <div class="warning"><span class="error"></span>至少6位字符（字母、数字、符号）</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4"><span class="error"></span>至少6位字符（字母、数字、符号）</div>
        </div>

        <div class="formlist relative" id="verifyDiv" style="display: none">
            <div class="formtext">验证码：</div>
            <div class="forminput"><input id="verifyCode" maxlength="20" type="text" class="textinput"/>
                <input type="hidden" id="needVerifyCode" value="false">
                <input type="hidden" id="fromUrl" value="${requestScope.fromUrl}">
            </div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>请输入4位验证码</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>验证码错误</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入4位验证码</div>
            </div>
            <div class="yzminput"><input type="hidden" value="" id="expireTimel">
                <img id="log_verifyCodeImg"
                     style="vertical-align:middle;cursor:pointer;padding-left:5px"
                     src="" width="58px" height="25px"
                     onClick="javascript:refleshVerifyCode();"><a href="javascript:void(0)"
                                                                  onclick="javascript:refleshVerifyCode();">看不清？换一张</a>
            </div>
        </div>
        <div class="blank10"></div>
        <div class="now_register">
            <a href="javascript:void(0)" class="loginbtn"></a><span class="oldusre oldusre1"><input type="checkbox"
                                                                                                    id="rmbUser"
                                                                                                    checked="checked"/>记住我？</span>

            <div class="blank20"></div>
            <a href="/forget.html" target="_blank">忘记密码？</a>
        </div>
        <div class="blank"></div>
    </div>
    <div class="nouser">
        <span class="left">还没有帐号？</span>
        <a href="/register.html" class="go_register">立即注册</a>

        <div class="blank"></div>
    </div>

</div>


<div class="blank" style="height:50px;"></div>

<jsp:include page="./common/bottom.jsp" flush="false"></jsp:include>
</body>
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="scripts/jquery.cookie.js"></script>
<script type="text/javascript" src="scripts/userhomesq.js"></script>
</html>
