<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-10-3
  Time: 上午11:25
  使用jqgrid显示订单
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <div id="accountInfo" style="display: none;">
        <h2 style="text-align: center;background-color: silver;">用户信息详情</h2>
        <a href="#" class="close" style="margin-top: -35px;" title="关闭">×</a>
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
    <div style="background-color:#8ac9ff;text-align:center;height:30px;line-height: 30px;" onclick="showQueryDiv()">
        单击蓝色区域显示查询条件
    </div>
    <div id="queryHeadGrid" style="display: none;">
        <div style="background-color:#8ac9ff;text-align:left;height:20px;vertical-align:middle;">
            请输入或者选择订单查询条件
        </div>
        <br>

        <form action="" method="post" id="orderGridForm" name="orderGridForm" style="margin:0px;display: inline;">
            <table id="queryTable" style="width: 500px;">
                <tr>
                    <td style="text-align: right;">&nbsp;游戏名称:</td>
                    <td style="text-align: center;">
                        <select id="gameNameGrid" name="gameNameGrid" size="1" style="width: 160px;"
                                onchange="initServerNameGrid();">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                        <input type="hidden" id="f_gameNameGrid" name="f_gameNameGrid" value="1">
                        <input type="hidden" id="f_orderGridCounts" name="f_orderGridCounts" value="">
                    </td>

                    <td style="text-align: right;">服务器:</td>
                    <td style="text-align: center;">
                        <select id="serverNameGrid" name="serverNameGrid" size="1" style="width: 160px;" onchange="">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                        <input type="hidden" id="f_serverNameGrid" name="f_serverNameGrid" value="0">
                    </td>
                </tr>
                <tr>

                    <td style="text-align: right">&nbsp;支付网关:</td>
                    <td style="text-align: center;">
                        <select id="channelIdGrid" name="channelIdGrid" size="1" style="width: 160px;"
                                onchange="initSubTypeGrid()">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                        <input type="hidden" id="f_channelIdGrid" name="f_channelIdGrid" value="">
                    </td>

                    <td style="text-align: right">支付方式:</td>
                    <td style="text-align: center;">
                        <select id="subTypeGrid" name="subTypeGrid" size="1" style="width: 160px;"
                                onchange="initPayBankOrSomethingGrid()">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                        <input type="hidden" id="f_subTypeGrid" name="f_subTypeGrid" value="">
                    </td>
                </tr>

                <tr>
                    <td style="text-align: right">&nbsp;支付渠道:</td>
                    <td style="text-align: center;">
                        <select id="bankGrid" name="bankGrid" Grid style="width: 160px;" size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                        </select>
                        <input type="hidden" id="f_bankGrid" name="f_bankGrid" value="">
                    </td>

                    <td style="text-align: right">支付状态:</td>
                    <td style="text-align: center;">
                        <select id="statusGrid" name="statusGrid" style="width: 160px;" size="1">
                            <option value="empty" selected="true">-----------所有----------</option>
                            <option value="success">成功</option>
                            <option value="failed">未支付</option>
                        </select>
                        <input type="hidden" id="f_statusGrid" name="f_statusGrid" value="">
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;提交时间:</td>
                    <td style="text-align: center;"><input id="payStartTimeGrid" name="payStartTimeGrid" type="text"
                                                           style="width: 150px;"
                                                           readonly="true" size="8"
                                                           onfocus="WdatePicker({el:'payStartTimeGrid',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                        <input type="hidden" id="f_payStartTimeGrid" name="f_payStartTimeGrid" value="">
                    </td>
                    <td style="text-align: center;"> ----</td>
                    <td style="text-align: center;"><input id="payEndTimeGrid" name="payEndTimeGrid"
                                                           style="width: 150px;"
                                                           readonly="true"
                                                           type="text"
                                                           onfocus="WdatePicker({el:'payEndTimeGrid',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                           size="8"/>
                        <input type="hidden" id="f_payEndTimeGrid" name="f_payEndTimeGrid" value="">
                    </td>
                </tr>
                <tr>

                    <td style="text-align: right">&nbsp;确认时间:</td>
                    <td style="text-align: center;"><input id="assertStartTimeGrid" name="assertStartTimeGrid"
                                                           style="width: 150px;"
                                                           onfocus="WdatePicker({el:'assertStartTimeGrid',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                           readonly="true" type="text" size="8"/>
                        <input type="hidden" id="f_assertStartTimeGrid" name="f_assertStartTimeGrid" value="">
                    </td>
                    <td style="text-align: center;">----</td>
                    <td style="text-align: center;"><input id="assertEndTimeGrid" name="assertEndTimeGrid" type="text"
                                                           readonly="true"
                                                           onfocus="WdatePicker({el:'assertEndTimeGrid',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                           style="width: 150px;" size="8"/>
                        <input type="hidden" id="f_assertEndTimeGrid" name="f_assertEndTimeGrid" value="">
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;订单号:</td>
                    <td style="text-align: center;"><input id="orderIdGrid" name="orderIdGrid" type="text" size="1"
                                                           style="width: 150px;"/>
                        <input type="hidden" id="f_orderIdGrid" name="f_orderIdGrid" value="">
                    </td>
                    <td style="text-align: right"> 网关订单号:</td>
                    <td style="text-align: center;"><input id="endIdGrid" name="endIdGrid" type="text" size="1"
                                                           style="width: 150px;"/>
                        <input type="hidden" id="f_endIdGrid" name="f_endIdGrid" value="">
                    </td>
                </tr>

                <tr>
                    <td style="text-align: right">&nbsp;充值金额:</td>
                    <td style="text-align: center;"><input id="startMoneyGrid" name="startMoneyGrid"
                                                           style="width: 150px;"
                                                           type="text" size="10"/>
                        <input type="hidden" id="f_startMoneyGrid" name="f_startMoneyGrid" value="">
                    </td>
                    <td style="text-align: center;"> ----</td>
                    <td style="text-align: center;"><input id="endMoneyGrid" name="endMoneyGrid" style="width: 150px;"
                                                           type="text"
                                                           size="10"/>
                        <input type="hidden" id="f_endMoneyGrid" name="f_endMoneyGrid" value="">
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">&nbsp;
                        <select id="queryUserTypeGrid" name="queryUserTypeGrid" size="1"
                                style="width: 90px;"
                                onchange="userInfoSelect()">
                            <option value="userId">用户ID</option>
                            <option value="userName" selected="true">用户名</option>
                        </select>
                        <input type="hidden" id="f_queryUserTypeGrid" name="f_queryUserTypeGrid" value="">
                    </td>
                    <td style="text-align: center;">
                        <input id="userIdGrid" name="userIdGrid" type="text" size="1" style="width: 150px;"/></td>
                    <input type="hidden" id="f_userIdGrid" name="f_userIdGrid" value="">
                    <td style="text-align: right">&nbsp;玩家ID:
                    </td>
                    <td style="text-align: center;">
                        <input id="playerIdGrid" name="playerIdGrid" type="text" size="10" style="width: 150px;"/>
                        <input type="hidden" id="f_playerIdGrid" name="f_playerIdGrid" value="">
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="30px"></td>
                </tr>
                <tr>
                    <td style="text-align: right;" colspan="1"> &nbsp;
                        <input id="queryButton" type="button" style="width: 100px;background-color: #c4eeff;"
                               value="查询订单"
                               onclick="queryOrderByGrid();"/>
                    </td>
                    <td style="text-align: center" colspan="2">
                        <input type="button" id="exportData" style="width: 100px;background-color: #c4eeff;"
                               value="导出订单"
                               onclick="exportQueryDataGrid()"/>
                    </td>
                    <td style="text-align: left" colspan="1">
                        <input type="reset" style="width: 100px;background-color: #c4eeff;" value="重置"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="queryOrderDiv" style="display: none">
        <jsp:include page="orderQueryCondition.jsp" flush="true"></jsp:include>
    </div>
    <table id="orderTable" style="font-size: smaller;"></table>
    <div id="orderPage" style="height: 30px;"></div>
</div>
<script type="text/javascript">
jQuery("#orderTable").jqGrid({
    url:'order?action=getOrderByGrid&type=init&f_orderGridCounts='+$("#f_orderGridCounts").val(),
    datatype:"json",
    colModel:[
        {label:'订单号', name:'orderId', index:'orderId', width:210, align:'left', editable:false, sortable:false},
        {label:'用户编号', name:'userId', index:'userId', width:65, editable:false},
        {label:'用户名', name:'userName', index:'userName', width:120, editable:false, sortable:false},
        {label:'玩家编号', name:'playerId', index:'playerId', width:65, editable:false},
        {label:'充值金额', name:'amount', index:'amount', width:80, editable:false},
        {label:'游戏币', name:'gold', index:'gold', width:90, editable:false},
        {label:'确认时间', name:'assertTime', index:'assertTime', width:170, editable:false},
        {label:'支付时间', name:'payTime', index:'payTime', width:170, editable:false},
        {label:'订单状态', name:'status', index:'status', width:60, editable:false, sortable:false},
        {label:'服务器名称', name:'serverName', index:'serverName', width:130, editable:false, sortable:false},
        {label:'网关', name:'channelName', index:'channelName', width:45, editable:false, sortable:false},
        {label:'充值方式', name:'subTypeName', index:'subTypeName', width:75, editable:false, sortable:false},
        {label:'充值渠道', name:'subTypeTagName', index:'subTypeTagName', width:105, editable:false, sortable:false},
        {label:'网关订单号', name:'endOrder', index:'endOrder', width:170, editable:false},
        {label:'是否掉单', name:'loseOrNot', index:'loseOrNot', textAlign:'center', width:60, editable:false, sortable:false}
    ],
    rowNum:25,
    rowList:[25, 40, 80, 100],
    pager:'#orderPage',
    sortname:'payTime',
    viewrecords:true,
    altRows:true,
    emptyrecords:'没有查到订单,请重新输入查询条件查询!',
    forceFit:true,
    loadtext:'正在努力加载订单数据，请稍后...',
    rownumbers:true,
    refresh:true,
    sortorder:"asc",
    autowidth:true, //自动匹配宽度
    height:602,
    gridview:true, //加速显示
    loadComplete:function (data) { //完成服务器请求后，回调函数
        if (data != null && data.rows == null) {
            alert("没有查到信息");
        }
        if (data != null && data.ext != null) {
            if(data.ext.orderViewObject!=null)
            {

            var condition = data.ext;
            var orderObj=condition.orderViewObject;
            $("#f_orderIdGrid").val(clearNullGrid(orderObj.orderId));
            $("#f_endIdGrid").val(clearNullGrid(orderObj.endOrder));
            $("#f_queryUserTypeGrid").val(clearNullGrid(data.ext.queryUserType));

            $("#f_userIdGrid").val(clearNullGrid(condition.userIdOrName));
            $("#f_playerIdGrid").val(clearNullGrid(orderObj.playerId));
            $("#f_gameNameGrid").val(clearNullGrid(orderObj.gameId));
            $("#f_serverNameGrid").val(clearNullGrid(orderObj.zoneId));
            $("#f_startMoneyGrid").val(clearNullGrid(condition.startMoney));
            $("#f_endMoneyGrid").val(clearNullGrid(condition.endMoney));
            $("#f_channelIdGrid").val(clearNullGrid(orderObj.channelName));
            $("#f_subTypeGrid").val(clearNullGrid(orderObj.subTypeName));
            $("#f_bankGrid").val(clearNullGrid(orderObj.subTypeTagName));
            $("#f_statusGrid").val(clearNullGrid(orderObj.status));
            $("#f_payStartTimeGrid").val(clearNullGrid(condition.payStartTime));
            $("#f_payEndTimeGrid").val(clearNullGrid(condition.payEndTime));
            $("#f_assertStartTimeGrid").val(clearNullGrid(condition.assertStartTime));
            $("#f_assertEndTimeGrid").val(clearNullGrid(condition.assertEndTime));
            }
            $("#f_orderGridCounts").val(data.ext.orderGrid_totalCounts);

        }
    }
});

function clearNullGrid(value)
{
  if($.trim(value).length===0)
  {
      return "";
  }
    else
  {
      return value;
  }
}

function initGameNameGrid() {
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderGameNameDrowdownMenu',
        dataType:'json',
        success:function (data) {
            var gameItem = data;
            var $gameName = $("#gameNameGrid");
            $gameName.empty();
            for (var i = 0; i < gameItem.length; i++) {
                if (i == 0) {
                    $gameName.append("<option selected='true' value='" + gameItem[0].value + "'>" + gameItem[i].label + "</option>");
                }
                else {
                    $gameName.append("<option value='" + gameItem[i].value + "'>" + gameItem[i].label + "</option>");
                }
            }
            var $serverName = $("#serverNameGrid").val();
            if ($serverName != "empty") {

            }
            else {
                initServerNameGrid();
            }
        }
    });

}
function initServerNameGrid() {
    var gameId = $("#gameNameGrid").val();
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderServerNameDrowdownMenu',
        dataType:'json',
        data:{gameID:gameId},
        success:function (data) {
            var serverObj = data;
            var $server = $("#serverNameGrid");
            $server.empty();
            $server.append("<option value='empty' selected='true'>-----------所有----------</option>");
            for (var i = 0; i < serverObj.length; i++) {
                $server.append("<option value='" + serverObj[i].value + "'>" + serverObj[i].label + "</option>");
            }
        }
    });
}
function initChannelGrid() {
    var index = $("#channelIdGrid").get(0).selectedIndex;
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderChannelsDrowdownMenu',
        dataType:'json',
        success:function (data, textStatus) {
            var channelObj = data;
            var $channel = $("#channelIdGrid");
            $channel.empty();
            $channel.append("<option value='empty' selected='true'>-----------所有----------</option>");
            for (var i = 0; i < channelObj.length; i++) {
                $channel.append("<option value='" + channelObj[i].value + "'>" + channelObj[i].label + "</option>");
            }
            if (!index.isEmptyObject) {
                $("#channelIdGrid").get(0).selectedIndex = index;//index为索引值
            }
        }

    });
}
function initSubTypeGrid() {
    var channelIdValue = $("#channelIdGrid").val();
    if (channelIdValue != "empty") {
        $.ajax({
            type:'post',
            url:'initDropdownMenu?action=doInitOrderSubTypeDrowdownMenu',
            dataType:'json',
            data:{channelId:channelIdValue},
            success:function (data) {
                var subTypeObj = data;
                var $subType = $("#subTypeGrid");
                $subType.empty();
                $subType.append("<option value='empty' selected='true'>-----------所有----------</option>");
                for (var i = 0; i < subTypeObj.length; i++) {
                    $subType.append("<option value='" + subTypeObj[i].value + "'>" + subTypeObj[i].label + "</option>");
                }
            }
        });
    }
    else {  //如果选了空，把它的一级和二级菜单也设置为空
        $("#subTypeGrid").val("empty");
        $("#bankGrid").val("empty");
    }
}

function initSubTypeGridFirst() {
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderSubTypeDrowdownMenu',
        dataType:'json',
        data:{channelId:""},
        success:function (data) {
            var subTypeObj = data;
            var $subType = $("#subTypeGrid");
            $subType.empty();
            $subType.append("<option value='empty' selected='true'>-----------所有----------</option>");
            for (var i = 0; i < subTypeObj.length; i++) {
                $subType.append("<option value='" + subTypeObj[i].value + "'>" + subTypeObj[i].label + "</option>");
            }
        }
    });

}


function initPayBankOrSomethingGrid() {
    var channelIdValue = $("#channelIdGrid").val();
    var subTypeValue = $("#subTypeGrid").val();
    if (subTypeValue != "empty") {
        $.ajax({
            type:'post',
            url:'initDropdownMenu?action=doInitOrderSubTypeTagDrowdownMenu',
            dataType:'json',
            data:{channelId:channelIdValue, subType:subTypeValue},
            success:function (data) {
                var bankObj = data;
                var $bank = $("#bankGrid");
                $bank.empty();
                $bank.append("<option value='empty' selected='true'>-----------所有----------</option>");
                for (var i = 0; i < bankObj.length; i++) {
                    $bank.append("<option value='" + bankObj[i].value + "'>" + bankObj[i].label + "</option>");
                }
            }
        });
    }
    else {
        $("#bankGrid").val("empty");
    }
}

function initPayBankOrSomethingGridFirst() {
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderSubTypeTagDrowdownMenu',
        dataType:'json',
        data:{channelId:"", subType:""},
        success:function (data) {
            var bankObj = data;
            var $bank = $("#bankGrid");
            $bank.empty();
            $bank.append("<option value='empty' selected='true'>-----------所有----------</option>");
            for (var i = 0; i < bankObj.length; i++) {
                $bank.append("<option value='" + bankObj[i].value + "'>" + bankObj[i].label + "</option>");
            }
        }
    });
}
//异步导出查询的数据到Excel
function exportQueryDataGrid() {
    //查询条件获取
    var orderId = $("#orderIdGrid").val();
    var endId = $("#endIdGrid").val();
    var queryUserType = $("#queryUserTypeGrid").val();
    var userId = $("#userIdGrid").val();
    var queryPlayerType = $("#queryPlayerTypeGrid").val();
    var playerId = $("#playerIdGrid").val();
    var gameName = $("#gameNameGrid").val();
    var serverName = $("#serverNameGrid").val();
    var startMoney = $("#startMoneyGrid").val();
    var endMoney = $("#endMoneyGrid").val();
    var channelId = $("#channelIdGrid").val();
    var subType = $("#subTypeGrid").val();
    var bank = $("#bankGrid").val();
    var status = $("#statusGrid").val();
    var payStartTime = $("#payStartTimeGrid").val();
    var payEndTime = $("#payEndTimeGrid").val();
    var assertStartTime = $("#assertStartTimeGrid").val();
    var assertEndTime = $("#assertEndTimeGrid").val();
//        var pageItemNumber = $("#pageItemNumberGrid").attr("value");
    //放到数组里
    var arr = new Array();
    arr.push(orderId);
    arr.push(endId);
    arr.push(userId);
    arr.push(playerId);
    arr.push(serverName);
    arr.push(startMoney);
    arr.push(endMoney);
    arr.push(channelId);
    arr.push(subType);
    arr.push(bank);
    arr.push(status);
    arr.push(payStartTime);
    arr.push(payEndTime);
    arr.push(assertStartTime);
    arr.push(assertEndTime);
    var nullOrNot = true;
    $.each(arr, function (key, val) {
        nullOrNot = nullOrNot && checkNullValue(val);
    });

    if (nullOrNot) {
        alert('请输入导出订单的条件！');
    }
    else {
        //拼接查询条件 '&pageNumber=' + page + '&pageItemNumber=' + pageItemNumber +
        var url = '&orderId=' + orderId + '&endId=' + endId + '&queryUserType=' + queryUserType + '&userId=' + encodeURI(encodeURI(userId)) +
                '&queryPlayerType=' + queryPlayerType
                + '&playerId=' + playerId + '&gameName=' + gameName + '&serverName=' + serverName + '&startMoney=' + startMoney + '&endMoney=' + endMoney + '&channelId=' + channelId
                + '&subType=' + subType + '&bank=' + bank + '&status=' + status + '&payStartTime=' + payStartTime + '&payEndTime=' + payEndTime + '&assertStartTime=' + assertStartTime +
                '&assertEndTime=' + assertEndTime;
        window.open('order?action=exportQueryData' + url, 'blank');
        hideQueryCondition();
    }
}


$(function () {
    initGameNameGrid();
    initChannelGrid();
    initSubTypeGridFirst();
});

</script>