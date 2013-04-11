<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">密保设置</h2>
    <div class="moblebox">
        <div class="moblebg questionbg left"></div>
        <div class="mobletext left relative">
            <h5 class="h5_title">密保问题</h5>
            <c:if test="${!isSetQuestion}">
                <p class="f14px">帐号密码，快速找回</p>
                <a class="boundmoble" href="javascript:forword('/bindQuestion?type=bind','')">立即设置</a>
            </c:if>
            <c:if test="${isSetQuestion}">
                <p class="f14px">使用状态：正常使用<br /> 绑定状态：已绑定</p>
                <a class="boundmoble" href="javascript:forword('/bindQuestion?type=bind','')">更换</a>
            </c:if>
        </div>
        <div class="blank"></div>
    </div>
    <h2 class="h2_title h3_title">功能说明：</h2>
    <div class="tit_explain"><p>由用户选定的3个问题及对应答案组成，专门用于找回密码和设置其他密保。主要包含用户的个人私有信息，包括：父亲的名字、配偶的姓名、学号或工号等。（注：第七大道公司不会泄露用户的隐私。）</p></div>
    <h2 class="h2_title h3_title">常见问题：</h2>
    <div class="tit_explain">
        <h4 class="h4_title" >密保问题如何防止遗忘？</h4>
        <p>由于密保问题是由您选定的3个问题和答案组成，为了防止遗忘，建议您一周验证记忆一次。</p>
        <h4 class="h4_title">密保问题如何用于找回帐号？</h4>
        <p>如果您的账号密码忘记，您可以直接通过验证密保问题和答案，重新设置新密码，那么帐号成功找回。</p>
    </div>
    <div class="blank20"></div>
</div>