<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-6-26
  Time: 下午2:36
 补单页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>调单查询</title>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="css/table.css">
    <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.21.custom.css">
    <link rel="stylesheet" type="text/css" href="css/ui.jqgrid.css">

</head>
<body>
<div id="queryDiv" style="text-align: center;">
    <br>
    请输入订单号： <textarea rows="1" cols="15" id="queryArea"></textarea>
    <input type="button" value="查询掉单" id="queryButton" onclick="queryLost()"/> <br>
</div>
<div id="loseOrderdataDiv">
    <h3 style="color: green;display: none">交易日志查询结果：</h3>
    <table id="grid_table">
        <%--<thead style="display: none">--%>
        <%--<th> 序号</th>--%>
        <%--<th> 订单号</th>--%>
        <%--<th> 玩家ID</th>--%>
        <%--<th> 充值金额</th>--%>
        <%--<th> 状态码</th>--%>
        <%--<th> 信息</th>--%>
        <%--<th> 支付时间</th>--%>
        <%--<th> 记录时间</th>--%>
        <%--</thead>--%>
        <%--<tbody>--%>
        <%--</tbody>--%>
    </table>
    <div id="grid_page"></div>
    <h2 style='color: red;font-size: 20px;display: none;' colspan='8'>没有查到记录,请重新输入订单号查询！</h2>
</div>
<br>

<div id="orderdata">
    <table id="tableContent" class="t1" style="display: none">
        <thead>
        <tr>
            <th>订单号</th>
            <th>用户ID</th>
            <th>用户名</th>
            <th>玩家ID</th>
            <th>金额</th>
            <th>游戏币</th>
            <th>确认时间</th>
            <th>支付时间</th>
            <th>订单状态</th>
            <th>服务器名称</th>
            <th>充值网关</th>
            <th>充值方式</th>
            <th>充值渠道</th>
            <th>网关订单号</th>
            <th>游戏名</th>
            <th>游戏币名称</th>

        </tr>
        </thead>
        <tbody id="tbodyContent" class="table table-bordered">
        </tbody>
    </table>
    <h1 id="emptyMsg" style="text-align: center;color: red;"></h1>
</div>
<br>
<input type="button" value="补单" id="addOrder" style="display: none;" onclick="rechargeOrder()"/>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/grid.locale-cn.js"></script>
<script type="text/javascript" src="js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="js/lose.js"></script>
</body>

</html>
