<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>支付渠道管理</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/bootstrap-responsive.min.css" rel="stylesheet">
    <style>
        .nav a { font-size: 14px;}
        .nav > li > a { text-align: center;}
        .tabs-left .nav-tabs > li > a, .tabs-right .nav-tabs > li > a { min-width: 100px;}
    </style>
</head>
<body>
<div class="tabbable tabs-left">
    <ul class="nav nav-tabs">
        <li class="active"><a href="#T0" data-toggle="tab">银行卡</a></li>
        <li><a href="#T1" data-toggle="tab">支付宝</a></li>
        <li><a href="#T2" data-toggle="tab">财付通</a></li>
        <li><a href="#T3" data-toggle="tab">神州行卡</a></li>
        <li><a href="#T4" data-toggle="tab">联通卡</a></li>
        <li><a href="#T5" data-toggle="tab">电信卡</a></li>
        <li><a href="#T6" data-toggle="tab">盛大卡</a></li>
        <li><a href="#T7" data-toggle="tab">骏网卡</a></li>
        <li><a href="#T8" data-toggle="tab">搜狐卡</a></li>
        <li><a href="#T9" data-toggle="tab">征途卡</a></li>
        <li><a href="#T10" data-toggle="tab">完美卡</a></li>
        <li><a href="#T11" data-toggle="tab">网易卡</a></li>
        <li><a href="#T12" data-toggle="tab">纵游卡</a></li>
        <li><a href="#T13" data-toggle="tab">天下卡</a></li>
        <li><a href="#T14" data-toggle="tab">天宏卡</a></li>
    </ul>
    <div class="tab-content">
        <div id="T0" class="tab-pane active fade in">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['bank']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId0" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T1" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <c:forEach var="item" items="${map['money']}" varStatus="status">
                    <c:if test="${item.channelId == 'A'}">
                        <c:set var="newItem" value="${item}" />
                    </c:if>
                </c:forEach>
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="${newItem.status == 0 ? '冻结' : '启用'}">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['money']}" varStatus="status">
                    <c:if test="${item.channelId == 'A'}">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId1" value="${item.id}" checked="checked"></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                    </c:if>
                </c:forEach>
            </table>

        </div>

        <div id="T2" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <c:forEach var="item" items="${map['money']}" varStatus="status">
                    <c:if test="${item.channelId == 'B'}">
                        <c:set var="newItem" value="${item}" />
                    </c:if>
                </c:forEach>
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="${newItem.status == 0 ? '冻结' : '启用'}">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['money']}" varStatus="status">
                    <c:if test="${item.channelId == 'B'}">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td><input type="radio" name="paySwitchId2" value="${item.id}" checked="checked"></td>
                            <td>${item.channelName}-${item.subTagName}</td>
                            <td>${item.comment}</td>
                            <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                            <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>

        <div id="T3" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['szx']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId3" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T4" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['unicom']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId4" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T5" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['telecom']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId5" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T6" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['sndacard']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId6" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="T7" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['junnet']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId7" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="T8" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['sohu']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId8" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T9" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['zhengtu']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId9" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T10" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['wanmei']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId10" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T11" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['netease']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId11" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T12" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="启用">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['zongyou']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId12" value="${item.id}" ${item.status == 0 ? 'checked="checked"': ''}></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T13" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <c:forEach var="item" items="${map['tianxia']}" varStatus="status">
                    <c:set var="newItem" value="${item}" />
                </c:forEach>
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="${newItem.status == 0 ? '冻结' : '启用'}">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['tianxia']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId13" value="${item.id}" checked="checked"></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div id="T14" class="tab-pane fade">
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <c:forEach var="item" items="${map['tianhong']}" varStatus="status">
                    <c:set var="newItem" value="${item}" />
                </c:forEach>
                <input type="button" name="btnPayOk" class="btn btn-primary input-xxlarge" value="${newItem.status == 0 ? '冻结' : '启用'}">
                <span class="label label-info">支付状态为正常时，则可用该方式进行支付，支付状态为关闭状态时，则不可用该支付方式支付；</span>
            </div>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>#</th>
                    <th>支付方式</th>
                    <th>说明</th>
                    <th>支付状态</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="item" items="${map['tianhong']}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td><input type="radio" name="paySwitchId14" value="${item.id}" checked="checked"></td>
                        <td>${item.channelName}-${item.subTagName}</td>
                        <td>${item.comment}</td>
                        <td>${item.status == 0 ? '<font class="pay-status" color="#468847">正常</font>' : '<font class="pay-status">冻结</font>'}</td>
                        <td>${item.deleteFlag == -1 ? '<font color="red">删除</font>' : '<font color="#468847">未删除</font>'}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        /*$("#T0 input[name=btnPayOk]").eq(0).click(function(){
            var id = $("#T0 input:radio:checked").val();
            var data = {id: [id], oId: []};
            var checkboxArray = $("#T0 input:radio").not($("#T0 input:radio:checked"));
            for(var i=0; i<checkboxArray.length; i++){
                data["oId"].push(checkboxArray.eq(i).val());
            }
            if (!confirm("启用后，原设置的支付方式将会被冻结，操作的当前支付方式将会被启用，你是否确认执行操作?"))
                return;
            $.ajax({
                type:"POST",
                dataType:"JSON",
                cache: false,
                url:"/paySwitch?action=update",
                data:$.param(data,true),
                beforeSend :function(){
                    $("#T0 input:button").attr("disabled", "disabled");
                },
                complete:function(){
                    $("#T0 input:button").removeAttr("disabled");
                },
                success:function (msg) {
                    $("#T0 font.pay-status").removeAttr("color").html("冻结");
                    var index = $("#T0 input:radio:checked").parent().parent().index();
                    $("#T0 font.pay-status").eq(index).attr("color", "#468847").html("正常");
                }
            });
        });*/

        $("#T1 input[name=btnPayOk]").eq(0).click(function(){
            var id = $("#T1 input:radio:checked").val();
            if (!confirm("启用后，原设置的支付方式将会被冻结，操作的当前支付方式将会被启用，你是否确认执行操作?"))
                return;
            $.ajax({
                type:"POST",
                dataType:"JSON",
                cache: false,
                url:"/paySwitch?action=updateStatus",
                data:{id: id},
                beforeSend :function(){
                    $("#T1 input:button").attr("disabled", "disabled");
                },
                complete:function(){
                    $("#T1 input:button").removeAttr("disabled");
                },
                success:function (msg) {
                    if ($("#T1 input:button").val() == '启用'){
                        $("#T1 input:button").val('冻结');

                        var index = $("#T1 input:radio:checked").parent().parent().index();
                        $("#T1 font.pay-status").eq(index).attr("color", "#468847").html("正常");
                    } else {
                        $("#T1 input:button").val('启用');
                        $("#T1 font.pay-status").removeAttr("color").html("冻结");
                    }
                }
            });
        });

        $("#T2 input[name=btnPayOk]").eq(0).click(function(){
            var id = $("#T2 input:radio:checked").val();
            if (!confirm("启用后，原设置的支付方式将会被冻结，操作的当前支付方式将会被启用，你是否确认执行操作?"))
                return;
            $.ajax({
                type:"POST",
                dataType:"JSON",
                cache: false,
                url:"/paySwitch?action=updateStatus",
                data:{id: id},
                beforeSend :function(){
                    $("#T2 input:button").attr("disabled", "disabled");
                },
                complete:function(){
                    $("#T2 input:button").removeAttr("disabled");
                },
                success:function (msg) {

                    if ($("#T2 input:button").val() == '启用'){
                        $("#T2 input:button").val('冻结');

                        var index = $("#T2 input:radio:checked").parent().parent().index();
                        $("#T2 font.pay-status").eq(index).attr("color", "#468847").html("正常");
                    } else {
                        $("#T2 input:button").val('启用');

                        $("#T2 font.pay-status").removeAttr("color").html("冻结");
                    }
                }
            });
        });

        function multiChannel(divId){
            var id = $(divId + " input:radio:checked").val();
            var data = {id: [id], oId: []};
            var checkboxArray = $(divId + " input:radio").not($(divId + " input:radio:checked"));
            for(var i=0; i<checkboxArray.length; i++){
                data["oId"].push(checkboxArray.eq(i).val());
            }
            if (!confirm("启用后，原设置的支付方式将会被冻结，操作的当前支付方式将会被启用，你是否确认执行操作?"))
                return;
            $.ajax({
                type:"POST",
                dataType:"JSON",
                cache: false,
                url:"/paySwitch?action=update",
                data:$.param(data,true),
                beforeSend :function(){
                    $(divId + " input:button").attr("disabled", "disabled");
                },
                complete:function(){
                    $(divId + " input:button").removeAttr("disabled");
                },
                success:function (msg) {
                    $(divId + " font.pay-status").removeAttr("color").html("冻结");
                    var index = $(divId + " input:radio:checked").parent().parent().index();
                    $(divId + " font.pay-status").eq(index).attr("color", "#468847").html("正常");
                }
            });
        }

        function singleChannel(divId) {
            var id = $(divId + " input:radio:checked").val();
            if (!confirm("启用后，原设置的支付方式将会被冻结，操作的当前支付方式将会被启用，你是否确认执行操作?"))
                return;
            $.ajax({
                type:"POST",
                dataType:"JSON",
                cache: false,
                url:"/paySwitch?action=updateStatus",
                data:{id: id},
                beforeSend :function(){
                    $(divId + " input:button").attr("disabled", "disabled");
                },
                complete:function(){
                    $(divId + " input:button").removeAttr("disabled");
                },
                success:function (msg) {

                    if ($(divId + " input:button").val() == '启用'){
                        $(divId + " input:button").val('冻结');

                        var index = $(divId + " input:radio:checked").parent().parent().index();
                        $(divId + " font.pay-status").eq(index).attr("color", "#468847").html("正常");
                    } else {
                        $(divId + " input:button").val('启用');

                        $(divId + " font.pay-status").removeAttr("color").html("冻结");
                    }
                }
            });
        }

        $("#T0 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T0");
        });

        $("#T3 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T3");
        });

        $("#T4 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T4");
        });

        $("#T5 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T5");
        });

        $("#T6 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T6");
        });

        $("#T7 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T7");
        });

        $("#T8 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T8");
        });

        $("#T9 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T9");
        });

        $("#T10 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T10");
        });

        $("#T11 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T11");
        });

        $("#T12 input[name=btnPayOk]").eq(0).click(function(){
            multiChannel("#T12");
        });

        $("#T13 input[name=btnPayOk]").eq(0).click(function(){
            singleChannel("#T13");
        });

        $("#T14 input[name=btnPayOk]").eq(0).click(function(){
            singleChannel("#T14");
        });
    });
</script>
</body>
</html>
