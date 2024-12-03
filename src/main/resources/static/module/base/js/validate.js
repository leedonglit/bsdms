var validate = {
	email : function(email){
		 var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
         return reg.test(email);
	},
	//密码 6-12个字符，至少包含一个大写，小写字母及数字
	pssword:function(pwd){
		//var upperCase = /[A-Z]/g; // 检测大写字母
	   	//var lowerCase = /[a-z]/g; // 检测小写字母
	    //var numbers = /[0-9]/g; // 检测数字
		var reg = /^[a-zA-Z0-9]+$/;
	    var lengthCheck = /^\S{6,12}$/; // 检测长度为6至12位
	    if(lengthCheck.test(pwd) && reg.test(pwd)) {
	        return true; // 密码有效
	    } else {
	        return false; // 密码无效
	    }
	},
	//是否为4位字符
	isFourDigitNumber:function(input) {
	   return input.length == 4;
	},
	//是否为4位数字
	isFourNumber:function(input) {
	    var regex = /^\d{4}$/;
	    return regex.test(input);
	},
	//只允许字母，数字和下划线 12
	username:function(name){
		if(name.length > 12){
			return false;
		}
		var regex =  /^[a-zA-Z0-9_-]{4,16}$/;
		return regex.test(name);
	},
	//允许的字符长度为 
	strLength:function(str){
		if(name.length > 40){
			return false;
		}else{
			return true;
		}
	}
	
}