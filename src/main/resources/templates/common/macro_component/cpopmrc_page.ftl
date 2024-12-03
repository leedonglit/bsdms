<#-- ajax 分页标签 -->

<#macro page1 page objID callback showCount="5">
    
    <#if page.pageNo gte 2 >
		<#assign prePageNo=page.pageNo-1 >
	<#else>
		<#assign prePageNo=1 >
	</#if>
    
    <#if page.hasNext >
		<#assign nextPageNo=page.nextPage >
	<#else>
		<#assign nextPageNo=page.totalpage >
	</#if>
     <ul class="net_page">
	        <li style="padding-right:43px;">总共${page.totalCount}条记录</li>
	         <li>
	      	   每页显示
			     <select name="pagesize" id="pagesize" onchange="AjaxClick2('${objID}','1','${callback}',this.value);">
			       <option <#if page.pageSize == 10>selected="selected"</#if> value="10">10</option>
			       <option <#if page.pageSize == 20>selected="selected"</#if> value="20">20</option>
			       <option <#if page.pageSize == 50>selected="selected"</#if> value="50">50</option>
			       <option <#if page.pageSize == 100>selected="selected"</#if> value="100">100</option>
			     </select>
			  条 
			 </li>
			<li><a href="javascript:void(0)" onclick="AjaxClick2('${objID}','1','${callback}','${showCount}');">首页</a></li>
	        <li>|</li>
			<li><a  href="javascript:void(0);" onclick="AjaxClick2('${objID}','${prePageNo}','${callback}','${showCount}');" >上一页</a></li>
	        <li>|</li>
	        <li><a href="javascript:void(0);" onclick="AjaxClick2('${objID}','${nextPageNo}','${callback}','${showCount}')" >下一页</a></li>
	        <li>|</li>
	        <li><a href="javascript:void(0)" onclick="AjaxClick2('${objID}','${page.totalpage}','${callback}','${showCount}');">尾页</a></li>
	        <li style="padding-left:20px; padding-right:20px;">第 ${page.pageNo} 页/共 ${page.totalpage}  页</li>
	        <li>  跳转到第<input type="text" class="com_page" value="${page.pageNo}" id="gogeno" onkeydown="return inputCheck(event)"/>页</li>
	        <li><a class="go" href="javascript:void(0)" onclick="gotoPage('${objID}','${page.totalpage}','${callback}','${showCount}');">GO</a></li>
      </ul>
    
    
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