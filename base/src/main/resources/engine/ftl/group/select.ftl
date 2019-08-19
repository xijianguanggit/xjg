<#-- 总宽度减去东西减去边框乘以比例减去marginpx-->
	<div class="input-group-inline" >
	    <label style="margin-left :3px;<#if g['required']&&EditMode != 'Readonly'&&g['edit'] > color:red;font-weight:bold</#if>">${g.title}</label>
    	<select class="easyui-combobox js-input"  width-data="${g.widthPencent}" style="width:2px;height:22px" tipPosition="bottom"
    	<#if EditMode == 'Readonly' || !g['edit']> readonly="true" </#if><#if g['required']&&EditMode != 'Readonly'&&g['edit'] > required="true" </#if> 
    	id="${panelObj.name}_${g.name}" name="${g.name}" data-options="editable:false,<#if EditMode == 'Readonly' || !g['edit']>cls:'readgrey',</#if> 
    	<#if g['multiple']>
	    	multiple:true,
	     	formatter:function(row){ 
		     	if(row.value == 'YorN'){
		             var opts = $(this).combobox('options');  
		             return  row[opts.textField];
		     	}
	            var opts = $(this).combobox('options');  
             	return '<input type=\'checkbox\' class=\'combobox-checkbox\' >' + row[opts.textField];
		     	
        	},
            onLoadSuccess: function () {  //下拉框数据加载成功调用  
                var opts = $(this).combobox('options');  
                var target = this;  
                var values = $(target).combobox('getValues');//获取选中的值的values  
                $.map(values, function (value) {
                    var el = opts.finder.getEl(target, value);  
                    el.find('input.combobox-checkbox')._propAttr('checked', true);   
                })  
            },  
            onSelect: function (row) {
			        var opts = $(this).combobox('options');
			        var el = opts.finder.getEl(this, row[opts.valueField]);
			        el.find('input.combobox-checkbox')._propAttr('checked', true);
			    },  
            onUnselect: function (row) {
			    var opts = $(this).combobox('options');
			    var el = opts.finder.getEl(this, row[opts.valueField]);
			    el.find('input.combobox-checkbox')._propAttr('checked', false);
			},
    	</#if>  
    	">
		</select>
    	<script>
	    	$('#${panelObj.name}_${g.name}').combobox({
				onChange: function changePercent(value)  {
				//修正comboBox多选时清空所选内容会保留最后一次所选bug
				if(value!=null){
					if(value==''){
						$(this).combobox('setValue', []);
					}else if(Object.prototype.toString.call(value) == '[object Array]' ){
						if(value == 'YorN'){	
							$(this).combobox('setValues',[]);
				        	var datas = $(this).combobox('getData');
				        	var values = new Array();
				        	for (var i=1;i<datas.length;i++){
								values[i-1] = datas[i].value;
							}
							if($(this).combobox('getValues') == values){
								$(this).combobox('setValue', values);
							}else{
								$(this).combobox('setValue', []);
							}
							$(this).combobox('setValues',values);
						}else if(value.indexOf('YorN') != -1){
							$(this).combobox('setValue', []);
						}
					}
				}
				  try {
					eval('${panelObj.name}_${g.name}_OnSelect()');
				  } catch(e) {}
				},
				
	    	}); 
		</script>   
	</div>
	
