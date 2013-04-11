<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>个人中心</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<br/>

<div>
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <th>#</th>
            <th class="yellow">游戏区编号</th>
            <th class="blue">游戏区号</th>
            <th class="green">游戏区名称</th>
            <th class="green">状态</th>
            <th class="green">开放时间</th>
            <th class="green">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="game" items="${list}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${game.id}</td>
                <td>${game.serverNo}</td>
                <td>${game.serverName}</td>
                <td>${game.serverStatus}</td>
                <td>${game.openingTime}</td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>