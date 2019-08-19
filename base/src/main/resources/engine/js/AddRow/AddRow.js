var ${name} = function (){
	var dg=$('#${in}')
	var row = dg.datagrid('getSelected');
	if (row){
		var index = dg.datagrid('getRowIndex', row);
	} else {
		index = 0;
	}
	dg.datagrid('insertRow', {
		index: index,
		row:{
			status:'P'
		}
	});
	dg.datagrid('selectRow',index);
	dg.datagrid('beginEdit',index);
}