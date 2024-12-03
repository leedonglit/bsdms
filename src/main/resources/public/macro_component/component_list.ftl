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



<#macro list1 column objID callback>
<#assign jsoncolumn=column?eval />
<table width="99%" border="0" class="com_information_content" cellspacing="0" cellpadding="0" style="border-collapse:collapse;">
 	<tr class="com_information_title1">
	 	<#list jsoncolumn as jc>
	 		<#assign col = jc.filed!>
		 	<#assign coltitle = jc.title!>
	 		<#if jc.index! == 'true'>
	 				<td <#if jc.width! != ''>width='${jc.width!}'</#if>>${coltitle!}</td>
	 		<#else>
		 		<#if jc.checkbox! == 'true'>
		 				<td <#if jc.width! != ''>width='${jc.width!}'</#if>><input type="checkbox" onclick="check.checkAll(this,'checkbox_list')"/></td>
		 		<#else>
		 			<td <#if jc.width! != ''>width='${jc.width!}'</#if>>${coltitle!}</td>
		 		</#if>
	 		</#if>
	 	</#list>
	 	</tr>
	 	<#if gpage.result?size gt 0> 
              <#list gpage.result as p>
              <tr class="com_information_content3">
              	<#list jsoncolumn as jc>
	              	<#if jc.index! == 'true'>
		 				<td>${p_index+1}</td>
		 			<#else>
	              		<#if jc.checkbox! == 'true'>
	                			<td><input type="checkbox" value="${p[jc.filed]!}" name="checkbox_list"/></td>
	                	<#else>
	                		<td <#if jc.align! != ''>align='${jc.align!}'</#if>>
                				<#if jc.opt! != ''>
                					<#assign jsonopt=jc.opt?eval />
                					<#assign param=p[jc.filed]! />
                					<#list jsonopt as jo>
                						<#if jo_index< jsonopt?size-1>
                							<a href='javascript:;' onclick="${jo.method!}('${param!}')">${jo.text!}</a>&nbsp;&nbsp;|&nbsp;&nbsp;
                						<#else>
                							<a href='javascript:;' onclick="${jo.method!}('${param!}')">${jo.text!}</a>
                						</#if>
                					</#list> 
                				<#else>
                					
							<#if jc.title="备注">
<img src="${ctxPath}/base/images/remark.png" width="23" height="23" onmouseover="ui.tips(this,'${p[jc.filed]!}',1)"/>
							<#else>
${p[jc.filed]!}
							</#if>
                				</#if>
	                		</td>
	                	</#if>	
                	</#if>
                </#list> 
              </tr>
        </#list> <#else>
				<tr class="com_information_content1">
					<td colspan="${jsoncolumn?size+1}">
						无数据
					</td>
				</tr>
		</#if>
  	</tr>
  	<tr class="com_content_page">
                <td colspan="${jsoncolumn?size+1}">
                   <@page1 page=gpage objID=objID callback=callback showCount=gpage.pageSize/>
                </td>
              </tr>
</table>
</#macro>