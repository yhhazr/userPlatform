<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-6-14
  Time: 下午4:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>服务器列表的精简展示和分页展示</title>
    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="js/simple.js"></script>
    <script type="text/javascript">
        function change() {
            var $length = $("select option").length;

            if ($length > 10) {
                $("select option").attr("size", "10");
            }
        }

    </script>
</head>
<body>
<div>
    <br>
    <input id="serverName" type="text" size="20">
    <a><img src="images/select_server.gif" alt="选择服务器" id="selectServer"></a>
    <br>
    <select name="aa" onclick="change()">
        <option value="0">1</option>
        <option value="1">2</option>
        <option value="2">3</option>
        <option value="3">4</option>
        <option value="4">5</option>
        <option value="5">6</option>
        <option value="6">7</option>
        <option value="7">8</option>
        <option value="8">9</option>
        <option value="9">10</option>
        <option value="10">11</option>
        <option value="11">12</option>
        <option value="12">13</option>
        <option value="13">14</option>
        <option value="14">15</option>
        <option value="15">16</option>
        <option value="16">17</option>
        <option value="17">18</option>
        <option value="18">19</option>
        <option value="19">20</option>
        <option value="20">21</option>
        <option value="21">22</option>
        <option value="22">23</option>
        <option value="23">24</option>
        <option value="0">1</option>
        <option value="1">2</option>
        <option value="2">3</option>
        <option value="3">4</option>
        <option value="4">5</option>
        <option value="5">6</option>
        <option value="6">7</option>
        <option value="7">8</option>
        <option value="8">9</option>
        <option value="9">10</option>
        <option value="10">11</option>
        <option value="11">12</option>
        <option value="12">13</option>
        <option value="13">14</option>
        <option value="14">15</option>
        <option value="15">16</option>
        <option value="16">17</option>
        <option value="17">18</option>
        <option value="18">19</option>
        <option value="19">20</option>
        <option value="20">21</option>
        <option value="21">22</option>
        <option value="22">23</option>
        <option value="23">24</option>
    </select>
</div>
</body>
</html>