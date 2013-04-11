function msgInfo(msg) {
    $.gritter.add({
        text:'<span style="color:#468847;"><strong>' + msg + '</strong></span>',
        image:imageDomainUrl + '/img/ok.png'
    });
}
function msgError(msg) {
    $.gritter.add({
        text:'<span style="color:#468847;"><strong>' + msg + '</strong></span>',
        image:imageDomainUrl + '/img/ex.png'
    });
}

function enterGameCallback(fun, msg) {
    fun(msg);
}
function enterGame(url, fun) {
    $.ajax({
        type:"POST",
        dataType:"jsonp",
        url:url,
        jsonp:"jsoncallback",
        data:{isAjax:true},
        beforeSend:function (XMLHttpRequest, textStatus) {

        },
        success:function (msg) {
            enterGameCallback(fun, msg)
        },
//        success:function (m) {
//            if(m.code == 0){
////                window.open(url,"_blank");
//                window.location.href=url;
//            }else if(m.code > 0){
//                msgError(m.msg);
//            }else {
//                timer=setTimeout('Redirect()',time * 1000); //跳转
//                msgError("您的Cookie失效，8秒后跳转，需要重新登陆！");
//            }
//        },
        error:function (m) {
            msgError("未知请求或者不能连接上服务器");
        }
    });
    return false;
}