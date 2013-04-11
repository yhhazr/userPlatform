<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-10-23
  Time: 上午10:39
  防沉迷注册页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>个人中心_第七大道</title>
    <link href="css/common.css" rel="stylesheet" type="text/css"/>
    <link href="css/home.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        body, td, th {
            color: #333;
        }
    </style>
</head>

<body>
<div class="head">
    <div class="headmenu">
        <a href="http://www.7road.com/" class="logo left">第七大道|打造绿色快乐的游戏</a>

        <div class="hasuser right">已有第七大道账号？请直接<a href="login.html">[登陆]</a></div>
    </div>
</div>

<div class="blank" style="height:60px;"></div>

<div class="register register_back">

    <h2>注册加入第七大道游戏</h2>

    <div class="createUser">
        <div class="createImg"></div>
        <div class="createDiv"><strong>创建帐号：</strong><span>以下内容我们承诺您的信息安全，不会透露给第三方</span></div>
    </div>
    <div class="blank"></div>
    <div class="formbox">
        <div class="formlist">
            <div class="formtext"><cite>*</cite>用户账号：</div>
            <div class="forminput"><input type="text" class="textinput" id="uName" maxlength="20"/></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>6-20个英文字符、数字或下划线组成</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>6-20个英文字符、数字或下划线</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入用户名</div>
            </div>
        </div>
        <div class="formlist">
            <div class="formtext"><cite>*</cite>登录密码：</div>
            <div class="forminput relative">
                <input type="password" class="textinput" id="uPsw" maxlength="20"/>

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
                <div class="warning"><span class="error"></span>请输入登录密码!</div>
            </div>
        </div>
        <div class="formlist">
            <div class="formtext"><cite>*</cite>确认密码：</div>
            <div class="forminput"><input type="password" class="textinput" id="uPswConfirm" maxlength="20"/></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>请输入和上面密码相同的字符</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>两次输入的密码不一致</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入确认密码!</div>
            </div>
        </div>
        <div class="formlist">
            <div class="formtext"><cite>*</cite>电子邮件：</div>
            <div class="forminput"><input type="text" class="textinput" id="uEmail" maxlength="50"/></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>输入您的邮箱，请认真填写</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>请输入正确的邮箱地址!</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入您的邮箱地址!</div>
            </div>
        </div>
        <div class="formlist relative">
            <div class="formtext">验证码：</div>
            <div class="forminput"><input type="text" class="textinput" id="code"/></div>
            <div class="formerr formerr1">
                <div class="warning" style="width:108px;"><span class="prompt"></span>请输入验证码</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning" style="width:138px;"><span class="error"></span>请输入4位的验证码!</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入验证码!</div>
            </div>
            <div class="yzminput">
                <img id="log_verifyCodeImgReg"
                     style="vertical-align:middle;cursor:pointer;padding-left:5px"
                     src="" width="58px" height="25px"
                     onClick="javascript:refleshVerifyCodeReg();"><a
                    href="javascript:refleshVerifyCodeReg();">看不清？换一张</a> <br>
                <input type="hidden" id="timeStr" name="timeStr">
            </div>
        </div>
        <div class="blank20"></div>
        <div class="blank20"></div>
    </div>
    <div class="blank20"></div>
    <div class="blank" style="border-bottom:1px solid #ddd"></div>
    <div class="createUser">
        <div class="createImg createImg2"></div>
        <div class="createDiv createDiv2"><strong>未成年人防沉迷认证：</strong><span>根据2010年8月1日实施的《网络游戏管理暂行办法》，网络游戏用户需使用有效身份证件进行实名注册。为保证流畅游戏体验，享受健康游戏生活，请广大第七大道游戏的玩家尽快实名注册。</span>
        </div>
    </div>

    <div class="formbox">
        <div class="formlist">
            <div class="formtext"><cite>*</cite>真实姓名：</div>
            <div class="forminput"><input type="text" class="textinput" id="rName" maxlength="8"/></div>
            <div class="formerr formerr1">
                <div class="warning" style="width:275px;"><span class="prompt"></span>请输入您的真实姓名，例如：李四</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning" style="width:275px;"><span class="error"></span>请输入合法的中文姓名，例如：李四</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入真实姓名</div>
            </div>
        </div>
        <div class="formlist">
            <div class="formtext"><cite>*</cite>身份证号码：</div>
            <div class="forminput relative">
                <input type="text" class="textinput" id="uIcn" maxlength="18"/>
            </div>
            <div class="formerr formerr1">
                <div class="warning" style="width:275px;"><span class="prompt"></span>请输入身份证号码，例如440106198101010155
                </div>
            </div>
            <div class="formerr formerr2">
                <div class="warning" style="width:275px;"><span class="error"></span>身份证号码错误</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入身份证号码!</div>
            </div>
        </div>
    </div>
    <div class="blank"></div>
    <div class="tongyishu" style="margin-left:230px;">
        <input type="checkbox" checked="checked" id="agreeRegister"/>&nbsp;<label>我看过并同意<a href="/register_notice.html"
                                                                                           target="_blank">《第七大道用户注册协议》</a></label>
    </div>
    <div class="now_register" style="margin-left:230px;">
        <a href="javascript:void(0)" id="registerFcm" class="zc_submit1"></a><a href="/login.html"
                                                                                class="oldusre u_line">老用户可直接登录</a>
    </div>
    <div class="blank"></div>
    <div class="wel_wenzi">已经有"第七大道通行证"？您可以<a href="/login.html">直接登录</a>，无需注册</div>
</div>


<div class="blank" style="height:60px;"></div>

<jsp:include page="./common/bottom.jsp" flush="false"></jsp:include>
</body>
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="scripts/userhome.js"></script>
<script type="text/javascript" src="scripts/common.js"></script>
<script type="text/javascript" src="scripts/registersq.js"></script>
<script type="text/javascript">

$(function () {
    refleshVerifyCodeRegfcm(); //页面加载的时候刷新验证码
    $('#uName').focusin(function () {
        $('#uName').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#uName').removeClass("textinput_hover");
                var username = $(this).val();
                var usern = /^[a-zA-Z0-9_]{1,}$/;
                if (!username) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (isChn(username) || !username.match(usern) || username == "" || username.length < 6 || username.length > 20) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });
    $("#uPsw").focusin(function () {
        $('#uPsw').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#uPsw').removeClass("textinput_hover");
                var passvalue = $(this).val();
                if (!passvalue) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (/^\d+$/.test(passvalue) && passvalue.length < 9) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else if (trimAll(passvalue).length < 6 || trimAll(passvalue).length > 20) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                        $(this).parent().siblings(".formerr3").children(".ok").addClass("hasok");
                    }
                }
            }).bind("keyup", function () {
                //判断密码强度
                var passvalue = $('#uPsw').val();
                if (passvalue.length > 5) {
                    var passvalueChar = check(passvalue);
                    if (passvalueChar == 1) {
                        $(".pass_lv2").css({"width":"5px", "right":"37px"});

                    } else if (passvalueChar == 2) {
                        $(".pass_lv2").css({"width":"10px", "right":"32px"})
                    } else if (passvalueChar == 10) {
                        $(".pass_lv2").css({"width":"15px", "right":"27px"})
                    } else if (passvalueChar == 3) {
                        $(".pass_lv2").css({"width":"20px", "right":"22px"})
                    } else if (passvalueChar == 11) {
                        $(".pass_lv2").css({"width":"25px", "right":"17px"})
                    } else if (passvalueChar == 12) {
                        $(".pass_lv2").css({"width":"30px", "right":"12px"})
                    } else if (passvalueChar == 13) {
                        $(".pass_lv2").css({"width":"35px", "right":"7px"})
                    }
                    ;
                }
            });
    $("#uPswConfirm").focusin(function () {
        $('#uPswConfirm').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#uPswConfirm').removeClass("textinput_hover");
                var passvalue = $("#uPsw").val();
                var haspassword = $(".ok").eq(1).hasClass("hasok");
                if (!$(this).val()) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if ($(this).val() != passvalue || passvalue == "") {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });

    $('#uEmail').focusin(function () {//邮箱
        $('#uEmail').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#uEmail').removeClass("textinput_hover");
                var uemail = $(this).val();
                if (!uemail) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (!isEmail(uemail)|| uemail == "" || uemail.length < 6 || uemail.length > 50) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });

    $('#code').focusin(function () {//验证码
        $('#code').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#code').removeClass("textinput_hover");
                var codev = $(this).val();
                if (!codev) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if ($.trim(codev).length != 4) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });

    $('#rName').focusin(function () { //真实姓名
        $('#rName').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#rName').removeClass("textinput_hover");
                var username = $.trim($(this).val());
                if (!username) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (!isChn(username) || username == "" || username.length < 2 || username.length > 8) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });

    $('#uIcn').focusin(function () { //真实姓名
        $('#uIcn').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#uIcn').removeClass("textinput_hover");
                var username = $(this).val();
                if (!username) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (validateIdCard(username) > 0 || username == "" || username.length < 6 || username.length > 18) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });

});

</script>
</html>
