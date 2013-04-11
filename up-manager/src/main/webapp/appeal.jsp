<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>申诉管理</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/querycondition.css">
    <link rel="stylesheet" type="text/css" href="css/easydialog.css">
    <script src="js/jquery.ui.core.js"></script>
    <script src="js/jquery.ui.widget.js"></script>
    <script src="js/jquery.ui.datepicker.js"></script>
    <script src="js/jquery.ui.datepicker-zh-CN.js"></script>
    <style>
        .nav a { font-size: 14px;}
    </style>
</head>
<body>
<!--
<table cellpadding="0" cellspacing="0" style="width:100%">
    <tr>
        <td></td>
    </tr>
</table>
-->
<style>
.appeal_query{margin: 10px 20px 10px 10px;}
.appeal_query label{ display: inline; vertical-align: middle; margin: 0; padding: 0;}
.appeal_query input{ vertical-align: middle; margin: 0;}
</style>

<div>
    <div class="appeal_query">
        <label>用户名：</label><input type="text" class="input" id="q_userName"/>
        <label>申诉时间：</label><input type="text" class="input" id="q_from_date" maxlength="10"/> <input type="text" class="input" id="q_to_date" maxlength="10"/>
    </div>
    <div class="appeal_query">
        <label>充值状态：</label>
        <input type="radio" name="q_pay" value="0"><label>未充值</label>
        <input type="radio" name="q_pay" value="1"><label>充值</label>
        <input type="radio" name="q_pay" value="-1" checked="checked"><label>全部</label>
    </div>
    <div class="appeal_query">
        <label>处理状态：</label>
        <input type="radio" name="q_status" value="0"><label>未处理</label>
        <input type="radio" name="q_status" value="1"><label>通过</label>
        <input type="radio" name="q_status" value="2"><label>未通过</label>
        <input type="radio" name="q_status" value="-1" checked="checked"><label>全部</label>
        <input type="button" class="btn" id="btnQuery" value="查 询" />
    </div>
    <div style="clear: both;"></div>
</div>
<table id="appealList" class="scroll" cellpadding="0" cellspacing="0"></table>
<div id="pager" class="scroll" style="text-align:right;"></div>
<div id="editAppeal" title="申诉处理" style="display:none; text-align: center;">
    <table class="table" cellpadding="0" cellspacing="0" width="98%">
        <tr>
            <td width="30%">用户名</td>
            <td width="70%"><span id="userName"></span><input id="appealId" type="hidden" name="appealId" value=""></td>
        </tr>
        <tr>
            <td>真实姓名</td>
            <td><span id="realName"></span></td>
        </tr>
        <tr>
            <td>身份证</td>
            <td><span id="idCard"></span></td>
        </tr>
        <tr>
            <td>邮箱</td>
            <td><span id="email"></span></td>
        </tr>
        <tr>
            <td>经常玩的游戏</td>
            <td><span id="oftenPlayGame"></span></td>
        </tr>
        <tr>
            <td>所在服务器</td>
            <td><span id="serverName"></span></td>
        </tr>
        <tr>
            <td>角色名称</td>
            <td><span id="playerName"></span></td>
        </tr>
        <tr>
            <td>角色等级</td>
            <td><span id="playerLevel"></span></td>
        </tr>
        <tr>
            <td>注册时间</td>
            <td><span id="createDate"></span></td>
        </tr>
        <tr>
            <td>注册城市</td>
            <td><span id="createCity"></span></td>
        </tr>
        <tr>
            <td>账号异常时间</td>
            <td><span id="exceptionDate"></span></td>
        </tr>
        <tr>
            <td>最后登录时间</td>
            <td><span id="lastLoginDate"></span></td>
        </tr>
        <tr>
            <td>是否充值过</td>
            <td><span id="pay"></span></td>
        </tr>
        <tr>
            <td>订单号</td>
            <td><span id="orderIds"></span></td>
        </tr>
        <tr>
            <td>身份证扫描:<span id="idCardImgPath"></span></td>
            <td>
                <a id="aTagIdCardImgPath" href="" target="_blank">
                    <img src="" width="300px" height="200px">
                </a>
                <span id="noIdCardImgPath" style="display: none;">没有图片信息</span>
                <!--
                <a id="aTagIdCardImgPath" href="" target="_blank">
                    <img src="images/testIdCard.jpg" width="300px" height="200px">
                </a>
                -->
            </td>
        </tr>
        <tr>
            <td>补充资料</td>
            <td><span id="otherInfo"></span></td>
        </tr>
        <tr>
            <td>申诉日期</td>
            <td><span id="appealTime"></span></td>
        </tr>
        <tr>
            <td>审核状态</td>
            <td><span id="status"></span></td>
        </tr>
        <tr>
            <td>审核时间</td>
            <td><span id="auditorTime"></span></td>
        </tr>
        <tr>
            <td>审核人</td>
            <td><span id="auditor"></span></td>
        </tr>
        <tr>
            <td>操作</td>
            <td>
                <input type="radio" name="status" value="0">未处理
                <input type="radio" name="status" value="1">通过
                <input type="radio" name="status" value="2">未通过
            </td>
        </tr>
        <!--
        <tr>
            <td>回复</td>
            <td><textarea id="huifu" rows="5" style="width:80%;"></textarea></td>
        </tr>
        -->
    </table>
</div>
<script type="text/javascript">
    $(function(){
        $( "#editAppeal").dialog( "destroy");
        $("div table.table").eq(1).parent().remove();
        var tbAppeal = jQuery("#appealList").jqGrid({
            url:'/appealManage?action=query',
            datatype: "json",
            colNames:['编号','用户名','真实姓名','身份证','经常玩的游戏','所在服务器',
                '角色名称','角色等级','邮箱','注册时间','注册城市','账号异常时间','最后登录时间',
                '是否充值过','订单号','身份证扫描','补充资料','申诉日期','处理状态','审核人','审核时间'],
            colModel:[
                {name:'id',index:'id', width:155, align:"right", hidden:true},
                {name:'userName',index:'userName',width:100},
                {name:'realName',index:'realName',width:100},
                {name:'idCard',index:'idCard',hidden:true},
                {name:'oftenPlayGame',index:'oftenPlayGame',width:100},
                {name:'serverName',index:'serverName'},
                {name:'playerName',index:'playerName'},
                {name:'playerLevel',index:'playerLevel'},
                {name:'email',index:'email',hidden:true},
                {name:'createDate',index:'createDate',hidden:true},
                {name:'createCity',index:'createCity',hidden:true},
                {name:'exceptionDate',index:'exceptionDate',hidden:true},
                {name:'lastLoginDate',index:'lastLoginDate',hidden:true},
                {name:'pay',index:'pay', align:"center",formatter:fmatterPay},
                {name:'orderIds',index:'orderIds', hidden:true},
                {name:'idCardImgPath',index:'idCardImgPath',hidden:true},
                {name:'otherInfo',index:'otherInfo',hidden:true},
                {name:'appealTime',index:'appealTime',formatter:'date',formatoptions:{srcformat : 'U', newformat : 'Y-m-d H:i:s'}},
                {name:'status',index:'status',align:"center", formatter:fmatterStatus},
                {name:'auditor',index:'auditor', hidden:true},
                {name:'auditorTime',index:'auditorTime', hidden:true,formatter:'date',formatoptions:{srcformat : 'U', newformat : 'Y-m-d H:i:s'}}
            ],
            rowNum:20,
            rowList:[20,30,40],
            rownumbers: true,
            pager: '#pager',
            sortname: 'id',
            viewrecords: true,
            shrinkToFit: true,
            autowidth: true,
            sortorder: "desc",
            onSortCol: function(name,index){ alert("Column Name: "+name+" Column Index: "+index);},
            ondblClickRow: function(id){
                var model = tbAppeal.jqGrid('getRowData', id);
                var appealId = id;
                $("#userName").text(model.userName);
                $("#realName").text(model.realName);
                $("#idCard").text(model.idCard);
                $("#oftenPlayGame").text(model.oftenPlayGame);
                $("#serverName").text(model.serverName);
                $("#playerName").text(model.playerName);
                $("#playerLevel").text(model.playerLevel);
                $("#status").text(model.status);
                $("#email").text(model.email);
                $("#createDate").text(model.createDate);
                $("#createCity").text(model.createCity);
                $("#exceptionDate").text(model.exceptionDate);
                $("#lastLoginDate").text(model.lastLoginDate);
                $("#pay").html(model.pay);
                $("#orderIds").html(model.orderIds.replace(/,/g,"<br>"));
                //$("#idCardImgPath").text(model.idCardImgPath);
                $("#noIdCardImgPath").hide();
                $("#aTagIdCardImgPath").hide();
                if(model.idCardImgPath.length == 0) {
                    $("#noIdCardImgPath").show();
                } else {
                    $("#aTagIdCardImgPath").show();
                    $("#aTagIdCardImgPath").attr("href", "/showIdCardImg?action=show&id=" + id);
                    $("#aTagIdCardImgPath img").attr("src", "/showIdCardImg?action=show&id=" + id);
                }
                $("#otherInfo").text(model.otherInfo);
                $("#appealTime").text(model.appealTime);
                $("#status").html(model.status);
                $("#auditor").text(model.auditor);
                $("#auditorTime").text(model.auditorTime);
                var s = $(model.status).attr("s");
                $("input[name='status']").eq(s).attr("checked","checked");
                $("#editAppeal").dialog({
                    height:500,
                    width:1000,
                    resizable:true,
                    modal:true,
                    bgiframe: true,
                    close:function(){
                        $(this).dialog("destroy");
                    },
                    buttons:{
                        "确定":function(){
                            var status = $("input[name='status']:checked").val();
                            if (status == '0') {
                                $("#editAppeal").dialog("close");
                                return;
                            }
                            $("button:contains('确定')").attr("disabled","disabled");
                            $.ajax({
                                type:"POST",
                                dataType:"JSON",
                                cache: false,
                                url:"/appealManage?action=modify",
                                data:{appealId:appealId, status:status, auditor:"${user.user_name}"},
                                success:function (msg) {
                                    if (msg.code > 0) {
                                        $(".ui-icon-refresh").click();
                                    } else {
                                        alert("操作失败");
                                    }
                                    $("#editAppeal").dialog("close");
                                }
                            });
                            //$(this).dialog("close");
                        },
                        "取消":function(){$(this).dialog("close");}
                    }
                });

                $("button:contains('确定')").hide();
                if(s == '0') $("button:contains('确定')").show();
            },
            caption:" 用户申诉信息",
            jsonReader: {
                repeatitems : false,
                id: "0"
            },
            height: '100%'
            //autowidth: true
        });
        $("#appealList").jqGrid('navGrid',"#pager",{search:false,edit:false,add:false,del:false});
        $("#appealList").jqGrid('setGridWidth', $(window).width() -30 , true);

        function fmatterPay (cellvalue, options, rowObject) {
            if (cellvalue == 0){
                return '否';
            }
            return '<font color="red">是</font>';
        }
        function fmatterStatus (cellvalue, options, rowObject) {
            if (cellvalue == 0){
                return "<lable style=\"color:#ff0000\" s=\"0\">未处理</lable>";
            }
            if (cellvalue == 1){
                return "<lable s=\"1\">通过</lable>";
            }
            return "<lable s=\"2\">未通过</lable>";
        }

        $("#btnQuery").click(function(){
            var userName = $("#q_userName").val();
            var fromDate = $("#q_from_date").val();
            var toDate = $("#q_to_date").val();
            var pay = $(":radio:checked[name='q_pay']").val();
            var status = $(":radio:checked[name='q_status']").val();
            tbAppeal.jqGrid('setGridParam',{
                url:"/appealManage?action=query",
                postData:{userName:userName,fromDate:fromDate,toDate:toDate, pay: pay, status:status}, //发送数据
                page:1
            }).trigger("reloadGrid"); //重新载入
        });

        $( "#q_from_date" ).datepicker({
            defaultDate: "+1w",
            changeMonth: true,
            numberOfMonths: 3,
            dateFormat: "yy-mm-dd",
            maxDate: "0",
            onSelect: function( selectedDate ) {
                $( "#q_to_date" ).datepicker( "option", "minDate", selectedDate );
            }
        });
        $( "#q_to_date" ).datepicker({
            defaultDate: "+1w",
            changeMonth: true,
            numberOfMonths: 3,
            dateFormat: "yy-mm-dd",
            maxDate: "1",
            onSelect: function( selectedDate ) {
                $( "#q_from_date" ).datepicker( "option", "maxDate", selectedDate );
            }
        });
    });
</script>
 <button disabled="disabled"></button>
</body>
</html>
