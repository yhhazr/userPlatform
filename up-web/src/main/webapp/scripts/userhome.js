// JavaScript Document
$(function () {
    $(".course li").slice(0,2).show();
    $(".textinput").focus(function () {
        $(this).addClass("textinput_hover");
        $(this).parent().siblings(".formerr").hide();
        $(this).parent().siblings(".formerr1").show();
    });
    $(".textinput").blur(function () {
        $(this).removeClass("textinput_hover");

    });

    $(".weluser").hover(function () {
        $(".welul").show();
    }, function () {
        $(".welul").hide();
    });
//    $(".menulist").each(function () {
//        var index = $(".menulist").index(this);
//        if ($(this).hasClass("menulist_select")) {
//            if (index == 2) {
//               // $(this).prev().prev().css("height", "39px");
//            } else if (index == 0) {
//
//            } else {
//               //$(this).prev().css("height", "39px");
//            }
//
//        }
//    });
    $(".menulist a.b,.menulist a.e").toggle(function(){
        $(this).parent().addClass("menulist_select").siblings(".menulist").removeClass("menulist_select");
        $(this).parent().next().show();
    },function(){
        $(this).parent().next().hide();

    });
//    $(".menulist a.b").toggle(function () {
//        $(".menulist").eq(1).css("height", "40px");
//    }, function () {
//        $(".menulist").eq(1).css("height", "39px");
//    });

    $(".menulist").click(function () {
        $(".sub_menulist").children("a").removeClass("select")

    });
    $(".sub_menulist a").click(function () {
        $(this).addClass("select").siblings("a").removeClass("select")
    });
    $("#testemail").click(function(){
        $("#testemail").addClass("menulist_select").siblings(".menulist").removeClass("menulist_select")
    }) ;
    //游戏历程
    $(".more-game").toggle(function(){
        $(".course li").slice(2).stop(true,true).slideDown();
        $(this).html('<span class="border_expand">-</span> 隐藏</strong');
        $(this).attr("title","点击'隐藏'隐藏用户部分游戏历程");
    },function(){
        $(".course li").slice(2).stop(true,true).slideUp();
        $(this).html('<span class="border_expand">+</span> 更多</strong>');
        $(this).attr("title","点击'更多'现实用户所有游戏历程");
    });

});



