var uploadutil = {

	uploadify : function(id,cfg){
			var UPLOAD_RULES = {
				'video': {
					allowFileExts: "*.flv;*.mp4;"
				},
				'docx': {
					allowFileExts: "*.docx;"
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
				fileSizeLimit:'1000000KB'
				
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