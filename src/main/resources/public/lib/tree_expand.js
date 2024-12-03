var treeExpand = {
		//伸缩
		flex:function(treeID,obj){
			var treeObj = $.fn.zTree.getZTreeObj(treeID.id);
			var flexFlag = $(obj).attr("value");
			treeObj.expandAll(!flexFlag);
			$(obj).attr("value",!flexFlag);
			if(flexFlag){
				$(obj).text("◀▶");
			}else{
				$(obj).text("▶◀");
			}
		},
		//复选框
		checkPick:function(treeID,obj){
			var treeObj = $.fn.zTree.getZTreeObj(treeID.id);
			var node = treeObj.getNodes();
			var nodes = treeObj.transformToArray(node);
			if(nodes.length > 0){
				var checkFlag = !$(obj).attr("value");
				for(var i = 0;i < nodes.length; i++){
					nodes[i].nocheck = checkFlag;
					treeObj.updateNode(nodes[i]);
				}
				$(obj).attr("value",checkFlag);
			}
		},
		//字体变化
		fontResize:function(change,obj,treeID){
			var fontSize = $(obj).parent().attr("value");
			fontSize = parseInt(fontSize);
			if(fontSize+change > 23 || fontSize+change < 10){
				return;
			}
			fontSize += change;
			$("#"+treeID.id+" *").css("font-size",fontSize+"px");
			$(obj).parent().attr("value",fontSize);
		},
		//使ztree所有节点去掉复选框
		removeCheck:function(treeID){
			var treeObj = $.fn.zTree.getZTreeObj(treeID);
			var node = treeObj.getNodes();
			var nodes = treeObj.transformToArray(node);
			if(nodes.length > 0){
				for(var i = 0;i < nodes.length; i++){
					nodes[i].nocheck = true;
					treeObj.updateNode(nodes[i]);
				}
			}
		},
};