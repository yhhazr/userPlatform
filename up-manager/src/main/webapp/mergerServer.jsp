<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-24
  Time: 下午12:49
  合服页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <div id="leftDiv" style="width: 15%;height: 700px;float: left;
color: #336699;
background-color:#ffffff;
border: solid 1px silver;
overflow: scroll;
scrollbar-face-color: #ffd700;
scrollbar-shadow-color: #e0ffff;
scrollbar-highlight-color: #fa8072;
scrollbar-3dlight-color: #ff0000;
scrollbar-darkshadow-color: #afeeee;
scrollbar-track-color: #ffb6c1;
scrollbar-arrow-color: #e6e6fa;
">
        区服结构列表<br>

        <div id="serverDiv" class="ztree"></div>
    </div>
    <div id="contentDiv" style="width: 84%;height: 100%;float: right;">
        <br>

        <div id="showServerInfoDiv" style="width: 100%;border-style:solid;border-width:2pt; border-color:#b6ecff;">

            <table style="width: 100%;height: 60px;display: none;font:italic normal bold 12pt Arial;rules:All">
                <thead>
                <tr>
                    <th colspan="3" align="left"><h2>服务区信息</h2>
                        <hr>
                    </th>
                </tr>
                </thead>
                <tr>
                    <td>ID: <span id="serverId"></span></td>
                    <td>编号：<span id="serverNo"></span></td>
                    <td>服务器名称：<span id="serverName"></span></td>
                </tr>
                <tr>
                    <td>服务器状态：<span id="serverStatus"></span></td>
                    <td>创建时间：<span id="createTime"></span></td>
                    <td>开服时间：<span id="openTime"></span></td>
                </tr>
            </table>
        </div>
        <hr>
        <div id="mergerDiv"
             style="width: 100%;background-color:rgba(118,213,254,0);border-style:solid;border-width:2pt; border-color:#b6ecff;">
            <h4>合并服务区</h4>

            <form id="merger_server" action="/merger?action=mergerServerInAction"
                  method="post" onsubmit="mergerServer();">
                <div id="menuContent" class="menuContent"
                     style="display:none; position: absolute;background-color: #ffffff;">
                    <ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 200px;"></ul>
                </div>
                请选择主区：<input id="mainServerSel" type="text" readonly value="" style="width:120px;"
                             onclick="showMainServer();"/> <input type="hidden" id="checkedMainServer"
                                                                  name="checkedMainServer"><input type="hidden"
                                                                                                  id="checkedMainServerId"
                                                                                                  name="checkedMainServerId">
                &nbsp;<a id="mainMenuBtn" href="#" onclick="showMainServer(); return false;">选择主区</a><br>
                请选择副区：
                <textarea id="serverSel" rows="6" cols="20" readonly onclick="showServer();"></textarea>

                &nbsp;<a id="menuBtn" href="#" onclick="showServer(); return false;">选择副区</a><br>
                <input type="hidden" id="checkedServers" name="checkedServers">
                &nbsp; &nbsp; &nbsp; <input
                    type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input
                    type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
            </form>
        </div>
        <hr>
        <div id="cancelMDive"
             style="width: 100%;background-color:rgba(196,238,255,0);border-style:solid;border-width:2pt; border-color:#b6ecff;">
            <h4>取消合并服务区</h4>

            <form action="/merger?action=cancelMergerInAction" method="post" id="mainCancelServerFrom">
                请选择取消合服的主区：<textarea id="mainCancelServerSel" readonly value="" rows="5" cols="20"
                                     onclick="showCancellServer();"></textarea> <input type="hidden"
                                                                                       id="checkedCancelServerSel"
                                                                                       name="checkedCancelServerSel">
                &nbsp;<a id="mainCancelMenuBtn" href="#" onclick="showCancellServer(); return false;">选择取消合服的主区</a><br>
                &nbsp; &nbsp; &nbsp; <input
                    type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <input
                    type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
$(document).ready(function () {
    $.ajax({
        url:'/merger?action=showServerTree&type=all',
        datatype:'json',
        type:'post',
        success:function (data) {
            var setting = {
                data:{
                    simpleData:{
                        enable:true
                    }
                },
                callback:{
                    onClick:showServerInfo
                }
            };
            $.fn.zTree.init($("#serverDiv"), setting, data);
        }
    });
});

function showTree() {
    $.ajax({
        url:'/merger?action=showServerTree&type=all',
        datatype:'json',
        type:'post',
        success:function (data) {
            var setting = {
                data:{
                    simpleData:{
                        enable:true
                    }
                },
                callback:{
                    onClick:showServerInfo
                }
            };
            $.fn.zTree.init($("#serverDiv"), setting, data);
        }
    });
}
function showServerInfo(event, treeId, treeNode, clickFlag) {
    var id = treeNode.id;
    $.ajax({
        url:'/merger?action=showServerInfo',
        data:{id:id},
        type:"post",
        datatype:'json',
        success:function (data) {
            $("table").show();
            $("#serverId").empty().append(data.id);
            $("#serverNo").empty().append(data.serverNo);
            $("#serverName").empty().append(data.serverName);
            $("#serverStatus").empty().append(data.serverStatus == 1 ? '火爆' : '维护');
            $("#recommand").empty().append(data.recommand);
            $("#createTime").empty().append(clearNull(data.createTime));
            $("#openTime").empty().append(clearNull(data.openingTime));
        }
    });
}
// 选择主区的js

function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.checkNode(treeNode, !treeNode.checked, null, true);
    return false;
}

function onCheckMain(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            nodes = zTree.getCheckedNodes(true),
            v = "",
            serverNo = "",
            serverId = "";
    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + ",";
        serverNo += nodes[i].serverNo;
        serverId += nodes[i].id;
    }
    if (v.length > 0) v = v.substring(0, v.length - 1);
    var cityObj = $("#mainServerSel");
    cityObj.attr("value", v);
    $("#checkedMainServer").empty().attr("value", serverNo);
    $("#checkedMainServerId").attr("value", serverId);
    $("#menuContent").hide();
}

function onCheck(e, treeId, treeNode) {

    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            nodes = zTree.getCheckedNodes(true),
            v = "",
            ids = "";

    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + "\n";
        var childs = zTree.transformToArray(nodes[i]);
        if (childs.length <= 1) {
            ids += nodes[i].id + ",";
        }
        else {
            for (var j = 0; j < childs.length; j++) {
                ids += childs[j].id + ",";
            }
        }

    }
    if (v.length > 0) v = v.substring(0, v.length - 1);
    var cityObj = $("#serverSel");
    cityObj.empty().attr("value", v);
    $("#checkedServers").attr("value", ids);
}

function onCheckCancell(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            nodes = zTree.getCheckedNodes(true),
            v = "",
            ids = "";

    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + "\n";
        var childs = zTree.transformToArray(nodes[i]);
        for (var j = 1; j < childs.length; j++) {
            ids += childs[j].id + ",";
        }
    }
    if (v.length > 0) v = v.substring(0, v.length - 1);
    var cityObj = $("#mainCancelServerSel");
    cityObj.empty().attr("value", v);
    $("#checkedCancelServerSel").attr("value", ids);
}


function showMainMenu() {
    var cityObj = $("#mainServerSel");
    var cityOffset = $("#mainServerSel").offset();
    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top - 40 + cityObj.outerHeight() + "px"}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}
function showMenu() {
    var cityObj = $("#mainServerSel");
    var cityOffset = $("#mainServerSel").offset();
    $("#menuContent").css({left:cityOffset.left + 224 + "px", top:cityOffset.top - 27 + cityObj.outerHeight() + "px"}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

function showCancellMenu() {
    var cityObj = $("#mainCancelServerSel");
    var cityOffset = $("#mainCancelServerSel").offset();
    $("#menuContent").css({left:cityOffset.left + 220 + "px", top:cityOffset.top - 130 + cityObj.outerHeight() + "px"}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "serverSel" || event.target.id == "menuBtn" || event.target.id == "mainMenuBtn" || event.target.id == "mainServerSel" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}
function beforeClick(e1, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.checkNode(treeNode, !treeNode.checked, null, true);
    return false;
}
function showMainServer() {
    $.ajax({
        url:'/merger?action=showServerTree&type=main',
        datatype:'json',
        type:'post',
        success:function (data) {
            var setting = {
                check:{
                    enable:true,
                    chkStyle:"radio",
                    radioType:"all"
                },
                view:{
                    dblClickExpand:false
                },
                data:{
                    simpleData:{
                        enable:true
                    }
                },
                callback:{
                    onClick:onClick,
                    onCheck:onCheckMain
                }
            };
            $.fn.zTree.init($("#treeDemo"), setting, data);
            showMainMenu();
        }
    });
}

function showServer() {
    $.ajax({
        url:'/merger?action=showServerTree&type=all',
        datatype:'json',
        type:'post',
        success:function (data) {
            var setting = {
                check:{
                    enable:true,
                    chkboxType:{"Y":"", "N":""}
                },
                view:{
                    dblClickExpand:false
                },
                data:{
                    simpleData:{
                        enable:true
                    }
                },
                callback:{
                    beforeClick:beforeClick,
                    onCheck:onCheck
                }
            };
            $.fn.zTree.init($("#treeDemo"), setting, data);
            showMenu();
        }
    });
}
function showCancellServer() {
    $.ajax({
        url:'/merger?action=showServerTree&type=cancell',
        datatype:'json',
        type:'post',
        success:function (data) {
            var setting = {
                check:{
                    enable:true,
                    chkboxType:{"Y":"", "N":""}
                },
                view:{
                    dblClickExpand:false
                },
                data:{
                    simpleData:{
                        enable:true
                    }
                },
                callback:{
                    beforeClick:beforeClick,
                    onCheck:onCheckCancell
                }
            };
            $.fn.zTree.init($("#treeDemo"), setting, data);
            showCancellMenu();
        }
    });
}
$(function () {
    $('#merger_server').ajaxForm({
        success:function (data) {
            $("#mainServerSel").val("");
            $("#serverSel").val("");
            $("#checkedMainServerId").val("");
            $("#checkedServers").val("");
            alert(data.msg);
            if (data.code == 200) { //如果成功提交
                showTree();
            }

        }
    });
    $('#mainCancelServerFrom').ajaxForm({
        success:function (data) {
            $("#mainCancelServerSel").val("");
            $("#checkedCancelServerSel").val("");
            alert(data.msg);
            if (data.code == 200) { //如果成功提交
                showTree();
            }
            $("#mainCancelServerFrom")[0].reset();
        }
    });
});


</script>
