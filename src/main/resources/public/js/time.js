/**
 * 
 */
$(function(){
	function time(){
		var d = new Date();
		var year = d.getFullYear();
		var month = (d.getMonth() < 9)?("0" + (d.getMonth() + 1)):d.getMonth() + 1;
		//var date = d.getDate();
		var date = (d.getDate() < 10)?("0" + d.getDate()):d.getDate();
		var hour = (d.getHours() < 10)?("0" + d.getHours()):d.getHours();
		var min = (d.getMinutes() < 10)?("0" + d.getMinutes()):d.getMinutes();
		var sec = (d.getSeconds() < 10)?("0" + d.getSeconds()):d.getSeconds();
		var now = year + "-" + month + "-" + date + " " + hour + ":" + min + ":" + sec;
		$("#time").html("当前真实时间："+now);
	}
	time();
	setInterval(function(){
		time();
	},1000);
});
