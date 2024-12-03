var olmap = {
		loadMap:function(container){
			var overlay = new ol.Overlay({});
			//定义一个地图调用函数
			var BaseMapLayer = function(options) {
				var layer = new ol.layer.Tile({
					source : new ol.source.XYZ({
						url : options.url,//瓦片图片路径
						wrapX : false,
						tilePixelRatio : 1,
						//    minZoom:2,//最小显示层级
						//    maxZoom:12//最大显示层级
					})
				});
				return layer;
			};
			//影像地图组
			var sate = new ol.layer.Group({
				layers : [ new BaseMapLayer({url : mapServer}),new BaseMapLayer({url : overlay_mapServer})]
			});
			//地图缩放标尺
			var scaleLineControl = new ol.control.ScaleLine();
			return new ol.Map({
				layers : [ sate ],
				overlays : [ overlay ],
				target : container,
				controls : ol.control.defaults({
					attributionOptions : ({
						collapsible : false
					})
				}).extend([ scaleLineControl ]),
				view : new ol.View({
					projection : 'EPSG:4326',
					//	center:ol.proj.fromLonLat([0,0]),//本层级中心点，转换成规定坐标
					center : [ 0, 0 ],//本层级中心点，转换成规定坐标
					zoom : 3,//本层级初始显示层级
					minZoom : 2,
					maxZoom : 10,
				})
			});
		} 
};
