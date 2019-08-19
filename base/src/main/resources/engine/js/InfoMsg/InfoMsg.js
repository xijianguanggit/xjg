var ${name}=function() {
	var title;
	if("${Title}"){
		title='${Title}';
	} else {
		title='提示信息';
	}
	$.messager.alert(title,'{Msg}', 'info', function(){
		${ifyes}
	});
}
