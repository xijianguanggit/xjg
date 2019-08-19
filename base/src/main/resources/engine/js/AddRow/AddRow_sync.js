var ${name} = function (){
	${logBegin}
	var dg=$('#${Out}')
	var panelId=dg.attr("id");
	if (eval('endEditing_'+panelId+'(dg)')){
		var row = dg.datagrid('getSelected');
		var index = dg.datagrid('getData').total
		$(".datagrid-empty").remove();
		var obj=new Object();
		obj.index=index;
		if("${In}"&&"${Columns}"){
			var Columns="${Columns}".split('|');
			for(var i=0;i<Columns.length;i++){
				var str=Columns[i].split(',');
				obj[str[1]]=getPanelControlValue("${In}."+str[0])
			}
		}
		dg.datagrid('appendRow', obj);
		obj.Mode='Add';
//		dg.datagrid('getRows')[index].Mode='Add';//bugbr,超过一页时
		eval("editIndex_"+panelId+" = dg.datagrid('getRows').length-1");
		dg.datagrid('selectRow',eval("editIndex_"+panelId));
		dg.datagrid('beginEdit',eval("editIndex_"+panelId));
	}
	
}