var ${name} = function (){
	${logBegin}
	var url = '${ctx}/api/save';
	//支持多表
	var panelName = "${In}";
	var param = $('#' + panelName).find('select, textarea, input, hidden').serializeJson(true);//构造参数
	var token = ${getToken};
	param['token'] = token;	
	param['Mode'] = "Edit";
	//end
	ajaxPost(url,param,function(res){//succeeded
		if(res.data && res.data.msg){
			parent.showError(res.data.msg);
		}else if(res.msg){
			parent.showError(res.msg);
		}
		if(res.code==0){//业务异常停住,不出错时
			if("${Out}"){
				var value = res.data;
				var arr = "${Out}".split(".");
				var panelId = arr[0];
				var controlName = arr[1];				
				var data = eval("({" + controlName + ":'" + res.data.id + "'})");//主键值
				$('#'+panelId).form('load',data);
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