<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>密码找回</title>
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
        <h2 class="h2_title">找回密码<strong></strong></h2>
        <div class="set_question set_question7">
            <div class="successfulAppeal">
                <ul>
                    <c:if test="${isSetQuestion}">
                        <li>
                            <a href="findPassWay?userName=${userObject.userName}&type=question" title="找回密码" class="right successfulAppealGo">找回密码</a>
                            <span class="left successfulAppealBtn successfulAppealBtn1"></span>
                            <p class="left successfulAppealTxt">
                                验证密保问题找回
                            </p>
                        </li>
                    </c:if>
                    <c:if test="${!isSetQuestion}">
                        <li>
                            <a href="javascript:void(0);" title="找回密码" class="right successfulAppealGo successfulAppealGo_shadow">尚未设置</a>
                            <span class="left successfulAppealBtn successfulAppealBtn1"></span>
                            <p class="left successfulAppealTxt">
                                尚未设置，不能通过密保重置密码
                            </p>
                        </li>
                    </c:if>
                    <c:if test="${isSetEmail}">
                        <li>
                            <a href="findPassWay?userName=${userObject.userName}&type=email" title="找回密码" class="right successfulAppealGo">找回密码</a>
                            <span class="left successfulAppealBtn successfulAppealBtn2"></span>
                            <p class="left successfulAppealTxt">
                                验证邮箱找回<br />密保邮箱：<em class="findPWTxt">${showEmail}</em>找回密码
                            </p>
                        </li>
                    </c:if>
                    <c:if test="${!isSetEmail}">
                        <li>
                            <a href="javascript:void(0);" title="找回密码" class="right successfulAppealGo successfulAppealGo_shadow">尚未设置</a>
                            <span class="left successfulAppealBtn successfulAppealBtn2"></span>
                            <p class="left successfulAppealTxt">
                                尚未设置，不能通过邮箱重置密码
                            </p>
                        </li>
                    </c:if>
                    <c:if test="${isSetMobile}">
                        <li>
                            <a href="findPassWay?userName=${userObject.userName}&type=mobile" title="找回密码" class="right successfulAppealGo">找回密码</a>
                            <span class="left successfulAppealBtn successfulAppealBtn3"></span>
                            <p class="left successfulAppealTxt">
                                密保手机发送短信找回密码<br />
                                密保手机：<em class="findPWTxt">${showMobile}</em>找回密码
                            </p>
                        </li>
                    </c:if>
                    <c:if test="${!isSetMobile}">
                        <li>
                            <a href="javascript:void(0);" title="找回密码" class="right successfulAppealGo successfulAppealGo_shadow">尚未设置</a>
                            <span class="left successfulAppealBtn successfulAppealBtn3"></span>
                            <p class="left successfulAppealTxt">
                                尚未设置，不能通过手机重置密码
                            </p>
                        </li>
                    </c:if>
                    <li>
                        <a href="findPassWay?userName=${userObject.userName}&type=appeal" title="申诉帐号" class="right successfulAppealGo">申诉帐号</a>
                        <span class="left successfulAppealBtn successfulAppealBtn4"></span>
                        <p class="left successfulAppealTxt">
                            帐号申诉找回密码<br />
                            通过提供历史资料和个人资料找回密码
                        </p>
                    </li>
                </ul>
            </div>
        </div><!--第一步end-->

        <div class="blank20"></div>
    </div>

    <div class="blank"></div>
</div>

<%@include file="/common/bottom.jsp"%>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery-1.7.1.js"></script>
</body>
</html>