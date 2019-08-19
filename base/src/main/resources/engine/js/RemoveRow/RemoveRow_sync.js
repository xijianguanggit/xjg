var ${name} = function (){
	var dg=$('#${In}');
	var panelId=dg.attr("id");
	if (eval('editIndex_'+panelId) == undefined){return}
		$.messager.confirm('删除','确定删除该行么?',function(r){
			if (r){
				// 设置选中行的下标
				
				var index=dg.datagrid('getRowIndex',dg.datagrid('getSelected'))
				setStorage('EditIndex_'+panelId, index);
				var row = dg.datagrid('getSelected');
				row['delete']="1"
					dg.datagrid('refreshRow',index); 
				
				eval("editIndex_"+panelId+" = undefined");
				${ifyes}
			}
		});
}