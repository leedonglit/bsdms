var page={
    getQformVal:function(id){
		var cds=[];
		$("#"+id+" input").each(function(i){
      if(this.type!='button' && this.type!='submit'){
      		var obj = $(this);
			if(obj.attr("value")!='') {
				var _need = true;
				if(obj.hasClass('dValue')) {
					if(obj.attr("value") == obj.attr('hintValue')) {
						_need = false;
					}
				}					
				if(_need) { 
					cds[cds.length]={
							col:  obj.attr("name"),
							value:obj.attr("value"),
							type:obj.attr("op")   
						};
				}
				
      		}
      }
	});
		
		$("#"+id+" select").each(function(i){
	       if(this.type!='button' && this.type!='submit'){
	       		var obj = $(this);
				if(obj.attr("value")!='') {
					cds[cds.length]={
						col:  obj.attr("name"),
						value:obj.attr("value"),
						type:obj.attr("op"),
						valueType:obj.attr("valueType")
					};
	       		}
	       }
 		});
 		
 		var pageNo = this.getPageNo(id); 		
 		var pageSize = this.getPageSize(id);
 		var jsonpage=  {
		 		condition:cds,
		 		pageNo:pageNo,
		 		pageSize:pageSize
	 	};
	 	var softcolumn = $("#"+id).attr("softcolumn");
	 	if(softcolumn!="" && softcolumn!=undefined){
	 		jsonpage.sort = softcolumn;
	 	}
	 	var softflag = $("#"+id).attr("softflag");
	 	if(softflag!="" && softflag!=undefined){
	 		jsonpage.dir = softflag;
	 	}
	 	return jsonpage;
		
	},
	
	getPageNo : function(id){
		var pageNo = $("#"+id).attr("pageNo");
 		var pageNoNow = $("#"+id).attr("pageNoNow");
 		
 	    /*if(pageNoNow!='1'){*/
 		if(pageNoNow == undefined){
  	     	pageNo='1';
  	     	$("#"+id).attr("pageNo","1");
  	     	$("#"+id).attr("pageNoNow","-1");
 	    }
 		
 		if(pageNo=="-1"){
 			$("#"+id).attr("pageNo","1");
 			pageNo = 1;
 		}
 		
 		if(pageNo==undefined || pageNo==""){
 			pageNo = 1;
 		}
 		return pageNo;
	},
	
	getPageSize:function(id){
		var pagesize = $("#"+id).attr("pagesize");
		if(pagesize==undefined || pagesize==""){
 			pagesize = 10;
 		}
 		
 		return pagesize;
	},
	getPageSizes:function()
	{
		
	
	},
	doCondition : function(config){
		ui.popProcess("数据加载中......", config.eclipseObj);
 		var data={
 			jsonpage:JSON.stringify(config.data)
 		};
 		config.data = "";
 		config.data = data;
 		var tempFunc = config.callBackFunc;
 		function callback(obj) {
 			if (tempFunc) 
 				tempFunc(obj);
 			ui.shutProcess();
 		}
 		config.callBackFunc = callback;
 		HD.doAjax(config);
	},
	

	
	getConditionQformVal:function(id){
		var cds=[];
	  $("#"+id+" input").each(function(i){
      if(this.type!='button' && this.type!='submit'){
      		var obj = $(this);
			if(obj.attr("value")!='') {
				var _need = true;
				if(obj.hasClass('dValue')) {
					if(obj.attr("value") == obj.attr('hintValue')) {
						_need = false;
					}
				}					
				if(_need) { 
					var nam = obj.attr("name");
					var vals = obj.attr("value");
					cds[cds.length]={
							col:  obj.attr("name"),
							value:[obj.attr("value")],
							type:obj.attr("op"),
							valueType:obj.attr("valueType")    
						
					};
					
				}
				
      		}
      }
	});
	$("#"+id+" select").each(function(i){
	       if(this.type!='button' && this.type!='submit'){
	       		var obj = $(this);
				if(obj.find("option:selected").val()!='') {
					cds[cds.length]={
						col:  obj.attr("name"),
						value:[obj.attr("value")],
						type:obj.attr("op"),
						valueType:obj.attr("valueType")
					};
	       		}
	       }
 		});
 		var pageNo = this.getPageNo(id); 		
 		var pageSize = this.getPageSize(id);
 		var jsonpage=  {
		 		condition:cds,
		 		pageNo:pageNo,
		 		pageSize:pageSize
	 	};
	 	return jsonpage;
	}
};