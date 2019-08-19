var ${name}=function(){
	var className = $("#${In}").attr("class");
	
	var id = getStorage('EditId');
	var data = "";
	if(className.indexOf("easyui-datagrid")>=0){
		//表格暂时没什么好传的
	}else{
		data = $('#${In}').find('select, textarea, input, hidden').serialize();	
	}
	data += "&primary=" + id + "&Sql=${Sql}&token=" + ${getToken};	
	$.ajax({
        type: 'POST',
        url: '${ctx}${server}',
        data: data,
        success: function (result)
        {
        	parent.showSlide("提示信息",result.msg);
			${ifyes}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown)
        {
        	parent.showError(XMLHttpRequest.responseText);
        }	
	});
}
