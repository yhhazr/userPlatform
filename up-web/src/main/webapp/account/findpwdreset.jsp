<%--
    Author     : jiangfan.zhou
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>

<title></title>
<link rel="shortcut icon" href="${imageDomainUrl}/img/favicon.ico" />
<meta name="baidu-site-verification" content="NHoJdn8RaeEp87Ly"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="stylesheet" href="${imageDomainUrl}/css/forgetpwd-layout.css" type="text/css"/>
<link rel="stylesheet" href="${imageDomainUrl}/css/forgetpwd-style.css" type="text/css"/>
<link href="${imageDomainUrl}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

<link href="${imageDomainUrl}/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
<!--[if lte IE 7]>
<link rel="stylesheet" href="iehacks.css" type="text/css"/>
<![endif]-->
<style>
body, p {
    font-family: "微软雅黑", Arial, "MS Trebuchet", sans-serif;
    background-color: white;
}

.m1 {
    width: 108px;
    height: 74px;
    background: url(${imageDomainUrl}/img/menu_01.jpg) no-repeat;
    display: inline-block;
}

.m2 {
    width: 134px;
    height: 74px;
    background: url(${imageDomainUrl}/img/menu_02.jpg) no-repeat;
    display: inline-block;
}

.m3 {
    display: inline-block;
    width: 130px;
    height: 74px;
    background: url(${imageDomainUrl}/img/menu_03.jpg) no-repeat;
}

.m4 {
    display: inline-block;
    width: 130px;
    height: 74px;
    background: url(${imageDomainUrl}/img/menu_04_on.jpg) no-repeat;
}

.m5 {
    display: inline-block;
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

.well {
    padding: 0;
}

.sidebar-nav li a {
    height: 38px;
    line-height: 38px;
}

.nav-list {
    padding-right: 15px;
    padding-left: 15px;
    margin-bottom: 0;
}

.nav {
    margin-bottom: 18px;
    margin-left: 0;
    list-style: none;
}

.nav-list .active a {
    background: url(${staticDomainUrl}/img/cz_left_menu01_on.jpg) no-repeat;
}

.nav-list  li a {
    background: url(${imageDomainUrl}/img/cz_left_menu01.jpg) no-repeat;
    padding: 0;
    text-align: center;
    width: 193px;
    display: block;

}

.nav-list  li  a:hover {
    font-size: 1.1em;
    color: #888;
}

.nav-header {
    margin-right: -15px;
    margin-left: 11px;
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
    padding: 3px 15px;
    font-weight: bold;
    line-height: 18px;
    color: #999;
    display: block;
}

.navbar-inner {
    filter: '';
}

.well0 {
    min-height: 20px;
    border: 1px solid #eee;
    border: 1px solid rgba(0, 0, 0, 0.05);
    border-bottom: 0;
    -webkit-border-radius: 4px 4px 0 0;
    -moz-border-radius: 4px 4px 0 0;
    border-radius: 4px 4px 0 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
    -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}

.well0 img {

}

.czcg {
    background: url(${imageDomainUrl}/img/czcg.jpg) no-repeat;
    width: 487px;
    height: 282px;
    position: absolute;
    z-index: 1002;
}

.czqr {
    background: url(${imageDomainUrl}/img/czqr.jpg) no-repeat;
    width: 487px;
    height: 323px;
    position: absolute;
    z-index: 1002;
}

.btnPannel a {
    width: 140px;
    float: left;
    background-image: url(${imageDomainUrl}/img/cz_cen_bon.jpg);
    color: #FFFFFF;
    font-size: 14px;
    font-weight: bold;
    line-height: 40px;
    text-decoration: none;
    text-align: center;
    margin-left: 100px;
}

.people {
    margin: 10px;
}

.frash {
    background-image: url(${imageDomainUrl}/img/role.gif);
    background-position: 0px 0px;
}

.label-important, .badge-important {
    background-color: #B94A48;
}

.label {
    display: inline;
    padding: 1px 4px 2px;
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    border-radius: 3px;
    font-size: 10.998px;
    font-weight: bold;
    line-height: 14px;
    color: white;
    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
    white-space: nowrap;
    vertical-align: baseline;
}

.ties {
    padding: 10px;
    color: #E36822;
    border: 1px solid #D2D2D2;
    line-height: 18px;
    background-color: #F8F5F5;
    height: 35px;
    margin-right: 10px;
    margin-left: 10px;
    margin-top: 20px;
}

.descripe {
    margin-top: 25px;
    margin-bottom: 5px;
    margin-right: 10px;
    margin-left: 10px;
}

    /*找回密码页面样式*/
ul, li {
    list-style: none outside none;
    margin: 0;
}

.getMain {
    height: 520px;
    margin-top: 50px;
}

.title-7 {
    font-size: 35px;
    font-weight: bold;
    color: green;
    font-family: "微软雅黑";
}

.title-en {
    font-size: 32px;
    font-weight: bold;
    color: black;
    font-family: "微软雅黑";
}

.title-ch {
    font-size: 28px;
    color: #666464;
    font-family: "微软雅黑";
}

.title-zhmm {
    font-size: 16px;
    color: #C5C5C5;
    font-weight: 30;
    font-family: "微软雅黑";
}

.getPwdBody {
    border: 1px solid #EDD28B;
    padding: 2px;
    border-radius: 3px;
    width: 655px;
    height: 450px;
    margin: 0 auto;
}

.inpuText {
    width: 200px;
    border: 1px solid #ccc;
    padding: 4px;
}

.btn_next {
    background-color: #FE9400;
    padding: 5px 20px;
    border-radius: 3px;
    cursor: pointer;
    color: white;
    font-weight: bold;
}

.btn_next_hover {
    background-color: #FF5704;
}

.tips {
    border: 1px solid #EDD28B;
    width: 390px;
    height: 45px;
    line-height: 45px;
    margin: 0 auto;
    color: #666;
}

.tips img {
    margin-left: 20px;
    border: 0px;
    vertical-align: middle;
}

.tips span {
    margin-left: 20px;
}

.inputMsg {
    margin-top: 120px;
}

.userInputMsg {
    text-align: left;
    width: 320px;
    margin: 0 auto;
    text-align: right;
    padding-right: 20px;
}

.up_title {
    text-align: right;
    width: 85px;
}

.updateMsg {
    background-color: #FEF5EE;
    font-weight: bold;
    border: 1px solid #FF6D1F;
}

.updateMsg {
    border: 1px solid #FF6D1F;
    padding: 30px 0px;
    text-align: center;
    width: 510px;
    margin: 0 auto;
    margin-top: 160px;
    background-color: #FEF5EE;
    display: none;
    font-weight: bold;
    color: red;
    font-size: 16px;
}

.up_suc {
    font-size: 28px;
    font-weight: bold;
    color: #6CD947;
}
</style>
</head>
<body>
<jsp:include page="/common/top.jsp" flush="true"/>
<!-- 找回密码部分开始-->
<div class="ym-col3">
    <div style="margin-top:1.5em;">
        <div id="payNet" class="getMain">


            <div class="getPwdBody">
                <div id="inputMsg" class="inputMsg">
                    <c:choose>
                        <c:when test="${isPass}">
                            <div id="userInputMsg" class="userInputMsg">
                                <input type="hidden" id="code" value="${_c}">
                                <input type="hidden" id="ut" value="${_ut}">
                                <input type="hidden" id="un" value="${_u}">
                                <input type="hidden" id="type" value="${type}">

                                <p>用户名：<span style="padding-right:153px; font-weight:bold;">${userName}</span></p>

                                <p>新密码：<input type="password" id="txt_pwd1" class="inpuText"/></p>

                                <p>确认新密码：<input type="password" id="txt_pwd2" class="inpuText"/></p>

                                <p style="margin:35px 0px;">
                                    <span class="btn_next" style="margin-right:90px;" id="btn_update_sub">重置密码</span>
                                </p>
                            </div>
                            <div class="tips" id="tips"><img src="${imageDomainUrl}/img/jg.gif" alt=""><span
                                    id="tipsMsg" style="color: red">友情提示：请认真填写以上所有信息。</span></div>
                        </c:when>
                        <c:otherwise>
                            <div id="updateMsg" class="tips">找回密码的链接错误、已过期或者链接失效。
                                <p style="margin:35px 0px; text-align:center;">
                                    <span class="btn_next" id="btn_login_sub" onclick="gotoGetBackPwd()"> 返回找回密码 </span>
                                </p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div id="updateMsg" class="updateMsg"></div>
            </div>

        </div>
    </div>
</div>
<!-- 找回密码部分结束-->
<jsp:include page="/common/bootom.jsp" flush="true"/>
</body>

<script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/jquery.js"></script>
<script type="text/javascript">
    $(function () {
        $('#txt_pwd1').focus();
        $('#btn_update_sub').click(function () {
            doUpdate();
        });
        btnHover();
    });
    function gotoGetBackPwd() {
        window.location.href = "/FindPwd";
    }
    function login() {
        window.location.href = "http://sq.7road.com";
    }
    function btnHover() {
        $('.btn_next').hover(
                function () {
                    $(this).addClass('btn_next_hover');
                }, function () {
                    $(this).removeClass('btn_next_hover');
                }
        );
    }

    function doUpdate() {
        var _un = $("#un").val();
        var _ut = $("#ut").val();
        var _c = $("#code").val();
        var _t = $("#type").val();
        var pwd = $('#txt_pwd1').val();
        var r_pwd = $('#txt_pwd2').val();
        if (pwd.length < 1) {
            $('#tipsMsg').html('操作提示：新密码不能为空！');
            $('#txt_pwd1').focus();
            return false;
        } else if (r_pwd.length < 1) {
            $('#tipsMsg').html('操作提示：重复密码不能为空！');
            $('#txt_pwd2').focus();
            return false;
        } else if (r_pwd != pwd) {
            $('#tipsMsg').html('操作提示：两次密码不相同！');
            $('#txt_pwd2').focus();
            return false;
        } else {
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/ResetPwdSubmit",
                data:{un:_un, ut:_ut, _c:_c, pwd:pwd, r_pwd:r_pwd, type: _t},
                beforeSend:function (XMLHttpRequest, textStatus) {

                },
                success:function (msg) {
                    if (msg.code > 0)$('#tipsMsg').html(msg.msg);
                    else {
                        var html = '<span class="up_suc">✓</span>密码修改成功！';
                        var btnHtml = '<p style="margin:35px 0px; text-align:center;"><span class="btn_next" id="btn_login_sub" onclick="login()"> 转到登录 </span></p> ';
                        $('#inputMsg').hide();
                        $('#updateMsg').show().html(html);
                        $('#updateMsg').after(btnHtml);
                        btnHover();
                    }
                },
                error:function (msg) {
                    $('#tipsMsg').html("未知请求或者不能连接上服务器");
                }
            });
        }
    }
</script>
</html>