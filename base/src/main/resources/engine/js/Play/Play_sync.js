/**
 * 文件预览功能
 * （目前只支持图片显示，以后会逐渐扩展）
 */
var ${name} = function (name,value){
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var url = "${ctx}${server}?token=" + token;
	var filterValue = getPanelControlValue("${In}");
	var businessId = getPanelControlValue("${Business}");
	console.log("previewFile查询参数值 in-value -> ${In} = " + filterValue);
	if(filterValue){
		url += "&id=" + filterValue+"&business="+businessId;
	}
	window.open(url, "_blank","toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no")
}