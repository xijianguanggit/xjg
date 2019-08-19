	<#if g.allowed>
		<#assign flow = g.flow>
		<#if '${panelObj.type}'=='Group'>&nbsp;</#if>
		
		<#if '${g.title}'?length gt 0>
			<#assign viewWidth = '${g.title}'?length * 12 + 16 + 'px'>
		<#else>
			<#assign viewWidth = '16px'>
		</#if>
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		<#if g.flow??>
		${g.flow.event}="${g.jsEvent}();antiClick('${panelObj.name}_${g.name}');" 
		</#if>
		id="${panelObj.name}_${g.name}" name="${g.name}"
		style="margin-right:10px;

		width:${viewWidth};
		
		<#if '${panelObj.type}'=='Group'>
			    margin-top: -8px;
		</#if>"
		>${g.title}</a>
	</#if>