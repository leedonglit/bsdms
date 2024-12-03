//ztree初始化
function NoeTreeControl(treeDefine){
	this.zTree = null;
	this.NoeZTreeCP = new NoeZTreeControl_OP();
	this.init(treeDefine);
}