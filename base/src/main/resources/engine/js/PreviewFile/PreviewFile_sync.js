/**
 * 文件预览功能 （目前只支持图片显示，以后会逐渐扩展）
 */
var ${name} = function (name,value){
	var token = ${getToken};
	restoreToken("${functionId}",token);
	var url = "${ctx}${server}?token=" + token;
	setStorage('${To}_EditMode','${Mode}');
	var filterValue = getPanelControlValue("${In}");
	console.log("previewFile查询参数值 in-value -> ${In} = " + filterValue);
	if(filterValue){
		url += "&id=" + filterValue;
	}
	
    	    var content = '<iframe id="frmDlg3" src="' + url + '" style="overflow:hidden;width:100%;height:100%;"></iframe>';  
    	    var boarddiv = '<div id="tmpSearcher1" style="overflow:hidden;width:90%;height:90%;" title="${Title}"></div>'// style="overflow:hidden;"可以去掉滚动条
    	    $(document.body).append(boarddiv);
    	    var windowSize;
    	    if("${windowSize}"){
    	    	if("${windowSize}"=="Xlarge"){
    	    		windowSize=document.documentElement.clientWidth+"|"+document.documentElement.clientHeight;
    	    	} else {
    	    		windowSize="${windowSize}";
    	    	}
    	    }  else {
    	    	windowSize="${Large}";
    	    }
    	    var width;
    	    var height;
    	    width=windowSize.split("|")[0];
    	    height=windowSize.split("|")[1];
    	    var wintop;
    	    try{
    	    	wintop = window.top;
    	    	wintop.$('#tmpSearcher1');
    		}catch(e){
    			wintop = window.self;
    		}
    	    if(wintop.$('#tmpSearcher1').length<1){
    	    	wintop.$(boarddiv).appendTo(wintop.document.body);	
    	    }else{
    	    	wintop.$('#tmpSearcher1').html(boarddiv);
    	    }
    	    var win = wintop.$("#tmpSearcher1").dialog({
    	        content: content,  
    	        width: width,
    	        height: height-30-10,  
    	        modal: true,   // 默认为模式对话框
    	        title: "文件预览",  
    	        top:0,
    	       /* left:left, */
    	    });  
    	    win.dialog('open'); 
    	    return win;
}