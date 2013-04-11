<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 13-2-19
  Time: 下午7:27
  防沉迷认证页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>个人中心_第七大道</title>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link href="css/home.css" rel="stylesheet" type="text/css"/>
    <link href="css/showcss.css" rel="stylesheet" type="text/css"/>
</head>
<body style="background-position:0 -26px">
<jsp:include page="common/topClient2.jsp" flush="false"></jsp:include>
<div class="blank10"></div>
<div class="register register_back">
    <div class="createUser">
        <div class="createImg createImg2"></div>
        <div class="createDiv createDiv2">
            <strong>防沉迷认证：</strong>
            <span>根据2010年8月1日实施的《网络游戏管理暂行办法》，网络游戏用户需使用有效身份证件进行实名注册。为保证流畅游戏体验，享受健康游戏生活，请广大第七大道游戏的玩家尽快实名注册。</span>
        </div>
    </div>
    <c:if test="${fcmInfo.status!='incomplete'}">
        <div class="formbox">
            <div class="formlist">
                <div class="formtext" style="width:auto; text-align: left;"><cite>*</cite>真实姓名：</div>
                <div class="forminput" style="line-height: 34px;"><label>${realName}</label></div>
            </div>
            <div class="formlist">
                <div class="formtext" style="width:auto; text-align: left;"><cite>*</cite>身份证号码：</div>
                <div class="forminput relative" style="line-height: 34px;">
                    <label> ${fcmInfo.cardId}</label>
                </div>

            </div>
            <div class="formlist" style="height:auto;">
                <c:if test="${fcmInfo.status=='yes'}">
                    <p style="color: red;">
                    年龄：未满18岁(受防沉迷系统保护)<br/>
                    <em class="red">您未满18岁，根据政策要求，会受到防沉迷系统的限制：</em><br/>
                    1.游戏过程，会提示您的累计在线时间。<br/>
                    2.累计游戏时间超过3小时，游戏收益(金钱、经验等)减半。<br/>
                    3.累计游戏时间超过5小时，游戏收益为0。<br/>
                    </p>
                </c:if>
                <c:if test="${fcmInfo.status=='no'}">
                    <p style="color: green;">
                    您已经注册过防沉迷信息了，感谢你的使用！<br/>
                    </p>
                </c:if>
            </div>
        </div>
    </c:if>
    <c:if test="${fcmInfo.status =='incomplete'}">
        <div class="formbox">
            <div class="formlist">
                <div class="formtext"><cite>*</cite>真实姓名：</div>
                <div class="forminput"><input type="text" class="textinput" id="rName" maxlength="8"/></div>
                <div class="formerr formerr1">
                    <div class="warning" style="width:275px;"><span class="prompt"></span>请输入您的真实姓名，例如：李四</div>
                </div>
                <div class="formerr formerr2">
                    <div class="warning" style="width:275px;"><span class="error"></span>请输入您的中文姓名，例如：李四</div>
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
                    <input type="hidden" id="submitOk" value="no"/>
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
        <div class="blank10"></div>

        <div class="now_register" style="margin-left:280px;">
            <a id="registerFcmRz" class="preventBtn prevent_submit" href="javascript:void(0)">提&nbsp;&nbsp;交</a>
        </div>
    </c:if>
    <div class="blank" style="height:60px;"></div>
</div>

<div class="baseAlert none">
    <div class="preventOk none">
        <div class="preOkBtn"></div>
        <div class="preventRig">
            信息提交成功
        </div>
        <div class="blank"></div>
        <a class="preventBtn loginAgain" href="/fcm_rz.html">重新登录</a>
    </div>
    <div class="preventOk18 none">
        <div class="preOkBtn"></div>
        <div class="preventRig">
            身份证号：<span id="icn_info">430********554</span> <br/>
            年龄：未满18岁(受防沉迷系统保护)<br/>
            <em class="red"><span id="name_info">张三</span> 您未满18岁，根据政策要求，会受到防沉迷系统的限制：</em><br/>
            1.游戏过程，会提示您的累计在线时间。<br/>
            2.累计游戏时间超过3小时，游戏收益(金钱、经验等)减半。<br/>
            3.累计游戏时间超过5小时，游戏收益为0。<br/>

            <div class="blank"></div>
            <a class="preventBtn yesPreSubmit" href="javascript:submitFor18();">确认提交</a>
            <a class="preventBtn noPreSubmit" href="/fcm_rz.html">修改资料</a>
        </div>
    </div>
    <div class="preventError none">
        <div class="preErrorBtn"></div>
        <div class="preventRig">
            您的防沉迷信息提交失败，请重试
        </div>
        <div class="blank"></div>
        <a class="preventBtn editAgain" href="/fcm_rz.html">确&nbsp;&nbsp;定</a>
    </div>
</div>
<div class="lockScreen"></div>


<div class="blank" style="height:60px;"></div>

<jsp:include page="./common/bottom.jsp" flush="false"></jsp:include>
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="scripts/common.js"></script>
<script type="text/javascript">
    //防沉迷注册js
    $(function () {
        $("#registerFcmRz").bind("click",fcmRegister);
        $(".prevent_submit").bind("click", fcmRegister);
        $('#rName').focusout(function () {
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
        }).focusin(function () { //真实姓名
                    $('#rName').addClass("textinput_hover");
                    $(this).parent().siblings(".formerr").hide();
                    $(this).parent().siblings(".formerr1").show();
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
                            fcmRegister();
                        }
                    }
                });


        $("input").bind("keydown", function (e) {
            var key = e.which;
            if (key == 13) {
                e.preventDefault();
                var _rName = $.trim($("#rName").val());
                var _icn = $.trim($("#uIcn").val());
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
                            }  }}}
            }
        });
    });
    function fcmRegister() {
        var _rName = $.trim($("#rName").val());
        var _icn = $.trim($("#uIcn").val());
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
                            url:'/fcmrz',
                            type:'post',
                            data:{ "rName":_rName, "uIcn":_icn, "submitStr":$("#submitOk").val()},
                            dataType:'json',
                            success:function (data) {
                                var code = data.code;
                                var msg = data.msg;
                                if (code == '200') {
                                    $(".baseAlert").show();
                                    $(".preventOk").show();
                                    lockScreen(true);
                                }
                                if (code == '204') {
                                    lockScreen(true);
                                    $(".baseAlert").show();
                                    $(".preventError").show();
                                }
                                if (code == '206') {
                                    $('#rName').focus();
                                    $('#rName').parent().siblings(".formerr").hide();
                                    $('#rName').parent().siblings(".formerr4").empty().
                                            addClass("warning").append("<span class='error'></span>" + msg).show();
                                }
                                if (code == '207') {
                                    $('#uIcn').focus();
                                    $('#uIcn').parent().siblings(".formerr").hide();
                                    $('#uIcn').parent().siblings(".formerr4").empty().
                                            addClass("warning").append("<span class='error'></span>" + msg).show();
                                }
                                if (code == '208') {
                                    lockScreen(true);
                                    $("#icn_info").empty().html(data.object._icn);
                                    $("#name_info").empty().html(data.object._rName);
                                    $(".baseAlert").show();
                                    $(".preventOk18").show();
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

    function submitFor18() {
        $("#submitOk").val("yes");
        $(".preventOk18").hide();
        fcmRegister();
    }
</script>

</body>
</html>







