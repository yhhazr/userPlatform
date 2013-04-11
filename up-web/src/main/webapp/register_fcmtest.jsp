<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-10-16
  Time: 下午12:02
 防沉迷注册页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>防沉迷注册</title>
</head>
<body>
<div>
    用户名： <input type="text" maxlength="20" id="uName" name="uName"><br>
    密码： <input type="text" maxlength="20" id="uPsw" name="uPsw"><br>
    确认密码： <input type="text" maxlength="20" id="uPswConfirm" name="uPswConfirm"><br>
    邮箱： <input type="text" maxlength="20" id="uEmail" name="uEmail"><br>
    验证码： <input type="text" maxlength="20" id="code" name="code"><br>
    <img id="log_verifyCodeImgReg"
         style="vertical-align:middle;cursor:pointer;padding-left:5px"
         src="" width="58px" height="25px"
         onClick="javascript:refleshVerifyCodeReg();"><a
        href="javascript:refleshVerifyCodeReg();">看不清？换一张</a> <br>
    时间戳： <input type="text" maxlength="20" id="timeStr" name="timeStr" disabled="true">
    <hr>
    真实姓名： <input type="text" maxlength="20" id="rName" name="rName"><br>
    身份证号码： <input type="text" maxlength="20" id="uIcn" name="uIcn"><br>
    <input type="button" id="registerFcm" value="防沉迷注册">
</div>
</body>
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript">
    function refleshVerifyCodeReg() {
        var timeStr = (new Date().getTime());
        $("#log_verifyCodeImgReg").attr("src", '/verifyCode?time=' + timeStr);
        $("#timeStr").val(timeStr);
    }
    $(function () {
        refleshVerifyCodeReg();
        $("#registerFcm").bind("click", function () {
            var _name = $("#uName").val();
            var _psw = $("#uPsw").val();
            var _pswC = $("#uPswConfirm").val();
            var _email = $("#uEmail").val();
            var _rName = $("#rName").val();
            var _icn = $("#uIcn").val();
            var _code = $("#code").val();
            var _timeStr = $("#timeStr").val();

            $.ajax({
                url:'/register2',
                type:'post',
                data:{"uName":_name,"uPsw":_psw,"uPswConfirm":_pswC,"uEmail":_email,"rName":_rName,"uIcn":_icn,"code":_code,"timeStr":_timeStr},
                dataType:'json',
                success:function(data){
                    alert(data.code+" ,"+data.msg);
                    if(data.code!=200)
                    {
                        refleshVerifyCodeReg();
                    }
                },
                error:function(){
                    alert('连接服务器错误!');
                }
            });
        });
    });

</script>
</html>