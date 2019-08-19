
var ${name}=function() {
	${logBegin}
	//保持页面原有条件
	var token = ${getToken};
	restoreToken("${functionId}",token);//不消费
	var url = "${ctx}${server}/${To}?token=" + token + "&type=Popup&puid=" + $("#uiid").val() + "&EditMode=${Mode}&from="+$("#from").val()+"&code="+$("#code").val()+"&theme=${theme}";
	setStorage('${To}_EditMode','${Mode}');
	    var content = '<iframe id="frmDlg" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:0;"></iframe>';  
	    var boarddiv = '<div id="tmpDlg" style="overflow:hidden;" title="' + '${Title}'+ '"></div>'// style="overflow:hidden;"可以去掉滚动条
	    $(document.body).append(boarddiv);
	    var windowSize;
	    if("${windowSize}"){
	    	if("${windowSize}"=="Xlarge"){
	    		windowSize=document.documentElement.clientWidth+"|"+document.documentElement.clientHeight
	    	} else {
	    		if("${windowSize}".search(",")>0){
	    			var sizes = "${windowSize}".split(",");
	    			if(sizes[0].search("%")>0){
	    				windowSize =Number(document.documentElement.clientWidth)*sizes[0].split("%")[0]/100+"|"+Number(document.documentElement.clientHeight)*sizes[1].split("%")[0]/100;
	    			}else{
		    			windowSize="${windowSize}";
		    		}
	    		}else{
	    			windowSize="${windowSize}";
	    		}
	    	}
	    }  else {
	    	windowSize="${Small}";
	    }
	    var width;
	    var height;
	    width=windowSize.split("|")[0];
	    height=windowSize.split("|")[1];
	    if("${windowSize}"=="Xlarge"){
	    	var left=0;
	    } else {
	    	var left=(this.innerWidth-width)/2;//(screen.width-width)/2-150;
	    }
	    var win = $('#tmpDlg').dialog({
	        content: content,  
	        width: width,
	        height: height,  
	        modal: true,   // 默认为模式对话框
	        title: '${Title}',  
	        top:0,
	        left:left,
	        onClose:function(){
//	        	$("#isMenu").val($('.js-ui').attr('id'));
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
