var ${name}=function(){
	console.log("带格式导出开始...");
	var url = '${ctx}${server}';
	//支持多表
	var arrPanelName = "${In}".split(",");
	var param = {};
	var len = arrPanelName.length;
	var token = ${getToken};
	param['token'] = token;
	//构造请求参数
	for (var i=0;i<len ;i++ ){
		var panelName = arrPanelName[i];
		var className = $('#'+panelName).attr("class");
		var a = className.indexOf("easyui-datagrid");
		if(className==undefined || className.indexOf("easyui-datagrid") >= 0){
			param[panelName] =  $("#" + panelName).datagrid("getData");
		}else{
			param[panelName] = $('#' + panelName).find('select, textarea, input, hidden').serializeJson();//构造参数
		}
	}
	restoreToken("${functionId}",token);
	//end
	ajaxPost(url,param,function(res){//succeeded
		var download = "<div align='center' ><h3>导出成功！<h3><br/>";
		var msg = res.id;
		if(res.data!=""){
			$.messager.alert("数据导出",download,downloadFile(res.data));
		}else{
			$.messager.alert("数据导出","生成文件失败，请重试");
		}
		${ifyes}
	},
	function(res){//error
    	parent.showError(res.responseText);
    	restoreToken("${functionId}",token);
    	${ifno}
	});
	//下载导出的数据文件
	function downloadFile(id){location.href = "${ctx}/localDownload?fileId="+id+"&methodType=export";}
}
