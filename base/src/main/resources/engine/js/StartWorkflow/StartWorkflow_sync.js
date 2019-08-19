var ${name}=function(){
	${logBegin}
    var url = '${ctx}${server}';
    var token = ${getToken};
    var param1 = {token: token};
    var param={};
    if ('${In}') {

        var modelId = getPanelControlValue("${In}");
        var workflow ="";
        if ('${Workflow}') {
            if ('${Workflow}'.indexOf(".") > -1) {

                workflow=getPanelControlValue('${Workflow}');
            } else {
                workflow='${Workflow}';
            }
            param2 = {processDefinitionKey:workflow ,viewUrl:'${ViewUrl}',modelId: modelId, panelName: '${In}'};
            param = $.extend(param1, param2);
            ajaxPost(url,param,function(res){//succeeded
                    ${ifyes}
                },
                function(res){//error
                    ${ifyes}
                })
        }else{
            showError("没有配置WorkFlow参数");
        }
    }else{
        showError("没有配置In参数");
    }
}
