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
		var selectorId = getControlId("${Out}");
		var selector = $(selectorId);
		var className = selector.attr("class")
		if(className && className.indexOf("combobox")>0){
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
		}else if($(panelId).attr("class") && $(panelId).attr("class").indexOf("datagrid")>0){
			debugger;
			var controlName = arr[0] + "_" + arr[1];

			console.log("datagrid:radio类型");
			var htm = "";
			for(var i=0;i<res.data.length;i++){
				htm += "<input type='radio' name='" + controlName + "' id=" + controlName + "_" + i + " value='" + res.data[i].value + "'>";
				htm += "<label style='margin-left :3px;' id='" + controlName + "_" + i + "_lbl" + "' style='margin-left :3px;'>" + res.data[i].text + "</label>"; 
			}
			$(htm).appendTo(selector);
			eval("arr" + controlName + " =  res.data;");
		}else{
			console.log("radio类型");
			debugger;
			for(var i=0;i<res.data.length;i++){
				if(res.data[i].value==val){
					$(selectorId+"_"+(i+1)).attr("checked","true");
				}
				$(selectorId+"_"+(i+1)).prev("label").text(res.data[i].text);
				$(selectorId+"_"+(i+1)).val(res.data[i].value);
			}
		}
		${ifyes}
	})	
}
