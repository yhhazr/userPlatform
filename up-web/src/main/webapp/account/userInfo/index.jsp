<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-20
  Time: 下午5:48
  基本信息页面
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="topline"></div>
<div class="rgtcontent" style="min-height:200px; _height:200px">
    <h2 class="h2_title">基本信息</h2>

    <div class="blank20"></div>
    <div class="welcome">
        <img src="${userObject['headDir']}" class="userheadimg" title="点击修改头像" style="cursor: pointer;"
             onclick="forword('/nav','avatar');">

        <div class="welcome_rgt f14px left relative">
            <strong id="loginUserName">${userObject['userName']}</strong>, 欢迎您！&gt; <a href="javascript:void(0);"
                                                                                       onclick="logout();"
                                                                                       class="f12px">退出登陆</a><br>

            <div class="blank10"></div>


            <span class="last-server-span"><c:if test="${newOpen==null}">
                上次登录服务器
            </c:if>
            <c:if test="${newOpen=='new'}">
                推荐服务器
            </c:if>
            ： ${gameEntry['gameName']} ${serverEntry['serverName']}
             </span>
            <c:if test="${gameEntry['id']<=1}">
                <a href="javascript:void(0);"
                   onclick="javascript:enterGame1('/PlayGame2?game=S&subGame=0&g=${serverEntry['gameId']}&z=${serverEntry['serverId']}');"
                   title="进入游戏" class="usergogame">进入游戏</a>
            </c:if>
            <c:if test="${gameEntry['id']==2}">
                <a href="javascript:void(0);"
                   onclick="javascript:enterGame1('/PlayGame3?game=X&subGame=0&g=${serverEntry['gameId']}&z=${serverEntry['serverId']}');"
                   title="进入游戏" class="usergogame">进入游戏</a>
            </c:if>

            <c:if test="${gameEntry['id']==3}">
                <a href="javascript:void(0);"
                   onclick="javascript:enterGame1('/PlayGame3?game=D&subGame=0&g=${serverEntry['gameId']}&z=${serverEntry['serverId']}');"
                   title="进入游戏" class="usergogame">进入游戏</a>
            </c:if>
        </div>
        <div class="blank"></div>
    </div>
    <div class="blank20"></div>
    <c:if test="${fn:length(userIPLogs)>=1}">
        <div class="prompting ml_20 ">
            <div class="sub_prompting dengpao">存在登录异常，现在去<a href="javascript:void(0);"
                                                            onclick="swapTo('ipException')">看看?</a></div>
        </div>
    </c:if>
    <div class="blank20"></div>
    <div>
        <c:if test="${nowRegister=='no'}"> <%--进过游戏的--%>
            <h2 class="h2_title">
                <c:if test="${fn:length(gameHistory)>=3}">
                    <strong class="more-game" title="点击'更多'显示用户所有的游戏历程"><span
                            class="border_expand">+</span> 更多</strong>
                </c:if>
                游戏历程</h2>
            <ul class="course">
                <c:forEach var="gh" items="${gameHistory}">
                    <li style="display: none;">
                        <span class="game-type-name">${gh.gameName}</span>
                        <span class="game-name">所在区：${gh.serverName}</span>
                        <a class="usergogame" href="#"
                           onclick="javascript:enterGame1('/PlayGame3?game=${gh.gameTag}&subGame=0&g=${gh['gameId']}&z=${gh['serverId']}');">进入游戏</a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${nowRegister=='yes'}">  <%--刚注册的--%>
            <h2 class="h2_title">游戏历程</h2>

            <div class="no-course">您还没有玩游戏，暂无游戏记录哦。</div>
            <h3 class="possible-love">猜您喜欢的游戏</h3>
            <ul class="possible-list">
                <c:forEach var="gh" items="${gameHistory}" varStatus="idx">
                    <c:if test="${idx.index%3==0}">
                        <li style="margin-right:0">
                    </c:if>
                    <c:if test="${idx.index%3!=0}">
                        <li>
                    </c:if>
                    <a href="#"
                       onclick="javascript:enterGame1('/PlayGame3?game=${gh.gameTag}&subGame=0&g=${gh['gameId']}&z=${gh['serverId']}');"><img
                            src="../../img/${gh.gameImg}"/></a>

                    <div class="possible-list-rgt">
                        <h4>${gh.gameName}</h4>
                        <h5 title="${gh.serverName}">${gh.serverName}</h5>
                        <a href="#"
                           onclick="javascript:enterGame1('/PlayGame3?game=${gh.gameTag}&subGame=0&g=${gh['gameId']}&z=${gh['serverId']}');">进入游戏</a>
                    </div>
                    </li>
                </c:forEach>
            </ul>
            <%--没有玩过游戏 end--%>
        </c:if>

    </div>


    <h2 class="h2_title">账号安全</h2>

    <div class="blank20"></div>
</div>
<ul class="secure zoom">
    <li class="ok">
        <span>当前安全级别：</span>

        <div class="li_text" style="margin-left: 0">当前安全级别：<strong class="pass_Lv3"
                                                                   id="safeStrength">${safeInfo['safeStrength']}</strong>

            <div class="pass_Lv"></div>
            <div class="pass_Lv2"></div>
            <%--<a href="#" class="u_line"> 怎样提高账号安全？</a>--%>
            <br>
            <c:if test="${safeInfo.safeStrength<7}">
                <span>为保障您的账号安全，请尽快完善密保</span>
            </c:if>
            <%--<a href="#" class="u_line">我要增强保护</a>--%>
        </div>
    </li>
    <li class="ok selectbg">
        <span>密&nbsp;&nbsp;&nbsp;&nbsp;码：</span>

        <div class="li_text">当前密码安全度 <strong class="pass_Lv3" id="pswStrength">${safeInfo['pswStrength']}</strong><br>
            建议密码由8位以上数字、字母和特殊字符组成
            <div class="pass_Lv pass_Lv5"></div>
            <div class="pass_Lv2 pass_Lv6 "></div>
        </div>
        <a href="javascript:void(0);"
           onclick="forword('nav','resetPsw');" class="edit_set">修改</a>
    </li>
    <c:if test="${safeInfo['email']=='empty'}">
        <li class="no">
            <span>安全邮箱：</span>

            <div class="li_text">您还没有绑定安全邮箱,请绑定安全邮箱<br>
                忘记密码时，您的绑定邮箱可以帮您找回密码。
            </div>
            <a onclick="forword('/bindEmail','');" href="javascript:void(0);" class="edit_set">设置</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['email']!='empty'}">
        <li class="ok">
            <span>安全邮箱：</span>

            <div class="li_text">${safeInfo.email}<br>
                忘记密码时，您的绑定邮箱可以帮您找回密码。
            </div>
            <a onclick="forword('/bindEmail','')" href="javascript:void(0);" class="edit_set">修改</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['phone']=='empty'}">
        <li class="no selectbg">
            <span>绑定手机：</span>

            <div class="li_text">没有绑定的手机号 <br>
                忘记密码时，您可以通过手机快速找密码。
            </div>
            <a onclick="forword('/bindMobile','')" href="javascript:void(0);" class="edit_set">设置</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['phone']!='empty'}">
        <li class="ok selectbg">
            <span>绑定手机：</span>

            <div class="li_text">绑定的手机号${safeInfo.phone}<br>
                手机找回密码
            </div>
            <a onclick="forword('/bindMobile','')" href="javascript:void(0);" class="edit_set">修改</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['certify']=='empty'}">
        <li class="no">
            <span>证件信息：</span>

            <div class="li_text">已设置证件信息<br>
                为了您的账号安全，证件信息设置后不能修改。
            </div>
            <a onclick="swapTo('baseInfo')" href="javascript:void(0);" class="edit_set">设置</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['certify']!='empty'}">
        <li class="ok">
            <span>证件信息：</span>

            <div class="li_text">已设置证件信息<br>
                为了您的账号安全，证件信息设置后不能修改。
            </div>
            <a onclick="swapTo('baseInfo')" href="javascript:void(0);" class="edit_set">修改</a>
        </li>
    </c:if>


    <c:if test="${safeInfo['userInfo']=='empty'}">
        <li class="no selectbg">
            <span>个人资料：</span>

            <div class="li_text">尚未填写个人资料信息<br>
                填写完整基本资料，可以方便我们更快捷为你提供服务。
            </div>
            <a onclick="swapTo('baseInfo')" href="javascript:void(0);" class="edit_set">设置</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['userInfo']!='empty'}">
        <li class="ok selectbg">
            <span>个人资料：</span>

            <div class="li_text">已经填写个人资料信息<br>
                填写完整基本资料，可以方便我们更快捷为你提供服务。
            </div>
            <a onclick="swapTo('baseInfo')" href="javascript:void(0);" class="edit_set">修改</a>
        </li>
    </c:if>


    <c:if test="${safeInfo['question']=='empty'}">
        <li class="no last">
            <span>密保设置：</span>

            <div class="li_text">尚未设置密码保护<br>
                设置密码保护后，账户出现异常时，可找回\修改密码。
            </div>
            <a onclick="forword('/bindQuestion','')" href="javascript:void(0);;" class="edit_set">设置</a>
        </li>
    </c:if>

    <c:if test="${safeInfo['question']!='empty'}">
        <li class="ok last">
            <span>密保设置：</span>

            <div class="li_text">已经设置密码保护 <br>
                设置密码保护后，账户出现异常时，可找回\修改密码。
            </div>
            <a onclick="forword('/bindQuestion','')" href="javascript:void(0);" class="edit_set">修改</a>
        </li>
        </li>
    </c:if>
</ul>

<script type="text/javascript" src="/scripts/jquery-1.7.1.js"></script>
<script type="text/javascript">
    $(function () {
        var safeLevel = '${safeInfo['safeStrength']}';
        var pswLevel = '${safeInfo['pswStrength']}';
        var headUrl = '${userObject['headDir']}';
        //设置头像
        if ($.trim(headUrl)) {
            $(".userheadimg").attr("src", headUrl);
        }
        else {
            $(".userheadimg").attr("src", "http://account.7road.com/userImg/geren_right_01.jpg");

        }
        //设置整体安全条的比例
        $("#safeStrength").empty().html(safeLevel);
        $(".pass_Lv2").css("width", safeLevel / 7 * 178 + "px");
        //设置密码的安全条比例
        $("#pswStrength").empty().html(pswLevel);
        $(".pass_Lv6").css("width", pswLevel / 7 * 178 + "px");
    });
</script>
<script type="text/javascript" src="../../scripts/accountcenter.js"></script>
<script type="text/javascript" src="../../scripts/app-common.js"></script>