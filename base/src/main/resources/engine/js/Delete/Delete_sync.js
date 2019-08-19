// 删除
var ${name}=function(){
	${logBegin}
	var token = ${getToken};
	var url = '${ctx}${server}';
	var currentId = getPanelId("${In}");//类似querybyId
	if(currentId){
		var param = {token:token,id:currentId};
		ajaxPost(url,param,function(res){
			if(res.data && res.data.msg){
				parent.showError(res.data.msg);
			}else if(res.msg){
				parent.showError(res.msg);
			}		
			if(!${isAntiDup} || res.code!=0){
				restoreToken("${functionId}",token);
			}
			${ifyes}
		},
		function(res){//error
	    	parent.showError(res.responseText);
	    	${ifno}
		})
	} else {
		${ifno}
	}
};
