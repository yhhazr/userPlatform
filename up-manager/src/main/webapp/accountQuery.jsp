<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-7-19
  Time: 上午10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户信息查询</title>
</head>
<body>

<div style="text-align: center;background-color:#d6f7ff;">
    <br>
    <select id="queryAccountType" name="queryUserType" size="1"
            style="width: 90px;">
        <option value="userId">用户ID</option>
        <option value="userName" selected="true">用户名</option>
    </select>
    <input type="text" size="20" id="accountId" name="accountId"> <input id="queryUserAccountByIDBtn" type="button"
                                                                         onclick="queryUserAccountByID();"
                                                                         value="查询用户">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" id="queryUserLogByIDBtn"
           onclick="queryUserLogByID();"
           value="查询用户日志">


</div>
<div style="text-align: center;">
    <h2 id="returnMsg" style="text-align: center;"></h2>

    <table class="table table-bordered">

        <thead style="display: none;" id="userAccountThead">
        <th>用户ID</th>
        <th>用户名</th>
        <th>用户Email</th>
        <th>注册时间</th>
        <th>最后登录时间</th>
        <th>最后登录IP</th>
        <th>登录次数</th>
        <th>用户状态</th>
        <th>重置密码</th>
        <th>重置邮箱</th>
        </thead>
        <tbody id="userAccountTbody">

        </tbody>
    </table>
</div>
<div id="userLogDiv">
    <table id="userLogTable" style="width: 600px;">
    </table>
    <div id="userLogPager" style="height: 30px;"></div>
</div>
<div id="inputNewPsw" style="display: none;text-align: center;">
    <h3 style="color: green;text-align: center;font: bold;">重置密码</h3>

    <form id="resetPswFrom" action="user?action=resetMailAndPsw&type=psw" method="post">
        用户ID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;<input id="userId" name="userId"
                                                                                type="text" size="20"
                                                                                readonly="true">
        <br>
        请输入新密码 <input id="userNewPsw" name="userNewPsw" type="password" size="20"> <br>
        再次输入密码 <input id="userNewPswAgain" name="userNewPswAgain" type="password" size="20"> <br>
        <input type="submit" value="提交" style="width: 80px;background-color: green;">
    </form>
</div>

<div id="inputNewEmail" style="display: none;text-align: center;">
    <h3 style="color: green;text-align: center;font: bold;">重置邮箱</h3>

    <form id="resetEmailFrom" action="user?action=resetMailAndPsw&type=mail" method="post">
        用户ID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input id="email_userId"
                                                                                name="email_userId"
                                                                                type="text" size="20"
                                                                                readonly="true"> <br>
        请输入新邮箱 <input id="emailAddress" name="emailAddress" type="text" size="20"> <br>
        <input type="submit" value="提交" style="width: 80px;background-color: green;">
    </form>
</div>


<script type="text/javascript" src="js/simple.js"></script>
<script type="text/javascript">
$("#userLogTable").jqGrid({
    url:'user?action=queryUserLog',
    datatype:"json",
    colNames:['日志ID', '用户名', '日志内容', '详情1', '详情2', '详情3', '日志创建时间', '日志类型'],
    colModel:[
        {name:'id', index:'id', width:30, editable:false, editoptions:{readonly:true, size:10}},

        {name:'userName', index:'userName', width:30, editable:true, editoptions:{size:25}},
        {name:'content', index:'content', width:300, editable:true, editoptions:{size:25}},
        {name:'ext1', index:'ext1', width:100, editable:true, editoptions:{size:10}}
        ,
        {name:'ext2', index:'ext2', width:100, editable:true, editoptions:{size:10}}
        ,
        {name:'ext3', index:'ext3', width:100, editable:true, editoptions:{size:10}},
        {name:'log_time', index:'log_time', width:80, align:"left", editable:true, editoptions:{size:10}},
        {name:'logType', index:'logType', width:50, editable:true, editoptions:{size:25}}
    ],
    rowNum:10,
    rowList:[10, 20, 30],
    pager:'#userLogPager',
    sortname:'id',
    viewrecords:true,
    sortorder:"asc",
    caption:"用户日志信息",
    autowidth:true,
    height:300,
    editurl:"",
    loadComplete:function (data) { //完成服务器请求后，回调函数
        if (data != null && data.rows == null) {
            alert("没有查到用户的日志信息");
        }
    }
});
function queryUserLogByID() {
    var queryAccountType = $("#queryAccountType").val();
    var accountId = $("#accountId").val();
    jQuery("#userLogTable").jqGrid('setGridParam', {url:"user?action=queryUserLog&accountId=" + accountId + "&queryAccountType=" + queryAccountType + "&date=" + new Date().getTime()}).trigger("reloadGrid");

}

$(function () {
    $('#resetPswFrom').ajaxForm({
        beforeSubmit:validatePsw, //验证表单
        success:function (data) {
            if (data.code == 200) { //如果成功提交
                queryUserAccountByID();
                queryUserLogByID();
                $.fancybox().close();
            } else {
                alert(data.msg);
            }
        }
    });
    $('#resetEmailFrom').ajaxForm({
        beforeSubmit:validateEmail, //验证表单
        success:function (data) {
            if (data.code == 200) { //如果成功提交
                queryUserAccountByID();
                queryUserLogByID();
                $.fancybox().close();
            } else {
                alert(data.msg);
            }
        }
    });
});

function validatePsw() {
    var flag = false;
    if (!$("#userId").val()) {
        alert('请先选择用户数据行！');
    }
    else {
        if (!$("#userNewPsw").val() || !$("#userNewPswAgain").val()) {
            alert('请输入新密码或确认密码!');
        }
        else {
            if ($("#userNewPsw").val().length < 6 || $("#userNewPsw").val().length > 20) {
                alert('密码长度小于6位或大于20位!');
            }
            else {
                if ($("#userNewPsw").val() == $("#userNewPswAgain").val()) {
                    flag = true;
                }
                else {
                    alert('新密码和确认密码不一致!');
                }
            }
        }

    }
    return flag;
}

function validateEmail() {
    var flag = false;
    if (!$("#email_userId").val()) {
        alert('请先选择用户数据行！');
    }
    else {
        if (isWhiteWpace($("#emailAddress").val())) {
            alert('邮箱地址中不能有空格!');
        }
        else {
            flag = true;
        }

    }
    return flag;
}

function validate(formData, jqForm, options) {
    for (var i = 0; i < formData.length; i++) {
        if (!formData[i].value) {
            alert("请输入完整相关信息");
            return false;
        }
    }
}

function queryUserAccountByID() {
    var $queryAccountType = $("#queryAccountType").val();
    var $accountId = $("#accountId").val();
    if ($accountId.length > 0) {
        $.ajax({
            url:'user?action=getAccountInfo',
            data:{queryAccountType:$queryAccountType, accountId:$accountId},
            datatype:'json',
            type:'post',
            success:function (msg) {
                var $dataObject = msg.object;
                if ($dataObject != null) {
                    if ($dataObject.length > 0) {
                        var $trObj = $("#userAccountTbody");
                        $trObj.empty().show();
                        for (var i = 0; i < $dataObject.length; i++) {
                            $data = $dataObject[i];
                            var $tr = createTR().append(createTD($data.id)).
                                    append(createTD($data.userName)).
                                    append(createTD($data.email)).
                                    append(createTD(clearNull($data.createTime))).
                                    append(createTD(clearNull($data.lastLoginTime))).
                                    append(createTD($data.lastIp)).
                                    append(createTD($data.loginSum)).
                                    append(createTD($data.status)).
                                    append(createTD("<input type='button' id='' value='重置密码' onclick='resetUserPsw(\"" + $data.id + "\")'/>")).append(createTD("<input type='button' id='' value='重置邮箱' onclick='resetUserEmail(\"" + $data.id + "\")'/>"));
                            $trObj.append($tr);
                        }
                        $("#returnMsg").empty().append('<img src="../images/success.jpg" alt="success">' + msg.msg);
                        $("table").show();
                        $("#userAccountThead").show();
                    }
                    else {
                        $("table").hide();
                        alert('没有查到信息！');
                    }
                }
                else {
                    $("table").hide();
                    alert('查询的用户信息不存在！');
                }
            }
        });
    }
    else {
        $("#returnMsg").empty();
        $("table").hide();
        alert('请输入用户ID或者用户名！');
    }
}

function resetUserPsw(id) {
    $.ajax({
        type:"post",
        url:"/master?action=getPermissionInfo",
        dataType:"json",
        success:function (data) {
            if (data != null) { //订单菜单
                if (data.resetMailAndPsw == "true") {
                    $.fancybox({
                        type:'inline',
                        width:350,
                        href:'#inputNewPsw',
                        title:'重置用户密码'
                    });
                    $("#userId").val(id);
                }
                else {
                    alert('您没有使用该功能的权限!');
                }

            }
        }
    });
}
function resetUserEmail(id) {
    $.ajax({
        type:"post",
        url:"/master?action=getPermissionInfo",
        dataType:"json",
        success:function (data) {
            if (data != null) { //订单菜单
                if (data.resetMailAndPsw == "true") {
                    $.fancybox({
                        type:'inline',
                        width:350,
                        href:'#inputNewEmail',
                        title:'重置密保邮箱'
                    });
                    $("#email_userId").val(id);
                }
                else {
                    alert('您没有使用该功能的权限!');
                }
            }
        }
    });
}


</script>
</body>
</html>
