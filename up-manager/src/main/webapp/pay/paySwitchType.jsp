<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>支付类型管理</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/bootstrap-responsive.min.css" rel="stylesheet">
    <style>
        .nav a { font-size: 14px;}
        .nav > li > a { text-align: center;}
        .tabs-left .nav-tabs > li > a, .tabs-right .nav-tabs > li > a { min-width: 100px;}
    </style>
</head>
<body>
<div class="tabbable tabs-left">
    <ul class="nav nav-tabs">
        <li class="active"><a href="#T1" data-toggle="tab">支付宝</a></li>
        <li><a href="#T2" data-toggle="tab">财付通</a></li>
        <li><a href="#T3" data-toggle="tab">快钱</a></li>
        <li><a href="#T4" data-toggle="tab">易宝</a></li>
    </ul>
    <div class="tab-content">
        <div id="T1" class="tab-pane active fade in">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>支付类别</th>
                    <th>标签</th>
                    <th>类型</th>
                    <th>渠道名称</th>
                    <th>渠道别名</th>
                    <th>比值(人民币/游戏币)</th>
                    <th>状态</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${list}" varStatus="index">
                    <c:if test="${item.channelName == '支付宝'}">
                    <tr>
                        <td>${item.subTagName}</td>
                        <td>${item.subTag}</td>
                        <td>${item.subTypeName}</td>
                        <td>${item.channelName}</td>
                        <td>${item.channelId}</td>
                        <td>1:${item.scale}</td>
                        <td>
                            ${item.status == 0 ? '<font color="#468847">正常</font>':'<font color="red">禁用</font>'}
                        </td>
                    </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
        <div id="T2" class="tab-pane fade">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>支付类别</th>
                    <th>标签</th>
                    <th>类型</th>
                    <th>渠道名称</th>
                    <th>渠道别名</th>
                    <th>比值(人民币/游戏币)</th>
                    <th>状态</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${list}">
                    <c:if test="${item.channelName == '财付通'}">
                        <tr>
                            <td>${item.subTagName}</td>
                            <td>${item.subTag}</td>
                            <td>${item.subTypeName}</td>
                            <td>${item.channelName}</td>
                            <td>${item.channelId}</td>
                            <td>1:${item.scale}</td>
                            <td>
                                    ${item.status == 0 ? '<font color="#468847">正常</font>':'<font color="red">禁用</font>'}
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
        <div id="T3" class="tab-pane fade">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>支付类别</th>
                    <th>标签</th>
                    <th>类型</th>
                    <th>渠道名称</th>
                    <th>渠道别名</th>
                    <th>比值(人民币/游戏币)</th>
                    <th>状态</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${list}" varStatus="index">
                    <c:if test="${item.channelName == '快钱'}">
                        <tr>
                            <td>${item.subTagName}</td>
                            <td>${item.subTag}</td>
                            <td>${item.subTypeName}</td>
                            <td>${item.channelName}</td>
                            <td>${item.channelId}</td>
                            <td>1:${item.scale}</td>
                            <td>
                                    ${item.status == 0 ? '<font color="#468847">正常</font>':'<font color="red">禁用</font>'}
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
        <div id="T4" class="tab-pane fade">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>支付类别</th>
                    <th>标签</th>
                    <th>类型</th>
                    <th>渠道名称</th>
                    <th>渠道别名</th>
                    <th>比值(人民币/游戏币)</th>
                    <th>状态</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${list}" varStatus="index">
                    <c:if test="${item.channelName == '易宝'}">
                        <tr>
                            <td>${item.subTagName}</td>
                            <td>${item.subTag}</td>
                            <td>${item.subTypeName}</td>
                            <td>${item.channelName}</td>
                            <td>${item.channelId}</td>
                            <td>1:${item.scale}</td>
                            <td>
                                    ${item.status == 0 ? '<font color="#468847">正常</font>':'<font color="red">禁用</font>'}
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

</body>
</html>
