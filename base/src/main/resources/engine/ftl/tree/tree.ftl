
<#if '${panelObj.search}'=="true">
	<input class="easyui-searchbox" data-options="searcher:searchDept,prompt:'搜索'" id="${panelObj.name}_SearchBox" treeId="${panelObj.name}"  style="width: 280px; height: 32px;"></input>
</#if>
<ul id="${panelObj.name}" class="easyui-tree" dataCheckBox="${panelObj.multiple}"  trigger="OnClick_${panelObj.flow.trigger}_${ui.name}()"></ul>