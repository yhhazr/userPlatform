//*****************登陆加载****************
$(document).ready(function () {
//初始值
    initHide();
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

    $("#close").click(function () {
        $("#showCon").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("body").css("overflow", "visible");
    });

    $("#cancle_button").click(function () {
        $("#showCon").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });

    $("#cancle_all").click(function () {
        $("#alert_open").css("display", "none");
        $("#mybg").remove();
        $("html").css("overflow", "auto");
        $("#body").css("overflow", "visible");
    });
    $("#close_all").click(function () {
        $("#alert_open").css("display", "none");
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
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
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
    $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
    role = 1;
    var uname = $("#username").val();

    var server = $("#server_id").val();
    if (serverId && !isNaN(serverId)) {
        server = serverId;
    }
    $.ajax({
        url:"getRole",
        cache:false,
        data:{username:uname, gameId:$("#gameId").val(), serverId:server},
        success:function (data) {
            if (data == "-1") {
                $("#actor_name").css("background-color", "#FF7900");
                $("#actor_name").empty().html("<option value='0'>--您在该服务器还未创建角色--</option>");
            } else if (data == "-3") {
                alert("系统繁忙，获取角色失败！");
            } else if (data == "-2") {
                alert("您所选的服务器正在维护！！");
            } else {
                $("#actor_name").css("background-color", "");
                $("#actor_name").empty();
                $("#actor_name").html(data);
                if (roleId && !isNaN(roleId)) {
                    $("#actor_name").find("option[value='" + roleId + "']").attr("selected", "selected");
                }
            }
            role = 0;
            roleId = null;
            name = null;
            serverId = null;
//            $("#back").css("background-position","-240px -24px");
//            $("#back").css("background-image","");
        }

    });
//     $("#back").removeClass("frash").addClass("icon-refresh");
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
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        userSign = 0;
        return false;
    }
    if (nickname.value.length > 50 || nickname.value.length < 6) {
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号长度应在6~50个字符</span></span>");
        nickname.style.backgroundColor = "#fbe2e2";
        nickname.style.borderColor = "#d28c8c";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
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
                            $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
                        }
//                        else{
//                            getUserOnBlurRole();
//                        }
                    }
                    return true;
                } else {
                    $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号不存在，请确认后输入</span></span>");
                    nickname.style.backgroundColor = "#fbe2e2";
                    nickname.style.borderColor = "#d28c8c";
                    $("#actor_name").css("background-color", "");
                    $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
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
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        userSign2 = 0;
        return false;
    }
    if (username1.value == "" || username1.value == null) {
        $('#userName1Tip').html("<span class='font_1'><SPAN>请再次输入您的七道账号</SPAN></span>");
        username1.style.border = "1px solid #C1C1C1";
        username1.style.backgroundColor = "#fbfbfb";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        userSign2 = 0;
        return false;
    }
    if (username1.value != username.value) {
        $('#userName1Tip').empty().html("<span class='font_1'><SPAN class='txt-err'>确认账号输入有误</SPAN></span>");
        username1.style.backgroundColor = "#fbe2e2";
        username1.style.borderColor = "#d28c8c";
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
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
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
        $('#userNameTip').empty().html("<span class='font_1'><span class='txt-err'>七道账号不存在，请确认后输入</span></span>");
        return false;
    }
    var newServer = $("#server_id").val();
    if (newServer == 0) {
        $("#actor_name").css("background-color", "");
        $("#actor_name").empty().html("<option value='0'>--请选择角色--</option>");
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
        getPayGameMoney(obj, value);
        return true;
    } else {
        return checkMoney();
    }
}

function checkMoney() {
    var obj = $(".active").children().html();
    document.getElementById("selectMoney").options[0].selected = "selected";
    var money = document.getElementById("money");
    if(money.value && money.value.length > 10){
        $('#moneyTip').html("<span class='font_1'></span><SPAN class='txt-err'>请输小于或等于10位数的整数</SPAN></span>");
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
//*****************金额验证结束****************


//检测角色
function selectRole() {
    var role = $("#actor_name");
    if (!role.val() || role.val == "0") {
        return false;
    } else if (document.getElementById("actor_name").options[0].selected) {
        return false;
    } else {
        $("#gameRoleTip").empty();
        return true;
    }
}

//*****************表单提交信息****************
//function accountSubmit() {
//    var rechageMoney = $("#money").val();
//    if (document.getElementById("server_id").options[0].selected) {
//        alert("服务器选择不正确！！");
//        return false;
//    }
//    if (!selectRole()) {
//        alert("角色选择不正确！！！");
//        return false;
//    }
//    if (!moneyChange()) {
//        alert("输入金额有误！！！");
//        return false;
//    }
//    var result = confirm("您的充值信息：\n" + "    充值方式：" + $("#pay_way_CN").html() + "\n    充值游戏：神曲" +
//        "\n    服 务 器：" + $("#server_id").find('option:selected').text()
//        + "\n    游戏角色：" + $("#actor_name").find('option:selected').text()
//        + "\n    充值金额：" + rechageMoney + "元" + "\n    游 戏 币：" + $("#coin").val());
//    if (result) {
//        $("#accountForm").submit();
//    }
//}

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
    if (userSign == 0 || userSign2 == 0) {
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
            //s $("#go2fail").attr("href", "/pay/result.html?_n=" + json);
//                alert("-f=" + $("#f_id").val()
//                    + ",  _c=" + $("#channel").val()
//                    + ",  _s=" + $("#s_id").val()
//                    + ", _n=" +  $("#sign").val()
//                    + ", user=" +  $("#username").val()
//                    + ", server=" +  $("#server_id").val()
//                    + ", role=" +  $("#actor_name").val()
//                    + ", amount=" +  $("#amount").val()
//                    + ", _g=" +  $("#gameId").val()
//                );

            submitConfirm();
        }
    });

}


//*****************获取表单提交的参数****************
function getParameters() {
//       var ele = document.getElementById("payUl").getElementsByTagName("li");
//        for(var i=0;i<ele.length;i++){  
//            if(ele[i].className=="active"){  
//                var text = ele[i].getElementsByTagName("a")[0].innerHTML;  
//                setParameter(text);
//            }  
//       }      
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
            channel:getSTChannel(),
            fName:getSZXfname(),
            s_id:"3",
            amount:money
        },
        "联通充值卡充值":{
            channel:"C",
            fName:"UNICOM-NET",
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
            s_id:"4",
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
            s_id:"4",
            amount:money
        },
        "其他卡类支付":{
            channel:setOtherChannel($("input[name = 'card']:checked").val()),
            fName:getOtherFname($("input[name = 'card']:checked").val()),
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
function getSTChannel() {
    var channel;
    if (showStr.indexOf("D") != -1) {
        channel = "D";
    } else {
        channel = "C";
    }
    return channel;
}

//*****************获取神州行充值卡的_f参数****************
function getSZXfname() {
    var data;
    if (showStr.indexOf("D") != -1) {
        data = "0";
    } else {
        data = "SZX-NET";
    }
    return data;
}

//*****************获取电信充值卡的_f参数****************
function getTELfname() {
    var data;
    if (showStr.indexOf("D") != -1) {
        data = "3";
    } else {
        data = "TELECOM-NET";
    }
    return data;
}

//****************获取其他卡类充值的_f参数****************
function getOtherFname(obj) {
    var fName;
    if (showStr.indexOf("D") != -1) {
        fName = obj;
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
    var data;
    if (showStr.indexOf("D") != -1) {
        data = {
            "U":"D",
            "M":"D",
            "D":"D",
            "TIANXIA-NET":"C",
            "ZONGYOU-NET":"C",
            "TIANHONG-NET":"C"
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
            "盛大一卡通":"C",
            "骏网一卡通":"4",
            "搜狐一卡通":"N"
        }
    }
    if (data[cardName] == null) {
        return;
    }
    return data[cardName][cardTag];
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
    $("#quick,#applay,#yibao").hide();
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
            $("#quick").show();
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
        $("#quick").show();
        $("#select_bank").show();
        $("#payway").show();
        $("#yibao").hide();
    } else {
        $("#money").val("");
        $("#selectPay").show();
        $("#MyInputMoney").hide();
        $("#custom_charge").hide();
        $("#MyInputMoney").hide();
    }
    if (obj === "aa7" || obj === "aa8" || obj === "aa9") {
        if (showStr.indexOf("D") != -1) {
            $("#quick").show();
            $("#quick input[type='radio']").attr("checked", "checked");
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
        $("#payway").hide();
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
function reset() {
    $("#accountForm")[0].reset();
    $("#form")[0].reset();

//    $("#server_id").removeClass("disableInputBg").addClass("normalInputBg");
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
        $("#yibao").hide();
        $("#aa5").hide();
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
    $("#quick").show();
    $("#yibao").hide();
}

