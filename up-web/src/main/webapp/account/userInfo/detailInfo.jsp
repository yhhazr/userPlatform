<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-21
  Time: 上午10:04
  详细信息页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        margin-left: 3px;
        vertical-align: middle;
        height: 28px;
        line-height: 28px;
    }

    .form-group .form-check-adjust {
        margin-left: 8px;
        vertical-align: middle;
        height: 28px;
        line-height: 28px;
    }
</style>

<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">添加详细资料</h2>

    <div class="blank15"></div>
    <div style="margin-top:50px;">

        <form action="">
            <div class="form-group">
                <label class="form-label">婚姻状态:</label><input type="hidden" id="isUserMarried"
                                                              value="${userObject.marryStatus}">
                <input type="radio" name="ismarried" value="0" checked="checked" class="form-radio-adjust"> 未婚
                <input type="radio" name="ismarried" value="1" class="form-radio-adjust"> 已婚

            </div>
            <div class="form-group">
                <label class="form-label" style="float: left;">个人爱好:</label>
                <input type="hidden" id="userHobby" value="${userObject.hobby}">

                <div style="display: inline-block;">
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="音乐"> 音乐
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="旅游"> 旅游
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="摄影"> 摄影
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="时尚"> 时尚
                    <br>
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="美食"> 美食
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="游戏"> 游戏
                    <input name="hobby" type="checkbox" class="form-check-adjust" value="宠物"> 宠物
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">当前职业:</label>
                <input type="hidden" id="userJob" value="${userObject.career}">
                <select name="job" style="margin-left:3px;" id="job">
                    <option value="0" selected="selected">--请选择--</option>
                    <option value="计算机">计算机</option>
                    <option value="邮电通信">邮电通信</option>
                    <option value="银行,金融,房地产">银行,金融,房地产</option>
                    <option value="建筑">建筑</option>
                    <option value="制造业">制造业</option>
                    <option value="商业，零售，批发">商业，零售，批发</option>
                    <option value="教育，学术研究">教育，学术研究</option>
                    <option value="医疗保健">医疗保健</option>
                    <option value="文艺，影视，娱乐，体育">文艺，影视，娱乐，体育</option>
                    <option value="广告，媒体，出版">广告，媒体，出版</option>
                    <option value="宾馆，餐饮，旅游">宾馆，餐饮，旅游</option>
                    <option value="国家机构，行政机关">国家机构，行政机关</option>
                    <option value="学生">学生</option>
                    <option value="其他">其他</option>
                </select>
            </div>
            <div style="margin-left: 265px;">
                <span class="save_a" href="#" onclick="modifyDetailInfo();">保 存</span>
                <span id="saveMsg_detailInfo" style="display: none;float: left;"  class="save_ok save_ok2">信息修改成功!</span>
            </div>
        </form>

    </div>
    <div class="blank20"></div>
</div>
<script type="text/javascript">
    $('document').ready(function () {
        var marryStatus = $("#isUserMarried").val();
        var userHobby = $("#userHobby").val();
        var userJob = $("#userJob").val();
        if (!marryStatus) {
        }
        else {
            $("input[name=ismarried][value=" + marryStatus + "]").attr("checked", true);//value=34的radio被选中
        }
        if (!userHobby) {
        }
        else {
            var hobbyArr = userHobby.split(',');
            for (var i = 0; i < hobbyArr.length; i++) {
                $("input[name=hobby][value=" + hobbyArr[i] + "]").attr("checked", true);//value=34的radio被选中
            }
        }
        if (!userJob) {
            $("#job").val(0);
        }
        else {
            $("#job").val(userJob);
        }
    });


    function modifyDetailInfo() {
        var marryStatus = $("input[name='ismarried']:checked").val();
        var myHobby = "";
        $('input[name="hobby"]:checked').each(function () {
            myHobby += $(this).val() + ",";
        });
        var career = $("#job").val();
        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"/AccountInfoSubmit",
            data:{type:"detail", userName:$("#userName").val(), marryStatus:marryStatus, hobby:myHobby, career:career},
            beforeSend:function (XMLHttpRequest, textStatus) {

            },
            success:function (msg) {
                if (msg.code == 200) {
                    $("#saveMsg_detailInfo").show();
                    setTimeout(function(){
                        $("#saveMsg_detailInfo").fadeOut(2000);
                    },1000);
                }
                else {
                    alert(msg.msg);
                }
            },
            error:function (msg) {
                window.location.href = "/login.html";
            }
        });
        return false;
    }


</script>
