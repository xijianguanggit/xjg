var ${name}=function(){
	${logBegin}
	var typeMap=new Object();
	var typeString = "${Type}";
	if(typeString.indexOf("jpeg")!=-1){
		reg = new RegExp("jpeg","g");
		typeString = typeString.replace(reg,"image/jpeg");
	}
	if(typeString.indexOf("jpg")!=-1){
		typeString = typeString.replace("jpg","image/jpeg");
		//alert(typeString);
	}
	if(typeString.indexOf("png")!=-1){
		 reg = new RegExp("png","g");
		 typeString = typeString.replace(reg,"image/png");
	}
	if(typeString.indexOf("gif")!=-1){
		 reg = new RegExp("gif","g");
		typeString = typeString.replace(reg,"image/gif");
	}
	if(typeString.indexOf("txt")!=-1){
		 reg = new RegExp("txt","g");
		typeString = typeString.replace(reg,"text/plain");
	}
	if(typeString.indexOf("doc")!=-1){
		 reg = new RegExp("doc","g");
		typeString = typeString.replace(reg,"application/msword");
	}
	if(typeString.indexOf("dot")!=-1){
		 reg = new RegExp("dot","g");
		typeString = typeString.replace(reg,"application/msword");
	}
	if(typeString.indexOf("pdf")!=-1){
		 reg = new RegExp("pdf","g");
		typeString = typeString.replace(reg,"application/pdf");
	}
	if(typeString.indexOf("ppt")!=-1){
		 reg = new RegExp("ppt","g");
		typeString = typeString.replace(reg,"application/vnd.ms-powerpoint");
	}
	if(typeString.indexOf("wps")!=-1){
		 reg = new RegExp("wps","g");
		typeString = typeString.replace(reg,"application/vnd.ms-works");
	}
	if(typeString.indexOf("md")!=-1){
		reg = new RegExp("wps","g");
		typeString = typeString.replace(reg,"application/vnd.ms-works");
	}
	$("#fileUploadId").attr("accept",typeString);
	var token = ${getToken};
	window.upToken = token;
	window.fileSize = "${Size}";
	window.fileType = "${Type}";
	window.fileAccessType = "${Bucket}";
	window.fileUploadUrl = "${UploadUrl}";
	window.outParam = "${Out}";
	window.pathParam = "${Path}";
	window.moduleParam = "${Module}";
	restoreToken("${functionId}",token);
}


