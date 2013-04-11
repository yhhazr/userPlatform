<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-10
  Time: 下午2:40
  上传编辑头像页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link href="/css/jquery.Jcrop.min.css" rel="stylesheet" type="text/css"/>
    <link href="/css/jquery.fancybox.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div>
    <div name="avater"
         style="zoom:1">
        <input type="hidden" name="userId" value="">

        <div class="header">
            <h1 class="title">
                帐号信息
                <span class="primaryColor marginLeft5">&gt;</span>
                <span class="marginLeft5 blankColor">修改头像</span>
            </h1>
        </div>
        <table summary="修改头像" cellspacing="0" cellpadding="0" class="formtable">
            <tbody>
            <tr>
                <th style="vertical-align:top; padding-top:20px;">
                    <div class="avatararea">
                        <p><img id="avatar"
                                src="/img/geren_right_01.jpg"
                                >
                            <br>
                            <input id="editHead" type="button" value="修改头像">
                        </p>
                    </div>
                </th>
                <td id="boxShow" style="vertical-align:top; padding-top:20px;">
                    <div id="avatarctrl" style="width:300px;height:300px;overflow:hidden;">
                        <img id='editImg' src="/img/geren_right_01.jpg"/>
                        <br>
                        <input type="button" value="保存头像" id="saveHead" style="display: none;">
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
                </td>
                <td id="bigBox" width="200px" align="center"><span>大图</span><br>

                    <div style="width:180px;height:180px;overflow:hidden;">

                        <img src="/img/geren_right_01.jpg" id="preview1" alt="Preview" class="jcrop-preview"
                             height="180px" width="180px">
                    </div>

                </td>
                <td id="midBox" width="100px" align="center">
                    <span>中图</span><br>

                    <div style="width:50px;height:50px;overflow:hidden;">
                        <img src="/img/geren_right_01.jpg" id="preview2" height="50px" width="50px"
                             class="jcrop-preview">
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="uploadUserHead" style="display: none;">
        <form id="uploadFrom" action="avatar?type=first" method="post"
              enctype="multipart/form-data">
            图片说明：<span>图片大小1M以内，图片格式仅限png,jpg,gif</span><br> <br>
            选择图片：<input type="file" id="cs_photoInfoText" name="cs_photoInfoText" size="25">
            <br> <br>

            <div style="text-align: center;">
                <input type="submit" value="提 交" onmouseover="this.style.borderColor='#75cd02'"
                       onmouseout="this.style.borderColor='#dcdcdc'" class="btn1 pbtn1"
                       style="border-color: rgb(220, 220, 220); background:url('/img/ok.gif') no-repeat 8px center ;">
            </div>
        </form>

    </div>

</div>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery.form.js"></script>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery.fancybox.js"></script>
<script type="text/javascript" charset="UTF-8" src="/scripts/jquery.Jcrop.min.js"></script>
<script type="text/javascript">
    $("#editHead").bind("click", function () {
        $.fancybox({
            type:'inline',
            width:400,
            href:'#uploadUserHead'
        });
    });
    $(function () {
        $("#uploadFrom").ajaxForm({
            success:function (msg) {
                if (msg.code == 200) { //如果成功提交
                    var data = msg.object;
//                    jcrop_api.setImage(data.path);
                    $("#editImg").attr("src", data.path).show();
                    $("#preview1").attr("src", data.path).show();
                    $("#preview2").attr("src", data.path).show();
                    $("#preview3").attr("src", data.path).show();
                    $("#saveHead").show();
                    $("#width").val(data.width);
                    $("#height").val(data.height);
                    $("#oldImgPath").val(data.path);
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
                        // Store the API in the jcrop_api variable
                        jcrop_api = this;
                    });
                    function updatePreview(c) {
                        if (parseInt(c.w) > 0) {
                            var rx = 180 / c.w;
                            var ry = 180 / c.h;
                            $('#preview1').css({

                                width:Math.round(rx * boundx) + 'px',
                                height:Math.round(ry * boundy) + 'px',
                                marginLeft:'-' + Math.round(rx * c.x) + 'px',
                                marginTop:'-' + Math.round(ry * c.y) + 'px'
                            });

                            $('#preview2').css({
                                width:Math.round(50 / c.w * boundx) + 'px',
                                height:Math.round(50 / c.h * boundy) + 'px',
                                marginLeft:'-' + Math.round(50 / c.w * c.x) + 'px',
                                marginTop:'-' + Math.round(50 / c.h * c.y) + 'px'
                            });
                        }
                        jQuery('#x').val(c.x);
                        jQuery('#y').val(c.y);
                        jQuery('#x2').val(c.x2);
                        jQuery('#y2').val(c.y2);
                        jQuery('#w').val(c.w);
                        jQuery('#h').val(c.h);
                    }

                    ;

                    $.fancybox().close();
                } else {
                    alert(data.msg);
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
            data:{x:x, y:y, w:w, h:h, width:width, height:height, oldImgPath:oldImgPath, fileExt:imgFileExt},
            datatype:'json',
            success:function (msg) {
                if (msg.code == 200) {
                    $("#avatar").attr("src", msg.object);
                    $("#boxShow").empty().append("<h1>头像修改完成!</h1>");
                    $("#bigBox").hide();
                    $("#midBox").hide();
                    $("#editHead").hide();

                }
                else {
                    alert(msg.msg);
                }
            }
        });
    });

</script>
</body>
</html>
