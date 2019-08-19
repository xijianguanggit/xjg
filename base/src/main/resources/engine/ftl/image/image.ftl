<#list panelObj.controlList as g>
	<#if "${g['type']}" == "ImageView">
		<#include "image/imageView.ftl">
</#if>
</#list>