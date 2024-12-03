var lindex = 0;
var list_index;
var ui ={
		popProcess:function(text, eclipseObj){//遮罩层，常用系统请求等待操作....
			var index = layer.load(0, {
			    shade: [0.2,'#ccc'] //0.1透明度的白色背景
			});
		},
		shutProcess:function(eclipseObj){//关闭系统所有遮罩层
			layer.closeAll("loading");
			
		},
		alert:function(msg){//单消息对话框
			layer.alert(msg, {
			    skin: 'layui-layer-lan' //样式类名layui-layer-lan
			    ,closeBtn: 1,shift: 6,icon:7,offset: ['38%', '40%']
			});
			return;
		},
		alertFun:function(msg,fun){//对话框带回调函数
			layer.alert(msg, function(index){
				  if(typeof(fun) == "function"){
					  fun();
				  }
				  layer.close(index);
			});       
		},
		msg:function(msg){//纯消息通知，无任何操作按钮
			layer.msg(msg, {
			    offset: 0,
			    shift: 6
			});
			return;
		},
		lodding:function(){//加载中遮罩层
			layer.load(1, { shade: [0.1,'#fff'] ,offset: ['45%', '49%'], }); 
		},
		closeWin:function(){//关闭界面所有弹出层
			layer.closeAll();
		},
		tips:function(obj,msg,loc,color){//对象，消息，位置 上1 下3 右2 左4
			if(msg==""||msg==null){
				msg = "&nbsp;";
			}
			list_index = layer.tips(msg, obj, {
			    tips: [loc, (color==undefined||color==null)?'#52B3EA':color],
			    time:400000,
			    closeBtn:[0,true]
			});
		},
		confirm:function(msg,fun){//询问对话框
			var index=layer.confirm(msg, {
				skin: 'layui-layer-lan',
				offset: ['38%', '40%'],
				icon:3,
			    btn: ['YES','NO']//按钮
			}, function(){
				//fun();  错误
				eval(fun);
				layer.close(index);
			}, function(){
			    layer.close(index);
			});
		},
		confirmBtn:function(msg,fun1,fun2,btn1,btn2){//询问对话框，多按钮
			btn1 = btn1 == undefined ? '确认' : btn1;
			btn2 = btn2 == undefined ? '取消' : btn2;
			var index=layer.confirm(msg, {
				skin: 'layui-layer-lan',
				offset: ['38%', '40%'],
				icon:3,
			    btn: [btn1,btn2]//按钮
			}, function(){ 
				eval(fun1);
				layer.close(index);
			}, function(){
				eval(fun2);
			    layer.close(index);
			});
		},
		confirmBoolean:function(msg){//询问对话框，返回点击按钮参数
			var index=layer.confirm(msg, {
				skin: 'layui-layer-lan',
				offset: ['38%', '40%'],
				icon:3,
			    btn: ['确认','取消']//按钮
			}, function(){ 
				layer.close(index);
				return true;
			}, function(){
			    layer.close(index);
			    return false;
			});
		},
		confirmDoAjax:function(msg,config){//询问对话框，传入点击执行参数对象
			var index=layer.confirm(msg, {
				skin: 'layui-layer-lan',
				offset: ['38%', '40%'],
				icon:3,
			    btn: ['YES','NO']//按钮
			}, function(){
				HD.doAjax(config);
				layer.close(index);
			}, function(){
			    layer.close(index);
			});
		},
		loadActiveLert:function(content){
			layer.open({
				type:1,
				title:false,
				closeBtn:0,
				shadeClose:false,
				skin:'yourclass',
				content:"<div style='width:110px;float:left;text-align:left;margin-left:50px;margin-top:65px'>"+content+"</div><div id='point_anim' style='width:100px;float:left;text-align:left;margin-top:65px'>.</div>"
			});
			var n = 0;
			var s = "";
			setInterval(function(){
				n = n > 12?0:++n;
				s = n > 0?s + ".":"";
				console.log(s);
				$("#point_anim").html(s);
			},150);
		},
		getCurrTime:function(prev,obj){
			var d = new Date();
			var year = d.getFullYear();
			var month = (d.getMonth() < 9)?("0" + (d.getMonth() + 1)):d.getMonth() + 1;
			//var date = d.getDate();
			var date = (d.getDate() < 10)?("0" + d.getDate()):d.getDate();
			var hour = (d.getHours() < 10)?("0" + d.getHours()):d.getHours();
			var min = (d.getMinutes() < 10)?("0" + d.getMinutes()):d.getMinutes();
			var sec = (d.getSeconds() < 10)?("0" + d.getSeconds()):d.getSeconds();
			var now = year + "-" + month + "-" + date + " " + hour + ":" + min + ":" + sec;
			obj.html(now);
		},
		loadCurrTime:function(prev,obj){
			ui.getCurrTime(prev,obj);
			setInterval(function(){ui.getCurrTime(prev,obj)},1000);
		},
		createWind:function(config){//弹出一个窗体
			if(config){
				var cfg = {
						type : "POST"
					};
					jQuery.extend(cfg, config);
					if (typeof(cfg.data)=='undefined')
						cfg.data={}; 
					if(config.title === undefined){
						config.title = '	窗口';
					}	
					if(config.closeBtn === undefined){
						config.closeBtn = true;
					}	
					cfg.data.isAjax=true;//标记Ajax请求
				    $.ajax({ 
					  	type:cfg.type, 
					  	url:cfg.url,
					  	data:cfg.data,
					  	dataType: "text", 
					  	contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					  	success:function(res){
					  		var h = $(window).height();
					  		var w = $(document.body).width();
					  		//top.Dialog.open({InnerHtml:res,Title:"普通窗口"});
					  		layer.open({
					            type: 1,
					            title: config.title,
					            fix: false, //不固定
					            closeBtn:config.closeBtn,
					            maxmin: config.maxmin==undefined?false:config.maxmin,
					            offset: config.offset==undefined?[(h/2-(config.height)/2)+"px",(w/2-config.width/2)+"px"]:config.offset,
					            skin: 'layui-layer-lan', //加上边框
							    area: [config.width==undefined?"800":config.width+'px', config.height==undefined?"600":config.height+'px'],
					            content: res ,
					        });
					  		lindex = layer.index;
					  		//如果有回调函数则回调
					  		if (cfg.callBackFunc){  
			                   cfg.callBackFunc(res);
			                }
					  		//ui.reloadUi()
					  	},
					  	error:function(XMLHttpRequest, textStatus){ 
					  		ui.alert("加载页面异常"+textStatus);
					  	}
				  	});
			}
		},
		createWindWithEnd:function(config){//弹出一个窗体关闭后触发时间
			if(config){
				var cfg = {
						type : "POST"
					};
					jQuery.extend(cfg, config);
					if (typeof(cfg.data)=='undefined')
						cfg.data={}; 
					if(config.title === undefined){
						config.title = '	窗口';
					}	
					if(config.closeBtn === undefined){
						config.closeBtn = true;
					}	
					cfg.data.isAjax=true;//标记Ajax请求
				    $.ajax({ 
					  	type:cfg.type, 
					  	url:cfg.url,
					  	data:cfg.data,
					  	dataType: "text", 
					  	contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					  	success:function(res){
					  		var h = $(window).height();
					  		var w = $(document.body).width();
					  		//top.Dialog.open({InnerHtml:res,Title:"普通窗口"});
					  		layer.open({
					            type: 1,
					            title: config.title,
					            fix: false, //不固定
					            closeBtn:config.closeBtn,
					            maxmin: config.maxmin==undefined?false:config.maxmin,
					            offset: config.offset==undefined?[(h/2-(config.height)/2)+"px",(w/2-config.width/2)+"px"]:config.offset,
					            skin: 'layui-layer-lan', //加上边框
							    area: [config.width==undefined?"800":config.width+'px', config.height==undefined?"600":config.height+'px'],
					            content: res ,
					            end:config.endFunc
					        });
					  		lindex = layer.index;
					  		//如果有回调函数则回调
					  		if (cfg.callBackFunc){  
			                   cfg.callBackFunc(res);
			                }
					  		//ui.reloadUi()
					  	},
					  	error:function(XMLHttpRequest, textStatus){ 
					  		ui.alert("加载页面异常"+textStatus);
					  	}
				  	});
			}
		},
		//当select导致弹窗被顶出滚动条时调用此方法-->type: 5
		createSelectWind:function(config){//弹出一个窗体
			if(config){
				var cfg = {
						type : "POST"
					};
					jQuery.extend(cfg, config);
					if (typeof(cfg.data)=='undefined')
						cfg.data={}; 
					if(config.title === undefined){
						config.title = '	窗口';
					}	
					if(config.closeBtn === undefined){
						config.closeBtn = true;
					}	
					cfg.data.isAjax=true;//标记Ajax请求
				    $.ajax({ 
					  	type:cfg.type, 
					  	url:cfg.url,
					  	data:cfg.data,
					  	dataType: "text", 
					  	contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					  	success:function(res){
					  		var h = $(window).height();
					  		var w = $(document.body).width();
					  		//top.Dialog.open({InnerHtml:res,Title:"普通窗口"});
					  		layer.open({
					            type: 5,
					            title: config.title,
					            fix: false, //不固定
					            closeBtn:config.closeBtn,
					            maxmin: config.maxmin==undefined?false:config.maxmin,
					            offset: config.offset==undefined?[(h/2-(config.height)/2)+"px",(w/2-config.width/2)+"px"]:config.offset,
					            skin: 'layui-layer-lan', //加上边框
							    area: [config.width==undefined?"800":config.width+'px', config.height==undefined?"600":config.height+'px'],
					            content: res ,
					        });
					  		lindex = layer.index;
					  		//如果有回调函数则回调
					  		if (cfg.callBackFunc){  
			                   cfg.callBackFunc(res);
			                }
					  		//ui.reloadUi()
					  	},
					  	error:function(XMLHttpRequest, textStatus){ 
					  		ui.alert("加载页面异常"+textStatus);
					  	}
				  	});
			}
		}
};
