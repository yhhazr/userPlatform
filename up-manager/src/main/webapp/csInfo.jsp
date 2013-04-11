<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-15
  Time: 下午2:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
<br>
<table style="width: 100%;">
    <tr>
        <td style="background-color:#ffffff;"> &nbsp; &nbsp;
            <input onclick="addCsQQ();" id="addQQ"
                   type="button" value="增加QQ群" onmouseover="this.style.borderColor='#75cd02'"
                   onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                   style="border-color: rgb(220, 220, 220); background:url('/images/add.png') no-repeat 8px center ;">
            &nbsp; &nbsp;&nbsp;&nbsp;
            <input onclick="addPhoto();" id="addPhoto"
                   type="button" value="增加轮播图片" onmouseover="this.style.borderColor='#75cd02'"
                   onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                   style="border-color: rgb(220, 220, 220); background:url('/images/add.png') no-repeat 8px center ;">
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input onclick="editCsInfoFn();"
                                                            type="button" value="修 改"
                                                            onmouseover="this.style.borderColor='#75cd02'"
                                                            onmouseout="this.style.borderColor='#dcdcdc'"
                                                            class="btn1 pbtn1"
                                                            style="border-color: rgb(220, 220, 220); background:url('/images/edit.png') no-repeat 8px center;">
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <input onclick="deleteCsInfoFn();"
                                                             type="button" value="删 除"
                                                             onmouseover="this.style.borderColor='#75cd02'"
                                                             onmouseout="this.style.borderColor='#dcdcdc'"
                                                             class="btn1 pbtn1"
                                                             style="border-color: rgb(220, 220, 220); background:url('/images/delete.gif') no-repeat 8px center ;">
        </td>
        <td colspan="2" align="right" valign="middle">
            编号：<input id="csInfo_id" type="text" size="20">
            <input onclick="queryCsInfoFn();"
                   type="button" value="查 询" onmouseover="this.style.borderColor='#75cd02'"
                   onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                   style="border-color: rgb(220, 220, 220); background:url('/images/query.png') no-repeat 8px center ;">
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table id="csInfoTable" style="width: 600px;">
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="csInfoPager" style="height: 30px;"></div>
        </td>
    </tr>
</table>
<div>

    <div id="edit_qqDiv" style="display: none;">
        <h3 style="color: green;text-align: center;font: bold;">编辑客服信息</h3>
        <br>

        <form id="edit_qq" action="/csInfo?action=saveCsInfo&oper=edit" method="post">
            信息编号：<input type="text" readonly="" id="edit_qqInfoId" name="edit_qqInfoId"> <br>
            客服信息：<input type="text" id="edit_qqInfoText" name="edit_qqInfoText"><br>
            客服说明：<input type="text" id="edit_qqInfoExt" name="edit_qqInfoExt"><br> <br>

            <div style="text-align: center;">
                <input
                        type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input
                    type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
            </div>
        </form>
    </div>
    <div id="add_photoDiv" style="display: none;">
        <h3 style="color: green;text-align: center;font: bold;">增加客服推播图片</h3>
        <br>

        <form id="add_photo" action="/csInfo?action=upload&type=add" method="post"
              enctype="multipart/form-data">
            图片说明：<input type="text" id="cs_photoInfoExt" name="cs_photoInfoExt"><br> <br>
            选择图片：<input type="file" id="cs_photoInfoText" name="cs_photoInfoText" size="25">
            <br> <br>

            <div style="text-align: center;">
                <input
                        type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input
                    type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
            </div>

        </form>
    </div>
    <div id="add_qqDiv" style="display: none;">
        <h3 style="color: green;text-align: center;font: bold;">增加客服QQ群</h3>
        <br>

        <form id="add_qq" action="/csInfo?action=addQQInfo" method="post">
            QQ群号：<input type="text" id="cs_qqInfoText" name="cs_qqInfoText"><br>
            Q群说明：<input type="text" id="cs_qqInfoExt" name="cs_qqInfoExt"><br> <br>

            <div style="text-align: center;">
                <input
                        type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input
                    type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
            </div>
        </form>
    </div>

    <div id="edit_photoDiv" style="display: none;">
        <h3 style="color: green;text-align: center;font: bold;">编辑客服推播图片信息</h3>
        <br>

        <form id="edit_photo" action="/csInfo?action=upload&type=edit" method="post"
              enctype="multipart/form-data">
            信息编号： <input type="text" readonly="" id="edit_photoInfoId" name="edit_photoInfoId"><br>
            图片说明：<input type="text" id="edit_photoInfoExt" name="edit_photoInfoExt"><br> <br>
            选择图片：<input type="file" id="edit_photoInfoText" name="edit_photoInfoText" size="25">
            <br> <br>

            <div style="text-align: center;">
                <input
                        type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                        onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                        style="border-color: rgb(220, 220, 220); background:url('/images/ok.gif') no-repeat 8px center ;">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input
                    type="reset" value="重 置" onmouseover="this.style.borderColor='#75cd02'"
                    onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                    style="border-color: rgb(220, 220, 220); background:url('/images/reset.gif') no-repeat 8px center ;">
            </div>

        </form>
    </div>

</div>
<script type="text/javascript">
    jQuery("#csInfoTable").jqGrid({
        url:'/csInfo?action=queryCsInfo&type=load&nd=' + new Date().getTime(),
        datatype:"json",
        colNames:['编号', '名称', '内容', '说明'],
        colModel:[
            {name:'id', index:'id', width:100, editable:false, editoptions:{readonly:true, size:10}},
            {name:'name', index:'name', width:100, editable:false, editoptions:{size:25}},
            {name:'text', index:'sortNo', width:100, align:"left", editable:true, editoptions:{size:10}},
            {name:'ext', index:'ext', width:200, editable:true, editoptions:{size:10}}
        ],
        rowNum:20,
        rowList:[10, 20, 30],
        pager:'#csInfoPager',
        sortname:'id',
        viewrecords:true,
        sortorder:"asc",
        autowidth:true,
        height:300,
        multiselect:true,
        editurl:"/csInfo?action=saveCsInfo",
        loadComplete:function (data) { //完成服务器请求后，回调函数
            if (data.records == 0) { //如果没有记录返回，追加提示信息，删除按钮不可用
                $("p").appendTo($("#list")).addClass("nodata").html('找不到相关数据！');
                $("#del_btn").attr("disabled", true);
            } else { //否则，删除提示，删除按钮可用
                $("p.nodata").remove();
                $("#del_btn").removeAttr("disabled");
            }
        }
    });

    function addCsQQ() {
        $.fancybox({
            type:'inline',
            width:350,
            href:'#add_qqDiv',
            title:'添加客服QQ信息'
        });
    }

    function addPhoto() {
        $.fancybox({
            type:'inline',
            width:400,
            href:'#add_photoDiv',
            title:'添加客服页的轮播图片'
        });
    }

    $(function () {
        $('#add_qq').ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#csInfoTable").trigger("reloadGrid");
                    $.fancybox().close();
                } else {
                    alert(data.msg);
                }
            }
        });
        $('#add_photo').ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#csInfoTable").trigger("reloadGrid");
                    $.fancybox().close();
                } else {
                    alert(data.msg);
                }
            }
        });

        submitForm("#edit_photo");
        submitForm("#edit_qq");
    });

    function submitForm(formId) {
        $(formId).ajaxForm({
            beforeSubmit:validate, //验证表单
            success:function (data) {
                if (data.code == 200) { //如果成功提交
                    $("#csInfoTable").trigger("reloadGrid");
                    $.fancybox().close();
                } else {
                    alert(data.msg);
                }
            }
        });
    }


    function validate(formData, jqForm, options) {
        for (var i = 0; i < formData.length; i++) {
            if (!formData[i].value) {
                alert("请输入完整相关信息");
                return false;
            }
        }
    }

    function reloadJqgrid() {
        $("#csInfoTable").trigger("reloadGrid");
    }

    function deleteCsInfoFn() {
        var sels = $("#csInfoTable").jqGrid('getGridParam', 'selarrrow');
        if (!sels) {
            alert("请选择要删除的项！");
        } else {
            var flag = true;
            for (var i = 0; i < sels.length; i++) {
                var selectedId = sels[i];
                var rowData = $("#csInfoTable").jqGrid("getRowData", selectedId);
                if (rowData.name == "cs_telephonenum" || rowData.name == "cs_telephonenum") {
                    flag = false;
                }
            }
            if (flag) {
                if (confirm("您是否确认删除？")) {
                    $.ajax({
                        type:"POST",
                        url:"/csInfo?action=saveCsInfo&oper=del",
                        data:"ids=" + sels,
                        success:function (data) {
                            $("#csInfoTable").trigger("reloadGrid");
                            alert(data.msg);
                        }
                    });
                }
            }
            else {
                alert("不能删除客服热线和邮箱！这是常备信息!");
            }
        }
    }

    function editCsInfoFn() {
        var selectedId = $("#csInfoTable").jqGrid("getGridParam", "selrow");
        if (selectedId) {
            var rowData = $("#csInfoTable").jqGrid("getRowData", selectedId);

            if (rowData.name.substr(0, 'cs_adphoto'.length) == 'cs_adphoto') {
                $("#edit_photoInfoId").val(rowData.id);
                $("#edit_photoInfoExt").val(rowData.ext);
                $.fancybox({
                    type:'inline',
                    width:450,
                    href:'#edit_photoDiv'
                });
            }
            else {
                $("#edit_qqInfoId").val(rowData.id);
                $("#edit_qqInfoText").val(rowData.text);
                $("#edit_qqInfoExt").val(rowData.ext);
                $.fancybox({
                    type:'inline',
                    width:450,
                    href:'#edit_qqDiv'
                });
            }
        }
        else {
            alert('请先选择行!');
        }
    }
    function queryCsInfoFn() {
        var $id = $("#csInfo_id").val();
        $("#csInfoTable").jqGrid('setGridParam', {
            url:"/csInfo?action=queryCsInfo&type=simple",
            postData:{'id':$id}, //发送数据
            page:1
        }).trigger("reloadGrid"); //重新载入

    }
</script>
</div>
