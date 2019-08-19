/**
 * 弹出搜索对话框,返回时将value和text填充到指定input
 * 必须前两列是编码和名称
 */
var ${name} = function (name,value){
	${logBegin}
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var url = "${ctx}${server}?token=" + token+"&EditMode=${Mode}";
	var filterValue = getPanelControlValue("${In}");
	console.log("find查询参数值 in-value -> ${In} = " + filterValue+"--->操作模式：${Mode}");
	
	if("${Mode}"=='Edit' ||"${Mode}"=='Readonly'){
		//修改时需要获取文件id
		url += "&fileId="+filterValue;
	}else if("${Mode}"=='Add' || "${Mode}"=='Edit'){
		url += "&cataId=" + filterValue;
	}
    	    var content = '<iframe id="frmDlg1" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:none"></iframe>';  
    	    var boarddiv = '<div id="tmpSearcher" style="overflow:hidden;width:90%;height:90%;" title="${Title}"></div>'//style="overflow:hidden;"可以去掉滚动条
    	    	
    	    $(document.body).append(boarddiv);
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
    	    var win = $('#tmpSearcher').dialog({
    	        content: content,  
    	        width: 800,
    	        height:550,
    	        modal: true,   //默认为模式对话框
    	        title: "${Title}",  
    	        top:0,
    	        onClose:function(){
    	        	try{
    		    	   if(flg){
    	        		showSaved();
    	        		flg = undefined;
    		    	    ${ifyes}
    		    	   }
    	        	} catch (e){
    	        	}
    	        }
    	    });  
    	    win.dialog('open'); 
    	    return win;
}