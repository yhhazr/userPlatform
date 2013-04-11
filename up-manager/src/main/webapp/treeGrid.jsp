<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-13
  Time: 下午7:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>树状结构</title>
    <link rel="stylesheet" type="text/css" media="screen" href="css/smoothness/jquery-ui-1.8.22.custom.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="css/ui.jqgrid.css"/>
</head>
<body>
<h3>常见问题种类管理</h3>
<table style="width: 100%" border="1px solid green">
    <tr>
        <td align="left" valign="middle"><input type="button" value="增 加" onclick="addFaqKindFn();"
                                                onmouseover="this.style.borderColor='#75cd02'"
                                                onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                                                style="border-color: rgb(220, 220, 220); background:url('/images/add.png') no-repeat 8px center;">
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input onclick="editFaqKindFn();"
                                                            type="button" value="修 改"
                                                            onmouseover="this.style.borderColor='#75cd02'"
                                                            onmouseout="this.style.borderColor='#dcdcdc'"
                                                            class="btn1 pbtn1"
                                                            style="border-color: rgb(220, 220, 220); background:url('/images/edit.png') no-repeat 8px center;">
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <input onclick="deleteFaqKindFn();"
                                                             type="button" value="删 除"
                                                             onmouseover="this.style.borderColor='#75cd02'"
                                                             onmouseout="this.style.borderColor='#dcdcdc'"
                                                             class="btn1 pbtn1"
                                                             style="border-color: rgb(220, 220, 220); background:url('/images/delete.gif') no-repeat 8px center ;">
        </td>
        <td colspan="2" align="right" valign="middle">
            编号：<input id="faqKind_id" type="text" size="20">
            <input onclick="queryFaqKindFn();"
                   type="button" value="查 询" onmouseover="this.style.borderColor='#75cd02'"
                   onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                   style="border-color: rgb(220, 220, 220); background:url('/images/query.png') no-repeat 8px center ;">
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <table id="treegrid"></table>

        </td>
    </tr>
    <tr>
        <td colspan="3">
            <div id="ptreegrid"></div>
        </td>
    </tr>
</table>

<div id="addFaqKindDiv" style="display: none;">
    <h3 style="background-color: #d6f7ff;text-align: center;font: bold;">增加常见问题种类</h3>

    <form id="add_faqKind" action="/faqKind?action=saveFaqKindsTree&oper=add" method="post">
        <table>
            <tr>
                <td>问题类别：</td>
                <td>
                    <select id="faqKind_parent" name="faqKind_parent" size="1" style="width: 220px;">
                    </select>
                </td>
            </tr>
            <tr>
                <td> 问题名称：</td>
                <td><input type="text" id="faqKind_name" name="faqKind_name"></td>
            </tr>
            <tr>
                <td>问题说明：</td>
                <td>
                    <textarea id="faqKind_ext" name="faqKind_ext" cols="10" rows="10">
                    </textarea>
                </td>
            </tr>
            <tr>
                <td> 排序编号：</td>
                <td><input type="text" id="faqKind_sortNo" name="faqKind_sortNo"></td>
            </tr>
            <tr>
                <td align="center"><input
                        type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                </td>
                <td align="center"><input
                        type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="editFaqKindDiv" style="display: none;">
    <h3 style="background-color: #d6f7ff;text-align: center;font: bold;">编辑常见问题种类</h3>

    <form id="edit_faqKind" action="/faqKind?action=saveFaqKindsTree&oper=edit" method="post">
        <table>
            <tr>
                <td> 问题类别：</td>
                <td><input type="hidden" id="u_faqKind_id" name="u_faqKind_id">
                    <select id="u_faqKind_parent" name="u_faqKind_parent" size="1" style="width: 220px;">
                    </select>

                </td>
            </tr>
            <tr>
                <td> 问题名称：</td>
                <td><input type="text" id="u_faqKind_name" name="u_faqKind_name"></td>
            </tr>
            <tr>
                <td> 问题说明：</td>
                <td><textarea id="u_faqKind_ext" name="u_faqKind_ext" cols="10" rows="10">
                </textarea>
                </td>
            </tr>
            <tr>
                <td> 排序编号：</td>
                <td><input type="text" id="u_faqKind_sortNo" name="u_faqKind_sortNo"></td>
            </tr>
            <tr>
                <td align="center"><input
                        type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                </td>
                <td align="center"><input
                        type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
                </td>
            </tr>
        </table>
    </form>
</div>


<script type="text/javascript">
    $(function () {
        $("#treegrid").jqGrid({
            url:'/faqKind?action=queryTreeFaq&type=load',
            datatype:'json',
            mtype:'post',
            colNames:['编号', '常见问题种类', '种类详细说明', '序号', '父编号'],
            colModel:[
                {name:'id', index:'id', width:30, hidden:true, editable:true, key:true},
                {name:'name', index:'name', width:200, editable:true},
                {name:'ext', index:'ext', width:200, editable:true, edittype:"textarea", editoptions:{rows:"2", cols:"32"}},
                {name:'sortNo', index:'sortNo', width:30, align:"right", editable:true},
                {name:'parentId', index:'parentId', hidden:true, width:100, align:"right", editable:true, edittype:'select',
                    editoptions:{value:""}}
            ],
            treeGridModel:'adjacency',
            height:400,
            autowidth:true,
            pager:"#ptreegrid",
            treeGrid:true,
            sortname:'id',
            sortorder:'asc',
            ExpandColumn:'name',
            ExpandColClick:true,
            loadComplete:function (data) { //完成服务器请求后，回调函数
                if (data == null || data.rows == null) {
                    alert("没有查到信息");
                }
            }
        });

    });
    function deleteFaqKindFn() {
        var sels = $("#treegrid").jqGrid('getGridParam', 'selrow');
        alert('选择了' + sels);
        if (sels == "") {
            alert("请选择要删除的项！");
        } else {
            if (confirm("您是否确认删除？")) {
                $.ajax({
                    type:"POST",
                    url:"/faqKind?action=saveFaqKindsTree&oper=del",
                    data:"ids=" + sels,
                    success:function (data) {
                        $("#treegrid").trigger("reloadGrid");
                        alert(data.msg);
                    }
                });
            }
        }
    }

    function addFaqKindFn() {
        initFaqSelfList("#faqKind_parent");
        $.fancybox({
            type:'inline',
            width:450,
            href:'#addFaqKindDiv'
        });
    }

    function editFaqKindFn() {
        var selectedId = $("#treegrid").jqGrid("getGridParam", "selrow");
        if (selectedId) {
            var rowData = $("#treegrid").jqGrid("getRowData", selectedId);
            $("#u_faqKind_id").val(rowData.id);
            $("#u_faqKind_parent").val(rowData.parentId);
            $("#u_faqKind_name").val(rowData.name);
            $("#u_faqKind_ext").val(rowData.ext);
            $("#u_faqKind_sortNo").val(rowData.sortNo);
            initFaqSelfList("#u_faqKind_parent");
            $.fancybox({
                type:'inline',
                width:450,
                href:'#editFaqKindDiv'
            });
        }
        else {
            alert('请先选择行!');
        }
    }

    function queryFaqKindFn() {
        var $id = $("#faqKind_id").val();
        $("#treegrid").jqGrid('setGridParam', {
            url:"/faqKind?action=queryTreeFaq&type=simple",
            postData:{'id':$id}, //发送数据
            page:1
        }).trigger("reloadGrid"); //重新载入

    }

    function initFaqSelfList(divId) {
        $.ajax({
            type:'post',
            url:'/initDropdownMenu?action=doInitFaqKindDropDownListOfParent',
            dataType:'json',
            success:function (data) {
                var faqKindObj = data;
                $faqKindDrop = $(divId);
                $faqKindDrop.empty();
                $faqKindDrop.append("<option value='" + 0 + "'>"
                        + "--空--" + "</option>");
                for (var i = 0; i < faqKindObj.length; i++) {
                    $faqKindDrop.append("<option value='" + faqKindObj[i].value + "'>"
                            + faqKindObj[i].label + "</option>");
                }
            }
        });
    }

    $(function () {
        $('#add_faqKind').ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#treegrid").trigger("reloadGrid");
                    $.fancybox().close();
                } else {
                    alert(data.msg);
                }
            }
        });
        $('#edit_faqKind').ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#treegrid").trigger("reloadGrid");
                    $.fancybox().close();
                } else {
                    alert(data.msg);
                }
            }
        });
    });

    function validate(formData, jqForm, options) {
        for (var i = 0; i < formData.length; i++) {
            if (!formData[i].value) {
                alert("请输入完整相关信息");
                return false;
            }
        }
    }
</script>
</body>
</html>
