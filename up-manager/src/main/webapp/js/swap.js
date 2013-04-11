/**
 * 时间对象的格式化
 */
Date.prototype.format = function (format) {
    /*
     * format="yyyy-MM-dd hh:mm:ss";
     */
    var o = {
        "M+":this.getMonth() + 1,
        "d+":this.getDate(),
        "h+":this.getHours(),
        "m+":this.getMinutes(),
        "s+":this.getSeconds(),
        "q+":Math.floor((this.getMonth() + 3) / 3),
        "S":this.getMilliseconds()
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
            - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
//判断字符串中是不是有空格的函数
function isWhiteWpace(s) {
    var whitespace = " \t\n\r";
    var i;
    for (i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if (whitespace.indexOf(c) >= 0) {
            return true;
        }
    }
    return false;
}
// /每页数据数量和页数获取
var page = parseInt($("#page").attr("value"));

function initElement() {
    initGameName();
    initServerName();
    initChannel();
    $("#showorderDateTableofData").hide();
//    showdStatisticData();
}

function query() {
    page = 1;
    initChannel();
//    initGameName();
//    initServerName();
    request();
    hideQueryCondition();

}
function setMenu() {
    $.ajax({
        type:"post",
        url:"/master?action=getPermissionInfo",
        dataType:"json",
        success:function (data) {
            if (data != null) { //订单菜单
                if (data.showOrderPage == "true") {
                    $("#showOrderPage").show();
                }
                if (data.showOrderByGrid == "true") {
                    $("#showOrderByGrid").show();
                }
                if (data.orderStatistical == "true") {
                    $("#orderStatistical").show();
                } //用户菜单
                if (data.swapToUserAccountPage == "true") {
                    $("#swapToUserAccountPage").show();
                }
                if (data.queryUserLog == "true") {
                    $("#swapToUserAccountPage").show();
                }
                if (data.resetMailAndPsw == "true") {
                    $("#swapToUserAccountPage").show();
                } //客服菜单
                if (data.keywordManage == "true") {
                    $("#keywordManage").show();
                }
                if (data.fullTextSearch == "true") {
                    $("#fullTextSearch").show();
                }
                if (data.faqManage == "true") {
                    $("#faqManage").show();
                }
                if (data.toFaqKindPage == "true") {
                    $("#toFaqKindPage").show();
                }
                if (data.csInfoManage == "true") {
                    $("#csInfoManage").show();
                } //运营菜单
                if (data.mergerServer == "true") {
                    $("#mergerServer").show();
                }
                if (data.splitServer == "true") {
                    $("#mergerServer").show();
                }
                if (data.queryServer == "true") {
                    $("#mergerServer").show();
                }
                // 支付管理
                if (data.paySwitch == "true") {
                    $("#paySwitch").show();
                }
                if (data.paySwitchType == "true") {
                    $("#paySwitchType").show();
                }
                if (data.orderManage == "true") {
                    if (data.showOrderPage == null && data.orderStatistical == null) {

                    }
                    else {
                        $("#orderManage").show();
                    }
                }
                if (data.gameUserManage == "true") {
                    if (data.swapToUserAccountPage == null && data.queryUserLog == null && data.resetMailAndPsw == null) {

                    }
                    else {
                        $("#gameUserManage").show();
                    }
                }
                if (data.csmanage == "true") {
                    if (data.faqManage == null && data.toFaqKindPage == null && data.csInfoMange == null && data.keywordManage == null && data.fullTextSearch == null) {

                    }
                    else {
                        $("#csmanage").show();
                    }
                }
                if (data.operationManage == "true") {
                    if (data.mergerServer == null && data.splitServer == null && data.queryServer == null) {

                    }
                    else {
                        $("#operationManage").show();
                    }
                }
                if (data.appealManage == "true") {
                    $("#appealManage").show();
                }
                if (data.payManage == "true") {
                    $("#payManage").show();
                }
            }
        }

    });
}
function loadQuery() {
    query();
    showQueryCondition();
}
//返回功能
function returnToOrign() {
    $("#orderForm")[0].reset();
    page = 1;
    $("#page").val(page);
    request();
}


function request() {
    $("#tableContent").hide();
    $("#showorderDateTable").hide();
    $("#emptyMsg").empty();
    $.md({modal:"#footDiv"});
    //查询条件获取
    var orderId = $("#orderId").val();
    var endId = $("#endId").val();
    var queryUserType = $("#queryUserType").val();
    var userId = $("#userId").val();
    var playerId = $("#playerId").val();
    var gameName = $("#gameName").val();
    var serverName = $("#serverName").val();
    var startMoney = $("#startMoney").val();
    var endMoney = $("#endMoney").val();
    var channelId = $("#channelId").val();
    var subType = $("#subType").val();
    var bank = $("#bank").val();
    var status = $("#status").val();
    var payStartTime = $("#payStartTime").val();
    var payEndTime = $("#payEndTime").val();
    var assertStartTime = $("#assertStartTime").val();
    var assertEndTime = $("#assertEndTime").val();
    var pageItemNumber = $("#pageItemNumber").attr("value");
    $.ajax({
        type:"post",
        url:"order?action=showOrderDataByAjaxReWrite",
        dataType:"json",
        data:{
            pageIndex:page,
            pageSize:pageItemNumber,
            orderId:orderId,
            endId:endId,
            queryUserType:queryUserType,
            userId:userId,
            playerId:playerId,
            gameName:gameName,
            serverName:serverName,
            startMoney:startMoney,
            endMoney:endMoney,
            channelId:channelId,
            subType:subType,
            bank:bank,
            status:status,
            payStartTime:payStartTime,
            payEndTime:payEndTime,
            assertStartTime:assertStartTime,
            assertEndTime:assertEndTime
        },
        success:function (data) {
            if (data == null) {
                $("#tableContent").hide();
                $("#showorderDateTable").hide();
                $("#emptyMsg").empty();
                $("#emptyMsg").append('网络繁忙!');
            }
            else {
                var json = data.list;
                if (json.length > 0) {
                    $("#tableContent").show();
                    $("#showorderDateTable").show();
                    $("#emptyMsg").empty();
                    var trObj = $("#tbodyContent");
                    trObj.empty();
                    for (var i = 0; i < json.length; i++) {
                        var $tr = createTR().append(createTD(json[i].orderId)).append(createTD(showLinkToUserAccount(json[i].userId))).append(createTD(json[i].userName)).append(createTD(json[i].playerId))
                            .append(createTD('￥' + json[i].amount)).append(createTD(changeGoldImg(json[i].gameId)+json[i].gold )).append(createTD(clearNull(json[i].assertTime)))
                            .append(createTD(new Date(parseInt(json[i].payTime)).format("yyyy-MM-dd hh:mm:ss"))).append(createStatusTD(json[i].status))
                            .append(createTD(json[i].serverName)).append(createTD(json[i].channelName)).append(createTD(json[i].subTypeName))
                            .append(createTD(json[i].subTypeTagName)).append(createTD(json[i].endOrder)).append(createTD(showLabel(json[i].loseOrNot, json[i].orderId)));
                        trObj.append($tr);
                    }
                    var $totalPage = parseInt(getTotalPages(data.total, pageItemNumber));
                    $("#totalItems").html("查到" + data.total + "条数据！一共" + $totalPage + "页");
                    $("#pageNumber").attr("value", page);
                    $("#totalPage").attr("value", $totalPage);
                    $("#tableContent").show();
                    $("#tablePageOfOrder").show();
                    selectTrChangeColor();
                }
                else {
                    $("#tableContent").hide();
                    $("#showorderDateTable").hide();
                    $("#emptyMsg").empty();
                    $("#emptyMsg").append('没有查询到符合要求的数据！请重新<a onclick="showQueryCondition()"> <h1 id="emptyMsg" style="text-align: center;color: blue;display : inline;">输入或者选择</h1></a>查询条件！');
                }
            }
            $("#footDiv").css("display", "none");
        }
    });
}

function changeGoldImg(gameId)
{
    if(parseInt(gameId)==1)
    {
        return  '<img src="../images/sq_zs.gif" height="16px" width="16px" >';
    }
    else
    {
        return  '<img src="../images/dq.png" height="16px" width="16px" >';
    }

}


function getTotalPages($totalItems, $pageItems) {
    var totals = parseInt($totalItems);
    var pageItems = parseInt($pageItems);
    var resl;
    if (totals % pageItems == 0) {
        resl = parseInt(totals / pageItems);
    }
    else {
        resl = totals / pageItems + 1;
    }
    return resl;
}

//构造行
function createTR() {
    return  $('<tr></tr>');
}
//构造列
function createTD($ItemValue) {
    if ($ItemValue == undefined || $ItemValue.length <= 0)
        $ItemValue = '无';
    return $('<td>' + $ItemValue + '</td>');
}
function createStatusTD($status) {
    var $statusTD;
    if ($status == "成功") {
        $statusTD = $("<td style='color: green;'>" + $status + "</td>");
    }
    else {
        $statusTD = ("<td style='color:red;'>" + $status + "</td>");
    }
    return $statusTD;
}


function clearNull(item) {
    if (item == undefined) {
        item = "无";
    }
    else {
        item = new Date(item).format("yyyy-MM-dd hh:mm:ss")
    }
    return item;
}


function first() {
    page = 1;
    request();

}
function last() {
    page = parseInt($("#totalPage").val());
    request();
}
function next() {
    var totalPage = parseInt($("#totalPage").val());
    if (page < totalPage) {
        page = parseInt(page) + parseInt(1);
        request();
    }
    else {
        alert("已经是最后一页！");
    }
}
function pre() {
    if (page > 1) {
        page = parseInt(page) - parseInt(1);
        request();
    } else {
        alert("已经是第一页！");
    }
}
//刷新当前页的数据
function reflashCurrentPage() {
    page = $("#pageNumber").val();
    request();
}


function initChannel() {
    var index = $("#channelId").get(0).selectedIndex;
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderChannelsDrowdownMenu',
        dataType:'json',
        success:function (data, textStatus) {
            var channelObj = data;
            var $channel = $("#channelId");
            $channel.empty();
            $channel.append("<option value='empty' selected='true'>-----------所有----------</option>");
            for (var i = 0; i < channelObj.length; i++) {
                $channel.append("<option value='" + channelObj[i].value + "'>" + channelObj[i].label + "</option>");
            }
            if (!index.isEmptyObject) {
                $("#channelId").get(0).selectedIndex = index;//index为索引值
            }
        }

    });
}
function initSubType() {

    var channelIdValue = $("#channelId").val();

    if (channelIdValue != "empty") {
        $.ajax({
            type:'post',
            url:'initDropdownMenu?action=doInitOrderSubTypeDrowdownMenu',
            dataType:'json',
            data:{channelId:channelIdValue},
            success:function (data) {
                var subTypeObj = data;
                var $subType = $("#subType");
                $subType.empty();
                $subType.append("<option value='empty' selected='true'>-----------所有----------</option>");
                for (var i = 0; i < subTypeObj.length; i++) {
                    $subType.append("<option value='" + subTypeObj[i].value + "'>" + subTypeObj[i].label + "</option>");
                }
            }
        });
    }
    else {  //如果选了空，把它的一级和二级菜单也设置为空
        $("#subType ").val("empty");
        $("#bank ").val("empty");
    }
}

function initGameName() {
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderGameNameDrowdownMenu',
        dataType:'json',
        success:function (data) {
            var gameItem = data;
            var $gameName = $("#gameName");
            $gameName.empty();
            for (var i = 0; i < gameItem.length; i++) {
                if (i == 0) {
                    $gameName.append("<option  value='" + gameItem[0].value + "'>" + gameItem[i].label + "</option>");
                }
                else {
                    $gameName.append("<option value='" + gameItem[i].value + "'>" + gameItem[i].label + "</option>");
                }
            }
            var $serverName = $("#serverName").val();
            if ($serverName != "empty") {

            }
            else {
                initServerName();
            }

        }
    });

}

function initServerName() {
    var gameId = $("#gameName").val();
    $.ajax({
        type:'post',
        url:'initDropdownMenu?action=doInitOrderServerNameDrowdownMenu',
        dataType:'json',
        data:{gameID:gameId},
        success:function (data) {
            var serverObj = data;
            var $server = $("#serverName");
            $server.empty();
            $server.append("<option value='empty' selected='true'>-----------所有----------</option>");
            for (var i = 0; i < serverObj.length; i++) {
                $server.append("<option value='" + serverObj[i].value + "'>" + serverObj[i].label + "</option>");
            }
        }
    });
}


function initPayBankOrSomething() {
    var channelIdValue = $("#channelId").val();
    var subTypeValue = $("#subType").val();
    if (subTypeValue != "empty") {
        $.ajax({
            type:'post',
            url:'initDropdownMenu?action=doInitOrderSubTypeTagDrowdownMenu',
            dataType:'json',
            data:{channelId:channelIdValue, subType:subTypeValue},
            success:function (data) {
                var bankObj = data;
                var $bank = $("#bank");
                $bank.empty();
                $bank.append("<option value='empty' selected='true'>-----------所有----------</option>");
                for (var i = 0; i < bankObj.length; i++) {
                    $bank.append("<option value='" + bankObj[i].value + "'>" + bankObj[i].label + "</option>");
                }
            }
        });
    }
    else {
        $("#bank ").val("empty");
    }
}

var showhide = true;
function showOrHide() {
    if (showhide) {
//        $("#queryHead").hide();
        showQueryCondition();
    }
    else {
//        $("#queryHead").show();
        hideQueryCondition();

    }
}
var mytimer = null;
function timeInFn() {
    mytimer = window.setTimeout('showQueryCondition()', 1000);
}
function timeOutFn() {
    clearTimeout(mytimer);
    mytimer = -1;
}
function showQueryCondition() {
    initGameName();
    initServerName();
//    $("#queryHead").show();
    var $width = $("#hideorshow").width();
    $("#queryHead").css("width", $width);
    easyDialog.open({
        container:'queryHead',
        fixed:false,
//        overlay:false,
        follow:'hidehref',
        followX:-parseInt($width) / 2 + 36,
        followY:14
    });
    showhide = false;
    $("#hidehref").html("隐藏搜索条件");
    $("#queryHead").addClass("shadow");
}

function hideQueryCondition() {
    $("#queryHead").hide();
    $("#hidehref").html("显示搜索条件");
    showhide = true;
}

function userInfoSelect() {
    selectItem($("#queryUserType"), $("#userId"));
}

function playerInfoSelect() {
    selectItem($("#queryPlayerType"), $("#playerId"));
}

function selectItem(queryType, inputText) {
    inputText.val('');
    var selectValue = queryType.val();
    if (selectValue != "empty") {
        inputText.removeAttr('disabled');
    }
    else {
        inputText.attr('disabled', true);
    }
}

function pageItemChange() {
    page = 1;
    request();
}

function selectTrChangeColor() {
    $("#showorderDateTable tr").mouseover(function () {
        $(this).css("background-color", "#c5c6cc");
    });

    $("#showorderDateTable tr").mouseout(function () {
        $(this).css("background-color", "white");
    });
}


function pageChange(e) {
    if (e.keyCode == 13) {
        request();
    }
}

//转换成按钮或者字符串
function showLabel(loseOrNotObj, orderIdNeeded) {
    var returnLable;
    if (loseOrNotObj == true) {
        returnLable = "<input type='button'  value='补单'  onmouseover=this.style.borderColor='#75cd02' onmouseout=this.style.borderColor='#dcdcdc' class=btn1 pbtn2 style=border-color: rgb(220, 220, 220); background:url('\images\/ok.gif') no-repeat 8px center ; onclick='rechargeOrder(\"" + orderIdNeeded + "\")'/>";
    }
    else {
        returnLable = "<strong style='color: green;'>否</strong>";
    }
    return returnLable;
}

function showLinkToUserAccount(value) {
    return   "<a href='#' onclick='showUserAccountDiv(\"" + value + "\")'>" + value + "</a>";
}

function showUserAccountDiv(value) {
    var $accountId = value;
    if ($accountId.length > 0) {
        $.ajax({
            url:'user?action=getAccountInfo&queryAccountType=userId',
            data:{accountId:$accountId},
            datatype:'json',
            type:'post',
            success:function (msg) {
                var $data = msg.object[0];
                if ($data != null) {
                    $("#accountInfoThead").show();
                    var $tr = createTR().append(createTD($data.id)).
                        append(createTD($data.userName)).
                        append(createTD($data.email)).
                        append(createTD(clearNull($data.createTime))).
                        append(createTD(clearNull($data.lastLoginTime))).
                        append(createTD($data.lastIp)).
                        append(createTD($data.loginSum)).
                        append(createTD($data.status));
                    $("#accountInfoTbody").show().empty().append($tr);
                    $.blockUI({
                        message:$('#accountInfo'),
                        css:{
                            top:'50%',
                            left:'30%',
                            textAlign:'left',
                            marginLeft:'-220px',
                            marginTop:'-145px',
                            width:'850px',
                            background:'white'
                        }
                    });
                    $('.blockOverlay').click($.unblockUI);
                    $('.close').attr('title', '单击关闭').click($.unblockUI);
                }
                else {
//                    $("table").hide();
                    $("accountInfo").hide();
                    alert('该用户信息不存在!');
                }
            }
        });

    }
    else {
        $("accountInfo").hide();
        alert('该用户信息不存在!');
    }


}

//完成补单操作
function rechargeOrder(orderIdObj) {
    $.ajax({
        url:'Recharge?action=replenishOrderByID',
        type:'post',
        data:{order:orderIdObj},
        datatype:'json',
        success:function (data) {
            if (data) {
                if (contains($.trim(data), 'success', true)) {
                    $("td:contains(\"" + orderIdObj + "\")").siblings("td:last").empty().append("<strong style='color: green;'>否</strong>");
                    alert('补单成功');
                }
                else {
                    alert(data);
                }
            }
            else {
                alert('服务器繁忙');
            }
        }

    });
}
//实现字符串的contains方法
function contains(string, substr, isIgnoreCase) {
    if (isIgnoreCase) {
        string = string.toLowerCase();
        substr = substr.toLowerCase();
    }
    var startChar = substr.substring(0, 1);
    var strLen = substr.length;
    for (var j = 0; j < string.length - strLen + 1; j++) {
        if (string.charAt(j) == startChar)//如果匹配起始字符,开始查找
        {
            if (string.substring(j, j + strLen) == substr)//如果从j开始的字符与str匹配，那ok
            {
                return true;
            }
        }
    }
    return false;
}

function getQueryConditionTips() {
    //查询条件获取
    var orderId = $("#orderId").val();
    var endId = $("#endId").val();
    var queryUserType = $("#queryUserType").val();
    var userId = $("#userId").val();
//    var queryPlayerType = $("#queryPlayerType").val();
    var playerId = $("#playerId").val();
    var gameName = $("#gameName").val();
    var serverName = $("#serverName").val();
    var startMoney = $("#startMoney").val();
    var endMoney = $("#endMoney").val();
    var channelId = $("#channelId").val();
    var subType = $("#subType").val();
    var bank = $("#bank").val();
    var status = $("#status").val();
    var payStartTime = $("#payStartTime").val();
    var payEndTime = $("#payEndTime").val();
    var assertStartTime = $("#assertStartTime").val();
    var assertEndTime = $("#assertEndTime").val();
    var pageItemNumber = $("#pageItemNumber").attr("value");

    alert(orderId);
    var returnStr;
    if (orderId) {
        returnStr = returnStr + "订单号：" + orderId.toString();
    }

    return orderId;
}

function createSpan($msg) {
    return $("<span>" + $msg + "</span>");
}

//异步导出查询的数据到Excel
function exportQueryData() {
    //查询条件获取
    var orderId = $("#orderId").val();
    var endId = $("#endId").val();
    var queryUserType = $("#queryUserType").val();
    var userId = $("#userId").val();
    var queryPlayerType = $("#queryPlayerType").val();
    var playerId = $("#playerId").val();
    var gameName = $("#gameName").val();
    var serverName = $("#serverName").val();
    var startMoney = $("#startMoney").val();
    var endMoney = $("#endMoney").val();
    var channelId = $("#channelId").val();
    var subType = $("#subType").val();
    var bank = $("#bank").val();
    var status = $("#status").val();
    var payStartTime = $("#payStartTime").val();
    var payEndTime = $("#payEndTime").val();
    var assertStartTime = $("#assertStartTime").val();
    var assertEndTime = $("#assertEndTime").val();
    var pageItemNumber = $("#pageItemNumber").attr("value");
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
        //拼接查询条件
        var url = '&pageNumber=' + page + '&pageItemNumber=' + pageItemNumber + '&orderId=' + orderId + '&endId=' + endId + '&queryUserType=' + queryUserType + '&userId=' + encodeURI(encodeURI(userId)) +
            '&queryPlayerType=' + queryPlayerType
            + '&playerId=' + playerId + '&gameName=' + gameName + '&serverName=' + serverName + '&startMoney=' + startMoney + '&endMoney=' + endMoney + '&channelId=' + channelId
            + '&subType=' + subType + '&bank=' + bank + '&status=' + status + '&payStartTime=' + payStartTime + '&payEndTime=' + payEndTime + '&assertStartTime=' + assertStartTime +
            '&assertEndTime=' + assertEndTime;
        window.open('order?action=exportQueryData' + url, 'blank');
        hideQueryCondition();
    }
}
//判断给定的值是不是空或者选择的是empty，空的返回true
function checkNullValue(value) {
    if (value.length == 0 || value == 'empty')
        res = true;
    else
        res = false;
    return res;
}
function transDigitalToString(value) {
    if (value == '0') {
        return '正常';
    }
}
function keyupEvent(event) {
    if (event.keyCode == 13) {
        page = $("#pageNumber").attr("value");

        if (page > parseInt($("#totalPage").val())) {
            alert('超出了最大页数,请重新输入！');
            $("#pageNumber").attr("value", 1);
            $("#pageNumber").focus().select();
            page = 1;
        }
        else {
            request();
        }
    }
}

function showQueryDiv()
{
    $.fancybox({
        type:'inline',
        maxWidth	: 600,
        maxHeight	: 600,
        width		: '70%',
        height		: '70%',
        autoSize	: false,
        href:'#queryHeadGrid',
        title:'点击弹出层之外可以关闭弹出层!'
    });
}

function queryOrderByGrid()
{
    $.fancybox.close();
    var orderId = $("#orderIdGrid").val();
    var endId = $("#endIdGrid").val();
    var queryUserType = $("#queryUserTypeGrid").val();
    var userIdOrName = $("#userIdGrid").val();
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


    var f_v_orderId = $("#f_orderIdGrid").val();
    var f_v_endId = $("#f_endIdGrid").val();
    var f_v_queryUserType = $("#f_queryUserTypeGrid").val();
    var f_v_userIdOrName = $("#f_userIdGrid").val();
    var f_v_queryPlayerType = $("#f_queryPlayerTypeGrid").val();
    var f_v_playerId = $("#f_playerIdGrid").val();
    var f_v_gameName = $("#f_gameNameGrid").val();
    var f_v_serverName = $("#f_serverNameGrid").val();
    var f_v_startMoney = $("#f_startMoneyGrid").val();
    var f_v_endMoney = $("#f_endMoneyGrid").val();
    var f_v_channelId = $("#f_channelIdGrid").val();
    var f_v_subType = $("#f_subTypeGrid").val();
    var f_v_bank = $("#f_bankGrid").val();
    var f_v_status = $("#f_statusGrid").val();
    var f_v_payStartTime = $("#f_payStartTimeGrid").val();
    var f_v_payEndTime = $("#f_payEndTimeGrid").val();
    var f_v_assertStartTime = $("#f_assertStartTimeGrid").val();
    var f_v_assertEndTime = $("#f_assertEndTimeGrid").val();

    var f_v_orderGridCounts=$("#f_orderGridCounts").val();

    $("#orderTable").jqGrid('setGridParam',
        {
            url:"order?action=getOrderByGrid&type=query",
            postData:{
                orderId:orderId,
                endId:endId,
                queryUserType:queryUserType,
                userIdOrName:userIdOrName,
                playerId:playerId,
                gameId:gameName,
                serverId:serverName,
                startMoney:startMoney,
                endMoney:endMoney,
                channelId:channelId,
                subType:subType,
                subTypeTag:bank,
                status:status,
                payStartTime:payStartTime,
                payEndTime:payEndTime,
                assertStartTime:assertStartTime,
                assertEndTime:assertEndTime,

                f_orderGridCounts:f_v_orderGridCounts,

                f_orderId:f_v_orderId,
                f_endId:f_v_endId,
                f_queryUserType:f_v_queryUserType,
                f_userIdOrName:f_v_userIdOrName,
                f_playerId:f_v_playerId,
                f_gameId:f_v_gameName,
                f_serverId:f_v_serverName,
                f_startMoney:f_v_startMoney,
                f_endMoney:f_v_endMoney,
                f_channelId:f_v_channelId,
                f_subType:f_v_subType,
                f_subTypeTag:f_v_bank,
                f_status:f_v_status,
                f_payStartTime:f_v_payStartTime,
                f_payEndTime:f_v_payEndTime,
                f_assertStartTime:f_v_assertStartTime,
                f_assertEndTime:f_v_assertEndTime
            } //发送数据
        }).trigger("reloadGrid"); //重新载入

}
//$(function(){
//
//    $("#serverName").live("change",function(){ //动态添加，阻止事件冒泡
//        showhide = true;
//        showOrHide();
//        showhide = true;
//    });
//
//}) ;
