function isMobile(){
				var sUserAgent = navigator.userAgent.toLowerCase();
			    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
			    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
			    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
			    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
			    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
			    var bIsAndroid = sUserAgent.match(/android/i) == "android";
			    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
			    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
			 
			    if (!(bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM)) {
			    	return false;
			    }else{
			    	return true;
				}
			}
var jsignature = {
		openQRCode:function(PO_ID,QZR,idx,c1,c2){
			if(!isMobile() || c1 != c2){
				return;
			}
			window.location.href= ctxPath + '/base/jsignature/openDysqlyLunz.html?order='+PO_ID+'&fzr='+QZR+"&idx="+idx+"&userId="+userId;
			/*var yijian = $("#yijian_" + QZR).val();
			var config = {
					data:{order:PO_ID,fzr:QZR,yijian:yijian},
					url:ctxPath + '/base/jsignature/openQRCode.html',
					callBackFunc:function(data){
						console.log(data);
						//页面层-图片
						layer.open({
							type: 1,
							title: false,
							closeBtn: 0,
							area: ['auto'],
							skin: 'layui-layer-nobg', //没有背景色
							shadeClose: true,
							content: "<img width='350' height='350' src='"+data+"'/>"
						});
					},
					callBackErrorFunc:function(){
					}
			};
			HD.doAjax(config);*/
		},
		saveOneOrder:function(){
			var obj = $("#myorderForm").serialize();
			var config = {
					data:obj,
					url:ctxPath+'/base/jsignature/createQz.html',
					callBackFunc:function(){
						ui.alert("保存成功！");
					},
					callBackErrorFunc:function(){
						ui.alert("保存失败！");
					}
			};
			HD.doAjax(config);
		},
		updateOneOrder:function(){
			var obj = $("#myorderForm").serialize();
			var config = {
					data:obj,
					url:ctxPath+'/base/jsignature/updateOneOrder.html',
					callBackFunc:function(){
						ui.alert("保存成功！");
					},
					callBackErrorFunc:function(){
						ui.alert("保存失败！");
					}
			};
			HD.doAjax(config);
		},
		saveJklz:function(){
			var obj = $("#jklzForm").serializeJSON();
			var config = {
					data:{obj:encodeURIComponent(JSON.stringify(obj)),order:$("input[name='PO_ID']").val()},
					url:ctxPath+'/base/jsignature/saveJklz.html',
					callBackFunc:function(){
						ui.alert("保存成功！");
					},
					callBackErrorFunc:function(){
						ui.alert("保存失败！");
					}
			};
			HD.doAjax(config);
		},
		saveYixiang:function(){
			var obj = new Object();
			obj.order = $("input[name='PO_ID']").val();
			
			
			obj.depcode = $(".hb_ul span:eq(0)").html();
			obj.depName = $(".hb_ul span:eq(1)").html();
			
			obj.year = $(".hb_ul input:eq(0)").val();
			obj.month = $(".hb_ul input:eq(1)").val();
			obj.day = $(".hb_ul input:eq(2)").val();
			
			obj.save_year = $("#save_time span:eq(0)").html();
			obj.save_month = $("#save_time span:eq(1)").html();
			obj.save_day = $("#save_time span:eq(2)").html();
			var arr = new Array();
			var pro = new Object();
			$(".table_style tr:gt(0)").each(function(){
				pro.ED_ID = $(this).find("input[name='d_id']").val();
				pro.ED_NAME = $(this).find("td:eq(1)").html();
				pro.xuqiu = $(this).find("td:eq(2) textarea").val();
				pro.total = $(this).find("td:eq(3)").html();
				pro.buyTime = $(this).find("td:eq(4) textarea").val();
				pro.remark = $(this).find("td:eq(5) textarea").val();
				arr.push(pro);
			});
			obj.pros = arr;
			var config = {
					data:{obj:encodeURIComponent(JSON.stringify(obj)),order:obj.order},
					url:ctxPath+'/base/jsignature/saveYixiang.html',
					callBackFunc:function(){
						ui.alert("保存成功！");
					},
					callBackErrorFunc:function(){
						ui.alert("保存失败！");
					}
			};
			HD.doAjax(config);
		},
		saveLunzheng:function(json){
			var obj = new Object();
			obj.order = $("input[name='PO_ID']").val();
			obj.c_one = $("textarea[name='c_one']").val();
			obj.c_two = $("textarea[name='c_two']").val();
			obj.c_three = $("textarea[name='c_three']").val();
			obj.c_four = $("textarea[name='c_four']").val();
			obj.c_five = $("textarea[name='c_five']").val();
			obj.c_six = $("textarea[name='c_six']").val();
			obj.c_seven = $("textarea[name='c_seven']").val();
			obj.c_eight = $("textarea[name='c_eight']").val();
			obj.c_nine = $("textarea[name='c_nine']").val();
			obj.c_ten = $("textarea[name='c_ten']").val();
			var config = {
					data:{obj:encodeURIComponent(JSON.stringify(obj)),order:obj.order},
					url:ctxPath+'/base/jsignature/saveLunzheng.html',
					callBackFunc:function(){
						ui.alert("保存成功！");
					},
					callBackErrorFunc:function(){
						ui.alert("保存失败！");
					}
			};
			HD.doAjax(config);
		},
		createCode:function(PO_ID){
			var config = {
					data:{order:PO_ID},
					url:ctxPath + '/base/jsignature/openLunzCode.html',
					callBackFunc:function(data){
						console.log(data);
						//页面层-图片
						layer.open({
							type: 1,
							title: false,
							closeBtn: 0,
							area: ['auto'],
							skin: 'layui-layer-nobg', //没有背景色
							shadeClose: true,
							content: "<img width='350' height='350' src='"+data+"'/>"
						});
					},
					callBackErrorFunc:function(){
					}
			};
			HD.doAjax(config);
		},
		createCodeDysqly:function(PO_ID){
			var config = {
					data:{order:PO_ID},
					url:ctxPath + '/base/jsignature/openDysqlyCode.html',
					callBackFunc:function(data){
						console.log(data);
						//页面层-图片
						layer.open({
							type: 1,
							title: false,
							closeBtn: 0,
							area: ['auto'],
							skin: 'layui-layer-nobg', //没有背景色
							shadeClose: true,
							content: "<img width='350' height='350' src='"+data+"'/>"
						});
					},
					callBackErrorFunc:function(){
					}
			};
			HD.doAjax(config);
		},
		createJklzCode:function(PO_ID){
			var config = {
					data:{order:PO_ID},
					url:ctxPath + '/base/jsignature/createJklzCode.html',
					callBackFunc:function(data){
						console.log(data);
						//页面层-图片
						layer.open({
							type: 1,
							title: false,
							closeBtn: 0,
							area: ['auto'],
							skin: 'layui-layer-nobg', //没有背景色
							shadeClose: true,
							content: "<img width='350' height='350' src='"+data+"'/>"
						});
					},
					callBackErrorFunc:function(){
					}
			};
			HD.doAjax(config);
		}
};
