<#-- ajax 分页标签 -->
<#macro page2 page objID callback showCount="5">
    <#if page.pageNo gte 2 >
		<#assign prePageNo=page.pageNo-1 >
	<#else>
		<#assign prePageNo=1 >
	</#if>
    
    <#if page.totalpage gt page.pageNo>
		<#assign nextPageNo=  page.pageNo+1 >
	<#else>
		<#assign nextPageNo= page.pageNo >
	</#if>
	
	<a class="page-bg" class="page-bg" onclick="AjaxClick2('${objID}','1','${callback}','${showCount}');">首页</a>
	<a class="page-bg" onclick="AjaxClick2('${objID}','${prePageNo}','${callback}','${showCount}');">上一页</a>
	<span>当前<b> ${page.pageNo}</b>/ <b>${page.totalpage}</b> 页</span>
	<a class="page-bg" onclick="AjaxClick2('${objID}','${nextPageNo}','${callback}','${showCount}')">下一页</a>
	<a class="page-bg" onclick="AjaxClick2('${objID}','${page.totalpage}','${callback}','${showCount}');">尾页</a>
	<span>跳转至
		<select name="select" id="selectpage"  onchange="AjaxClick2('${objID}',this.value,'${callback}','${showCount}')">
             <#list 1..page.totalpage as t>
                 <#if t = page.pageNo>
                     <option value="${t}" selected>${t}</option>
                 <#else>
                  <option value="${t}">${t}</option>
                 </#if>
             </#list>
          </select>页
	</span>
	<span>每页显示 <select name="pagesize" id="pagesize" onchange="AjaxClick2('${objID}','1','${callback}',this.value);">
			       <option <#if page.pageSize == 10>selected="selected"</#if> value="10">10</option>
			       <option <#if page.pageSize == 20>selected="selected"</#if> value="20">20</option>
			       <option <#if page.pageSize == 50>selected="selected"</#if> value="50">50</option>
			       <option <#if page.pageSize == 100>selected="selected"</#if> value="100">100</option>
			     </select>条</span>
	<span>共 ${page.totalCount} 条</span>
	
	
	
    <script type="text/javascript">
    	function gotoPage(objID,pageNo,cbalBack,showCount){
    		var gopage = $("#gogeno").val();	
    		gopage = parseInt(gopage);
    		if(gopage>pageNo || gopage < 1){
    			return false;
    		}else{
    			AjaxClick2(objID,gopage,cbalBack,showCount);
    		}
    	}
    
	    function inputCheck(event){
	    		var e = event || window.event;
	    		var keycode = e.keyCode;
				if(!(keycode==46)&&!(keycode==8)&&!(keycode==37)&&!(keycode==39))
				if(!((keycode>=48&&keycode<=57)||(keycode>=96&&keycode<=105)))
				return false;
		}
		function AjaxClick2(objID,pageNo,cbalBack,showCount){
				$("#"+objID).attr("pageNo",pageNo);
				$("#"+objID).attr("pageNoNow",'1');
				$("#"+objID).attr("pagesize",showCount);
				eval(cbalBack);
		}
	</script>
</#macro>