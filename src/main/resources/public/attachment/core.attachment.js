var attachment = {
		init:function(config){
			config.obj.uploadify({
				height : config.height==undefined?40:config.height,
				swf : ctxPath+'/plugins/uploadify/uploadify.swf',
				uploader : config.url,
				width : config.width==undefined?70:config.width,
				buttonClass:'uploadBtn',
				buttonText:'' ,
				fileTypeExts:'*.xls;*.xlsx;*.jpeg;*.jpg;*.png;*.docx;*.doc;*.pdf;*.rar;*.zip',
				queueID:config.queueID==undefined?"fileQueue":config.queueID,
				onUploadSuccess:function (file, data, response) {
					/*var obj = JSON.parse(JSON.parse(data).data);
					for(var i = 0;i<obj.length;i++){
						var tr = "<tr style='line-height:25px'><td width='70%' style='line-height:25px'><a href='javascript:;' onclick=\"attachment.openFile('"+obj[i].uuidName+"')\">"+obj[i].realFileName+"</a></td>"+
						"<td align='center' style='line-height:25px'><a href='javascript:;' onclick=\"attachment.openFile('"+obj[i].uuidName+"')\">查看</a>&nbsp;|&nbsp;"+
						"<a href='javascript:;' onclick=\"attachment.downloadFile('"+obj[i].uuidName+"')\">下载</a>&nbsp;|&nbsp;"+
						"<a href='javascript:;' onclick=\"attachment.delFile(this,'"+obj[i].uuidName+"')\">删除</a></td></tr>";
						var trs = $("."+config.moduleName+" tr");
						if(trs.length==0){
							$("."+config.moduleName).append(tr);
							$("."+config.moduleName).append("<tr><td style='height:5px' colspan='2'><hr></td></tr>");
						}else{
							$("."+config.moduleName+" tr:eq(0)").before(tr);
							$("."+config.moduleName+" tr:eq(0)").after("<tr><td style='height:5px' colspan='2'><hr  style='border:1px dashed #ccc'></td></tr>");
						}
					}*/
					eval(config.onUploadSuccess(data));
				}
			});
		},
		initFive:function(config){
			config.obj.uploadifive({
				'uploadScript' : config.uploadScript+"?"+attachment.getUrlParam(config.param,null),
				'buttonText':'' ,
				'height' : config.height==undefined?40:config.height,
				'width' : config.width==undefined?70:config.width,
				'queueID':config.queueID==undefined?"fileQueue":config.queueID,
				'auto':true,
				'buttonClass':'uploadBtn',
				'removeCompleted':true,
				'onUploadComplete':function (file, data) {
					/*var obj = JSON.parse(data).data;
					obj = JSON.parse(obj);
					for(var i = 0;i<obj.length;i++){
						var tr = "<tr style='line-height:25px'><td width='70%' style='line-height:25px'><a href='javascript:;' onclick=\"attachment.openFile('"+obj[i].uuidName+"')\">"+obj[i].realFileName+"</a></td>"+
						"<td align='center' style='line-height:25px'><a href='javascript:;' onclick=\"attachment.openFile('"+obj[i].uuidName+"')\">查看</a>&nbsp;|&nbsp;"+
						"<a href='javascript:;' onclick=\"attachment.downloadFile('"+obj[i].uuidName+"')\">下载</a>&nbsp;|&nbsp;"+
						"<a href='javascript:;' onclick=\"attachment.delFile(this,'"+obj[i].uuidName+"')\">删除</a></td></tr>";
						var trs = $("."+config.moduleName+" tr");
						if(trs.length==0){
							$("."+config.moduleName).append(tr);
							$("."+config.moduleName).append("<tr><td style='height:5px' colspan='2'><hr></td></tr>");
						}else{
							$("."+config.moduleName+" tr:eq(0)").before(tr);
							$("."+config.moduleName+" tr:eq(0)").after("<tr><td style='height:5px' colspan='2'><hr  style='border:1px dashed #ccc'></td></tr>");
						}
					}*/
					eval(config.onUploadSuccess(data));
				}
			});
		},
		getUrlParam:function(param, key){
			var paramStr="";
			if(param==undefined){
				return paramStr;
			} 
		    if(param instanceof String||param instanceof Number||param instanceof Boolean){
		        paramStr+="&"+key+"="+encodeURIComponent(param);
		    }else{
		        $.each(param,function(i){
		            var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
		            paramStr+='&'+attachment.getUrlParam(this, k);
		        });
		    }
		    return paramStr.substr(1);
		},
		openFile:function(filename){
			var flag = attachment.getFileType(filename);
			if(flag == "image" || flag == "office"){
				var config = {
					width:900, 
					height:630,
					title:'查看附件',
					maxmin:true,
					url:ctxPath+'/sys/adjunct/openFile.html?ft='+flag+"&filename="+filename
				};
				ui.createWind(config);
			}else{
				ui.alert("该文件不支持打开，请下载查看！");
			}
		},
		downloadFile:function(oid){
			window.location.href = ctxPath + "/sys/adjunct/downloadOnefile.html?jid="+oid;
		},
		delFile:function(t,oid){
			var config = {
				data:{file:oid},
				url:ctxPath+'/sys/adjunct/delFileByMore.html',
				callBackFunc:function(data){
					if(data){
						$(t).parent().parent().next().remove();
						$(t).parent().parent().remove();
					}
					else{
						ui.alert("删除失败！");
					}
				},
				callBackErrorFunc:function(){
					ui.alert("删除失败！");
				}
			};
			ui.confirmDoAjax('确认删除该附件?',config);
		},
		getFileType:function(filename){
			var index1=filename.lastIndexOf(".");  
			var index2=filename.length; 
			var postf=filename.substring(index1,index2);
			var imgFilter=".jpeg|.gif|.jpg|.png|.bmp|";
			var officeFilter=".doc|.docx|.xls|.xlsx|.ppt|.pptx|.pdf|";
            var strPostfix=postf + '|';        
            strPostfix = strPostfix.toLowerCase();
            if(imgFilter.indexOf(strPostfix)>-1)
            {
                return "image";
            }
            if(officeFilter.indexOf(strPostfix)>-1)
            {
                return "office";
            }
		}
}
