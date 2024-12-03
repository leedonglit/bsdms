/****JS的一些相关操作、包括AJAX操作、提示框、对话框、等等****/
var HD ={
		/***统一POST 提交 config 参数说明 config:{url:"发送请求地址"，data："待发送 Key/value 参数。"，callBackFunc ："发送成功时回调函数。",}**/
		doPost:function(config){
			var cfg = {
					type : "JSON"
			};
			jQuery.extend(cfg, config);
			if (typeof(cfg.data)=='undefined')
			{
				cfg.data={};
			};

			$.post( cfg.url,
					cfg.data, 
					cfg.callBackFunc,
					cfg.type);	

		},
		/**统一AJAX操作**/
		doAjax:function(config,async){
			var cfg = {
					type : "POST"
			};
			jQuery.extend(cfg, config);
			if (typeof(cfg.data)=='undefined'){
				cfg.data={};
			}
			if(async == undefined){
				async = true;
			}
			cfg.data.isAjax=true;//标记Ajax请求
			$.ajax({ 
				type:cfg.type, 
				url:cfg.url,
				data:cfg.data,
				dataType: "json",
				async:async,
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				//contentType:'application/json; charset=UTF-8',
				success:function(res){
					//Session检测  如果失效  则跳转到指定页面
					if(res.sessiontimeout)
					{	
						if (top.location != self.location){
							top.location=self.location;  
						}else{
							window.location.href=res.data;
						}
						return ;
					};
					if (res.success){
						if (cfg.callBackFunc){  
							cfg.callBackFunc(res.data);
						}else{
							ui.alert("未找到成功回调函数！");
						}
					}else{
						if(cfg.callBackErrorFunc){
							cfg.callBackErrorFunc(res);
						}else{
							//alert("处理异常"+res.errmsg+":"+res.status);
							ui.msg("System Abnormality！");
						}
					}
				},
				error:function(XMLHttpRequest, textStatus){ 
					ui.msg("System abnormality！");
					if (XMLHttpRequest.status==8401){
						window.location=ctxPath;
					} else {
						if(cfg.errorCallBackFunc) {
							cfg.errorCallBackFunc();
						}
					}

				}
			});

		},
		doGet:function(config){
			var cfg = {
					type : "JSON"
			};
			jQuery.extend(cfg, config);
			if (typeof(cfg.data)=='undefined')
			{
				cfg.data={};
			};
			$.get(cfg.url,
					cfg.data, 
					cfg.callBackFunc,
					cfg.type);
		},

		doLoad:function(config){
			if (typeof(config.data)=='undefined'){
				config.data={};
			}
			config.data._postTime=new Date().getTime();
			config.data.isAjax=true;//标记Ajax请求
			config.data.type="get";
			$(config.obj).load(config.url,config.data,function(response,status,xhr){
				if(status == 'success'){
					if(config.callBackFunc){
						config.callBackFunc();
					}else{
						ui.alert("未找到成功回调函数！");
					}
				}
				if(status == "timeout"){
					ui.alert("请求超时！");
				}
				if(status == "error"){
					ui.alert("请求失败，错误的请求地址！");
				}
			});
		},

		load:function(config){
			if (typeof(config.data)=='undefined')
				config.data={};
			config.data._postTime=new Date().getTime();
			config.data.isAjax=true;//标记Ajax请求
			config.data.type="get";
			$.ajax({type:'POST', url: config.url,data:config.data,
				//contentType:'application/json;charset=utf-8',
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				success: function(res){
					/*try{
						//Session检测  如果失效  则跳转到指定页面
						eval("var obj="+res);
						if(obj.sessiontimeout)
						{
							//window.location.href=obj.data;
							if (top.location != self.location){
								top.location=self.location;  
							}else{
								window.location.href=res.data;
							}
							return ;
						};
					}
					catch(e)
					{
						alert("系统异常，未知错误！");
					};*/
					debugger
					$(config.obj).html(res);
					if(config.callBackFunc){
						config.callBackFunc();
					}else{
						ui.alert("未找到成功回调函数！");
					}
				},error:function(XMLHttpRequest, textStatus){
					ui.msg("System abnormality！");
					if (XMLHttpRequest.status==8401)
						window.location=ctxPath;
				}});
		},
		loadAsy:function(config){
			if (typeof(config.data)=='undefined')
				config.data={};
			config.data._postTime=new Date().getTime();
			config.data.isAjax=true;//标记Ajax请求
			config.data.type="get";
			$.ajax({type:'POST', url: config.url,data:config.data,async:false,
				//contentType:'application/json;charset=utf-8',
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				success: function(res){
					try{
						//Session检测  如果失效  则跳转到指定页面
						eval("var obj="+res);
						if(obj.sessiontimeout)
						{
							//window.location.href=obj.data;
							if (top.location != self.location){
								top.location=self.location;  
							}else{
								window.location.href=res.data;
							}
							return ;
						};
					}
					catch(e)
					{
						//console.log("");
					};
					$(config.obj).html(res);
					if(config.callBackFunc){
						config.callBackFunc();
					}else{
						ui.alert("未找到成功回调函数！");
					}
				},error:function(XMLHttpRequest, textStatus){
					ui.msg("System abnormality！");
					if (XMLHttpRequest.status==8401)
						window.location=ctxPath;
				}});
		},
		//带有遮罩层的ajax提交   基于doAjax 方法
		doCondition : function(config){
			ui.popProcess("数据加载中......", config.eclipseObj);
			var tempFunc = config.callBackFunc;
			var tempErroFunc = config.callBackErrorFunc
			function callback(obj) {
				if (tempFunc) 
					tempFunc(obj);
				ui.shutProcess();
			};
			function Errocallback(obj){
				if (tempErroFunc) 
					tempErroFunc(obj);
				ui.shutProcess();
			};

			config.callBackFunc = callback;
			config.callBackErrorFunc = Errocallback;
			HD.doAjax(config);
		},
		doCondition : function(config,async){
			ui.popProcess("数据加载中......", config.eclipseObj);
			var tempFunc = config.callBackFunc;
			var tempErroFunc = config.callBackErrorFunc
			function callback(obj) {
				if (tempFunc) 
					tempFunc(obj);
				ui.shutProcess();
			};
			function Errocallback(obj){
				if (tempErroFunc) 
					tempErroFunc(obj);
				ui.shutProcess();
			};

			config.callBackFunc = callback;
			config.callBackErrorFunc = Errocallback;
			HD.doAjax(config,async);
		},
		//带有遮罩层的加载页面DIV  Load的方式
		doConditionRoad : function(config){
			ui.popProcess("数据加载中......", config.eclipseObj);

			var tempFunc = config.callBackFunc;
			function callback(obj) {
				if (tempFunc) 
					tempFunc(obj);
				ui.shutProcess();
			}
			config.callBackFunc = callback;
			HD.load(config);
		},
		//设置页面排序
		//coum 待排序的字段 type 0 升序 1降序
		setPageSort:function(coum,type){
			//存入点击列的每一个TD的内容；
			var aTdCont = [];
			//点击列的索引值
			var thi = 0

			//重新对TR进行排序
			var setTrIndex = function(tdIndex){
				for(i=0;i<aTdCont.length;i++){
					var trCont = aTdCont[i];
					$("tbody tr").each(function() {
						var thisText = $(this).children("td:eq("+tdIndex+")").text();

						if(thisText == trCont){
							$("tbody").append($(this));
						}
					});		
				}
			}

			//比较函数的参数函数
			var compare_down = function(a,b){
				if(a>=b)
					return 1
					else
						return 0;
			}

			var compare_up = function(a,b){
				if(a>=b)
					return 0
					else
						return 1;
			}

			//比较函数
			var fSort = function(compare){
				aTdCont.sort(compare);
			}

			//取出TD的值，并存入数组,取出前二个TD值；
			var fSetTdCont = function(thIndex){
				$("tbody tr:gt(0) ").each(function() {
					var tdCont = $(this).children("td:eq("+thIndex+")").text();
					aTdCont.push(tdCont);
				});
			}
			//点击时需要执行的函数
			var clickFun = function(thindex){
				aTdCont = [];
				//获取点击当前列的索引值
				var nThCount = thindex;
				//调用sortTh函数 取出要比较的数据
				fSetTdCont(nThCount);
			}


			$("#"+coum).click(function(){
				$("#"+coum).unbind("click");
				//调用比较函数,降序

				thi= $(this).index();
				if(type==1){
					clickFun(thi);
					fSort(compare_up);
					//重新排序行
					setTrIndex(thi);
				}else if(type==0){
					clickFun(thi);
					fSort(compare_down);
					//重新排序行
					setTrIndex(thi);
				}

			});


		},

		cascadeCheckBoxClick:function(isAll, checkBoxAllID, checkBoxName) {
			if (isAll) {// 级联 最顶层box
				//alert($("#" + checkBoxAllID).attr("checked")== 'checked');
				if ($("#" + checkBoxAllID).attr("checked") ) { // 全选

					$("input[name='" + checkBoxName + "']").each(function() {
						$(this).attr("checked", true);
					});
				} else { // 取消全选
					$("input[name='" + checkBoxName + "']").each(function() {
						$(this).attr("checked", false);
					});
				}
			} else {// 具体的box
				if ($("input[name='" + checkBoxName + "']:checked").length <= 0) {
					$("#" + checkBoxAllID).attr("checked", false);
				}
			}
		},

		getCheckBoxIDStr : function(checkBoxName) {
			var idStrArr = new Array();
			$("input[name='" + checkBoxName + "']:checked").each(function() {
				idStrArr.push($(this).attr("value"));
			});

			var idStr = idStrArr.join(",");
			return idStr;
		},

		getNoCheckBoxIDStr:function(checkBoxName){
			var idStrArr = new Array();
			$("input[name='" + checkBoxName + "']").each(function() {
				if($(this).attr("checked")==false)
					idStrArr.push($(this).attr("value"));
			});
			var idStr = idStrArr.join(",");
			return idStr;
		}
};