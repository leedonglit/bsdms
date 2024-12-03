// home 页面
$(function(){
    $("#homeSearch").click(function (){
        var value = $(this).prev().val();
        if(value == null || value === ""){
            //请输入搜索信息
            layer.msg("Please enter KEY!");
            return false;
        }
        window.location.href='/academic/content?key='+value;
    })

    let config = {
        url: ctxPath + "/home/loadMsg",
        callBackFunc:function (obj){
            if(obj != null){
                for (let i = 0; i < obj.length; i++) {
                    $(".news_list").prepend("<li>\n" +
                        "                    <a class=\"news_l_content\">\n" +
                        "                        <h2>"+obj[i].msg_TIME.substring(0,19)+"</h2>\n" +
                        "                        <p>"+obj[i].msg_TITLE+"</p>\n" +
                        "                    </a>\n" +
                        "                </li>");
                }
            }
        }
    };
    HD.doAjax(config);
})