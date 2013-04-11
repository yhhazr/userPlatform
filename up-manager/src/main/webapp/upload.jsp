<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-22
  Time: 上午11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>common-fileUpload上传helloWorld</title>
</head>
<body>
<form action="faq?action=upload" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile" size="50">
    <br>
    <input type="submit" value="提交">
</form>
</body>
</html>
