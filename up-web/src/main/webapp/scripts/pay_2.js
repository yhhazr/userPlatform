//*****************登陆加载****************
$(document).ready(function () {
//初始值
    initHide();
    //获取服务器列表
    getServerList();
    if (NAME) {
        $("#username").val(NAME);
        $("#username_1").val(NAME);
        if (SERVERID && !isNaN(SERVERID)) {
            getRoleList();
        }
    }
    var newUser = $("#username").val();
    if (newUser) {
        USERSIGN = 1;
        USERSIGN2 = 1;
    }

//*****************开始注册事件****************
    //信息提示框关闭事件
    $("#close").click(function () {
        $("#showCon").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("body").css("overflow", "visible");
    });
    //信息提示框取消按钮事件
    $("#cancle_button").click(function () {
        $("#showCon").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });
    //充值完成信息查询框取消按钮事件
    $("#cancle_all").click(function () {
        $("#alert_open").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });
    //充值完成信息查询框关闭按钮事件
    $("#close_all").click(function () {
        $("#alert_open").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });
    //自定义金额按钮事件
    $(".moneyPannel input").click(function () {
        var rechargeMoney = $("input[name = 'rechargeMoney']:checked").val();
        $("#selectCoin").val(rechargeMoney * 9);
    });
    $("#MyInputMoney").click(function () {
        showMyInputMoney();
    });
    //服务器改变事件：获取角色
    $("#server_id").change(function () {
        getFirstRole();
    });

    //切换充值方式
    $("ul#payUl li a").click(function () {
        changePayWay(this.id);
        return false;
    });
    //*****************注册事件结束****************

});

//*****************获取服务器列表****************
function getServerList() {
    $.ajax({
        url:"getServerList",
        cache:false,
        type:"GET",
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
                if (!isNaN(SERVERID)) {
                    $("#server_id").find("option[value='" + SERVERID + "']").attr("selected", "selected");
                }
            }
        }
    });
}

//*****************重新获取角色,暂时以关闭****************
function getRefreshRole() {
    $("#refresh_actor_name").click(function () {
        var newServer = $("#server_id").val();
        if (newServer == 0) {
            alert("请您选择服务器");
            return;
        }
        if (USERSIGN == 0 || USERSIGN2 == 0) {
            return;
        }
        if (ROLE == 0) {
            getRoleList();
        }
    });
}

//*****************显示用户输入金额的文本框****************
function showMyInputMoney() {
    $("#selectPay").hide();
    $("#MyInputMoney").hide();
    $("#custom_charge").show();
    $("#moneyTip").show();
    $("#coin").val("");
}

//*****************通过游戏充值接口充值页面加载获取角色****************
function getFirstRole() {
    var newServer = $("#server_id").val();
    if (newServer == 0) {
        SERVER = newServer;
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        return false;
    }
    var newUser = $("#username").val();
    if (!newUser) {
        return false;
    }
    if (USERSIGN == 0 || USERSIGN2 == 0) {
        return false;
    }
    if (newUser != $("#username_1").val()) {
        return false;
    }
    if (newServer != SERVER) {
        SERVER = newServer;
        getRoleList();
    }
}

//*****************一般渠道获取角色****************
function getRoleList() {
    $("#actor_name").css("background-color", "");
    $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
    ROLE = 1;
    var uname = $("#username").val();

    var server = $("#server_id").val();
    if (SERVERID && !isNaN(SERVERID)) {
        server = SERVERID;
    }

    $.ajax({
        url:"getRole",
        cache:false,
        data:{username:uname, gameId:$("#gameId").val(), serverId:server},
        success:function (data) {
            if (data == "-1") {
                $("#actor_name").css("background-color", "#FF7900");
                $("#actor_name").html("<option value='0'>--您在该服务器还未创建角色--</option>");
            } else if (data == "-3") {
                alert("系统繁忙，获取角色失败！");
            } else if (data == "-2") {
                alert("您所选的服务器正在维护！！");
            } else {
                $("#actor_name").css("background-color", "");
                $("#actor_name").empty();
                $("#actor_name").html(data);
                if (ROLEID && !isNaN(ROLEID)) {
                    $("#actor_name").find("option[value='" + ROLEID + "']").attr("selected", "selected");
                }
            }
            ROLE = 0;
            ROLEID = null;
            NAME = null;
            SERVERID = null;
        },
        error:function () {
            $("#actor_name").css("background-color", "");
            $("#actor_name").html("<option value='0'>--请选择角色--</option>");
            alert("系统繁忙，请稍后查询！");
            ROLE = 0;
            ROLEID = null;
            NAME = null;
            SERVERID = null;
        }

    });
}

//*****************用户验证开始****************
//用户验证
function userNameOnBlur() {
    var nickname = $("#username");
    var username1 = $("#username_1");
    if (nickname.val() == "" || nickname.val() == null) {
        $('#userNameTip').empty().html("<span class='font_1'><span>请输入您的七道账号</span></span>");
        $('#userName1Tip').empty().html("<span class='font_1'><span>请再次输入您的七道账号</span></span>");
        $("#username_1").val("");
        username1.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
        nickname.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        USERSIGN = 0;
        return;
    }
    if (nickname.val().length > 50 || nickname.val().length < 6) {
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号长度应在6~50个字符</span></span>");
        nickname.css({borderColor:"#d28c8c", backgroundColor:"#fbe2e2"});
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        USERSIGN = 0;
    } else {
        return $.ajax({
            async:false,
            url:"CheckAccount",
            type:"GET",
            data:{value:nickname.val(), type:1},
            cache:false,
            success:function (data) {
                if (data == "success") {
                    $('#userNameTip').empty().html("<span class='font_1'><span class='txt-succ'>该七道账号存在</span></span>");
                    nickname.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
                    if ($("#username_1").val() != "" || $("#username_1").val() != null) {
                        if (nickname.val() != $("#username_1").val()) {
                            $("#actor_name").css("background-color", "");
                            $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
                        }
                    }
                    USERSIGN = 1;
                } else {
                    $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号不存在，请确认后输入</span></span>");
                    nickname.css({borderColor:"#d28c8c", backgroundColor:"#fbe2e2"});
                    $("#actor_name").css("background-color", "");
                    $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
                    USERSIGN = 0;
                }
            },
            error:function () {
                alert("用户验证有误！");
            }
        });
    }
}

function userNameOnFocus() {
    $("#username").addClass("g-ipt-active");
}

function userName1OnFocus() {
    $("#username_1").addClass("g-ipt-active");
}

function userName1OnBlur() {
    var username = $("#username");
    var username1 = $("#username_1");
    if (username.val() == "" || username.val() == null) {
        $('#userName1Tip').html("<span class='font_1'><SPAN>请再次输入您的七道账号</SPAN></span>");
        username1.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        USERSIGN2 = 0;
        return;
    }
    if (username1.val() == "" || username1.val() == null) {
        $('#userName1Tip').html("<span class='font_1'><SPAN>请再次输入您的七道账号</SPAN></span>");
        username1.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        USERSIGN2 = 0;
        return;
    }
    if (username1.val() != username.val()) {
        $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-err'>确认账号输入有误</SPAN></span>");
        username1.css({borderColor:"#d28c8c", backgroundColor:"#fbe2e2"});
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        USERSIGN2 = 0;
    } else {
        $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-succ'>确认账号输入正确</SPAN></span>");
        username1.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
        USERSIGN2 = 1;
        getUserOnBlurRole();
    }
}

function getUserOnBlurRole() {
    if (USERSIGN == 0) {
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号不存在，请确认后输入</span></span>");
        return;
    }
    var newServer = $("#server_id").val();
    if (newServer == 0) {
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        return;
    }
    $("#actor_name").css("background-color", "");
    var length = $("#actor_name")[0].options.length;
    var newUser = $("#username").val();
    if (newUser == MYUSER) {
        if (length >= 2) {
            return;
        }
    }
    if (!MYUSER) {
        if (length >= 2) {
            return;
        }
    }
    MYUSER = newUser;
    getRoleList();
}
//*****************用户验证结束****************


//*****************金额验证****************
function moneyOnFocus() {
    $("#moey").addClass("g-ipt-active");
    $('#moneyTip').empty().html("<span class='font_1'>请输入你想充值的金额(范围1~50000的整数)</span>");
}

function moneyChange() {
    if ($("#selectMoney").val() != 0) {
        var value = $("#selectMoney").val();
        $("#money").val("");
        var obj = $(".active").children().html();
        getPayGameMoney(obj, value);
        return true;
    } else {
        return checkMoney();
    }
}

function checkMoney() {
    var obj = $(".active").children().html();
    $("#selectMoney").find("option[value='0']").attr("selected", "selected");
    var money = $("#money");
    if(money.val() && money.val().length > 10){
        $('#moneyTip').html("<span class='font_1'></span><SPAN class='txt-err'>请输小于或等于10位数的整数</SPAN></span>");
        money.css({backgroundColor:"#fbe2e2", borderColor:"#d28c8c"});
        $("#coin").val("");
        return false;
    }
    var regx = new RegExp("^(([1-9][0-9]{1,}))$");
    if (!regx.test(money.val())) {
        $("#moneyTip").html("<span class='font_1'></span><SPAN class='txt-err'>请输入不小于10的整数</SPAN></span>");
        money.css({backgroundColor:"#fbe2e2", borderColor:"#d28c8c"});

        if (!money.val()) {
            $('#moneyTip').html("<span class='font_1'></span><SPAN>请输入您想充值的金额（不小于10的整数）</SPAN></span>");
            money.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
        }
        $("#coin").val("");
        return false;
    } else {
        $('#moneyTip').empty();
        getPayGameMoney(obj, money.val());
        money.css({border:"1px solid #C1C1C1", backgroundColor:"#fbfbfb"});
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
//*****************金额验证结束****************


//检测角色
function selectRole() {
    var role = $("#actor_name");
    if (!role.val() || role.val() == "0") {
        return false;
    } else {
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
    if (USERSIGN == 0 || USERSIGN2 == 0) {
        alert("用户名输入不正确或不存在！！！");
        return false;
    }
    if (!selectRole()) {
        alert("您还未选择角色！！！");
        return false;
    }
    if (document.getElementById("server_id").options[0].selected) {
        alert("您还未选择服务器！！");
        return false;
    }
    if (!moneyChange()) {
        alert("您输入的金额不正确！！！");
        return false;
    }
    $("#pay_card_id").html(payWay);
    $("#char_ser_id").html($("#server_id").find('option:selected').text());
    $("#char_amount").html(rechageMoney + "元");
    $("#char_game_amount").html(coin + "钻石");
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

    $("#showCon").css({
        display:"block",
        position:"absolute",
        top:"50%",
        left:"50%",
        marginTop:"-161px",
        marginLeft:"-244px",
        zIndex:1002
    });

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
    autoScrollResize();
}

//*****************提交表单****************
function okbutton() {
    document.getElementById("showCon").style.display = "none";
    var open = document.getElementById("alert_open");
    open.style.display = "block";
    open.style.position = "absolute";
    open.style.top = "40%";
    open.style.left = "45%";
    open.style.marginTop = "-75px";
    open.style.marginLeft = "-150px";
    open.style.zIndex = "1002";
    if ($("#sign").val() != ORDER) {
        ORDER = $("#sign").val();
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
            channel:"",
            fName:"",
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
            channel:"D",
            fName:getSTFname("神州行卡充值"),
            s_id:"3",
            amount:money
        },
        "联通充值卡充值":{
            channel:"D",
            fName:getSTFname("联通充值卡充值"),
            s_id:"3",
            amount:money
        },
        "电信充值卡充值":{
            channel:"D",
            fName:getSTFname("电信充值卡充值"),
            s_id:"3",
            amount:money
        },
        "盛大一卡通":{
            channel:"D",
            fName:setCardFname("盛大一卡通"),
            s_id:"4",
            amount:money
        },
        "骏网一卡通":{
            channel:"D",
            fName:setCardFname("骏网一卡通"),
            s_id:"3",
            amount:money
        },
        "搜狐一卡通":{
            channel:"D",
            fName:setCardFname("搜狐一卡通"),
            s_id:"4",
            amount:money
        },
        "其他卡类支付":{
            channel:"D",
            fName:$("input[name = 'card']:checked").val(),
            s_id:"4",
            amount:money
        }
    };
    $("#channel").val(data[obj]["channel"]);
    $("#s_id").val(data[obj]["s_id"]);
    $("#f_id").val(data[obj]["fName"]);
    $("#user").val($("#username").val());
    $("#server").val($("#server_id").val());
    $("#role").val($("#actor_name").val());
    $("#amount").val(data[obj]["amount"]);
}

//*****************获取神州行充值卡和电信充值卡的渠道****************
function getSTFname(obj) {
    var data = {
        "神州行卡充值":"0",
        "联通充值卡充值":"1",
        "电信充值卡充值":"3"
    }
    return data[obj];
}


//****************获取其他卡类充值的充值渠道****************
function setOtherChannel(obj) {
    var data = {
        "U":"D",
        "M":"D",
        "D":"D"
    }
    return data[obj];
}

//*****************获取充值卡充值的_f参数的值****************
function setCardFname(cardName) {
    data = {
        "盛大一卡通":"C",
        "骏网一卡通":"4",
        "搜狐一卡通":"N"
    }
    return data[cardName];
}


function initHide() {
    $("#paytype").hide();
    $(".descripe:not(#unionpay)").hide();
}

//*****************切换充值方式****************
function changePayWay(obj) {
    var mappingfortab = {"aa1":"#unionpay", "aa3":"#alipay", "aa4":"#shenzhou", "aa5":"#unioncom", "aa6":"#chinanet", "aa7":"#shengda", "aa8":"#junwang", "aa9":"#sohupay", "aa10":"#otherpay"};
    var descripeid = mappingfortab[obj];
    $("#select_bank").hide();
    $("#paytype").hide();
    $("#payway").hide();
    $("#MyInputMoney").hide();
    $("#moneyTip").hide();
    if (obj === "aa1") {
        $("#MyInputMoney").show();
        $("#select_bank").show();
        $("#payway").show();
    } else {
        $("#money").val("");
        $("#selectPay").show();
        $("#payway").hide();
        $("#MyInputMoney").hide();
        $("#custom_charge").hide();
        $("#MyInputMoney").hide();
    }
    if (obj === "aa10") {
        $("#paytype").show();
    }
    $(descripeid).show();
    $(".descripe:not(" + descripeid + ")").hide();
    $("#" + obj).parent().addClass("active");
    $("#" + obj).parent().siblings().removeClass("active");

    var payWay = $("#" + obj).html();
    updateSelect(payWay);
    getProportion(payWay);
    moneyChange();
}

//*****************设置充值方式的可选择的充值金额****************
function updateSelect(obj) {
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


//*****************设置充值比例****************
function getProportion(text) {
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
    var showText = "充值比例： 1:" + data[text];
    $("#proportion").html(showText);
}

//*****************获取各种充值方式充值金额所对应的游戏币****************
function getPayGameMoney(obj, value) {
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
    $("#coin").val(value * data[obj]);
}

//*****************设置各种充值渠道的银行的_f参数****************
function setBankName(channel) {
    var fName = $("input[name='_f1']:checked").val();
    var name = "";
    if (channel == "A") {
        name = fName;
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
function reset() {
    $("#accountForm")[0].reset();
    $("#form")[0].reset();

    if ($("#ICBC").length > 0) {
        $("#ICBC").attr("checked", "checked");
    }
    if ($("#applay").length > 0) {
        $("#applay").attr("checked", "checked");
    }
    $("#actor_name").find("option").remove();
    $("#actor_name").css("background-color", "");
    $("#actor_name").html("<option value='0'>--请选择角色--</option>");
    $("input[name=rechargeMoney]:eq(0)").attr("checked", 'checked');
    $("input[name=card]:eq(0)").attr("checked", 'checked');

    $("input[name=pay]:eq(0)").attr("checked", 'checked');
    $("#selectCoin").val(90);
    $("#userNameTip").empty().html("<span class='font_1'><span>请输入您的七道账号</span></span>");
    ;
    $("#userName1Tip").empty().html("<span class='font_1'><span>请再次输入您的七道账号</span></span>");
    ;
}


