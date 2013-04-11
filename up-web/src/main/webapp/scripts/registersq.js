$(function () {
    refleshVerifyCodeReg();
    $("#listUserName p").live("click",function(){
        $(".recommendUser").hide();
        var thisValue = $(this).children("label").text();
        $("#username").val(thisValue);
        checkUserName();
    })
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
        $(".recommendUser").hide();
    }).focusout(function () {
            $('#username').removeClass("textinput_hover");
            var username = $(this).val();
            if (!username) {
                $(this).parent().siblings(".formerr").hide();
                $(this).parent().siblings(".formerr4").show();
            }
            else {
                if (isChn(username)|| username == "" || username.length < 6 || username.length > 20) {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr2").show();
                } else {
                    checkUserName();
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
                } else {
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr3").show();
                    $(this).parent().siblings(".formerr3").children(".ok").addClass("hasok");
                }
            }
        }).bind("keyup", function () {
            //判断密码强度
            var passvalue = $(this).val();
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
    $("input").bind("keydown", function (e) {
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
    var uname = $.trim($("#username").val());
    var psw = trimAll($.trim($("#password").val()));
    var pswc = $.trim($("#passwordaffirm").val());
    var verifyCode = $.trim($("#verifyCode").val());

    $('#username').removeClass("textinput_hover");
    $('#password').removeClass("textinput_hover");
    $('#passwordaffirm').removeClass("textinput_hover");
    $('#verifyCode').removeClass("textinput_hover");

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
                                        data:{type:'register', user:uname, psw:psw, verifyCode:$("#verifyCode").val(),
                                            timeStr:$("#expireTimeR").val(),"fromUrl":$("#fromUrl").val()},
                                        success:function (data) {
                                            var code = data.code;
                                            var msg = data.msg;
                                            if (code == 199) {
                                                $('#username').focus();
                                                $('#username').parent().siblings(".formerr").hide();
                                                $('#username').parent().siblings(".formerr4").empty().
                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                refleshVerifyCodeReg();
                                            }
                                            if (code == 201) {
                                                $('#password').focus();
                                                $('#password').parent().siblings(".formerr").hide();
                                                $('#password').parent().siblings(".formerr4").empty().
                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                refleshVerifyCodeReg();
                                            }
                                            if (code == 202) {
                                                $('#verifyCode').focus();
                                                $('#verifyCode').parent().siblings(".formerr").hide();
                                                $('#verifyCode').parent().siblings(".formerr4").empty().
                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                refleshVerifyCodeReg();
                                            }
                                            if (code == 200) {
                                                window.location.href = "/accountcenter.html";
                                            }
                                            if (code == 204) {
                                                alert(msg);
                                                window.location.href = '/register.html';
                                            }
                                            if (code == 400) {
                                                window.location.href = data.object;
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

function checkUserName()
{
    var uname = $.trim($("#username").val());
    $.ajax({
        url:'/checkUserName',
        type:'post',
        datatype:'json',
        data:{type:'register', userName:uname},
        success:function (data) {
            var code = data.code;
            var msg = data.msg;
            if (code == 199) {
                if(msg=="已经存在的用户名!")
                {
                    $("#listUserName").empty();
                    $.each(data.object,function(index,value){  $("#listUserName").append("<p><input type='radio' name='recommendname'  /><label>"+value+"</label></p>")});
                    $(".recommendUser").show();
                }
                else
                {
                    $('#username').focus();
                    $('#username').parent().siblings(".formerr").hide();
                    $('#username').parent().siblings(".formerr4").empty().
                        addClass("warning").append("<span class='error'></span>" + msg).show();
                    refleshVerifyCodeReg();
                }
            } else
            {
                $('#username').parent().siblings(".formerr").hide();
                $('#username').parent().siblings(".formerr3").show();
                $('#password').focus();
            }
        },
        error:function () {
            window.location.href = '/register.html';
        }
    });
}

//去掉空格
function trimAll(str) {
    return str.replace(/(^\s*)|(\s*)|(\s*$)/g, "");
}
;
// 刷新验证码
function refleshVerifyCodeReg() {
    var timeStr = (new Date().getTime());
    $("#log_verifyCodeImgReg").attr("src", '/verifyCode?time=' + timeStr);
    $("#expireTimeR").val(timeStr);
}

//防沉迷注册js
$("#registerFcm").bind("click", function () {
    var chec = $("#agreeRegister").attr("checked");
    if (chec != "checked") {
        alert('请同意第七大道用户注册协议！');
    }
    else {
        var _name = $.trim($("#uName").val());
        var _psw = $.trim($("#uPsw").val());
        var _pswC = $.trim($("#uPswConfirm").val());
        var _email = $.trim($("#uEmail").val());
        var _rName = $.trim($("#rName").val());
        var _icn = $.trim($("#uIcn").val());
        var _code = $.trim($("#code").val());
        var _timeStr = $.trim($("#timeStr").val());
        if (!_name) {
            $("#uName").parent().siblings(".formerr").hide();
            $("#uName").parent().siblings(".formerr4").show();
        }
        else {
            var usern = /^[a-zA-Z0-9_]{1,}$/;
            if (isChn(_name) || !_name.match(usern) || _name == "" || _name.length < 6 || _name.length > 20) {
                $("#uName").parent().siblings(".formerr").hide();
                $("#uName").parent().siblings(".formerr2").show();
            } else {
                if (!_psw) {
                    $("#uPsw").parent().siblings(".formerr").hide();
                    $("#uPsw").parent().siblings(".formerr4").show();
                }
                else {
                    if (/^\d+$/.test(_psw) && _psw.length < 9) {
                        $("#uPsw").parent().siblings(".formerr").hide();
                        $("#uPsw").parent().siblings(".formerr2").show();
                    } else if (trimAll(_psw).length < 6 || trimAll(_psw).length > 20) {
                        $("#uPsw").parent().siblings(".formerr").hide();
                        $("#uPsw").parent().siblings(".formerr2").show();
                    } else if (trimAll(_psw).length !=$("#uPsw").val().length) {
                        $('#uPsw').focus();
                        $('#uPsw').parent().siblings(".formerr").hide();
                        $('#uPsw').parent().siblings(".formerr4").empty().
                            addClass("warning").append("<span class='error'></span>密码不能含有空格!").show();
                    }

                    else {
                        if (!_pswC) {
                            $("#uPswConfirm").parent().siblings(".formerr").hide();
                            $("#uPswConfirm").parent().siblings(".formerr4").show();
                        }
                        else {
                            if (_psw != _pswC || _pswC == "") {
                                $("#uPswConfirm").parent().siblings(".formerr").hide();
                                $("#uPswConfirm").parent().siblings(".formerr2").show();
                            } else {
                                if (!_email) {
                                    $("#uEmail").parent().siblings(".formerr").hide();
                                    $("#uEmail").parent().siblings(".formerr4").show();
                                }
                                else {
                                    if (!isEmail(_email) || _email == "" || _email.length < 6 || _email.length > 50) {
                                        $("#uEmail").parent().siblings(".formerr").hide();
                                        $("#uEmail").parent().siblings(".formerr2").show();
                                    } else {
                                        if (!_code) {
                                            $("#code").parent().siblings(".formerr").hide();
                                            $("#code").parent().siblings(".formerr4").show();
                                        }
                                        else {
                                            if ($.trim(_code).length != 4) {
                                                $("#code").parent().siblings(".formerr").hide();
                                                $("#code").parent().siblings(".formerr2").show();
                                            } else {
                                                if (!_rName) {
                                                    $("#rName").parent().siblings(".formerr").hide();
                                                    $("#rName").parent().siblings(".formerr4").show();
                                                }
                                                else {
                                                    if (!isChn(_rName) || _rName == "" || _rName.length < 2 || _rName.length > 8) {
                                                        $("#rName").parent().siblings(".formerr").hide();
                                                        $("#rName").parent().siblings(".formerr2").show();
                                                    } else {
                                                        if (!_icn) {
                                                            $("#uIcn").parent().siblings(".formerr").hide();
                                                            $("#uIcn").parent().siblings(".formerr4").show();
                                                        }
                                                        else {
                                                            if (validateIdCard(_icn) > 0 || _icn == "" || _icn.length < 6 || _icn.length > 18) {
                                                                $("#uIcn").parent().siblings(".formerr").hide();
                                                                $("#uIcn").parent().siblings(".formerr2").show();
                                                            } else {
                                                                $.ajax({
                                                                    url:'/register2',
                                                                    type:'post',
                                                                    data:{"uName":_name, "uPsw":_psw, "uPswConfirm":_pswC, "uEmail":_email, "rName":_rName, "uIcn":_icn, "code":_code, "timeStr":_timeStr},
                                                                    dataType:'json',
                                                                    success:function (data) {
                                                                        var code = data.code;
                                                                        var msg = data.msg;
                                                                        if (code != 200) {
                                                                            refleshVerifyCodeRegfcm();
                                                                            if (code == 201) {
                                                                                $('#uName').parent().siblings(".formerr").hide();
                                                                                $('#uName').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                            if (code == 202) {
                                                                                $('#uPsw').parent().siblings(".formerr").hide();
                                                                                $('#uPsw').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                            if (code == 203) {
                                                                                $('#uPswConfirm').parent().siblings(".formerr").hide();
                                                                                $('#uPswConfirm').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                            if (code == 204) {
                                                                                $('#uEmail').parent().siblings(".formerr").hide();
                                                                                $('#uEmail').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                            if (code == 205) {
                                                                                $('#code').parent().siblings(".formerr").hide();
                                                                                $('#code').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                            if (code == 206) {
                                                                                $('#rName').parent().siblings(".formerr").hide();
                                                                                $('#rName').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                            if (code == 207) {
                                                                                $('#uIcn').parent().siblings(".formerr").hide();
                                                                                $('#uIcn').parent().siblings(".formerr4").empty().
                                                                                    addClass("warning").append("<span class='error'></span>" + msg).show();
                                                                            }
                                                                        }
                                                                        else {
                                                                            window.location.href = "/accountcenter.html";
                                                                        }
                                                                    },
                                                                    error:function () {
                                                                        alert('连接服务器错误!');
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
                        }
                    }
                }
            }
        }


    }

});
function refleshVerifyCodeRegfcm() {
    var timeStr = (new Date().getTime());
    $("#log_verifyCodeImgReg").attr("src", '/verifyCode?time=' + timeStr);
    $("#timeStr").val(timeStr);
}




