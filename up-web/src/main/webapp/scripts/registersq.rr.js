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
                                        url:'/register',
                                        type:'post',
                                        datatype:'json',
                                        data:{type:'register',user:uname, psw:psw, verifyCode:$("#verifyCode").val(),timeStr:$("#expireTimeR").val()},
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
                                            }
                                            if (code == 200) {
                                                $.ajax(
                                                    {   url:'/login',
                                                        type:'post',
                                                        datatype:'json',
                                                        data:{userName:uname, psw:psw},
                                                        success:function (data) {
                                                            if (data.code == 200) {
                                                                window.location.href = "/accountcenter.html";
                                                            }
                                                            else {
                                                                window.location.href = "/login.html";
                                                            }
                                                        },
                                                        error:function () {
                                                            window.location.href = "/login.html";
                                                        }
                                                    });
                                            }
                                            if (code == 204) {
                                                alert(msg);
                                                window.location.href = '/register.html';
                                            }
                                        },
                                        error:function () {
                                            window.location.href = '/register.html';
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




