<%--
  ~ Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
  --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@page contentType="text/html;charset=utf-8" %>
    <%
        String path = request.getContextPath();
    %>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>神曲充值页面</title>
    <!-- Le styles -->
    <link href="${imageDomainUrl}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link href="${imageDomainUrl}/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/jquery.js"></script>
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style>
        body, p {
            font-family: "微软雅黑", Arial, "MS Trebuchet", sans-serif;
        }

        .m1 {
            width: 108px;
            height: 74px;
            background: url(${imageDomainUrl}/img/menu_01.jpg) no-repeat;
            margin-left: 187px;
        }

        .m2 {
            width: 134px;
            height: 74px;
            background: url(${imageDomainUrl}/img/menu_02_on.jpg) no-repeat;
        }

        .m3 {
            width: 130px;
            height: 74px;
            background: url(${imageDomainUrl}/img/menu_03.jpg) no-repeat;
        }

        .m4 {
            width: 130px;
            height: 74px;
            background: url(${imageDomainUrl}/img/menu_04.jpg) no-repeat;
        }

        .m5 {
            width: 108px;
            height: 74px;
            background: url(${imageDomainUrl}/img/menu_05.jpg) no-repeat;
        }

        .m1:hover {
            background: url(${imageDomainUrl}/img/menu_01_on.jpg) no-repeat;
        }

        .m2:hover {
            background: url(${imageDomainUrl}/img/menu_02_on.jpg) no-repeat;
        }

        .m3:hover {
            background: url(${imageDomainUrl}/img/menu_03_on.jpg) no-repeat;
        }

        .m4:hover {
            background: url(${imageDomainUrl}/img/menu_04_on.jpg) no-repeat;
        }

        .m5:hover {
            background: url(${imageDomainUrl}/img/menu_05_on.jpg) no-repeat;
        }

        .pTitle {
            background: url(${imageDomainUrl}/img/right_01.jpg);
            height: 34px;
            overflow: hidden;
            border-right: solid 1px #cccccc;
            border-left: solid 1px #cccccc;
            line-height: 34px;
        }

        .nav-tabs > li {
            margin-left: 8px;
        }

        .tab-pane {
            margin-top: 10px;
        }

        .breadcrumb {
            border: 0;
            border-bottom: 1px solid #ddd;
        }

        .nav-tabs li a {
            background-repeat: no-repeat;
            background-position: 17px -36px;
        }

        .nav-tabs .active > a {
            background-position-y: -2px;
        }

        .navbar-inner {
            filter: '';
        }


    </style>
    <link href="${imageDomainUrl}/img/ind.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${imageDomainUrl}/img/oth.css"/>
</head>

<body>
<div id="main">
    <jsp:include page="/common/topClient2.jsp" flush="true"></jsp:include>
    <div class="geren_content">
        <div class="cz_left" align="center">
            <div class="err_ties" style="margin-top:30px;">
                <img src="${imageDomainUrl}/img/czyc_error.jpg" width="42" height="42"/>&nbsp;&nbsp;暂未开放
            </div>
        </div>
        <div class="cz_right">
            <table width="178" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="108" valign="top"><a href="${customerUrl}"><img
                            src="${imageDomainUrl}/img/cz_right_img01.jpg"/></a></td>
                </tr>
                <tr>
                    <td height="67" valign="top"><a href="${meagerUrl}"><img
                            src="${imageDomainUrl}/img/cz_right_img02.jpg"/></a></td>
                </tr>
                <tr>
                    <td valign="top"><a href="${payHelp}" target="_blank"><img src="${imageDomainUrl}/img/cz_right_img03.jpg"/></a>
                    </td>
                </tr>
            </table>
        </div>
        <div class="bom">
            <jsp:include page="../common/bottom.jsp" flush="false"></jsp:include>
        </div>

    </div>
</body>
</html>
