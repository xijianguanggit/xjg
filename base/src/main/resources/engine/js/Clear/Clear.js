//初始化panel内的组件值
////easyui对组件的命名问题，不能用id引用到实际输入值的input，实际input是一个只有namedhidden
//使用组件时需用其对应的赋值方式
var ${name}=function (){
	${logBegin}
	var token = ${getToken};
	var param = {"token":token};
	restoreToken("${functionId}",token);
	var url='${ctx}${server}';
	if("${Out}" && "${Out}".indexOf("|")>0){
		${emptyControlsStatemet};
		${ifyes}
	}else{
		var out=$('#${Out}');
	    var ifyes='${ifyes}'.replace("();","");
	    var ifno='${ifno}'.replace("();","");
		eval('${loadData}(out,url,param, "${setOption}", eval(ifyes),eval(ifno))');
	}
 }
