<table width="100%">
<tr><td colspan="2">数据校验失败:</td></tr>
<#list errors as err>
	<tr><td style="vertical-align:top;width:120px">${err.field}</td><td>${err.defaultMessage}</td></tr>    
</#list>  		    
</table>