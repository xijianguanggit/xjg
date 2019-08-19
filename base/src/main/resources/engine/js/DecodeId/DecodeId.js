// 取得当前dg选中行id
var ${name}=function(){
	var EditId=getStorage('EditId');
	var arr = "${Out}".split(".");
	var panelId = arr[0];
	var controlName = arr[1];
	var data = {controlName:EditId};
	var data = eval("({" + controlName + ":" + EditId + "})");
	$('#'+panelId).form('load',data);
	${ifyes}
 }

