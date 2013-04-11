<%@page contentType="text/html" pageEncoding="UTF-8" %>

<div class="rgtcontent">
    <h2 class="h2_title">解除邮箱-<strong>设置</strong></h2>
    <div class="blank15"></div>
    <div class="emailbox emailbox_1">
        <div class="now_buzhou now_buzhou13"></div>
        <div class="entryemail relative left">
            <label class="f14px">输入旧邮箱：</label><input type="text" class="textinput emailYz" id="oldEmail" maxlength="30" />
            <!--
            <label class="f12px">请输入有效的邮箱地址，以便收取验证邮件</label>
            <span class="send_email"></span>
            -->
            <div class="formerr formerr_sendemail">
                <div class="warning"><span class="prompt"></span>正在发送...请在5分钟内查看邮箱</div>
            </div>
            <a href="javascript:void(0)" title="下一步" class="thenext thenext_1" style="margin: 22px 0 0 75px;">下一步</a>
            <div class="formerr formerr_email" style="left: 434px;">
                <div class="warning"><span class="error"></span>邮箱错误</div>
            </div>
        </div>
        <div class="blank"></div>
    </div><!--第一步end-->
    <div class="emailbox none emailbox_2">
        <div class="now_buzhou now_buzhou14"></div>
        <div class="blank15"></div>
        <div class="yz_email">
            <div class="yz_emailbox">
                <div class="anquandun"></div>
                <div class="anquantishi">
                    <strong class="a">"安全邮箱验证邮件"已发送到您的邮箱</strong>
                    <strong class="b">@qq.com</strong>
                    <strong class="c">请在30分钟内查找邮件并在下方填写您收到的验证码。
                        <!--<a href="#" target="_blank" class="u_line">去邮箱收信》</a>--></strong>
                    <div class="blank15"></div>
                    <div class="yanzhengma relative">
                        <label>收到的验证码：</label>
                        <input type="text" class="textinput email_yzminput" value="" maxlength="6" />
                        <div class="formerr formerr_yzm">
                            <div class="warning"><span class="error"></span>验证码错误</div>
                        </div>
                    </div>
                    <a href="javascript:void(0)" title="确定" class="save_a thenext_2">确定</a>

                </div>
                <div class="blank"></div>
            </div>
            <div class="prompting mt_15 ">
                <div class="sub_prompting gantan">没有收到验证邮件？您可以到邮件垃圾箱里找找，或者点击这里<a id="sendMail" href="javascript:void(0);">重新获取验证码</a></div>
            </div>
        </div>
    </div><!--第二步end-->
    <div class="emailbox none emailbox_3">
        <div class="now_buzhou now_buzhou15"></div>
        <div class="blank15"></div>
        <div class="set_mobleok">
            <div class="anquandun emaildun"></div>
            <strong class="f16px left setsj_ok">旧邮箱解除成功！</strong>
            <div class="blank"></div>
            <div class="jiechuemail_ok">
                <a href="/accountcenter.html" class="theprev left theprev_1">返回首页</a>
                <a href="javascript:forword('/bindEmail?type=bind','')" title="立即设置" class="thenext left thenext_6" style="margin:0 0 0 30px;">立即设置</a>
            </div>
            <div class="blank20"></div>
            <div class="blank20"></div>
            <div class="blank20"></div>
        </div>
    </div><!--第三步end-->
    <div class="blank20"></div>
</div>

<script type="text/javascript">
    var time = 0;
    var email = "${user.email}";
    var tagSend;

    $(function(){
        tagSend = $("#sendMail");
        $(".thenext_1").click(function(){
            var thisval = $(".emailYz").val();
            var shijian = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if(thisval==''){
                $(".entryemail label.f12px").hide();
                $(".formerr_email").show().find(".warning").html("<span class=\"error\"></span>请输入邮箱");
                return false;
            }
            if(thisval=''||!shijian.test(thisval)){
                $(".entryemail label.f12px").hide();
                $(".formerr_email").show().find(".warning").html("<span class=\"error\"></span>邮箱错误");
                return false;
            }
            var oldEmail = $("#oldEmail").val();
            if (email != oldEmail) {
                $(".formerr_email").show().find(".warning").html("<span class=\"error\"></span>旧邮箱错误");
                return false;
            }
            $(".formerr_sendemail").show();
            sendEmailCode();
        });

        $(".thenext_2").click(function(){
            var code = $(".email_yzminput").val();
            if (code == "") {
                $(".formerr_yzm").show().find(".warning").html("<span class=\"error\"></span>请输入验证码");
                return false;
            }
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/sendBindEmailSubmit?" + new Date().getTime(),
                data:{userName:"", email:email, code:code, time:time, type:"unbind"},
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".emailbox_3").show().siblings(".emailbox").hide();
                    } else {
                        $(".formerr_yzm").show().find(".warning").html("<span class=\"error\"></span>验证码错误");
                    }
                }
            });
        });

        $(".emailbox .textinput").focus(function(){
            $(this).siblings(".formerr").hide();
        });
    });

    function sendEmailCode(){
        tagSend.unbind("click");
        var tDelay = 60;
        var fnDelay = function(){
            if (tDelay < 0) {
                tagSend.text("重新获取验证码");
                tagSend.bind("click", sendEmailCode);
            } else {
                tagSend.text("(" + tDelay + "秒)重新获取验证码");
                tDelay --;
                setTimeout(function(){fnDelay();},1000);
            }
        };
        fnDelay();

        time = new Date().getTime();
        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"/sendBindEmail?" + new Date().getTime(),
            data:{userName:"", email:email, time:time, type:"unbind"},
            async:false,
            success:function (msg) {
                if (msg.code > 0) {
                    $(".emailbox_2").show().siblings(".emailbox").hide();
                    $(".anquantishi .b").html(email);
                    $(".anquantishi .two").html(email);
                } else {
                    $(".formerr_sendemail").hide();
                    alert("发送失败，请稍后再操作。");
                }
            }
        });
    }
</script>

