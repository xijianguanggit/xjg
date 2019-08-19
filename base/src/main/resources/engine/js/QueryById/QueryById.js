// 取得当前dg选中行id
var ${name}=function(){
	var editMode = getUIMode("${ui}");
	var data = "queryParam=${Sql}&Mode=" + editMode + "&token=" + ${getToken};
	if(editMode!='Add'){//AddMode时返回初始化数据
		decodeid = getStorage('EditId');
		data += "&eq_id=" + decodeid;
	}
	$.ajax({
        type: 'POST',
        url: '${ctx}${server}',
        data: data,
        success: function (response)
        {
			$('#${Out}').form('load',response.data);
			updateClientContext(response.clientContext);
			${ifyes}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown)
        {
        	parent.showError(XMLHttpRequest.responseText);
        	${ifno}
        }	
	})
 };

