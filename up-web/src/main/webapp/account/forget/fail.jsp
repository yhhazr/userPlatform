<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>重置密码</title>
    <link href="${imageDomainUrl}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/userhome.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/serviceCenter.css" rel="stylesheet" type="text/css" />
</head>

<body>
<jsp:include page="/common/topClient.jsp" flush="true"></jsp:include>

<div class="blank"></div>

<div class="serviceCenterMain">

    <div class="topline wbfb"></div>
    <div class="rgtcontent">
        <h2 class="h2_title">密保邮箱-<strong>找回密码</strong></h2>
        <div class="set_question set_question7">
            <div class="phoneFind">
                <div class="findSuccess">
                    <div class="submitSuccessfulBtn left"></div>
                    <div class="submitSuccessfulInfo left">
                        <h4>操作失败！</h4>
                        <h5>您的邮件链接已过期，请重新申请。</h5>
                        <!--
                        <div class="serviceWarning" style="margin:10px 0 0;"><span class="prompt"></span>申请成功过后请在24小时内使用链接。</div>
                        -->
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