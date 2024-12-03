var account = {
    loadAccount : function () {
        HD.load({
            url : ctxPath + "/account/loadAccount",
            data:{},
            obj:$(".my_right"),
            callBackFunc : function (obj) {
            }
        });
    },
    sendEmail:function(email){
        var config ={
            url:"/login/send",
            data:{"email":email},
            callBackFunc:function(res){
                if(res == true){
                    layer.msg("Send success !");
                }else{
                    layer.msg("Send error !");
                }
            }
        };
        HD.doPost(config);
    },
    updateUser:function (id){
        // 校验是否输入 邮箱验证码
        if($("#userAccunt input[name='user_email_code']").val() == ''){
            layer.msg("Please input eamil Code !");
            return;
        }
        let data = {};
        data.user_id = id;
        data.user_name = $("#userAccunt input[name='user_name']").val();
        data.user_org = $("#userAccunt input[name='user_org']").val();
        data.user_page = $("#userAccunt input[name='user_page']").val();
        data.user_interests = $("#userAccunt input[name='user_interests']").val();
        data.user_email = $("#userAccunt input[name='user_email']").val();
        data.user_email_code = $("#userAccunt input[name='user_email_code']").val();
        data.user_pwd = $("#userAccunt input[name='user_pwd']").val();
        data.user_pwd_rpt = $("#userAccunt input[name='user_pwd_rpt']").val();
        if(data.user_pwd !== data.user_pwd_rpt){
            layer.msg("password inconsistency !");
            return;
        }
        var config ={
            url:"/account/updateUser",
            data:{"data":JSON.stringify(data)},
            callBackFunc:function(res){
                if(res == true){
                    layer.msg("Update success !");
                }else{
                    layer.msg("Update error !");
                }
            }
        };
        HD.doPost(config);
    }
}

