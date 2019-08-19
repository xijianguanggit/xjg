var ${name}=function(){
	${logBegin}
	var url = '${ctx}${server}';
	//支持多表
	var arrPanelName = "${In}".split(",");
	var len = arrPanelName.length;
	//构造请求参数
	var mPanelName = arrPanelName[0];
	var param = $('#' + mPanelName).find('select, textarea, input, hidden').serializeJson();//构造参数
	param['Mode'] = getUIMode("${ui}");
	
	//含有Ueditor
	if($('#' + mPanelName).find("div").hasClass("input-group-inline js-UEditor_"+mPanelName)){
		var propertyName = $('#' + mPanelName).find("div.js-UEditor_"+mPanelName).attr("id");
		param[propertyName] = UE.getEditor(mPanelName+'_'+propertyName).getContent();
	}
	
	//加入明细Panel的参数
	for (var i=1;i<len ;i++ ) {
		var arrDetail = arrPanelName[i].split(".");
		var detailPanelName = arrDetail[0];
		var className = $('#'+detailPanelName).attr("class");//表格
		if(className==undefined || className.indexOf("easyui-datagrid")<0){
			console.log("无效的配置:一对多保存，子表"+detailPanelName + "不是datagrid组件");
			return;
		}else{
			var out=$("#"+detailPanelName);
			if(!eval('${acceptChanges}')){
				return;
			}
		}
		param[detailPanelName] = $("#" + detailPanelName).datagrid("getRows");//子表统一为datagrid
	}	
	var token = ${getToken};
	param['token'] = token;	
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
