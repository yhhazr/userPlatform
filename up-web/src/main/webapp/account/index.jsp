<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-20
  Time: 下午3:33
  主页
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>基本信息</title>
    <link rel="shortcut icon" href="img/favicon.ico"/>
    <link href="../css/home.css" rel="stylesheet" type="text/css"/>
    <link href="../css/userhome.css" rel="stylesheet" type="text/css"/>
    <link href="/css/jquery.fancybox.css" rel="stylesheet" type="text/css"/>
    <link href="/css/jquery.Jcrop.min.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="../scripts/jquery-1.7.1.js"></script>
</head>

<body>
<jsp:include page="../common/top.jsp" flush="false"></jsp:include>
<div class="main">
    <div class="leftbox">
        <jsp:include page="../common/leftBox.jsp" flush="false"></jsp:include>
    </div>
    <div class="rightbox">
        <div id="showAjax" style="display: none;">
        </div>
        <div id="workInfoDiv" style="display: none;">
            <jsp:include page="userInfo/workInfo.jsp" flush="true"></jsp:include>
        </div><div id="ipExceptionDiv" style="display: none;">
        <jsp:include page="userInfo/IpException.jsp" flush="true"></jsp:include>
    </div>
        <div id="indexInfoDiv" style="display: block;">
            <jsp:include page="userInfo/index.jsp" flush="true"></jsp:include>
        </div>
        <div id="baseInfoDiv" style="display: none;">
            <jsp:include page="userInfo/baseInfo.jsp" flush="true"></jsp:include>
        </div>
        <div id="detailInfoDiv" style="display: none;">
            <jsp:include page="userInfo/detailInfo.jsp" flush="true"></jsp:include>
        </div>
        <div id="eduInfoDiv" style="display: none;">
            <jsp:include page="userInfo/eduInfo.jsp" flush="true"></jsp:include>
        </div>


    </div>
    <div class="blank"></div>
</div>
<div class="blank" style="height:60px;"></div>

<jsp:include page="../common/bottom.jsp" flush="false"></jsp:include>
</body>

<script type="text/javascript" src="../scripts/jquery.cookie.js"></script>
<script type="text/javascript" src="../scripts/userhome.js"></script>
<script type="text/javascript" src="../scripts/userhomesq.js"></script>
<script type="text/javascript" src="../scripts/common.js"></script>
<script type="text/javascript">
    $(function () {
        var safeLevel = '${safeInfo.safeStrength}';
        var pswLevel = '${safeInfo.pswStrength}';
        var headUrl = '${userObject.headDir}';
        //设置头像
        if ($.trim(headUrl)) {
            $(".userheadimg").attr("src", headUrl);
        }
        else {
            $(".userheadimg").attr("src", "http://account.7road.com/userImg/geren_right_01.jpg");

        }
        //设置整体安全条的比例
        $("#safeStrength").empty().html(safeLevel);
        $(".pass_Lv2").css("width", safeLevel / 7 * 178 + "px");
        //设置密码的安全条比例
        $("#pswStrength").empty().html(pswLevel);
        $(".pass_Lv6").css("width", pswLevel / 7 * 178 + "px");
    });
</script>
</html>

