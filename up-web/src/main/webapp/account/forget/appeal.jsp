<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>账号申诉</title>
    <link href="${imageDomainUrl}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/userhome.css" rel="stylesheet" type="text/css" />
    <link href="${imageDomainUrl}/css/serviceCenter.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
            /* -- Table Styles ------------------------------- */
        td {
            font: 10pt Helvetica, Arial, sans-serif;
            vertical-align: top;
        }

        .progressWrapper {
            width: 357px;
            overflow: hidden;
        }

        .progressContainer {
            margin: 5px;
            padding: 4px;
            border: solid 1px #E8E8E8;
            background-color: #F7F7F7;
            overflow: hidden;
        }
            /* Message */
        .message {
            margin: 1em 0;
            padding: 10px 20px;
            border: solid 1px #FFDD99;
            background-color: #FFFFCC;
            overflow: hidden;
        }
            /* Error */
        .red {
            border: solid 1px #B50000;
            background-color: #FFEBEB;
        }

            /* Current */
        .green {
            border: solid 1px #DDF0DD;
            background-color: #EBFFEB;
        }

            /* Complete */
        .blue {
            border: solid 1px #CEE2F2;
            background-color: #F0F5FF;
        }

        .progressName {
            font-size: 8pt;
            font-weight: 700;
            color: #555;
            width: 323px;
            height: 14px;
            text-align: left;
            white-space: nowrap;
            overflow: hidden;
        }

        .progressBarInProgress,
        .progressBarComplete,
        .progressBarError {
            font-size: 0;
            width: 0%;
            height: 2px;
            background-color: blue;
            margin-top: 2px;
        }

        .progressBarComplete {
            width: 100%;
            background-color: green;
            visibility: hidden;
        }

        .progressBarError {
            width: 100%;
            background-color: red;
            visibility: hidden;
        }

        .progressBarStatus {
            margin-top: 2px;
            width: 337px;
            font-size: 7pt;
            font-family: Arial;
            text-align: left;
            white-space: nowrap;
        }

        a.progressCancel {
            font-size: 0;
            display: block;
            height: 14px;
            width: 14px;
            background-image: url(scripts/swfupload/cancelbutton.gif);
            background-repeat: no-repeat;
            background-position: -14px 0px;
            float: right;
        }

        a.progressCancel:hover {
            background-position: 0px 0px;
        }
            /* -- SWFUpload Object Styles ------------------------------- */
        .swfupload {
            vertical-align: top;
        }
		.formerr2{ _width:140px;}
    </style>
</head>

<body>
<jsp:include page="/common/topClient.jsp" flush="true"></jsp:include>

<div class="blank"></div>

<div class="bread"><p><a href="/">首页</a>&gt;<a href="/forget.html">客服中心</a>&gt;<span>帐号申诉</span></p></div>

<div class="serviceCenterMain">

    <div class="topline wbfb"></div>
    <div class="rgtcontent">
        <h2 class="h2_title">帐号申诉-<strong>找回密码</strong></h2>
        <form method="post">
            <input type="hidden" id="userName" name="userName" value="${param.userName}">
            <input type="hidden" id="idCardImgPath" name="idCardImgPath" value="">
            <input type="hidden" id="uploading" name="uploading" value="0">
            <div class="set_question set_question1">
                <div class="yiwang">密码成功找回后，此帐号已绑定的所有密保将会被重置。</div>
                <div class="serviceCenterNav">基本信息</div>
                <div class="serviceList zoom">
                    <label>用户名：</label>
                    <span>${param.userName}</span>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>真实姓名：</label>
                    <input id="realName" name="realName" type="text" class="textinput" maxlength="5" onkeyup="value=value.replace(/[^\u4E00-\u9FA5]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\u4E00-\u9FA5]/g,''))"/>
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>身份证：</label>
                    <input id="idCard" name="idCard" type="text" class="textinput" maxlength="18">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>经常玩的游戏：</label>
                    <input id="oftenPlayGame" name="oftenPlayGame" type="text" class="textinput" maxlength="20">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>所在服务器：</label>
                    <input id="serverName" name="serverName" type="text" class="textinput" maxlength="10">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>角色名称：</label>
                    <input id="playerName" name="playerName" type="text" class="textinput" maxlength="10">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>角色等级：</label>
                    <input id="playerLevel" name="playerLevel" type="text" class="textinput" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g))" maxlength="3">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceCenterNav">联系信息</div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>邮箱：</label>
                    <input id="email" name="email" type="text" class="textinput" maxlength="30">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                    <div class="serviceWarning"><span class="prompt"></span><cite>邮箱用于接收申诉通知邮件，请认真填写。</cite></div>
                </div>
                <div class="serviceCenterNav">申诉基本资料</div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>注册时间：</label>
                    <input id="createDate" name="createDate" type="text" class="textinput" maxlength="20">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom relative">
                    <label><em style="color:red">*</em>注册城市：</label>
                    <input id="createCity" name="createCity" type="text" class="textinput" maxlength="20">
                    <div class="formerr formerr2">
                        <div class="warning"><span class="error"></span>不能为空</div>
                    </div>
                </div>
                <div class="serviceList zoom">
                    <label>账号异常时间：</label>
                    <input id="exceptionDate" name="exceptionDate" type="text" class="textinput" maxlength="30">
                </div>
                <div class="serviceList zoom">
                    <label>最后登录时间：</label>
                    <input id="lastLoginDate" name="lastLoginDate" type="text" class="textinput" maxlength="30">
                    <div class="serviceWarning"><span class="prompt"></span>如记不清具体时间，可大致描述，如：3月10日至15日。</div>
                </div>
                <div class="serviceCenterNav">充值资料</div>
                <div class="serviceList zoom">
                    <label style="margin-top: 0"><em style="color:red">*</em>是否充值过：</label>
                    <input type="radio" name="pay" value="1" /><label style="float:none; margin-top: 0; width:auto;">是</label>
                    <input type="radio" name="pay" value="0" checked="checked" /><label style="float:none; margin-top: 0; width:auto;">否</label>
                </div>
                <div class="serviceList zoom orderIds none">
                    <div>
                        <label>订单号：</label>
                        <input name="orderId" type="text" class="textinput" maxlength="21"></div>
                </div>
                <div class="serviceList zoom btn_orderIds none">
                    <label>更多订单号：</label>
                    <a href="javascript:void(0)" class="moreBtn">点击增加</a>
                    <div class="blank5"></div>
                    <div class="serviceWarning"><span class="prompt"></span>为了保证您的账号安全，充值过的用户请务必提供订单号，否则我们将驳回申诉。<br />&nbsp;&nbsp;&nbsp;&nbsp;多个订单号将提高申诉成功的可能性。</div>
                </div>
                <div class="serviceCenterNav">身份证扫描件上传</div>
                <div class="serviceList zoom">
                    <label style="margin-top:15px; width:auto" class="uploadBtn">
                        <em class="red none">* </em>
                        <span id="spanBtnSelect"></span>
                        <input id="btnCancel" type="button" value="取消" onclick="swfu.cancelQueue();" disabled="disabled" style="margin-left: 2px; font-size: 8pt; height: 29px;display: none;" />
                    </label>
                    <div class="blank20"></div>
                    <div>
                        <div class="fieldset flash" id="fsUploadProgress">
                            <span class="legend"></span>
                        </div>
                        <div id="divStatus"></div>
                    </div>
                    <div class="blank20"></div>
                    <p class="uploadTxt">
                        1. 请上传与注册身份证号码一致的原件扫描件或数码照片。<br />
                        2. 文件必须小于5MB，且格式必须是jpg、png、gif、bmp。<br />
                        3. 不得涂改，须确保四角边完整，水印、 文字、 图片、 证件号码等清晰可辨。
                    </p>
                </div>
                <div class="blank"></div>
                <div class="serviceCenterNav">补充资料</div>
                <div class="serviceList">
                    <textarea id="otherInfo" name="otherInfo" class="supplement"onkeyup="value=value.substr(0,500)"></textarea>
                    <div class="serviceWarning" style="margin-left:0;"><span class="prompt"></span>您可以根据实际情况填写，这将提高申诉成功的可能性。</div>
                </div>
                <div class="serviceList zoom">
                    <a class="prevBtn" href="javascript:history.back();">上一步</a>
                    <a class="nextBtn" title="下一步" href="javascript:void(0)">下一步</a>
                </div>
            </div><!--第一步end-->
            <div class="set_question set_question2 none">
                <div class="set_mobleok">
                    <div class="anquandun emaildun"></div>
                    <strong class="f14px left setsj_ok" style="margin-top: 30px;">亲爱的${param.userName},您的申诉已经提交成功<br />
                        一旦申诉成功，该帐号当前密码和所有密保将会被重置
                    </strong>
                    <div class="blank" ></div>
                    <span class="f14px left setsj_ok" style="margin: 15px 0 0 120px;">提示：     <br />
                        1. 申诉结果会在2-3个工作日内通过邮件发送给您。       <br />
                        2. 根据您提交的申诉资料及申诉号码的争议情况，第七大道可能延长审核期限，最多不超过5个工作日。

                    </span>
                    <div class="edit_email">
                        <!--
                        <a href="/" title="返回首页" class="editemail_a"></a>
                        -->
                    </div>
                </div>
                <div class="blank" style="height:40px;"></div>
                <div class="blank" style="height:40px;"></div>
            </div><!--第2步end-->
        </form>
        <div class="blank20"></div>
    </div>

    <div class="blank"></div>
</div>

<%@include file="/common/bottom.jsp"%>
<script type="text/javascript" src="${staticDomainUrl}/scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/userhome.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="${staticDomainUrl}/scripts/swfupload/handlers.js"></script>
<script type="text/javascript">
    var shijian = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    $(function(){
        $(".nextBtn").click(function(){
            if($.trim($("#realName").val()) == ''){
                alert("请输入真实姓名");
                return false;
            }
            if($.trim($("#oftenPlayGame").val()) == ''){
                alert("请输入经常玩的游戏");
                return false;
            }
            if($.trim($("#serverName").val()) == ''){
                alert("请输入所在服务器");
                return false;
            }
            if($.trim($("#playerName").val()) == ''){
                alert("请输入角色名称");
                return false;
            }
            if($.trim($("#playerLevel").val()) == ''){
                alert("请输入角色等级");
                return false;
            }
            if($.trim($("#createDate").val()) == ''){
                alert("请输入注册时间");
                return false;
            }
            if( $.trim($("#createCity").val()) == ''){
                alert("请输入邮箱");
                return false;
            }
            if($.trim($("#createCity").val()) == ''){
                alert("请输入注册城市");
                return false;
            }
            if($.trim($("#idCard").val()) == ''){
                alert("请输入身份证");
                return false;
            }

            if(!isIdCardNo($("#idCard").val())){
                alert("身份证不正确");
                return false;
            }
            if(!shijian.test($("#email").val())){
                alert("邮件地址不正确");
                return false;
            }

            var pay = $(":radio:checked").val();
            if(pay == 1){
                for(i=0;i<$("[name=orderId]").length;i++){
                    if($("[name=orderId]").eq(i).val() == ''){
                        alert("请输入订单号");
                        return false;
                    }
                }
            }

            if($("#uploading").val() == '1') {
                alert("文件上传中");
                return false;
            }

            var formObj = $("form");
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"appeal",
                data: formObj.serialize(),
                success:function(msg){
                    if(msg.code > 0){
                        $(".set_question1").hide();
                        $(".set_question2").show();

                        var count = 3;
                        var tmp = setInterval(function(){
                            if (i-- == 0){
                                clearInterval(tmp);
                                location = '${hostDomain}/login.html';
                            }
                        }, 1000);
                    } else {
                        alert("申诉失败");
                    }
                }
            });
        });

        $(":radio").click(function(){
            if($(this).val() == '0'){
                $(".orderIds,.btn_orderIds").hide();
            }
            if($(this).val() == '1'){
                $(".orderIds,.btn_orderIds").show();
            }
        });

        $(".moreBtn").click(function(){
            var moreOrderHtml = "<div class=\"more_orderId\"><label>订单号：</label><input name=\"orderId\" type=\"text\" class=\"textinput\" maxlength=\"21\"><a href=\"javascript:void(0);\"><img src=\"img/delete.png\"></a></div>";
            if ($(".orderIds div").length > 4) {
                alert("最多填写五个订单号");
                return;
            }
            $(".orderIds").append(moreOrderHtml);
        });
        $(".more_orderId a").live("click", function(){
            $(this).parent().remove();
        });
        $("#oftenPlayGame,#serverName,#playerName,#createDate,#createCity,#realName").blur(function(){
            if($.trim($(this).val())==''){
                $(this).siblings('.formerr').show();
            };
        });
        $("#playerLevel").blur(function(){
            if($(this).val()==''||$(this).val().length<1){
                $(this).siblings('.formerr').show();
            };
        });
        $('.textinput').focus(function(){
            $(this).siblings('.formerr').hide();
        });
        $("#idCard").blur(function(){
            if($(this).val() == ''){
                $(this).siblings('.formerr').show().html("<div class=\"warning\"><span class=\"error\"></span>不能为空</div>");
                return false;
            }
            if(!isIdCardNo($(this).val())){
                $(this).siblings('.formerr').show().html("<div class=\"warning\"><span class=\"error\"></span>身份证号不正确</div>");
            }
        });
        $("#email").blur(function(){
            var thisval = $(this).val();
            if(thisval==''){
                //$(this).siblings('.serviceWarning').children('cite').text('邮箱错误');
                $(this).siblings('.formerr').show().html("<div class=\"warning\"><span class=\"error\"></span>不能为空</div>");
                return false;
            }
            if(!shijian.test(thisval)){
                $(this).siblings('.formerr').show().html("<div class=\"warning\"><span class=\"error\"></span>邮箱地址不正确</div>");
                return false;
            }
        });
    });

    /*判断身份证*/
    function isIdCardNo(num){
        var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1),error,varArray = new Array(),intValue,lngProduct = 0,intCheckDigit,intStrLen = num.length,idNumber = num;
        if ((intStrLen != 15) && (intStrLen != 18)) {
            return false;
        }
        for(i=0;i<intStrLen;i++){
            varArray[i] = idNumber.charAt(i);
            if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
                return false;
            }else if (i < 17) {
                varArray[i] = varArray[i]*factorArr[i];
            }
        }
        if (intStrLen == 18) {
            var date8 = idNumber.substring(6,14);
            if (checkDate(date8) == false) {
                return false;
            }

            for(i=0;i<17;i++) {
                lngProduct = lngProduct + varArray[i];
            }

            intCheckDigit = 12 - lngProduct % 11;
            switch (intCheckDigit) {
                case 10:
                    intCheckDigit = 'X';
                    break;
                case 11:
                    intCheckDigit = 0;
                    break;
                case 12:
                    intCheckDigit = 1;
                    break;
            }

            if (varArray[17].toUpperCase() != intCheckDigit) {
                return false;
            }
        }else{
            var date6 = idNumber.substring(6,12);
            if (checkDate(date6) == false) {
                return false;
            }
        }
        return true;
    }

    function checkDate(date){
        return true;
    }
</script>
</body>
</html>