var ${name}=function(){
	console.log("transform逻辑开始..");
	var url = '${ctx}${server}';
	var param = {};
	if("${In}"){
		var arrPanelName = "${In}".split(",");
		var len = arrPanelName.length;
		var mPanelName = arrPanelName[0];
		var param = $('#' + mPanelName).find('select, textarea, input, hidden').serializeJson();//构造参数

//		var inClassName = $('#${In}').attr("class");		
//		if(inClassName==undefined || inClassName.indexOf("js-group") > -1){
//			param = $('#${In}').find('select, textarea, input, hidden').serializeJson();
//		}
		//循环构造panelName:serialzation
		for (var i=1;i<len ;i++ ) {
			var arrDetail = arrPanelName[i].split(".");
			var detailPanelName = arrDetail[0];
			var className = $('#'+detailPanelName).attr("class");//表格
			if(className==undefined || className.indexOf("easyui-datagrid")<0){
				param[detailPanelName] = $('#' + detailPanelName).find('select, textarea, input, hidden').serializeJson();//构造参数
			}else{
//				var out=$("#"+detailPanelName);
//				if(!eval('${acceptChanges}')){
//					return;
//				}
				param[detailPanelName] = $("#" + detailPanelName).datagrid("getRows");//子表统一为datagrid
			}
		}			
	}
	var token = ${getToken};
	param['token'] = token;
	restoreToken("${functionId}",token);
	ajaxPost(url,param,function(res){//succeeded
		if("${Out}"){
			if(res.data){
			    eval("${loadTransform}('${Out}', res)");//已注册到componentList中支持多种组件
			}
		}
		if(res.msg){
			if(res.data && res.data.msg){
				parent.showError(res.data.msg);
			}else if(res.msg){
				parent.showError(res.msg);
			}
		}
		${ifyes}
	},
	function(res){//error
    	parent.showError(res.responseText);
    	${ifno}
	});	
}
