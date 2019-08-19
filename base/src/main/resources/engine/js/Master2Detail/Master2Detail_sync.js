var ${name}=function(){
	${logBegin}
	var panelControl = "${In}";
	var arrPanelControl = panelControl.split(".");
	
	var panelName = arrPanelControl[0];
	var columnPropertyName = arrPanelControl[1];
	
	var ids = getPanelControlValue("${In}");//
	var dg = $("#${Out}");//
	if(ids){
		dg.datagrid('loadData',{total:0,rows:[]});
		var arrData = ids.split(",");
		for(var i=0;i<arrData.length;i++){
			var rowData = param = $('#'+panelName).find('select, textarea, input, hidden').serializeJson();	
			rowData["Mode"] = "Add";
			rowData[columnPropertyName] = arrData[i];
			dg.datagrid('insertRow', {
				row:rowData
			});
		}
		${ifyes}
	} else {
		${ifno}
	}
 }
 
