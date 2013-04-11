//*****************登陆加载****************
/*取的游戏的id,1：神曲，2：新弹，3：弹2*/
var game_id_type = getUrlParam("gameid"), pay_scale = 1;
if (game_id_type == 3) {
    pay_scale = 10;
}
$(document).ready(function () {
//初始值
    initHide();
    $.ajax({
        url:"getServerList",
        cache:false,
        type:"GET",
        async:false,
        data:{gameId:$("#gameId").val()},
        success:function (json) {
            //empty
            $("#server_id").css("backgroun-color", "");
            if (json == "nothing") {
                $("#server_id").html("<option value='0' style='color:red;'>--暂无可以充值的服务器--</option>");
                $("#server_id").css("background-color", "#FF7900");
            } else {
                $("#server_id").empty();
                $("#server_id").css("background-color", "");
                $("#server_id").html(json);
                if (!isNaN(serverId)) {
                    $("#server_id").find("option[value='" + serverId + "']").attr("selected", "selected");
                }
            }
        }
    });
    if (name) {
        $("#username").val(name);
        $("#username_1").val(name);
        if (serverId && !isNaN(serverId)) {
            getRoleList();
        }
    }
    var newUser = $("#username").val();
    if (newUser) {
        userSign = 1;
        userSign2 = 1;
    }

    //打开充值的类型，传入充值渠道的标识,showStr为包含A、B、C任意组合的全局变量，在网页中定义
    showPayWay(showStr);

    $("#refresh_actor_name").click(function () {
        var newServer = $("#server_id").val();
        if (newServer == 0) {
            alert("请您选择服务器");
            return;
        }
        if (userSign == 0 || userSign2 == 0) {
            return;
        }
        if (role == 0) {
//            $("#back").css("background-position","0px 0px");
//            $("#back").css("background-image","url(/img/role.gif)");
            getRoleList();
        }
    });
    //给充值按钮绑定事件
    $("#submit_button").bind("click", submitPay);

    $("#close").click(function () {
        $("#showCon").hide();
        $("#actor_name,#selectMoney,#server_id").show();
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("body").css("overflow", "visible");

    });

    $("#cancle_button").click(function () {
        $("#showCon").css("display", "none");
        $("#actor_name,#selectMoney,#server_id").show();
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });

    $("#cancle_all").click(function () {
        $("#alert_open").css("display", "none");
        $("#actor_name,#selectMoney,#server_id").show();
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });
    $("#close_all").click(function () {
        $("#alert_open").css("display", "none");
        $("#actor_name,#selectMoney,#server_id").show();
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });
    $(".moneyPannel input").click(function () {
        var rechargeMoney = $("input[name = 'rechargeMoney']:checked").val();
        $("#selectCoin").val(rechargeMoney * 9);
    });
    $("#server_id").change(function () {
        getFirstRole();
    });


    //*****************切换充值方式****************
    $("ul#payUl li a").click(function () {
        changePayWay(this.id);
        return false;
    });
    $("#MyInputMoney").click(function () {
        showMyInputMoney();
    });
    if (serverId && !isNaN(serverId)) {
        var serverItem = $("#server_id").find("option[value='" + serverId + "']");
        if (serverItem) {
            serverItem.attr("selected", "selected");
        }
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-succ'>该七道账号存在</span></span>");
        $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-succ'>确认账号输入正确</SPAN></span>");
    }


    if (game_id_type == 3) {
        $("#proportion").html("充值比例1:100");
    }

    changePayWay("aa1");
});

//*****************显示用户输入金额的文本框****************
function showMyInputMoney() {
    $("#selectPay").hide();
    $("#MyInputMoney").hide();
    $("#custom_charge").show();
    $("#moneyTip").show();
    $("#coin").val("");
}


//*****************获取角色****************
function getFirstRole() {
//    $("#server_id").removeClass("disableInputBg").addClass("normalInputBg");
//    if($("#server_id").find('option:selected').text().indexOf("正在维护") != -1){
//        $("#server_id").removeClass("normalInputBg").addClass("disableInputBg");
//    }


    var newServer = $("#server_id").val();
    if (newServer == 0) {
        server = newServer;
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        return false;
    }
    var newUser = $("#username").val();
    if (!newUser) {
        return false;
    }
    if (userSign == 0 || userSign2 == 0) {
        return false;
    }
    if (newUser != $("#username_1").val()) {
        return false;
    }
//    var length = $("#actor_name")[0].options.length; 
//    if(newUser == myUser){
//        if(length < 2){
//           getRoleList(); 
//        }
//    }
    if (newServer != server) {
        server = newServer;
        getRoleList();
    }
}

//获取角色
function getRoleList() {
    $("#actor_name").css("background-color", "");
    $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
    role = 1;
    var uname = $("#username").val();

    var server = $("#server_id").val();
    if (serverId && !isNaN(serverId)) {
        server = serverId;
    }
    $.ajax({
        url:"getRole2",
        cache:false,
        beforeSend:function () {
            //选择服务器时，让此下拉框暂时禁用，待响应结束后，再设置可用(防止多次请求)；
            $("#server_id").attr("disabled", "disabled");
            $("#actor_name").empty().html("<option value='-4'>正在加载角色名...</option>");
        },
        data:{username:uname, gameId:$("#gameId").val(), serverId:server},
        success:function (data) {
            $("#server_id").removeAttr("disabled");
            if (data == "-1") {
                $("#actor_name").css("background-color", "#FF7900");
                $("#actor_name").empty().html("<option value='-1'>--您在该服务器还未创建角色--</option>");
            } else if (data == "-3") {
                $("#actor_name").css("background-color", "#FF7900");
                $("#actor_name").empty().html("<option value='-3'>--系统繁忙获取角色失败--</option>");
            } else if (data == "-2") {
                $("#actor_name").css("background-color", "#FF7900");
                $("#actor_name").empty().html("<option value='-2'>--您所选的服务器正在维护--</option>");
            } else {
                $("#actor_name").css("background-color", "");
                $("#actor_name").empty();
                $("#actor_name").html(data);
            }
            role = 0;
            roleId = null;
            name = null;
            serverId = null;
        }

    });
}


//特殊字符
$("#username,#username_1").keyup(function () {
    var str = ['@', '#', '$', '%', '^', '&', '*', '<', '>', '/', '.'];
    for (var i = 0; i < str.length; i++) {
        if ($(this).val().indexOf(str[i]) >= 0) {
            alert("输入内容不能包含： '" + str[i] + "'  字符！");
            $(this).val($(this).val().replace(str[i], ""));
            return;
        }
    }
});


//*****************用户验证开始****************
//用户验证
function userNameOnBlur() {
    var nickname = document.getElementById("username");
    var username1 = document.getElementById("username_1");
    if (nickname.value == "" || nickname.value == null) {
        $('#userNameTip').empty().html("<span class='font_1'><span>请输入您的七道账号</span></span>");
        $('#userName1Tip').empty().html("<span class='font_1'><span>请再次输入您的七道账号</span></span>");
        $("#username_1").val("");
        username1.style.border = "1px solid #C1C1C1";
        username1.style.backgroundColor = "#fbfbfb";
        nickname.style.border = "1px solid #C1C1C1";
        nickname.style.backgroundColor = "#fbfbfb";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        userSign = 0;
        return false;
    }
    if (nickname.value.length > 20 || nickname.value.length < 6) {
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号长度应在6~20个字符</span></span>");
        nickname.style.backgroundColor = "#fbe2e2";
        nickname.style.borderColor = "#d28c8c";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        userSign = 0;
    } else {
        return $.ajax({
            async:false,
            url:"CheckAccount",
            type:"GET",
            data:{value:nickname.value, type:1},
            cache:false,
            success:function (data) {
                if (data == "success") {
                    $('#userNameTip').empty().html("<span class='font_1'><span class='txt-succ'>该七道账号存在</span></span>");
                    userSign = 1;
                    nickname.style.border = "1px solid #C1C1C1";
                    nickname.style.backgroundColor = "#fbfbfb";
                    if ($("#username_1").val() != "" || $("#username_1").val() != null) {
                        if (nickname.value != $("#username_1").val()) {
                            $("#actor_name").css("background-color", "");
                            $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
                        }
                    }
                    return true;
                } else {
                    $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号不存在，请确认后输入</span></span>");
                    nickname.style.backgroundColor = "#fbe2e2";
                    nickname.style.borderColor = "#d28c8c";
                    $("#actor_name").css("background-color", "");
                    $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
                    userSign = 0;
                }
            },
            error:function () {
                alert("用户验证有误！");
            }
        });
    }
    return false;
}

function userNameOnFocus() {
    var nickname = document.getElementById("username");
    nickname.className = "g-ipt-active";
}

function userName1OnFocus() {
    document.getElementById("username_1").className = "g-ipt-active";
}

function userName1OnBlur() {
    var username = document.getElementById("username");
    var username1 = document.getElementById("username_1");
    if (username.value == "" || username == null) {
        $('#userName1Tip').html("<span class='font_1'><SPAN>请再次输入您的七道账号</SPAN></span>");
        username1.style.border = "1px solid #C1C1C1";
        username1.style.backgroundColor = "#fbfbfb";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        userSign2 = 0;
        return false;
    }
    if (username1.value == "" || username1.value == null) {
        $('#userName1Tip').html("<span class='font_1'><SPAN>请再次输入您的七道账号</SPAN></span>");
        username1.style.border = "1px solid #C1C1C1";
        username1.style.backgroundColor = "#fbfbfb";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        userSign2 = 0;
        return false;
    }
    if (username1.value != username.value) {
        $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-err'>确认账号输入有误</SPAN></span>");
        username1.style.backgroundColor = "#fbe2e2";
        username1.style.borderColor = "#d28c8c";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        userSign2 = 0;
        return false;
    } else {
        $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-succ'>确认账号输入正确</SPAN></span>");
        username1.style.border = "1px solid #C1C1C1";
        username1.style.backgroundColor = "#fbfbfb";
        userSign2 = 1;
        getUserOnBlurRole();
        return true;
    }
}

function getUserOnBlurRole() {
    if (userSign == 0) {
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号不存在，请确认后输入</span></span>");
        return false;
    }
    var newServer = $("#server_id").val();
    if (newServer == 0) {
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请先选择服务器--</option>");
        return false;
    }
    $("#actor_name").css("background-color", "");
    var length = $("#actor_name")[0].options.length;
    var newUser = $("#username").val();
    if (newUser == myUser) {
        if (length >= 2) {
            return;
        }
    }
    if (!myUser) {
        if (length >= 2) {
            return;
        }
    }
    myUser = newUser;
    getRoleList();
}
//*****************用户验证结束****************


//*****************金额验证****************
function moneyOnFocus() {
    document.getElementById("moey").className = "g-ipt-active";
    $('#moneyTip').empty().html("<span class='font_1'>请输入你想充值的金额(范围1~50000的整数)</span>");
}

function moneyChange() {
    if (!document.getElementById("selectMoney").options[0].selected) {
        var value = $("#selectMoney").val();
        $("#money").val("");
        var obj = $(".active").children().html();
        if ($("#aa1").html() == obj) {
            obj = "网银充值";
        }
        getPayGameMoney(obj, value);
        return true;
    } else {
        return checkMoney();
    }
}

function checkMoney() {
    var obj = $(".active").children().html();
    if ($("#aa1").html() == obj) {
        obj = "网银充值";
    }
    document.getElementById("selectMoney").options[0].selected = "selected";
    var money = document.getElementById("money");
    if (money.value && money.value.length > 7) {
        $('#moneyTip').html("<span class='font_1'></span><SPAN class='txt-err'>请输小于或等于7位数的整数</SPAN></span>");
        money.style.backgroundColor = "#fbe2e2";
        money.style.borderColor = "#d28c8c";
        $("#coin").val("");
        return false;
    }
    var regx = new RegExp("^(([1-9][0-9]{1,}))$");
    if (!regx.test(money.value)) {
        $("#moneyTip").html("<span class='font_1'></span><SPAN class='txt-err'>请输入不小于10的整数</SPAN></span>");
        money.style.backgroundColor = "#fbe2e2";
        money.style.borderColor = "#d28c8c";

        if (!money.value) {
            $('#moneyTip').html("<span class='font_1'></span><SPAN>请输入您想充值的金额（不小于10的整数）</SPAN></span>");
            money.style.border = "1px solid #C1C1C1";
            money.style.backgroundColor = "#fbfbfb";
        }
        $("#coin").val("");
        return false;
    } else {
        $('#moneyTip').empty();
        getPayGameMoney(obj, money.value);
        money.style.border = "1px solid #C1C1C1";
        money.style.backgroundColor = "#fbfbfb";
        return true;
    }
}


function getMoney(num) {
    $("#coin").val($("#card_money").val() * $("#scale").text() * num);
}

//金额验证
function isDigital() {
    var money = document.getElementById("money");
    var value = $("#money").val();
    var len = $("#money").val().length;
    var regx = new RegExp("^([0-9])$");
    if (value != null || value != "") {
        if (len == 1) {
            var reg = new RegExp("^([1-9])$");
            if (!reg.test(value)) {
                $("#money").val("");
            }
        } else if (!regx.test(value.substring(len - 1))) {
            $("#money").val(value.substring(0, len));
        }
    } else {
        $("#money").val("");
    }
}

//检测角色
function selectRole() {
    var role = $("#actor_name");
    if (!role.val() || role.val() == "-1" || role.val() == "0" || role.val() == "-2" || role.val() == "-3") {
        return false;
    }
    else {
        $("#gameRoleTip").empty();
        return true;
    }
}


//*****************信息确认框中信息****************
function confirmDl() {
    var rechageMoney = "";
    if ($("#selectMoney").val() == 0) {
        rechageMoney = $("#money").val();
    } else {
        rechageMoney = $("#selectMoney").val();
    }

    var coin = $("#coin").val();
    var payWay = $(".active").children().html();
    if (payWay == "其他卡类支付") {
        var card = $("input[name = 'card']:checked").val();
        payWay = getCard(card);
    }
    if (!$.trim($("#username").val())) {
        alert('请填写用户名！');
        return false;
    }
    if (!$.trim($("#username_1").val())) {
        alert('请填写确认用户名！');
        return false;
    }

    if (userSign == 0 || userSign2 == 0) {
        alert("用户名不存在！！！");
        return false;
    }
    if (document.getElementById("server_id").options[0].selected) {
        alert("您还未选择服务器！！");
        return false;
    }
    if (!selectRole()) {
        if ($("#actor_name").val() == "0") {
            var alertText = "请选择服务器！";
        }
        if ($("#actor_name").val() == "-1") {
            var alertText = "您在该服务区未创建角色！";
        }
        if ($("#actor_name").val() == "-3") {
            var alertText = "抱歉，系统繁忙，请稍后重试!！";
        }
        if ($("#actor_name").val() == "-2") {
            var alertText = "你所选的服务器正在维护，充值入口已经关闭!";
        }
        alert(alertText);
        return false;
    }

    if (!moneyChange()) {
        alert("您输入的金额不正确！！！");
        return false;
    }
    if (Number($("#actor_name").val()) <= 0) {//成功获取到角色
        alert("请等待角色名加载完成再提交");
        return false;
    }

    /*采用局部变量取游戏的id,1：神曲，2：新弹，3：弹2*/
    var game_id_type = getUrlParam("gameid"), pay_unit = "钻石";
    if (game_id_type == 3 || game_id_type == 2) {
        pay_unit = "点卷";
    }
    $("#pay_card_id").html(payWay);
    $("#char_ser_id").html($("#server_id").find('option:selected').text());
    $("#char_amount").html(rechageMoney + "元");
    $("#char_game_amount").html(coin + pay_unit);
    $("#char_user").html($("#username").val());
    return true;
}

//*****************获取其他卡类型充值的充值卡名****************
function getCard(obj) {
    var data = {
        "D":"征途游戏卡",
        "M":"网易一卡通",
        "U":"完美一卡通",
        "TIANXIA-NET":"天下一卡通",
        "ZONGYOU-NET":"纵游一卡通",
        "TIANHONG-NET":"天宏一卡通"
    };
    return data[obj];
}


//*****************弹出层显示充值填写信息****************
function submitConfirm() {
    if (!confirmDl()) {
        return;
    }

    $("#showCon").show();
    $("#actor_name,#selectMoney,#server_id").hide();
    mybg = document.createElement("div");
    mybg.setAttribute("id", "mybg");
    mybg.style.background = "#f5f5f5";
    mybg.style.width = (document.body.clientWidth - document.body.scrollLeft) + "px";
    mybg.style.height = (document.body.clientHeight - document.body.scrollTop) + "px";
    mybg.style.position = "absolute";
    mybg.style.top = "0";
    mybg.style.left = "0";
    mybg.style.zIndex = "1001";
    mybg.style.opacity = "0.20";
    mybg.style.filter = "Alpha(opacity=80)";
    document.body.appendChild(mybg);

    //autoScrollResize();
}

//*****************提交表单****************
function okbutton() {
    document.getElementById("showCon").style.display = "none";
    $("#alert_open").show();
    // var open = document.getElementById("alert_open");
    //open.style.display = "block";
    //open.style.position = "absolute";
    //open.style.top = "40%";
    // open.style.left = "45%";
    //open.style.marginTop = "-75px";
    //open.style.marginLeft = "-150px";
    //open.style.zIndex = "1002";
    if ($("#sign").val() != order) {
        order = $("#sign").val();
        $("#accountForm").submit();
    } else {
        alert("信息已提交，请您不要重复提交信息！");
    }
}

//*****************设置弹出层的显示相关参数****************
function autoScrollResize() {
    var content_h = document.body.clientHeight; // 内容高度
    var content_w = document.body.clientWidth;
    var broswer_h = document.documentElement.clientHeight; // 浏览器窗口的可视高度
    var broswer_w = document.documentElement.clientWidth;
    document.getElementsByTagName('body')[0].style.overflow = 'hidden';
    document.getElementsByTagName('html')[0].style.overflow = 'hidden'; // 在DTD标准下，为html元素设置overflow:hidden才能去掉滚动条
}

//*****************提交表单同时生成账单****************
function submitPay() {
    var _t = Date.parse(new Date());
    getParameters();
    var channel = $("#channel").val();
    $.ajax({
        url:"getUuid",
        type:"GET",
        data:{channel:channel, type:"UUID", _t:_t},
        success:function (json) {
            $("#sign").val(json);
            $("#go2success").attr("href", "result.html?_n=" + json);
            submitConfirm();
        }
    });

}


//*****************获取表单提交的参数****************
function getParameters() {
    var text = $(".active").children().html();
    setParameter(text);
}

//*****************设置表单参数****************
function setParameter(obj) {
    if ($("#aa1").html() == obj) {
        obj = "网银充值";
    }

    var money = "";
    if ($("#selectMoney").val() == "0") {
        money = $("#money").val();
    } else {
        money = $("#selectMoney").val();
    }
    var data = {
        "网银充值":{
            channel:$("input[name = 'pay']:checked").val(),
            fName:setBankName($("input[name = 'pay']:checked").val()),
            s_id:"1",
            amount:money
        },
        "财付通充值":{
            channel:"B",
            fName:setBankName("B"),
            s_id:"1",
            amount:money
        },

        "支付宝余额充值":{
            channel:"A",
            fName:"",
            s_id:"0",
            amount:money
        },
        "神州行卡充值":{
            channel:getSTChannel(),
            fName:getSZXfname(),
            s_id:"3",
            amount:money
        },
        "联通充值卡充值":{
            channel:getLTChannel(),
            fName:getLTfname(),
            s_id:"3",
            amount:money
        },
        "电信充值卡充值":{
            channel:getSTChannel(),
            fName:getTELfname(),
            s_id:"3",
            amount:money
        },
        "盛大一卡通":{
            channel:$("input[name = 'pay']:checked").val(),
            fName:setCardFname($("input[name = 'pay']:checked").val(), "盛大一卡通"),
            s_id:getSid(),
            amount:money
        },
        "骏网一卡通":{
            channel:$("input[name = 'pay']:checked").val(),
            fName:setCardFname($("input[name = 'pay']:checked").val(), "骏网一卡通"),
            s_id:"3",
            amount:money
        },
        "搜狐一卡通":{
            channel:$("input[name = 'pay']:checked").val(),
            fName:setCardFname($("input[name = 'pay']:checked").val(), "搜狐一卡通"),
            s_id:getSid(),
            amount:money
        },
        "其他卡类支付":{
            channel:setOtherChannel($("input[name = 'card']:checked").val()),
            fName:getOtherFname($("input[name = 'card']:checked").val()),
            s_id:getSid(),
            amount:money
        }
    };
    var channelName = data[obj]["channel"];
    var bankName = setBankName("B")

    if (channelName == 'B' && (bankName == "ICBCB2B" || bankName == "CMBB2B" || bankName == "CCBB2B" || bankName == "ABCB2B" || bankName == "SPDBB2B")) {
        $("#s_id").val("2");
    }
    else if (channelName == 'B' && bankName == "BL") {
        $("#s_id").val("0");
    }
    else {
        $("#s_id").val(data[obj]["s_id"]);
    }
    $("#channel").val(data[obj]["channel"]);
    var $bankName = data[obj]["fName"];
    if (channelName == 'A' && $bankName == "SDB") {
        $bankName = "SPABANK";
    }
    $("#f_id").val($bankName);
    $("#user").val($("#username").val());
    $("#server").val($("#server_id").val());
    $("#role").val($("#actor_name").val());
    $("#amount").val(data[obj]["amount"]);
}

//*****************获取联通行充值卡和电信充值卡的渠道****************
function getLTChannel() {
    var channel = $("input[name = 'pay']:checked").val();;
    /*if (showStr.indexOf("D") != -1) {
        channel = "D";
    } else {
        channel = "C";
    }*/
    return channel;
}

//*****************获取联通充值卡的_f参数****************
function getLTfname() {
    var data = $("input[name=pay]:checked").val();
    if (data == 'D') {
        return "1";
    }
    if (data == 'C') {
        return "UNICOM-NET";
    }
    return "1";
    /*if (showStr.indexOf("D") != -1) {
        data = "1";
    } else {
        data = "SZX-NET";
    }
    return data;*/
}

//*****************获取神州行充值卡和电信充值卡的渠道****************
function getSTChannel() {
    var channel = $("input[name = 'pay']:checked").val();
    /*if (showStr.indexOf("D") != -1) {
        channel = "D";
    } else {
        channel = "C";
    }*/
    return channel;
}

//*****************获取神州行充值卡的_f参数****************
function getSZXfname() {
    var data = $("input[name=pay]:checked").val();
    if (data == 'D') {
        return "0";
    }
    if (data == 'C') {
        return "SZX-NET";
    }
    return "0";
    /*if (showStr.indexOf("D") != -1) {
        data = "0";
    } else {
        data = "SZX-NET";
    }
    return data;*/
}

//*****************获取电信充值卡的_f参数****************
function getTELfname() {
    var data = $("input[name=pay]:checked").val();
    if (data == 'D') {
        return "3";
    }
    if (data == 'C') {
        return "TELECOM-NET";
    }
    return "3";
    /*if (showStr.indexOf("D") != -1) {
        data = "3";
    } else {
        data = "TELECOM-NET";
    }
    return data;*/
}

//****************获取其他卡类充值的_f参数****************
function getOtherFname(obj) {
    //var fName;
    //if (showStr.indexOf("D") != -1) {
    var data = $("input[name=pay]:checked").val();
    if (data == 'D') {
        var data = {
            "U":"12",
            "M":"14",
            "D":"11",
            "TIANXIA-NET":"TIANXIA-NET",
            "ZONGYOU-NET":"15",
            "TIANHONG-NET":"TIANHONG-NET"
        }
        fName = data[obj];
    } else {
        var data = {
            "U":"WANMEI-NET",
            "M":"NETEASE-NET",
            "D":"ZHENGTU-NET",
            "TIANXIA-NET":"TIANXIA-NET",
            "ZONGYOU-NET":"ZONGYOU-NET",
            "TIANHONG-NET":"TIANHONG-NET"
        }
        fName = data[obj];
    }
    return fName;
}

//****************获取其他卡类充值的充值渠道****************
function setOtherChannel(obj) {
    var channel = $("input[name = 'pay']:checked").val();
    var data;
    if (channel == "D") {
        data = {
            "U":"D",
            "M":"D",
            "D":"D",
            "ZONGYOU-NET":"D"
        }
    } else {
        data = {
            "U":"C",
            "M":"C",
            "D":"C",
            "TIANXIA-NET":"C",
            "ZONGYOU-NET":"C",
            "TIANHONG-NET":"C"
        }
    }
    return data[obj];
}

//*****************获取充值卡充值的_f参数的值****************
function setCardFname(cardName, cardTag) {
    data = {
        C:{
            "盛大一卡通":"SNDACARD-NET",
            "骏网一卡通":"JUNNET-NET",
            "搜狐一卡通":"SOHU-NET"
        },
        D:{
            "神州行充值卡":"0",
            "联通充值卡":"1",
            "电信充值卡":"3",
            "骏网一卡通":"4",
            "任一卡类型":"9",
            "盛大一卡通":"10",
            "征途一卡通":"11",
            "完美一卡通":"12",
            "搜狐一卡通":"13",
            "网易一卡通":"14",
            "纵游一卡通":"15",
            "Q币卡":"16"
        }
    }
    if (data[cardName] == null) {
        return;
    }
    return data[cardName][cardTag];
}

function getSid() {
    var sid = "4";
    var data = $("input[name=pay]:checked").val();
    if (data == 'D') {
        sid = "3";
    }
    return sid;
    /*if (showStr.indexOf("D") != -1) {
        sid = "3";
    }
    return sid;*/
}


function initHide() {
    $("#paytype").hide();
    $(".descripe:not(#unionpay)").hide();
}

//*****************切换充值方式****************
function changePayWay(obj) {
    var mappingfortab = {"aa1":"#unionpay", "aa2":"#tenpay", "aa3":"#alipay", "aa4":"#shenzhou", "aa5":"#unioncom", "aa6":"#chinanet", "aa7":"#shengda", "aa8":"#junwang", "aa9":"#sohupay", "aa10":"#otherpay"};
    var descripeid = mappingfortab[obj];
    $("#select_bank").hide();
    $("#select_bankOfTen").hide();
    $("#paytype").hide();
    //$("#quick,#applay,#yibao").hide();
    $("#quick,#applay").hide();
    $("#payway").hide();
    $("#MyInputMoney").hide();
    $("#moneyTip").hide();
    if (obj === "aa1") {
        $("#MyInputMoney").show();
        if (showStr.indexOf("A") != -1) {
            $("#applay").show();
            $("#applay input[type='radio']").attr("checked", "checked");
        }
        if (showStr.indexOf("D") != -1) {
            //$("#quick").show();
            if (showStr.indexOf("A") == -1) {
                $("#quick input[type='radio']").attr("checked", "checked");
            }
        }
        if (showStr.indexOf("C") != -1) {
            $("#yibao").show();
            if (showStr.indexOf("D") == -1 && showStr.indexOf("A") == -1) {
                $("#yibao input[type='radio']").attr("checked", "checked");
            }
        }
        //$("#quick").show();
        $("#select_bank").show();
        $("#payway").show();
        //$("#yibao").hide();
    } else if (obj === "aa2") {
        $("#MyInputMoney").show();
        $("#select_bankOfTen").show();
        $("#BL_ten").attr("checked", "checked");
    } else {
        $("#money").val("");
        $("#selectPay").show();
        $("#MyInputMoney").hide();
        $("#custom_charge").hide();
        $("#MyInputMoney").hide();
    }
    if (obj === "aa7" || obj === "aa8" || obj === "aa9" || obj === "aa4" || obj === "aa5" || obj === "aa6" || obj === "aa10") {
        if (showStr.indexOf("D") != -1) {
            //$("#quick").show();
            //$("#quick input[type='radio']").attr("checked", "checked");
            $("#yibao").show();
            $("#yibao input[type='radio']").attr("checked", "checked");
        }
        if (showStr.indexOf("C") != -1) {
            $("#yibao").show();
            if (showStr.indexOf("D") == -1) {
                $("#yibao input[type='radio']").attr("checked", "checked");
            }
        }
        $("#payway").show();
    }
    if (obj === "aa10") {
        $("#paytype").show();
        //$("#payway").hide();
    }
    $(descripeid).show();
    $(".descripe:not(" + descripeid + ")").hide();
    $("#" + obj).parent().addClass("active");
    $("#" + obj).parent().siblings().removeClass("active");

    var payWay = $("#" + obj).html();
//        $("#pay_way_CN").html(payWay);
    updateSelect(payWay);
    getProportion(payWay);
    moneyChange();
}

//*****************设置充值方式的可选择的充值金额****************
function updateSelect(obj) {
    if ($("#aa1").html() == obj) {
        obj = "网银充值";
    }
    var data = {
        "网银充值":"5000,2000,1000,800,500,300,200,100,50,20,10",
        "财付通充值":"5000,2000,1000,800,500,300,200,100,50,20,10",
        "支付宝余额充值":"5000,2000,1000,800,500,300,200,100,50,20,10",
        "神州行卡充值":"1000,500,300,200,100,50,30,20,10",
        "联通充值卡充值":"500,300,100,50,30,20",
        "电信充值卡充值":"100,50",
        "盛大一卡通":"1000,350,300,100,50,45,35,30,25,15,10",
        "骏网一卡通":"100,50,30,20,10",
        "搜狐一卡通":"100,40,30,15,10",
        "其他卡类支付":"500,468,300,250,208,180,120,100,68,60,50,30,25,20,18,15,10"
    }

    if (showStr.indexOf("D") != -1) {
        data = {
            "网银充值":"5000,2000,1000,800,500,300,200,100,50,20,10",
            "财付通充值":"5000,2000,1000,800,500,300,200,100,50,20,10",
            "支付宝余额充值":"5000,2000,1000,800,500,300,200,100,50,20,10",
            "神州行卡充值":"500,300,200,100,50,30,20,10",
            "联通充值卡充值":"500,300,100,50,30,20",
            "电信充值卡充值":"100,50",
            "盛大一卡通":"500,350,300,100,50,45,35,30,25,15,10",
            "骏网一卡通":"100,50,30,20,10",
            "搜狐一卡通":"100,40,30,15,10",
            "其他卡类支付":"500,468,300,250,208,180,120,100,68,60,50,30,25,20,18,15,10"
        }
    }

    $("#selectMoney").find("option").remove();
    var str = data[obj];
    var value = str.split(",");
    var option = "";
    $("#selectMoney").append("<option value='0'>请选择充值金额</option>")
    for (var i = 0; i < value.length; i++) {
        option = "<option value='" + value[i] + "'>" + value[i] + "元</option>";
        $("#selectMoney").append(option);
    }
}

//*****************设置选择的充值方式的名字****************
function getPayWayCN() {
    var text = $(".active").children().html();
//    $("#pay_way_CN").html(text);
//     var ele = document.getElementById("payUl").getElementsByTagName("li");
//     for(var i=0;i<ele.length;i++){  
//         if(ele[i].className=="active"){  
//             var text = ele[i].getElementsByTagName("a")[0].innerHTML;
//             $("#pay_way_CN").html(text);
//         }
//     }
}

//*****************设置充值比例****************

function getProportion(text) {
    if ($("#aa1").html() == text) {
        text = "网银充值";
    }

    var data = {
        "网银充值":"10",
        "财付通充值":"10",
        "支付宝余额充值":"10",
        "神州行卡充值":"10",
        "联通充值卡充值":"10",
        "电信充值卡充值":"10",
        "盛大一卡通":"9",
        "骏网一卡通":"9",
        "搜狐一卡通":"9",
        "其他卡类支付":"9"
    }
    var showText = "充值比例1:" + data[text] * pay_scale;
    $("#proportion").html(showText);
}

//*****************获取各种充值方式充值金额所对应的游戏币****************
function getPayGameMoney(obj, value) {
    if ($("#aa1").html() == obj) {
        obj = "网银充值";
    }

    var data = {
        "网银充值":"10",
        "财付通充值":"10",
        "支付宝余额充值":"10",
        "神州行卡充值":"10",
        "联通充值卡充值":"10",
        "电信充值卡充值":"10",
        "盛大一卡通":"9",
        "骏网一卡通":"9",
        "搜狐一卡通":"9",
        "其他卡类支付":"9"
    }

    $("#coin").val(value * data[obj] * pay_scale);
}

//*****************设置各种充值渠道的银行的_f参数****************
function setBankName(channel) {
    var fName = $("input[name='_f1']:checked").val();
    var name = "";

    if (channel == "A") {
        name = fName;
    }
    if (channel == "B") {
        name = $("input[name='_ften']:checked").val();
    }
    if (channel == "C") {
        name = fName + "-NET-B2C";
        if (fName == "CMB") {
            name = "CMBCHINA-NET-B2C";
        }
        if (fName == "COMM") {
            name = "BOCO-NET-B2C";
        }
        if (fName == "CITIC") {
            name = "ECITIC-NET-B2C";
        }
        if (fName == "ICBCB2C") {
            name = "ICBC-NET-B2C";
        }
        if (fName == "CEBBANK") {
            name = "CEB-NET-B2C";
        }
        if (fName == "POSTGC") {
            name = "POST-NET-B2C";
        }
    }
    if (channel == "D") {
        name = fName;
        if (fName == "ICBCB2C") {
            name = "ICBC";
        }
        if (fName == "BOCB2C") {
            name = "BOC";
        }
        if (fName == "CEBBANK") {
            name = "CEB";
        }
        if (fName == "POSTGC") {
            name = "PSBC";
        }
        if (fName == "COMM") {
            name = "BCOM";
        }
    }
    return name;
}

//*****************重置****************
function payFormReset() {
    $("#accountForm")[0].reset();
    $("#form")[0].reset();
    $("#server_id").removeAttr("disabled");
//    $("#server_id").removeClass("disableInputBg").addClass("normalInputBg");
    if ($("#ICBC").length > 0) {
        $("#ICBC").attr("checked", "checked");
    }
    if ($("#applay").length > 0) {
        $("#applay").attr("checked", "checked");
    }
    $("#actor_name").find("option").remove();
    $("#actor_name").css("background-color", "");
    $("#actor_name").html("<option value='0'>--请先选择服务器--</option>");
    $("input[name=rechargeMoney]:eq(0)").attr("checked", 'checked');
    $("input[name=card]:eq(0)").attr("checked", 'checked');

    if (showStr.indexOf("A") != -1) {
        if ($("input[name=pay]:eq(0)").is(":visible") == false) {
            if (showStr.indexOf("D") != -1) {
                $("input[name=pay]:eq(1)").attr("checked", 'checked');
            } else {
                $("input[name=pay]:eq(2)").attr("checked", 'checked');
            }
        } else {
            $("input[name=pay]:eq(0)").attr("checked", 'checked');
        }
    } else if (showStr.indexOf("D") != -1) {
        $("input[name=pay]:eq(1)").attr("checked", 'checked');
    } else {
        $("input[name=pay]:eq(2)").attr("checked", 'checked');
    }

    $("#selectCoin").val(90);
    $("#userNameTip").empty().html("<span class='font_1'><span>请输入您的七道账号</span></span>");
    ;
    $("#userName1Tip").empty().html("<span class='font_1'><span>请再次输入您的七道账号</span></span>");
    ;
}

//*****************显示充值的类型****************
function showPayWay(str) {
    //str必须是 str="A,C,D"字符串;
    var a = str.indexOf("A");
    var c = str.indexOf("C");
    var d = str.indexOf("D");

    if (a == -1) {
        $("#applay").hide();
        $("#aa3").hide();
        if (showStr.indexOf("D") == -1) {
            $("#quick").hide();
            $("input[name=pay]:eq(2)").attr("checked", "checked");
        } else {
            $("input[name=pay]:eq(1)").attr("checked", 'checked');
        }
    } else if (c == -1) {
        //$("#yibao").hide();
        //$("#aa5").hide();
        if (showStr.indexOf("A") == -1) {
            $("#applay").hide();
            $("input[name=pay]:eq(1)").attr("checked", "checked");
        } else {
            $("input[name=pay]:eq(0)").attr("checked", 'checked');
        }

    } else if (d == -1) {
        $("#quick").hide();
        if (showStr.indexOf("A") == -1) {
            $("#applay").hide();
            $("input[name=pay]:eq(2)").attr("checked", "checked");
        } else {
            $("input[name=pay]:eq(0)").attr("checked", 'checked');
        }
    }
    if (d != -1) {
        $("#otherPayWay").hide();
    }
    $("#quick").show();
    //$("#yibao").hide();
}


/**********取url参数*************/
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}


