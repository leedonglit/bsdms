/**

 @Name：layuiAdmin 主页控制台
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：GPL-2
    
 */


layui.define(function(exports){
  
  /*
    下面通过 layui.use 分段加载不同的模块，实现不同区域的同时渲染，从而保证视图的快速呈现
  */
  
  
  //区块轮播切换
  layui.use(['admin', 'carousel'], function(){
    var $ = layui.$
    ,admin = layui.admin
    ,carousel = layui.carousel
    ,element = layui.element
    ,device = layui.device();

    //轮播切换
    $('.layadmin-carousel').each(function(){
      var othis = $(this);
      carousel.render({
        elem: this
        ,width: '100%'
        ,arrow: 'none'
        ,interval: othis.data('interval')
        ,autoplay: othis.data('autoplay') === true
        ,trigger: (device.ios || device.android) ? 'click' : 'hover'
        ,anim: othis.data('anim')
      });
    });
    
    element.render('progress');
    
  });

  //日志量统计
  layui.use(['carousel', 'echarts'], function(){
    var $ = layui.$
    ,carousel = layui.carousel
    ,element = layui.element
    ,echarts = layui.echarts;
    
    var echartsApp = [], options = [
      //今日流量趋势
      {
        title: {
          text: '今日流量趋势',
          x: 'center',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['','']
        },
        xAxis : [{
          type : 'category',
          boundaryGap : false,
          data: ['00:00','01:00','02:00','03:00','04:00','05:00','06:00','07:00','08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00']
        }],
        yAxis : [{
          type : 'value'
        }],
        series : [{
          name:'日志量',
          type:'line',
          smooth:true,
          itemStyle: {normal: {areaStyle: {type: 'default'}}},
          data: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }]
      }
    ]
    ,elemDataView = $('#LAY-index-dataview').children('div')
    ,renderDataView = function(index){
      echartsApp[index] = echarts.init(elemDataView[index], layui.echartsTheme);
      echartsApp[index].setOption(options[index]);
      window.onresize = echartsApp[index].resize;
    };
    
    //没找到DOM，终止执行
    if(!elemDataView[0]) return;
    
    renderDataView(0);
    
    var config = {
			url : ctxPath+'/manage/getHomeData',
			data : {},
			callBackFunc : function(obj){
				//用户总数
				var userCount = obj.userCount;
				$("#userCount").text(userCount);
				//当前在线用户总数
				var userLineCount = obj.userLineCount;
				$("#userLineCount").text(userLineCount);
				//今日登陆用户总数
				var userLoginCount = obj.userLoginCount;
				$("#userLoginCount").text(userLoginCount);
				//今日日志量
				var logCount = obj.logCount;
				$("#logCount").text(logCount);
				//服务器IP
				var hostAddress = obj.hostAddress;
				$("#hostAddress").text(hostAddress);
				//服务器的当前时间
				var systemDateTime = obj.systemDateTime;
				$("#systemDateTime").text(systemDateTime);
				//服务器CPU核心数
				var cpuSize = obj.cpuSize;
				$("#cpuSize").text(cpuSize);
				//服务器内存大小
				var memerySize = obj.memerySize;
				$("#memerySize").text(memerySize);
				//服务器硬盘容量
				var diskSize = obj.diskSize;
				$("#diskSize").text(diskSize);
				//服务器JVM内存大小
				var jvmMemerySize = obj.jvmMemerySize;
				$("#jvmMemerySize").text(jvmMemerySize);
				//服务器操作系统
				var systemName = obj.systemName;
				$("#systemName").text(systemName);
				var xAxisList = obj.xAxisList;
				var dataList = obj.dataList;
				console.log(xAxisList);
				console.log(dataList);
				
				 //数据概览
				  layui.use(['carousel', 'echarts'], function(){
				    var $ = layui.$
				    ,carousel = layui.carousel
				    ,echarts = layui.echarts;
				    
				    var echartsApp = [], options = [
				      //今日日志量趋势
				      {
				        title: {
				          text: '今日日志量趋势',
				          x: 'center',
				          textStyle: {
				            fontSize: 14
				          }
				        },
				        tooltip : {
				          trigger: 'axis'
				        },
				        legend: {
				          data:['','']
				        },
				        xAxis : [{
				          type : 'category',
				          boundaryGap : false,
				          data: xAxisList
				        }],
				        yAxis : [{
				          type : 'value'
				        }],
				        series : [{
				          name:'日志量',
				          type:'line',
				          smooth:true,
				          itemStyle: {normal: {areaStyle: {type: 'default'}}},
				          data: dataList
				        }]
				      },
				    ]
				    ,elemDataView = $('#LAY-index-dataview').children('div')
				    ,renderDataView = function(index){
				      echartsApp[index] = echarts.init(elemDataView[index], layui.echartsTheme);
				      echartsApp[index].setOption(options[index]);
				      window.onresize = echartsApp[index].resize;
				    };
				    
				    //没找到DOM，终止执行
				    if(!elemDataView[0]) return;
				    
				    renderDataView(0);
				    //$("#index").html("首页");
				  });
				
			},
			callBackErrorFunc: function(){
			}
		};
		HD.doAjax(config,true);
		var config = {
			url : ctxPath+'/manage/getCpuRatioData',
			data : {},
			callBackFunc : function(obj){
				//cpu使用率
				var cpuRatio = obj.cpuRatio;
				element.progress('cpuRatio', cpuRatio);
				if(cpuRatio < '50%'){
					$("#cpuRatio").removeClass("layui-bg-red").removeClass("layui-bg-blue").addClass("layui-bg-green");
				}else if(cpuRatio > '50%' && cpuRatio < '80%'){
					$("#cpuRatio").removeClass("layui-bg-red").removeClass("layui-bg-green").addClass("layui-bg-blue");
				}else{
					$("#cpuRatio").removeClass("layui-bg-blue").removeClass("layui-bg-green").addClass("layui-bg-red");
				}
				
				//内存占用率
				var memeryRatio = obj.memeryRatio;
				element.progress('memeryRatio', memeryRatio);
				if(memeryRatio < '50%'){
					$("#memeryRatio").removeClass("layui-bg-red").removeClass("layui-bg-blue").addClass("layui-bg-green");
				}else if(memeryRatio > '50%' && memeryRatio < '80%'){
					$("#memeryRatio").removeClass("layui-bg-red").removeClass("layui-bg-green").addClass("layui-bg-blue");
				}else{
					$("#memeryRatio").removeClass("layui-bg-blue").removeClass("layui-bg-green").addClass("layui-bg-red");
				}
				
				//硬盘使用率
				var diskRatio = obj.diskRatio;
				element.progress('diskRatio', diskRatio);
				if(diskRatio < '50%'){
					$("#diskRatio").removeClass("layui-bg-red").removeClass("layui-bg-blue").addClass("layui-bg-green");
				}else if(diskRatio > '50%' && diskRatio < '80%'){
					$("#diskRatio").removeClass("layui-bg-red").removeClass("layui-bg-green").addClass("layui-bg-blue");
				}else{
					$("#diskRatio").removeClass("layui-bg-blue").removeClass("layui-bg-green").addClass("layui-bg-red");
				}
				//jvm内存占用率
				var jvmMemeryRatio = obj.jvmMemeryRatio;
				element.progress('jvmMemeryRatio', jvmMemeryRatio);
				if(diskRatio < '50%'){
					$("#jvmMemeryRatio").removeClass("layui-bg-red").removeClass("layui-bg-blue").addClass("layui-bg-green");
				}else if(diskRatio > '50%' && diskRatio < '80%'){
					$("#jvmMemeryRatio").removeClass("layui-bg-red").removeClass("layui-bg-green").addClass("layui-bg-blue");
				}else{
					$("#jvmMemeryRatio").removeClass("layui-bg-blue").removeClass("layui-bg-green").addClass("layui-bg-red");
				}
				//$("#index").html("首页");
			},
			callBackErrorFunc: function(){
			}
		};
		HD.doAjax(config,true);
    
  });

  exports('console', {})
});