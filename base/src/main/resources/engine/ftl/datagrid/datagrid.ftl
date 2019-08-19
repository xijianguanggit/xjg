<script>
	function opFormatter_${panelObj.name}(val,row,index){
		GridButtonContent = "";
		var html = "";
		if(!"${panelObj.modelId}"){
			console.log("grid可能没有配置Object属性或者Object没有主键？");
			var id='';
		} else {
			var id=row['${panelObj.modelId}'];
		}
		
		<#list panelObj.controlList as g>
		<#if '${g.type}'=='Link'>
		if(row["${g.name}"]!=undefined) {
			GridButtonContent = row["${g.name}"];
			html += "<a class='common' href='javascript:setId(" + index + ",\""+id+"\");${g.jsEvent}();'><u>";
			html += GridButtonContent + "</u></a>    ";
		} 
		</#if>
		</#list>
		return html;
	}
	<#list panelObj.controlList as g1> 
		<#if '${g1.type}'=='DataLink'> 
			function viewFormatter_${panelObj.name}_${g1.name}(val,row,index){
				if(val){
					if(row['x_${g1.name}']==''){
						return val;
					}else{
						var id=row['${panelObj.modelId}'];
						return "<a class='common' href='javascript:setId(" + index + ",\""+id+"\");${g1.jsEvent}();'><u>" + val  + "</u></a>";
					}
				}else{
					return "";
				}
			}
		</#if>
		<#if '${g1.type}'=='CheckBox'> 
		function chkFormatter_${panelObj.name}_${g1.name}(val,row,index){
		    if (val == "1") {
		        return "<input type='checkbox' checked='checked' >";
		    }else {
		        return "<input type='checkbox' >";
		    }
		}
		</#if>
		<#if '${g1.type}'=='RadioButton'> 
		function rbFormatter_${panelObj.name}_${g1.name}(val,row,index){
			//返回和editor一样
			if (arr${panelObj.name}_${g1.name}==undefined) {
				console.log("radio值未初始化");
			}else{
				content = "";
				for(var i = 0,len = arr${panelObj.name}_${g1.name}.length; i < len; i++){
				    var row = arr${panelObj.name}_${g1.name}[i];
				    if(val==row.value){
						content += "<input type='radio' checked name='tmp${panelObj.name}_${g1.name}" + index + "' value='" + row.value + "'>";
					}else{
						content += "<input type='radio'         name='tmp${panelObj.name}_${g1.name}" + index + "' value='" + row.value + "'>";
					}
					content += "<label style='margin-left :3px;' style='margin-left :3px;'>" + row.text + "</label>";
				} 
			}
			return content;
		}
		</#if>		
		<#if '${g1.type}'=='ImageView'> 
			function imageFormatter_${panelObj.name}_${g1.name}(val,row,index){
			debugger
				if(val != null && String(val).indexOf("http:") != -1){
					return '<img src='+val+' style="width:66px; height:60px"/>';
				}else{
					return '<img src="${ctx}/localDownload?methodType=markdown&fileId='+val+'" style="width:66px; height:60px"/>';
				}
			}
		</#if>
	</#list>
	//用于拼接操作列链接时简化调用 。否则容易引起运行时刻的easyui解析错误（多空格等）造成的语句无法成功执行
	function setId(index,id){
		setStorage('EditIndex', index);
		setPanelId("${panelObj.name}", id);
	}
	
	
		var editIndex_${panelObj.name} = undefined;
		function endEditing_${panelObj.name}(dg){
			if (editIndex_${panelObj.name} == undefined){return true}
			if (dg.datagrid('validateRow', editIndex_${panelObj.name})){
					dg.datagrid('endEdit', editIndex_${panelObj.name});
				editIndex_${panelObj.name} = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickRow_${panelObj.name}(index, dg){
			//if (editIndex_${panelObj.name} != index){
				if (endEditing_${panelObj.name}(dg)){
					dg.datagrid('selectRow', index);
							dg.datagrid('beginEdit', index);
					editIndex_${panelObj.name} = index;
				//} else {
					//dg.datagrid('selectRow', editIndex_${panelObj.name});
				//}
			}
		}
</script>
 
 		<#assign containsButton = '1'>
 		<#list panelObj.controlList as g>
			<#if '${g.type}'=='Button'>
				<#assign containsButton = '0'>
			</#if>
		</#list>

 <table  class="easyui-datagrid"  
				name="${panelObj.name}" id="${panelObj.name}" 
				data-options="
				<#if '${panelObj.edit}'=='edit' && '${EditMode}'!='Readonly'>
				onBeforeEdit:function(index,row){
					row.editing = true;
					$(this).datagrid('updateRow',{
						index: index,
						row:{}
					});
				},
				rowStyler: function (index, row) {
	                    if (row['delete'] == '1') {//条件      
                            return 'display:none';
                        }
                },
				onBeginEdit:function(index, rowData){
					var fileds=$('#${panelObj.name}').datagrid('getEditors',index);
					var controlMap={};
					<#list panelObj.controlList as c>
					controlMap['${c.property}']='${c.name}';
					</#list>
					for(var i=0;i<fileds.length;i++){
			            var smEditor = $('#${panelObj.name}').datagrid('getEditor', {  
			                index : index,    
			                 field : fileds[i].field
			            }); 
						if(fileds[i].type=='combobox'){
				            $(smEditor.target).combobox('loadData',$('#${panelObj.name}_'+controlMap[fileds[i].field]).combobox('getData'));
						}
					}
				},
				</#if>
				
				<#if containsButton == '0'>
					 toolbar:'#tb_${panelObj.name}',
				</#if>
				
				onClickRow: function (index, row) {
					<#if '${panelObj.edit}'=='edit' && '${EditMode}'!='Readonly'>
					onClickRow_${panelObj.name}(index,$('#${panelObj.name}'));
					</#if>
					$('#${panelObj.name}').datagrid('uncheckAll');
					$('#${panelObj.name}').datagrid('checkRow', index);
					
					
				},
				<#if '${panelObj.isCheckAll}'=='N'>
				onLoadSuccess:function(){
					$('#${panelObj.name}').prev().find('.datagrid-header-check').children().hide()
				},
				</#if>
				fit:true,
				rownumbers: true,
				singleSelect:true,
				selectOnCheck:false,checkOnSelect:true,
				pagination:${panelObj.pagination},
				pageSize:${panelObj.pageSize},
				pageList:${panelObj.pageList},
				nowrap:false,
				view:myview,
				sortName:'${panelObj.sortOrder}',
				sortOrder:'${panelObj.sortName}'"> 
						<thead>
		<#assign GridButtonContent=''>
		<#assign GridButtonContentJS=''>
		<#assign flg=0>
			<tr>
			<#if '${panelObj.multiple}'=='Y'>
			<th data-options="field:'ck',checkbox:true"></th>
			</#if>
			<#list panelObj.controlList as g>  
			<#if '${g.type}'!='Link' && '${g.type}'!='Hidden' &&'${g.type}'!='DataLink'&&'${g.type}'!='Button'>
					 <#if '${g.edit}'=='true'||'${g.type}'=='PopupBox'||'${g.type}'=='FileBox'||'${g.type}'=='DateBox'||'${g.type}'=='TimeBox'||'${g.type}'=='ImageView' ||'${g.type}'=='RadioButton'>
						<#if '${g.type}'=='TextBox'>
						<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center',editor:{type:
						<#if '${g.propertyObj.type}'=='Long'>
						'numberbox'
						<#else>'textbox'
						</#if>
						,options:{validType:'length[1,${g.propertyObj.length}]'
						<#if g.required>
						,required:true,name:'${g.name}'
						</#if>
						}}
						</#if>
						<#if '${g.type}'=='DateBox'>
						<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center',editor:{type:'datebox',options:
							{onSelect:function(){				  
							try {
							eval('OnClick_${panelObj.name}_${g.name}_${ui.name}()');
				 		 } catch(e) {}}}}
						</#if>
						<#if '${g.type}'=='TimeBox'>
						<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center',editor:{type:'timespinner'}
						</#if>
						<#if '${g.type}'=='ComboBox'>
						<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center',
						formatter:function(value){
							var controlMap={};
							<#list panelObj.controlList as c>
							controlMap['${c.property}']='${c.name}';
							</#list>
						 	var data=$('#'+'${panelObj.name}_'+controlMap['${g.property}']).combobox('getData');
								for(var i=0; i<data.length; i++){
									if (data[i].value == value){
										return data[i].text;
									}
								}
						 },
						editor:{type:'combobox',options: { 
						 valueField: 'value', textField: 'text',
 						<#if g.required>
						required:true
						</#if>
						 }}
						</#if>
						<#if '${g.type}'=='CheckBox'>
						<th data-options="field:'${g.property}',width:${g.width},halign:'center',editor:{type:'checkbox',options: {on: '1', off: '0'}},formatter:chkFormatter_${panelObj.name}_${g.name}" 
						</#if>
						<#if '${g.type}'=='RadioButton'>
						<th data-options="field:'${g.property}',width:${g.width},halign:'center',editor:{type:'radiobutton',options: {id: '${panelObj.name}_${g.name}'}},formatter:rbFormatter_${panelObj.name}_${g.name}" 
						</#if>						
						<#if '${g.type}'=='PopupBox'>
						<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center',editor:{type:'searchbox',options:{
 						<#if g.required>
						required:true,
						</#if>
						pName:'${panelObj.name}', cName:'${g.name}',datagrid:'#${panelObj.name}',searcher:'OnClick_${panelObj.name}_${g.name}_${ui.name}' }}
						</#if>
						<#if '${g.type}'=='FileBox'>
						<th data-options="field:'${g.property}',width:${g.width},halign:'center',editor:{type:'filebox',options:{
 						<#if g.required>
						required:true,
						</#if>
						buttonText:'点击上传',prompt:'请选择文件...',datagrid:'#${panelObj.name}',name:'${g.jsEvent}',<#include 'group/uploadFunction.ftl'>}}
						</#if>
						<#if '${g.type}'=='ImageView'>
						<th data-options="field:'${g.property}',width:'${g.width}', formatter:imageFormatter_${panelObj.name}_${g.name}
						</#if>
					<#else>
						<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center'
					</#if>
						<#if '${g.propertyObj.type}'=='Long'||'${g.propertyObj.type}'=='Date'>
						,align:'center'
						<#elseif '${g.propertyObj.type}'=='Double'>
						,align:'right'
						<#else>
						,align:'left'
						</#if>
						" 
					${g.formatter}>${g.title}</th>
			</#if>
			<#if '${g.type}'=='Link'>
				<#if flg==0>
				<th data-options="fitColumns:true,field:'view',halign:'center',width:${g.width},align:'left',formatter:opFormatter_${panelObj.name}">操作</th>
				</#if>
				<#assign flg=1>
			</#if>
			<#if '${g.type}'=='DataLink'>
				<input type="hidden" name="linkJsFunction" value="${g.jsEvent}()">
				<th data-options="field:'${g.property}',sortable:true,width:${g.width},halign:'center'
					<#if '${g.propertyObj.type}'=='Long'||'${g.propertyObj.type}'=='Date'>
					,align:'center'
					<#elseif '${g.propertyObj.type}'=='Double'>
					,align:'right'
					<#else>
					,align:'left'
					</#if>
				,formatter:viewFormatter_${panelObj.name}_${g.name}">${g.title}</th>
			</#if>
			<#if '${g.type}'=='Hidden'>
				<th data-options="field:'${g.property}',sortable:true,width:0,halign:'center',hidden:true">${g.title}</th>
			</#if>
			</#list>
			</tr>
		</thead>
</table> 
<div id="tb_${panelObj.name}" style="padding:5px;height:auto">
	<div style="margin-bottom:0px">
		<#list panelObj.controlList as g>
			<#if '${g.type}'=='Button'>
			
				<#if '${g.title}'?length gt 0>
					<#assign viewWidth = '${g.title}'?length * 12 + 16 + 'px'>
				<#else>
					<#assign viewWidth = '16px'>
				</#if>
			
				<a class="easyui-linkbutton" href="javascript:void(0)" 
				<#if g.flow??>
					${g.flow.event}="${g.jsEvent}()" 
				</#if> id="${panelObj.name}_${g.name}" name="${g.name}" style="margin-right:10px; width:${viewWidth};">${g.title}</a>
			</#if>
		</#list>
	</div>
</div>
<#list panelObj.controlList as g>  
<#if '${g.type}'=='ComboBox'>
<select class="easyui-combobox" style="display:none" id="${panelObj.name}_${g.name}" name="${g.name}">
</select>
</#if>
<#if '${g.type}'=='RadioButton'>
<div style="display:none" id="${panelObj.name}_${g.name}" />
</#if>
</#list>

