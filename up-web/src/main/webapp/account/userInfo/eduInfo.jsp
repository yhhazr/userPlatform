<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-21
  Time: 上午10:05
  教育信息
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

    .form-group .form-select-adjust {
        margin-left: 3px;
        padding: 5px 0;
        vertical-align: middle;
        height: 28px;
        line-height: 28px;
    }

</style>


<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">添加教育信息</h2>

    <div class="blank15"></div>
    <div style="margin-top:50px;">
        <form action="">

            <div class="form-group">
                <label for="schoolType" class="form-label">学校类型:</label><input type="hidden" id="userSchoolType"
                                                                               value="${userObject.schoolType}">
                <select name="schoolType" id="schoolType" class="form-select-adjust">
                    <option value="0">请选择</option>
                    <option value="4">大学</option>
                    <option value="3">高中</option>
                    <option value="2">初中</option>
                    <option value="1">小学</option>
                    <option value="100">其它</option>
                </select>

            </div>
            <div class="form-group">
                <label class="form-label">入学年份:</label><input type="hidden" id="startEduYear"
                                                              value="${userObject.startEduYear}">

                <select name="starSchoolYear" id="starSchoolYear" class="form-select-adjust">
                    <option value="请选择" disabled="">请选择</option>
                    <option value="2010">2010</option>
                </select>
            </div>
            <div class="form-group">
                <label for="college" class="form-label">学校名称:</label>
                <input type="text" name="college" class="textinput" id="college" size="20"   maxlength="20"
                       value="${userObject.schoolName}" onfocus="if(value =='请输入学校名称'){value =''}"
                       onblur="if (value ==''){value='请输入学校名称'}"> <span id="schoolNameMsg"></span>
            </div>
            <div class="form-group">
                <label class="form-label">教育程度:</label><input type="hidden" id="userEduLevel"
                                                              value="${userObject.eduLevel}">
                <select name="edu_status" id="edu_status" class="form-select-adjust">
                    <option value="0">请选择</option>
                    <option value="5">大专</option>
                    <option value="6">本科</option>
                    <option value="7">硕士</option>
                    <option value="8">博士</option>
                    <option value="4">中专</option>
                    <option value="3">中技</option>
                    <option value="2">高中</option>
                    <option value="1">初中</option>
                    <option value="100">其他</option>
                </select>
            </div>
            <div style="margin-left: 265px;">
                <span class="save_a save_a1" onclick="modifyEduInfo()">保 存</span>
                <span id="saveMsg_eduInfo" style="display: none;" class="save_ok save_ok1">信息修改成功!</span>
            </div>
        </form>

    </div>
    <%--<div class="set_mobleok none">--%>
        <%--<div class="anquandun emaildun"></div>--%>
        <%--<strong class="f16px left setsj_ok">信息修改成功！</strong>--%>

        <%--<div class="blank"></div>--%>
        <%--<div class="edit_email">--%>
            <%--&lt;%&ndash;<a href="javascript:void(0)"&ndash;%&gt;--%>
            <%--&lt;%&ndash;onclick="forword('/nav','index')" title="返回首页" class="editemail_a"></a>&ndash;%&gt;--%>
        <%--</div>--%>
        <%--<div class="blank20"></div>--%>
        <%--<div class="blank20"></div>--%>
        <%--<div class="blank20"></div>--%>
    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--<div class="blank20"></div>--%>
<%--</div>--%>
<script type="text/javascript">
    $('document').ready(function () {
        initSchoolYear();

    });

    function initSchoolYear() {
        var date = new Date();
        $("#starSchoolYear").empty();
        for (var i = parseInt(date.getFullYear()); i >= 1900; i--) {
            $("#starSchoolYear").append("<option value='" + i + "'>" + i + "</option>");
        }
        var startEduYear = $("#startEduYear").val();
        var schoolType = $("#userSchoolType").val();
        var eduLevel = $("#userEduLevel").val();
        var schoolName = $("#college").val();
        if (!schoolType) {
            $("#schoolType").val(0);
        }
        else {
            $("#schoolType").val(schoolType);
        }
        if (!eduLevel) {
            $("#edu_status").val(0);
        }
        else {
            $("#edu_status").val(eduLevel);
        }
        if (!startEduYear) {
            $("#starSchoolYear").prepend("<option value='0'>请选择</option>").val(0);
        }
        else {
            $("#starSchoolYear").prepend("<option value='0'>请选择</option>");
            $("#starSchoolYear").val(startEduYear);
        }
        if (!schoolName) {
            $("#college").val("请输入学校名称");
        }
        else {
            $("#college").val(schoolName);
        }


    }

    function modifyEduInfo() {
        $("#schoolNameMsg").empty();
        var eduLevel = $("#edu_status").val();
        var schoolType = $("#schoolType").val();
        var schoolName = $("#college").val();
        var startEduYear = $("#starSchoolYear").val();

        if (schoolName && schoolName.length > 20) {
            $("#schoolNameMsg").html("  学校名称长度20字符以内！").css("color","red");
        }
        else
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/AccountInfoSubmit",
                data:{type:"edu", userName:$("#userName").val(), eduLevel:eduLevel, schoolType:schoolType, schoolName:schoolName, startEduYear:startEduYear},
                beforeSend:function (XMLHttpRequest, textStatus) {

                },
                success:function (msg) {
                    if (msg.code == 200) {
                        $("#saveMsg_eduInfo").show();
                        setTimeout(function () {
                            $("#saveMsg_eduInfo").fadeOut(2000);
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
        return false;
    }


</script>
