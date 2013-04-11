<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>密保问题</title>
    <link href="${imageDomainUrl}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/userhome.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/serviceCenter.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
    .formerr{ _width:140px;}
    </style>
</head>

<body>
<jsp:include page="/common/topClient.jsp" flush="true"></jsp:include>

<div class="blank"></div>

<div class="bread"><p><a href="/">首页</a>&gt;<a href="/forget.html">客服中心</a>&gt;<span>找回密码</span></p></div>

<div class="serviceCenterMain">

    <div class="topline wbfb"></div>
    <div class="rgtcontent">
        <h2 class="h2_title">密保手机-<strong>找回密码</strong></h2>
        <div class="set_question set_mobile1">
            <div class="phoneFind">
                <div class="serviceList zoom" style="margin-left:150px;">
                    <label>密保手机：</label><input type="text" value="${showMobile}" class="textinput" readonly="readonly">
                </div>
                <div class="serviceList zoom">
                    <a id="sendMessage" href="javascript:void(0);" class="getCode" style="margin-left: 92px">获取短信验证码</a>
                    <span class="left" style="margin-top:9px;">当天只能获取五次</span></div>
                <div class="blank"></div>
                <div class="serviceList relative zoom" style="margin-left:150px;">
                    <label>验证码：</label><input id="code" type="text" class="textinput" maxlength="6">
                    <div class="formerr error_required" style="left:350px; width:140px;">
                        <div class="warning"><span class="error"></span>请获取验证码</div>
                    </div>
                    <div class="formerr error_empty" style="left:350px; width:140px;">
                        <div class="warning"><span class="error"></span>请输入验证码</div>
                    </div>
                    <div class="formerr error_invalid" style="left:350px; width:140px;">
                        <div class="warning"><span class="error"></span>验证码错误</div>
                    </div>
                </div>
                <div class="blank"></div>
                <div class="serviceList">
                    <a href="javascript:history.back();" class="prevBtn" style="margin:0 0 0 92px;">上一步</a>
                    <a href="javascript:void(0);" title="下一步" class="nextBtn">下一步</a>
                </div>
            </div>
        </div><!--第一步end-->
        <div class="set_question set_mobile2 none">
            <div class="yiwang"><span style="color:#333">为了更好的保护您的帐号安全，避免您的游戏账号损失，建议您设置密码时：</span><br />1.先将电脑全面杀毒，再设置新密码<br />2.不同的帐号使用不同的密码，不要设置和其它网站相同的密码。</div>
            <div class="blank20"></div>
            <div class="editpass">
                <div class="relative editpass_input">
                    <label class="red">*</label>
                    <label>新 密 码：</label>
                    <input type="password" class="textinput" id="newpassword" maxlength="20" />
                    <div class="pass_lv"></div>
                    <div class="pass_lv2"></div>
                    <div class="formerr formerr1">
                        <div class="warning" style="width: 250px;"><span class="prompt"></span>6-20数字字母符号,不能为9个以下纯数字</div>
                    </div>
                    <div class="formerr formerr2 formerr2_1">
                        <div class="warning" style="width: 250px;"><span class="error"></span>6-20数字字母符号,不能为9个以下纯数字</div>
                    </div>
                    <div class="formerr formerr2 formerr2_2">
                        <div class="warning" style="width: 250px;"><span class="error"></span>密码不能由低于9位纯数字组成</div>
                    </div>
                    <div class="formerr formerr3"><span class="ok"></span></div>
                    <div class="pass_lv"></div>
                    <div class="pass_lv2"></div>
                </div>
                <div class="relative editpass_input">
                    <label class="red">*</label>
                    <label>确认密码：</label>
                    <input type="password" class="textinput" id="newpassword1" maxlength="20">
                    <div class="formerr formerr1">
                        <div class="warning"><span class="prompt"></span>请再次输入密码</div>
                    </div>
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>请输入和上面相同的密码</div>
                    </div>
                    <div class="formerr formerr3"><span class="ok"></span></div>
                </div>
                <div class="blank"></div>
                <a id="btnConfirm" href="javascript:void(0)" title="确定" class="thenext left thenext_8" style="margin:20px 0 0 30px;">确定</a>
            </div>
        </div><!--第二步end-->
        <div class="set_question set_mobile3 none">
            <div class="phoneFind">
                <div class="findSuccess">
                    <div class="submitSuccessfulBtn left"></div>
                    <div class="submitSuccessfulInfo left">
                        <h4>新密码设置成功</h4>
                        <h5>为了增强账号安全，建议您返回首页了解更多安全建议。</h5>
                        <div class="blank20"></div>
                        <div><a class="nextBtn" style="margin:0;" title="返回首页" href="javascript:logout();">返回首页</a></div>
                    </div>
                </div>
            </div>
        </div><!--第二步end-->

        <div class="blank20"></div>
    </div>

    <div class="blank"></div>
</div>



<%@include file="/common/bottom.jsp"%>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/resetpass.js"></script>
<script type="text/javascript">
    $(function(){
        var time = 0;
        var code = '';
        var tagSend = $("#sendMessage");
        tagSend.click(sendMobileCode);

        $(".nextBtn").click(function(){
            var inputCode = $("#code");
            if(time == 0){
                inputCode.siblings(".error_required").show();
                return;
            }
            if(inputCode.val() == '') {
                inputCode.siblings(".error_empty").show();
                return;
            }
            code = inputCode.val();
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/resetPassMobile?" + new Date().getTime(),
                data:{user:"${param.userName}", code: code, time:time, type:"reset"},
                async:false,
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".set_mobile1").hide();
                        $(".set_mobile2").show();
                    } else {
                        inputCode.siblings(".error_invalid").show();
                        return;
                    }
                }
            });
        });

        $("#btnConfirm").click(function(){
            var pass = $("#newpassword");
            var pass1 = $("#newpassword1");
            var passvalueChar = check(pass.val());
            if(pass.val().length < 6 || containSpace(pass.val())) {
                pass.siblings(".formerr").hide().siblings(".formerr1").show();
                return false;
            }
            if(passvalueChar==1 && (5<pass.val().length&&pass.val().length<9)) {
                pass.siblings(".formerr").hide().siblings(".formerr2_1").show();
                return false;
            }
            if(pass.val() != pass1.val()){
                pass1.siblings(".formerr").hide().siblings(".formerr2").show();
                return false;
            }
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/resetPassMobileSubmit?" + new Date().getTime(),
                data:{user:"${param.userName}", code:code, time:time, newPass:pass.val(), type:"reset"},
                async:false,
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".set_mobile2").hide();
                        $(".set_mobile3").show();
                    } else {
                        alert(msg.msg);
                    }
                }
            });
        });

        function sendMobileCode(){
            tagSend.unbind("click");
            var tDelay = 60;
            var fnDelay = function(){
                if (tDelay < 0) {
                    if($(".set_mobile1 .serviceList").eq(1).find("span.left").text().indexOf("0") == -1){
                        tagSend.bind("click", sendMobileCode);
                    }
                    tagSend.text("获取短信验证码");
                } else {
                    tagSend.text("(" + tDelay + "秒)重新获取");
                    tDelay --;
                    setTimeout(function(){fnDelay();},1000);
                }
            };
            fnDelay();

            time = new Date().getTime();
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/sendMobileCode?" + new Date().getTime(),
                data:{user:"${param.userName}", mobile:"${showMobile}", time:time, type:"reset"},
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".set_mobile1 .serviceList").eq(1).find("span.left").html(msg.msg);
                    } else {
                        $(".set_mobile1 .serviceList").eq(1).find("span.left").html(msg.msg);
                    }
                }
            });
        }

        $(".set_mobile1 .textinput").focus(function(){
            $(this).siblings(".formerr").hide();
        });

        $('.textinput').focus(function(){
            $(this).siblings(".formerr").hide();
        });
        $("#newpassword").blur(function(){
            var passvalue = $(this).val();
            var passvalueChar = check(passvalue);
            $(this).siblings(".formerr").hide();
            if(passvalue.length<6|| containSpace(passvalue)){
                $(this).siblings(".formerr2_1").show();
            }else if(passvalueChar==1 && (5<passvalue.length&&passvalue.length<9)){
                $(this).siblings(".formerr2_2").show();
            }else{
                $(this).siblings(".formerr3").show();
            }

        })
        $("#newpassword").keyup(function(){
            //判断密码强度
            var passvalue = $(this).val();
            if(passvalue.length>5){
                var passvalueChar = check(passvalue);
                if(passvalueChar==1){
                    $(".pass_lv2").css({"width":"5px","left":"280px"})
                }else if(passvalueChar==2){
                    $(".pass_lv2").css({"width":"10px","left":"280px"})
                }else if(passvalueChar==10){
                    $(".pass_lv2").css({"width":"15px","left":"280px"})
                }else if(passvalueChar==3){
                    $(".pass_lv2").css({"width":"20px","left":"280px"})
                }else if(passvalueChar==11){
                    $(".pass_lv2").css({"width":"25px","left":"280px"})
                }else if(passvalueChar==12){
                    $(".pass_lv2").css({"width":"30px","left":"280px"})
                }else if(passvalueChar==13){
                    $(".pass_lv2").css({"width":"35px","left":"280px"})
                };

            }

        });
        $("#newpassword1").blur(function(){
            var passvalue = $("#newpassword").val();
            $(this).siblings(".formerr").hide();
            if($(this).val()!=passvalue){
                $(this).siblings(".formerr2").show();
            }else{
                $(this).siblings(".formerr3").show();
            }
        });
    });

    function isMobil(s) {
        // /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/
        var p = /^0?1[3|4|5|8][0-9]{9}$/;
        if (!p.exec(s)) {
            return false;
        }
        return true;
    }
    function logout() {
        $.ajax({
            type:"GET",
            dataType:"text",
            url:"/logout",
            data:{action:"logout"},
            success:function (msg) {
                window.location.href = "${homePageUrl}";
            }
        });
    }
</script>
</body>
</html>