/**
 * 弹出搜索对话框,返回时将value和text填充到指定input
 * 必须前两列是编码和名称
 */
var ${name} = function (name,value){
	${logBegin}
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var url = "${ctx}${server}?token=" + token;
	var filterValue = "";
	if("${In}".indexOf(".")>0){
		filterValue = getPanelControlValue("${In}");//panel.control。兼容旧版
	}
	if(filterValue){
		url += "&id=" + filterValue;
	}
    var content = '<iframe id="frmDlg1" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:none"></iframe>';  
    var boarddiv = '<div id="tmpSearcher" style="overflow:hidden;width:90%;height:80%;" title="${Title}"></div>'//style="overflow:hidden;"可以去掉滚动条
   	 $(document.body).append(boarddiv);
    //window.top.$('#tmpSearcher').hide();
    //if(window.top.$('#tmpSearcher').length<1){
    //	window.top.$(boarddiv).appendTo(window.top.document.body);	
    //}else{
    //	window.top.$('#tmpSearcher').html(boarddiv);
    //}
	
    var windowSize;
    if("${windowSize}"){
    	windowSize="${windowSize}";
    }  else {
    	windowSize="${Medium}";
    } 
    var width;
    var height;
    width=windowSize.split("|")[0];
    height=windowSize.split("|")[1];
    //var win = window.top.$("#tmpSearcher").dialog({
    var win = $('#tmpSearcher').dialog({
        content: content,  
        width: 390,
        modal: true,   //默认为模式对话框
        title: "${Title}",  
        top:80,
        onClose:function(){
        	$("#fromUi").val($('.js-ui').attr('id'));
			var retValue = win.find('iframe').get(0).contentWindow.retValue;
			var retName = win.find('iframe').get(0).contentWindow.retName;
			var selectType = win.find('iframe').get(0).contentWindow.selectType;//1是表格行选择
			var selected = win.find('iframe').get(0).contentWindow.selected;
			var retRow = win.find('iframe').get(0).contentWindow.retRow;
			var multi = false;
			if(selected){
				if(retRow.length){
					multi = true;//
					console.log("多选返回值");
				}
				var dataChanged = false;//选择前后值是否有变化
				var Columns = "${Columns}";//"id,编码|name,姓名,lk_empName|email,邮箱"
				var Options = "${Options}";
				var arrC = Columns.split("|");
				var data = {};
				if(selectType=="1") {
					dataChanged = true;
					var len = arrC.length;
					for(var i=0;i<len;i++){//逐列循环
						var arrProperty = arrC[i].split(",");//参数0:表格属性;参数2:目标组件名
						if(arrProperty[2]!=undefined){//有 目标填充组件
							if(multi){
								data[arrProperty[2]] = getPropertyValues(retRow,arrProperty[0]);
							}else{
								data[arrProperty[2]] = retRow[arrProperty[0]];
							}
						}
					}
				}else{
					data = retRow;//固定项已构造好组件属性值
				}
				if($('#${Out}').attr("class").indexOf("js-group")>-1){
    		    	$('#${Out}').form('load',data);
				} else if($('#${Out}').attr("class").indexOf("easyui-datagrid")>-1){
					loadGridRow($('#${Out}'), data);
				}
				if(dataChanged){
					${ifyes}	
				}else{
					${ifno}
				}
			}else{
				${ifno}
			}
        }
    });  
    win.dialog('open'); 
    return win;
}