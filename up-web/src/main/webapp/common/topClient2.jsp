<%@page contentType="text/html" pageEncoding="UTF-8" %>
<link rel="stylesheet" type="text/css" href="http://static.7road.com/platform/css/platformCommon.css" />

    <div id="platformHead"></div>
<iframe src="#" id="platIframeHead" style="display:none"></iframe>
<script type="text/javascript" src="http://static.7road.com/platform/js/src/platformCommonHead.js"></script>
<div class="homehead">
    <div class="banner">
        <a href="${homePageUrl}" class="userlogo"></a>
    </div>
    <div class="pay-nav">
        <ul class="menu">
            <li class="a"><a href="${homePageUrl}">首页</a></li>
            <li class="b"><a href="${accountUrl}">个人中心</a></li>
            <li class="c"><a href="${payPageUrl}">游戏充值</a></li>
            <li class="d"><a href="http://account.7road.com/forget.html">客服中心</a></li>
            <li class="e"><a href="${forumUrl}">游戏论坛</a></li>
			 <a id="login-out" href="#" onclick="logout();" style="display:none;">安全退出</a>
            <%--
            <li class="weluser">
                您好，yibulaxi
                <ul class="welul">
                    <li><a href="#">个人中心</a></li>
                    <li><a href="#">我要充值</a></li>
                    <li><a href="#">安全退出</a></li>
                </ul>
            </li>
            --%>
        </ul>
        <div class="hotgame">
            最热门的游戏：<a href="http://sq.7road.com" target="_blank" class="hot">神曲</a>
            <%--<a href="#" target="_blank">弹弹堂</a>--%>
            <a href="http://hs.7road.com" target="_blank" class="hot">海神</a>
            <%--<a href="#" target="_blank">T</a><a href="#" target="_blank">精灵猎手</a><a href="#" target="_blank">捕鱼日志</a>--%>
        </div>
    </div>
</div>
