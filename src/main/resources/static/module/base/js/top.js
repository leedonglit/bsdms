var topTitle = ['home','search','tutorial','contact','about']
var bmdmstop = {
    loadTopPage:function(index){
        var uri = topTitle[index]+"/index";
        HD.load({
            url : ctxPath + uri,
            data:{},
            obj:$("#bmdmsContent"),
            callBackFunc : function (obj) {
            }
        });
    },
    //用户详情点击 href="/account/index" id 用户id
    loadUserInfo:function(userId){
        HD.load({
            url : ctxPath + '/account/index/'+userId,
            data:{},
            obj:$("#bmdmsContent"),
            callBackFunc : function (obj) {
            }
        });
    }
}
$(function(){
    // 顶部菜单 点击事件
    $(".nav ul li").click(function(n,m){
        var index  = $(this).index();
        $(".nav ul li a").removeClass("show_01");
        $(this).find("a").addClass("show_01");
        // 加载页面
        bmdmstop.loadTopPage(index);
    });
})