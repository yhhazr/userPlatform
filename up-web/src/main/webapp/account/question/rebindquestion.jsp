<%@page contentType="text/html" pageEncoding="UTF-8" %>

<div class="rgtcontent">
    <h2 class="h2_title">密保问题-<strong>设置</strong></h2>
    <div class="set_question set_question7">
        <div class="yiwang">以下是您刚刚设置的密保问题，请依次做出问答，以便进行确认。</div>
        <div class="blank15"></div>
        <div class="now_buzhou now_buzhou7"></div>
        <div class="blank" style="height:40px;"></div>
        <div class="queslist">
            <label>问题一：</label>
            <span>${list[0].question}</span>
        </div>
        <div class="queslist relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" />
            <div class="formerr formerr_question">
                <div class="warning" style="width:180px"><span class="error"></span>请输入大于2个字符</div>
            </div>
        </div>
        <div class="queslist">
            <label>问题二：</label>
            <span>${list[1].question}</span>
        </div>
        <div class="queslist relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" />
            <div class="formerr formerr_question">
                <div class="warning" style="width:180px"><span class="error"></span>请输入大于2个字符</div>
            </div>
        </div>
        <div class="queslist ">
            <label>问题三：</label>
            <span>${list[2].question}</span>
        </div>
        <div class="queslist relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" />
            <div class="formerr formerr_question">
                <div class="warning" style="width:180px"><span class="error"></span>请输入大于2个字符</div>
            </div>
        </div>
        <div class="blank"></div>
        <a href="javascript:forword('/bindQuestion','')" class="theprev left theprev_1">上一步</a>
        <a href="javascript:void(0)" title="下一步" class="thenext left thenext_7" style="margin:20px 0 0 30px;">下一步</a>
        <div class="blank"></div>
    </div><!--第一步end-->
    <div class="set_question set_question8 none">
        <div class="yiwang">为避免遗忘，请填写真实信息，这将帮助您以后通过回答问题快速找回帐号密码。</div>
        <div class="blank15"></div>
        <div class="now_buzhou now_buzhou8"></div>
        <div class="blank" style="height:40px;"></div>
        <div class="queslist relative">
            <label>问题一：</label>
            <select id="q1">
                <option value="0">请选择密保问题</option>
                <option value="您母亲的姓名是？">您母亲的姓名是？</option>
                <option value="您配偶的生日是？">您配偶的生日是？</option>
                <option value="您的学号（或工号）是？">您的学号（或工号）是？</option>
                <option value="您母亲的生日是？">您母亲的生日是？</option>
                <option value="您高中班主任的名字是？">您高中班主任的名字是？</option>
                <option value="您父亲的姓名是？">您父亲的姓名是？</option>
                <option value="您小学班主任的名字是？">您小学班主任的名字是？</option>
                <option value="您父亲的生日是？">您父亲的生日是？</option>
                <option value="您配偶的姓名是？">您配偶的姓名是？</option>
                <option value="您初中班主任的名字是？">您初中班主任的名字是？</option>
                <option value="您最熟悉的童年好友名字是？">您最熟悉的童年好友名字是？</option>
                <option value="您最熟悉的学校宿舍室友名字是？">您最熟悉的学校宿舍室友名字是？</option>
                <option value="对您影响最大的人名字是？">对您影响最大的人名字是？</option>
            </select>
            <div class="formerr formerr_question1">
                <div class="warning"><span class="error"></span>请选择问题</div>
            </div>
        </div>
        <div class="queslist queslist1 relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" onkeyup="value=value.replace(/\s/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\s/g,''))"/>
            <div class="formerr formerr_question">
                <div class="warning" style="width:180px"><span class="error"></span>请输入大于2个字符</div>
            </div>
        </div>
        <div class="queslist queslist1 relative">
            <label>问题二：</label>
            <select id="q2">
                <option value="0">请选择密保问题</option>
                <option value="您母亲的姓名是？">您母亲的姓名是？</option>
                <option value="您配偶的生日是？">您配偶的生日是？</option>
                <option value="您的学号（或工号）是？">您的学号（或工号）是？</option>
                <option value="您母亲的生日是？">您母亲的生日是？</option>
                <option value="您高中班主任的名字是？">您高中班主任的名字是？</option>
                <option value="您父亲的姓名是？">您父亲的姓名是？</option>
                <option value="您小学班主任的名字是？">您小学班主任的名字是？</option>
                <option value="您父亲的生日是？">您父亲的生日是？</option>
                <option value="您配偶的姓名是？">您配偶的姓名是？</option>
                <option value="您初中班主任的名字是？">您初中班主任的名字是？</option>
                <option value="您最熟悉的童年好友名字是？">您最熟悉的童年好友名字是？</option>
                <option value="您最熟悉的学校宿舍室友名字是？">您最熟悉的学校宿舍室友名字是？</option>
                <option value="对您影响最大的人名字是？">对您影响最大的人名字是？</option>
            </select>
            <div class="formerr formerr_question1">
                <div class="warning"><span class="error"></span>请选择问题</div>
            </div>
        </div>
        <div class="queslist queslist1 relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" onkeyup="value=value.replace(/\s/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\s/g,''))"/>
            <div class="formerr formerr_question">
                <div class="warning" style="width:180px"><span class="error"></span>请输入大于2个字符</div>
            </div>
        </div>
        <div class="queslist queslist1 relative">
            <label>问题三：</label>
            <select id="q3">
                <option value="0">请选择密保问题</option>
                <option value="您母亲的姓名是？">您母亲的姓名是？</option>
                <option value="您配偶的生日是？">您配偶的生日是？</option>
                <option value="您的学号（或工号）是？">您的学号（或工号）是？</option>
                <option value="您母亲的生日是？">您母亲的生日是？</option>
                <option value="您高中班主任的名字是？">您高中班主任的名字是？</option>
                <option value="您父亲的姓名是？">您父亲的姓名是？</option>
                <option value="您小学班主任的名字是？">您小学班主任的名字是？</option>
                <option value="您父亲的生日是？">您父亲的生日是？</option>
                <option value="您配偶的姓名是？">您配偶的姓名是？</option>
                <option value="您初中班主任的名字是？">您初中班主任的名字是？</option>
                <option value="您最熟悉的童年好友名字是？">您最熟悉的童年好友名字是？</option>
                <option value="您最熟悉的学校宿舍室友名字是？">您最熟悉的学校宿舍室友名字是？</option>
                <option value="对您影响最大的人名字是？">对您影响最大的人名字是？</option>
            </select>
            <div class="formerr formerr_question1">
                <div class="warning"><span class="error"></span>请选择问题</div>
            </div>
        </div>
        <div class="queslist queslist1 relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" onkeyup="value=value.replace(/\s/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\s/g,''))"/>
            <div class="formerr formerr_question">
                <div class="warning" style="width:180px"><span class="error"></span>请输入大于2个字符</div>
            </div>
        </div>
        <div class="blank"></div>
        <a href="javascript:void(0)" class="theprev left theprev_2">上一步</a>
        <a href="javascript:void(0)" title="下一步" class="thenext thenext_8" style="margin:20px 0 0 380px;">下一步</a>
    </div><!--第二步end-->

    <div class="set_question set_question9 none">
        <div class="yiwang">以下是您刚刚设置的密保问题，请依次做出问答，以便进行确认。</div>
        <div class="blank15"></div>
        <div class="now_buzhou now_buzhou9"></div>
        <div class="blank" style="height:40px;"></div>
        <div class="queslist">
            <label>问题一：</label>
            <span class="span_question"></span>
        </div>
        <div class="queslist relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" />
            <div class="formerr formerr_question">
                <div class="warning"><span class="error"></span>答案错误</div>
            </div>
        </div>
        <div class="queslist">
            <label>问题二：</label>
            <span class="span_question"></span>
        </div>
        <div class="queslist relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" />
            <div class="formerr formerr_question">
                <div class="warning"><span class="error"></span>答案错误</div>
            </div>
        </div>
        <div class="queslist ">
            <label>问题三：</label>
            <span class="span_question"></span>
        </div>
        <div class="queslist relative">
            <label class="da_label">答案：</label><input type="text" class="textinput" maxlength="20" />
            <div class="formerr formerr_question">
                <div class="warning"><span class="error"></span>答案错误</div>
            </div>
        </div>
        <div class="blank"></div>
        <a href="javascript:void(0)" class="theprev left theprev_3">上一步</a>
        <a href="javascript:void(0)" title="下一步" class="thenext left thenext_9" style="margin:20px 0 0 30px;">下一步</a>
        <div class="blank"></div>
    </div><!--第三步end-->
    <div class="set_question set_question10 none">
        <div class="yiwang">您设置了密保问题，还可设置密保手机保护您帐号。</div>
        <div class="blank15"></div>
        <div class="now_buzhou now_buzhou10"></div>
        <div class="blank" ></div>
        <div class="set_mobleok">
            <div class="anquandun emaildun"></div>
            <strong class="f16px left setsj_ok">密保问题修改成功！</strong>
            <div class="blank"></div>
            <div class="edit_email">
                <a href="/accountcenter.html" title="返回首页" class="editemail_a"></a>
            </div>
            <div class="blank20"></div>
            <div class="blank20"></div>
            <div class="blank20"></div>
        </div>
    </div><!--第四步end-->
    <div class="blank20"></div>
</div>

<script type="text/javascript">
    $(function(){
        var oldQuestionArray = ["${list[0].question}","${list[1].question}","${list[2].question}"];
        var oldAnswerArray = [];
        var oldQuestionKey = "";
        var qaArrayObj = [];

        $(".thenext_7").click(function(){
            qaArrayObj = [];
            oldAnswerArray = [];
            var anArray1 = $(".set_question7 .textinput");
            for(i=0;i<anArray1.length;i++){
                var an = anArray1.eq(i).val();
                if(an.length < 2) {
                    $(".set_question7 .formerr_question").eq(i).show();
                    return false;
                }
                oldAnswerArray.push(anArray1.eq(i).val());
            }

            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/questionSubmit?" + new Date().getTime(),
                data:{q1:oldQuestionArray[0], q2:oldQuestionArray[1], q3:oldQuestionArray[2], a1:oldAnswerArray[0], a2:oldAnswerArray[1], a3:oldAnswerArray[2], type:"bind"},
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".set_question7").hide();
                        $(".set_question8").show();
                    } else {
                        alert(msg.msg);
                    }
                }
            });
        });
        $(".thenext_8").click(function(){
            qaArrayObj = [];
            var selectArray = $(".set_question8 select");
            for(i=0;i<selectArray.length;i++){
                if(selectArray.eq(i).val()=='0'){
                    $(".set_question8 .formerr_question1").eq(i).show();
                    return false;
                }
                if($(".set_question8 .textinput").eq(i).val().length < 2){
                    $(".set_question8 .formerr_question").eq(i).show();
                    return false;
                }
                qaArrayObj.push({q:$(".set_question8 select").eq(i).val(), a:$(".set_question8 .textinput").eq(i).val()});
            }
            if (selectArray.eq(0).val() == selectArray.eq(1).val() || selectArray.eq(0).val() == selectArray.eq(2).val() || selectArray.eq(1).val() == selectArray.eq(2).val()){
                alert("问题不能相同");
                return false;
            }
            if (qaArrayObj[0].a == qaArrayObj[1].a || qaArrayObj[0].a == qaArrayObj[2].a || qaArrayObj[1].a == qaArrayObj[2].a){
                alert("答案不能相同");
                return false;
            }
            qaArrayObj.sort(function(){return Math.random()>0.5?-1:1;});
            for(x in qaArrayObj){
                $(".span_question").eq(x).html(qaArrayObj[x].q)
            }
            $(".set_question8").hide();
            $(".set_question9").show();
        });
        $(".thenext_9").click(function(){
            for(i=0;i<$(".set_question9 .textinput").length;i++){
                if($(".set_question9 .textinput").eq(i).val()!=qaArrayObj[i].a){
                    $(".set_question9 .formerr_question").eq(i).show();
                    return false;
                }
            }
            $.ajax({
                type:"POST",
                dataType:"JSON",
                url:"/questionSubmit?" + new Date().getTime(),
                data:{q1:oldQuestionArray[0], q2:oldQuestionArray[1], q3:oldQuestionArray[2],
                    a1:oldAnswerArray[0], a2:oldAnswerArray[1], a3:oldAnswerArray[2],
                    rq1:qaArrayObj[0].q, rq2:qaArrayObj[1].q, rq3:qaArrayObj[2].q,
                    ra1:qaArrayObj[0].a, ra2:qaArrayObj[1].a, ra3:qaArrayObj[2].a, type:"rebind"},
                async:false,
                success:function (msg) {
                    if (msg.code > 0) {
                        $(".set_question9").hide();
                        $(".set_question10").show();
                    } else {
                        alert(msg.msg);
                    }
                }
            });
        });
        $(".set_question .textinput").focus(function(){
            $(this).siblings(".formerr_question").hide();
        })
        $("select").change(function(){
            //$(this).siblings(".formerr_question1").hide();
            $(".formerr_question1").hide();
        })

        $(".theprev_2").click(function(){
            $(".set_question7").show();
            $(".set_question8").hide();
        });
        $(".theprev_3").click(function(){
            $(".set_question8").show();
            $(".set_question9").hide();
        });
    });
</script>
