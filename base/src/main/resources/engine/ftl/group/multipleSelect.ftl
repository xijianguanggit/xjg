<#-- 总宽度减去东西减去边框乘以比例减去marginpx-->
	<div class="input-group-inline" >
	    <label style="margin-left :3px;
	   		 <#if g['required']&&EditMode != 'Readonly'&&g['edit'] >
		    color:red;font-weight:bold
		    </#if>"
	    >${g.title}  </label>
    	<select class="easyui-combobox js-input"  width-data="${g.widthPencent}"/>
		    	<#if EditMode == 'Readonly' || !g['edit']>
		    	readonly="true"
		    	</#if> 
    	id="${panelObj.name}_${g.name}" name="${g.name}" data-options="editable:false, multiple:true, panelHeight:'auto'">
		</select>
    	<script>
		</script>   
	</div>
	
