<!doctype html>
<html>
<head>
<%@page contentType="text/html;charset=utf-8" %>
<%
    String path = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>游戏充值中心-第七大道网页游戏平台</title>
<meta name="Description" content="第七大道网页游戏平台是第七大道旗下专业网页游戏运营平台，致力于为全球游戏玩家提供健康、快乐、绿色的网页游戏，最新最全最好玩的网页游戏，尽在第七大道网页游戏。" />
<meta name="Keywords" content="网页游戏、第七大道、第七大道网页游戏、第七大道神曲、第七大道弹弹堂、第七大道海神" />
<!-- stylesheets -->
<script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/jquery.js"></script>
<script type="text/javascript" charset="UTF-8" src="${staticDomainUrl}/scripts/online_game.js?v=20130228"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/bootstrap.min.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery.gritter.js"></script>
<link rel="shortcut icon" href="${imageDomainUrl}/img/favicon.ico"/>
<link rel="stylesheet" href="${staticDomainUrl}/css/layout.css" type="text/css"/>
<link rel="stylesheet" href="${staticDomainUrl}/css/style.css" type="text/css"/>
<link rel="stylesheet" href="${staticDomainUrl}/css/showMessage.css" type="text/css"/>
<!--[if lte IE 7]>
<link rel="stylesheet" href="${staticDomainUrl}/css/iehacks.css" type="text/css"/>
<![endif]-->
<link rel="stylesheet" href="${staticDomainUrl}/css/showcss.css" type="text/css"/>
<style type="text/css">
.czqr, .czcg {
    display: block;
    position: fixed;
    _position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -161px;
    _margin-top: 0;
    margin-left: -244px;
    z-index: 1002;
    _top: expression(eval(document.compatMode && document.compatMode=='CSS1Compat') ? documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 : document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);
}
.mt_6{ /*margin-top: 6px;*/}
.mt_1{ margin-top: 1px;}
.close {
    float: none;
    padding: 0;
    margin: 5px 0 0 440px;
}

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
    background: url(${imageDomainUrl}/img/menu_03_on.jpg) no-repeat;
}

.m4 {
    display: inline-block;
    width: 130px;
    height: 74px;
    background: url(${imageDomainUrl}/img/menu_04.jpg) no-repeat;
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
    padding-left: 0px;
    margin-bottom: 0;
}

.nav {
    margin-bottom: 18px;
    margin-left: 0;
    list-style: none;
}

.nav-list .active a {
    background: url(${imageDomainUrl}/img/cz_left_menu01_on.jpg) no-repeat;
    color: #fff;
}

.nav-list  li a {
    background: url(${imageDomainUrl}/img/cz_left_menu01.jpg) no-repeat;
    padding: 0;
    text-align: center;
    width: 193px;
    display: block;
	font-size:14px;
}
.nav-list .active a:hover{
    color:#fff;
}
.nav-list  li  a:hover {
    font-size: 1.1em;
    color: #888;
    text-decoration: none;
}

.nav-header {
    margin-right: -15px;
    margin-left: 0px;
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
    overflow: hidden;
    height: 322px;
    width: 487px;
}

.czqr {
    background: url(${imageDomainUrl}/img/czqr.jpg) no-repeat;

    height: 323px;

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
    font-family: "微软雅黑", Arial, "MS Trebuchet", sans-serif;
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

    /*        .nav-tabs > li {margin-left: 8px;}
    .tab-pane{margin-top:10px;}
    .breadcrumb{border: 0;border-bottom: 1px solid #ddd;}
    .nav-tabs li a {
      background-repeat: no-repeat;
      background-position: 17px -36px;
    }

    */
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

a#submit_button {

    width: 153px;
    height: 46px;
    background-image: url(${imageDomainUrl}/img/btn1_off.jpg);
    line-height: 46px;
    text-decoration: none;

    display: inline-block;
    color: #FFFFFF;

}

a#reset_button {
    width: 153px;
    height: 46px;
    background-image: url(${imageDomainUrl}/img/btn2_off.jpg);
    line-height: 46px;
    text-decoration: none;
    display: inline-block;

    color: #FFFFFF;

}

a#submit_button:hover {
    background-image: url(${imageDomainUrl}/img/btn1_on.jpg);
}

a#reset_button:hover {
    background-image: url(${imageDomainUrl}/img/btn2_on.jpg);
}

.img_border {
    border: 1px solid #FE9900;
}
/*20130109edit*/
.blank20{ height:20px; overflow:hidden; font-size:0; clear:both; display:block;}
</style>
</head>
<body>
<div class="ym-wrapper">
<jsp:include page="../common/topClient2.jsp" flush="false"></jsp:include>
<div id="main"  style="width:980px;margin:0 auto;overflow:hidden;clear:both;*zoom:1;">
<div class="ym-column">
<div class="ym-col1">
    <div class="sidebar-nav">
        <ul id="payUl" class="nav nav-list">
            <li class="nav-header well0"
                style="text-align: center;padding-top: 5px;width: 142px; font-size:15px;background:url(${imageDomainUrl}/img/cz-qrxinxi_cen_bg.jpg) repeat-x;">
                游戏充值
            </li>
            <li class="active">
            <a href="#" id="aa1" style="padding-left: 12px; text-align: left; width: 181px;">网银充值(储蓄\信用卡)</a></li>
            <li><a href="#" id="aa3">支付宝余额充值</a></li>
            <li><a href="#" id="aa2">财付通充值</a></li>
            <li><a href="#" id="aa4">神州行卡充值</a></li>
            <li><a href="#" id="aa5">联通充值卡充值</a></li>
            <li><a href="#" id="aa6">电信充值卡充值</a></li>
            <li><a href="#" id="aa7">盛大一卡通</a></li>
            <li><a href="#" id="aa8">骏网一卡通</a></li>
            <li><a href="#" id="aa9">搜狐一卡通</a></li>
            <li><a href="#" id="aa10">其他卡类支付</a></li>

        </ul>
    </div>

    <div style="margin-top:1.5em;">
        <a href="${payHelpUrl}" target="_blank"><img src="${imageDomainUrl}/img/cz_right_img03.jpg"></a>
    </div>
</div>

<div class="ym-col3" style="float:left;margin:0;width:780px">
<div style="margin-top:1.5em;">
<div id="payNet" style="border: 1px solid rgba(0, 0, 0, 0.05);">
<div style="text-align:center;padding-bottom: 12px;"><img src="${imageDomainUrl}/img/tem_05.jpg" width="777"
                                                          height="26"></div>
<form id="accountForm" method="post" action="${gatewayPurchaseUrl}" target="_blank">
    <input type="hidden" name="user" value="" id="user">
    <input type="hidden" name="_z" value="" id="server">
    <input type="hidden" name="p" value="" id="role">
    <input type="hidden" name="amount" value="" id="amount">
    <input type="hidden" name="_g" id="gameId" value="${requestScope.gameId }">
    <input type="hidden" name="_c" value="C" id="channel">
    <input type="hidden" name="_s" value="3" id="s_id">
    <input type="hidden" name="_n" value="" id="sign">
    <input type="hidden" name="_f" value="" id="f_id">
</form>
<form class="ym-form ym-columnar" id="form" method="post" action="${gatewayPurchaseUrl}" target="_blank">
<div class="ym-grid">
    <div class="ym-g15 ym-gl">
        <label for="game" style="height:70px;line-height: 70px;overflow: hidden;">充值游戏:</label>
    </div>
    <div class="ym-g25 ym-gl">
        <div class="ym-gbox ym-fbox-text">
            <img src="${imageDomainUrl}/img/${game.gameLogoChargePage}" width="201" height="70" id="game">
        </div>
    </div>

    <div class="ym-g15 ym-gl">
        <label for="server_id" style="height:70px;line-height: 70px;overflow: hidden;text-align: center;">充值服务器:</label>
    </div>
    <div class="ym-g30 ym-gl">
        <div class="ym-fbox-select" style="display: inline-table;
margin-top: 20px;
margin-left: -30px;">
            <select id="server_id">
            </select>
        </div>
    </div>


</div>

<div class="ym-grid">

</div>

<div class="ym-grid">
    <div class="ym-g15 ym-gl mt_6">
        <label for="username">七道通行证:</label>
    </div>
    <div class="ym-g30 ym-gl">
        <div class="ym-fbox-text">
            <input type="text" id="username" placeholder="请在此输入您的账号." maxlength="20" onBlur="userNameOnBlur()"
                   onfocus="userNameOnFocus()" value="" onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')"/>
        </div>
    </div>
    <div class="ym-g30 ym-gl mt_6" id="userNameTip">
        请输入您的七道账号
    </div>
</div>
<div class="ym-grid">
    <div class="ym-g15 ym-gl mt_6">
        <label for="username">确认账号:</label>
    </div>
    <div class="ym-g30 ym-gl">
        <div class="ym-fbox-text">
            <input type="text" id="username_1" placeholder="请再次输入您的账号." maxlength="20" onBlur="userName1OnBlur()"
                   onfocus="userName1OnFocus()" onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')" value="">
        </div>
    </div>
    <div class="ym-g30 ym-gl mt_6" id="userName1Tip">
        请再次输入您的七道账号
    </div>
</div>
<div class="ym-grid">
    <div class="ym-g15 ym-gl mt_6">
        <label for="server_id">充值角色:</label>
    </div>
    <div class="ym-g30 ym-gl">
        <div class="ym-fbox-select">
            <select id="actor_name" style="width:205px;">
                <option value="0" selected="selected">--请先选择服务器--</option>
            </select>
        </div>
    </div>
</div>

<div class="ym-grid">
    <div class="ym-g15 ym-gl mt_6">
        <label for="selectMoney">充值金额:</label>
    </div>
    <div class="ym-g30 ym-gl">
        <div id="selectPay" class="ym-fbox-select">
            <select id="selectMoney" onChange="moneyChange()" style="width:205px;">
                <option value="0">请选择充值金额</option>
                <option value="5000">5000元</option>
                <option value="2000">2000元</option>
                <option value="1000">1000元</option>
                <option value="800">800元</option>
                <option value="500">500元</option>
                <option value="300">300元</option>
                <option value="200">200元</option>
                <option value="100">100元</option>
                <option value="50">50元</option>
                <option value="20">20元</option>
                <option value="10">10元</option>
            </select>
        </div>
        <!--自定义金额显示-->
        <div class="ym-fbox-text" id="custom_charge" style="display:none;">
            <input type="text" value="" id="money" onKeyUp="checkMoney();" maxlength="10"/>
        </div>

    </div>
    <div class="ym-g30 ym-gl">
        <a href="javascript:void(0);" class="ym-button" id="MyInputMoney">自定义金额</a>

        <div id="moneyTip" style="display:none;">
            请输入充值的金额（不小于10的整数）
        </div>
    </div>
</div>

<div class="ym-grid">
    <div class="ym-g15 ym-gl mt_6">
        <label>充值游戏币:</label>
    </div>
    <div class="ym-g30 ym-gl">
        <div class="ym-fbox-text">
            <input type="text" disabled="" id="coin" style="color:#FF6201;font-weight:bold;font-size:15px;">
        </div>
    </div>
    <div class="ym-g25 ym-gl mt_6" id="proportion">
        充值比例1:${game.goldScale}
    </div>
</div>

<div class="ym-grid" id="select_bankOfTen" style="display: none;">
    <div class="ym-g15 ym-gl mt_6">
        <label>选择银行:</label>
    </div>

    <div class="ym-g80 ym-gl">
        <!--                        第一列银行-->
        <div class="ym-g30 ym-gl">
            <div class="ym-fbox-check">
                <div class="bankradio">
                    <input type="radio" name="_ften" id="BOC_ten"  value="BOC"/>
                    <img src="${imageDomainUrl}/img/bk7.jpg" width="126"
                         alt="中国银行" height="36" onClick="checkedRaido('BOC_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften"  id="ICBC_ten" value="ICBC">
                    <img src="${imageDomainUrl}/img/bk1.jpg" width="126" height="36"
                         alt="工商银行" onClick="checkedRaido('ICBC_ten');"/>
                </div>


                <div class="bankradio">
                    <input type="radio" name="_ften" id="SDB_ten" value="SDB"/>
                    <img src="${imageDomainUrl}/img/bk10.jpg" width="126"
                         alt="深圳发展银行" height="36" onClick="checkedRaido('SDB_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="PAB_ten" value="PAB">
                    <img src="${imageDomainUrl}/img/pabank.jpg" width="126"
                         alt="平安银行" height="36" onClick="checkedRaido('PAB_ten');">
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften"  id="CIB_ten" value="CIB">
                    <img src="${imageDomainUrl}/img/xybank.jpg" width="126" height="36"
                         alt="兴业银行" onClick="checkedRaido('CIB_ten');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften" id="BOB_ten" value="BOB"/>
                    <img src="${imageDomainUrl}/img/bjbank.jpg" width="126"
                         alt="北京银行" height="36" onClick="checkedRaido('BOB_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="GDB_ten" value="GDB"/>
                    <img src="${imageDomainUrl}/img/gfbank.jpg" width="126"
                         alt="广发银行" height="36" onClick="checkedRaido('GDB_ten');"/>
                </div>
            </div>
        </div>

        <!--  第二 列银行-->
        <div class="ym-g30 ym-gl">

            <div class="ym-fbox-check">
                <div class="bankradio">
                    <input type="radio" name="_ften" id="CMB_ten" value="CMB"/>
                    <img src="${imageDomainUrl}/img/bk6.jpg" width="126"
                         alt="招商银行" height="36" onClick="checkedRaido('CMB_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="COMM_ten" value="COMM"/>
                    <img src="${imageDomainUrl}/img/bk5.jpg" width="126"
                         alt="交通银行" height="36" onClick="checkedRaido('COMM_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="CEB_ten" value="CEB"/>
                    <img src="${imageDomainUrl}/img/bk8.jpg" width="126"
                         alt="光大银行" height="36" onClick="checkedRaido('CEB_ten');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften" id="SPDB_ten" value="SPDB"/>
                    <img src="${imageDomainUrl}/img/bk11.jpg" width="126"
                         alt="浦发银行" height="36" onClick="checkedRaido('SPDB_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" checked="checked" id="NJCB_ten" value="NJCB">
                    <img src="${imageDomainUrl}/img/njbank.jpg" width="126" height="36"
                         alt="南京银行" onClick="checkedRaido('NJCB_ten');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften" id="NBCB_ten" value="NBCB"/>
                    <img src="${imageDomainUrl}/img/nbbank.jpg" width="126"
                         alt="宁波银行" height="36" onClick="checkedRaido('NBCB_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="POSTGC_ten" value="POSTGC">
                    <img src="${imageDomainUrl}/img/yzyh.jpg" width="126"
                         alt="邮政银行" height="36" onClick="checkedRaido('POSTGC_ten');">
                </div>
            </div>
        </div>
        <div class="ym-g30 ym-gl">
            <div class="ym-fbox-check">
                <div class="bankradio">
                    <input type="radio" name="_ften" id="CCB_ten" value="CCB"/>
                    <img src="${imageDomainUrl}/img/bk2.jpg" width="126"
                         alt="建设银行" height="36" onClick="checkedRaido('CCB_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="ABC_ten" value="ABC"/>
                    <img src="${imageDomainUrl}/img/bk3.jpg" width="126"
                         alt="农业银行" height="36" onClick="checkedRaido('ABC_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" id="CITIC_ten" value="CITIC"/>
                    <img src="${imageDomainUrl}/img/bk9.jpg" width="126"
                         alt="中信银行" height="36" onClick="checkedRaido('CITIC_ten');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften" id="CMBC_ten" value="CMBC"/>
                    <img src="${imageDomainUrl}/img/bk12.jpg" width="126"
                         alt="中国民生银行" height="36" onClick="checkedRaido('CMBC_ten');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_ften" checked="checked" id="BEA_ten" value="BEA">
                    <img src="${imageDomainUrl}/img/dybank.jpg" width="126" height="36"
                         alt="东亚银行" onClick="checkedRaido('BEA_ten');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften" id="SRCB_ten" value="SRCB"/>
                    <img src="${imageDomainUrl}/img/shnsbank.jpg" width="126"
                         alt="上海农商银行" height="36" onClick="checkedRaido('SRCB_ten');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_ften" id="BL_ten" value="BL"  style="float:left;margin-top: 0;">
                    <span width="126" height="36" onClick="checkedRaido('BL_ten');"
                          style="float: left; margin: 4px 0 0 7px;">财付通余额充值</span>
                    <span style="clear:both"></span>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="ym-grid" id="select_bank">
    <div class="ym-g15 ym-gl">
        <label>选择银行:</label>
    </div>

    <div class="ym-g80 ym-gl">
        <!--                        第一列银行-->
        <div class="ym-g30 ym-gl">
            <div class="ym-fbox-check">
                <div class="bankradio">
                    <input type="radio" name="_f1" checked="checked" id="ICBC" value="ICBCB2C">
                    <img src="${imageDomainUrl}/img/bk1.jpg" width="126" height="36"
                         onclick="checkedRaido('ICBC');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_f1" id="POSTGC" value="POSTGC">
                    <img src="${imageDomainUrl}/img/bk4.jpg" width="126" height="36"
                         onclick="checkedRaido('POSTGC');">
                </div>
                <div class="bankradio">
                    <input type="radio" name="_f1" id="BOC" value="BOCB2C"/>
                    <img src="${imageDomainUrl}/img/bk7.jpg" width="126" height="36" onClick="checkedRaido('BOC');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_f1" id="SDB" value="SDB"/>
                    <img src="${imageDomainUrl}/img/bk10.jpg" width="126" height="36"
                         onclick="checkedRaido('SDB');"/>
                </div>
            </div>
        </div>

        <!--  第二 列银行-->
        <div class="ym-g30 ym-gl">
            <div class="ym-fbox-check">
                <div class="bankradio">
                    <input type="radio" name="_f1" id="CCB" value="CCB"/>
                    <img src="${imageDomainUrl}/img/bk2.jpg" width="126" height="36" onClick="checkedRaido('CCB');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_f1" id="BOCO" value="COMM"/>
                    <img src="${imageDomainUrl}/img/bk5.jpg" width="126" height="36"
                         onclick="checkedRaido('BOCO');"/>
                </div>
                <div class="bankradio">
                    <input type="radio" name="_f1" id="CEBBANK" value="CEBBANK"/>
                    <img src="${imageDomainUrl}/img/bk8.jpg" width="126" height="36"
                         onclick="checkedRaido('CEBBANK');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_f1" id="SPDB" value="SPDB"/>
                    <img src="${imageDomainUrl}/img/bk11.jpg" width="126" height="36"
                         onclick="checkedRaido('SPDB');"/>
                </div>
            </div>

        </div>

        <div class="ym-g30 ym-gl">
            <div class="ym-fbox-check">
                <div class="bankradio">
                    <input type="radio" name="_f1" id="ABC" value="ABC"/>
                    <img src="${imageDomainUrl}/img/bk3.jpg" width="126" height="36" onClick="checkedRaido('ABC');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_f1" id="CMBCHINA" value="CMB"/>
                    <img src="${imageDomainUrl}/img/bk6.jpg" width="126" height="36"
                         onclick="checkedRaido('CMBCHINA');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_f1" id="ECITIC" value="CITIC"/>
                    <img src="${imageDomainUrl}/img/bk9.jpg" width="126" height="36"
                         onclick="checkedRaido('ECITIC');"/>
                </div>

                <div class="bankradio">
                    <input type="radio" name="_f1" id="CMBC" value="CMBC"/>
                    <img src="${imageDomainUrl}/img/bk12.jpg" width="126" height="36"
                         onclick="checkedRaido('CMBC');"/>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="ym-grid" id="payway">
    <div class="ym-g15 ym-gl">
        <label>支付方式:</label>
    </div>
    <div class="ym-g66 ym-gl">
        <div class="ym-grid">

            <div class="ym-g30 ym-gl" id="applay">
                <div class="ym-fbox-check">
                    <input type="radio" id="payA" name="pay" value="A" style="margin:0; padding:0;" checked="checked" />
                    <label onClick="checkedRaido('payA');"  style="margin:0; padding:0;">支付宝</label>
                </div>
            </div>
            <%--<div class="ym-g30 ym-gl" id="tenplay">--%>
            <%--<div class="ym-fbox-check">--%>
            <%--<input type="radio" id="payB" name="pay" value="B" checked="checked"/>--%>
            <%--<label onclick="checkedRaido('payB');">财付通</label>--%>
            <%--</div>--%>
            <%--</div>--%>
            <div class="ym-g30 ym-gl" id="quick">
                <div class="ym-fbox-check">
                    <input type="radio" id="payD" name="pay" value="D" style="margin:0; padding:0;" />
                    <label onClick="checkedRaido('payD');" style="margin:0; padding:0;">快钱</label>
                </div>
            </div>

            <div class="ym-g38 ym-gl">
                <div class="ym-fbox-check" id="yibao">
                    <input type="radio" name="pay" id="payC" value="C" style="margin:0; padding:0;" />
                    <label onClick="checkedRaido('payC');"  style="margin:0; padding:0;">易宝支付</label>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="ym-grid" id="paytype">
    <div class="ym-g15 ym-gl">
        <label>充值卡类型:</label>
    </div>
    <div class="ym-g80 ym-gl">
        <div class="ym-grid">
            <div class="ym-g50 ym-gl" style="width:200px;">
                <div class="ym-fbox-check">
                    <input type="radio" id="ZHENGTU" name="card" checked="checked" value="D"  style="margin:0; padding:0;">
                    <label onClick="checkedRaido('ZHENGTU');"  style="margin:0; padding:0;">征途游戏卡</label>
                </div>
            </div>
            <div class="ym-g50 ym-gl"  style="width:200px;">
                <div class="ym-fbox-check">
                    <input type="radio" id="U" name="card" value="U"  style="margin:0; padding:0;">
                    <label onClick="checkedRaido('U');"  style="margin:0; padding:0;">完美一卡通</label>
                </div>
            </div>
        </div>

        <div class="ym-grid">
            <div class="ym-g50 ym-gl"   style="width:200px;">
                <div class="ym-fbox-check">
                    <input type="radio" id="M" name="card" value="M"  style="margin:0; padding:0;">
                    <label onClick="checkedRaido('M');"  style="margin:0; padding:0;">网易一卡通</label>
                </div>
            </div>
            <div class="ym-g50 ym-gl"   style="width:200px;">
                <div class="ym-fbox-check">
                    <input type="radio" id="ZONGYOU-NET" name="card" value="ZONGYOU-NET"  style="margin:0; padding:0;">
                    <label onClick="checkedRaido('ZONGYOU-NET');"  style="margin:0; padding:0;">纵游游戏卡</label>
                </div>
            </div>
        </div>

        <div class="ym-grid" id="otherPayWay" style=" display:none;">
            <div class="ym-g50 ym-gl">
                <div class="ym-fbox-check">
                    <input type="radio" id="TIANXIA-NET" name="card" value="TIANXIA-NET"  style="margin:0; padding:0;">
                    <label onClick="checkedRaido('TIANXIA-NET');"  style="margin:0; padding:0;">天下一卡通</label>
                </div>
            </div>
            <div class="ym-g50 ym-gl">
                <div class="ym-fbox-check">
                    <input type="radio" id="TIANHONG-NET" name="card" value="TIANHONG-NET"  style="margin:0; padding:0;">
                    <label onClick="checkedRaido('TIANHONG-NET');"  style="margin:0; padding:0;">天宏一卡通</label>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="ym-grid">

    <div class="ym-g50 ym-gl" style="text-align:center;">
        <a href="javascript:void(0)"  id="submit_button">
            <!--                          <img src="${imageDomainUrl}/img/btn1_off.jpg"  />-->
        </a>
    </div>
    <div class="ym-g50 ym-gr" style="text-align:left;">
        <a href="#" onClick="payFormReset();" id="reset_button">
            <!--                          <img src="${imageDomainUrl}/img/btn2_off.jpg" />-->
        </a>
    </div>


</div>
<div class="blank20"></div>
</form>

</div>

<div class="ym-grid">
    <div class="ties">
        <b>温馨提示：</b>
        您在充值成功后，此次充值金额可能会需要一段时间才会出现在您的账号中，请您不要担心，这是正常现象。如果充值失败，请选择其他充值方式
    </div>
</div>

<div>
<div class="descripe" id="unionpay">
                    <span>
                      <strong>网银充值(储蓄\信用卡)说明：</strong>
                    </span>

    <ol>
        <li>您必须开通了网上银行业务</li>
        <li>使用网银支付，对消费者来说，目前不需要任何的手续费</li>
        <li>请您关闭所有屏蔽弹出窗口之类的功能，否则在线支付将无法继续，比如：3721、上网助手等</li>
        <li>请使用IE，TT，Maxthon浏览器以确保快钱充值成功</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div class="descripe" id="alipay">
                    <span>
                      <strong>支付宝余额充值说明：</strong>
                    </span>

    <ol>
        <li>您必须开通了网上银行业务</li>
        <li>使用网银支付，对消费者来说，目前不需要任何的手续费</li>
        <li>请您关闭所有屏蔽弹出窗口之类的功能，否则在线支付将无法继续，比如：3721、上网助手等</li>
        <li>请使用IE，TT，Maxthon浏览器以确保快钱充值成功</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>
<div id="tenpay" class="descripe" style="display:none;">
                    <span>
                          <strong>财付通充值说明：</strong>
                    </span>
    <ol class="desOl">
        <li>通过财付通可以直接为账号充值</li>
        <li>充值成功后需要1-10分钟到账，请耐心等待！如果您需要充值咨询，请致电我们的客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="shenzhou" class="descripe">
                    <span>
                       <strong>神州行卡(快钱,易宝)充值说明：</strong>
                    </span>

    <ol class="desOl">
        <li>请确认您选择的“充值卡实际面额”和实际的充值卡面额一致，否则会导致支付不成功和余额丢失</li>
        <li>移动支持的充值卡面额为10元、20元、30元、50元、100元、300元、500元，请勿使用其他面额进行支付</li>
        <li>请确认您的充值卡是序列号17位、密码18位，由中国移动发行的全国通用的神州行充值卡，而且是没有使用过的</li>
        <li>本充值方式还支持江苏、浙江、广东、辽宁、福建的神州行地方卡</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="unioncom" class="descripe">
                    <span>
                        <strong>联通充值卡(快钱,易宝)充值说明：</strong>
                    </span>

    <ol class="desOl">
        <li>请确认您选择的“充值卡实际面额”和实际的充值卡面额一致，否则会导致支付不成功和余额丢失</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>

</div>

<div id="chinanet" class="descripe">
                     <span>
                          <strong>电信充值卡(快钱,易宝)充值说明：</strong>
                    </span>

    <ol class="desOl">
        <li>填写充值帐号及选择购买面值等相关信息后提交支付申请</li>
        <li>按照跳转页面显示的声讯热线号码进行拨打</li>
        <li>拨打声讯热线确认支付，充值成功</li>
        <li>扣除15元充价值10元的游戏币，扣除30元充价值20元的游戏币，扣除45元充价值30元的游戏币</li>
        <li>如有疑问，请联系全国电信固话手机充值客服：021-65753393或第七大道在线客服</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="shengda" class="descripe">
                    <span>
                          <strong>盛大一卡通充值说明：</strong>
                      </span>

    <ol class="desOl">
        <li>填写充值帐号及选择购买面值等相关信息后提交支付申请</li>
        <li>请使用卡号以CSC5、CS、S、CA、CSB、YC、YD、YA、YB开头的“盛大互动娱乐卡”进行支付，暂不支持SC开头的卡</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="junwang" class="descripe">
                    <span>
                          <strong>骏网一卡通充值说明：</strong>
                      </span>

    <ol class="desOl">
        <li>请确认您的骏卡充值卡是由“北京汇元网科技有限责任公司”发行的充值卡</li>
        <li>请按卡面金额进行充值，如填写额度不正确可能会无法完成充值</li>
        <li>如果有疑问，请拨打骏卡客服电话：010－59059099</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="sohupay" class="descripe">
                    <span>
                         <strong>搜狐一卡通充值说明：</strong>
                      </span>

    <ol class="desOl">
        <li>请使用卡号以CSC5、CS、S、CA、CSB、YC、YD、YA、YB开头的“搜狐互动娱乐卡”进行支付，暂不支持SC开头的卡</li>
        <li>请按卡面金额进行充值，如填写额度不正确可能会无法完成充值</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="otherpay" class="descripe">
                    <span>
                          <strong>充值说明：</strong>
                    </span>

    <ol class="desOl">
        <li>充值卡支持的面额有：10元，20元，30元，50元，100元</li>
        <li>如果有疑问，请联系我们在线客服或拨打客服电话</li>
        <li style="color:red;">请充值时务必确认好您的充值金额准确无误后再进行充值，避免输错金额导致的失误，如因未仔细确认金额造成的充值问题，我们将一律不予处理此类退款申诉</li>
    </ol>
</div>

<div id="mannualpay" style="display:none;">

    <!--                <h1>暂未开放</h1>     -->
    一、联系方式：<br>
    在线客服：427924355(7×24小时) 客服电话：0755-61886777 （7x24小时）<br>
    <br><br>
    二、银行转帐充值流程：<br>
    1）、官方充值帐号：<br>
    <table class="bankTable">
        <tbody>
        <tr>
            <th width="100px">开户地</th>
            <th width="100px">开户行</th>
            <th width="200px">帐&nbsp;&nbsp;户</th>
            <th width="200px">卡&nbsp;&nbsp;号</th>
        </tr>
        <tr>
            <td>深圳</td>
            <td>中国工商银行</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>深圳</td>
            <td>中国农业银行</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>深圳</td>
            <td>中国建设银行</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>深圳</td>
            <td>招商银行</td>
            <td></td>
            <td></td>
        </tr>
        </tbody>
    </table>


    <br>
    因汇款查询较为繁琐，我们会在半个工作日内为您处理您的汇款，如您急需使用钻石，请更换其他方式进行充值或联系在线客服为您处理，对您造成的不便请您谅解。
    由于银行方面的结算制度，会使人工汇款有可能出现延迟到帐。下面我们为大家列出几种可能会出现延迟到帐的情况：<br>
    1、跨行汇款，例如：从工商银行汇款到农业银行的账户；<br>
    2、跨地区汇款，例如：从上海汇到深圳；<br>
    3、非银行结算时间段汇款，例如：周末或下午17:00以后；<br>
    以上情况只是有可能会出现延迟，并非100%出现。请各位朋友汇款后及时联系我们的客服人员，并提供相关资料，我们会在收到款后第一时间为您进行处理。如您急需使用游戏币，建议您选择其他方式进行充值，避免银行系统结算延迟给您造成的影响。
    温馨提示：目前第七大道接收汇款的账户暂不接收公司账户的汇款。<br>

    2）、兑换比例：<br>
    兑换比例(同网银比例)<br>
    3）、人工充值流程：<br>
    第1步、请从官网充值中心获取人工充值银行帐号信息。<br>
    第2步、请向官方相应银行帐号汇款或转帐。<br>
    第3步、联系在线客服以及客服电话。向充值客服提供准确的游戏帐号,游戏名称,游戏区服,游戏角色,充值银行,充值金额,真实姓名。<br>
    例如：<br>
    游戏帐号：abcd<br>
    游戏名称：神曲<br>
    游戏区服：[双线1服]神曲一区<br>
    游戏角色：abcd<br>
    充值银行：工商银行<br>
    汇款金额（元 人民币）：100.01<br>
    真实姓名：abcd<br>
    第4步、第七大道充值客服确认玩家汇款或转帐到账，实时充游戏币。<br>
    第5步、请玩家查收游戏币。<br>
    <br><br>
    三. Paypal转账 充值流程<br>
    1）请从官网充值中心获取官方 Paypal帐户： <br>
    2）向官方Paypal帐户进行转账。 <br>
    3）通过在线联系方式联系充值客服。提交信息： paypal帐户、paypal姓名、金额、交易号、游戏帐号、游戏名称、游戏区域、游戏角色 <br>
    4）充值客服在线的情况下，查询到账后会第一时间为您处理。<br>
    5）请玩家登录游戏查收游戏币 <br>
    注 ：a、电子支票从付款人的账户中结清资金后（通常需要3-5个工作日）才算转账成功。 <br>
    b、转账Paypal手续费由转账人承担。比例是：4%+0.3USD / 笔 <br>
    如：转账100美金，paypal手续费是100×4%+0.3USD / 笔，手续费是4.3美金。实际到账金额是：95.7美金。 <br>
    c、建议使用的币种选择为美金，以减少汇率损失。<br>
    d、为了保障paypal账户的安全性，请使用paypal转账充值的玩家提供身份证、驾照或护照其中一样扫描证件给充值客服核对，以便查询处理。此信息为官方核对paypal转账之用，不会外泄。<br>
    <br><br>
    四、特别说明：<br>
    1、人工充值sq.7road.com官方服务在线快充，每个账号目前只接受单笔充值100元RMB以上，其他方式请按照官网充值中心操作。<br>
    2、请汇款或转帐时将转账金额增加角和分，例如：充值100元，请转账100.01元到100.99元之间的金额，方便官方充值客服查询。<br>
    3、银行汇款或转帐的实到金额享有和网银充值(储蓄\信用卡)相同的充值比例，异地和跨行汇款或转帐的手续费根据各银行规定由银行向用户收取。<br>
    4、请汇款或转帐的时候保留单据和转账订单号。<br>
    5、如海外汇款的玩家，建议先向当地银行咨询所需资料以后再联系sq.7road.com客服人员，确认资料正确后再进行汇款。<br>
    6、充值完成后请联系sq.7road.com客服人员核对充值金额及提供充值资料。<br>
    7、请尽量选择我们支持的银行进行汇款，不建议使用跨行汇款，如条件不允许的情况下使用跨行汇款，请务必保留您的汇款凭据以及联系客服时将您的汇款流水号告知客服便于我们查询。跨行汇款处理时间3—5个工作日。<br>
    <br><br>
    五、服务说明：<br>
    1、人工充值需要sq.7road.com官方手动处理，务必正确告知客服您需要充值的游戏帐号，游戏帐号填写错误导致的损失请自行承担！<br>
    2、充值后如客服在线，半天内充值完成；如客服没在线，请留言，我们承诺第一时间为您充值！<br>
    3、核实完成后，请玩家立即进入游戏查收游戏币！<br>
    4、一旦充值成功，系统将不提供充值修正服务，如填写金额错误导致的损失我们不承担！<br>
    5、人工充值不需要您提供任何密码，请各位玩家提高安全意识，非本页公布的帐号信息均非我平台官方帐号。<br>
</div>
<!--人工充值-->
</div>
</div>
</div>
</div>
</div>
<br/>


<jsp:include page="../common/bottom.jsp" flush="false"></jsp:include>
</div>
</div>

<div class="czqr" id="showCon" style="display: none;min-width:0px;">
    <div class="close"><a href="javascript:void(0);" id="close">[关闭]</a></div>
    <div class="qr-contant" style="font-size:14px; ">
        <ul style=" margin:0; padding-top:0.5em">
            <li>您充值的方式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="pay_card_id">银行卡（快钱）充值</span>
            </li>
            <li>您充值的游戏&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="char_game_id">${game.gameName}</span>
            </li>
            <li>您充值的区服&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="char_ser_id" class="fnt_spe">烟雨暮雪（八服）</span>
            </li>
            <li>您充值的账号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="char_user" class="fnt_spe"></span>
            </li>
            <li>您充值的金额&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="char_amount" class="fnt_spe">10元</span>
            </li>
            <li>&nbsp;&nbsp;&nbsp;您充值所得&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                    id="char_game_amount" class="fnt_spe">100钻石</span></li>
        </ul>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <br>
        <a href="javascript:void(0);" id="ok_button" onClick="okbutton();"><img
                src="${imageDomainUrl}/img/czqr_btn_1.jpg"
                width="115" height="36"/></a>&nbsp;&nbsp;<a
            href="javascript:void(0);" id="cancle_button"><img src="${imageDomainUrl}/img/czcg_btn_3.jpg" width="115"
                                                               height="36"/></a>

    </div>
</div>

<div class="czcg" id="alert_open" style="display: none;">
    <div class="close"><a href="#" id="close_all">[关闭]</a></div>
    <div class="cz-contant">
        <h3>请您在新打开的支付页面上完成付款充值</h3>
        <h4><b>付款完成前请不要关闭或刷新此窗口。</b><br/>
            完成付款后请根据您的情况点击下面的按钮。</h4>


        <p style="margin: 40px 0 0 0;">
            <a id="go2success" href="#"><img src="${imageDomainUrl}/img/czcg_btn_1.jpg" width="115" height="36"/></a>&nbsp;&nbsp;
            <a id="go2fail" href="${payHelpUrl}"><img src="${imageDomainUrl}/img/czcg_btn_2.jpg" width="115"
                                                      height="36"/></a>&nbsp;&nbsp;<a href="#" id="cancle_all"><img
                src="${imageDomainUrl}/img/czcg_btn_3.jpg" width="115" height="36"/></a></p>
    </div>
</div>
</body>
<script type="text/javascript">
    var serverId = "${server}";
    var roleId = "${roleId}";
    var name = "${userName}";
    var server = "";
    var myUser = "";
    var userSign = 0;
    var userSign2 = 0;
    var role = 0;
    var showStr = "ACD";
    var order = "";
    var money = '${money}';
    var serverParam = '${server}';
    var roleIdParam = '${roleId}';
    $(function () {
        if (money == "vip") {
            showMyInputMoney();
            $("#money").val(10000);
            checkMoney();
        }
        if (serverParam && !isNaN(serverParam)) {
            var serverItem= $("#server_id").find("option[value='" + serverParam + "']");
            if(serverItem)
            {
                serverItem.attr("selected", "selected");
            }
            $('#userNameTip').empty().html("<span class='font_1'><span class='txt-succ'>该七道账号存在</span></span>");
            $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-succ'>确认账号输入正确</SPAN></span>");
        }

    });

    function checkedRaido(radioId) {
        $("#"+radioId).attr("checked","checked");
    }
    var gateWay = '${gatewayDomain}';
    $(".bankradio img").hover(function () {
        $(this).addClass("img_border");
    }, function () {
        $(this).removeClass("img_border");
    })
</script>
<!--<script type="text/javascript" charset="UTF-8" src="http://pay.7road.com/scripts/jquery.js"></script>
<script type="text/javascript" charset="UTF-8" src="神曲充值页面_files/online_game.js"></script>-->
</html>