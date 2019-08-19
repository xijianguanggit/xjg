var ${name}=function() {
	var url = "${ctx}${server}/${To}?EditMode=${EditMode}";
	var uiName;
	if("${EditMode}".toLowerCase()=="add"){
		uiName="新增";
	} else if("${EditMode}".toLowerCase()=="edit"){
		uiName="编辑";
	}else if("${EditMode}".toLowerCase()=="readonly"){
		uiName="查看";
	}
	setStorage('${To}_EditMode','${EditMode}');
	    var content = '<iframe id="frmDlg" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:0;"></iframe>';  
	    var boarddiv = '<div id="tmpDlg" style="overflow:hidden;" title="' + uiName+ '"></div>'// style="overflow:hidden;"可以去掉滚动条
	    $(document.body).append(boarddiv);
	    var windowSize;
	    if("${Window}"=="small"){
	    	windowSize="${small}";
	    } else if("${Window}"=="medium"){
	    	windowSize="${medium}";
	    } else if("${Window}"=="large"){
	    	windowSize="${large}";
	    } else {
	    	windowSize="${small}";
	    }
	    var width;
	    var height;
	    width=windowSize.split("|")[0];
	    height=windowSize.split("|")[1];
	    var win = $('#tmpDlg').dialog({
	        content: content,  
	        width: width,
	        height: height,  
	        modal: true,   // 默认为模式对话框
	        title: uiName,  
	        top:0,
	        onClose:function(){
	        	try{
		    	    if(flg){
		    	    	${ifyes}
		    	    }
	        	} catch (e){
	        	}
	        }
	    });  
	    win.dialog('open'); 
	    return win;
}
