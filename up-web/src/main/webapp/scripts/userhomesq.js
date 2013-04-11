$(function () {
    refleshVerifyCode();
    $("#userName").focusin(function () {
        $("#userName").addClass("textinput_hover");
    }).focusout(function () {
            var userName = $("#userName").val();
            if (!userName) {
                $("#userName").removeClass("textinput_hover");
                $("#userName").parent().siblings(".formerr").hide();
                $("#userName").parent().siblings(".formerr2").show();
            }
            else {
                $("#userName").removeClass("textinput_hover");
                $("#userName").parent().siblings(".formerr").hide();
            }
        });
    $("#psw").focusin(function () {
        $("#psw").addClass("textinput_hover");
    }).focusout(function () {
            var psw = $("#psw").val();
            if (!psw) {
                $("#psw").removeClass("textinput_hover");
                $("#psw").parent().siblings(".formerr").hide();
                $("#psw").parent().siblings(".formerr2").show();
            }
            else {
                $("#psw").removeClass("textinput_hover");
                $("#psw").parent().siblings(".formerr").hide();
            }
        });
    $("#verifyCode").focusin(function () {
        $("#verifyCode").addClass("textinput_hover");
    });
    $(".loginbtn").click(login);
    $("#rmbUser").click(saveUserInfo);
    $("input").bind("keydown", function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            login();
        }
    });
});
function login() {
    var userName = $("#userName").val();
    var psw = $("#psw").val();
    var needVerifyCode = $("#needVerifyCode").val();
    var verifyCode = $("#verifyCode").val();
    var timeStr= $("#expireTimel").val();
    if (!userName) {
        $("#userName").focus();
        $("#userName").removeClass("textinput_hover");
        $("#userName").parent().siblings(".formerr").hide();
        $("#userName").parent().siblings(".formerr2").show();
    }
    else {
        if (userName.length >= 6) {
            if (!psw) {
                $("#psw").focus();
                $("#psw").removeClass("textinput_hover");
                $("#psw").parent().siblings(".formerr").hide();
                $("#psw").parent().siblings(".formerr2").show();
            }
            else {
                saveUserInfo();
                if (needVerifyCode == "true") {
                    if (!verifyCode) {
                        $("#verifyCode").focus();
                        $("#verifyCode").removeClass("textinput_hover");
                        $("#verifyCode").parent().siblings(".formerr").hide();
                        $("#verifyCode").parent().siblings(".formerr4").show();
                    }
                    else {
                        if (verifyCode.length != 4) {
                            $("#verifyCode").focus();
                            $("#verifyCode").removeClass("textinput_hover");
                            $("#verifyCode").parent().siblings(".formerr").hide();
                            $("#verifyCode").parent().siblings(".formerr4").show();
                        }
                        else {
                            $.ajax(
                                {   url:'/login',
                                    type:'post',
                                    datatype:'json',
                                    data:{userName:userName, psw:psw, verifyCode:verifyCode, useVerifyCode:needVerifyCode,timeStr:timeStr},
                                    success:function (data) {

                                        var msg = data.msg;
                                        var code = data.code;
                                        if (code == 200) {
                                            window.location.href = "/accountcenter.html";
                                        }
                                        if (data.object == "showVerifyCode") {
                                            $("#verifyDiv").show();
                                            $("#needVerifyCode").val("true");
                                        }

                                        if (code == 204) {
                                            $('#userName').focus();
                                            $('#verifyCode').parent().siblings(".formerr").hide();
                                            $('#userName').parent().siblings(".formerr").hide();
                                            $('#userName').parent().siblings(".formerr4").empty().
                                                addClass("warning").append("<span class='error'></span>" + msg).show();
                                            refleshVerifyCode();
                                        }

                                        if (code == 202) {
                                            $('#verifyCode').focus();
                                            $('#userName').parent().siblings(".formerr").hide();
                                            $('#verifyCode').parent().siblings(".formerr").hide();
                                            $('#verifyCode').parent().siblings(".formerr4").empty().
                                                addClass("warning").append("<span class='error'></span>" + msg).show();
                                            refleshVerifyCode();
                                        }
                                        if (code == 400) {
                                            window.location.href = data.object;
                                        }

                                    },
                                    error:function () {
                                        window.location.href = "/login.html";
                                    }
                                });
                        }
                    }
                }
                else {
                    $.ajax(
                        {   url:'/login',
                            type:'post',
                            datatype:'json',
                            data:{userName:userName, psw:psw, verifyCode:verifyCode, useVerifyCode:needVerifyCode,timestr:timeStr,fromUrl:$("#fromUrl").val()},
                            success:function (data) {

                                var msg = data.msg;
                                var code = data.code;
                                if (code == 200) {
                                    window.location.href = "/accountcenter.html";
                                }
                                if (data.object == "showVerifyCode") {
                                    $("#verifyDiv").show();
                                    $("#needVerifyCode").val("true");
                                }else
                                {
                                    $("#verifyDiv").hide();
                                    $("#needVerifyCode").val("false");
                                }

                                if (code == 204) {
                                    $('#userName').focus();
                                    $('#verifyCode').parent().siblings(".formerr").hide();
                                    $('#userName').parent().siblings(".formerr").hide();
                                    $('#userName').parent().siblings(".formerr4").empty().
                                        addClass("warning").append("<span class='error'></span>" + msg).show();
                                    refleshVerifyCode();

                                }

                                if (code == 202) {
                                    $('#verifyCode').focus();
                                    $('#userName').parent().siblings(".formerr").hide();
                                    $('#verifyCode').parent().siblings(".formerr").hide();
                                    $('#verifyCode').parent().siblings(".formerr4").empty().
                                        addClass("warning").append("<span class='error'></span>" + msg).show();
                                    refleshVerifyCode();
                                }
                                if (code == 400) {
                                    window.location.href = data.object;
                                }
                            },
                            error:function () {
                                window.location.href = "/login.html";
                            }
                        });
                }
            }
        }
        else {
            $("#userName").focus();
            $("#userName").removeClass("textinput_hover");
            $("#userName").parent().siblings(".formerr").hide();
            $("#userName").parent().siblings(".formerr4").show();
        }
    }
}


//初始化页面时验证是否记住了密码
$(function () {
    if ($.cookie("rmbUser") == "true") {
        $("#rmbUser").attr("checked", true);
        $("#userName").val($.cookie("userName"));
//        $("#psw").val($.cookie("passWord"));
    }
});
//保存用户信息
function saveUserInfo() {
    var rmb = $("#rmbUser").attr("checked");
    if (rmb == "checked") {
        var userName = $("#userName").val();
        var passWord = $("#psw").val();
        $.cookie("rmbUser", "true", { expires:7 }); // 存储一个带7天期限的 cookie
        $.cookie("userName", userName, { expires:7 }); // 存储一个带7天期限的 cookie
//        $.cookie("passWord", passWord, { expires:7 }); // 存储一个带7天期限的 cookie
    }
    else {
        $.cookie("rmbUser", "false", { expires:-1 });
        $.cookie("userName", '', { expires:-1 });
//        $.cookie("passWord", '', { expires:-1 });
    }
}

// 刷新验证码
function refleshVerifyCode() {
    var timeStr=(new Date().getTime());
    $("#log_verifyCodeImg").attr("src", '/verifyCode?time=' + timeStr);
    $("#expireTimel").val(timeStr);

}
