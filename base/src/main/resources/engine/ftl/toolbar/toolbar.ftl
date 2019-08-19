<div class="btn-group" style="text-align:right;<#if panelObj.type=='Hidden'>display:none</#if>">
<div style="float: left;line-height: 29px;" class="js-path"></div>
	<#list panelObj.controlList as g>
		<#include "group/button.ftl">
	</#list>
</div>
