<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">密保手机-<strong>设置</strong></h2>
    <div class="set_moblebox">
        <div style="line-height: 32px; height:32px; overflow: hidden; float:left ">
            <label>请输入手机号：</label>
            <input id="mobile" type="text" class="textinput" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g))" />
        </div>
        <div class="blank"></div>
        <!--
        <div class="f12px">若当前号码已不用/丢失，或无法收到验证码？<a href="#" target="_blank">提交申请单</a>由客服帮助处理</div>
        -->
        <div class="getyzmbox mt_5 left" style="_display:inline;">
            <a id="sendMessage" class="getCode" href="javascript:void(0);">获取短信验证码</a>
            <span class="left">当天只能获取五次</span>
        </div>
        <div class="blank"></div>
        <div class="yzm_input relative left" style="_display:inline;">
            <label>验证码：</label><input id="mobileCode" type="text" maxlength="6" />
            <div class="formerr formerr_setmoble">
                <div class="warning"><span class="error"></span>验证码错误</div>
            </div>
        </div>
        <div class="blank"></div>
        <a href="javascript:void(0)" class="thenext  thenext_4">下一步</a>
    </div><!--第一步end-->
    <div class="set_mobleok none">
        <div class="zoom">
            <div class="anquandun emaildun"></div>
            <strong class="f16px left setsj_ok">密保手机设置成功！</strong>
        </div>
        <div class="blank"></div>
        <div class="edit_email">
            <a href="/accountcenter.html" class="theprev left theprev_1">返回首页</a>
        </div>
        <div class="blank20"></div>
        <div class="blank20"></div>
        <div class="blank20"></div>
    </div><!--第二步end-->
    <div class="blank20"></div>
</div>

<script type="text/javascript">
    $(function(){
        var time = 0;
        var tagSend = $("#sendMessage");
        tagSend.click(sendMobileCode);
        //设置手机密保
        $(".thenext_4").click(function(){
            var mobile = $("#mobile").val();
            var mobileCode = $("#mobileCode").val();
            if (mobile == '') {
                $(".formerr_setmoble").css({"top":"-77px","left":"285px"}).show().find(".warning").html("<span class=\"error\"></span>请输入手机号");
                return false;
            }
            if (!isMobil(mobile)) {
                $(".formerr_setmoble").css({"top":"-77px","left":"285px"}).show().find(".warning").html("<span class=\"error\"></span>手机号码错误");
                return false;
            }
            if (mobileCode == '') {
                $(".formerr_setmoble").css({"top":"-2px","left":"160px"}).show().find(".warning").html("<span class=\"error\"></span>请输入验证码");
                return false;
            }
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/sendMobileCodeSubmit?" + new Date().getTime(),
                data:{user:"${userObject.userName}",mobile:mobile, code:mobileCode, time:time, type:"bind"},
                async:false,
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".set_moblebox").hide();
                        $(".set_mobleok").show();
                    } else {
                        $(".formerr_setmoble").css({"top":"-2px","left":"160px"}).show().find(".warning").html("<span class=\"error\"></span>验证码错误");
                    }
                }
            });
        })

        function sendMobileCode(){
            var mobile = $("#mobile").val();
            if (mobile == '') {
                $(".formerr_setmoble").css({"top":"-77px","left":"285px"}).show().find(".warning").html("<span class=\"error\"></span>不能为空");
                return false;
            }
            if (!isMobil(mobile)) {
                $(".formerr_setmoble").css({"top":"-77px","left":"285px"}).show().find(".warning").html("<span class=\"error\"></span>手机号码错误");
                return false;
            }
            tagSend.unbind("click");
            var tDelay = 60;
            var fnDelay = function(){
                if (tDelay < 0) {
                    if($(".getyzmbox .left").text().indexOf("0") == -1){
                        tagSend.bind("click", sendMobileCode);
                    }
                    tagSend.text("获取短信验证码");
                } else {
                    tagSend.text("(" + tDelay + "秒)重新获取");
                    tDelay --;
                    setTimeout(function(){fnDelay();},1000);
                }
            };
            fnDelay();
            time = new Date().getTime();
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/sendMobileCode?" + new Date().getTime(),
                data:{user:"${userObject.userName}", mobile:mobile, time:time, type:"bind"},
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".getyzmbox .left").html(msg.msg);
                    } else {
                        $(".getyzmbox .left").html(msg.msg);
                    }
                }
            });
        }

        $(".set_moblebox input:text").focus(function(){
            $(".set_moblebox .formerr").hide();
        });

    });
    function isMobil(s) {
        // /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/
        var p = /^0?1[3|4|5|8][0-9]{9}$/;
        if (!p.exec(s)) {
            return false;
        }
        return true;
    }
</script>


