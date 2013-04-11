<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-21
  Time: 上午10:02
  基本信息页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style type="text/css">
    .form-label {
        font-size: 14px;
        width: 80px;
        text-align: right;
        line-height: 28px;
        height: 28px;
        display: inline-block;
    }

    .form-group {
        margin-left: 175px;
        margin-top: 10px;
    }

    .form-group .form-radio-adjust {
        margin-left: 5px;
        _display: inline;
        vertical-align: middle;
        height: 28px;
        line-height: 28px;
    }

    .css3pie {
        behavior: url(../../css/PIE.htc);
    }

    .ui-btn-rounded {
        border: 1px solid #d1d2d5;
        -webkit-border-radius: 2px;
        -moz-border-radius: 2px;
        border-radius: 2px;
        background-color: #fdfdfd;
        padding: 3px 6px;
        color: #0663b1;
        cursor: pointer;
    }

    .red-star {
        color: red;
    }
</style>
<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">基本资料</h2>

    <div class="blank15"></div>
    <div style="margin-top:50px;">
        <div class="form-group">
            <label for="loginname" class="form-label">登录名:</label>
            <span style="padding-left:5px" id="loginname"> ${userObject.userName}</span>
            <span><button class="ui-btn-rounded css3pie " onclick="forword('nav','resetPsw');">修改密码</button></span>
        </div>
        <div class="form-group">
            <label for="nickname" class="form-label">昵称:</label>
            <input type="text" name="nickname" class="textinput" id="nickname" maxlength="20" size="20"
                   value="${userObject.nickName}">
            <span id="nickNameMsg"></span>
        </div>
        <div class="form-group">
            <label for="truename" class="form-label"><span class="red-star">* </span>真实姓名:</label>
            <input type="text" name="truename" class="textinput" id="truename" size="20" maxlength="20"
                   value="${userObject.realName}"><span id="realNameMsg"></span>
        </div>
        <div class="form-group">
            <label class="form-label"><span class="red-star">* </span>所在地:</label> <span id="cityMsg"></span>
            <input type="hidden" id="userCity" value="${userObject.city}">

            <select id="province" style="margin-right:10px;"></select>
            <select id="city" style="width: 80px;"></select>
        </div>
        <div class="form-group">
            <label class="form-label">
                <span class="red-star">* </span>性别:</label>
            <input type="hidden" id="userSex" value="${userObject.gender}">
            <input type="radio" name="sex" value="1" class="form-radio-adjust"> 男
            <input type="radio" name="sex" value="0" class="form-radio-adjust"> 女
            <span id="sexMsg"></span>
        </div>
        <div class="form-group">
            <input type="hidden" id="userBirthday" value="${userObject.birthday}">
            <label class="form-label">生日:</label>
            <select style="margin-right:5px;width: 60px" id="year"></select>年
            <select style="width: 40px" id="month" onchange="monthChange()"></select>月
            <select style="width: 40px" id="day"></select>日
        </div>
        <div class="form-group">
            <label for="email" class="form-label">Email:</label>
            <span style="padding-left:5px;color:#0663b1;" id="email"> ${userObject.account.email}</span>
            <span><input type="button" value="修改" class="ui-btn-rounded css3pie " id="setEmail"
                         onclick="forword('/bindEmail','')"/></span>
        </div>
        <div class="form-group">
            <label for="idcard" class="form-label"><span class="red-star">* </span>身份证号:</label>
            <input type="text" name="idcard" class="textinput" id="idcard" size="20" maxlength="18"
                   value="${userObject.icn}"><span id="icnMsg"></span>
        </div>
        <div class="form-group">
            <label for="qq" class="form-label">QQ:</label>
            <input type="text" name="qq" class="textinput" id="qq" size="20" maxlength="15" value="${userObject.qq}">
            <span id="qqMsg"></span>
        </div>
        <div class="form-group">
            <label for="msn" class="form-label">MSN:</label>
            <input type="text" name="msn" class="textinput" id="msn" size="20" maxlength="30" value="${userObject.msn}">
            <span id="msnMsg"></span>
        </div>
        <div class="form-group">
            <label for="phone" class="form-label"><span class="red-star">* </span>联系电话:</label>

            <input type="text" name="phone" class="textinput" id="phone" size="20" maxlength="20"
                   value="${userObject.linkPhone}">
            <span id="phoneMsg"></span>
        </div>
        <div class="form-group">
            <label for="self-intro" class="form-label">自我介绍:</label>
            <textarea name="self-intro" id="self-intro" cols="30" rows="5"
                      style="margin-left:3px;border:1px solid #cacaca">${userObject.selfIntroduction}</textarea>
            <span id="selfMsg"></span>
        </div>
        <div style="margin-left: 265px;">
            <span class="save_a" onclick="modifyBaseInfo();">保 存</span>
            <span id="saveMsg_baseInfo" style="display: none;float: left;" class="save_ok save_ok2">信息修改成功!</span>
        </div>
    </div>
    <div class="blank20">
    </div>
</div>
<script type="text/javascript" src="../scripts/jquery-1.7.1.js"></script>
<script type="text/javascript">
var provinceArr = [];
provinceArr[0] = ['北京市'];
provinceArr[1] = ['天津市'];
provinceArr[2] = ['上海市'];
provinceArr[3] = ['重庆市'];
provinceArr[4] = ['河北省'];
provinceArr[5] = ['河南省'];
provinceArr[6] = ['云南省'];
provinceArr[7] = ['辽宁省'];
provinceArr[8] = ['黑龙江省'];
provinceArr[9] = ['湖南省'];
provinceArr[10] = ['安徽省'];
provinceArr[11] = ['山东省'];
provinceArr[12] = ['新疆维吾尔自治区'];
provinceArr[13] = ['江苏省'];
provinceArr[14] = ['浙江省'];
provinceArr[15] = ['江西省'];
provinceArr[16] = ['湖北省'];
provinceArr[17] = ['广西壮族'];
provinceArr[18] = ['甘肃省'];
provinceArr[19] = ['山西省'];
provinceArr[20] = ['内蒙古自治区'];
provinceArr[21] = ['陕西省'];
provinceArr[22] = ['吉林省'];
provinceArr[23] = ['福建省'];
provinceArr[24] = ['贵州省'];
provinceArr[25] = ['广东省'];
provinceArr[26] = ['青海省'];
provinceArr[27] = ['西藏'];
provinceArr[28] = ['四川省'];
provinceArr[29] = ['宁夏回族'];
provinceArr[30] = ['海南省'];
provinceArr[31] = ['台湾省'];
provinceArr[32] = ['香港特别行政区'];
provinceArr[33] = ['澳门特别行政区'];
var cityArr = [];
cityArr[0] = ['北京市', '东城区', '西城区', '崇文区', '宣武区', '朝阳区', '丰台区', '石景山区', '海淀区', '门头沟区', '房山区', '通州区', '顺义区', '昌平区', '大兴区', '怀柔区', '平谷区', '密云县', '延庆县'];
cityArr[1] = ['天津市', '和平区', '河东区', '河西区', '南开区', '河北区', '红桥区', '塘沽区', '汉沽区', '大港区', '东丽区', '西青区', '津南区', '北辰区', '武清区', '宝坻区', '宁河县', '静海县', '蓟县'];
cityArr[2] = ['上海市', '黄浦区', '卢湾区', '徐汇区', '长宁区', '静安区', '普陀区', '闸北区', '虹口区', '杨浦区', '闵行区', '宝山区', '嘉定区', '浦东新区', '金山区', '松江区', '青浦区', '南汇区', '奉贤区', '崇明县'];
cityArr[3] = ['重庆市', '万州区', '涪陵区', '渝中区', '大渡口区', '江北区', '沙坪坝区', '九龙坡区', '南岸区', '北碚区', '万盛区', '双桥区', '渝北区', '巴南区', '黔江区', '长寿区', '江津区', '合川区', '永川区', '南川区', '綦江县', '潼南县', '铜梁县', '大足县', '荣昌县', '璧山县', '梁平县', '城口县', '丰都县', '垫江县', '武隆县', '忠县', '开县', '云阳县', '奉节县', '巫山县', '巫溪县', '石柱土家族自治县', '秀山土家族苗族自治县', '酉阳土家族苗族自治县', '彭水苗族土家族自治县'];
cityArr[4] = ['河北省', '石家庄市', '唐山市', '秦皇岛市', '邯郸市', '邢台市', '保定市', '张家口市', '承德市', '沧州市', '廊坊市', '衡水市'];
cityArr[5] = ['河南省', '郑州市', '开封市', '洛阳市', '平顶山市', '安阳市', '鹤壁市', '新乡市', '焦作市', '济源市', '濮阳市', '许昌市', '漯河市', '三门峡市', '南阳市', '商丘市', '信阳市', '周口市', '驻马店市'];
cityArr[6] = ['云南省', '昆明市', ' 曲靖市', '玉溪市', '保山市', '昭通市', '丽江市', '思茅市', '临沧市', '楚雄彝族自治州', '红河哈尼族彝族自治州', '文山壮族苗族自治州', '西双版纳傣族自治州', '大理白族自治州', '德宏傣族景颇族自治州', '怒江傈僳族自治州', '迪庆藏族自治州'];
cityArr[7] = ['辽宁省', '沈阳市' , '大连市' , '鞍山市' , '抚顺市' , '本溪市' , '丹东市' , '锦州市' , '营口市' , '阜新市' , '辽阳市' , '盘锦市' , '铁岭市' , '朝阳市' , '葫芦岛市'];
cityArr[8] = ['黑龙江省', '哈尔滨市', '齐齐哈尔市', '鸡西市', '鹤岗市', '双鸭山市', '大庆市', '伊春市', '佳木斯市', '七台河市', '牡丹江市', '黑河市', '绥化市', '大兴安岭地区'];
cityArr[9] = ['湖南省', '长沙市', '株洲市', '湘潭市', '衡阳市', '邵阳市', '岳阳市', '常德市', '张家界市', '益阳市', '郴州市', '永州市', '怀化市', '娄底市', '湘西土家族苗族自治州'];
cityArr[10] = ['安徽省', '合肥市', '芜湖市', '蚌埠市', '淮南市', '马鞍山市', '淮北市', '铜陵市', '安庆市', '黄山市', '滁州市', '阜阳市', '宿州市', '巢湖市', '六安市', '亳州市', '池州', '宣城市'];
cityArr[11] = ['山东省', '济南市', '青岛市', '淄博市', '枣庄市', '东营市', '烟台市', '潍坊市', '济宁市', '泰安市', '威海市', '日照市', '莱芜市', '临沂市', '德州市', '聊城市', '滨州市', '菏泽市'];
cityArr[12] = ['新疆维吾尔自治区', '乌鲁木齐市', '克拉玛依市', '吐鲁番地区', '哈密地区', '昌吉回族自治州 ', '博尔塔拉蒙古自治州 ', '巴音郭楞蒙古自治州 ', '阿克苏地区', '克孜勒苏柯尔克孜自治州 ', '喀什地区', '和田地区', '伊犁哈萨克自治州', '塔城地区', '阿勒泰地区', '石河子市', '阿拉尔市', '图木舒克市', '五家渠市' ];
cityArr[13] = ['江苏省', '南京市', '无锡市', '徐州市', '常州市', '苏州市', '南通市', '连云港市', '淮安市', '盐城市', '扬州市', '镇江市', '泰州市', '宿迁市' ];
cityArr[14] = ['浙江省', '杭州市', '宁波市', '温州市', '嘉兴市', '湖州市', '绍兴市', '金华市', '衢州市', '舟山市', '台州市', '丽水市'];
cityArr[15] = ['江西省', '南昌市', '景德镇市', '萍乡市', '九江市', '新余市', '鹰潭市', '赣州市', '吉安市', '宜春市', '抚州市', '上饶市'];
cityArr[16] = ['湖北省', '武汉市', '黄石市', '十堰市', '宜昌市', '襄樊市', '鄂州市', '荆门市', '孝感市', '荆州市', '黄冈市', '咸宁市', '随州市', '恩施土家族苗族自治州', '仙桃市', '潜江市', '天门市', '神农架林区'];
cityArr[17] = ['广西壮族', '南宁市', '柳州市', '桂林市', '梧州市', '北海市', '防城港市', '钦州市', '贵港市', '玉林市', '百色市', '贺州市', '河池市', '来宾市', '崇左市'];
cityArr[18] = ['甘肃省', '兰州市', '嘉峪关市', '金昌市', '白银市', '天水市', '武威市', '张掖市', '平凉市', '酒泉市', '庆阳市', '定西市', '陇南市', '临夏回族自治州', '甘南藏族自治州'];
cityArr[19] = ['山西省', '太原市', '大同市', '阳泉市', '长治市', '晋城市', '朔州市', '晋中市', '运城市', '忻州市', '临汾市', '吕梁市' ];
cityArr[20] = ['内蒙古自治区', '呼和浩特市', '包头市', '乌海市', '赤峰市', '通辽市', '鄂尔多斯市', '呼伦贝尔市', '巴彦淖尔市', '乌兰察布市', '兴安盟', '锡林郭勒盟', '阿拉善盟' ];
cityArr[21] = ['陕西省', '西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '延安市', '汉中市', '榆林市', '安康市', '商洛市' ];
cityArr[22] = ['吉林省', '长春市', '吉林市', '四平市', '辽源市', '通化市', '白山市', '松原市', '白城市', '延边朝鲜族自治州'];
cityArr[23] = ['福建省', '福州市', '厦门市', '莆田市', '三明市', '泉州市', '漳州市', '南平市', '龙岩市', '宁德市' ];
cityArr[24] = ['贵州省', '贵阳市', '六盘水市', '遵义市', '安顺市', '铜仁地区', '黔西南布依族苗族自治州', '毕节地区', '黔东南苗族侗族自治州', '黔南布依族苗族自治州'];
cityArr[25] = ['广东省', '广州市', '韶关市', '深圳市', '珠海市', '汕头市', '佛山市', '江门市', '湛江市', '茂名市', '肇庆市', '惠州市', '梅州市', '汕尾市', '河源市', '阳江市', '清远市', '东莞市', '中山市', '潮州市', '揭阳市', '云浮市'];
cityArr[26] = ['青海省', '西宁市' , '海东地区', '海北藏族自治州', '黄南藏族自治州', '海南藏族自治州', '果洛藏族自治州', '玉树藏族自治州', '海西蒙古族藏族自治州'];
cityArr[27] = ['西藏', '拉萨市', '昌都地区', '山南地区', '日喀则地市', '那曲地区', '阿里地区', '林芝地区' ];
cityArr[28] = ['四川省', '成都市', '自贡市', '攀枝花市', '泸州市', '德阳市', '绵阳市', '广元市', '遂宁市', '内江市', '乐山市', '南充市', '眉山市', '宜宾市', '广安市', '达州市', '雅安市', '巴中市', '资阳市', '阿坝藏族羌族自治州', '甘孜藏族自治州', '凉山彝族自治州'];
cityArr[29] = ['宁夏回族', '银川市', '石嘴山市', '吴忠市', '固原市', '中卫市'];
cityArr[30] = ['海南省', '海口市', '三亚市', '五指山市', '琼海市', '儋州市', '文昌市', '万宁市', '东方市', '定安县', '屯昌县', '澄迈县', '临高县', '白沙黎族自治县', '昌江黎族自治县', '乐东黎族自治县', '陵水黎族自治县', '保亭黎族苗族自治县', '琼中黎族苗族自治县' ];
cityArr[31] = ['台湾省', '台北市', '高雄市', '基隆市', '台中市', '台南市', '新竹市', '嘉义市'];
cityArr[32] = ['香港特别行政区', '中西区', '湾仔区', '东区', '南区', '油尖旺区', '深水埗区', '九龙城区', '黄大仙区', '观塘区', '荃湾区', '葵青区', '沙田区', '西贡区', '大埔区', '北区', '元朗区', '屯门区', '离岛区' ];
cityArr[33] = ['澳门特别行政区', '澳门'];
var ccCity = null;
$(document).ready(function () {
    var sex = $("#userSex").val();
    var birth = $("#userBirthday").val();
    var city = $("#userCity").val();
    var email = $("#email").html();
    var icn = $("#idcard").val();
    if ($.trim(icn)) {
        $("#idcard").attr("disabled", "true");
    }
    if (!email || !$.trim(email)) {
        $("#setEmail").val("设置");
    }
    else {
        $("#setEmail").val("修改");
    }
    if (!sex) {
    }
    else {
        $("input[name=sex][value=" + sex + "]").attr("checked", true);//value=34的radio被选中
    }
    var year = 0, month = 0, day = 0;
    var birthday = '${userObject.birthday}';

    var START_YEAR = 1900;
    if (document.all) {
        var navAgent = navigator.userAgent.toLowerCase();
        if (navAgent.indexOf("msie") > -1 && navAgent.indexOf('opera') == -1 && navAgent.indexOf("9.0") > -1) {
            START_YEAR = 1900;
        } else {
            START_YEAR = 0;
        }
    }
    var date;
    if (birthday != null && birthday != "") {
        if (document.all) {
            var tearDate = birthday.split("-");
            date = new Date(tearDate[0], tearDate[1] - 1, tearDate[2]);
        } else {
            date = new Date(birthday);
        }
        year = START_YEAR + date.getYear();
        month = date.getMonth() + 1;
        day = date.getDate();
    }
    var cCity = '${userObject.city}';
    var ccP = null;
    if (cCity != null) {
        var c = cCity.split(",");
        ccP = c[0];
        ccCity = c[1];
    }
    var currDate = new Date();
    var cYear = START_YEAR + currDate.getYear();
    var yearSelect = $("#year");
    for (var i = 0; i < 100; i++) {
        var ccYear = cYear - i;
        var str = "";
        if (ccYear == year) {
            str = "<option selected=\"selected\">" + ccYear + "</option>";
        } else {
            str = "<option>" + ccYear + "</option>";
        }
        yearSelect.append(str);
    }
    var monthSelect = $("#month");
    for (var i = 1; i < 13; i++) {
        if (month == i) {
            monthSelect.append("<option selected = \"selected\">" + i + "</option>");
        } else {
            monthSelect.append("<option>" + i + "</option>");
        }
    }
    if (day == 0)day = 1;
    //初始化日期
    var nDate = new Date(year, month);
    var days = (new Date(nDate.getTime() - 1000 * 60 * 60 * 24)).getDate();
    var daySelect = $("#day");
    for (var i = 1; i <= days; i++) {
        if (day == i) {
            daySelect.append("<option selected=\"selected\">" + i + "</option>");
        } else {
            daySelect.append("<option>" + i + "</option>");
        }
    }
    var provinceSelect = $("#province");
    if (ccP == null) {
        ccP = provinceArr[0];
    }
    for (var i = 0; i < provinceArr.length; i++) {
        if (ccP != null && ccP == provinceArr[i]) {
            provinceSelect.append("<option selected=\"selected\">" + provinceArr[i] + "</option>");
        } else {
            provinceSelect.append("<option>" + provinceArr[i] + "</option>");
        }
    }
    if (ccCity == null) {
        ccCity = cityArr[0];
    }
    var citySelect = $("#city");
    citySelect.empty();
    var currProvince = provinceSelect.val();
    var count = cityArr.length;
    for (var i = 0; i < count; i++) {
        if (currProvince == provinceArr[i][0]) {
            for (var j = 1; j < cityArr[i].length; j++) {
                var city = cityArr[i][j];
                if (ccCity != null && ccCity == city) {
                    citySelect.append("<option selected=\"selected\">" + city + "</option>");
                } else {
                    citySelect.append("<option>" + city + "</option>");
                }
            }
        }
    }
    provinceSelect.change(function () {
        var citySelect = $("#city");
        citySelect.empty();
        var currProvince = provinceSelect.val();
        var count = cityArr.length;
        for (var i = 0; i < count; i++) {
            if (currProvince == provinceArr[i][0]) {
                for (var j = 1; j < cityArr[i].length; j++) {
                    var city = cityArr[i][j];
                    citySelect.append("<option>" + city + "</option>");
                }
            }
        }
    });
});
function monthChange() {
    var year = $("#year").val();
    var month = $("#month").val();
    var nDate = new Date(year, month);
    var days = (new Date(nDate.getTime() - 1000 * 60 * 60 * 24)).getDate();
    var daySelect = $("#day");
    daySelect.empty();
    for (var i = 1; i <= days; i++) {
        daySelect.append("<option>" + i + "</option>");
    }
}
function modifyBaseInfo() {
    $("#realNameMsg").empty();
    $("#cityMsg").empty();
    $("#sexMsg").empty();
    $("#icnMsg").empty();
    $("#phoneMsg").empty();
    $("#qqMsg").empty();
    $("#selfMsg").empty();
    $("#msnMsg").empty();
    var nickName = $("#nickname").val();
    var realName = $("#truename").val();
    var city = $("#province").val() + ',' + $("#city").val();
    var gender = $("input[name='sex']:checked").val();
    var birthday = $("#year").val() + "-" + $("#month").val() + "-" + $("#day").val();
    var icn = $("#idcard").val();
    var qq = $("#qq").val();
    var msn = $("#msn").val();
    var linkPhone = $("#phone").val();
    var selfIntroduction = $("#self-intro").val();

    if (!realName || !city || !gender || !icn || !linkPhone || $.trim(realName).length < 1 || $.trim(linkPhone).length < 8 ||
            $.trim(linkPhone).length > 20 || (($.trim(qq).length < 4 || $.trim(qq).length > 15) && qq) ||
            (nickName && ($.trim(nickName).length < 2 || $.trim(nickName).length > 20))) {
        if (!realName || $.trim(realName).length < 1) {
            $("#realNameMsg").html("  真实姓名为必填项(不含空格)！").css("color", "red");
        }
        if (!city) {
            $("#cityMsg").html("  所在地为必填项！").css("color", "red");
        }
        if (!gender) {
            $("#sexMsg").html("  性别为必选项！").css("color", "red");
        }
        if (!icn) {
            $("#icnMsg").html("  身份证号为必填项!(填写之后不能修改)").css("color", "red");
        }
        if (!linkPhone || $.trim(linkPhone).length < 8 || $.trim(linkPhone).length > 20) {
            $("#phoneMsg").html("  联系电话为必填项(8~12位)！").css("color", "red");
        }
        if (($.trim(qq).length < 4 || $.trim(qq).length > 15) && qq) {
            $("#qqMsg").html("  QQ位数(4~15位)！").css("color", "red");
        }
        if ((nickName && ($.trim(nickName).length < 2 || $.trim(nickName).length > 20))) {
            $("#nickNameMsg").html("  昵称位数(2~20位)！").css("color", "red");
        }
    }
    else {

        if (!makeCallThis($.trim(linkPhone)) || (qq && !isQQ($.trim(qq))) || (selfIntroduction && $.trim(selfIntroduction).length > 200) ||
                (msn && !msn.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)) || (realName && realName.length > 8) || (icn && icn.indexOf("*") < 0 && 0 != validateIdCard($.trim(icn)))) {
            if (!makeCallThis($.trim(linkPhone)))
                $("#phoneMsg").html(" 请输入正确的格式！固话请加横线！").css("color", "red");
            if (qq && !isQQ($.trim(qq))) {
                $("#qqMsg").html("  请输入正确的QQ号码格式！").css("color", "red");
            }
            if (selfIntroduction && $.trim(selfIntroduction).length > 200) {
                $("#selfMsg").html("  自我介绍不能超过200字符！").css("color", "red");
            }

            if (msn && !msn.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) && msn.length <= 50) {
                $("#msnMsg").html(" 请输入正确的msn！(长度30以内)").css("color", "red");
            }
            if (realName && realName.length > 8) {
                $("#realNameMsg").html("  真实姓名不超过8个字符(含空格)！").css("color", "red");
            }
            if (icn && 0 != validateIdCard($.trim(icn)) && icn.indexOf("*") < 0) {
                $("#icnMsg").html("  非法身份证号!(填写之后不能修改)").css("color", "red");
            }
        } else {
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/AccountInfoSubmit",
                data:{type:"basic", userName:$("#userName").val(),
                    nickName:nickName,
                    realName:realName,
                    city:city,
                    gender:gender,
                    birthday:birthday,
                    icn:icn,
                    qq:qq,
                    msn:msn,
                    linkPhone:linkPhone,
                    selfIntroduction:selfIntroduction},
                beforeSend:function (XMLHttpRequest, textStatus) {

                },
                success:function (msg) {
                    if (msg.code == 200) {
                        var icn = $("#idcard").val();
                        if ($.trim(icn)) {
                            $("#idcard").attr("disabled", "true");
                        }
                        $("#saveMsg_baseInfo").show();
                        setTimeout(function () {
                            $("#saveMsg_baseInfo").fadeOut(2000);
                        }, 1000);
                    }
                    else {
                        alert(msg.msg);
                    }
                },
                error:function (msg) {
                    window.location.href = "/login.html";
                }
            });
        }

    }

    return false;
}
</script>

