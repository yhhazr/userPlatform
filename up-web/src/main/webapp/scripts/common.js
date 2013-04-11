/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-8
 * Time: 下午5:05
 * 放一些公共的输入验证方法
 */


    //判断电话格式
function makeCallThis(phone) {
    var flag = false;
    var reg0 = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;   //判断 固话
    var reg1 = /^((\(\d{2,3}\))|(\d{3}\-))?(13|15|18)\d{9}$/;                     //判断 手机
    if (reg0.test(phone)) flag = true;
    if (reg1.test(phone)) flag = true;
    if (!flag) {
        return false;
    } else {
        return true;
    }
}
//判断邮箱格式
function isEmail(str) {
    if (/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(str)) {
        return true
    }
    return false;
}
//判断QQ格式
function isQQ(str) {
    if (/^\d{5,9}$/.test(str)) {
        return true;
    }
    return false;
}
/*
 功能：验证身份证号码是否有效
 提示信息：未输入或输入身份证号不正确！
 使用：validateIdCard(obj)
 返回：0,1,2,3
 */
function validateIdCard(obj) {
    var aCity = {11:"北京", 12:"天津", 13:"河北", 14:"山西", 15:"内蒙古", 21:"辽宁", 22:"吉林", 23:"黑龙江", 31:"上海", 32:"江苏", 33:"浙江", 34:"安徽", 35:"福建", 36:"江西", 37:"山东", 41:"河南", 42:"湖北", 43:"湖南", 44:"广东", 45:"广西", 46:"海南", 50:"重庆", 51:"四川", 52:"贵州", 53:"云南", 54:"西藏", 61:"陕西", 62:"甘肃", 63:"青海", 64:"宁夏", 65:"新疆", 71:"台湾", 81:"香港", 82:"澳门", 91:"国外"};
    var iSum = 0;
    //var info = "";
    var strIDno = obj;
    var idCardLength = strIDno.length;
    if (!/^\d{17}(\d|x)$/i.test(strIDno) && !/^\d{15}$/i.test(strIDno))
        return 1; //非法身份证号
    if (aCity[parseInt(strIDno.substr(0, 2))] == null)
        return 2;// 非法地区
    // 15位身份证转换为18位
    if (idCardLength == 15) {
        sBirthday = "19" + strIDno.substr(6, 2) + "-" + Number(strIDno.substr(8, 2)) + "-" + Number(strIDno.substr(10, 2));
        var d = new Date(sBirthday.replace(/-/g, "/"))
        var dd = d.getFullYear().toString() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
        if (sBirthday != dd)
            return 3; //非法生日
        strIDno = strIDno.substring(0, 6) + "19" + strIDno.substring(6, 15);
        strIDno = strIDno + GetVerifyBit(strIDno);
    }
    // 判断是否大于2078年，小于1900年
    var year = strIDno.substring(6, 10);
    if (year < 1900 || year > 2078)
        return 3;//非法生日
    //18位身份证处理
    //在后面的运算中x相当于数字10,所以转换成a
    strIDno = strIDno.replace(/x$/i, "a");
    sBirthday = strIDno.substr(6, 4) + "-" + Number(strIDno.substr(10, 2)) + "-" + Number(strIDno.substr(12, 2));
    var d = new Date(sBirthday.replace(/-/g, "/"))
    if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate()))
        return 3; //非法生日
    // 身份证编码规范验证
    for (var i = 17; i >= 0; i--)
        iSum += (Math.pow(2, i) % 11) * parseInt(strIDno.charAt(17 - i), 11);
    if (iSum % 11 != 1)
        return 1;// 非法身份证号
    // 判断是否屏蔽身份证
    var words = new Array();
    words = new Array("11111119111111111", "12121219121212121");
    for (var k = 0; k < words.length; k++) {
        if (strIDno.indexOf(words[k]) != -1) {
            return 1;
        }
    }
    return 0;
}

function isChn(str) {
    var reg = /^[\u4E00-\u9FA5]+$/;
    if (!reg.test(str)) {
        return false;
    }
    return true;
}


/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-21
 * Time: 上午11:11
 * 个人中心js
 */
function checkstr(str) {
    if (str >= 48 && str <= 57) {//数字
        return 1;
    } else if (str >= 65 && str <= 90) {//大写字母
        return 2;
    } else if (str >= 97 && str <= 122) {//小写字母
        return 3;
    } else {//特殊字符
        return 4;
    }
}

function check(string) {
    l_num = 0;
    var sz = false, dzm = false, xzm = false, ts = false;
    for (i = 0; i < string.length; i++) {
        asc = checkstr(string.charCodeAt(i));
        if (asc == 1 && sz == false) {
            l_num += 1;
            sz = true;
        }
        if (asc == 3 && xzm == false) {
            l_num += 2;
            xzm = true;
        }
        if (asc == 2 && dzm == false) {
            l_num += 2;
            dzm = true;
        }
        if (asc == 4 && ts == false) {
            l_num += 10;
            ts = true;
        }
    }
    return l_num;
}

function forword(url, action) {
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
            $("#showAjax").empty();
        },
        success:function (msg) {
            var backColor = "#2C9907";
            if (url.indexOf("/bindEmail") >= 0) {
                document.title = '密保邮箱';
                $("#currentFn").empty().append("密保邮箱");
                $("#bindEmail").parent().siblings(".menulist").removeClass("menulist_select");
            }

            if (url.indexOf("/bindEmail?type=info") >= 0) {
                document.title = '邮箱绑定';
                $("#currentFn").empty().append("邮箱绑定");
                $("#bindEmailInfo").parent().siblings(".menulist").removeClass("menulist_select");
            }
            if (url.indexOf("/bindQuestion") >= 0) {
                document.title = '密保问题';
                $("#currentFn").empty().append("密保问题");
                $("#bindQuestion").parent().siblings(".menulist").removeClass("menulist_select");
            }
            if (url.indexOf("/bindMobile") >= 0) {
                document.title = '密保手机';
                $("#currentFn").empty().append("密保手机");
                $("#bindMobile").parent().siblings(".menulist").removeClass("menulist_select");
            }
            if (action == "avatar") {
                document.title = '修改头像';
                $("#currentFn").empty().append("修改头像");
                $("#avatarNav").parent().siblings(".menulist").removeClass("menulist_select");
            }
            if (action == "resetPsw") {
                document.title = '修改密码';
                $("#currentFn").empty().append("修改密码");
                $("#resetPswNav").siblings(".menulist").removeClass("menulist_select");
                $("#resetPswNav").addClass("menulist_select");
            }
            if (action == "index") {
                document.title = '基本信息';
                $("#currentFn").empty().append("基本信息");
                $("#indexNav").siblings(".menulist").removeClass("menulist_select");
                $("#indexNav").addClass("menulist_select");
            }


            if (msg) {
               $("#showAjax").empty().append(msg).show().siblings().hide();
            }
            else {
                window.location.href = "/login.html";
            }

        },
        error:function (msg) {
            window.location.href = "/login.html";
        }
    });
}

function swapTo(action) {
    var backColor = "#2C9907";
    if (action == "index") {
        document.title = '基本信息';
        $("#currentFn").empty().append("基本信息");
        $("#indexNav").siblings(".menulist").removeClass("menulist_select");
        $("#indexNav").addClass("menulist_select");
        $("#indexInfoDiv").show().siblings().hide();

    }
    if (action == "ipException") {
        document.title = '异常信息';
        $("#currentFn").empty().append("异常信息");
        $("#ipExceptionDiv").show().siblings().hide();
    }

    if (action == "workInfo") {
        document.title = '工作信息';
        $("#currentFn").empty().append("工作信息");
        $("#workInfoNav").parent().siblings(".menulist").removeClass("menulist_select");
        $("#workInfoDiv").show().siblings().hide();
    }
    if (action == "eduInfo") {
        document.title = '教育信息';
        $("#currentFn").empty().append("教育信息");
        $("#eduInfoNav").parent().siblings(".menulist").removeClass("menulist_select");
        $("#eduInfoDiv").show().siblings().hide();
    }
    if (action == "detailInfo") {
        document.title = '详细资料';
        $("#currentFn").empty().append("详细资料");
        $("#detailInfoNav").parent().siblings(".menulist").removeClass("menulist_select");
        $("#detailInfoDiv").show().siblings().hide();
    }
    if (action == "baseInfo") {
        document.title = '基本资料';
        $("#currentFn").empty().append("基本资料");
        $("#baseInfoNav").parent().siblings(".menulist").removeClass("menulist_select");
        $("#baseInfoDiv").show().siblings().hide();
    }


}


function logout() {
    $.ajax({
        type:"GET",
        dataType:"text",
        url:"/logout",
        data:{action:"logout"},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            var result = confirm('确认退出吗？');
            if (result == true)
                window.location.href = "/login.html";
        },
        error:function (msg) {

            window.location.href = "/login.html";
        }
    });
}

//防沉迷锁屏
function lockScreen(boolean){
    if(boolean){
        $(".lockScreen").height($(document).height()).css("opacity","0.4").show();
    }else{
        $(".lockScreen").hide();    
    }
}