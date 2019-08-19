var ${name}=function(){
	${logBegin}
	var imageName = "${In}".split(",");
	var len = imageName.length;
	var data = [];
	for(i=0; i<len; i++){
		var cValue = "";
		cValue = getPanelControlValue(imageName[i]);
		if(cValue.indexOf("http:") != -1){
			data[i] = {href:"#",img:cValue};
		}else{
			data[i] = {href:"#",img:"${ctx}/localDownload?methodType=upload&fileId="+cValue};
			
		}
	}//必传参数 图片数据
	var photoid = getPanelControlValue(imageName[0]);
    var opts = {
        index:2,                               //开始显示的图片索引值
        carousleTime:5000,                     //轮播图速度（ms）
        imgWidth:150,                          //图片的宽度
        parentDom:".carousel",                 //必传参数 父容器
        data:data,
    }
    var carousel = new Carousel(opts); 
	${ifyes}
}


