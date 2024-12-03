var isSend = false;
var isAgree = false;
var login = {
    rest:function(){
        var email = $(".pregister-form input[name='email']").val();
        if(email == ''){
            layer.msg("Please input email !");
            return;
        }
        if(!validate.email(email)){
            layer.msg("email is nonlicet ！");
            return;
        }
        //检测是否存在
        if(!login.emailIsExiting(email)){
            layer.msg("email not exists ! Please Register Directly ! ");
            return;
        }

        var eamilCode = $(".pregister-form input[name='eamilCode']").val();
        if(eamilCode == ''){
            layer.msg("Please input eamil Code !");
            return;
        }

        var password = $(".pregister-form input[name='password']").val();
        if(password == ''){
            layer.msg("Please input password !");
            return;
        }
        if(!validate.pssword(password)){
            layer.msg("password is nonlicet ！At least 6-12 characters, only letters and numbers.");
            return;
        }
        var passwordCondfirm = $(".pregister-form input[name='passwordCondfirm']").val();
        if(password != passwordCondfirm){
            layer.msg("password inconsistency !");
            return;
        }

        //数据校验
        var config = {
            url:'/login/rest',
            data:{
                "email":email,
                "eamilCode":eamilCode,
                "password":password,
            },
            callBackFunc:function(data){
                if(data == '0'){
                    layer.msg("registered successfully!");
                    //刷新页面
                    login.regisToLogin();
                }
                if(data == '1'){
                    layer.msg("Verification eamil Code error!");
                }
                if(data == '2'){
                    layer.msg("Reset Failed, Please Retry!");
                }
            },
            callBackErrorFunc:function(){}
        };
        HD.doPost(config);//提交
    },
    openTerms:function(){
        window.open("/login/terms", '_blank');;
    },
    agree:function(obj){
        var agreeObj = $(obj);
        if(agreeObj.is(":checked")){
            isAgree = true;
        }else{
            isAgree = false;
        }
    },
    emailIsExiting:function(email){
        var flag = false;
        $.ajax({
            type:"POST",
            url:'/login/emailIsExist',
            data:{
                "email":email,
            },
            async:false,
            success:function(res){
                flag = res;
            },
            error:function(XMLHttpRequest, textStatus){

            }
        });
        return flag;
    },
    //注册
    regis:function(){
        if(!isAgree){
            layer.msg("Please review and agree to the Terms of use And Management Standards!");
            return;
        }
        var userName = $(".pregister-form input[name='userName']").val();
        if(userName == ''){
            layer.msg("Please input userName ！");
            return;
        }
        if(!validate.username(userName)){
            layer.msg("Username only 4-16 character alphanumeric underscores/minus are allowed ！");
            return;
        }

        var email = $(".pregister-form input[name='email']").val();
        if(email == ''){
            layer.msg("Please input email !");
            return;
        }
        if(!validate.email(email)){
            layer.msg("email is nonlicet ！");
            return;
        }
        //检测是否存在
        if(login.emailIsExiting(email)){
            layer.msg("email already exists ！");
            return;
        }

        var eamilCode = $(".pregister-form input[name='eamilCode']").val();
        if(eamilCode == ''){
            layer.msg("Please input eamil Code !");
            return;
        }

        var password = $(".pregister-form input[name='password']").val();
        if(password == ''){
            layer.msg("Please input password !");
            return;
        }
        if(!validate.pssword(password)){
            layer.msg("password is nonlicet ！At least 6-12 characters, only letters and numbers.");
            return;
        }
        var passwordCondfirm = $(".pregister-form input[name='passwordCondfirm']").val();
        if(password != passwordCondfirm){
            layer.msg("password inconsistency !");
            return;
        }

        var company = $(".pregister-form input[name='company']").val();
        if(company == ''){
            layer.msg("Please input company !");
            return;
        }
        //数据校验
        var config = {
            url:'/login/regis',
            data:{
                "userName":userName,
                "email":email,
                "eamilCode":eamilCode,
                "password":password,
                "company":company
            },
            callBackFunc:function(data){
                if(data == '1'){
                    layer.msg("Verification eamil Code error");
                }
                if(data == '0'){
                    layer.msg("registered successfully");
                    //刷新页面
                    window.location.href = "/";
                }
            },
            callBackErrorFunc:function(){}
        };
        HD.doPost(config);//提交
    },
    sendEmail:function(){
        var email = $("#email").val();
        if(email == ''){
            layer.msg("Please enter Email Address !");
            return;
        }
        if(!validate.email(email)){
            layer.msg("email is nonlicet ！");
            return;
        }
        if(isSend){
            layer.msg("Have already sent Email !");
            return;
        }
        login.send(email);
    },
    send:function(email){
        var config ={
            url:"/login/send",
            data:{"email":email},
            callBackFunc:function(res){
                if(res == true){
                    isSend = true;
                    //1分钟后可以重新发送
                    setTimeout(function(){
                        isSend = false;
                    },60000);
                    layer.msg("Send success !");
                }else{
                    layer.msg("Send error !");
                }
            }
        };
        HD.doPost(config);
    },
    openLogin:function (t){
        let config = {
            width:615,
            height:748,
            title:false,
            closeBtn:true,
            url:ctxPath + '/login/openLogin/'+t,
        };
        ui.createWind(config);
    },
    forgotPwd:function(){
        ui.closeWin();
        let config = {
            width:615,
            height:748,
            title:false,
            closeBtn:true,
            url:ctxPath + '/login/forgotPwd',
        };
        ui.createWind(config);
    },
    openRegister:function (){
        let config = {
            width:615,
            height:748,
            title:false,
            closeBtn:true,
            url:ctxPath + '/login/openRegister',
        };
        ui.createWind(config);
    },
    doLogin:function(userId,passWord,code,from){
        const config = {
            url: "/login/doLogin",
            data: {
                u: userId,
                p: passWord,
                v: code
            },
            callBackFunc: function (res) {
                //0 暂未审核  1 正常登录 2 被锁定 3用户名或密码错误 4 验证码错误
                if (res != null) {
                    if (res == 0) {
                        layer.msg("The user has not yet reviewed !");
                    }
                    if (res == 1) {
                        layer.msg("Land successfully !");
                        if (from == "0"){
                            window.location.href = "/home";
                        }
                        if (from == "1"){
                            window.location.href = "/account/index?page=0";
                        }
                        //主页登陆
                        if(from == "undefined"){
                            window.location.href = "/home";
                        }

                    }

                    if (res == 2) {
                        layer.msg("The user locked !");
                    }

                    if (res == 3) {
                        layer.msg("User name or password is incorrect,please log in again!");
                    }
                    if (res == 4) {
                        layer.msg("Verification code is incorrect,please log in again!");
                    }
                }
            }
        };
        HD.doPost(config);
    },
    //验证码
    getCaptcha:function(){
        const windowUrl = window.URL || window.webkitURL;//处理浏览器兼容性
        const xhr = new XMLHttpRequest();
        const url = "/login/captcha";//验证码请求地址
        xhr.open("GET", url, true);
        xhr.responseType = "blob";
        xhr.onload = function () {
            if (this.status == 200) {
                const blob = this.response;
                $("#verificationCode").attr("src", windowUrl.createObjectURL(blob));
            }
        }
        xhr.send();
    },
    logout:function ()
    {
        ui.confirm('Are you sure you want to log out?',"login.logoutSys()");
    },
    logoutSys:function(){
        window.location.href = ctxPath + "/login/logout";
    },
    regisToLogin:function (){
        ui.closeWin();
        login.openLogin();
    },
    loginToRegis:function (){
        ui.closeWin();
        login.openRegister();
    }
}

