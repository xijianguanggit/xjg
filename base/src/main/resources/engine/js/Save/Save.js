var ${name}=function(){
	var data = $('#${In}').find('select, textarea, input, hidden').serialize();
	var editMode = getUIMode("${ui}");
	var decodeid = getStorage('EditId');
	var  token = ${getToken};
	data += "&Mode=" + editMode + "&primary="+decodeid + "&token=" + token ;
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
        	console.log("error.result:"+XMLHttpRequest.responseText);
        	parent.showError(XMLHttpRequest.responseText);
        }	
	});
}
