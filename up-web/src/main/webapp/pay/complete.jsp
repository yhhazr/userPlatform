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
    <link href="${imageDomainUrl}/img/ind.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${imageDomainUrl}/img/oth.css"/>
    <script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/jquery.js"></script>
    <script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/getRechargeType.js"></script>
    <script>

        function enterGame2(url) {
            enterGame(url, function (data) {
                if (data.code > 0) {
                    alert("${requestScope.gameServerName}" + data.msg);
                } else if (data.code == 0) {
                    window.location.href = url;
                } else {
                    window.location.href = "${homePageUrl}";
                }
            });
        }
        var result = "${requestScope.status}";
        function showPannel() {
            if (result == "1") {
                $("#fail").css("display", "none");
                $("#showError").css("display", "none");
                $("#success").css("display", "block");
            } else {
                $("#fail").css("display", "block");
                $("#showError").css("display", "block");
                $("#success").css("display", "none");
            }
        }
    </script>
</head>

<body>
<div id="main">
    <jsp:include page="${hostDomain}/common/topClient2.jsp" flush="true"></jsp:include>
    <div class="geren_content">
        <div class="cz_left" align="center">
            <div style=" height: 22px; float: left;padding-right:10px;padding-top: 10px;padding-left:10px;">充值账单查询</div>
            <br/>

            <div class="czyc" style="margin-top:50px;">
                <input type="hidden" id="typeInfo" value="${requestScope.typeInfo}"/>
                <input type="hidden" id="result" value="${requestScope.result}"/>

                <div id="fail" class="err_ties"><img src="${imageDomainUrl}/img/czyc_error.jpg" width="42"
                                                     height="42"/>&nbsp;&nbsp;未完成支付
                </div>

                <div id="success" class="err_ties"><img src="${imageDomainUrl}/img/czyc_ok.jpg" width="42"
                                                        height="42"/>&nbsp;&nbsp;恭喜您，充值成功！
                </div>

                <div class="err_msg">
                    <h2 id="showError">充值扣款失败，请联系 0755-61886777</h2>

                    <div id="showResult">
                        <table width="70%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="45%" class="fnt_czyc" align="right">您充值的方式：</td>
                                <td width="55%" class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="pay_way">${requestScope.payWay}</span></td>
                            </tr>
                            <tr>
                                <td class="fnt_czyc" align="right">您充值的游戏：</td>
                                <td class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="game">${requestScope.gameName} </span></td>
                            </tr>
                            <tr>
                                <td class="fnt_czyc" align="right">您充值的区服：</td>
                                <td class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="game_server">${requestScope.gameServerName}</span></td>
                            </tr>
                            <tr>
                                <td class="fnt_czyc" align="right">您的订单编号：</td>
                                <td class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="cha_no">${requestScope.orderId}</span></td>
                            </tr>
                            <tr>
                                <td class="fnt_czyc" align="right">您的充值账号：</td>
                                <td class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="cha_no"><span id="cha_user">${requestScope.userName}</span></span></td>
                            </tr>
                            <tr>
                                <td class="fnt_czyc" align="right">您的充值的金额：</td>
                                <td class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="cha_amount">${requestScope.amount}元</span></td>
                            </tr>
                            <tr>
                                <td class="fnt_czyc" align="right">充值所得：</td>
                                <td class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        id="game_amount">${requestScope.gold}钻石</span></td>
                            </tr>
                        </table>
                    </div>


                    <h6>【温馨提示】<span class="err_fnt"> 为了提高您账号的安全，系统推荐您进行邮箱绑定</span> <a href="${accountUrl}">去绑定</a></h6>

                    <br/>

                    <div class="czyc_btn">
                        <a id="go2Game" href="javascript:void(0)"
                           onclick="enterGame2('${gatewayDomain}/PlayGame?g=${requestScope.gameId}&z=${requestScope.zoneId}')"><img
                                src="${imageDomainUrl}/img/czyc_btn_1.jpg" width="121" height="36"/></a>
                        <a href="index.html?type=game&gameid=${requestScope.gameId}"><img
                                src="${imageDomainUrl}/img/czyc_btn_2.jpg" width="121" height="36"/></a>
                    </div>
                </div>
            </div>

        </div>


        <div class="cz_right">
            <div class="span2" style="width:178px;">
                <%--<a href="${customerUrl}"><img src="${imageDomainUrl}/img/cz_right_img01.jpg" style="margin-bottom:4px;"/></a>--%>
                <a href="${payHelpUrl}" target="_blank"><img src="${imageDomainUrl}/img/cz_right_img03.jpg"/></a>
            </div>
        </div>
        <div class="bom">
            <jsp:include page="${hostDomain}/common/bottom.jsp" flush="true"></jsp:include>
        </div>

    </div>
</div>

</body>
</html>
