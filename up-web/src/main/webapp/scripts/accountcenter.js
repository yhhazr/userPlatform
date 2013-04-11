function checkEmail(el) {
    var regu = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]|net|NET|asia|ASIA|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN|cc|CC)$"
    var re = new RegExp(regu);
    if (el.search(re) == -1) {
        return false; //非法
    }
    return true;//正确
}
function isMobil(s) {
    // /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/
    var p = /^0?1[3|4|5|8][0-9]{9}$/;
    if (!p.exec(s)) {
        return false;
    }
    return true;
}
function modifyAccountInfo() {
    var userName = $("#userName").text();
    var gender = $("div button[class='btn active']").attr("value");
    var year = $("#year").val();
    var month = $("#month").val();
    var day = $("#day").val();
    var birthday = year + "-" + month + "-" + day
    var city = $("#province").val() + "," + $("#city").val();
    var career = $("#career").val();
    var eduLevel = $("#eduLevel").val();
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/AccountInfoSubmit",
        data:{"userName":userName, "gender":gender, "birthday":birthday, "city":city, "career":career, "eduLevel":eduLevel},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0)msgInfo(msg.msg);
            else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}
function validate(email) {
    if (email == "") {
        msgError("邮箱不能为空");
        return false;
    }
    if (!checkEmail(email)) {
        msgError("邮箱格式不正确");
        return false;
    }
    return true;
}
function emailVerifySend(userName) {
    var email = $("#email").val();
    var pwd = $("#email_pwd").val();
    if (email == "") {
        msgError("请输入邮箱地址");
        return false;
    }
    if (!validate(email))return false;
    if (pwd == "") {
        msgError("请输入密码");
        return false;
    }
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/EmailVerifySend",
        data:{"userName":userName, "email":email, "userPass":pwd},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                msgInfo(msg.msg);
                $("#email_pwd").val("");
            }
            else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}

function emailModify(obj) {
    $(obj).hide();
    $('#changeEmailSpan').show();
    return false;
}
function emailModifySubmit(userName, oldEmail) {
    var email = $("#new_email").val();
    var pwd = $("#email_pwd2").val();
    if (email == "") {
        msgError("请输入邮箱地址");
        return false;
    }
    if (!validate(email))return false;
    if (pwd == "") {
        msgError("请输入密码");
        return false;
    }
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/EmailVerifySend",
        data:{"userName":userName, "userPass":pwd, "oldEmail":oldEmail, "email":email},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                msgInfo(msg.msg);
                $("#email_pwd2").val("");
            }
            else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}
function bindEmail(userName) {
    var email = $("#email").val();
    var btnEmail = $("#emailBindBtn");
    if (email == "") {
        msgError("请输入邮箱地址");
        return false;
    }
    if (!checkEmail(email)) {
        msgError("邮箱格式不正确");
        return false;
    }
    btnEmail.attr("disabled","disabled");
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/EmailBind",
        data:{"userName":userName, "email":email, type: "bind"},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0)msgInfo(msg.msg);
            else msgError(msg.msg);
            btnEmail.removeAttr("disabled");
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}
function unBindEmail(userName, email) {
    var btnEmail = $("#emailUnbindBtn")
    btnEmail.attr("disabled","disabled");
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/EmailBind",
        data:{"userName":userName, "email":email, type: "unbind"},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0)msgInfo(msg.msg);
            else msgError(msg.msg);
            btnEmail.removeAttr("disabled");
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}
function modifyPassword(userName) {
    var oldPwd = $("#old_pwd");
    var newPwd = $("#new_pwd");
    var newPwdr = $("#confirm_pwd");
    if (oldPwd.val().trim().length < 1 || newPwd.val().trim().length < 1 || newPwdr.val().trim().length < 1) {
        if (oldPwd.val().trim().length < 1)
            $("#old_password_info").empty().append('请输入原密码（非空格）').removeClass("infoOk").addClass("infoError").show();
        if (newPwd.val().trim().length < 1)
            $("#password_info").empty().append('请输入新密码,至少6位字符(字母,数字,符号)').removeClass("infoOk").addClass("infoError").show();
        if (newPwdr.val().trim().length < 1)
            $("#confirm_password_info").empty().append('请输入确认密码(不含空格)').removeClass("infoOk").addClass("infoError").show();
        return false;
    }
    else if (newPwd.val().trim().length < 6 && newPwd.val().trim().length > 32) {
//        msgInfo("密码的长度为6位到32位!");
        return false;
    }
    else if (newPwd.val().trim().length != newPwdr.val().trim().length) {
//        msgInfo("确认密码跟原密码不等!");
        return false;
    }
    else {
        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"/PasswordModifySubmit",
            data:{"userName":userName, "oldPwd":oldPwd.val(), "newPwd":newPwd.val(), "newPwdr":newPwdr.val()},
            beforeSend:function (XMLHttpRequest, textStatus) {

            },
            success:function (msg) {
                if (msg.code > 0) {
                    msgInfo(msg.msg);
                    oldPwd.val("");
                    newPwd.val("");
                    newPwdr.val("");
                    $("#old_password_info").empty().hide();
                    $("#password_info").empty().hide();
                    $("#confirm_password_info").empty().hide();
                    pswLenStrengh(3, 'silver');
                }
                else {
                    $("#old_password_info").empty().append('原密码错误').removeClass("infoOk").addClass("infoError").show();
                }

            },
            error:function (msg) {
                msgError("未知请求或者不能连接上服务器");
            }
        });
        return false;
    }
}
var time = 8; //时间,秒 

function Redirect() {
    window.location = accountURL;
}
function enterGame1(url) {
    enterGame(url + "&status=1", function (m) {
        if (m.code == 0) {
//                window.open(url,"_blank");
            var _t = Date.parse(new Date());
            window.location.href = url + "&_t=" + _t;
        } else if (m.code > 0) {
            msgError(m.msg);
        } else {
            timer = setTimeout('Redirect()', time * 1000); //跳转
            msgError("您的Cookie失效，8秒后跳转，需要重新登陆！");
        }
    });
}

function showQuestionForm(showId) {
    var arrId = ["#questionStep1", "#questionStep2", "#questionMobile", "#questionR1", "#questionUnMobile"];
    $.each(arrId, function (i, n) {
        $(n).hide();
    });
    $(showId).show();
}

function questionNext(userName) {
    var q1 = $('#q1').val();
    var q2 = $('#q2').val();
    var q3 = $('#q3').val();
    var a1 = $('#a1').val();
    var a2 = $('#a2').val();
    var a3 = $('#a3').val();

    if (q1 == -1 || q2 == -1 || q3 == -1) {
        msgError("请选择额问题");
        return false;
    }
    if (q1 == q2 || q1 == q3 || q2 == q3) {
        msgError("问题不能重复");
        return false;
    }
    if (a1 == "" || a2 == "" || a3 == "") {
        msgError("答案不能为空");
        return false;
    }
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/Question",
        data:{"userName":userName, "q1":q1, "a1":a1, "q2":q2, "a2":a2, "q3":q3, "a3":a3, "type":"validate"},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                var questions = msg.object;
                $('#span_a1').html(questions[0].question);
                $('#span_a2').html(questions[1].question);
                $('#span_a3').html(questions[2].question);

                $('#a11').val(questions[0].answer);
                $('#a12').val(questions[1].answer);
                $('#a13').val(questions[2].answer);
                showQuestionForm("#questionStep2");
            }
            else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}

function updateQuestion(userName) {
    var q1 = $('#span_a1').html();
    var q2 = $('#span_a2').html();
    var q3 = $('#span_a3').html();
    var a11 = $('#a11').val();
    var a12 = $('#a12').val();
    var a13 = $('#a13').val();
    var ca1 = $('#ca1').val();
    var ca2 = $('#ca2').val();
    var ca3 = $('#ca3').val();
    if (a11 != ca1 || a12 != ca2 || a13 != ca3) {
        msgError("回复不正确，请重新输入.");
        return false;
    }
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/QuestionSubmit",
        data:{"userName":userName, "q1":q1, "a1":a11, "q2":q2, "a2":a12, "q3":q3, "a3":a13},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                msgInfo(msg.msg + "，三秒钟后将返回个人中心首页。");
                setTimeout("window.location.reload(true)", 3000);
            } else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}

function findQuestion(userName) {
    $('#ra1').val('');
    $('#ra2').val('');
    $('#ra3').val('');
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/Question",
        data:{"userName":userName, "type":"find"},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                var questions = msg.object;
                $('#span_q1').html(questions[0].question);
                $('#span_q2').html(questions[1].question);
                $('#span_q3').html(questions[2].question);
                showQuestionForm('#questionR1');
            }
            else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}

function checkFindQuestion(userName) {
    var q1 = $('#span_q1').html();
    var q2 = $('#span_q2').html();
    var q3 = $('#span_q3').html();
    var a1 = $('#ra1').val();
    var a2 = $('#ra2').val();
    var a3 = $('#ra3').val();

    if (a1 == "" || a2 == "" || a3 == "") {
        msgError("答案不能为空");
        return false;
    }

    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/Question",
        data:{"userName":userName, "q1":q1, "a1":a1, "q2":q2, "a2":a2, "q3":q3, "a3":a3, "type":"check"},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                showQuestionForm('#questionStep1');
            }
            else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}

function bindMobile() {
    $("#m_code").attr("kind", "bind");
    $("#mobile").removeAttr("readonly");
    showQuestionForm('#questionMobile');
}

function unBindMobile() {
    $("#m_code").attr("kind", "unbind");
    $("#mobile").attr("readonly", "readonly");
    showQuestionForm('#questionMobile');
}

function sendMobileCode(userName) {
    var _t = new Date().getTime();
    var mobile = $("#mobile").val();
    if (!isMobil(mobile)) {
        msgInfo("手机号码格式不正确，请重新输入");
        return;
    }
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/SendMobileCode",
        data:{"userName":userName, "mobile":mobile, "_t":_t},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                msgInfo(msg.msg);
                $("#questionMobile button").removeAttr("disabled");
                $("#m_code").attr("verify", "message_" + _t + "_" + userName);

                var tagSend = $("#questionMobile a");
                var tagClick = tagSend.attr("onclick");
                tagSend.removeAttr("onclick");
                var tDelay = 60;
                var fnDelay = function () {
                    if (tDelay < 0) {
                        tagSend.attr("onclick", tagClick);
                        tagSend.text("重新获取");
                    } else {
                        tagSend.text("(" + tDelay + "秒)重新获取");
                        tDelay--;
                        setTimeout(function () {
                            fnDelay();
                        }, 1000);
                    }
                };
                fnDelay();
            } else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
}

function mobileCodeSubmit(userName) {
    var v = $("#m_code").attr("verify");
    var k = $("#m_code").attr("kind");
    var c = $("#m_code").val();
    var mobile = $("#mobile").val();
    if (c == "" || v == null) {
        msgInfo("请输入验证码");
        return false;
    }
    if (!isMobil(mobile)) {
        msgInfo("手机号码格式不正确，请重新输入");
        return false;
    }
    $.ajax({
        type:"POST",
        dataType:"JSON",
        url:"/SendMobileCodeSubmit",
        data:{"userName":userName, "mobile":mobile, "k":k, "v":v, "c":c},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            if (msg.code > 0) {
                msgInfo(msg.msg + ",3秒钟后将返回个人中心。");
                $("#questionMobile button").attr("disabled", "disabled");
                $("#m_code").removeAttr("verify");
                setTimeout("window.location.reload(true)", 3000);
            } else msgError(msg.msg);
        },
        error:function (msg) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}


function CharMode(iN) {
    if (iN >= 48 && iN <= 57) //数字
        return 1;
    if (iN >= 65 && iN <= 90) //大写字母
        return 2;
    if (iN >= 97 && iN <= 122) //小写
        return 4;
    else
        return 8; //特殊字符
}
//bitTotal函数
//计算出当前密码当中一共有多少种模式
function bitTotal(num) {
    modes = 0;
    for (i = 0; i < 4; i++) {
        if (num & 1) modes++;
        num >>>= 1;
    }
    return modes;
}
//checkStrong函数
//返回密码的强度级别
function checkStrong(sPW) {
    if (sPW.length <= 4)
        return 0; //密码太短
    Modes = 0;
    for (i = 0; i < sPW.length; i++) {
//测试每一个字符的类别并统计一共有多少种模式.
        Modes |= CharMode(sPW.charCodeAt(i));
    }
    return bitTotal(Modes);
}
//pwStrength函数
//当用户放开键盘或密码输入框失去焦点时,根据不同的级别显示不同的颜色
function pwStrength(pwd) {

    S_level = checkStrong(pwd);
    switch (S_level) {
        case 0:
            Lcolor = Mcolor = Hcolor = O_color;
        case 1:
            Lcolor = L_color;
            Mcolor = Hcolor = O_color;
            break;
        case 2:
            Lcolor = Mcolor = M_color;
            Hcolor = O_color;
            break;
        default:
            Lcolor = Mcolor = Hcolor = H_color;
    }

    return;
}
