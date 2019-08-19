<#-- 总宽度减去东西减去边框乘以比例减去marginpx-->
	<div class="input-group-inline" >
	    <label style="margin-left :3px;<#if g['required']&&EditMode != 'Readonly'&&g['edit'] >color:red;font-weight:bold;</#if><#if g.height gt 1 >vertical-align:top;</#if>">${g.title}</label> 
	    <#if g['type']=='TextBox'>
	     <input type="text"
 	    	<#if EditMode == 'Readonly' || !g['edit']>readonly="true"</#if>
	    	data-options="prompt:'${g.tip}',
	    	<#-- 设置只读css-->
 	    	<#if EditMode == 'Readonly' || !g['edit']>cls:'readgrey',</#if>
 	    	<#if g.height gt 1 >multiline:true,<#if g['jsFunctionName'] !=''>validType:'${g.jsFunctionName}',</#if></#if>" 
	     id="${panelObj.name}_${g.name}" name="${g.name}" 
	     class="easyui-textbox js-input<#if g['jsFunctionName'] !=''> easyui-validatebox</#if>" style="width:2px;height: ${g.height?number*22}px; text-align: top" </#if>
      	<#if g['type']=='DateBox' || g['type']=='DateTimeBox' || g['type']=='TimeBox'>
	     <input type="text" data-options="buttons:buttons,prompt:'${g.tip}',<#if g['type']=='TimeBox'>showSeconds:true,</#if>
		     onSelect:function(){
				  try {
					eval('${panelObj.name}_${g.name}_OnSelect()');
				  } catch(e) {}
		     }
  	    	<#if EditMode == 'Readonly' || !g['edit']>,cls:'readgrey'</#if>"
 	    	<#if EditMode == 'Readonly' || !g['edit']> readonly="true" </#if>
	    	js-edit="${g['edit']}"
	    	<#if g['type']=='DateBox'> class="easyui-datebox js-input" </#if>
	    	<#if g['type']=='DateTimeBox'>class="easyui-datetimebox js-input" </#if>
	    	<#if g['type']=='TimeBox'> class="easyui-timespinner js-input"</#if>
	     	id="${panelObj.name}_${g.name}" name="${g.name}" 
 	    	<#if g['type']=='TimeBox'> editable="true" <#else>editable="false"</#if>style="width:2px;"
      	</#if>
		<#if g['type']=='PopupBox' >
	    		<script type="text/javascript">
		    	$(function(){
			    $("#${panelObj.name}_${g.name}").next("span").click(function(){
			    if('${g.jsEvent}')
			    	eval('${g.jsEvent}()')
			    });
			    })
			</script>
	    	<input class="easyui-searchbox js-input" 
	    	data-options="prompt:'${g.tip}',<#if EditMode == 'Readonly' || !g['edit']> cls:'readgrey' </#if> "
	    	<#if EditMode == 'Readonly' || !g['edit']> readonly="true" </#if>js-edit="${g['edit']}"
	    	id="${panelObj.name}_${g.name}" name="${g.name}" 
	    	style="width:2px;height: ${g.height?number*22}px;"
    	</#if>
		<#if g['type']=='PasswordBox' >
	    	<input class="easyui-textbox js-input js-password" type="password" id="${panelObj.name}_${g.name}" name="${g.name}"
	    	<#if EditMode == 'Readonly' || !g['edit']> readonly="true"</#if> js-edit="${g['edit']}" 
	    	data-options="prompt:'Password',iconCls:'icon-lock',iconWidth:38,<#if EditMode == 'Readonly' || !g['edit']>cls:'readgrey'</#if>"
	    	 style="width:2px"
			</#if>	  
    	<#-- 如果空间是只读的 不能设置required="true" 不然css会有问题-->
    	<#if g['required']&&EditMode != 'Readonly'&&g['edit'] > required="true" </#if>
    	tipPosition="bottom" width-data="${g.widthPencent}"/>
 	<#if g['jsEventOnEnter'] >  
<script>
/* $('#${panelObj.name}_${g.name}').textbox({
        inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
            keydown: function test(e) {
                if(e.keyCode == 13){
                	${g.jsEvent}();
				}  
            }
        })
    }); */
    
$(function(){
	 $('#${panelObj.name}_${g.name}').textbox('textbox').bind('keypress', function(e) {
	 // console.log("注册事件1111111");  
	  //debugger;    
	  if(e.keyCode==13){          
	  	if('${g.jsEventOnEnter}'){
		    eval('${g.jsEventOnEnter}()')
	  	}
	    //debugger
	    //OnEnter_pCondition_lk_empName_frmEmpList();
	    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
	  }
	 });
	}); 
	
</script>
</#if>

	</div>
		<#if g['type']=='PopupBox' >
    	<script>
			 $("#${panelObj.name}_${g.name}").searchbox({
				onChange: function (a,b)  {
				setTimeout("try {typeof(eval('${panelObj.name}_${g.name}_OnSelect()'))} catch(e) {}",500)
				}
	    	})
    	</script>
    	</#if>
