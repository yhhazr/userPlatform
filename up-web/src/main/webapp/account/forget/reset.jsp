<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>密码重置</title>
    <link href="${imageDomainUrl}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/userhome.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/serviceCenter.css" rel="stylesheet" type="text/css" />
</head>

<body>
<jsp:include page="/common/topClient.jsp" flush="true"></jsp:include>

<div class="blank"></div>

<div class="bread"><p><a href="/">首页</a>&gt;<a href="/forget.html">客服中心</a>&gt;<span>密码重置</span></p></div>

<div class="serviceCenterMain">

    <div class="topline wbfb"></div>
    <div class="rgtcontent">
        <div class="blank20"></div>

        <div class="set_question finpassbox finpassbox_2">
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
                        <div class="warning"><span class="error"></span>密码不能由低于9位纯数字组成</div>
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
                <a href="javascript:void(0)" title="确定" class="thenext left thenext_8" style="margin:20px 0 0 30px;">确定</a>
            </div>
        </div> <!--第二步end-->
        <div class="set_question finpassbox none finpassbox_3">
            <div class="set_mobleok">
                <div class="anquandun emaildun"></div>
                <strong class="f16px left setsj_ok">密码修改成功！</strong>
                <div class="blank"></div>
                <strong class="f14px left setsj_ok" style="margin:10px 0 0 115px; _display: inline;font-weight:normal">为了加强帐号安全，建议您返回首页了解更多安全建议。</strong>
                <div class="blank"></div>
                <div class="edit_email">
                    <a href="javascript:logout();" title="返回首页" class="editemail_a"></a>
                </div>
            </div>
            <div class="blank" style="height:40px;"></div>
            <div class="blank" style="height:40px;"></div>
        </div><!--第3步end-->
        <div class="blank"></div>
    </div>
</div>

<%@include file="/common/bottom.jsp"%>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/resetpass.js"></script>
<script type="text/javascript">
    $(function(){
        $('.textinput').focus(function(){
            $(this).siblings(".formerr").hide();
        });
        $("#newpassword").blur(function(){
            var passvalue = $(this).val();
            var passvalueChar = check(passvalue);
            $(this).siblings(".formerr").hide();
            if(passvalue.length<6 || containSpace(passvalue)){
                $(this).siblings(".formerr2_1").show();
            }else if(passvalueChar==1 && (5<passvalue.length&&passvalue.length<9)){
                $(this).siblings(".formerr2_2").show();
            }else{
                $(this).siblings(".formerr3").show();
            }
        });

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
            } else if($(this).val() == ''){
                $(this).siblings(".formerr1").show();
            }else{
                $(this).siblings(".formerr3").show();
            }
        });
        $(".theprev_1").click(function(){
            $(".finpassbox_2").hide();
            $(".finpassbox_1").show();
        })

        $(".thenext_8").click(function(){
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
                url:"/resetPassSubmit?" + new Date().getTime(),
                data:{user:"${param.user}", code:"${param.code}", time:"${param.time}", key:"${param.key}", newPass:pass.val()},
                async:false,
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".finpassbox_2").hide();
                        $(".finpassbox_3").show();
                    } else {
                        alert(msg.msg);
                    }
                }
            });
        });
    })
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