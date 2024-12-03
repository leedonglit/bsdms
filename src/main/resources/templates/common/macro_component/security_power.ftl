<#macro load btncode class datatype onclick>
	<#assign btn = POWER.checkBtnPower('${btncode}')?eval>
	<#if btn.nopower>
		<!--表示没有权限-->
	<#else>
		<button class="${class!}" data-type="${datatype!}" onclick="${onclick!}">${btn.btnName!}</button>
	</#if>
</#macro>
