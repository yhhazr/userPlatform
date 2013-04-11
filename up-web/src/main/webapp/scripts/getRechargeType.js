$(document).ready(function () {
//    alert($("#typeInfo").val());
    if ($("#result").val() == "fail") {
        $("#showResult").empty().html("<h3>您查询的账单不存在，该账单可能还在充值中，请稍后查询！</h3>");
        return;
    }
    if (result == "1") {
        $("#fail").css("display", "none");
        $("#showError").css("display", "none");
        $("#success").css("display", "block");
    } else {
        $("#fail").css("display", "block");
        $("#showError").css("display", "block");
        $("#success").css("display", "none");
    }
    var value = $("#typeInfo").val().split(",");
//    alert(value[0]+"*******"+value[1]);
    var data = {
        A0:{
            "":"支付宝余额充值"
        },
        A1:{
            BOCB2C:"中国银行(支付宝)充值",
            ICBCB2C:"中国工商银行(支付宝)充值",
            CMB:"招商银行(支付宝)充值",
            CCB:"中国建设银行(支付宝)充值",
            ABC:"中国农业银行(支付宝)充值",
            SPDB:"上海浦发银行(支付宝)充值",
            CIB:"兴业银行(支付宝)充值",
            GDB:"广东发展银行(支付宝)充值",
            SPABANK:"深圳发展银行(支付宝)充值",
            CMBC:"中国民生银行(支付宝)充值",
            COMM:"交通银行(支付宝)充值",
            CITIC:"中信银行(支付宝)充值",
            CEBBANK:"中国光大银行(支付宝)充值",
            POSTGC:"中国邮政(支付宝)充值"
        },
        A2:{

        },
        A3:{

        },
        B0:{
            BL:"财付通余额充值"
        },
        B1:
        {
            ICBC:"中国工商银行(财付通)充值",
            CCB:"中国建设银行(财付通)充值",
            ABC:"中国农业银行(财付通)充值",
            CMB:"中国招商银行(财付通)充值",
            SPDB:"上海浦发银行(财付通)充值",
            SDB:"深圳发展银行(财付通)充值",
            CIB:"兴业银行(财付通)充值",
            BOB:"北京银行(财付通)充值",
            CEB:"光大银行(财付通)充值",
            CMBC:"中国民生银行(财付通)充值",
            CITIC:"中信银行(财付通)充值",
            GDB:"广东发展银行(财付通)充值",
            PAB:"平安银行(财付通)充值",
            BOC:"中国银行(财付通)充值",
            COMM:"中国交通银行(财付通)充值",
            NJCB:"南京银行(财付通)充值",
            NBCB:"宁波银行(财付通)充值",
            SRCB:"上海农商银行(财付通)充值",
            POSTGC:"邮政储蓄银行(财付通)充值",
            BEA:"东亚银行(财付通)充值"
        },
        C1:{
            BOCB2C_NET_B2C:"中国银行(易宝)充值",
            ICBC_NET_B2C:"中国工商银行(易宝)充值",
            CMBCHINA_NET_B2C:"招商银行(易宝)充值",
            CCB_NET_B2C:"中国建设银行(易宝)充值",
            ABC_NET_B2C:"中国农业银行(易宝)充值",
            SPDB_NET_B2C:"上海浦发银行(易宝)充值",
            CIB_NET_B2C:"兴业银行(易宝)充值",
            GDB_NET_B2C:"广东发展银行(易宝)充值",
            SDB_NET_B2C:"深圳发展银行(易宝)充值",
            CMBC_NET_B2C:"中国民生银行(易宝)充值",
            BOCO_NET_B2C:"交通银行(易宝)充值",
            ECITIC_NET_B2C:"中信银行(易宝)充值",
            CEB_NET_B2C:"中国光大银行(易宝)充值",
            POST_NET_B2C:"中国邮政(易宝)充值"
        },
        C2:{

        },
        C3:{
            JUNNET_NET:"骏网一卡通(易宝)充值",
            UNICOM_NET:"联通充值卡(易宝)充值",
            SZX_NET:"神州行充值卡(易宝)充值",
            TELECOM_NET:"电信充值卡(易宝)充值"
        },
        C4:{
            ZHENGTU_NET:"征途游戏卡",
            WANMEI_NET:"完美一卡通",
            NETEASE_NET:"网易一卡通",
            ZONGYOU_NET:"纵游一卡通",
            TIANXIA_NET:"天下一卡通",
            SNDACARD_NET:"盛大一卡通(易宝)充值",
            SOHU_NET:"搜狐一卡通(易宝)充值",
            TIANHONG_NET:"天宏一卡通"

        },
        D1:{
            BOC:"中国银行(快钱)充值",
            ICBC:"中国工商银行(快钱)充值",
            CMB:"招商银行(快钱)充值",
            CCB:"中国建设银行(快钱)充值",
            ABC:"中国农业银行(快钱)充值",
            SPDB:"上海浦发银行(快钱)充值",
            CIB:"兴业银行(快钱)充值",
            GDB:"广东发展银行(快钱)充值",
            SDB:"深圳发展银行(快钱)充值",
            CMBC:"中国民生银行(快钱)充值",
            BCOM:"交通银行(快钱)充值",
            CITIC:"中信银行(快钱)充值",
            CEB:"中国光大银行(快钱)充值",
            PSBC:"中国邮政(快钱)充值"
        },
        D2:{

        },
        D3:{
			0:"神州行充值卡(快钱)充值",
			1:"联通充值卡(快钱)充值",
            3:"电信充值卡(快钱)充值",
			4:"骏网一卡通(快钱)充值",
			9:"充值卡(快钱)充值",
			10:"盛大一卡通(快钱)充值",
			11:"征途游戏卡(快钱)充值",
			12:"完美一卡通(快钱)充值",
			13:"搜狐一卡通(快钱)充值",
			14:"网易一卡通(快钱)充值",
			15:"纵游游戏卡(快钱)充值",
			16:"Q币卡(快钱)充值"
        },
        D4:{
            C:"盛大一卡通(快钱)充值",
            M:"网易一卡通",
            N:"搜狐一卡通(快钱)充值",
            D:"征途游戏卡",
            U:"完美一卡通"
        }
    }
    var str = "_";
    if (value[1].indexOf("-") != -1) {
        var sub = value[1].split("-");
        for (var i = 0; i < sub.length; i++) {
            str += sub[i] + "_";
        }
        str = str.substring(1, str.length - 1);
    } else {
        str = value[1];
    }
    //$("#pay_way").html(data[value[0]][value[1]]);
    $("#pay_way").html(data[value[0]][str]);

});

function showResult() {
    if ($("#result").val() == "fail") {
        $("#showResult").empty().html("<h3>您查询的账单不存在，该账单可能还在充值中，请稍后查询！</h3>");
    }
}