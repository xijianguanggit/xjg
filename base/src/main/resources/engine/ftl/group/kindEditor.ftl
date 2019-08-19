<script>
//上传图片已经指向至后台服务器代码实现,
var editor1_${panelObj.name}_${g.name} ;
KindEditor.ready(function(K) {
	editor1_${panelObj.name}_${g.name} = K.create('textarea[name="${g.name}"]', {
				cssPath : '${ctx}/view/common/css/plugins/kindEditor/plugins/code/prettify.css',
				//uploadJson : '${ctx}/view/common/css/plugins/kindEditor/jsp/upload_json.jsp',
				uploadJson : '${ctx}/localKEUpload',
				//fileManagerJson : '../view/common/css/plugins/kindEditor/jsp/file_manager_json.jsp',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					self.sync();
				},
				afterChange:function(){
				},
				afterBlur:function(){
					//blur事件可以设置html值和获取html值
					this.sync();
				}
			});
			//prettyPrint();
		});
		
</script>
<div class="input-group-inline js-KindEditor_${panelObj.name}" dataOptions="">
	    <label style="margin-left :3px;
	    <#if g['required']&&EditMode != 'Readonly'&&g['edit'] >
		    color:red;font-weight:bold;
		    </#if>
 	    	<#if g.height gt 1 > 
		    vertical-align:top;
		    </#if>
		    "
	    >${g.title}</label> 
		<textarea name="${g.name}" id="${panelObj.name}_${g.name}" width-data="${g.widthPencent}" cols="100" rows="8" style="height:${g.height?number*22}px;visibility:hidden;" class="textareakind"  >
		</textarea>
	<br />
</div>