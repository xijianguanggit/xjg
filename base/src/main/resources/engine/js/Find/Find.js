/**
 * 弹出搜索对话框,返回时将value和text填充到指定input
 * 必须前两列是编码和名称
 */
var ${name} = function (name,value){
	var url = "${ctx}${server}?Sql=${Sql}&token=" + ${getToken};
    	    var content = '<iframe id="frmDlg1" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:none"></iframe>';  
    	    var boarddiv = '<div id="tmpSearcher" style="overflow:hidden;" title="${Title}"></div>'//style="overflow:hidden;"可以去掉滚动条  
    	    $(document.body).append(boarddiv);
    	    var windowSize;
    	    if("${windowSize}"){
    	    	windowSize="${windowSize}";
    	    }  else {
    	    	windowSize="${large}";
    	    } 
    	    console.log("..........${Columns}");

    	    var width;
    	    var height;

    	    width=windowSize.split("|")[0];
    	    height=windowSize.split("|")[1];
    	    var win = $('#tmpSearcher').dialog({
    	        content: content,  
    	        width: width,
    	        height: height-30,  
    	        modal: true,   //默认为模式对话框
    	        title: "${Title}",  
    	        top:0,
    	        onClose:function(){
    				var retValue = win.find('iframe').get(0).contentWindow.retValue;
    				var retName = win.find('iframe').get(0).contentWindow.retName;
    				
    				var Columns = "${Columns}";//"id,编码|name,姓名,lk_empName|email,邮箱"
    				var arrC = Columns.split("|");
    				arrC1 = arrC[0].split(",");
    				arrC2 = arrC[1].split(",");
    				var data = {};
    				if(arrC1.length>2){
    					data[arrC1[2]] = retValue;
    				}
    				if(arrC2.length>2){
    					data[arrC2[2]] = retName;
    				}
    		    	$('#${Out}').form('load',data);
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