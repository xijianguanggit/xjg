var ${name} =function () {
	${logBegin}
    var out = '${Out}';
    if ('${In}') {
        var param2 = $("#${In} :input,hidden").serializeJson();
    }
    debugger;
    var token = ${getToken};
	restoreToken("${functionId}",token);
    var param = {token: token};
    if (param2) {
        var param = Object.assign(param, param2);
    }
    if("${setParam}"){
    	param=eval("${setParam}(out,param);");
    }
    if("${getOption}"){
    	eval("${getOption}(out);");
    }
    var ifyes='${ifyes}'.replace("();","");
    var ifno='${ifno}'.replace("();","");
    eval("${loadData}(out,'${ctx}${server}',param,'${setOption}',eval(ifyes),eval(ifno))");
//   ${loadData}
    isMenu = undefined;
};