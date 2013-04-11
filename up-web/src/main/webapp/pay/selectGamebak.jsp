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
    <link rel="shortcut icon" href="${imageDomainUrl}/img/favicon.ico" />
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
            background: url(${imageDomainUrl}/img/menu_02.jpg) no-repeat;
        }

        .m3 {
            width: 130px;
            height: 74px;
            background: url(${imageDomainUrl}/img/menu_03_on.jpg) no-repeat;
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

    <script type="text/javascript">
        $(function () {
            var i = 5;
            var tmp = setInterval(function () {
                $('#lbTimer').html(i);
                if (i-- == 0) {
                    clearInterval(tmp);
                    location = 'index.html?gameid=1&type=game';
                }
            }, 1000);
        });

    </script>
    <link href="${imageDomainUrl}/img/ind.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${staticDomainUrl}/css/showcss.css" type="text/css"/>
</head>

<body>
<div id="main">
    <jsp:include page="${hostDomain}/common/topClient2.jsp" flush="true"></jsp:include>
    <div class="geren_content">
        <div class="cz_left" align="center">
            <div style=" width: 780px;">
                <div style=" height: 22px; float: left;padding-right:10px;padding-top: 10px;">请选择游戏</div>
                <br/>

                <div style=" height: 83px; text-align: center;margin-top: 30px;+margin-top:40px;"><img
                        src="${imageDomainUrl}/img/cz_xzyx_jindu.jpg"/></div>

                <div class="cz_xzyx_img" style=" text-align: center; width: 306px;height: 150px;">
                    <a href="index.html?gameid=1&type=game"></a>
                </div>

                <div class="cz_xzyx_bon"
                     style=" text-align: center; width: 306px;height: 85px;margin-top: 50px;padding-left: 100px;">
                    <a href="index.html?gameid=1&type=game">下一步（还有<font
                            style="color:#FFFF00"><span id="lbTimer">5</span></font>秒进入）</a>
                </div>
            </div>

        </div>


        <div class="cz_right">
            <div class="span2" style="width:160px;margin:0 0 0 10px;_margin:0 0 0 7px;display: inline;">
                <%--<a href="${customerUrl}"><img src="${imageDomainUrl}/img/cz_right_img01.jpg" style="margin-bottom:4px;"/></a>--%>
                <a href="${payHelpUrl}"><img src="${imageDomainUrl}/img/cz_right_img03.jpg"/></a>
            </div>
        </div>
        <div style="width:100%;clear:both;height:20px;overflow: hidden;"> </div>

    </div>
</div>
<jsp:include page="${hostDomain}/common/bottom.jsp" flush="true"></jsp:include>
</body>
</html>
