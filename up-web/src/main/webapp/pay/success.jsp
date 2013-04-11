<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@page contentType="text/html;charset=utf-8" %>
    <%
        String path = request.getContextPath();
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>神曲充值页面</title>
    <link rel="shortcut icon" href="${imageDomainUrl}/img/favicon.ico" />
    <link rel="stylesheet" href="${imageDomainUrl}/img/oth.css"/>
    <script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/jquery.js"></script>
    <script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/getRechargeType.js?_v=20130228"></script>
    <script>

        function gameStart(url) {
            $.ajax({
                async:false,
                url:url,
                type:"GET",
                dataType:'jsonp',
                jsonp:'jsoncallback',
                // data:{g:${requestScope.gameId}, z:${requestScope.zoneId}},
                success:function (data) {
                    if (data == false) {
                        alert("${requestScope.gameServerName}" + "暂未开放，敬请期待！");
                    } else if (data == "unlogin") {
                        window.location.href = "http://sq.7road.com";
                    } else {
                        window.location.href = url;
                    }
                }

            });
        }
    </script>
    <link rel="stylesheet" href="${staticDomainUrl}/css/showcss.css" type="text/css"/>
</head>
<body>
<jsp:include page="${hostDomain}/common/topClient2.jsp" flush="true"></jsp:include>
<div id="main">
    <div class="geren_content" style="width:980px;margin:0 auto;">
        <div style=" width:980px;margin:0 auto;height: 22px;padding-right:10px;padding-top: 10px;padding-left:10px;">充值账单查询</div>
        <br/>

        <div style=" height: 30px; text-align: center;margin-top: 10px;"><img
                src="${imageDomainUrl}/img/cz_cen_jindu3.jpg"/></div>
        <div class="cz_left" align="center" style="width:810px;float:left;">
            <div class="czyc" style="margin-top:30px;width:807px;">
                <input type="hidden" id="typeInfo" value="${requestScope.typeInfo}"/>

                <div class="err_ties"><img src="${imageDomainUrl}/img/czyc_ok.jpg" width="42" height="42"/>&nbsp;&nbsp;恭喜您，充值成功！
                </div>
                <div class="err_msg">
                    <table width="70%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td width="45%" class="fnt_czyc" align="right">您充值的方式：</td>
                            <td width="55%" class="fnt_czyc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="pay_way">${requestScope.payWay}</span></td>
                        </tr>
                        <tr>
                            <td class="fnt_czyc" align="right">您充值的游戏：</td>
                            <td class="fnt_czyc" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="game">${requestScope.gameName} </span></td>
                        </tr>
                        <tr>
                            <td class="fnt_czyc" align="right">您充值的区服：</td>
                            <td class="fnt_czyc" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="game_server">${requestScope.gameServerName}</span></td>
                        </tr>
                        <tr>
                            <td class="fnt_czyc" align="right">您的订单编号：</td>
                            <td class="fnt_czyc" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="cha_no">${requestScope.orderId}</span></td>
                        </tr>
                        <tr>
                            <td class="fnt_czyc" align="right">您的充值账号：</td>
                            <td class="fnt_czyc" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="cha_no"><span id="cha_user">${requestScope.userName}</span></span></td>
                        </tr>
                        <tr>
                            <td class="fnt_czyc" align="right">您的充值的金额：</td>
                            <td class="fnt_czyc" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="cha_amount">${requestScope.amount}元</span></td>
                        </tr>
                        <tr>
                            <td class="fnt_czyc" align="right">充值所得：</td>
                            <td class="fnt_czyc" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                    id="game_amount">${requestScope.gold}钻石</span></td>
                        </tr>
                    </table>
                    <h6>【温馨提示】<span class="err_fnt"> 为了提高您账号的安全，系统推荐您进行邮箱绑定</span> <a href="${accountUrl}">去绑定</a></h6>
                    <br/>

                    <div class="czyc_btn">
                        <!--                <form method="post" action="${gateWayDomain}/PlayGame">
                    <input type="hidden" name="g" value="${requestScope.gameId}"/>
                    <input type="hidden" name="z" value="${requestScope.zoneId}"/>
                    <input type="hidden" name="isAjax" value="true"/>
                    <input type="image" src="${imageDomainUrl}/img/czyc_btn_1.jpg"/>
                    <a href="index.html"><img
                            src="${imageDomainUrl}/img/czyc_btn_2.jpg" width="121" height="36"/></a>
                </form>-->
                        <a id="go2Game" href="javascript:void(0)"
                           onclick="gameStart('${hostDomain}/PlayGame?g=${requestScope.gameId}&z=${requestScope.zoneId}')"><img
                                src="${imageDomainUrl}/img/czyc_btn_1.jpg" width="121" height="36"/></a>
                        <a href="index.html"><img
                                src="${imageDomainUrl}/img/czyc_btn_2.jpg" width="121" height="36"/></a>
                    </div>
                </div>
            </div>

        </div>


        <div class="cz_right"  style="width:160px;float:left;margin: 30px 0 0 10px;">
            <div class="span2" style="width:160px;">
                <%--<a href="${customerUrl}"><img src="${imageDomainUrl}/img/cz_right_img01.jpg" style="margin-bottom:4px;"/></a>--%>
                <a href="${payHelpUrl}" target="_blank"><img src="${imageDomainUrl}/img/cz_right_img03.jpg"/></a>
            </div>
        </div>
        <div style="width:100%;clear:both;height:20px;overflow: hidden;"> </div>

    </div>
</div>
<jsp:include page="${hostDomain}/common/bottom.jsp" flush="true"></jsp:include>
</body>
</html>
