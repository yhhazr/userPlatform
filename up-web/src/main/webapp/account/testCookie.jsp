<%-- 
    Document   : testCookie
    Created on : 2012-5-9, 20:17:51
    Author     : leo.liao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
</head>
<body>
<h1>Hello World!</h1>
<a href="${hostDomain}/accountcenter.html">个人中心</a>
<a href="${hostDomain}/bindEmail" target="_blank">密保邮箱</a>
<a href="${hostDomain}/bindEmail?type=info" target="_blank">邮箱绑定</a>
<a href="${hostDomain}/bindMobile" target="_blank">手机绑定</a>
<a href="${hostDomain}/bindQuestion" target="_blank">密保问题</a>
</body>
</html>
