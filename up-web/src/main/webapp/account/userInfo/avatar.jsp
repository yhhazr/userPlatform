<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-21
  Time: 上午10:09
  修改头像
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
        margin-left: 1px;
        margin-top: 5px;
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

    .pbtn1 {
        border: solid 2px #dcdcdc;
        padding: 4px 14px 4px 34px;
        color: #959595;
    }

    .btn1 {

        background-color: #f9f9f9;
    }
    .btn2 {
        margin-left: 1px;
        margin-top: 10px;
    }

</style>

<div class="topline"></div>
<div class="rgtcontent">
    <h2 class="h2_title">修改头像</h2>

    <div style="margin-top:10px;">
        <div class="btn2">
            <div>
                <div name="avater"
                     style="zoom:1">
                    <input type="hidden" name="userId" value="">
                    <table summary="修改头像" cellspacing="0" cellpadding="0" class="formtable" style="margin-top: 5px;">
                        <tbody>
                        <tr>
                            <th style="vertical-align:top; padding-top:20px;">
                                <div class="avatararea" style="cursor: pointer;">
                                    <p style="position:relative;"><img id="avatar" style="height: 80px;width: 80px;"
                                                                       src="${userObject.headDir}"
                                            >
                                        <br>
                                        <input style="color: green; background: none;"
                                               id="editHead" type="button" value="修改头像">
                                    </p>
                                </div>
                            </th>
                            <td id="boxShow" style="vertical-align:top; padding-top:20px;">
                                <div id="avatarctrl"
                                     style="width:300px;height:300px;overflow:hidden;border-style:ridge;background-image: url('/img/loadTip.jpg')">
                                    <img id='editImg' style="width:300px;height:300px;repeat:no-repeat;" src=""/>
                                    <br>
                                    <input type="hidden" id="x" name="x"/>
                                    <input type="hidden" id="y" name="y"/>
                                    <input type="hidden" id="x2" name="x2"/>
                                    <input type="hidden" id="y2" name="y2"/>
                                    <input type="hidden" id="w" name="w"/>
                                    <input type="hidden" id="h" name="h"/>

                                    <!-- 源图片宽度和高度 -->
                                    <input type="hidden" id="width" name="width" value=""/>
                                    <input type="hidden" id="height" name="height" value=""/>
                                    <input type="hidden" id="oldImgPath" name="oldImgPath" value=""/>
                                    <input type="hidden" id="imgFileExt" name="imgFileExt" value=""/>
                                </div>
                                <div class="form-group zoom" style="display:none;">
                                    <span class="save_a left" id='saveHead'>保存</span>
                                        <span class="theprev left" style="margin:30px 0 0 10px;"
                                              onclick="forword('/nav','avatar')">取消</span>

                                </div>
                            </td>
                            <td id="bigBox" width="200px" align="center"><span>预览</span><br>

                                <div style="width:80px;height:80px;overflow:hidden;">

                                    <img style="height: 80px;width: 80px;"
                                         src="${userObject.headDir}" id="preview1" alt="Preview" class="jcrop-preview"
                                            >
                                </div>

                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div id="uploadUserHead" style="display: none;">
                    <form id="uploadFrom" action="avatar?type=first" method="post"
                          enctype="multipart/form-data">
                        图片说明：<span>图片大小1M以内，格式仅限png,jpg,gif</span><br> <br>
                        选择图片：<input type="file" id="uploadImg" name="uploadImg" size="20" onchange="">
                        <br> <br>
                        <img src="" id="fileChecker" style="display: none" alt="test"  height="180"/>
                        <div style="text-align: center;">
                            <input type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                                   onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                                   style="border-color: rgb(220, 220, 220); background:url('/img/ok.gif') no-repeat 8px center ;">
                            <span id="uploadMsg" style="color: red;"></span>
                        </div>
                    </form>

                </div>

            </div>
        </div>
        <div class="prompting ml_20 mt_15">
            <%--<div class="sub_prompting dengpao">头像修改后，我们将在48小时内重新审核你的照片。</div>--%>
        </div>

    </div>
    <div class="blank20"></div>

</div>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery.form.js"></script>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery.Jcrop.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery.fancybox.js"></script>
<script type="text/javascript">
    $("#avatar").bind("click", function () {
        showUploadDiv();
    });
    $("#editHead").bind("click", function () {
        showUploadDiv();
    });
    function showUploadDiv() {
         var editSrc=$("#editImg").attr("src");
        if(editSrc)
        {
            alert('您已经选择了图片，请先取消！');
        }
        else
        {
            $("#uploadMsg").empty();
            $.fancybox({
                type:'inline',
                width:400,
                height:250,
                href:'#uploadUserHead'
            });
        }


    }
    $(function () {
        $("#uploadFrom").ajaxForm({
            beforeSubmit:checkImg,
            error:function(data,status){
                alert(status+' , '+data);
                $("#uploadMsg").html('上传文件超过1M!');
            },
            success:function (data,status) {
              try{
                  var msg = $.parseJSON(data);
                  if (msg.code == 200)
                  { //如果成功提交
                      javascript:$.fancybox.close();
                      $("#uploadUserHead").hide();
                      var data = msg.object;
                      $("#editImg").attr("src", data.path).show();
                      $("#preview1").attr("src", data.path).show();
                      $(".zoom").show();
                      $("#width").val(data.width);
                      $("#height").val(data.height);
                      $("#oldImgPath").val(data.realPath);
                      $("#imgFileExt").val(data.fileExt);
                      var api, jcrop_api, boundx, boundy;
                      $('#editImg').Jcrop({
                          onChange:updatePreview,
                          onSelect:updatePreview,
                          aspectRatio:1,
                          bgOpacity:0.5,
                          bgColor:'white',
                          addClass:'jcrop-light'
                      }, function () {
                          api = this;
                          api.setSelect([130, 65, 130 + 350, 65 + 285]);
                          api.setOptions({ bgFade:true });
                          api.ui.selection.addClass('jcrop-selection');
                          var bounds = this.getBounds();
                          boundx = bounds[0];
                          boundy = bounds[1];
                          jcrop_api = this;
                      });
                      function updatePreview(c) {
                          if (parseInt(c.w) > 0) {
                              var rx = 80 / c.w;
                              var ry = 80 / c.h;
                              $('#preview1').css({
                                  width:Math.round(rx * boundx) + 'px',
                                  height:Math.round(ry * boundy) + 'px',
                                  marginLeft:'-' + Math.round(rx * c.x) + 'px',
                                  marginTop:'-' + Math.round(ry * c.y) + 'px'
                              });
                          }
                          jQuery('#x').val(c.x);
                          jQuery('#y').val(c.y);
                          jQuery('#x2').val(c.x2);
                          jQuery('#y2').val(c.y2);
                          jQuery('#w').val(c.w);
                          jQuery('#h').val(c.h);
                      }
                  }
                  if (msg.code == 204) {
                      $("#uploadMsg").html(msg.msg);
                  }
              }catch (e){
                  $("#uploadMsg").html('上传文件超过1M!');
              }

            }
        });
    });

    $("#saveHead").bind("click", function () {
        var width = $("#width").val();
        var height = $("#height").val();
        var oldImgPath = $("#oldImgPath").val();
        var imgFileExt = $("#imgFileExt").val();
        var x = $('#x').val();
        var y = $('#y').val();
        var w = $('#w').val();
        var h = $('#h').val();

        $.ajax({
            url:'/imgCrop',
            type:'post',
            data:{x:x, y:y, w:w, h:h,oldImgName:oldImgPath},
            datatype:'json',
            success:function (msg) {
                if (msg.code == 200) {
                    forword('/nav', 'index');
                }
                else {
                    alert(msg.msg);
                }
            }
        });
    });
    function checkImg() {
        //限制上传文件的大小和后缀名
        var filePath = $("input[name='uploadImg']").val();
        if (!filePath) {
            $("#uploadMsg").html("请选择上传文件!").show();
            return false;
        }
        else {
            var extStart = filePath.lastIndexOf(".");
            var ext = filePath.substring(extStart, filePath.length).toUpperCase();
            if (ext != ".PNG" && ext != ".GIF" && ext != ".JPG") {
                $("#uploadMsg").html("图片限于png,gif,jpg格式!").show();
                return false;
            }
        }
        return true;
    }
    var oFileChecker = document.getElementById("fileChecker");
    function changeSrc(filePicker)
    {
        if(window.addEventListener){ // FF
            oFileChecker.src = document.getElementById('uploadImg').files[0].getAsDataURL();
            oFileChecker.addEventListener('load', function(){isFFCompleted(document.getElementById('uploadImg').files[0]);}, false);

        }else{ // IE
            oFileChecker.src = filePicker.value;
            oFileChecker.attachEvent("onreadystatechange", function(){isIECompleted(oFileChecker);});
        }
    }

    function isIECompleted(fileObj){
        if (oFileChecker.readyState == "complete")
        {
            checkSize(fileObj);
        }
    }

    function isFFCompleted(fileObj){

        if ( oFileChecker.complete )
        {
            checkSize(fileObj);
        }

    }

    function checkSize(fileObj)
    {

        var limit  = 500 * 1024;

        if (fileObj.fileSize > limit)
        {
            alert("文件太大");

        }
        else
        {
            alert("文件正确");
        }
    }
</script>
