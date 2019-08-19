// 取得当前dg选中行id
//当对应的panel组件是dataGird时，取dataGird当前选中行对应的主键列的值
//当panel是普通panel时，取panel的对应组件值
var ${name}=function(){
	${logBegin}
	var currentId = getPanelControlValue("${In}");//更多组件支持请扩展getPanelControlValue方法
	// id可能是用，分隔的空id 例如[,,,]
	if(currentId===currentId+''&&currentId.indexOf(",")>-1){
		var arr = currentId.split(",");
		var flg=true;
		for(var i=0;i<arr.length;i++){
			if(!arr[i]||arr[i]=='-1'){
				flg=false;
				break;
			} 
		}
		if(flg){
			setStorage('EditId', currentId);
			${ifyes}
		} else {
			${ifno}
		}
	} else {
		if(currentId&&currentId!='-1'){
			setStorage('EditId', currentId);
			${ifyes}
		} else {
			${ifno}
		}
	}
 }
 
