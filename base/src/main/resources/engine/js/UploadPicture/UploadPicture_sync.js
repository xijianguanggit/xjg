var ${name}=function(){
	${logBegin}
	var token = ${getToken};
	var out
	if('${Out}'){
		out="${Out}".replace("\.","_");
	} else {
		console.log("uploadPicture 没有获取到${out}值");
	}
	if('${minSize}'){
		var minSize=eval("${minSize}");
	} else {
		var minSize=[50, 50];
	}
	if('${maxSize}'){
		var maxSize=eval("${maxSize}");
	} else {
		var maxSize=[300, 300];
	}
	if('${type}'){
		var type=eval("${type}");
	} else {
		var type=['jpg', 'jpeg', 'png', 'gif'];
	}
    Crop.init({
        id: "TCrop",
        /* 上传路径 */
        url: '${ctx}/localUpload',
        /* 允许上传的图片的后缀 */
        allowsuf: type,
        /* JCrop参数设置 */
        cropParam: {
        	out:out,
            minSize: minSize,          // 选框最小尺寸
            maxSize: maxSize,        // 选框最大尺寸
            allowSelect: true,          // 允许新选框
            allowMove: true,            // 允许选框移动
            allowResize: true,          // 允许选框缩放
            dragEdges: true,            // 允许拖动边框
            onChange: function(c) {},   // 选框改变时的事件，参数c={x, y, x1, y1, w, h}
            onSelect: function(c) {}    // 选框选定时的事件，参数c={x, y, x1, y1, w, h}
        },
        /* 是否进行裁剪，不裁剪则按原图上传，默认进行裁剪 */
        isCrop: true,
        /* 图片上传完成之后的回调，无论是否成功上传 */
        onComplete: function(data) {
        	url='${ctx}${server}'
            console.log('upload complete!');
        }
    });
}


