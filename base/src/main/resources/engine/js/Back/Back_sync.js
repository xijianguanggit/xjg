var ${name}=function() {
	${logBegin}
	var token = ${getToken};
	if(token){
		//返回需要记录跳转路径 做成数组 没跳转一次push back一次pop tab关闭时候清除storage 唯一标识为页面code和from
		var path=$("#from").val()+$("#code").val();
		var pathValue = JSON.parse(JSON.stringify(getStorage(path)));
		pathValue.pop();
		setStorage(path, pathValue);
		var pathNameValue = JSON.parse(JSON.stringify(getStorage(path + "_name")));
		pathNameValue.pop();
		setStorage(path + "_name", pathNameValue);
		//window.location.href = "${ctx}${server}/"+getStorage(path)[getStorage(path).length-1]+"?EditMode=${Mode}&token=" + token + "&type=Back&puid=" + $("#uiid").val() + "&back=Back&code="+$("#code").val()+"&from="+$("#from").val();
		if(pathValue==null||pathValue==''){
			console.log("跳转到首页！");
			window.location.href = "${ctx}/content/index";
		}else{
			console.log("正常跳转！");
			window.location.href = "${ctx}${server}/"+getStorage(path)[getStorage(path).length-1]+"?token=" + token + "&type=Back&puid=" + $("#uiid").val() + "&back=Back&code="+$("#code").val()+"&from="+$("#from").val();
		}
	}
}
