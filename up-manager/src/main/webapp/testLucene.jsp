<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-8-21
  Time: 下午5:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <div id="LuceneSearch">
        <input type="button" value="建立索引" id="buildIndex" onclick="buildIndexFn();"> <br>
        <input type="text" id="keyworld" size="20">
        <input type="button" value="搜索" onclick="searchResultFn();">
    </div>
    <div id="searchResult">


    </div>
    <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript">
        function buildIndexFn() {
            $.ajax({
                url:'/faq?action=faqQuery',
                type:'post',
                dataType:'json',
                success:function (data) {
                    $("#searchResult").empty().append("<h1 style='color: #ff0000;'>" + data.msg + "</h1>");
                }
            });
        }

        function searchResultFn() {
            var keyword = $("#keyworld").val();
            $.ajax({
                url:'/faq?action=QueryFaq',
                type:'post',
                data:{keyword:keyword},
                dataType:'json',
                success:function (data) {
                    if (data != 0) {

                        var content = $("<div></div>");
                        for (var i = 0; i < data.length; i++) {
                            content.append("<h2>问题：" + data[i].question + "<br>答案：" + data[i].answer + "</h2><hr>");
                        }
                        $("#searchResult").empty().append(content);
                    }
                    else {
                        $("#searchResult").empty().append("<h1 style='color: #ff0000;'>没有搜到相关结果</h1>");
                    }
                }
            });
        }

    </script>
</div>
