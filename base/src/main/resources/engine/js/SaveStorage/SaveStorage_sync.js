var ${name}=function(){
	debugger;
	${logBegin}
	var url = '${ctx}${server}';
	//支持多Pan
	var arrPanelName = "${In}".split(",");
	var len = arrPanelName.length;
	
	//storage 为 in参数中最后一个pan
	for(var i=0;i<len-1;i++){
			var dg = arrPanelName[i];
			var displayData = $('#'+dg).datagrid("getData");
			var result = acceptChangesGrid($('#'+dg));
			
			var key = arrPanelName[len-1];
			if (key) {
		        var storageParam = $("#"+key+" :input,hidden").serializeJson();
		        var storage = storageParam.processId; //key 
		    }
			// 放入本地变量
		    if (!localDB.select(getPath($('#'+dg))+"_"+storage+"_columnOption")||localDB.select(getPath($('#'+dg))+"_"+storage+"_columnOption").length==0) {
		    	localDB.createSpace(getPath($('#'+dg))+"_"+storage+"_columnOption");
		        localDB.insert(getPath($('#'+dg))+"_"+storage+"_columnOption", displayData);
		    } else {
		    	console.log("update");
		    	localDB.update(getPath($('#'+dg))+"_"+storage+"_columnOption", localDB.select(getPath($('#'+dg))+"_"+storage+"_columnOption"), displayData);
		    }
	}
    //返回msg
    parent.showError("已暂存成功");
    ${ifyes}
}
