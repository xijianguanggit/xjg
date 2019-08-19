/**
 * 为下拉组件设置数据源
 * list忽略token
 */
var ${name}=function(){
	${logBegin}
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var param1 = {'token':token};
	var param2 = {};
	if("${In}"){
		param2 = $("#${In} :input,hidden").serializeJson();
	}
	var queryParams = $.extend(param1, param2);
	var url = '${ctx}${server}';
	ajaxPost(url,queryParams,function(res){
		var arr = '${Out}'.split(".");
		var panelId = "#"+arr[0];
		var val=$(getControlId("${Out}")).combobox('getValue');
		var flg=false;
		for(var i=0;i<res.data.length;i++){
			if(res.data[i].value==val){
				flg=true;
				break;
			}
		} 
		if(!flg){
			$(getControlId("${Out}")).combobox('clear');
		}
		$(getControlId("${Out}")).combobox('loadData',res.data);
	})	
	${ifyes}
}
