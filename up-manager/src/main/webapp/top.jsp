<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-6-15
  Time: 上午10:31
  头部
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>平台管理后台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

</head>
<body>
<div class="navbar navbar-fixed-top" id="navigateHead">
    <div class="navbar-inner">
        <div class="container" style="width: auto;" height="1800">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#">运营平台管理后台</a>

            <div class="dropdown">
                <ul class="nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="gameUserManage"
                           style="display: none;">用户中心 <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="nav-header" id="swapToUserAccountPage" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/user','swapToUserAccountPage')">用户查询</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="orderManage"
                           style="display: none;">订单管理 <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="nav-header" id="showOrderPage" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/order','showOrderPage')">订单查询</a></li>
                            <li class="nav-header" id="orderStatistical" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/order','orderStatistical')">订单统计</a></li>
                            <li class="nav-header" id="showOrderByGrid" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/order','showOrderByGrid')">补单查询</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="csmanage" style="display: none;">客服中心
                            <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="nav-header" id="faqManage" style="display: none;"><a href="javascript:void(0)"
                                                                                            onclick="forword('/faq','faqManage')">FAQ管理</a>
                            </li>
                            <li class="nav-header" id="toFaqKindPage" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/faqKind','toFaqKindPage')">FAQ类别</a></li>
                            <li class="nav-header" id="csInfoManage" style="display: none;"><a href="javascript:void(0)"
                                                                                               onclick="forword('/csInfo','csInfoManage')">客服信息</a>
                            </li>
                            <li class="nav-header" id="keywordManage" style="display: none;"><a
                                    href="javascript:void(0)"
                            <%--toFaqKindTreePage--%>
                                    onclick="forword('/faqKind','keywordManager')">关键字管理</a></li>

                            <li class="nav-header" id="fullTextSearch" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/user','fullTextSearch')">全文检索</a>
                            </li>
                            <li class="nav-header" id="appealManage" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/appealManage','show')">申诉管理</a>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="operationManage"
                           style="display: none;">运营管理 <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="nav-header" id="mergerServer" style="display: none;"><a
                                    href="javascript:void(0)"
                                    onclick="forword('/user','mergerServer')">合服管理</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="payManage"
                           style="display: none;">支付管理 <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="nav-header" id="paySwitch">
                                <a href="javascript:void(0)" onclick="forword('/paySwitch','show')">支付渠道管理</a></li>
                            <li class="nav-header" id="paySwitchType">
                                <a href="javascript:void(0)" onclick="forword('/paySwitchType','show')">支付类型管理</a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav pull-right">
                    <li><a href="javascript:void(0)" onclick="logout()">注销用户</a></li>
                    <li class="divider-vertical"></li>
                </ul>
            </div>
            <!-- /.nav-collapse -->
        </div>
    </div>
    <!-- /navbar-inner -->
</div>


<script type="text/javascript">
    function logout() {
        $.ajax({
            type:"GET",
            dataType:"text",
            url:"/user",
            data:{action:"logout"},
            beforeSend:function (XMLHttpRequest, textStatus) {

            },
            success:function (msg) {
                var result = confirm('确认注销吗？');
                if (result == true)
                    window.location.href = "/login.html";
            },
            error:function (msg) {
                window.location.href = "/login.html";
            }
        });
    }
    function forword(url, action) {
        setMenu();
        if (url.indexOf('?') != -1) {
            url = url + "&" + new Date().getTime();
        } else {
            url = url + "?" + new Date().getTime();
        }
        $.ajax({
            type:"GET",
            dataType:"text",
            url:url,
            data:{action:action},
            beforeSend:function (XMLHttpRequest, textStatus) {
                $("#contentDiv").empty();
            },
            success:function (msg) {
                if(msg)
                {
                    $("#contentDiv").append(msg);
                    $.ajax({
                        type:"post",
                        url:"/master?action=getPermissionInfo",
                        dataType:"json",
                        success:function (data) {
                            if (data.swapToUserAccountPage == null) {
                                $("#queryUserAccountByIDBtn").hide();
                            }
                            if (data.queryUserLog == null) {
                                $("#queryUserLogByIDBtn").hide();
                                $("#userLogDiv").hide();
                            }
                            if (data.queryServer == null) {
                                $("#showServerInfoDiv").hide();
                            }
                            if (data.splitServer == null) {
                                $("#cancelMDive").hide();
                            }
                            if (data.mergerServer == null) {
                                $("#mergerDiv").hide();
                            }
                        }
                    });
                }

            },
            error:function (msg) {
                alert(msg);
                $("#navigateHead").hide();
                window.location.href = "/login.html";
            }
        });
    }


</script>
<!-- /navbar -->
</body>
</html>
