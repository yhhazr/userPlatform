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
</head>

<body>
<jsp:include page="/common/topClient.jsp" flush="true"></jsp:include>

<div class="blank"></div>

<div class="bread"><p><a href="/">首页</a>&gt;<a href="/forget.html">客服中心</a>&gt;<span>找回密码</span></p></div>

<div class="serviceCenterMain">

    <div class="topline wbfb"></div>
    <div class="rgtcontent">
        <div class="blank20"></div>
        <div class="set_question finpassbox finpassbox_1">
            <div class="yiwang">找回密码关系到您的帐号安全，请您正确填写验证密保</div>
            <div class="blank20"></div>
            <div class="blank" style="height:40px;"></div>
            <div class="queslist">
                <label>问题一：</label>
                <span>${list[0].question}</span>
            </div>
            <div class="queslist relative">
                <label class="da_label">答案：</label><input type="text" class="textinput" />
                <div class="formerr formerr_question">
                    <div class="warning" style="width: 120px;"><span class="error"></span>请输入答案</div>
                </div>
            </div>
            <div class="queslist">
                <label>问题二：</label>
                <span>${list[1].question}</span>
            </div>
            <div class="queslist relative">
                <label class="da_label">答案：</label><input type="text" class="textinput" />
                <div class="formerr formerr_question">
                    <div class="warning" style="width: 120px;"><span class="error"></span>请输入答案</div>
                </div>
            </div>
            <div class="queslist ">
                <label>问题三：</label>
                <span>${list[2].question}</span>
            </div>
            <div class="queslist relative">
                <label class="da_label">答案：</label><input type="text" class="textinput" />
                <div class="formerr formerr_question">
                    <div class="warning" style="width: 120px;"><span class="error"></span>请输入答案</div>
                </div>
            </div>
            <div class="blank"></div>
            <a href="javascript:history.back();" class="theprev left theprev_1" style="margin: 20px 0 0 340px;">上一步</a>
            <a href="javascript:void(0)" title="下一步" class="thenext left thenext_7" style="margin:20px 0 0 30px;">下一步</a>
            <div class="blank20"></div>
        </div><!--第一步end-->

        <div class="set_question finpassbox finpassbox_2 none">
            <div class="yiwang"><span style="color:#333">为了更好的保护您的帐号安全，避免您的游戏账号损失，建议您设置密码时：</span><br />1.先将电脑全面杀毒，再设置新密码<br />2.不同的帐号使用不同的密码，不要设置和其它网站相同的密码。</div>
            <div class="blank20"></div>
            <div class="editpass">
                <div class="relative editpass_input">
                    <label class="red">*</label>
                    <label>新 密 码：</label>
                    <input type="password" class="textinput" id="newpassword" maxlength="20" />
                    <div class="pass_lv"></div>
                    <div class="pass_lv2"></div>
                    <div class="formerr formerr1" style="width: 207px;">
                        <div class="warning"><span class="prompt"></span>由6-20位字母、数字、符号组成</div>
                    </div>
                    <div class="formerr formerr2 formerr2_1" style="width: 207px;">
                        <div class="warning"><span class="error"></span>由6-20位字母、数字、符号组成</div>
                    </div>
                    <div class="formerr formerr2 formerr2_2" style="width: 207px;">
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
                    <div class="formerr formerr1" style="width: 207px;">
                        <div class="warning"><span class="prompt"></span>请再次输入密码</div>
                    </div>
                    <div class="formerr formerr2" style="width: 207px;">
                        <div class="warning"><span class="error"></span>请输入和上面相同的密码</div>
                    </div>
                    <div class="formerr formerr3"><span class="ok"></span></div>
                </div>
                <div class="blank"></div>
                <a href="javascript:void(0)" class="theprev left theprev_1" style="margin: 20px 0 0 40px;">上一步</a>
                <a href="javascript:void(0)" title="下一步" class="thenext left thenext_8" style="margin:20px 0 0 30px;">下一步</a>
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
    var questionArray = ["${list[0].question}","${list[1].question}","${list[2].question}"];
    var answerArray = [];
    $(function(){
        $(".thenext_7").click(function(){
            answerArray = [];
            var anArray = $(".finpassbox_1 .textinput");
            for(i=0;i<anArray.length;i++){
                var txtAnswer = anArray.eq(i);
                if(txtAnswer.val() == '') {
                    txtAnswer.siblings(".formerr").show();
                    return false;
                }
                answerArray.push(txtAnswer.val());
            }
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/findPassQuestion?" + new Date().getTime(),
                data:{userName:"${param.userName}", q1:questionArray[0], q2:questionArray[1], q3:questionArray[2], a1:answerArray[0], a2:answerArray[1], a3:answerArray[2]},
                async:false,
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".finpassbox_1").hide();
                        $(".finpassbox_2").show();
                    } else {
                        alert(msg.msg);
                    }
                }
            });
        });

        $('.textinput').focus(function(){
            $(this).siblings(".formerr").hide();
        });
        $("#newpassword").blur(function(){
            var passvalue = $(this).val();
            var passvalueChar = check(passvalue);
            $(this).siblings(".formerr").hide();
            if(passvalue.length<6){
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
            if($(this).val()!=passvalue){
                $(this).siblings(".formerr").hide();
                $(this).siblings(".formerr2").show();
            }else{
                $(this).siblings(".formerr").hide();
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
                url:"/findPassQuestion?" + new Date().getTime(),
                data:{userName:"${param.userName}", method:"modify", newPass: pass.val(),q1:questionArray[0], q2:questionArray[1], q3:questionArray[2],
                    a1:answerArray[0], a2:answerArray[1], a3:answerArray[2]},
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