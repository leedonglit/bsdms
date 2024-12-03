/**
 * 自定义jquery验证函数
 *  
 */      // 判断是否选中了海南省
jQuery.validator.addMethod("isHn", function(value, element) {    
	return this.optional(element) || ($("#SED_PROV").val() == '01')||($("#REC_PROV").val() == '01');   
}, "两个省必须有一个是海南"); 
// 手机号码
jQuery.validator.addMethod("isMobile", function(value, element) {    
	var length = value.length;    
	return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/.test(value));    
}, "请正确填写您的手机号码"); 
// 验证日期
jQuery.validator.addMethod("isDate", function(value, element) {    
	var length = value.length;    
	return this.optional(element) || ( /^(\d{4})(-)(\d{2})(-)(\d{2}) (\d{2}):(\d{2}):(\d{2})$/.test(value));    
}, "请填写正确的日期");    

// 验证数字
jQuery.validator.addMethod("isNUM", function(value, element) {    
	var tel =/^\d+$/;    
	return this.optional(element) || (tel.test(value));    
}, "请正确填写您的电话号码");    

// 邮政编码验证
jQuery.validator.addMethod("isZipCode", function(value, element) {    
	var tel = /^[0-9]{6}$/;    
	return this.optional(element) || (tel.test(value));    
}, "请正确填写您的邮政编码");
//身份证验证
jQuery.validator.addMethod("isIdCard", function(value, element) {    
	var tel = /^(\d{6})(18|19|20)?(\d{2})([01]\d)([0123]\d)(\d{3})(\d|X)?$/;
	return this.optional(element) || (tel.test(value));    
}, "请正确填写您的身份证号");  

jQuery.validator.addMethod("ziprange", function(value, element) {
	return this.optional(element) || /^90[2-5]\d\{2}-\d{4}$/.test(value);
}, "Your ZIP-code must be in the range 902xx-xxxx to 905-xx-xxxx");
// 下面是验证代码
$(document).ready(function(){
	$("#frmd").validate({// "#frmd"为form的id
		rules:{// 验证规则开始
			BAND_ID:{// 为input的id
				// required:true,
				// maxlength:16
			},
			WAYBILL_NO:{
				required:true,
				maxlength:64
			},
			SED_NAME: {
				required:true,
				maxlength:32
			},    
			REC_NAME: {
				required:true,
				maxlength:32
			},
			SED_IDCARD:{ 
				isIdCard:true
			},
			REC_IDCARD:{
				isIdCard:true
			},
			SED_PHONE:{ 
				required:false,
				isNUM:true, 
				maxlength:11
			},
			REC_PHONE:{ 
				required:false,
				isNUM:true, 
				maxlength:11
			},
			SED_PROV:{ 
				required:true,
				isHn:true
			},
			REC_PROV:{ 
				required:true,
				isHn:true
			},
			SED_ADDRESS:{ 
				maxlength:256
			},
			REC_ADDRESS:{   
				maxlength:256
			},
			SED_COM:{ 
				maxlength:64
			},
			SED_POST_CODE:{ 
				isZipCode:true
			},
			REC_COM:{ 
				maxlength:64
			},
			REC_POST_CODE:{ 
				isZipCode:true
			},
			ITEM_NAME:{ 
				required:true,
				maxlength:32
			},
			ITEM_TYPE:{ 
				required:true,
				maxlength:3
			},
			ITEM_WEIGHT:{ 
				number:true,
				maxlength:3
			},
			ITEM_AMOUNT:{ 
				isNUM:true, 
				maxlength:8
			},
			REC_TIME:{ 
				isDate:true
			},
			DISP_TIME:{ 
				isDate:true
			},
			WAYBILL_FEE:{ 
				required:true,
				number:true,
				maxlength:10
			}
		},
		messages:{// 验证消息，要是这里把红色部分全部去掉，消息提示就会出来验证内置的英文提示。
			BAND_ID:{ // 为input的id
				required:"企业标识不可为空",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			WAYBILL_NO:{ 
				required:"运单号不可为空",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			SED_NAME: { 
				required:"寄件人姓名不可为空",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},    
			REC_NAME: {  
				required:"收件人姓名不可为空",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			SED_IDCARD:{ 
				isIdCard:"输入错误"
			},
			REC_IDCARD:{
				isIdCard:"输入错误"
			},
			SED_PHONE:{
				isNUM:"请输入数字", 
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			REC_PHONE:{ // 为input的id
				isNUM:"请输入数字", 
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			SED_PROV:{ 
				required:"寄件省不可为空"
			},
			REC_PROV:{ 
				required:"收件省不可为空"
			},
			SED_ADDRESS:{ 
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			REC_ADDRESS:{   
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			SED_COM:{ 
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			SED_POST_CODE:{ 
				isZipCode:"请输入正确的邮政编码"
			},
			REC_COM:{ 
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			REC_POST_CODE:{ 
				isZipCode:"请输入正确的邮政编码"
			},
			ITEM_NAME:{ 
				required:"内件品名不可为空",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			ITEM_TYPE:{ 
				required:"内件类型不可为空",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			ITEM_WEIGHT:{ 
				number:"必须为数值",
				maxlength:3
			},
			ITEM_AMOUNT:{ 
				isNUM:"必须为数字", 
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			},
			REC_TIME:{ 
				isDate:"请检查日期格式"
			},
			DISP_TIME:{ 
				isDate:"请检查日期格式"
			},
			WAYBILL_FEE:{ 
				required:"费用不可为空",
				number:"必须为数值",
				maxlength:jQuery.format("长度不能超过 {0} 个字符")
			}
		},
		errorPlacement:function(error, element){
			error.appendTo(element.next("em"));// 要是有错误信息，信息到什么地方显示。
		}
	});

});