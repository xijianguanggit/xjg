var ${name} = function (){
	${logBegin}
	
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var url = "${ctx}${server}?token=" + token;
	debugger;
	var content = '<iframe id="frmDlg" src="' + url + '" style="overflow:hidden;width:100%;height:100%;">'+
	+' <table  class="easyui-datagrid" name="displayGrid" id="displayGrid" ' 
		+'data-options=\'onDblClickCell:function(index,field,value){$(this).datagrid("beginEdit", index);},fit:true,singleSelect:true,selectOnCheck:false,checkOnSelect:false,toolbar:"#tb_displayBar",halign:"center",fitColumns:false,selectOnCheck:false,checkOnSelect:false,pagination:false,columns:['+columns+'],nowrap:false\'></table>';
    	+'<div id="tb_displayBar" style="padding:5px;height:auto"><div style="margin-bottom:5px">';
    	+'<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$(\'#displayGrid\').datagrid(\'checkAll\')">全选</a>  ';
    	+'<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$(\'#displayGrid\').datagrid(\'clearChecked\')">取消全选</a>';
    	+'<a class="easyui-linkbutton" href="javascript:void(0)" onclick="moveUp(\'displayGrid\')">上移</a>';	
    	+'<a class="easyui-linkbutton" href="javascript:void(0)" onclick="moveDown(\'displayGrid\')">下移</a>';	
    	+'<a class="easyui-linkbutton" href="javascript:void(0)" onclick="confirmDisplay(\'displayGrid\',\'csd\',\''+$(jq).attr("id")+'\')">确定</a>';	
    	+'<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$(\'#csd\').dialog(\'close\')">取消</a></div></div>'+'</iframe>';
	
	var dlg = $("<div id='zichax' style='overflow:hidden;width:90%;height:90%;'/>").dialog({
	    title: '编辑子查询',
	    width: 600,
	    height: 500,
	    content:content,
	    modal: true,
	    onClose : function() {
            $(this).dialog('destroy');
        }
    });
	dlg.dialog('open');
	//加载数据
	var rowdata=new Array();
	$.each(columnFields, function (i, columnField) {
		var fieldOptions = oGrid.datagrid("getColumnOption", columnField);
		if(!fieldOptions.formatter&&fieldOptions.field!='ck'){
			rowdata.push({displaytitle:fieldOptions.title,displaywidth:fieldOptions.width,field:fieldOptions.field});
		}
	});
	$("#displayGrid").datagrid('loadData', rowdata); 
	//设置勾选状态
	var displayData = $('#displayGrid').datagrid("getData");
	$.each(columnFields, function (i, columnField) {
		var fieldOptions = oGrid.datagrid("getColumnOption", columnField);
		for(var j=0;j<displayData.rows.length;j++){
			if(!fieldOptions.formatter&&fieldOptions.field!='ck'&&!fieldOptions.hidden&&columnField==displayData.rows[j].field){
				$("#displayGrid").datagrid("checkRow",j);
			}
		}
	});
	
}