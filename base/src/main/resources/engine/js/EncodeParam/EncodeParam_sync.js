// 取得当前dg选中行id
//当对应的panel组件是dataGird时，取dataGird当前选中行对应的主键列的值
//当panel是普通panel时，取panel的对应组件值
var ${name}=function(){
	${logBegin}
	var panelControlName = "${In}";//pTable.item
	var arr = panelControlName.split(".");
	var panelId = "#"+arr[0];
	var controlName = arr[1];	
	var rowIndex = getStorage('EditIndex');
	try{
		var className = $(panelId).attr("class");//表格
		if(className==undefined || className.indexOf("easyui-datagrid")<0){
			var paramObj = $(panelId).find('select, textarea, input, hidden').serializeJson();//构造参数
			setEncodeParam('${Out}',paramObj);
			${ifyes}
		}else{
			var row = $(panelId).datagrid("getRows")[rowIndex];
			if(row && row[controlName+"Drill"]){
				var param = row[controlName+"Drill"];//json string
				var paramObj = eval("(" + param + ")");
				setEncodeParam('${Out}',paramObj);
				${ifyes}
			}else{
				${ifno}
			}
		}
	} catch (e) {
		console.log(e.name + ": " + e.message);
	}
 }
 
