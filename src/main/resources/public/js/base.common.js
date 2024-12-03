/**
 * 全选或全不选
 * @param obj
 */
function yesOrNot(obj) {
	var table = $(obj).parent();
	while (!($(table)[0].tagName == "TABLE")) {
		table = table.parent();
	}
	if ($(obj).prop("checked")) {
		table.find("._item").prop("checked", true);
	} else {
		table.find("._item").prop("checked", false);
	}
}
/**
 * 选择后，判断全选框是否被选择
 * @param obj
 */
function yesOrNotAll(obj) {
	var table = $(obj).parent();
	while (!($(table)[0].tagName == "TABLE")) {
		table = table.parent();
	}

	var flag = true;
	table.find("._item").each(function() {
		if (!$(this).prop("checked")) {
			flag = false;
			return;
		}
	});
	table.find("._allItems").prop("checked", flag);
	
	var action = $(obj).attr("action");
	if(action != undefined){
		eval(action);
	}
}

/**
 * 单选框
 * @param obj
 */
function checkOne(obj){
	var table = $(obj).parent();
	while (!($(table)[0].tagName == "TABLE")) {
		table = table.parent();
	}
	
	if($(obj).prop("checked")){
		table.find("._item").prop("checked", false);
		$(obj).prop("checked", true);
	}
	
	var action = $(obj).attr("action");
	if(action != undefined){
		eval(action);
	}
}

/**
 * 获取选择项的value，已逗号分割
 * @param id
 * @returns {String}
 */
function getSelectedItems(id){
	var value = "";
	var parentObj;
	if(id == undefined){
		parentObj = $("body");
	}else{
		parentObj = $("#" + id);
	}
	
	if(parentObj.length <= 0){
		return value;
	}
	
	//var allItems = parentObj.find("input[name='_allItems']");
	var items = parentObj.find("._item:checked");
	items.each(function(){
		value += $(this).val() + ",";
	});
	
	if(value != ""){
		value = value.substring(0, value.length-1);
	}
	return value;
}

/**
 * 获取时间差
 * @param startDateId
 * @param endDateId
 * @param target
 */
function getBDN(startDateId,endDateId,target){  
	target = target == undefined ? ".timelong" : target;
	if(startDateId == undefined || endDateId == undefined){
		return;
	}
	var startDate = $("#" + startDateId).val();
	var endDate = $("#" + endDateId).val();
	
	if(startDate == "" || endDate == ""){
		$(target).val(""); 
		return;
	}
    var startTime = new Date(Date.parse(startDate.replace(/-/g, "/"))).getTime();     
    var endTime = new Date(Date.parse(endDate.replace(/-/g, "/"))).getTime(); 
    var dates = Math.abs((startTime - endTime))/(1000*60*60*24) + 1;  
    
    $(target).val(dates); 
}

function getBDM(startDateId,endDateId){  
	target = ".timelong";
	if(startDateId == undefined || endDateId == undefined){
		return;
	}
	var startDate = $("#" + startDateId).val();
	var endDate = $("#" + endDateId).val();
	
	if(startDate == "" || endDate == ""){
		$(target).val(""); 
		return;
	}
    var startTime = new Date(Date.parse(startDate.replace(/-/g, "/"))).getTime();     
    var endTime = new Date(Date.parse(endDate.replace(/-/g, "/"))).getTime(); 
    var dates =Math.floor((endTime-startTime)/(24*3600*1000)/30);
    //var dates = Math.floor((startTime - endTime))/(1000*60*60*24*30);  
    
    $(target).val(dates); 
}

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}