<#-- 总宽度减去东西减去边框乘以比例减去marginpx-->
<#if content?length lt 5>
<#list 1..g.width?number as index>
	<div class="input-group-inline" >
	    <label style="margin-left :3px;"></label> 
		<input type="radio" tipPosition="bottom" name="${panelObj.name}_${g.name}" 
		id="${panelObj.name}_${g.name}_${index}" /><span></span>
	</div>  
</#list>
</#if>
