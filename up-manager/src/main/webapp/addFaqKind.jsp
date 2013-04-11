<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-3
  Time: 下午4:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div id="form1">
    <table>
        <tr>
            <td>
                请选择父类：
            </td>
            <td>
                <input name="TreeSelect" class="mini-treeselect" multiSelect="true"
                       textField="text" valueField="id" parentField="pid" checkRecursive="true"
                       value="ajax,button"
                        />
            </td>
        </tr>
        <tr>
            <td>
                <label for="name">请输入Faq类型名称：</label>
            </td>
            <td>
                <input id="name" name="name" class="mini-textbox" required="true"/>
            </td>
        </tr>

        <tr>
            <td>
                <label for="ext">请输入Faq类型描叙：</label>
            </td>
            <td>
                <input id="ext" name="ext" class="mini-textarea"
                       style="width: 400px;height: 200px;vertical-align: text-top;"/>
            </td>
        </tr>


        <tr>
            <td>
                <input value="SetForm" type="button" onclick="setForm()"/>
                <input value="GetForm" type="button" onclick="getForm()"/>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    mini.parse();
    function resetForm() {
        var form = new mini.Form("#form1");
        form.reset();
    }


    function submitForm() {


        //提交表单数据
        var form = new mini.Form("#form1");
        var data = form.getData();      //获取表单多个控件的数据
        var json = mini.encode(data);   //序列化成JSON
        $.ajax({
            url:"../data/FormService.aspx?method=SaveData",
            type:"post",
            data:{ submitData:json },
            success:function (text) {
                alert("提交成功，返回结果:" + text);
            }
        });

    }
</script>
</body>
</html>
