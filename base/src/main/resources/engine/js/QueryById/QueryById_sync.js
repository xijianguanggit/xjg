var ${name}=function(){
	${logBegin}
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var id = getPanelId("${In}");
	if(id){
		var condition = $("#${In} :input,hidden").serializeJson();
		condition["token"] = token;
		condition["eq_id"] = id;
		var url = '${ctx}${server}';
		ajaxPost(url,condition,function(res){
			$('#${Out}').form('load',res.data);
		    for(var i in res.data){
		    	if($("#${Out}_"+i).attr('type')!='hidden'&&$("#${Out}_"+i).parent().find(".textbox-text").attr("readonly")=="readonly"){
		    		$("#${Out}_"+i).parent().find(".textbox-text").attr("title",res.data[i])
			    }else if($("#${Out}_"+i).hasClass("js-label")){
			    	$("#${Out}_"+i).html(res.data[i]);
			    }else if($("#${Out}_"+i).hasClass("edui-default")){//为Ueditor
			    	UE.getEditor("${Out}_"+i).setContent(res.data[i]);
			    	if($("#${Out}_"+i).parent().attr("readonly")=="readonly"){//只读
			    		UE.getEditor("${Out}_"+i).setDisabled('fullscreen');
			    	}
			    }
		    } 
			${ifyes}
		});
	}else{
		console.log("querybyid 没有获取到${In}的id值");
		${ifyes}
	}	
 };

