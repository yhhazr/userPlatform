<%--
  Created by IntelliJ IDEA.
  User: cutter.li
  Date: 12-9-20
  Time: 下午5:21
  左边导航栏
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="menubox">
    <h2>账号信息</h2>

    <div class="menulist menulist_first  menulist_select" id="indexNav"><a href="/accountcenter.html"
                                                                           class="a"><strong>基本信息</strong></a></div>
    <div class="menulist" id="test_2" >
        <a href="#" class="b">
            <strong>个人资料</strong><span></span>
        </a></div>
    <div class="sub_menulist none" style="display: none;">
        <a href="javascript:void(0);" id="baseInfoNav"    onclick="swapTo('baseInfo');"    class="one">基本资料</a>
        <a href="javascript:void(0);" id="detailInfoNav"  onclick="swapTo('detailInfo');"  class="two">详细资料</a>
        <a href="javascript:void(0);" id="avatarNav"       onclick="forword('nav','avatar');"      class="three">修改头像</a>
        <a href="javascript:void(0);" id="eduInfoNav"      onclick="swapTo('eduInfo');"     class="four">教育背景</a>
        <a href="javascript:void(0);" id="workInfoNav"     onclick="swapTo('workInfo');"    class="five">工作信息</a>
    </div>
    <div class="menulist" id="testemail">
        <a href="javascript:void(0);" id="bindEmailInfo"  onclick="forword('/bindEmail?type=info','');" class="c">
            <strong>邮箱绑定</strong>
        </a>
    </div>
    <div class="menulist" id="resetPswNav">
        <a href="javascript:void(0);" onclick="forword('nav','resetPsw')" class="d">
            <strong>修改密码</strong>
        </a>
    </div>
    <div class="menulist" id="test_3">
        <a href="#" class="e">
            <strong>密保设置</strong>
            <span></span>
        </a>
    </div>
    <div class="sub_menulist sub_menulist_last none" style="display: none; ">
        <a href="javascript:void(0);" class="six"  id="bindEmail" onclick="forword('/bindEmail','')">密保邮箱</a>
        <a href="javascript:void(0);" class="seven" id="bindQuestion"   onclick="forword('/bindQuestion','')">密保问题</a>
        <a href="javascript:void(0);" class="eight" id="bindMobile" onclick="forword('/bindMobile','')">密保手机</a>
    </div>
</div>
<div class="blank10" style="height:12px"><input type="hidden" id="userName" value="${userObject['userName']}"/></div>
<img src="../img/dianhua.png">

<div class="blank10"></div>
<script type="text/javascript" src="../scripts/common.js"></script>
<%--<img src="../img/qun.png">--%>


