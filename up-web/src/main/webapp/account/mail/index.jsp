<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">密保设置</h2>
    <div class="moblebox">
        <div class="moblebg emailbg left"></div>
        <div class="mobletext left relative">
            <h5 class="h5_title">密保邮箱</h5>
            <c:if test="${!isSetEmail}">
                <p class="f14px">设置密保邮箱，快捷、方便、安全，找回帐号</p>
                <a class="boundmoble" href="javascript:forword('/bindEmail?type=bind','')">立即设置</a></c:if>
            <c:if test="${isSetEmail}">
                <p class="f14px">使用状态：正常使用<br />
                    绑定邮箱：<a href="javascript:void(0)"><strong>${showEmail}</strong></a></p>
                <a class="boundmoble" href="javascript:forword('/bindEmail?type=bind','')">解除绑定</a>
            </c:if>
        </div>
        <div class="blank"></div>
    </div>
    <h2 class="h2_title h3_title">功能说明：</h2>
    <div class="tit_explain"><p>如果您忘了帐号密码，可以通过密保邮箱验证，重新设定帐号密码。</p></div>
    <h2 class="h2_title h3_title">常见问题：</h2>
    <div class="tit_explain">
        <h4 class="h4_title" >怎样更换密保邮箱？</h4>
        <p>更换密保邮箱，需验证现有邮箱，成功验证解绑后，即可更换新邮箱。</p>
        <h4 class="h4_title">密保邮箱如何找回帐号密码？</h4>
        <p>通过密保邮箱发送验证邮件，验证成功后，可直接设置新密码。</p>
    </div>
    <div class="blank20"></div>
</div>


