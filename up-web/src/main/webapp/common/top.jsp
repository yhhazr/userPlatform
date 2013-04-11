<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<link rel="stylesheet" type="text/css" href="http://static.7road.com/platform/css/platformCommon.css" />

    <div id="platformHead"></div>
<iframe src="#" id="platIframeHead" style="display:none"></iframe>
<script type="text/javascript" src="http://static.7road.com/platform/js/src/platformCommonHead.js"></script>
<div class="homehead">
    <div class="banner">
        <a href="${homePageUrl}" class="userlogo"></a>
    </div>
    <div class="nav">
        <ul class="menu">
            <li class="a"><a href="http://www.7road.com/">首页</a></li>
            <li class="b select"><a href="${accountUrl}">个人中心</a></li>
            <li class="c"><a href="${payPageUrl}">游戏充值</a></li>
            <li class="d"><a href="/forget.html">客服中心</a></li>
            <li class="e"><a href="${forumUrl}">游戏论坛</a></li>
            <li class="weluser">
                您好，${userNameShow}
                <ul class="welul">
                    <li><a href="${accountUrl}">个人中心</a></li>
                    <li><a href="${payPageUrl}">我要充值</a></li>
                    <li><a id='login-out' href="#" onclick="logout();">安全退出</a></li>
                </ul>
            </li>
        </ul>
        <div class="hotgame">
            最热门的游戏：
            <a href="http://sq.7road.com" target="_blank" class="hot">神曲</a>
            <%--<a href="#">弹弹堂</a>--%>
            <a href="http://hs.7road.com/" class="hot">海神</a>
            <%--<a href="#">疯狂弹弹堂</a>--%>
            <%--<a href="#">精灵猎手</a>--%>
            <%--<a href="#">捕鱼日志</a>--%>
        </div>
    </div>
</div>
<div class="blank"></div>
<div class="bread"><p><a href="${homePageUrl}">首页</a>&gt;<a href="${accountUrl}">个人中心</a>&gt;<span id="currentFn">基本信息</span></p></div>

