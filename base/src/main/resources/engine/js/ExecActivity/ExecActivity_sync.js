var ${name}=function(){
    ${logBegin}
    var title="审批工作流";
    var token = ${getToken};
    var url = "${ctx}${server}?token=" + token;
    var filterValue = getPanelControlValue("${In}");
    console.log("审批工作流-业务ID-> ${In} = " + filterValue);
    restoreToken("${functionId}",token);
    if(filterValue){
        url += "&modelId=" + filterValue;
        if ('${Workflow}'.indexOf(".") > -1) {

            workflow=getPanelControlValue('${Workflow}');
        } else {
            workflow='${Workflow}';
        }
        url+="&processDefinitionKey="+workflow;
    }
    var content = '<iframe id="workFlowWindow" src="' + url + '" style="overflow:hidden;width:100%;height:100%;border:0;"></iframe>';
    var boarddiv = '<div id="workFlowWindowDiv" style="overflow:hidden;" title="'+title+'"></div>'
    $(document.body).append(boarddiv);

    var width;
    var height;
    width=400;
    height=300;
    var  workFlowWindowDiv = $('#workFlowWindowDiv').dialog({
        content: content,
        width: width,
        height: height,
        modal: true,   // 默认为模式对话框
        title: title,
        top:0,
        onBeforeClose:function(){
          var saveOpt =  workFlowWindowDiv.find('iframe').get(0).contentWindow.saveOpt;
          if(saveOpt==0){
              ${ifyno}
          }else{
              ${ifyes}
          }
        }
    });
    workFlowWindowDiv.dialog('open');
    return workFlowWindowDiv;
}