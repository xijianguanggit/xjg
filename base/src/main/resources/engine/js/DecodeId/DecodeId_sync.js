// 取得当前dg选中行id
var ${name}=function(){
	${logBegin}
	var EditId=getStorage('EditId');
	setStorage('EditId',undefined);
	if(EditId&&EditId!='-1'){
		var arr = "${Out}".split(".");
		var panelId = arr[0];
		var controlName = arr[1];
		var data = {controlName:EditId};
		var data = eval("({" + controlName + ":'" + EditId + "'})");
		$('#'+panelId).form('load',data);
		${ifyes}
	} else{
		${ifno} //20170929 取不到ID时走no分支
	}
 }

