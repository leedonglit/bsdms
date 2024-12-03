var uploadifyUtil = {

	init:function(id,cfg){
		 var UPLOAD_RULES = {
				'video': {
					allowFileExts: "*.flv;*.mp4;"
				},
				'docx': {
					allowFileExts: "*.docx;*.doc"
				},
				'kmplayer' : {
					allowFileExts: "*.avi;*.wmv;*.wma;*.flv;*.mp3;*.mp4;*.rm;*.rmvb;"
				},
				'attachment' : {
					allowFileExts: "*.docx;"
				},
				'' :{
					allowFileExts: ""
				}
			};
			var DEFAULT_CFG = {
				swf : ctxPath+'/static/pluign/uploadify/uploadify.swf', 
				height : 26,   
				width  : 52, 
				queueID: 'fileQueue',
				fileSizeLimit:'1000000KB',
				auto   : false,
				onComplete       : function (event, queueID, fileObj, response, data){    
                  $('<li></li>').appendTo('.files').text(response); },    
                onError          : function(event, queueID, fileObj){ 
                   alert("文件:" + fileObj.name + " 上传失败");}
				
			};
			
			var data = $.extend({},DEFAULT_CFG,cfg);
			var uType = data.uploadType;
			if(typeof uType != 'undefined'){
				data.fileTypeExts = UPLOAD_RULES[uType].allowFileExts;
			}
			try{
				$('#'+id).uploadify(data);
			}catch(e){
			 	alert("上传初始化："+e);
			}
	}




};