<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-21
  Time: 上午10:08
  工作信息
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
    <h2 class="h2_title">添加工作信息</h2>

    <div class="blank15"></div>
    <div style="margin-top:50px;">
        <form action="">
            <div class="form-group">
                <label for="working_place" class="form-label">单位名称:</label>
                <input type="text" id="working_place" name="working_place" class="textinput"
                       value="${userObject.companyName}" id="college" size="20" maxlength="51">
                <span id="working_placeMsg"></span>
            </div>
            <div class="form-group">
                <label class="form-label">工作时间:</label> <input type="hidden" value="${userObject.workEndYear}" id="workEndYear">
                <select name="work_time_start" id="work_time_start" class="form-select-adjust">
                    <option value="2000">2000</option>
                    <option value="2001">2001</option>
                </select>
                至 <input type="hidden" value="${userObject.workStartYear}" id="workStartYear">
                <select name="work_time_end" id="work_time_end" class="form-select-adjust">
                    <option value="2011">2011</option>
                    <option value="2012">2012</option>
                </select>
                <span id="working_yearMsg"></span>
            </div>
            <div class="form-group">
                <label for="job_position" class="form-label">部门/职位:</label>
                <input type="text" name="job_position" class="textinput"
                       value="${userObject.workPost}" id="job_position" size="20" maxlength="21">
                <span id="job_positionMsg"></span>
            </div>
            <div style="margin-left: 265px;">
                <span class="save_a" id="workInfo_save" onclick="modifyWorkInfo()">保 存</span>
                <span id="saveMsg_workInfo" style="display: none;float: left;"  class="save_ok save_ok1">信息修改成功!</span>
            </div>
        </form>
    </div>
    <div class="blank20"></div>
</div>
<script type="text/javascript" src="../scripts/jquery-1.7.1.js"></script>
<script type="text/javascript">

    $('document').ready(function () {
        initWorkYear();
        $("#work_time_start").change(function () {
            var checkValue = $("#work_time_start").val();
            var date = new Date();
            $("#work_time_end").empty();
            for (var i = parseInt(date.getFullYear()); i >= checkValue; i--) {
                $("#work_time_end").append("<option value='" + i + "'>" + i + "</option>");
            }
        });

    });

    function initWorkYear() {
        var workStartYear=$("#workStartYear").val();
        var workEndYear=$("#workEndYear").val();
            var date = new Date();
            $("#work_time_start").empty();
            $("#work_time_end").empty();
            for (var i = parseInt(date.getFullYear()); i >= 1900; i--) {
                $("#work_time_start").append("<option value='" + i + "'>" + i + "</option>");
                $("#work_time_end").append("<option value='" + i + "'>" + i + "</option>");
            }
        if(!workStartYear)
        {
            $("#work_time_start").prepend("<option value='0'>请选择</option>").val(0);
            $("#work_time_end").prepend("<option value='0'>请选择</option>").val(0);
        }
        else
        {
            $("#work_time_start").val(workStartYear);
            $("#work_time_end").val(workEndYear);
        }
    }

    function modifyWorkInfo() {
        $("#job_positionMsg").empty();
        $("#working_placeMsg").empty();
        var companyName = $("#working_place").val();
        var workStartYear = $("#work_time_start").val();
        var workEndYear = $("#work_time_end").val();
        var workPost = $("#job_position").val();

        if((companyName&&companyName.length>50)||(workPost&&workPost.length>20)||parseInt(workStartYear)>parseInt(workEndYear))
        {
           if(companyName&&companyName.length>50)
           {
               $("#working_placeMsg").html("  单位名称长度50字符以内！").css("color","red");
           }
            if(workPost&&workPost.length>20)
            {
                $("#job_positionMsg").html("  职位长度20字符以内！").css("color","red");
            }
            if(parseInt(workStartYear)>parseInt(workEndYear))
            {
                $("#working_yearMsg").html("  请选择工作开始年份！").css("color","red");
            }
        }
        else
        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"/AccountInfoSubmit",
            data:{type:"work", userName:$("#userName").val(), companyName:companyName, workStartYear:workStartYear, workEndYear:workEndYear, workPost:workPost},
            beforeSend:function (XMLHttpRequest, textStatus) {

            },
            success:function (msg) {
                if (msg.code == 200) {
                    $("#working_yearMsg").empty();
                    $("#working_placeMsg").empty();
                    $("#job_positionMsg").empty();
                    $("#saveMsg_workInfo").show();
                    setTimeout(function(){
                        $("#saveMsg_workInfo").fadeOut(2000);
                    },1000);
                }
                else {
                    alert(msg.msg);
                }
            },
            error:function (msg) {
                window.location.href="/login.html";
            }
        });
        return false;
    }


</script>


