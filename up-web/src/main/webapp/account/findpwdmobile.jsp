<%--
    Author     : jaingfan.zhou
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>

<title></title>
<link rel="shortcut icon" href="${imageDomainUrl}/img/favicon.ico" />
<meta name="baidu-site-verification" content="NHoJdn8RaeEp87Ly"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<!-- stylesheets -->
<link rel="stylesheet" href="${imageDomainUrl}/css/forgetpwd-layout.css" type="text/css"/>
<link rel="stylesheet" href="${imageDomainUrl}/css/forgetpwd-style.css" type="text/css"/>
<link href="${imageDomainUrl}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

<link href="${imageDomainUrl}/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
<!--[if lte IE 7]>
<link rel="stylesheet" href="${imageDomainUrl}/css/iehacks.css" type="text/css"/>
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
    background: url(${imageDomainUrl}/img/cz_left_menu01_on.jpg) no-repeat;
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

    /*        .btnPannel{
        margin-top:50px;
        text-align: center;
    }*/
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

.tabs {
    border-bottom: 0 dashed #666666;
    margin: 0 1px;
    text-align: center;
    background-color: white;
}

.ul_tabs {
    background: url("${imageDomainUrl}/img/pic_titline.gif") repeat-x scroll 0 0 transparent;
    font-size: 14px;
    height: 31px;
    overflow: hidden;
    padding: 0 19px;
    text-align: left;
    width: auto;
}

.tb-item {
    background: url("${imageDomainUrl}/img/pic_titover.gif") no-repeat scroll 0 0 transparent;
    color: #F64A00;
    float: left;
    font-weight: bold;
    height: 31px;
    line-height: 31px;
    margin-right: 10px;
    text-align: center;
    width: 115px;
}

.getPwdMain {
    background-color: white;
    height: 340px;
    padding-top: 58px;
}

.getContent {
    width: 300px;
    height: 230px;
    line-height: 230px;
    margin: -20px 0px 0px 200px;
}

.inpuText {
    width: 200px;
    border: 1px solid #ccc;
    padding: 4px;
}

.imgCode {
    border: 0px;
    margin-left: 58px;
}

.codeText {
    text-decoration: underline;
    color: black;
    cursor: pointer;
    margin-left: 10px;
}

.btn_next {
    background-color: #FE9400;
    padding: 5px 20px;
    border-radius: 3px;
    cursor: pointer;
    margin-left: 110px;
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
    background-color: #FEF5EE;
    padding-top: 20px;
}

.sendMsg {
    border: 1px solid #FF6D1F;
    padding: 30px 0px;
    text-align: center;
    width: 510px;
    margin: 0 auto;
    margin-top: 160px;
    background-color: #FEF5EE;
    display: none;
    font-weight: bold;
}

.user_email {
    color: red;
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
                    <div id="tabs" class="tabs">
                        <ul class="ul_tabs">
                            <li id="item-1" class="tb-item">密保手机找回密码</li>
                        </ul>
                    </div>

                    <div class="getPwdMain" id="getPwd_item_1">
                        <div class="getContent">
                            <form id="mobileForm" name="mobileForm" method="post" action="FindPwdReset">
                                <input type="hidden" id="userName" name="userName" value="${userName}">
                                <input type="hidden" id="code" name="code" value="${code}">
                                <input type="hidden" id="type" name="type" value="${type}">
                                <input type="hidden" id="verify" name="verify" value="${verify}">
                                <input type="hidden" id="mobileVerify" name="mobileVerify" value="">

                                <p>手机号码：${mobile} <span id="span_q1"></span> <a href="javascript:;">发送验证码</a></p>
                                <p>验证码：<input type="text" id="mobileCode" name="mobileCode" maxlength="30"></p>

                                <p style="margin:35px 0px;"><span class="btn_next" id="btn_next_sub">下一步</span></p>
                            </form>
                        </div>
                        <div class="tips" id="tips">
                            <img src="${imageDomainUrl}/img/jg.gif" alt="">
                            <span id="tipsMsg" style="color: red">友情提示：请认真填写以上所有信息。</span>
                        </div>
                    </div>
                </div>
                <div id="sendMsg" class="sendMsg"></div>

        </div>
    </div>
</div>
<!-- 找回密码部分结束-->

</div>
<br/>
<br/>
<jsp:include page="/common/bootom.jsp" flush="true"/>
</body>
<script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/jquery.js"></script>
<script type="text/javascript">

    $(function () {
        btnHover();
        $("#mobileForm a").click(function(){
            sendMobileCode();
        });

        $("#btn_next_sub").click(function(){
            if ($("#mobileVerify").val() == ""){
                $("#tipsMsg").text("没有发送验证码");
                return;
            }
            if ($("#mobileCode").val() == "") {
                $("#tipsMsg").text("验证码不能为空");
                return;
            }
            document.mobileForm.submit();
        });
        //$("#btn_next_sub").hide();
    });

    function btnHover() {
        $('.btn_next').hover(
                function () {
                    $(this).addClass('btn_next_hover');
                }, function () {
                    $(this).removeClass('btn_next_hover');
                }
        );
    }

    function sendMobileCode(){
        var _t = new Date().getTime();
        var user = $("#userName").val();

        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"/FindPwdSendMobileCode",
            data:{_u:user, _t:_t },
            beforeSend:function (XMLHttpRequest, textStatus) {

            },
            success:function (msg) {
                if (msg.code > 0) {
                    $("#tipsMsg").text(msg.msg);
                    $("#mobileVerify").val( _t);

                    var tagSend = $("#mobileForm a");
                    tagSend.unbind("click");
                    var tDelay = 60;
                    var fnDelay = function(){
                        if (tDelay < 0) {
                            tagSend.bind("click", sendMobileCode);
                            tagSend.text("重新获取");
                        } else {
                            tagSend.text("(" + tDelay + "秒)重新获取");
                            tDelay --;
                            setTimeout(function(){fnDelay();},1000);
                        }
                    };
                    fnDelay();
                } else {
                    $("#tipsMsg").text(msg.msg);
                }
            },
            error:function (msg) {
                $('#tipsMsg').html("未知请求或者不能连接上服务器");
            }
        });
    }

</script>
</html>