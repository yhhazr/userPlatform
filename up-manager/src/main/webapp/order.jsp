<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-6-14
  Time: 下午2:48
  订单页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>查询订单</title>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/querycondition.css">
    <link rel="stylesheet" type="text/css" href="css/easydialog.css">

</head>
<body onload="query();">
<script type="text/javascript">
    query();
</script>
<div id="accountInfo" style="display: none;">
    <h2 style="text-align: center;background-color: silver;">用户信息详情</h2>
    <a href="#" class="close" title="关闭">close</a>
    <table class="table table-bordered">
        <thead style="display: none;" id="accountInfoThead">
        <th>用户ID</th>
        <th>用户名</th>
        <th>用户Email</th>
        <th>注册时间</th>
        <th>最后登录时间</th>
        <th>最后登录IP</th>
        <th>登录次数</th>
        <th>用户状态</th>
        </thead>
        <tbody id="accountInfoTbody">
        </tbody>
    </table>
</div>
<div id="loadding"></div>
<div id="mainDiv">
    <%--<%@ include file="top.jsp" %>--%>
    <div id="topDiv">
        <jsp:include page="orderQueryCondition.jsp" flush="true"></jsp:include>

    </div>

    <div id="bottomDiv" style="display: inline;" onmouseover="hideQueryCondition();">
        <table id="tableContent" class="table table-bordered" style="display: none;">
            <thead>
            <tr style="text-align: center;">
                <th>订单号</th>
                <th>用户ID</th>
                <th>用户名</th>
                <th>玩家ID</th>
                <th>金额</th>
                <th>游戏币</th>
                <th>确认时间</th>
                <th>提交时间</th>
                <th>订单状态</th>
                <th>服务器名称</th>
                <th>充值网关</th>
                <th>充值方式</th>
                <th>充值渠道</th>
                <th>网关订单号</th>
                <%--<th>游戏名</th>--%>
                <%--<th>游戏币名称</th>--%>
                <th>是否掉单</th>
            </tr>
            </thead>
            <tbody id="tbodyContent" class="table table-bordered">
            </tbody>
        </table>
        <div id="tablePageOfOrder" style="display: none;">
            <table class="table table-bordered" id="showorderDateTable">
                <tr style="text-align: center;">
                    <td colspan="21" style="text-align: center;">
                        <input type="hidden" id="page" value="1">
                        <input type="hidden" id="totalPage" value="1">
                        <select id="pageItemNumber" name="pageItemNumber" style="width: 80px;height: 25px;"
                                size="1" onchange="pageItemChange()">
                            <option value="15" selected="true">15</option>
                            <option value="20">20</option>
                            <option value="30">30</option>
                            <option value="50">50</option>
                        </select>
                        &nbsp;
                        &nbsp; <strong id="totalItems"></strong>
                        &nbsp; &nbsp;
                        <input type="text" id="pageNumber" style="width: 40px;" value="1" onkeyup="keyupEvent(event);"/>
                        &nbsp; &nbsp;
                        <a href="javascript:void(0)" onclick="first()">第一页</a> &nbsp; &nbsp;
                        <a href="javascript:void(0)" onclick="pre()">上一页</a> &nbsp; &nbsp;
                        <a href="javascript:void(0)" onclick="next()">下一页</a> &nbsp; &nbsp;
                        <a href="javascript:void(0)" onclick="last()">最后一页</a> &nbsp; &nbsp;
                        &nbsp; &nbsp;
                        <input type="button" id="reflash" style="width: 40px;" value="刷新"
                               onclick="reflashCurrentPage()"/> &nbsp; &nbsp;
                        <input type="button" id="reflash" style="width: 40px;" value="返回"
                               onclick="returnToOrign()"/>
                        <%--<strong id="pageNumber">1</strong>--%> &nbsp; &nbsp;

                    </td>
                </tr>
            </table>
        </div>
        <h1 id="emptyMsg" style="text-align: center;color: red;"></h1>
    </div>
    <!--  end of  bottomDiv-->
</div>
</body>
</html>
