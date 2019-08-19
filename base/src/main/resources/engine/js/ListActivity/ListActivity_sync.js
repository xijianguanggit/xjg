var ${name}=function(){
    ${logBegin}
	var title="流程进度查看";
    var token = ${getToken};
    var url = "${ctx}${server}?token=" + token;
    var filterValue = getPanelControlValue("${In}");
    var workflow="";
    console.log("流程进度查看-业务ID-> ${In} = " + filterValue);
    if(filterValue){
        url += "&modelId=" + filterValue;
        if ('${Workflow}'.indexOf(".") > -1) {

            workflow=getPanelControlValue('${Workflow}');
        } else {
            workflow='${Workflow}';
        }
        url+="&processDefinitionKey="+workflow;
    }
    restoreToken("${functionId}",token);
    var content = '<iframe id="workFlowListWindow" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:0;"></iframe>';
    var boarddiv = '<div id="workFlowListWindowDiv" style="overflow:hidden;" title="'+title+'"></div>'
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
    var win = $('#workFlowListWindowDiv').dialog({
        content: content,
        width: width,
        height: height,
        modal: true,   // 默认为模式对话框
        title: title,
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
