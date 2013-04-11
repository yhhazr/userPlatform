<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-3
  Time: 上午9:36
  查询faq分类的信息
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body style="text-align: center;">
<h1>常见问题种类管理</h1>
<table id="faqKindTable" style="width: 600px;">

</table>
<div id="pager2" style="height: 30px;"></div>
<script type="text/javascript">
    jQuery("#faqKindTable").jqGrid({
        url:'/faqKind?action=queryFaq',
        datatype:"json",
        colNames:['编号', '名称', '详情', '序号', '父编号'],
        colModel:[
            {name:'id', index:'id', width:100, editable:false, editoptions:{readonly:true, size:10}},
            {name:'name', index:'name', width:100, editable:true, editoptions:{size:25}},
            {name:'ext', index:'ext', width:200, editable:true, editoptions:{size:10}},
            {name:'sortNo', index:'sortNo', width:100, align:"right", editable:true, editoptions:{size:10}},
            {name:'parentId', index:'parentId', width:100, align:"right", editable:true, editoptions:{size:10}}
        ],
        rowNum:10,
        rowList:[10, 20, 30],
        pager:'#pager2',
        sortname:'id',
        viewrecords:true,
        sortorder:"asc",
        caption:"常见问题类别的管理",
        autowidth:true,
        height:300,
        multiselect:true,
        editurl:"/faqKind?action=saveFaqKinds"
    });
    //            .navGrid("#pager2",{edit:true,add:true,del:true});
    //    jQuery("#faqKindTable").jqGrid('navGrid', '#pager2', {edit:true, add:true, del:true});
    jQuery("#faqKindTable").jqGrid('navGrid', '#pager2', {del:true, add:true, edit:true}, {}, {width:400}, {width:400}, {multipleSearch:true, width:800});

</script>
</body>
</html>
