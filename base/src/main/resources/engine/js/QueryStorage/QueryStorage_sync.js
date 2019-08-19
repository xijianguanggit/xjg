var ${name}=function(){
	debugger;
	${logBegin}
	var url = '${ctx}${server}';
	
	var arrPanelName = "${In}";
	var outPanelName = "${Out}".split(",");
	var len = outPanelName.length;
	
	if ('${In}') {
        var param2 = $("#${In} :input,hidden").serializeJson();
    }
	var storageId = param2.processId; // 前端传过来的唯一key，利用这个key新开一个存储空间，区分Common.js 的命名空间查询
    
	for(var i=0;i<len;i++){
		var datagrid = $('#'+outPanelName[i]);
		if (localDB.select(getPath(datagrid)+"_"+storageId+"_columnOption")&&localDB.select(getPath(datagrid)+"_"+storageId+"_columnOption").total>0) {
			datagrid.datagrid("loadData",localDB.select(getPath(datagrid)+"_"+storageId+"_columnOption"));
			${ifyes}
		}else{
			${ifno}
		}
	}
	
}
