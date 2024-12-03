<#macro upfile otherid moduleName >
     <table class="${moduleName!}" style="width:80%" border="0" cellspacing="0" cellpadding="0">
     <#assign files = ADJ.getListByOtherid(otherid)>
     <#list files as f>
		<tr style="line-height:25px">
			<td width="70%" style="line-height:22px"><a href="javascript:;" onclick="attachment.openFile('${f.ADJTRUENAME}')">${f.ADJSHOWNAME!}</a></td>
			<td align="center" style="line-height:22px"><a href="javascript:;" onclick="attachment.openFile('${f.ADJTRUENAME}')">查看</a>&nbsp;|&nbsp;
			<a href="javascript:;" onclick="attachment.downloadFile('${f.ADJTRUENAME}')">下载</a>&nbsp;|&nbsp;
			<a href="javascript:;" onclick="attachment.delFile(this,'${f.ADJTRUENAME}')">删除</a></td>
		</tr>
		<tr>
			<td style="height:5px" colspan="2"><hr style="border:1px dashed #ccc"></td>
		</tr>
	 </#list> 
	</table>
</#macro>

<#macro upfile1 otherid moduleName >
     <table class="${moduleName!}" style="width:80%" border="0" cellspacing="0" cellpadding="0">
     <#assign files = ADJ.getListByOtherid(otherid)>
     <#list files as f>
		<tr style="line-height:25px">
			<td width="70%" style="line-height:22px"><a href="javascript:;" onclick="window.open('${ctxPath}/sys/adjunct/openFile.html?ft=image&filename=${f.ADJTRUENAME!}">${f.ADJTRUENAME}</a></td>
			<td align="center" style="line-height:22px"><a href="javascript:;" onclick="window.open('${ctxPath}/sys/adjunct/openFile.html?ft=image&filename=${f.ADJTRUENAME!}')">查看</a>&nbsp;|&nbsp;
			<a href="javascript:;" onclick="attachment.downloadFile('${f.ADJTRUENAME}')">下载</a>&nbsp;|&nbsp;
			<a href="javascript:;" onclick="attachment.delFile(this,'${f.ADJTRUENAME}')">删除</a></td>
		</tr>
		<tr>
			<td style="height:5px" colspan="2"><hr style="border:1px dashed #ccc"></td>
		</tr>
	 </#list> 
	</table>
</#macro>

<#macro findfiles otherid>
     <table class="file_tables" style="width:80%" border="0" cellspacing="0" cellpadding="0">
     <#assign files = ADJ.getListByOtherid(otherid)>
     <#list files as f>
		<tr style="line-height:25px">
			<td width="70%" style="line-height:22px"><a href="javascript:;" onclick="attachment.openFile('${f.ADJTRUENAME}')">${f.ADJSHOWNAME!}</a></td>
			<td align="right" style="line-height:22px"><a href="javascript:;" onclick="attachment.openFile('${f.ADJTRUENAME}')">查看</a>&nbsp;&nbsp;|&nbsp;
			<a href="javascript:;" onclick="attachment.downloadFile('${f.ADJTRUENAME}')">下载</a> 
		</tr>
		<tr>
			<td style="height:5px" colspan="2"><hr style="border:1px dashed #ccc"></td>
		</tr>
	 </#list> 
	 <#if files?size == 0>
		 <tr> 	
			<td style="height:5px;padding-top:5px;" colspan="2" align="left">无附件</td>
		</tr>
	 </#if>
	</table>
</#macro>