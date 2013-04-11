<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>用户注册_第七大道</title>
    <link rel="shortcut icon" href="img/favicon.ico"/>
    <link href="css/home.css" rel="stylesheet" type="text/css"/>
    <link href="css/common.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="head">
    <div class="headmenu">
        <a href="http://www.7road.com/" class="logo left">第七大道|打造绿色快乐的游戏</a>

        <div class="hasuser right">已有第七大道账号？请直接<a href="login.html">[登陆]</a></div>
    </div>
</div>

<div class="blank" style="height:60px;"></div>

<div class="register">
    <h2>注册加入第七大道游戏</h2>

    <div class="formbox">
        <div class="formlist">
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
        </div>
        <div class="formlist">
            <div class="formtext">密码：</div>
            <div class="forminput relative">
                <input type="password" class="textinput" id="password" maxlength="20"/>

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
            <div class="forminput"><input type="text" id="verifyCode" class="textinput" maxlength="4"/></div>
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

<jsp:include page="../common/bottom.jsp" flush="false"></jsp:include>

<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="scripts/jquery.gritter.js"></script>
<script type="text/javascript" src="scripts/common.js"></script>
<script type="text/javascript">
$(function () {
    refleshVerifyCodeReg();
    //验证表单。
    $('#verifyCode').focusin(function () {
        $('#verifyCode').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#verifyCode').removeClass("textinput_hover");
                var thisval = $(this).val();
                if (!thisval) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (thisval.length != 4) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr4").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });
    $('#username').focusin(function () {
        $('#username').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#username').removeClass("textinput_hover");
                var username = $(this).val();
                var usern = /^[a-zA-Z0-9_]{1,}$/;
                if (!username) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr4").show();
                }
                else {
                    if (!username.match(usern) || username == "" || username.length < 6 || username.length > 20) {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr2").show();
                    } else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                    }
                }
            });
    $("#password").focusin(function () {
        $('#password').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#password').removeClass("textinput_hover");
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
                    }else {
                        $(this).parent().siblings(".formerr").hide();
                        $(this).parent().siblings(".formerr3").show();
                        $(this).parent().siblings(".formerr3").children(".ok").addClass("hasok");
                    }
                }
            }).bind("keyup",function(){
                //判断密码强度
                var passvalue = $(this).val();
                if(passvalue.length>5){
                    var passvalueChar = check(passvalue);
                    if(passvalueChar==1){
                        $(".pass_lv2").css({"width":"5px","right":"37px"});

                    }else if(passvalueChar==2){
                        $(".pass_lv2").css({"width":"10px","right":"32px"})
                    }else if(passvalueChar==10){
                        $(".pass_lv2").css({"width":"15px","right":"27px"})
                    }else if(passvalueChar==3){
                        $(".pass_lv2").css({"width":"20px","right":"22px"})
                    }else if(passvalueChar==11){
                        $(".pass_lv2").css({"width":"25px","right":"17px"})
                    }else if(passvalueChar==12){
                        $(".pass_lv2").css({"width":"30px","right":"12px"})
                    }else if(passvalueChar==13){
                        $(".pass_lv2").css({"width":"35px","right":"7px"})
                    };

                }

            });
    $("#passwordaffirm").focusin(function () {
        $('#passwordaffirm').addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    }).focusout(function () {
                $('#passwordaffirm').removeClass("textinput_hover");
                var passvalue = $("#password").val();
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
    $(".zc_submit").click(register);
    $("#agreeRegister").click(function () {
        var chec = $("#agreeRegister").attr("checked");
        if (chec != "checked") {
            alert('请同意第七大道用户注册协议！');
        }
    });
    $("input").bind("keydown",function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            register();
        }
    });
});

function register() {
    var chec = $("#agreeRegister").attr("checked");
    if (chec != "checked") {
        alert('请同意第七大道用户注册协议！');
        return;
    }
    var uname = $("#username").val();
    var psw =trimAll($("#password").val());
    var pswc = $("#passwordaffirm").val();
    var verifyCode = $("#verifyCode").val();

    $('#username').removeClass("textinput_hover");
    $('#password').removeClass("textinput_hover");
    $('#passwordaffirm').removeClass("textinput_hover");
    $('#verifyCode').removeClass("textinput_hover");
    if (!uname) {
        $('#username').focus();
        $('#username').parent().siblings(".formerr").hide();
        $('#username').parent().siblings(".formerr4").show();

    }
    else {
        var usern = /^[a-zA-Z0-9_]{1,}$/;
        if (!uname.match(usern) || uname == "" || uname.length < 6 || uname.length > 20) {
            $('#username').focus();
            $('#username').parent().siblings(".formerr").hide();
            $('#username').parent().siblings(".formerr2").show();
        } else {
            if (!psw) {
                $('#password').focus();
                $('#password').parent().siblings(".formerr").hide();
                $('#password').parent().siblings(".formerr4").show();
            }
            else {
                if (/^\d+$/.test(psw) && psw.length < 9) {
                    $('#password').focus();
                    $('#password').parent().siblings(".formerr").hide();
                    $('#password').parent().siblings(".formerr2").show();
                } else if (psw.length < 6 || psw.length > 20) {
                    $('#password').focus();
                    $('#password').parent().siblings(".formerr").hide();
                    $('#password').parent().siblings(".formerr2").show();
                } else {
                    if (!pswc) {
                        $('#passwordaffirm').focus();
                        $('#passwordaffirm').parent().siblings(".formerr").hide();
                        $('#passwordaffirm').parent().siblings(".formerr4").show();
                    }
                    else {
                        if (pswc != psw || psw == "") {
                            $('#passwordaffirm').focus();
                            $('#passwordaffirm').parent().siblings(".formerr").hide();
                            $('#passwordaffirm').parent().siblings(".formerr2").show();
                        } else {
                            var shijian = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
                            if (!verifyCode) {
                                $('#verifyCode').focus();
                                $('#verifyCode').parent().siblings(".formerr").hide();
                                $('#verifyCode').parent().siblings(".formerr4").show();
                            }
                            else {
                                if (verifyCode.length != 4) {
                                    $('#verifyCode').focus();
                                    $('#verifyCode').parent().siblings(".formerr").hide();
                                    $('#verifyCode').parent().siblings(".formerr4").show();
                                } else {
                                    $.ajax({
                                        url:'/updatenameSubmit',
                                        type:'post',
                                        datatype:'json',
                                        async:false,
                                        data:{user:uname, psw:psw, verifyCode:$("#verifyCode").val(),timeStr:$("#expireTimeR").val(),
                                        oldname:"${param.currentname}",oldsite:"${param.currentsite}", newsite:"${param.tosite}",k:"${param.k}",s:"${param.s}"},
                                        success:function (data) {
                                            var code = data.code;
                                            var msg = data.msg;
                                            if (code == 199) {
                                                $('#username').focus();
                                                $('#username').parent().siblings(".formerr").hide();
                                                $('#username').parent().siblings(".formerr4").empty().
                                                        addClass("warning").append("<span class='error'></span>" + msg).show();
                                            }
                                            if (code == 201) {
                                                $('#password').focus();
                                                $('#password').parent().siblings(".formerr").hide();
                                                $('#password').parent().siblings(".formerr4").empty().
                                                        addClass("warning").append("<span class='error'></span>" + msg).show();
                                            }
                                            if (code == 202) {
                                                $('#verifyCode').focus();
                                                $('#verifyCode').parent().siblings(".formerr").hide();
                                                $('#verifyCode').parent().siblings(".formerr4").empty().
                                                        addClass("warning").append("<span class='error'></span>" + msg).show();
                                                refleshVerifyCodeReg();
                                            }
                                            if (code == 200) {
                                                alert("注册成功");
                                                $.ajax(
                                                        {   url:'/login',
                                                            type:'post',
                                                            datatype:'json',
                                                            data:{userName:uname, psw:psw},
                                                            success:function (data) {
                                                                if (data.code == 200) {
                                                                    //window.location.href = "http://account.7road.com";
                                                                    //enterGame1('/PlayGame2?game=S&subGame=0&g=${g}&z=${z}');
                                                                    enterGame2('/PlayGame2?game=S&subGame=0&g=${g}&z=${z}');
                                                                } else {
                                                                    alert(data.msg);
                                                                }
                                                            },
                                                            error:function () {
                                                                window.location.href = "/login.html";
                                                            }
                                                        });
                                            }
                                            if (code == 204) {
                                                alert(msg);
                                                window.location.href = "http://sq.7road.com/";
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

            }
        }

    }


}
//去掉空格
function trimAll(str){
    return str.replace(/(^\s*)|(\s*)|(\s*$)/g, "");
};
// 刷新验证码
function refleshVerifyCodeReg() {
    var timeStr=(new Date().getTime());
    $("#log_verifyCodeImgReg").attr("src", '/verifyCode?time=' +timeStr );
    $("#expireTimeR").val(timeStr);
}
function enterGame2(url){
    enterGame(url + "&status=1", function (m) {
        if (m.code == 0) {
            var _t = Date.parse(new Date());
            window.location.href = url + "&_t=" + _t;
        } else {
            alert(m.msg);
            window.location.href = "http://account.7road.com";
        }
    });
}
</script>
<script type="text/javascript" src="../scripts/accountcenter.js"></script>
<script type="text/javascript" src="../scripts/app-common.js"></script>
</body>
</html>
