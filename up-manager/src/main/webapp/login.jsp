<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-6-12
  Time: 下午6:32

--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>运营平台后台---登录</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <!-- CSS -->
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <link rel="stylesheet" href="/css/dynamic.css" type="text/css"/>
    <link rel="stylesheet" href="/css/login.css" type="text/css"/>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#loading").ajaxStart(function () {
                $(this).fadeIn();
                $("#lbContent").fadeTo(1000, 0.3);
                $("#saveForm").attr("disabled", true);
                $("#btnReset").attr("disabled", true);
            });
            //重置事件
            $("#btnReset").click(function () {
                $("ul>li:first", "#loginForm").removeClass("error");
                $("ul>li:eq(1)", "#loginForm").removeClass("error");
                $("#myerror_uid").fadeOut("fast");
                $("#myerror_pwd").fadeOut("fast");
                $("form").trigger("reset");
            });
            //登录事件
            $("#saveForm").click(login
            )//end 登录事件

        });

        function login() {
            var uid = $("#username").val(); //帐号
            var pwd = $("#password").val(); //密码
            var remember = $("#remember").attr("checked") ? 1 : 0;
            if (uid == "") {
                $("ul>li:first", "#loginForm").addClass("error");
                $("#myerror_uid").fadeIn(1000, function () {
                    $(this).text("帐号不允许为空！");
                });

                return false;
            } else {
                $("ul>li:first", "#loginForm").removeClass("error");
                $("#myerror_uid").fadeOut(1000, function () {
                    $(this).text("帐号或密码错误，请重试！");
                });
            }

            if (pwd == "") {
                $("ul>li:eq(1)", "#loginForm").addClass("error");
                $("#myerror_pwd").fadeIn(10, function () {
                    $(this).text("密码不允许为空！");
                });
                return false;
            } else {
                $("ul>li:eq(1)", "#loginForm").removeClass("error");
                $("#myerror_pwd").fadeOut(10, function () {
                    $(this).text("");
                });
            }
            $.ajax({
                type:"POST",
                dataType:"json",
                url:"/master",
                data:{action:"login", username:uid, password:pwd},
                beforeSend:function (XMLHttpRequest, textStatus) {

                },
                success:function (data) {
                    var loginCode = data.code;
                    if (loginCode == 0) {
                        window.location.href = "/index";
                    }  else if (loginCode == 205) {
                        if (confirm(data.msg)) {
                            window.location.href = data.object;
                        }  else
                        {
                            window.location.href = "/login.html";
                        }
                    } else
                    {
                        alert(decodeURI(data.msg));
                        window.location.href = "/login.html";
                    }
                },
                fail:function () {
                    alert('主数据通信异常，请联系开发人员！');
                },
                error:function (data) {
                    window.location.href = "/login.html";
                }
            });
        }
        function passwordEnter(e) {
            if (e.keyCode == 13) {
                login();
            }
        }
    </script>
</head>
<body id="public" class="login">
<div id="loading"
     style="background:#CC4444;color:#FFF;width:80px;padding-left:5px;position:absolute;line-height:22px;display:none;z-index:9999;left:0px;top:0px;">
    系统验证中...
</div>
<div id="lightbox" class="done">
    <div id="lbContent" class="clearfix">
        <form id="loginForm" action="user?action=login" method="post">
            <div class="info">
                <h2 style="color:#adff2f;">运营平台后台欢迎您</h2>

                <div style="font-size:small;">hi 亲爱的用户, 请先登录.</div>
            </div>
            <ul>
                <li>
                    <div>
                        <label class="desc" for="username">登录帐号</label>
                        <input id="username" name="username" type="input" value="cutter"/>
                    </div>
                    <p class="myerror" id="myerror_uid" style="display:none"></p>
                </li>
                <li>
                    <div>
                        <label class="desc" for="password">密码</label>
                        <input id="password" name="password" value="abc123!" type="password"
                               onkeyup="passwordEnter(event)"/>
                    </div>
                    <p class="myerror" id="myerror_pwd" style="display:none"></p>
                </li>
                <li class="">
                    <a id="saveForm" class="button"><img src="images/door_in.png"/>登录 </a>
                    &nbsp; &nbsp; &nbsp; &nbsp;
                    <%--<input type="submit" value="登录" id="saveForm" class="button"/>--%>
                    <a id="btnReset" class="button"><img src="images/cross.png">重置</a>
                    <%--<input type="reset" id="btnReset" class="button" value="重置">--%>
                </li>
            </ul>

            <br/>
        </form>
        <div id="noaccount">
            <a href="#">
                <h2>没有帐号? <b>No Worries.</b>
                    <span>创建一个新的.</span> <b>It's easy!</b></h2>
            </a>
        </div>
        <noscript>
            <style type="text/css">

                #lightbox form, #noaccount {
                    display: none;
                }

                #lightbox {
                    visibility: visible !important
                }

            </style>

            <h2 class="error">需要javascript的支持，请检查你的IE是否支持？</h2>

            <h3>对不起，我的网站只能工作在支持javascript的浏览器中!</h3>
        </noscript>
    </div>
</div>
</body>
</html>
