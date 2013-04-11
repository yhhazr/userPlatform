<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-17
  Time: 上午11:00
  显示整体的安全信息
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>帐号安全信息</h1>

<div>
    <div id="safeInfoDiv">
        <c:if test="${safeInfo}">

        </c:if>
    </div>
    <div id="pswDiv"></div>
    <div id="emailDiv"></div>
    <div id="phoneDiv"></div>
    <div id="certifyDiv"></div>
    <div id="userInfoDiv"></div>
    <div id="questionDiv"></div>
</div>
</body>
</html>
