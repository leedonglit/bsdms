var login={
	doLogin:function()
	{
		const loginName = $("#_loginName").val();
		const password = $("#_password").val();
		const validatecode = $("#_validatecode").val();
		const isenable = $("#isenable").val();
		if(isenable == 'true'){
			if(loginName !== "" && password !== "" && validatecode !== ""){
				login.login(loginName,password,validatecode);
				return false;
			}
		} else{
			if(loginName !== "" && password !== ""){
				login.login(loginName,password,validatecode);
				return false;
			}
		}
		if(loginName == ""){
			alert("请输入用户名！");
			$("#_loginName").focus();
		}else if(password == ""){
			alert("请输入密码！");
			$("#_password").focus();
		}else if(isenable == 'true' && validatecode == ""){
			alert("请输入验证码！");
			$("#_validatecode").focus();
		}
	},
	logout:function ()
	{ 
		ui.confirm('确定要退出当前登录吗?',"login.logoutSys()");
	},
	logoutSys:function(){
		var config = {
		  	url:ctxPath+"/logout/logout/toLogout.html",
			callBackFunc : function(obj){ 
			  window.location.href=ctxPath + "/login/login/toLogin.html"; 
			} 
		};
	    HD.doAjax(config);
	} ,
	login:function(loginName,password,validatecode){
		var from_site = $("#from_site").val();
		from_site = from_site==undefined?"":from_site;
		//password = encodeURIComponent(hex_sha1(password));
		var url = ctxPath+"/login/login/check.html?src="+ from_site;
		var params={loginName:loginName,password:password,validatecode:validatecode};
		var config = {
		  	url:url,
		  	data:params,
			callBackFunc : function(obj){
				if(obj == '\u9A8C\u8BC1\u7801\u9519\u8BEF\uFF01'){
					alert(obj);
					$("#vcode_2 img").attr('src',ctxPath+'/sys/common/getImage.html?d='+new Date().getTime())
				}else{
					window.location.href=ctxPath+obj;
				}
			},
			callBackErrorFunc:function(){
				alert("用户名或密码错误！");
				return ;
			}
		}
		HD.doAjax(config);
	},
	getCurStuPower_t:function(){
		var config = {
			width:550, 
			height:350,
			title:'切换授权人',
			url:ctxPath+'/sys/common/getCurStuPower.html'
		};
		ui.createWind(config);
	},
	switchPower:function(){
		var tcher = $("input[name='UIDS']:checked").val();
		if (tcher == undefined) {
			ui.alert("请选择授权人！");
			return false;
		}
		var config = {
		  	url:ctxPath+"/sys/common/switchPower.html",
		  	data:{id:tcher},
			callBackFunc : function(obj){
				window.location.href=ctxPath+obj;
			},
			callBackErrorFunc:function(data){
				ui.alert("获取系统权限失败！");
				return ;
			}
		}
		HD.doAjax(config);
	}
};
$(document).ready(function(){
$("#_loginName").focus();
var isenable = $("#isenable").val();
	$("#_loginName").keydown(function(event){
		var  loginName = $("#_loginName").val();
		if(event.keyCode === 13){
			if(loginName === ''){
				alert("请输入用户名！");
				return false;
			}
			$("#_password").focus();
		}
	});
	$("#_password").keydown(function(event){
		var  password = $("#_password").val();
		if(event.keyCode === 13){
			if(password === ''){
				alert("请输入密码！");
				return false;
			}
			if(isenable == 'true'){
				$("#_validatecode").focus();
				return false;
			}
			login.doLogin()
		}
	});
	$("#_validatecode").keydown(function(event){
		var  validatecode = $("#_validatecode").val();
		if(event.keyCode === 13 && isenable ==='true'){
			if(validatecode  === ''){
				alert("请输入验证码！");
				return false;
			}
			login.doLogin()
		}
	});
	
}); 