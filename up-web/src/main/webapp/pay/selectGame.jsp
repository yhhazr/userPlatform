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
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib   prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt"  %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>游戏充值中心-第七大道网页游戏平台</title>
    <meta name="Description" content="第七大道网页游戏平台是第七大道旗下专业网页游戏运营平台，致力于为全球游戏玩家提供健康、快乐、绿色的网页游戏，最新最全最好玩的网页游戏，尽在第七大道网页游戏。"/>
    <meta name="Keywords" content="网页游戏、第七大道、第七大道网页游戏、第七大道神曲、第七大道弹弹堂、第七大道海神"/>
    <link rel="stylesheet" href="${staticDomainUrl}/css/showcss.css" type="text/css"/>
    <link rel="stylesheet" href="${staticDomainUrl}/css/index.css" type="text/css"/>
</head>
<body class="wrapper">
<jsp:include page="${hostDomain}/common/topClient2.jsp" flush="true"></jsp:include>
<div class="main">
    <div class="main-inner clearfix">
        <div class="choose-box">
            <div class="hd">
                <h2>请选择游戏</h2>
            </div>
            <div class="bd-nav clearfix">
                <!--  <span class="bd-nav-item-1">选择游戏</span>
              <span class="bd-nav-item-2">确认信息</span>
              <span class="bd-nav-item-3">充值完成</span> -->
                <img src="../img/cz_xzyx_jindu.jpg" alt="充值进度">
            </div>
            <div class="games clearfix">
                <c:forEach var="gameItem" items="${gameList}" step="1">
                    <li class="games-item">
                        <a href="index.html?gameid=${gameItem.id}&type=game">
                            <img src="../img/${gameItem.gameLogoSelectPage}" alt="${gameItem.gameName}"/></a>

                        <p class="game-name">${gameItem.gameName}</p>

                        <p class="game-type">类型：
                            <c:if test="${gameItem.gameType=='0'}">
                                角色扮演
                            </c:if>
                            <c:if test="${gameItem.gameType=='1'}">
                                竞技射击
                            </c:if>
                        </p>

                        <p class="game-btn"><a href="index.html?gameid=${gameItem.id}&type=game" class="btn-recharge">进入充值</a>
                        </p>
                    </li>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</div>
<jsp:include page="${hostDomain}/common/bottom.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="../scripts/jquery-1.7.1.js"></script>
<script type="text/javascript">
    //第一排游戏居中
    $(function () {
        var gameContainer = $("div.games"),
                gameLength = gameContainer.children("li").length;
        if (gameLength < 4) {
            gameContainer.css("margin-left", (978 - gameLength * 226) / 2 + "px");
        }
    })

</script>
<!--[if IE 6]>
<script type="text/javascript" src="../scripts/DD_belatedPNG.js"></script>
<script type="text/javascript">
    //IE6半透明
    DD_belatedPNG.fix(".fix-png");

</script>
<![endif]-->
</body>


</html>
