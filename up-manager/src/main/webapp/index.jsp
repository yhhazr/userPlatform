<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>运营平台后台系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="css/smoothness/jquery-ui-1.8.22.custom.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="css/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.fancybox.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="css/zTreeStyle/zTreeStyle.css"/>
    <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
    <script src="js/jquery.form.js" type="text/javascript"></script>
    <script src="js/grid.locale-cn.js" type="text/javascript"></script>
    <script src="js/jquery.jqGrid.min.js" type="text/javascript"></script>

</head>
<body>
<div id="mainDiv">
    <jsp:include page="top.jsp" flush="true"></jsp:include>


    <div id="contentDiv" class="contentDiv" onmouseover="hideQueryCondition();">

        <h2><strong>欢迎您！${user.data.user_comment}</strong> ，您是  ${user.data.configs[0].type_name} ！
      </h2>
        <h2> 如碰到使用或者功能问题请RTX cutter同学！感谢您的使用！ </h2>

    </div>

    <div id="footDiv" class="xs3">
        <table width="100" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td><img src="images/loading.gif" width="24" height="24"/></td>
            </tr>
            <tr>
                <td style="text-align:center; color:#7b7c80;">处理中...</td>
            </tr>
        </table>
    </div>
</div>

<script src="js/jquery.progressbar.min.js" type="text/javascript"></script>
<script src="js/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="js/jquery.modal.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.blockUI.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/easydialog.min.js"></script>
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<script src="js/jquery-ui-1.8.22.custom.min.js" type="text/javascript"></script>
<script src="js/jquery.fancybox.js" type="text/javascript"></script>
<script src="js/jquery.ztree.all-3.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/swap.js"></script>
<script type="text/javascript" src="js/simple.js"></script>

<script type="text/javascript">
    $(function () {
        setMenu();
        resetContentDivHeight();
        $(window).resize(function () {
            resetContentDivHeight();
        });
    });
    function resetContentDivHeight() {
        var documentHeight = $(window).height();
        var bodyDivHeight = documentHeight - $('#headDiv').height() - $('#footDiv').height();
        $('#contentDiv').css('height', bodyDivHeight);
    }
</script>
</body>
</html>
