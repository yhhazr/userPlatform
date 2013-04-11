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
        <h2 class="h2_title">密保邮箱-<strong>找回密码</strong></h2>
        <div class="set_question set_question7">
            <div class="phoneFind">
                <div class="findSuccess">
                    <div class="submitSuccessfulBtn left"></div>
                    <div class="submitSuccessfulInfo left">
                        <h4>您的申请已提交成功！</h4>
                        <h5>请查看您的邮箱<em>${showEmail}</em>邮箱。</h5>
                        <div class="serviceWarning" style="margin:10px 0 0;"><span class="prompt"></span>发送邮件可能比较缓慢，请耐心稍等5-10分钟。</div>
                        <div class="blank20"></div>
                        <div><a class="nextBtn" style="margin:0;" title="返回首页" href="javascript:logout();">返回首页</a></div>
                    </div>
                </div>
            </div>
        </div><!--第一步end-->

        <div class="blank20"></div>
    </div>

    <div class="blank"></div>
</div>

<%@include file="/common/bottom.jsp"%>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery-1.7.1.js"></script>
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
            if(passvalue.length<6){
                $(this).siblings(".formerr").hide();
                $(this).siblings(".formerr2_1").show();
            }else if(passvalueChar==1 && (5<passvalue.length&&passvalue.length<10)){
                $(this).siblings(".formerr").hide();
                $(this).siblings(".formerr2_2").show();

            }else{
                $(this).siblings(".formerr").hide();
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
            if(pass.val().length < 6 ) {
                pass.siblings(".formerr").hide().siblings(".formerr1").show();
                return false;
            }
            if(pass1.val().length < 6 ) {
                pass1.siblings(".formerr").hide().siblings(".formerr2").show();
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


    function checkstr(str){
        if(str>=48&&str<=57){//数字
            return 1;
        }else if(str>=65&&str<=90){//大写字母
            return 2;
        }else if(str>=97&&str<=122) {//小写字母
            return 3;
        }else{//特殊字符
            return 4;
        }
    }

    function check(string){
        l_num=0;
        var sz=false,dzm=false,xzm=false,ts=false;
        for(i=0;i<string.length;i++){
            asc = checkstr(string.charCodeAt(i));
            if(asc==1 && sz==false){
                l_num+=1;
                sz=true;
            }
            if(asc==3 && xzm==false){
                l_num+=2;
                xzm=true;
            }
            if(asc==2&& dzm==false){
                l_num+=2;
                dzm =true;
            }
            if(asc==4&&ts==false){
                l_num+=10;
                ts=true;
            }
        }
        return l_num;
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