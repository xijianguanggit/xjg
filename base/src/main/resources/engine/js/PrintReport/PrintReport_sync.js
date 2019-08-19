var ${name}=function(){
		debugger;
		${logBegin}
		console.log("PrintReport打印报表开始...");
		var url = '${ctx}${server}';
		 var out = '${Out}';
		var templet = '${Templet}'
		//支持多表
		var arrPanelName = "${In}".split(",");
		var param = {};
		var len = arrPanelName.length;
		var param2 = null;
		/*if ('${In}') {
	        var param2 = $("#${In} :input,hidden").serializeJson();
	    }*/
		    
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
		    if (param2) {
		        var param = $.extend(param,param2);
		    }
		    if("${setParam}"){
		    	param=eval("${setParam}(out,param);");
		    }
		    if("${getOption}"){
		    	eval("${getOption}(out);");
		    }
		   
	
	    var token = ${getToken};
		restoreToken("${functionId}",token);
	    var param = {token: token,templet:templet};

	    var ifyes='${ifyes}'.replace("();","");
	    var ifno='${ifno}'.replace("();","");
	
		ajaxPost(url,param,function(res){//succeeded
			var msg = res.id;
			if(res.data!=""){
				window.open("${ctx}/localDownload?fileId="+res.data+"&preview=pre");
			}else{
				$.messager.alert("数据导出","生成文件失败，请重试");
			}
			${ifyes}
		},
		function(res){//error
	    	parent.showError(res.responseText);
	    	restoreToken("${functionId}",token);
	    	
	    	/*$.messager.alert('提示', '未配置打印模板', 'warning');
			return false;*/
			
	    	${ifno}
	    	
		});
		restoreToken("${functionId}",token);
}
