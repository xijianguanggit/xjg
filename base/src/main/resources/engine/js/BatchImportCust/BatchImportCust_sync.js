/**
 * 弹出搜索对话框,返回时将value和text填充到指定input
 * 必须前两列是编码和名称
 */
var ${name} = function (name,value){
	${logBegin}
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var url = "${ctx}${server}?title=${Title}&token=" + token;
    	    var content = '<iframe id="frmDlgImport" src="' + url + '" style="overflow:hidden;width:100%;height:100%;"></iframe>';  
    	    var boarddiv = '<div id="tmpImport" style="overflow:hidden;width:90%;height:90%;" title="${Title}"></div>'//style="overflow:hidden;"可以去掉滚动条
    	    	
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
    	    var win = $('#tmpImport').dialog({
    	        content: content,  
    	        width: width,
    	        height:height,
//    	        height: height-200-10,  //不要高度了
    	        modal: true,   //默认为模式对话框
    	        title: "${Title}",  
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