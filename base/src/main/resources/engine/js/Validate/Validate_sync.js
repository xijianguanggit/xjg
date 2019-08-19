/**
 * validate校验分支
 */
var ${name}=function() {
	${logBegin}
	var url = '${ctx}${server}';
	var panelName = "${In}";
	var className = $('#'+panelName).attr("class");
	var id = getPanelId(panelName);
	var param = {};
	if(className==undefined){
		//如果是hiddenpanel等用于临时保存的，没有id
		param = $('#${In}').find('select, textarea, input, hidden').serializeJson();	
	}else if(className.indexOf("easyui-datagrid")>=0){
		//表格暂时没什么好传的TODO 可以改为传当前row对象
	}else{
		param = $('#${In}').find('select, textarea, input, hidden').serializeJson();	
	}
	var token = ${getToken};
	param['id'] = id;
	param['token'] = token;
	restoreToken("${functionId}",token);
	ajaxPost(url,param,function(res){//succeeded
		if(res.data==true){
			${ifyes}
		}else{
			${ifno}
		}
	},
	function(res){//error
    	parent.showError(res.responseText);
    	${ifno}
	});	
}


