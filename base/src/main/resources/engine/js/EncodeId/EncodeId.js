// 取得当前dg选中行id
//当对应的panel组件是dataGird时，取dataGird当前选中行对应的主键列的值
//当panel是普通panel时，取panel的对应组件值
var ${name}=function(){
	var arr = "${In}".split(".");
	var panelId = "#"+arr[0];
	var controlName = arr[1];
	var className = $(panelId).attr("class");
	var currentId = "-1";
	if(!className) return currentId;
	
	if(className.indexOf('datagrid')>=0){
		var rowIndex = getStorage('EditId');
		if(rowIndex == null){
			rowIndex = 0;
		}
		var row = $(panelId).datagrid('getRows')[rowIndex];
		setStorage('EditId', row[controlName]);
	}else{
		currentId = $(getControlId("${In}")).val();
		setStorage('EditId', currentId);
	}
	${ifyes}
 }

