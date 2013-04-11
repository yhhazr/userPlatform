/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 上午10:32
 * 精简展示和分页展示
 */

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

//定义全局的页码 和总页码
var $page = $("#pageOfData").val();
var $totalPageOfData = null;
var $pageCount = null;

function selectAll() {
    $("input[type='checkbox']").each(function () {
        $(this).attr("checked", true);
    });
}
//展示最基本的统计数据
function showdStatisticData() {
    $.md({modal:"#footDiv"});
    $.ajax({
        url:'DataStatistic?action=showBasicDataStatistical',
        dataType:'json',
        success:function (data) {
            if (data == null) {
                $("#showMsg").empty();
                $("#showMsg").append('没有查询到符合要求的数据！');
            }
            else {
                var json = data;
                if (json.length > 0) {
                    $("#emptyMsg").empty();
                    $("#overAll").empty().show().append('基本统计数据');
                    var $tr = createTR();
                    $tr.append(createTD('支付网关')).append(createTD('充值金额')).append(createTD('游戏币')).append(createTD('订单数量')).append(createTD('用户数量')).append(createTD('玩家数量'));
                    $("#countResultHead").empty().show().append($tr);
                    var trObj = $("#dataBodyContent");
                    trObj.empty();
                    for (var i = 0; i < json.length; i++) {
                        var $tr = createTR().append(createTD(json[i].channelName)).
                            append(createTD('￥' + json[i].sumMoney)).
                            append(createTD(changeGoldImgByName(json[i].gameName)+json[i].gameGoldCount)).
                            append(createTD(json[i].orderCount)).
                            append(createTD(json[i].userCount)).
                            append(createTD(json[i].playerCount));
                        trObj.append($tr);
                    }
                    $("#showorderDateTableofData").hide();
                }
                else {
                    $("#emptyMsg").empty();
                    $("#emptyMsg").append("没有查询到符合要求的数据!");
                }
            }
            $("#footDiv").css("display", "none");
        }
    });
}

function changeGoldImgByName(gameName)
{
    if(gameName=='神曲')
    {
        return  '<img src="../images/sq_zs.gif" height="16px" width="16px" >';
    }
    else
    {
        return  '<img src="../images/dq.png" height="16px" width="16px" >';
    }

}

$('#accountId')
    .bind('keyup', function (event) {
        // aa(event);
        if (event.keyCode == 13) {
            queryUserAccountByID();
        }
    });
//拿到定制的数据
function showCustomizeStatistical() {
    $("#overAll").empty();
    $("#countResultHead").empty();
    $("#dataBodyContent").empty();
    $.md({modal:"#footDiv"});
    //1，获得是否选择条件的条件
    var $checkPayStartTime = $("#checkPayStartTime").attr("checked");
    var $checkAssertStartTime = $("#checkAssertStartTime").attr("checked");
    var $checkGameName = $("#checkGameName").attr("checked");
    var $checkServerName = $("#checkServerName").attr("checked");
    var $checkChannelId = $("#checkChannelId").attr("checked");
    var $checkSubType = $("#checkSubType").attr("checked");
    var $checkBank = $("#checkBank").attr("checked");
    var $checkStatus = $("#checkStatus").attr("checked");
    var $checkGold = $("#gold").attr("checked");
    var $checkUserAmount = $("#userAmount").attr("checked");
    var $checkPlayerAmount = $("#playerAmount").attr("checked");
    var $checkStatusAmount = $("#statusAmount").attr("checked");
    //2，得到选择的条件，
    var $payStartTime = $("#payStartTime").val();
    var $payEndTime = $("#payEndTime").val();
    var $assertStartTime = $("#assertStartTime").val();
    var $assertEndTime = $("#assertEndTime").val();
    var $gameName = transEmptyToAll($("#gameName"));
    var $serverName = transEmptyToAll($("#serverName"));
    var $channelId = transEmptyToAll($("#channelId"));
    var $subType = transEmptyToAll($("#subType"));
    var $bank = transEmptyToAll($("#bank"));
    var $status = $("#status").val();
    var $amount = $("#amount").val();
    var $gold = $("#gold").val();
    var $userAmount = $("#userAmount").val();
    var $playerAmount = $("#playerAmount").val();
    var $statusAmount = $("#statusAmount").val();
    $page = $("#pageNumberOfData").val();
//    $pageCount = $("#pageItemNumberOfData").val();
    // 3，然后传到servlet ,拿到数据并且展示出来
    $.ajax({
        url:'DataStatistic?action=showCustomizeDataStatistical',
        type:'post',
        dataType:'json',
        data:{
            //选择标记
            "$checkPayStartTime":$checkPayStartTime,
            "$checkAssertStartTime":$checkAssertStartTime,
            "$checkGameName":$checkGameName,
            "$checkServerName":$checkServerName,
            "$checkChannelId":$checkChannelId,
            "$checkSubType":$checkSubType,
            "$checkBank":$checkBank,
            "$checkStatus":$checkStatus,
            "$checkGold":$checkGold,
            "$checkUserAmount":$checkUserAmount,
            "$checkPlayerAmount":$checkPlayerAmount,
            "$checkStatusAmount":$checkStatusAmount,
            //真实数据
            "$payStartTime":$payStartTime,
            "$payEndTime":$payEndTime,
            "$assertStartTime":$assertStartTime,
            "$assertEndTime":$assertEndTime,
            "$gameName":$gameName,
            "$serverName":$serverName,
            "$channelId":$channelId,
            "$subType":$subType,
            "$bank":$bank,
            "$page":$page,
            "$pageCount":$pageCount,
            "$status":$status
        },
        success:function (data) {
            //1,在表的头部上面增加整体信息
            $("#overAll").empty().show().append(data.overAll);
            //2,显示表头
            var $tr = createTR();
            for (var i = 0; i < data.head.length; i++) {
                $tr.append(createTD(data.head[i]));
            }
            $("#countResultHead").empty().show().append($tr);
            //3,显示表的内容
            $("#dataBodyContent").empty();
            var $contentItems = data.dataContent;
            for (var j = 0; j < $contentItems.length; j++) {
                var $contend = $contentItems[j];
                var $bodyTR = createTR();
                if ($contend.gameName != undefined)
                    $bodyTR.append(createTD($contend.gameName));
                if ($contend.serverName != undefined)
                    $bodyTR.append(createTD($contend.serverName));
                if ($contend.channelName != undefined)
                    $bodyTR.append(createTD($contend.channelName));
                if ($contend.subTypeName != undefined)
                    $bodyTR.append(createTD($contend.subTypeName));
                if ($contend.subTagName != undefined)
                    $bodyTR.append(createTD($contend.subTagName));
                if ($contend.status != undefined)
                    $bodyTR.append(createTD($contend.status));
                if ($contend.sumMoney != undefined)
                    $bodyTR.append(createTD('￥' + $contend.sumMoney));
                if ($contend.goldSum != undefined)
                    $bodyTR.append(createTD(changeGoldImgByName($contend.gameName)+$contend.goldSum));
                if ($contend.userSum != undefined)
                    $bodyTR.append(createTD($contend.userSum));
                if ($contend.playerSum != undefined)
                    $bodyTR.append(createTD($contend.playerSum));
                $("#dataBodyContent").append($bodyTR);
            }

            $pageCount = $("#pageItemNumberOfData").val();
            var $t = parseInt(data.total) % parseInt($pageCount);
            var $pageT = parseInt(parseInt(data.total) / parseInt($pageCount));
            if ($t == 0)
                $totalPageOfData = $pageT;
            else
                $totalPageOfData = parseInt($pageT) + parseInt(1);

            $("#showorderDateTableofData").show();
            $("#totalItemsOfData").empty().append("共查到" + data.total + "条数据,共" + $totalPageOfData + "页");
            if (parseInt(data.total) <= parseInt($pageCount)) {  //如果拿到的数据条数少于每页的条数，影藏页面导航
                $("#showorderDateTableofData").hide();
            }
            hideQueryCondition();
            $("#footDiv").css("display", "none");
            $("#tips").hide();
        }
    })
    ;
}


function pageItemChangeOfData() {
    $page = 1;
    $("#pageNumberOfData").val($page);
    $pageCount = $("#pageItemNumberOfData").val();
    showCustomizeStatistical();
}
function keyupEventOfData(event) {
    if (event.keyCode == 13) {
        $page = $("#pageNumberOfData").attr("value");

        if (parseInt($page) > parseInt($totalPageOfData)) {
            alert('超出了最大页数,请重新输入!');
            $("#pageNumberOfData").attr("value", 1);
            $page = 1;
            $("#pageNumberOfData").focus().select();
        }
        else {
            showCustomizeStatistical();
        }
    }
}
function firstOfData() {
    $page = 1;
    $("#pageNumberOfData").val($page);
    showCustomizeStatistical();
}
function preOfData() {
    $page = parseInt($page) - parseInt(1);
    if (parseInt($page) >= 1) {
        $("#pageNumberOfData").attr("value", $page);
        showCustomizeStatistical();
    } else {
        alert("已经是第一页！");
        $page = parseInt($page) + parseInt(1);
    }

}
function nextOfData() {
    $page = parseInt($page) + parseInt(1);
    if (parseInt($page) <= parseInt($totalPageOfData)) {
        $("#pageNumberOfData").attr("value", $page);
        showCustomizeStatistical();
    }
    else {
        alert("已经是最后一页！");
        $page = parseInt($page) - parseInt(1);
    }
}
function lastOfData() {
    $page = $totalPageOfData;
    $("#pageNumberOfData").attr("value", $page);
    showCustomizeStatistical();
}
function reflashCurrentPageOfData() {
    $("#pageNumberOfData").attr("value", $page);
    showCustomizeStatistical();
}
//如果选的是empty，传回all，否则传回实际的值
function transEmptyToAll(value) {
    var relvalue = null;
    if (value.val() == "empty")
        relvalue = "all";
    else
        relvalue = value.find("option:selected").text();
    return relvalue;
}
//如果不为空，就增加一行
function appendNanNull(value, TR) {
    if (value != undefined) {
        TR.append(createTD(value));
    }
    return TR;
}
//导出统计的数据
function exportStatisticalData() {
//    //1，获得是否选择条件的条件
    var checkPayStartTime = $("#checkPayStartTime").attr("checked");
    var checkAssertStartTime = $("#checkAssertStartTime").attr("checked");
    var checkGameName = $("#checkGameName").attr("checked");
    var checkServerName = $("#checkServerName").attr("checked");
    var checkChannelId = $("#checkChannelId").attr("checked");
    var checkSubType = $("#checkSubType").attr("checked");
    var checkBank = $("#checkBank").attr("checked");
    var checkStatus = $("#checkStatus").attr("checked");
    var checkGold = $("#gold").attr("checked");
    var checkUserAmount = $("#userAmount").attr("checked");
    var checkPlayerAmount = $("#playerAmount").attr("checked");
    var checkStatusAmount = $("#statusAmount").attr("checked");
    //2，得到选择的条件，
    var payStartTime = $("#payStartTime").val();
    var payEndTime = $("#payEndTime").val();
    var assertStartTime = $("#assertStartTime").val();
    var assertEndTime = $("#assertEndTime").val();
    var gameName = transEmptyToAll($("#gameName"));
    var serverName = transEmptyToAll($("#serverName"));
    var channelId = transEmptyToAll($("#channelId"));
    var subType = transEmptyToAll($("#subType"));
    var bank = transEmptyToAll($("#bank"));
    var status = $("#status").val();
    var amount = $("#amount").val();
    var gold = $("#gold").val();
    var userAmount = $("#userAmount").val();
    var playerAmount = $("#playerAmount").val();
    var statusAmount = $("#statusAmount").val();
    //放到数组里
    var arr = new Array();
    arr.push(checkPayStartTime);
    arr.push(checkAssertStartTime);
    arr.push(checkGameName);
    arr.push(checkServerName);
    arr.push(checkChannelId);
    arr.push(checkSubType);
    arr.push(checkBank);
    arr.push(checkStatus);
    arr.push(checkGold);
    arr.push(checkUserAmount);
    arr.push(checkPlayerAmount);
    arr.push(checkStatusAmount);
    var nullOrNot = true;
    $.each(arr, function (key, val) {
        nullOrNot = nullOrNot && checkIsChecked(val);
//        alert('key:'+key+' value:'+val + '结果：'+nullOrNot);
    });

    if (nullOrNot) {
        alert('请选择导出统计数据的列！');
    }
    else {
        //拼接查询条件
        var url = '&checkPayStartTime=' + checkPayStartTime + '&checkAssertStartTime=' + checkAssertStartTime + '&checkGameName=' + checkGameName + '&checkServerName=' + checkServerName + '&checkChannelId=' + checkChannelId + '&checkSubType=' + checkSubType + '&checkBank=' + checkBank
            + '&checkStatus=' + checkStatus + '&checkGold=' + checkGold + '&checkUserAmount=' + checkUserAmount + '&checkPlayerAmount=' + checkPlayerAmount + '&checkStatusAmount=' + checkStatusAmount + '&payStartTime=' + payStartTime
            + '&payEndTime=' + payEndTime + '&assertStartTime=' + assertStartTime + '&assertEndTime=' + assertEndTime + '&gameName=' + encodeURI(encodeURI(gameName)) + '&serverName=' + encodeURI(encodeURI(serverName)) + '&channelId=' + encodeURI(encodeURI(channelId))
            + '&subType=' + encodeURI(encodeURI(subType)) + '&bank=' + encodeURI(encodeURI(bank)) + '&status=' + encodeURI(encodeURI(status)) + '&amount=' + amount
            + '&gold=' + gold + '&userAmount=' + userAmount + '&playerAmount=' + playerAmount + '&statusAmount=' + statusAmount;
        window.open('DataStatistic?action=exportStatisticalData' + url);
        hideQueryCondition();
//        showCustomizeStatistical();

    }
}
function checkIsChecked(value) {
    var rel = null;
    if ("checked" == value)
        rel = false;
    else
        rel = true;
    return rel;
}
function openSpecfiyWindown(windowName) {
    window.open('about:blank', windowName);
}


function StatisticalPageItemChange() {
    page = 1;
    request();
}


function returnToOrignOfData() {
    $("#dataStatisticalFrom")[0].reset();
    $page = 1;
    $("#pageNumberOfData").val($page);
    showCustomizeStatistical();
}



