<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-6-20
  Time: 下午2:55
  订单查询条件页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="images/favicon.ico"/>
</head>
<body onload="initQueryCondition()">
<div id="navConditionDiv">
    <div id="hideorshow"
    <%--onmouseover="showQueryCondition()"--%>
         onmouseover="timeInFn()" onmouseout="timeOutFn()" onclick="showOrHide()" style="text-align: center;">
        <br> <a id="hidehref" href="#" style="font-size:15px;font-weight: bold;">显示查询条件</a>
    </div>
    <div id="queryHead">
        <form action="" method="post" id="orderForm" name="orderForm" style="margin:0px;display: inline;">
            <%--<h4 class="top_h4">请输入或选择查询条件</h4>--%>
            <%--<a href="#" class="close" title="关闭">close</a>--%>
            <table id="queryTable">
                <tr>
                    <td style="text-align: right">&nbsp;提交时间:</td>
                    <td style="text-align: center;"><input id="payStartTime" name="payStartTime" type="text"
                                                           style="width: 150px;"
                                                           readonly="true" size="8"
                                                           onfocus="WdatePicker({el:'payStartTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>
                    <td style="text-align: center;"> ----</td>
                    <td style="text-align: center;"><input id="payEndTime" name="payEndTime" style="width: 150px;"
                                                           readonly="true"
                                                           type="text"
                                                           onfocus="WdatePicker({el:'payEndTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                           size="8"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;确认时间:</td>
                    <td style="text-align: center;"><input id="assertStartTime" name="assertStartTime"
                                                           style="width: 150px;"
                                                           onfocus="WdatePicker({el:'assertStartTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                           readonly="true" type="text" size="8"/>
                    </td>
                    <td style="text-align: center;">----</td>
                    <td style="text-align: center;"><input id="assertEndTime" name="assertEndTime" type="text"
                                                           readonly="true"
                                                           onfocus="WdatePicker({el:'assertEndTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                           style="width: 150px;" size="8"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;">&nbsp;游戏名称:</td>
                    <td style="text-align: center;">
                        <select id="gameName" name="gameName" size="1" style="width: 160px;"
                                onchange="initServerName();">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>

                    <td style="text-align: right;">服务器:</td>
                    <td style="text-align: center;">
                        <select id="serverName" name="serverName" size="1" style="width: 160px;" onchange="">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;支付网关:</td>
                    <td style="text-align: center;">
                        <select id="channelId" name="channelId" size="1" style="width: 160px;" onchange="initSubType()">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>

                    <td style="text-align: right">支付方式:</td>
                    <td style="text-align: center;">
                        <select id="subType" name="subType" size="1" style="width: 160px;"
                                onchange="initPayBankOrSomething()">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;支付渠道:</td>
                    <td style="text-align: center;">
                        <select id="bank" name="bank" style="width: 160px;" size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>

                    <td style="text-align: right">支付状态:</td>
                    <td style="text-align: center;">
                        <select id="status" name="status" style="width: 160px;" size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td style="text-align: right">&nbsp;充值金额:</td>
                    <td style="text-align: center;"><input id="startMoney" name="startMoney" style="width: 150px;"
                                                           type="text" size="10"/></td>
                    <td style="text-align: center;"> ----</td>
                    <td style="text-align: center;"><input id="endMoney" name="endMoney" style="width: 150px;"
                                                           type="text"
                                                           size="10"/></td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;
                        <select id="queryUserType" name="queryUserType" size="1"
                                style="width: 90px;"
                                onchange="userInfoSelect()">
                            <option value="userId">用户ID</option>
                            <option value="userName" selected="true">用户名</option>
                        </select>
                    </td>
                    <td style="text-align: center;">
                        <input id="userId" name="userId" type="text" size="1" style="width: 150px;"/></td>

                    <td style="text-align: right">&nbsp;玩家ID:
                        <%--<select id="queryPlayerType" name="queryPlayerType" size="1" style="width: 90px;"--%>
                        <%--onchange="playerInfoSelect()">--%>
                        <%--<option value="playerId" selected="true">玩家ID</option>--%>
                        <%--&lt;%&ndash;<option value="playerName" selected="true">玩家名称</option>&ndash;%&gt;--%>
                        <%--</select>--%>
                    </td>
                    <td style="text-align: center;">
                        <input id="playerId" name="playerId" type="text" size="10" style="width: 150px;"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;订单号:</td>
                    <td style="text-align: center;"><input id="orderId" name="orderId" type="text" size="1"
                                                           style="width: 150px;"/>
                    </td>
                    <td style="text-align: right"> 网关订单号:</td>
                    <td style="text-align: center;"><input id="endId" name="endId" type="text" size="1"
                                                           style="width: 150px;"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;" colspan="1"> &nbsp;
                        <input id="queryButton" type="button" style="width: 100px;background-color: #c4eeff;"
                               value="查询订单"
                               onclick="query();"/>
                    </td>
                    <td style="text-align: center" colspan="2">
                        <input type="button" id="exportData" style="width: 100px;background-color: #c4eeff;"
                               value="导出订单"
                               onclick="exportQueryData()"/>
                    </td>
                    <td style="text-align: left" colspan="1">
                        <input type="reset" style="width: 100px;background-color: #c4eeff;" value="重置"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>
