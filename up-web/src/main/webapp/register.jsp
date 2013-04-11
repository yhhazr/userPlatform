<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-17
  Time: 下午4:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>个人中心_第七大道</title>
    <link rel="shortcut icon" href="img/favicon.ico"/>
    <link href="css/home.css" rel="stylesheet" type="text/css"/>
    <link href="css/common.css" rel="stylesheet" type="text/css"/>
    <link href="css/showcss.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<jsp:include page="common/topClient2.jsp" flush="false"></jsp:include>

<div class="blank" style="height:40px;"></div>


<div class="register">
    <h2>注册加入第七大道游戏</h2>

    <div class="formbox">
        <div class="formlist relative" style="z-index: 20">
            <div class="formtext">用户名：</div>
            <div class="forminput"><input type="text" class="textinput" id="username" maxlength="20"/></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>6-20个英文字符,数字或下划线,以字母开头</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>6-20个英文字符、数字或下划线</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入用户名</div>
            </div>
            <div class="recommendUser" style="display: none;">
                <div class="triangle"></div>
                <div class="userErrorTips">该用户名已被注册</div>
                <div class="waringImg"></div>
                <div class="recommendUserList">
                    <p><span>推荐帐号：</span></p>
                    <div id="listUserName">
                    <p><input type="radio" name="recommendname"  /><label>32131231321</label></p>
                    <p><input type="radio" name="recommendname"  /><label>32131231321</label></p>
                    <p><input type="radio" name="recommendname"  /><label>32131231321</label></p>
                    <p><input type="radio" name="recommendname"  /><label>32131231321</label></p>
                    <p><input type="radio" name="recommendname"  /><label>32131231321</label></p>
                    </div>
                </div>

            </div>
        </div>
        <div class="formlist">
            <div class="formtext">密码：</div>
            <div class="forminput relative"  style="z-index: 10">
                <input type="password" class="textinput" id="password" maxlength="20"/>
                 <input type="hidden" id="fromUrl" value="${fromUrl}">
                <div class="pass_lv"></div>
                <div class="pass_lv2"></div>
            </div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>6-20数字字母符号，不能为9个以下纯数字</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>6-20数字字母符号，不能为9个以下纯数字</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入密码(不含有空格)</div>
            </div>
        </div>
        <div class="formlist">
            <div class="formtext">确认密码：</div>
            <div class="forminput"><input type="password" class="textinput" id="passwordaffirm" maxlength="20"/></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>请输入和上面密码相同的字符</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>两次输入的密码不一致</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入确认密码</div>
            </div>
        </div>

        <div class="formlist relative">
            <div class="formtext">验证码：</div>
            <div class="forminput"><input type="text" id="verifyCode" class="textinput"/></div>
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
            <div class="yzminput">
                <input type="hidden" value="" id="expireTimeR">
                <img id="log_verifyCodeImgReg"
                     style="vertical-align:middle;cursor:pointer;padding-left:5px"
                     src="" width="58px" height="25px"
                     onClick="javascript:refleshVerifyCodeReg();"><a
                    href="javascript:refleshVerifyCodeReg();">看不清？换一张</a>
            </div>
        </div>
        <div class="blank20"></div>
        <div class="blank20"></div>
        <div class="tongyishu">
            <input type="checkbox" id="agreeRegister" checked="checked"/>&nbsp;<label>我看过并同意<a href="/register_notice.html"
                                                                            target="_blank">《第七大道用户注册协议》</a></label>
        </div>
        <div class="now_register">
            <a href="javascript:void(0)" class="zc_submit"></a><a href="/login.html" class="oldusre u_line">老用户可直接登录</a>
        </div>
        <div class="blank"></div>
    </div>
    <div class="wel_wenzi">已经有"第七大道通行证"？您可以<a href="login.html">直接登录</a>，无需注册</div>
</div>


<div class="blank" style="height:60px;"></div>

<jsp:include page="./common/bottom.jsp" flush="false"></jsp:include>
</body>
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="scripts/common.js"></script>
<script type="text/javascript" src="scripts/registersq.js"></script>
</html>
