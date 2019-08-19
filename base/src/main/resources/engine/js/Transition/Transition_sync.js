var ${name}=function() {
	${logBegin}
	var token = ${getToken};
	if(token){
		//返回需要记录跳转路径 做成数组 没跳转一次push back一次pop tab关闭时候清除storage 唯一标识为页面code和from
		eval('${package}');
		setUIMode('${To}', '${Mode}')
		window.location.href = getTransitionUrl(token, 'Transition', $("#uiid").val(),'${Mode}', $("#from").val(), $("#code").val(),'${ToTitle}', '${To}', '${ctx}${server}/');
	}
}
