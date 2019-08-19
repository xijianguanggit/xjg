var ${name} =function () {
	${logBegin}
    var out = '${Out}';
    var condition = new Object();
    if ('${In}') {
    	condition = $("#${In} :input,hidden").serializeJson();
    }
    var token = ${getToken};
	restoreToken("${functionId}",token);
    condition["token"] = token;
    
    if("${setParam}"){
    	condition=eval("${setParam}(out,condition);");
    }
    if("${getOption}"){
    	eval("${getOption}(out);");
    }
    var ifyes='${ifyes}'.replace("();","");
    var ifno='${ifno}'.replace("();","");
    eval("${loadData}(out,'${ctx}${server}',condition,'${setOption}',eval(ifyes),eval(ifno))");
//   ${loadData}
    isMenu = undefined;
};

