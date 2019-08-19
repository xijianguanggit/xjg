var ${name}=function() {
	var title;
	if("${Title}"){
		title='${Title}';
	} else {
		title='提示信息';
	}
	var msg = ${InterMsg};
	$.messager.alert(title,msg, 'info', function(){
		${ifyes}
	});
}
