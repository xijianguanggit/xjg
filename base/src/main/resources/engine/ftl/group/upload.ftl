<#-- 总宽度减去东西减去边框乘以比例减去marginpx-->
	<div class="input-group-inline js-image${g.name}" >
	    <link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/jquery.Jcrop.css?${date}" rel="stylesheet" media="screen"/>
	    <#assign flow = g.flow>
	    <#assign procedureList = flow.procedureList>
		 <#list procedureList as p>
		     <#if p_index == 0>
		    <#assign logic = p.logic>
		     </#if>
	    </#list> 
		<#if "${logic}" =="UploadPicture">
			<div id='TCrop' onselect="${panelObj.name}_${g.name}_OnSelect()"></div>
    <script type="text/javascript">
		$(function() {
		eval("OnClick_${panelObj.name}_${g.name}_${ui.name}()");
        });
    </script>   
		<#else>
	    <label style="margin-left :3px;
	   		 <#if g['required']&&EditMode != 'Readonly'&&g['edit'] >
		    color:red;font-weight:bold
		    </#if>"
	    >${g.title} </label>
	    	<input class="easyui-filebox js-input"  width-data="${g.widthPencent}"
    	<#if g['required']&&EditMode != 'Readonly'&&g['edit'] >
	    	 required="true"
    	</#if>
	    	id="fileUploadId_${fileIdx}" name="${g.name}" data-options="buttonText:'...',prompt:'${g.tip}',
	    	    <#include 'group/uploadFunction.ftl'>"  />
		</#if>
	</div>
	
