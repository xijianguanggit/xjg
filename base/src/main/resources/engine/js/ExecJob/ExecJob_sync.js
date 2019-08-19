var ${name}=function(){
	${logBegin}
	var id = getPanelId('${In}');
	var param = {};
	param['id'] = id;
	param['token'] = ${getToken};
	ajaxPost('${ctx}${server}',param,function(res){//succeeded
		parent.showSlide("提示信息","已经开始立即执行");
		if(!${isAntiDup}){
			restoreToken("${functionId}",token);
		}
		${ifyes}
	},
	function(res){//error
    	parent.showError(res.responseText);
    	${ifno}
	});	
}