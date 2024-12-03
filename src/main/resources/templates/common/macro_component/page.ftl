<#-- ajax 分页标签 -->
<#macro page1 page objID callback showCount="5">
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

	<ul class="page_content">
		<li style="margin-right: 10px;"><span style="color: #fff;line-height: 30px;font-size: 16px;">Total ${page.totalCount} </span></li>
		<li style="margin-right: 10px;"><a style="width: 64px;" onclick="AjaxClick2('${objID}',1,'${callback}','${showCount}');">One</a></li>
		<li style="margin-right: 10px;"><a style="width: 64px;" onclick="AjaxClick2('${objID}','${prePageNo}','${callback}','${showCount}');">Prev</a></li>
		<#if page.totalpage gt 0 >
			<#list 1..page.totalpage as t>
				<#if t = page.pageNo>
					<li><a class="show_02">${t}</a></li>
				<#else>
					<li><a onclick="AjaxClick2('${objID}','${t}','${callback}','${showCount}')">${t}</a></li>
				</#if>
			</#list>
		</#if>
		<li style="margin-left: 10px;"><a style="width: 64px;" onclick="AjaxClick2('${objID}','${nextPageNo}','${callback}','${showCount}')">Next</a></li>
		<li style="margin-left: 10px;"><a style="width: 64px;" onclick="AjaxClick2('${objID}','${page.totalpage}','${callback}','${showCount}');">Last</a></li>
	</ul>

     <script>
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