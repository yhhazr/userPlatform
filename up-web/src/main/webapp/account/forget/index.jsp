<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>找回密码</title>
    <link href="${imageDomainUrl}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/userhome.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/serviceCenter.css" rel="stylesheet" type="text/css" />
</head>

<body>
<jsp:include page="/common/topClient.jsp" flush="true"></jsp:include>

<div class="blank"></div>

<div class="bread"><p><a href="/">首页</a>&gt;<a href="/forget.html">客服中心</a>&gt;<span>密码找回</span></p></div>

<div class="serviceCenterMain">

    <div class="topline wbfb"></div>
    <div class="rgtcontent">
        <h2 class="h2_title">找回密码<strong></strong></h2>
        <div class="set_question set_forget_login">
            <div class="findPWtips"><span class="prompt"></span>友情提示：请认真填写以下所有信息。</div>
            <form name="forgetForm" action="findPass" method="post">
                <input type="hidden" name="verify" value="">
                <div class="serviceList relative zoom">
                    <label>用户名：</label><input id="userName" name="userName" type="text" class="textinput" maxlength="20">
                    <div class="formerr empty" style="left:350px">
                        <div class="warning" style="width:220px"><span class="error"></span>至少6位字符（字母、数字、符号）</div>
                    </div>
                    <div class="formerr error" style="left:350px">
                        <div class="warning" style="width:220px"><span class="error"></span>用户名不存在</div>
                    </div>
                </div>
                <div class="blank"></div>
                <div class="serviceList relative zoom">
                    <label>验证码：</label><input id="verifyCode" name="code" type="text" class="textinput" maxlength="4">
                    <div class="formerr empty" style="left:350px">
                        <div class="warning" style="width:220px"><span class="error"></span>请输入4位验证码</div>
                    </div>
                    <div class="formerr error" style="left:350px">
                        <div class="warning" style="width:220px"><span class="error"></span>验证码错误</div>
                    </div>
                </div>
                <div class="blank"></div>
            </form>
            <div class="serviceList">
                <label></label>
                请输入图中字符，不区分大小写
            </div>
            <div class="serviceList" style="overflow:hidden">
                <label></label>
                <div class="verification">
                    <!--
                    <span><img id="verifyCodeImg" style="cursor:pointer;width:70px;height:40px;" src="" onClick="javascript:this.src='verifyCode?'+(new Date().getTime());" /></span>
                    -->
                    <span><img id="verifyCodeImg" style="cursor:pointer;padding-top:10px;" src="" onClick="javascript:loadVerifyCode();" /></span>
                    <a href="javascript:loadVerifyCode();" title="换一张">看不清？换一张</a>
                </div>
                <div class="blank"></div>
            </div>
            <div class="blank"></div>
            <div class="serviceList">
                <label></label>
                <a style="cursor:pointer;" title="下一步" class="nextBtn">下一步</a>
            </div>
        </div><!--第一步end-->

        <div class="blank20"></div>
    </div>

    <div class="blank"></div>
</div>

<%@include file="/common/bottom.jsp"%>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery-1.7.1.js"></script>
<script type="text/javascript">
    $(function(){
        $(".nextBtn").click(function(){
            var name = $("#userName");
            var code = $("#verifyCode");
            var verify = $("input[name='verify']").val();
            if (name.val().length < 6) {
                name.siblings(".formerr").hide().siblings(".empty").show();
                return false;
            }
            if (code.val().length < 4) {
                code.siblings(".formerr").hide().siblings(".empty").show();
                return false;
            }
            $(".formerr").hide();
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/CheckVerifyCode",
                data:{"userName":name.val(), "verifyCode":verify, "code":code.val()},
                success:function (msg) {
                    if (msg.code == 0) {
                        //location.href = "/findPass?userName=" + name.val() + "&verify=" + verify + "&code=" + code.val();
                        $("form").submit();
                    } else if (msg.code == -1) {
                        code.siblings(".formerr").hide().siblings(".error").show();
                        loadVerifyCode();
                    } else {
                        name.siblings(".formerr").hide().siblings(".error").show();
                    }
                }
            });
            //document.forgetForm.submit();
        });
        // 聚焦隐藏错误提示信息
        $(".set_forget_login .textinput").focus(function(){
            $(this).siblings(".formerr").hide();
        });

        loadVerifyCode();
    });

    function loadVerifyCode() {
        var _t = new Date().getTime();
        $("#verifyCodeImg").attr("src", '/VerifyCode?_t=' + _t);
        $("input[name='verify']").val( _t);
    }
</script>
</body>
</html>