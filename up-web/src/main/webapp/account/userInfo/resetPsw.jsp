<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-20
  Time: 下午3:36
  修改密码
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="topline"></div>
<div class="rgtcontent">
    <div class="yiwang f14px bottomline">更改你的密码</div>

    <div class="editpass">
        <div class="relative editpass_input">
            <label class="red">*</label>
            <label>旧 密 码：</label>
            <input type="password" class="textinput" id="old_pwd" maxlength="20"/>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>请输入旧密码</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>原密码错误</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入旧密码(6~20位)</div>
            </div>
        </div>
        <div class="relative editpass_input">
            <label class="red">*</label>
            <label>新 密 码：</label>
            <input type="password" class="textinput" id="new_pwd" maxlength="20"/>
            <div class="pass_lv"></div>
            <div class="pass_lv2"></div>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>由6-20位字母、数字、符号组成</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>密码错误</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
            <div class="warning"><span class="error"></span>请输入新密码(6~20位)非空格</div>
        </div>
            <div class="formerr formerr5">
                <div class="warning"><span class="error"></span>密码不能为9位以下纯数字</div>
            </div>
        </div>
        <div class="relative editpass_input">
            <label class="red">*</label>
            <label>确认密码：</label>
            <input type="password" class="textinput" id="confirm_pwd" maxlength="20"/>
            <div class="formerr formerr1">
                <div class="warning"><span class="prompt"></span>请输入确认密码</div>
            </div>
            <div class="formerr formerr2">
                <div class="warning"><span class="error"></span>请输入和上面相同的密码</div>
            </div>
            <div class="formerr formerr3"><span class="ok"></span></div>
            <div class="formerr formerr4">
                <div class="warning"><span class="error"></span>请输入确认密码(非空格)</div>
            </div>
        </div>
        <a href="javascript:void(0)" title="保存"  id="resetPswUser" class="save_a thenext_2" style="margin-left: 150px;">保存</a>
    </div>


    <div class="set_mobleok none">
        <div class="anquandun emaildun"></div>
        <strong class="f16px left setsj_ok">密码修改成功！</strong>
        <div class="blank"></div>
        <div class="edit_email">
            <a href="javascript:void(0)"
               onclick="forword('/nav','index')" title="返回首页" class="editemail_a"></a>
        </div>
        <div class="blank20"></div>
        <div class="blank20"></div>
        <div class="blank20"></div>
    </div>
    <div class="blank20"></div>
</div>
 <script type="text/javascript">

     $(function(){
         $(".textinput").focusin(function(){
             $(this).addClass("textinput_hover");
             $(this).siblings(".formerr").hide()
             $(this).siblings(".formerr1").show();
         });
         $("#old_pwd").focusout(function(){
             var old_pwd=$.trim($("#old_pwd").val());
             $("#old_pwd").removeClass("textinput_hover");
             if(old_pwd.length>5)
             {
                 $(this).siblings(".formerr").hide()
                 $(this).siblings(".formerr3").show();
             }
             else
             {
                 $(this).siblings(".formerr").hide()
                 $(this).siblings(".formerr4").show();
             }
         });

         $("#new_pwd").focusout(function(){
             $(this).removeClass("textinput_hover")
             var passvalue = $.trim($(this).val());
             var passvalueChar = check(passvalue);
             if(passvalue.length<6||passvalue.length>20){
                 $(this).siblings(".formerr").hide();
                 $(this).siblings(".formerr2_1").show();
                 $(this).siblings(".formerr4").show();
             }else if(passvalueChar==1 && (5<passvalue.length&&passvalue.length<10)){
                 $(this).siblings(".formerr").hide();
                 $(this).siblings(".formerr2_2").show();
                 $(this).siblings(".formerr3").show();

             }else{
                 $(this).siblings(".formerr").hide();
                 $(this).siblings(".formerr3").show();
             }

         })
         $("#confirm_pwd").focusout(function(){
             $(this).removeClass("textinput_hover")
             var passvalue = $.trim($("#new_pwd").val());
             if(! $.trim($(this).val()))
             {
                 $(this).siblings(".formerr").hide();
                 $(this).siblings(".formerr4").show();
             }
             else
             {
                 if( $.trim($(this).val())!=passvalue){
                     $(this).siblings(".formerr").hide();
                     $(this).siblings(".formerr2").show();
                 }else{
                     $(this).siblings(".formerr").hide();
                     $(this).siblings(".formerr3").show();
                 }
             }
         })

         $("#resetPswUser").click(function(){
             modifyPassword($.trim($("#userName").val()));
         });

         $("#new_pwd").keyup(function(){
             //判断密码强度
             var passvalue =  $.trim($(this).val());
             if(passvalue.length>5){
                 var passvalueChar = check(passvalue);
                 if(passvalueChar==1){
                     $(".pass_lv2").css({"width":"5px","right":"265px"})
                 }else if(passvalueChar==2){
                     $(".pass_lv2").css({"width":"10px","right":"260px"})
                 }else if(passvalueChar==10){
                     $(".pass_lv2").css({"width":"15px","right":"255px"})
                 }else if(passvalueChar==3){
                     $(".pass_lv2").css({"width":"20px","right":"250px"})
                 }else if(passvalueChar==11){
                     $(".pass_lv2").css({"width":"25px","right":"245px"})
                 }else if(passvalueChar==12){
                     $(".pass_lv2").css({"width":"30px","right":"240px"})
                 }else if(passvalueChar==13){
                     $(".pass_lv2").css({"width":"35px","right":"235px"})
                 };

             }

         })
     });

     function modifyPassword(userName) {
         var oldPwd = $("#old_pwd");
         var newPwd = $("#new_pwd");
         var newPwdr = $("#confirm_pwd");
         if ($.trim(oldPwd.val()).length < 1 ||$.trim( newPwd.val()).length < 1 || $.trim(newPwdr.val()).length < 1) {
             if ($.trim(oldPwd.val()).length < 1)
             {
                 $("#old_pwd").siblings(".formerr").hide();
                 $("#old_pwd").siblings(".formerr4").show();
             }

             if ($.trim(newPwd.val()).length < 1)
             {
                 $("#new_pwd").siblings(".formerr").hide();
                 $("#new_pwd").siblings(".formerr4").show();
             }
             if ($.trim(newPwdr.val()).length < 1)
             {
                 $("#confirm_pwd").siblings(".formerr").hide();
                 $("#confirm_pwd").siblings(".formerr4").show();
             }
             return false;
         }
         else if ($.trim(newPwd.val()).length < 6 || $.trim(newPwd.val()).length > 20) {
             newPwd.siblings(".formerr").hide();
             newPwd.siblings(".formerr2_1").show();
             newPwd.siblings(".formerr4").show();
             return false;
         }
         else if ($.trim(newPwd.val()).length !=$.trim( newPwdr.val()).length) {
             newPwdr.siblings(".formerr").hide();
             newPwdr.siblings(".formerr2").show();
             return false;
         }
         else {
             $.ajax({
                 type:"POST",
                 dataType:"JSON",
                 url:"/PasswordModifySubmit",
                 data:{"userName":userName, "oldPwd":$.trim(oldPwd.val()), "newPwd":$.trim(newPwd.val()), "newPwdr":$.trim(newPwdr.val())},
                 beforeSend:function (XMLHttpRequest, textStatus) {

                 },
                 success:function (msg) {
                     if (msg.code== 200) {
                         $(".set_mobleok").show();
                         $(".editpass").hide();
                     }
                     if(msg.code==203)
                     {
                         newPwd.siblings(".formerr").hide();
                         newPwd.siblings(".formerr2_1").show();
                         newPwd.siblings(".formerr5").show();
                     }
                     if(msg.code==204) {
                         $('#new_pwd').parent().siblings(".formerr").hide();
                         $('#new_pwd').parent().siblings(".formerr4").empty().
                                 addClass("warning").append("<span class='error'></span>" + msg.msg).show();
                     }
                     if(msg.code==205)
                     {
                         oldPwd.siblings(".formerr").hide()
                         oldPwd.siblings(".formerr2").show();
                     }

                 },
                 error:function (msg) {
                     msgError("未知请求或者不能连接上服务器");
                 }
             });
             return false;
         }
     }


 </script>