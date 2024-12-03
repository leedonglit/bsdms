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
<div class="layui-box layui-laypage layui-laypage-default" id="layui-laypage-10">
<span class="layui-laypage-count">共 ${page.totalCount} 条 第${page.pageNo}/${page.totalpage}页</span>
<a href="javascript:;" class="layui-laypage-prev" onclick="AjaxClick2('${objID}','${prePageNo}','${callback}','${showCount}');">上一页</a>


<#if page.totalpage gt 1>
   <#if page.totalpage lt 6>
     <#list 1..page.totalpage as inpage>
	   <#if (inpage==page.pageNo)>
		    <span class="layui-laypage-curr">
			     <em class="layui-laypage-em"></em>
				<em>${inpage}</em>
			</span>
	   <#else>
	   <a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
	   
	   </#if>
	   </#list>
   <#else>
         
         <#list 1..page.totalpage as inpage>
           <#if page.pageNo lt 4>
	               <#if inpage gt 5>
	                    <#if (inpage==page.totalpage)>
	                        <a href="javascript:;" data-page="${inpage}">.....</a>
	                    	<a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
	                    </#if>
	               <#else>
	                      <#if (inpage==page.pageNo)>
							    <span class="layui-laypage-curr">
								     <em class="layui-laypage-em"></em>
									<em>${inpage}</em>
								</span>
						   <#else>
						        <a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
						   </#if>
	               </#if>
             <#elseif (page.pageNo gt page.totalpage-4)>
                       <#if (inpage==1)>
                       			<a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
                       	        <a href="javascript:;" data-page="${inpage}">.....</a>
                       <#elseif (inpage gt page.totalpage-5)>
	                              <#if (inpage==page.pageNo)>
									    <span class="layui-laypage-curr">
										     <em class="layui-laypage-em"></em>
											<em>${inpage}</em>
										</span>
								   <#else>
								        <a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
								   </#if>
	                   </#if>  
             <#else>
                      <#if (inpage==1)>
                       			<a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
                       	        <a href="javascript:;" data-page="${inpage}">.....</a>
                       <#elseif (inpage==page.totalpage)>
	                            <a href="javascript:;" data-page="${inpage}">.....</a>
	                        	<a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
	                   
                       <#elseif (inpage gt page.pageNo-3)>
                           <#if (inpage lt page.pageNo+3)>
	                              <#if (inpage==page.pageNo)>
									    <span class="layui-laypage-curr">
										     <em class="layui-laypage-em"></em>
											<em>${inpage}</em>
										</span>
								   <#else>
								        <a href="javascript:;" data-page="${inpage}"   onclick="AjaxClick2('${objID}','${inpage}','${callback}','${showCount}')">${inpage}</a>
								   </#if>
                           </#if>
	                   </#if>  
             </#if>  
		 </#list>
   </#if>
  
</#if>


<a href="javascript:;" class="layui-laypage-last" onclick="AjaxClick2('${objID}','${nextPageNo}','${callback}','${showCount}')">下一页</a>
<a href="javascript:;" class="layui-laypage-next"  onclick="AjaxClick2('${objID}','${page.totalpage}','${callback}','${showCount}');">尾页</a>
<span class="layui-laypage-limits">
<select name="pagesize" id="pagesize"  onchange="AjaxClick2('${objID}','${page.pageNo}','${callback}',this.value)">
 <#list page.pageSizes as v>
	           <#if v = showCount>
	       		<option value="${v}" selected>${v}</option>
	           <#else>
	               <option value="${v}">${v}</option>
	           </#if>
	       </#list>
</select>
</span>
<span class="layui-laypage-skip">
到第<input type="text" min="1" class="layui-input" value="${page.pageNo}" id="gogeno">页
<button type="button" class="layui-laypage-btn" onclick="gotoPage('${objID}','${page.totalpage}','${callback}','${showCount}');">确定</button>
</span>
</div>
 <script type="text/javascript">
    	function gotoPage(objID,pageNo,cbalBack,showCount){
    		var gopage = $("#gogeno").val();
    		if(isNaN(gopage)){
    			 ui.alert("请输入合法的数字页码！");
    			return false;
    		}
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