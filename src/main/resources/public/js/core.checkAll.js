var check = {
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
	
	}
};