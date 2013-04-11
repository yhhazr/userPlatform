<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="rgtcontent">
    <h2 class="h2_title">密保设置</h2>
    <div class="moblebox">
        <div class="moblebg left"></div>
        <div class="mobletext left relative">
            <h5 class="h5_title">密保手机</h5>
            <c:if test="${!isSetMobile}">
                <p class="f14px">密保手机是以短信方式保护帐号密保工具，绑定后可以直接使用短信找回账号密码</p>
                <a class="boundmoble" href="javascript:forword('/bindMobile?type=bind','')">立即设置</a>
            </c:if>
            <c:if test="${isSetMobile}">
                <p class="f14px">手机号码：${showMobile} <br />使用状态：已绑定</p>
                <a class="boundmoble" href="javascript:forword('/bindMobile?type=unbind','')">解除绑定</a>
            </c:if>
        </div>
        <div class="blank"></div>
    </div>
    <h2 class="h2_title h3_title">功能说明：</h2>
    <div class="tit_explain"><p>如果您忘记了账号密码，可以通过密保手机验证重新设定帐号密码。</p></div>
    <h2 class="h2_title h3_title">常见问题：</h2>
    <div class="tit_explain">
        <h4 class="h4_title" >密保手机是否收费？</h4>
        <p>对密保手机不收取任何费用，当天只能获取五次短信验证码。</p>
        <h4 class="h4_title">密保手机如何找回帐号密码？</h4>
        <p>通过短信验证密保手机，验证成功后，可直接设置新密码。</p>
    </div>
    <div class="blank20"></div>
</div>

