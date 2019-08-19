var ${name} = function (){
	${logBegin}
	var dg=$("#"+"${In}".split(",")[0]);
	if(dg.attr('class').indexOf("easyui-datagrid")>-1){
		if($(getControlId("${In}".split(",")[1])).attr('checked')=="checked"){
			dg.datagrid('checkAll');
		} else {
			dg.datagrid('uncheckAll');
		}
		var row=dg.datagrid('getChecked');
		var ids='';
		for(var i=0;i<row.length;i++){
            ids = ids + "'" + row[i].id + "'" + ","
        }
		var obj= new Object();
		obj['${Out}'.split('.')[1]]=ids.substring(0,ids.length-1);
		$('#'+'${Out}'.split('.')[0]).form('load', obj);
		${ifyes}
	} else {
		console.warn("In参数格式可能不是datagrid");
	}
	
}