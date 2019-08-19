var ${name}=function() {
	var title;
	if('${Title}'){
		title = '${Title}';
	} else {
		title = '确认';
	}
	if('${Mode}'=='YesNo'){
		$.messager.defaults = { ok: "Yes", cancel: "No" };
	}
	if('${Mode}'=='OkCanncel'){
		$.messager.defaults = { ok: "Ok", cancel: "Canncel" };
	}
//	$.messager.defaults = { ok: "是", cancel: "否", width:300 };
	$.messager.confirm(title, '${Msg}', function(r){
		if (r){
			${ifno}
		} else {
			${ifno}
		}
	});
}


