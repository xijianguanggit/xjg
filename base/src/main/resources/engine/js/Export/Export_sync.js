/**
 * 数据导出
 */
var ${name}=function() {
	${logBegin}
    var param = $('#${In}').find('select, textarea, input, hidden').serializeJson();//构造参数
    param.ctx="${ctx}"
	param.title="${title}"	
    param.url='${ctx}${server}'
	var token=${getToken};
    param["token"] = token;
	$('#${Format}').datagrid("exportData",param);
	restoreToken("${functionId}",token);
}
