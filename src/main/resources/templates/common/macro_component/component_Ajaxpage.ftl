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
    
    <div class="linepage">
       <a href="javascript:void(0);" id="up" onclick="AjaxClick2('${objID}','1','${callback}','${showCount}');" >&lt;&lt;第一页</a> 
       <a href="javascript:void(0);" onclick="AjaxClick2('${objID}','${prePageNo}','${callback}','${showCount}');" >&lt;上一页</a> 
       第${page.pageNo}/${page.totalpage} 
       <a href="javascript:void(0);" onclick="AjaxClick2('${objID}','${nextPageNo}','${callback}','${showCount}')" >下一页&gt;</a> 
       <a href="javascript:void(0)" onclick="AjaxClick2('${objID}','${page.totalpage}','${callback}','${showCount}');">最后一页&gt;&gt;</a> 
       跳转至<span class="td-al">
          
          <select name="select" id="selectpage"  onchange="AjaxClick2('${objID}',this.value,'${callback}','${showCount}')">
             <#list 1..page.totalpage as t>
                 <#if t = page.pageNo>
                     <option value="${t}" selected>${t}</option>
                 <#else>
                  <option value="${t}">${t}</option>
                 </#if>
             </#list>
          </select>
   </span>页 
	   <#if page.pageSizes?size gt 0>    
	     每页显示<span class="td-al">
	     <select name="pagesize" id="pagesize" onchange="AjaxClick2('${objID}','${page.pageNo}','${callback}',this.value)">
	       <#list page.pageSizes as v>
	           <#if v = showCount>
	       		<option value="${v}" selected>${v}</option>
	           <#else>
	               <option value="${v}">${v}</option>
	           </#if>
	       </#list>
	     </select>
	  	</span>条
	  	共${page.totalCount}条
	  	<#else>
		共0条	  	
	  	
	  </#if>   
  </div>
    
    <script>
		function AjaxClick2(objID,pageNo,cbalBack,showCount){
			$("#"+objID).attr("pageNo",pageNo);
			$("#"+objID).attr("pageNoNow",'1');
        	$("#"+objID).attr("pagesize",showCount);
        	eval(cbalBack);
		}
       
	</script>
    
    <script type="text/javascript">
        
    </script>
    
</#macro>