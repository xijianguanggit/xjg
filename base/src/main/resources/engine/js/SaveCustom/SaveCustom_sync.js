var ${name}=function(){
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
		param = $('#${In}').datagrid('getData');
	}else{
		param = $('#${In}').find('select, textarea, input, hidden').serializeJson();	
	}
	
	//含有Ueditor
	if($('#' + panelName).find("div").hasClass("input-group-inline js-UEditor_"+panelName)){
		var propertyName = $('#' + panelName).find("div.js-UEditor_"+panelName).attr("id");
		param[propertyName] = UE.getEditor(panelName+'_'+propertyName).getContent();
	}
	
	var token = ${getToken};
	param['id'] = id;
	param['token'] = token;
	param['Mode'] = getUIMode("${ui}");
	ajaxPost(url,param,function(res){//succeeded
		if(res.data && res.data.msg){
			parent.showError(res.data.msg);
		}else if(res.msg){
			parent.showError(res.msg);
		}
		if(res.code==0){//业务异常停住
			if("${Out}"){
				var value = res.data;
				var arr = "${Out}".split(".");
				var panelId = arr[0];
				var controlName = arr[1];				
				var data = eval("({" + controlName + ":'" + res.data.id + "'})");//主键值
				$('#'+panelId).form('load',data);
			}			
			if(!${isAntiDup}){
				restoreToken("${functionId}",token);
			}
			${ifyes}
		}else{
			restoreToken("${functionId}",token);//后台校验/业务异常时会恢复消费掉的token
		}
	},
	function(res){//error
    	parent.showError(res.responseText);
    	${ifno}
	});	
}
