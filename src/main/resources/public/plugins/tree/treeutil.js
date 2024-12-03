var treeutil = {

	/**
	 * 树的初始化
	 * id			待初始化的标签id
	 * setting		树的基本属性
	 * nodes		树的节点数组
	**/
	treeinit:function(id,setting,nodes){
		//添加扩展组件
		if(setting.toolbar){
			var treeID = id.substring(id.indexOf("#")+1,id.length);
			var tooldata = setting.tooldata;
			if(tooldata){
				var content = "<div id='ztree_Exp'>"+
								"<div value='18' style='margin-left: 14px;margin-top: 10px;'>";
				var split = tooldata.split(",");
				if(split[0] == 1){
					content += "<span class='layui-badge' title='伸缩' onclick='treeExpand.flex("+treeID+",this)' value='true'>▶◀</span>"
				}
				if(split[1] == 1){
					content += "<span id='checkSpan' class='layui-badge' title='复选框' onclick='treeExpand.checkPick("+treeID+",this)' value='true'>" +
									"<input type='checkbox'/></span>"
				}
				if(split[2] == 1){
					content += "<span class='layui-badge' title='加大字体' onclick='treeExpand.fontResize(1,this,"+treeID+")'>+</span>"
				}
				if(split[3] == 1){
					content += "<span class='layui-badge' title='减小字体' onclick='treeExpand.fontResize(-1,this,"+treeID+")'>-</span>"
				}
				content += "</div></div>";
				$(id).parent().before(content);
			}else{
				$(id).parent().before("<div id='ztree_Exp'>"+
						"<div value='18' style='margin-left: 14px;margin-top: 10px;'>"+
							"<span class='layui-badge' title='伸缩' onclick='treeExpand.flex("+treeID+",this)' value='true'>▶◀</span>"+
							"<span id='checkSpan' class='layui-badge' title='复选框'  onclick='treeExpand.checkPick("+treeID+",this)' value='true'>" +
									"<input type='checkbox'/></span>"+
							"<span class='layui-badge' title='加大字体' onclick='treeExpand.fontResize(1,this,"+treeID+")'>+</span>"+
							"<span class='layui-badge' title='减小字体' onclick='treeExpand.fontResize(-1,this,"+treeID+")'>-</span>"+
						"</div>"+
					"</div>")
			}
			
			$("#ztree_Exp span")
				.css({"margin-left":"10px",
					"background-color": "#f2f2f2",
					"color": "black",
					"cursor": "pointer",
					"line-height": "18px",
				})
				
			$("#"+treeID).css({"margin-top":"-7px"})
		}
		$.fn.zTree.init($(id), setting,nodes);
	},
	
	//响应checkbox的选中/取消的回调函数
	treeCheck:function(event,treeId,treeNode){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getChangeCheckedNodes();
		//var nodearry = treeObj.transformToArray(nodes);
		var ids = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			ids = nodes[i].id +","+ ids;
		}
		
		treeutil.clearCheckedOldNodes("sqfunmenutree");
		
		
	},
	
	/**
	 *	获取树的checkbox改变的nodeid
	 *  返回值：JSON {ids,cancelids,addids}
	 *	ids:所有状态改变的nodeid,cancelids:记录取消的nodeid,addids:记录增加的nodeid
	**/
	getCheckChangeNodeIds:function(treeId){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getChangeCheckedNodes();
		var ids = "";
		var cancelids = "";
		var addids = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			ids = nodes[i].id +","+ ids;
			if(nodes[i].checked==true)
				addids = nodes[i].id + "," + addids;
			else
				cancelids = nodes[i].id + "," + cancelids;
		}
		treeutil.clearCheckedOldNodes(treeId);
		return {ids:ids,cancelids:cancelids,addids:addids};
	},
	
	
	//清除勾选状态，将状态初始至原状态
	clearCheckedOldNodes:function (id) {
			var zTree = $.fn.zTree.getZTreeObj(id),
			nodes = zTree.getChangeCheckedNodes();
			for (var i=0, l=nodes.length; i<l; i++) {
				nodes[i].checkedOld = nodes[i].checked;
			}
	},
	
	//创建根节点
	addRootNode:function(treeId,config){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		nodes = zTree.getSelectedNodes();
		treeNode = nodes[0];
		var newTreeNode = {};
		
		if (treeNode) {
			//子节点
			alert("只能添加根节点，不能添加子节点!");
			if(config.callBackFunc)
				config.callBackFunc();
			return ;
		} else {
			//根节点
			newTreeNode = {id:config.data.id, pId:0, name:config.data.name};
			treeNode = zTree.addNodes(null, newTreeNode);
			
			HD.doCondition(config);
		}
	},
	
	//创建树节点,既可创建根节点,又可创建子节点
	addTreeNode:function(treeId,config){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		nodes = zTree.getSelectedNodes();
		treeNode = nodes[0];
		var newTreeNode = {};
		var id = config.data.id;
		var name = config.data.name;
		if (treeNode) {
			//子节点
			//alert(treeNode.id);
			config.data.pId=treeNode.id;
			newTreeNode = {id:id, pId:treeNode.id,  name:name };
			treeNode = zTree.addNodes(treeNode,newTreeNode );
			HD.doCondition(config);
		} else {
			//根节点
			newTreeNode = {id:id, pId:0, name:name};
			treeNode = zTree.addNodes(null, newTreeNode);
			config.data.pId=0;
			HD.doCondition(config);
		}
	},
	
	//创建子节点
	addZiTreeNode1:function(treeId,config){
		var treeObj = $.fn.zTree.getZTreeObj("treemenu");  
	    //返回一个根节点  
		var node = treeObj.getNodesByFilter(
		function (node) { return node.level == 0 }, true);
		var newTreeNode = {};
		var id = config.data.id;
		var name = config.data.name;
		if (treeNode) {
			//子节点
			//alert(treeNode.id);
			config.data.pId=treeNode.id;
			newTreeNode = {id:id, pId:node.id,  name:name };
			node = zTree.addNodes(node,newTreeNode );
			HD.doCondition(config);
		}
	},
	
	//创建子节点
	addZiTreeNode1:function(treeId,config){
		var treeObj = $.fn.zTree.getZTreeObj("treemenu");  
	    //返回一个根节点  
		var node = treeObj.getNodesByFilter(
		function (node) { return node.level == 0 }, true);
		var newTreeNode = {};
		var id = config.data.id;
		var name = config.data.name;
		if (treeNode) {
			//子节点
			//alert(treeNode.id);
			config.data.pId=treeNode.id;
			newTreeNode = {id:id, pId:node.id,  name:name };
			node = zTree.addNodes(node,newTreeNode );
			HD.doCondition(config);
		}
	},
	
	//创建子节点
	addZiTreeNode:function(treeId,config){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		nodes = zTree.getSelectedNodes();
		treeNode = nodes[0];
		var newTreeNode = {};
		var id = config.data.id;
		var name = config.data.name;
		if (treeNode) {
			//子节点
			//alert(treeNode.id);
			config.data.pId=treeNode.id;
			newTreeNode = {id:id, pId:treeNode.id,  name:name };
			treeNode = zTree.addNodes(treeNode,newTreeNode );
			HD.doCondition(config);
		}
	},
	
	
	//删除树节点
	removeTreeNode:function(treeId,config){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		nodes = zTree.getSelectedNodes();
		var tnodes = zTree.transformToArray(nodes);
		var ids = "";
		for(var i=0;i<tnodes.length;i++)
			ids = tnodes[i].id+','+ids;
		
		treeNode = nodes[0];
		if (nodes.length == 0) {
			alert("请先选择一个节点");
			return;
		}
		HD.doCondition(config);
	},
	//获取当前选择节点
	getSelecteNodes:function(treeId)
	{
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			return zTree.getSelectedNodes();
	},
	//缓存级别修改当前选中节点不做数据库的异步刷新
	updateSelectedNode:function(treeId,node)
	{
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length>0)
		{
				jQuery.extend(nodes[0], node);
				treeObj.updateNode(nodes[0]);	
		}
			
	}
};