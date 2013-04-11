<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-7-13
  Time: 下午3:12
  订单统计功能
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>订单统计页面</title>
    <link type="text/css" rel="stylesheet" href="css/orderCount.css">
    <script type="text/javascript">
        initElement();
    </script>
</head>
<body>
<div id="navConditionDiv">
    <div id="hideorshow"
    <%--onmouseover="showQueryCondition()"--%>
         onmouseover="timeInFn()" onmouseout="timeOutFn()" onclick="showOrHide()"
         style="text-align: center;background-color: #adff2f; height: 30px;">
        <a id="hidehref" href="#" style="line-height: 40px;">显示查询条件</a>
    </div>
    <div id="queryHead" style="background-color: white;" class="shadow">
        <form action="" method="post" id="dataStatisticalFrom" style="margin:0px;display: inline;">
            <%--<h4 class="top_h4">请输入或选择查询条件</h4>--%>
            <%--<a href="#" class="close" title="关闭">close</a>--%>
            <table id="queryTable" style="width: 55%;">
                <tr>
                    <td style="text-align: right;"><input type="checkbox" id="checkPayStartTime"
                                                          name="checkPayStartTime">提交时间
                    </td>
                    <td style="text-align: center;">
                        <input
                                id="payStartTime"
                                name="payStartTime"
                                type="text"
                                style="width: 150px;"
                                readonly="true" size="8"
                                onfocus="WdatePicker({el:'payStartTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>

                    <td style="text-align: right;">------------------------------------------------------------</td>
                    <td style="text-align: center;">
                        <input id="payEndTime" name="payEndTime" style="width: 150px;"
                               readonly="true"
                               type="text"
                               onfocus="WdatePicker({el:'payEndTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                               size="8"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;"><input type="checkbox" id="checkAssertStartTime"
                                                          name="checkAssertStartTime">确认时间
                    </td>
                    <td style="text-align: center;">
                        <input
                                id="assertStartTime"
                                name="assertStartTime"
                                style="width: 150px;"
                                onfocus="WdatePicker({el:'assertStartTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                readonly="true" type="text"
                                size="8"/>
                    </td>

                    <td style="text-align: right;"> ------------------------------------------------------------</td>
                    <td style="text-align: center;">
                        <input id="assertEndTime" name="assertEndTime" type="text"
                               readonly="true"
                               onfocus="WdatePicker({el:'assertEndTime',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                               style="width: 150px;" size="8"/>
                    </td>
                </tr>


                <tr>
                    <td style="text-align: right"><input type="checkbox" id="checkGameName" name="checkGameName"
                                                         checked="true" disabled="true">游戏名称
                    </td>
                    <td style="text-align: center;">
                        <select
                                id="gameName"
                                name="gameName"
                                size="1"
                                style="width: 160px;"
                                onchange="initServerName();">
                        </select>
                    </td>

                    <td style="text-align: right"><input type="checkbox" id="checkServerName" name="checkServerName"
                                                         checked="true">服务器名
                    </td>
                    <td style="text-align: center;">
                        <select id="serverName"
                                name="serverName"
                                size="1"
                                style="width: 160px;"
                                onchange="">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right"><input type="checkbox" id="checkChannelId" name="checkChannelId"
                                                         checked="true">支付网关
                    </td>
                    <td style="text-align: center;">
                        <select id="channelId"
                                name="channelId"
                                size="1"
                                style="width: 160px;"
                                onchange="initSubType()">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>

                    <td style="text-align: right"><input type="checkbox" id="checkSubType" name="checkSubType"
                                                         checked="true">支付方式
                    </td>
                    <td style="text-align: center;">
                        <select id="subType" name="subType" size="1" style="width: 160px;"
                                onchange="initPayBankOrSomething()">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;"><input type="checkbox" id="checkBank" name="checkBank"
                                                          checked="true">支付渠道
                    </td>
                    <td style="text-align: center;">
                        <select id="bank" name="bank" style="width: 160px;" size="1" checked="true">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                    </td>
                    <td style="text-align: right;"><input type="checkbox" id="checkStatus" name="checkStatus">支付状态</td>
                    <td style="text-align: center;">
                        <select id="status" name="status" style="width: 160px;" size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                    </td>
                </tr>
                <tr style=" height: 40px;">
                    <td style="text-align: right;"><input type="checkbox" id="amount" name="amount" checked="true">充值金额
                    </td>
                    <td style="text-align: center; height: 40px;">
                        <select style="width: 160px; display: none; " size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                    </td>
                    <td style="text-align: right;"><input type="checkbox" id="gold" name="gold" checked="true">游戏币数</td>
                    <td style="text-align: center;">
                        <select style="width: 160px; display: none; " size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                    </td>
                </tr>
                <tr style=" height: 40px;">
                    <td style="text-align: right"><input type="checkbox" id="userAmount" name="userAmount"
                                                         checked="true">用户数量
                    </td>
                    <td style="text-align: center;">
                        <select style="width: 160px;display: none; " size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                    </td>
                    <td style="text-align: right;"><input type="checkbox" id="playerAmount" name="playerAmount"
                                                          checked="true">玩家数量
                    </td>
                    <td style="text-align: center;">
                        <select style="width: 160px;display: none; " size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td style="text-align: center;"><input type="button" name="query" value="查询"
                                                           onclick="firstOfData();"
                                                           style="width: 100px;background-color: #c4eeff;"></td>
                    <td style="text-align: center;"><input type="button" name="export" value="导出"
                                                           style="width: 100px;background-color: #c4eeff;"
                                                           onclick="exportStatisticalData();">
                    </td>
                    <td colspan="2" style="text-align: center;">
                        <input type="reset" name="gameName" value="重置" style="width: 100px;background-color: #c4eeff;">
                    </td>
                    <%--<td style="text-align: center" colspan="1">--%>
                    <%--&lt;%&ndash;<input type="button" style="width: 100px;background-color: #c4eeff;" value="关闭"&ndash;%&gt;--%>
                    <%--&lt;%&ndash;onclick="hideQueryCondition();"/>&ndash;%&gt;--%>
                    <%--</td>--%>
                </tr>
            </table>
        </form>
    </div>
</div>
<div id="countResult">
    <br>
    <h4 id="overAll" style="display: none;text-align: center;">统计结果展示</h4>
    <br>

    <div>
        <table id="dataStatistical" class="table table-bordered">
            <thead id="countResultHead" style="display: none;">
            <tr>
                <th>充值网关</th>
                <th>金额</th>
                <th>游戏币</th>
                <th>订单数</th>
                <th>用户数</th>
                <th>玩家数</th>
                <%--<th>金额</th>--%>
                <%--<th>游戏币</th>--%>
                <%--<th>确认时间</th>--%>
                <%--<th>支付时间</th>--%>
                <%--<th>订单状态</th>--%>
                <%--<th>服务器名称</th>--%>
                <%--<th>充值网关</th>--%>
                <%--<th>充值方式</th>--%>
                <%--<th>充值渠道</th>--%>
                <%--<th>网关订单号</th>--%>
                <%--<th>游戏名</th>--%>
                <%--<th>游戏币名称</th>--%>
                <%--<th>是否掉单</th>--%>
            </tr>
            </thead>
            <tbody id="dataBodyContent" class="table table-bordered">
            <h2 id="tips">请先选择统计条件。单击绿色区域弹出统计条件界面... </h2>
            </tbody>
        </table>
        <div id="showMsg"></div>
    </div>
</div>
<div>
    <table class="table table-bordered" id="showorderDateTableofData">
        <tr style="text-align: center;">
            <td colspan="21" style="text-align: center;">
                <input type="hidden" id="pageOfData" value="1">
                <select id="pageItemNumberOfData" name="pageItemNumberOfData" style="width: 80px;height: 25px;"
                        size="1" onchange="pageItemChangeOfData();">
                    <option value="15" selected="true">15</option>
                    <option value="20">20</option>
                    <option value="30">30</option>
                    <option value="50">50</option>
                </select>
                &nbsp;
                &nbsp; <strong id="totalItemsOfData"></strong>
                &nbsp; &nbsp;
                <input type="text" id="pageNumberOfData" style="width: 40px;" value="1"
                       onkeyup="keyupEventOfData(event);"/>
                &nbsp; &nbsp;
                <a href="javascript:void(0)" onclick="firstOfData()">第一页</a> &nbsp; &nbsp;
                <a href="javascript:void(0)" onclick="preOfData()">上一页</a> &nbsp; &nbsp;
                <a href="javascript:void(0)" onclick="nextOfData()">下一页</a> &nbsp; &nbsp;
                <a href="javascript:void(0)" onclick="lastOfData()">最后一页</a> &nbsp; &nbsp;
                &nbsp; &nbsp;
                <input type="button" id="reflash" style="width: 40px;" value="刷新"
                       onclick="reflashCurrentPageOfData()"/>
                &nbsp; &nbsp;
                <input type="button" style="width: 40px;" value="返回"
                       onclick="returnToOrignOfData()"/>
                <%--<strong id="pageNumber">1</strong>--%> &nbsp; &nbsp;

            </td>
        </tr>
    </table>
    <br>
</div>
<h1 id="emptyMsg" style="text-align: center;color: red;"></h1>
</body>
</html>
