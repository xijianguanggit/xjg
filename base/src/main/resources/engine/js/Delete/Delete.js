// 删除
var ${name}=function(){
	var data = "primary=" + getStorage('EditId') + "&token=" + ${getToken};
	$.messager.confirm('确认','确认删除当前记录吗?',function(r){
		$.ajax({
	        type: 'POST',
	        url: '${ctx}${server}',
	        data: data,
	        success: function (data)
	        {
	        	$.messager.show({
	        		title:'提示信息',
	        		msg:data.msg,
	        		timeout:3000,
	        		showType:'slide'
	        	});
	        	${ifyes}
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown)
	        {
	        	showError(XMLHttpRequest.responseText);
	        }	
		})
	});
 };
