var ${name}=function(){
    ${logBegin}
    var title="代码编辑";
    parent.showError(${Msg});
    var token = ${getToken};
    var catalog  = getPanelControlValue('${In}')
    console.log("hash " + catalog  + "==>" +MD5(encodeURI(catalog.replace(/\//g,''))));
    var url = "${ctx}${server}?token=" + token+"&key="+ MD5(encodeURI(catalog.replace(/\//g,''))) + "&catalog="+catalog;
    restoreToken("${functionId}",token);
    window.open(url);



}