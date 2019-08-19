<script>
//上传图片已经指向至后台服务器代码实现,
var editor1_${panelObj.name}_${g.name} ;

UE.getEditor('${panelObj.name}_${g.name}',{

//这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
//toolbars:[['FullScreen','simpleupload','Source', 'Undo', 'Redo','Bold','test']],

//focus时自动清空初始化时的内容
autoClearinitialContent:true,

//关闭字数统计
wordCount:false,

//关闭elementPath
elementPathEnabled:false,

//默认的编辑区域高度
initialFrameHeight:300
//更多其他参数，请参考ueditor.config.js中的配置项
});
UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
	//这里很重要，很重要，很重要，要和配置中的imageActionName值一样

	if (action == 'uploadimage' || action == 'uploadfile' || action == 'uploadvideo') {
	//这里调用后端我们写的图片上传接口
		return '${ctx}/localUEUpload';
	}else{
		return this._bkGetActionUrl.call(this, action);
	}
}
</script>
<div class="input-group-inline js-UEditor_${panelObj.name}" id="${g.name}" <#if EditMode == 'Readonly' || !g['edit']> readonly="true" </#if>>
	    <label style="margin-left :3px;
	    <#if g['required']&&EditMode != 'Readonly'&&g['edit'] >color:red;font-weight:bold;</#if>
 	    <#if g.height gt 1 > vertical-align:top;</#if> ">${g.title}</label>
		<script id="${panelObj.name}_${g.name}" type="text/plain" style="width:${g.width?number*48}px;height:${g.height?number*22}px;margin-left:100px;margin-top:-14px;" width-data="${g.widthPencent}"></script>
	<br />
</div>