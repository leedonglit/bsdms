var check = {
	//全部选中
	checkAll:function(t,what){  
		if(t.checked === false){
			$("input[name='"+what+"']").each(function(){$(this).attr("checked",false);});
		}else{
			$("input[name='"+what+"']").each(function(){
				if($(this).attr("disabled") === undefined || $(this).attr("disabled") === false || $(this).attr("disabled") === null){
					$(this).attr("checked",true);
				}
			});
			
		}
	},
	lateralCheck:function(){
	
	},
	
	//取消或者选中其中的全选按钮跟着自动变化
	checkOther:function(qxId,name){
		$("#"+qxId).attr("checked",$("input[name='"+name+"']:checked").length==$("input[name='"+name+"']").length);
	},
};