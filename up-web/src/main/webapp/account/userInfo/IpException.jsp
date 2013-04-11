<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-20
  Time: 下午5:52
  IP登录异常
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <div class="topline"></div>
    <div class="rgtcontent">
        <h2 class="h2_title">异地登录记录</h2>
        <div class="yiwang f14px bottomline" style="border-bottom:0">提示：以下是您最近三个月的异地登陆记录。</div>
        <div class="prompting">
            <div class="sub_prompting" style="padding:5px 10px;">由于近期一些宽带服务提供商在做网络调整，可能出现登陆地点不准确的情况，请您先核对登陆时间；如果确定在该时间段内
                没有登陆，请立即<a href="javascript:void(0)"
                           onclick="forword('nav','resetPsw')" class="u_line">修改密码</a></div>
        </div>
        <div class="blank20"></div>
        <table width="696" border="0" class="yichang_table">
            <tbody><tr class="first_tr">
                <td>IP地址</td>
                <td>登录地点</td>
                <td>登录时间</td>
            </tr>
            <c:forEach  var="IPlog" items="${userIPLogs}" begin="0" end="10" step="1">
                <tr>
                    <td>${IPlog.content}</td>
                    <td>${IPlog.ext1}</td>
                    <td>${IPlog.log_time}</td>
                </tr>
            </c:forEach>
            </tbody></table>

        <div class="blank20"></div>
    </div>



