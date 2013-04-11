<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-15
  Time: 下午4:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
<br>
<table style="width: 100%" border="1px solid green">
    <tr>
        <td align="left" valign="middle"><input type="button" value="增 加" onclick="addFaqFn();"
                                                onmouseover="this.style.borderColor='#75cd02'"
                                                onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                                                style="border-color: rgb(220, 220, 220); background:url('/images/add.png') no-repeat 8px center;">
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input onclick="editFaqFn();"
                                                            type="button" value="修 改"
                                                            onmouseover="this.style.borderColor='#75cd02'"
                                                            onmouseout="this.style.borderColor='#dcdcdc'"
                                                            class="btn1 pbtn1"
                                                            style="border-color: rgb(220, 220, 220); background:url('/images/edit.png') no-repeat 8px center;">
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <input onclick="deleteFaqFn();"
                                                             type="button" value="删 除"
                                                             onmouseover="this.style.borderColor='#75cd02'"
                                                             onmouseout="this.style.borderColor='#dcdcdc'"
                                                             class="btn1 pbtn1"
                                                             style="border-color: rgb(220, 220, 220); background:url('/images/delete.gif') no-repeat 8px center ;">
        </td>
        <td colspan="2" align="right" valign="middle">
            编号：<input id="faq_id" type="text" size="20">
            <input onclick="queryFaqFn();"
                   type="button" value="查 询" onmouseover="this.style.borderColor='#75cd02'"
                   onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                   style="border-color: rgb(220, 220, 220); background:url('/images/query.png') no-repeat 8px center ;">
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <table id="faqTable" style="width: 600px;">

            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <div id="faqPager" style="height: 30px;"></div>
        </td>
    </tr>
</table>

<div id="addFaqDiv" style="display: none;">
    <h3 style="background-color: #d6f7ff;text-align: center;font: bold;">增加FAQ</h3>

    <form id="add_faq" action="faq?action=saveFaq&oper=add" method="post">
        <table>
            <tr>
                <td>问题类别：</td>
                <td>
                    <select id="faq_faqName" name="faq_faqName" size="1" style="width: 220px;">
                    </select>
                </td>
            </tr>
            <tr>
                <td> 问题：</td>
                <td><input type="text" id="faq_question" name="faq_question"></td>
            </tr>
            <tr>
                <td>答案：</td>
                <td>
                    <textarea id="faq_answer" name="faq_answer" cols="10" rows="10">
                    </textarea>
                </td>
            </tr>
            <tr>
                <td>排序号：</td>
                <td>
                    <input id="faq_sortNum" name="faq_sortNum"/>
                </td>
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

<div id="editFaqDiv" style="display: none;">
    <h3 style="background-color: #d6f7ff;text-align: center;font: bold;">编辑FAQ</h3>

    <form id="edit_faq" action="faq?action=saveFaq&oper=edit" method="post">
        <table>
            <tr>
                <td> 问题类别：</td>
                <td><input type="hidden" id="u_faq_id" name="u_faq_id">
                    <select id="u_faq_faqName" name="u_faq_faqName" size="1" style="width: 220px;">
                    </select>

                </td>
            </tr>
            <tr>
                <td> 问题：</td>
                <td><input type="text" id="u_faq_question" name="u_faq_question"></td>
            </tr>
            <tr>
                <td> 答案：</td>
                <td><textarea id="u_faq_answer" name="u_faq_answer" cols="10" rows="10">
                </textarea>
                </td>
            </tr>
            <tr>
                <td> 点击数 ：</td>
                <td><input type="text" id="u_faq_visitSum" name="u_faq_visitSum"></td>
            </tr>
            <tr>
                <td> 排序号 ：</td>
                <td><input type="text" id="u_faq_sortNo" name="u_faq_sortNo"></td>
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
    jQuery("#faqTable").jqGrid({
        url:'/faq?action=queryFaq&type=load',
        datatype:"json",
        colNames:['编号', '类别', '问题', '答案', '访问次数', '序号', '类别编号'],
        colModel:[
            {name:'id', index:'id', width:30, editable:false, editoptions:{readonly:true, size:10}, searchoptions:{
                sopt:["eq"]}},
            {name:'name', index:'name', width:40, editable:true, editoptions:{size:25}, searchoptions:{
                sopt:["eq"]}},
            {name:'question', index:'question', width:200, align:"left", editable:true, editoptions:{size:10}, searchoptions:{
                sopt:["eq"]}},
            {name:'answer', index:'answer', width:400, editable:true, editoptions:{size:25}},
            {name:'visitSum', index:'visitSum', width:40, editable:true, editoptions:{size:25}},
            {name:'sortNum', index:'sortNum', width:20, editable:true, editoptions:{size:10}} ,
            {name:'cid', index:'cid', width:20, hidden:true, editable:true, editoptions:{size:10}}
        ],
        rowNum:10,
        rowList:[10, 20, 30],
        pager:'#faqPager',
        sortname:'id',
        refresh:true,
        viewrecords:true,
        sortorder:"asc",
        autowidth:true,
        height:300,
        multiselect:true,
        loadComplete:function (data) { //完成服务器请求后，回调函数
            if (data != null && data.rows == null) {
                alert("没有查到信息");
            }
        }
    });
    function addFaqFn() {
        initFaqKindList("#faq_faqName");
        $.fancybox({
            type:'inline',
            width:450,
            href:'#addFaqDiv'
        });
    }

    function editFaqFn() {
        var selectedId = $("#faqTable").jqGrid("getGridParam", "selrow");
        if (selectedId) {
            var rowData = $("#faqTable").jqGrid("getRowData", selectedId);
            $("#u_faq_id").val(rowData.id);
            $("#u_faq_question").val(rowData.question);
            $("#u_faq_answer").val(rowData.answer);
            $("#u_faq_sortNo").val(rowData.sortNum);
            $("#u_faq_visitSum").val(rowData.visitSum);
            initFaqKindList("#u_faq_faqName");
            $.fancybox({
                type:'inline',
                width:450,
                href:'#editFaqDiv'
            });
            $("#u_faq_faqName").attr("value", $.trim(rowData.name));
        }
        else {
            alert('请先选择行!');
        }
    }

    function deleteFaqFn() {
        var sels = $("#faqTable").jqGrid('getGridParam', 'selarrrow');
        if (sels == "") {
            alert("请选择要删除的项！");
        } else {
            if (confirm("您是否确认删除？")) {
                $.ajax({
                    type:"POST",
                    url:"/faq?action=saveFaq&oper=del",
                    data:"ids=" + sels,
                    success:function (data) {
                        $("#faqTable").trigger("reloadGrid");
                        alert(data.msg);
                    }
                });
            }
        }
    }
    function queryFaqFn() {
        var $id = $("#faq_id").val();
        $("#faqTable").jqGrid('setGridParam', {
            url:"/faq?action=queryFaq&type=simple",
            postData:{'id':$id}, //发送数据
            page:1
        }).trigger("reloadGrid"); //重新载入

    }

    function initFaqKindList(divId) {
        $.ajax({
            type:'post',
            url:'/initDropdownMenu?action=doInitFaqKindDropDownList',
            dataType:'json',
            success:function (data) {
                var faqKindObj = data;
                $faqKindDrop = $(divId);
                $faqKindDrop.empty();
                for (var i = 0; i < faqKindObj.length; i++) {
                    $faqKindDrop.append("<option value='" + faqKindObj[i].value + "'>"
                            + faqKindObj[i].label + "</option>");
                }
            }

        });
    }

    $(function () {
        $('#add_faq').ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#faqTable").trigger("reloadGrid");
                    $.fancybox().close();
                } else {
                    alert(data.msg);
                }
            }
        });
        $('#edit_faq').ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#faqTable").trigger("reloadGrid");
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
</div>
