<!DOCTYPE HTML >
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="no-cache">
<meta http-equiv="expires" content="-1">
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<!-- 动态引用easyui样式 -->
<#if theme??> <#if theme !="default" >
<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui_${theme}.css?${date}"
	rel="stylesheet" media="screen" />
<link href="${ctx}/view/common/css/style_${theme}.css?${date}"
	rel="stylesheet" media="screen" />
<#else>
<!-- 标准 easyui / style 样式 -->
<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui.css?${date}"
	rel="stylesheet" media="screen" />
<link href="${ctx}/view/common/css/style.css?${date}" rel="stylesheet"
	media="screen" />
</#if> <#else>
<!-- 标准 easyui / style 样式 -->
<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui.css?${date}"
	rel="stylesheet" media="screen" />
<link href="${ctx}/view/common/css/style.css?${date}" rel="stylesheet"
	media="screen" />
</#if>
<link href="${ctx}/view/common/css/imageView.css?${date}"
	rel="stylesheet" media="screen" />
<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/icon.css?${date}"
	rel="stylesheet" media="screen" />
<script type="text/javascript">var ctx = "${ctx}";
</script>
<script src="${ctx}/view/common/js/jquery-1.8.3.min.js?${date}"></script>
<script
	src="${ctx}/view/common/css/plugins/easyui-1.5.2/jquery.easyui.min.js?${date}"></script>
<script
	src="${ctx}/view/common/css/plugins/easyui-1.5.2/easyui.plugins.js?${date}"></script>
<script src="${ctx}/view/common/js/Common.js?${date}"></script>
<!-- 引用EasyUI的国际化文件,让它显示中文 -->
<script
	src="${ctx}/view/common/css/plugins/easyui-1.5.2/locale/easyui-lang-zh_CN.js?${date}"
	type="text/javascript"></script>
<script src="${ctx}/view/common/js/base.js?${date}"></script>
<script src="${ctx}/view/common/js/MD5.js?${date}"></script>
<script src="${ctx}/view/common/js/jsExpression.js?${date}"></script>

<!-- js本地存储-->
<script src="${ctx }/view/common/js/store/myStorage.js?${date}"
	charset="utf-8"></script>
<script src="${ctx }/view/common/js/store/json2.js?${date}"
	charset="utf-8"></script>
<script src="${ctx }/view/common/js/store/localDB.js?${date}"
	charset="utf-8"></script>
<!-- ajax请求-->
<script src="${ctx}/view/common/js/ajaxUtil.js?${date}"></script>



<#assign rlist=ui.panelSet> <#list rlist as data> <#assign
clist=ui.controlSet> <#list clist as data1> <#if data1=='KindEditor'>
<!--kindEditor-->
<script
	src="${ctx}/view/common/css/plugins/kindEditor/kindeditor-all.js"></script>
<script charset="utf-8"
	src="${ctx}/view/common/css/plugins/kindEditor/lang/zh-CN.js"></script>
</#if> <#if data1=='ImageView'>
<script src="${ctx}/view/common/js/carousel-v0.7.js?${date}"></script>
</#if> <#if data1=='FileBox'>
<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/jquery.Jcrop.css?${date}"
	rel="stylesheet" media="screen" />
<script
	src="${ctx}/view/common/css/plugins/easyui-1.5.2/plugins/jquery.Jcrop.min.js?${date}"></script>
<script
	src="${ctx}/view/common/css/plugins/easyui-1.5.2/plugins/imgCropUpload.js?${date}"></script>
<script
	src="${ctx}/view/common/css/plugins/easyui-1.5.2/plugins/jquery.filebox.js?${date}"></script>
<script src="${ctx}/view/common/js/ajaxfileupload.js?${date}"></script>
</#if> </#list> <#if data=='Grid'>
<script src="${ctx}/view/common/js/datagrid.js?${date}"></script>
</#if> <#if data=='Group'>
<script src="${ctx}/view/common/js/group.js?${date}"></script>
</#if> <#if data=='Tree'>
<script src="${ctx}/view/common/js/tree.js?${date}"></script>
</#if> <#if
data=='LineChart'||data=='PieChart'||data=='ColumnChart'||data=='BarChart'>
<script src="${ctx}/view/common/js/echarts.common.min.js?${date}"></script>
<script src="${ctx}/view/common/js/chart.js?${date}"></script>
</#if> 
  <#if data=='GanttChart'>
            <script src="${ctx}/view/common/js/echarts.common.min.js?${date}"></script>
		    <script src="${ctx}/view/common/js/ganttchart.js?${date}"></script>
         </#if>
<#if data=='Hidden'>
<script src="${ctx}/view/common/js/hidden.js?${date}"></script>
</#if> </#list>
<script>
	function closes() {
		$("#Loading").fadeOut("normal", function() {
			$(this).remove();
		});
	}

	var pc;

	function loaded() {
		if (pc) clearTimeout(pc);
		pc = setTimeout(closes, 0);
	}
</script>

<script>
	//禁止浏览器后退
	(function(global) {
		if (typeof (global) === "undefined") {
			throw new Error("window is undefined");
		}

		var _hash = "!";
		var noBackPlease = function() {
			global.location.href += "#";

			// making sure we have the fruit available for juice....
			// 50 milliseconds for just once do not cost much (^__^)
			global.setTimeout(function() {
				global.location.href += "!";
			}, 50);
		};

		// Earlier we had setInerval here....
		global.onhashchange = function() {
			if (global.location.hash !== _hash) {
				global.location.hash = _hash;
			}
		};

		global.onload = function() {
			foo();
			noBackPlease();

			// disables backspace on page except on input fields and textarea..
			document.body.onkeydown = function(e) {
				var elm = e.target.nodeName.toLowerCase();
				if (e.which === 8 && (elm !== 'input' && elm !== 'textarea')) {
					e.preventDefault();
				}
				// stopping event bubbling up the DOM tree..
				e.stopPropagation();
			};

		};

	})(window);

	//禁止浏览器后退
	function foo() {
		var items = $('.textbox-text');
		var item = null;
		for (var i = 0; i < items.length; i++) {
			item = items[i];
			(function() {
				var next = (i + 1) < items.length ? i + 1 : 0;
				item.onkeydown = function(event) {
					//debugger  
					var eve = event ? event : window.event;
					if ((eve.keyCode == 13) && (event.ctrlKey)) { //ctrl+ enter 换行
						var oldV = $(items[next - 1]).val();
						$(items[next - 1]).val(oldV + '\n');
					} else if (eve.keyCode == 13) { //enter 回车
						$(items[next]).focus();
					}
				}
			})();
		}
	}
</script>

</head>
<body class="easyui-layout js-ui" title="${ui.title}" id="${ui.name}"
	style="width: 100%; height: 100%; background-color: white;">

	<div id='Loading'
		style="position: absolute; z-index: 1000; top: 0px; left: 0px; width: 100%; height: 100%; background: #FFFFFF; text-align: center; padding-top: 10%;">
		<h1
			style="display: inline-block; border: 1px solid #95b9e7; font-size: 16px; padding: 5px;">
			<image
				src='${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/images/loading.gif' />
			<font color="#15428B" size="2">正在处理，请稍等···</font>
		</h1>
	</div>

	<input type="hidden" id="from" value="${from}">
	<input type="hidden" id="code" value="${code}">
	<input type="hidden" id="uiid" value="${uiid}">
	<input type="hidden" id="puid" value="${puid}"> <#assign
	rlist=ui.layout.regionList> <#assign base=request.contextPath />
	<#assign EditMode=EditMode>
	<style>
    .input-group-inline .textbox-text {
        padding-top: 0px;
        padding-bottom: 10px;
    }
    .input-group-inline textarea {
            font-family: Arial;
        }
    .readgrey{border:1px solid #eaeaea;background:#f9f9f9}
</style>
	<!--begin-->

<div data-options="region:'north'"
     style="">
    <div class="easyui-layout" data-options="fit:true">
                    <div data-options="border:false,region:'north'" style="
                        overflow:hidden;" class="js-panel" trigger="">
<div class="btn-group" style="text-align:right;">
<div style="float: left;line-height: 29px;" class="js-path"></div>
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_btnCreate_frmPcartonResultList()" 
		id="pToolbar_btnCreate" name="btnCreate"
		style="margin-right:10px;"
		>生成</a>
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_btnQuery_frmPcartonResultList()" 
		id="pToolbar_btnQuery" name="btnQuery"
		style="margin-right:10px;"
		>查询</a>
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_btnClear_frmPcartonResultList()" 
		id="pToolbar_btnClear" name="btnClear"
		style="margin-right:10px;"
		>清空</a>
</div>

                    </div>
    </div>
</div>
<!--North end-->
<!--Center begin-->
<div data-options="region:'center'"
     style="">
    <div class="easyui-layout" data-options="fit:true">
                    <div data-options="border:false,region:'north'" style="
                        overflow:hidden;
                                                                        
                        
                            " class="js-panel" trigger="">
<div name="pCondition" id="pCondition" style="padding-top:15px;"
     class="js-group">
	<input type="hidden" name="lk_clCartonId" id="pCondition_lk_clCartonId">
			<div class="input-group-inline" >
	    <label style="margin-left :3px;color:red;font-weight:bold;">产品族选择</label> 

	    		<script type="text/javascript">
		    	$(function(){
			    $("#pCondition_prdFamilyDesc").next("span").click(function(){
			    if('OnClick_pCondition_prdFamilyDesc_frmPcartonResultList')
			    	eval('OnClick_pCondition_prdFamilyDesc_frmPcartonResultList()')
			    });
			    })
			</script>
	    	<input class="easyui-searchbox js-input" 
	    	data-options="prompt:'', "
	    	js-edit="true"
	    	id="pCondition_prdFamilyDesc" name="prdFamilyDesc" 
	    	style="width:2px;height: 22px;"
    	 required="true" 
    	tipPosition="bottom" width-data="0.25"/>
    	<script>
			$(function(){
			 $('#pCondition_prdFamilyDesc').textbox('textbox').bind('keypress', function(e) {
			  //debugger;    
			  if(e.keyCode==13){          
			  	if(''){
				    eval('()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			});
	</script>
    	
	</div>
    	<script>
			 $("#pCondition_prdFamilyDesc").searchbox({
				onChange: function (a,b)  {
				setTimeout("try {typeof(eval('pCondition_prdFamilyDesc_OnSelect()'))} catch(e) {}",500)
				}
	    	})
    	</script>
		<div class="input-group-inline" >
	    <label style="margin-left :3px; color:red;font-weight:bold">产线代码</label>
    	<select class="easyui-combobox js-input"  width-data="0.25" style="width:2px;height:22px" tipPosition="bottom"
    	 required="true"  
    	id="pCondition_lineId" name="lineId" data-options="editable:false, 
    	">
		</select>
    	<script>
	    	$('#pCondition_lineId').combobox({
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
					eval('pCondition_lineId_OnSelect()');
				  } catch(e) {}
				},
				
	    	}); 
		</script>   
	</div>
				<div class="input-group-inline" >
	    <label style="margin-left :3px;color:red;font-weight:bold;">生成数量</label> 
	     <input type="text" data-options="prompt:''," 
	     id="pCondition_count" name="count" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	 required="true" 
    	tipPosition="bottom" width-data="0.25"/>
    	<script>
			$(function(){
			 $('#pCondition_count').textbox('textbox').bind('keypress', function(e) {
			  //debugger;    
			  if(e.keyCode==13){          
			  	if(''){
				    eval('()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			});
	</script>
    	
	</div>
	<br>
		<div class="input-group-inline" >
	    <label style="margin-left :3px;">箱号查找</label> 
	     <input type="text"
	    	data-options="prompt:''," 
	     id="pCondition_lk_pCartonId" name="lk_pCartonId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	tipPosition="bottom" width-data="0.25"/>
    	<script>
			$(function(){
			 $('#pCondition_lk_pCartonId').textbox('textbox').bind('keypress', function(e) {
			  //debugger;    
			  if(e.keyCode==13){          
			  	if(''){
				    eval('()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			});
	</script>
	</div>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">创建日期从</label> 

	     <input type="text" data-options="buttons:buttons,prompt:'',
		     onSelect:function(){
				  try {
					eval('pCondition_modifyStartDate_OnSelect()');
				  } catch(e) {}
		     }
  	    	"
 	    	
	    	js-edit="true"
	    	 class="easyui-datebox js-input" 
	    	
	    	
	     	id="pCondition_modifyStartDate" name="modifyStartDate" 
 	    	editable="false"style="width:2px;"
    	
    	tipPosition="bottom" width-data="0.35"/>
    	<script>
			$(function(){
			 $('#pCondition_modifyStartDate').textbox('textbox').bind('keypress', function(e) {
			  //debugger;    
			  if(e.keyCode==13){          
			  	if(''){
				    eval('()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			});
	</script>
    	
	</div>

			<div class="input-group-inline" >
	    <label style="margin-left :3px;">至</label> 

	     <input type="text" data-options="buttons:buttons,prompt:'',
		     onSelect:function(){
				  try {
					eval('pCondition_modifyEndDate_OnSelect()');
				  } catch(e) {}
		     }
  	    	"
 	    	
	    	js-edit="true"
	    	 class="easyui-datebox js-input" 
	    	
	    	
	     	id="pCondition_modifyEndDate" name="modifyEndDate" 
 	    	editable="false"style="width:2px;"
    	
    	tipPosition="bottom" width-data="0.25"/>
    	<script>
			$(function(){
			 $('#pCondition_modifyEndDate').textbox('textbox').bind('keypress', function(e) {
			  //debugger;    
			  if(e.keyCode==13){          
			  	if(''){
				    eval('()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			});
	</script>
	</div>
<br>
	<input type="hidden" name="maxid" id="pCondition_maxid">
</div>

                    </div>
                <div data-options="border:true,region:'center'" style="
                    
                    
                    
                        ">
                    <div id="tab" class="easyui-tabs" data-options="closable:false,fit:true,plain:true,border:true">
                                <div title="查询结果" class="js-panel" trigger="">
<script>
	function opFormatter_pTable(val,row,index){
		GridButtonContent = "";
		var html = "";
		if(!"id"){
			console.log("grid可能没有配置Object属性或者Object没有主键？");
			var id='';
		} else {
			var id=row['id'];
		}
		
		if(row["ctlDelete"]!=undefined) {
			GridButtonContent = row["ctlDelete"];
			html += "<a class='common' href='javascript:setId(" + index + ",\""+id+"\");OnClick_pTable_ctlDelete_frmPcartonResultList();'><u>";
			html += GridButtonContent + "</u></a>    ";
		} 
		if(row["ctlPrint"]!=undefined) {
			GridButtonContent = row["ctlPrint"];
			html += "<a class='common' href='javascript:setId(" + index + ",\""+id+"\");OnClick_pTable_ctlPrint_frmPcartonResultList();'><u>";
			html += GridButtonContent + "</u></a>    ";
		} 
		return html;
	}
	//用于拼接操作列链接时简化调用 。否则容易引起运行时刻的easyui解析错误（多空格等）造成的语句无法成功执行
	function setId(index,id){
		setStorage('EditIndex', index);
		setPanelId("pTable", id);
	}
	
	
		var editIndex_pTable = undefined;
		function endEditing_pTable(dg){
			if (editIndex_pTable == undefined){return true}
			if (dg.datagrid('validateRow', editIndex_pTable)){
					dg.datagrid('endEdit', editIndex_pTable);
				editIndex_pTable = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickRow_pTable(index, dg){
			//if (editIndex_pTable != index){
				if (endEditing_pTable(dg)){
					dg.datagrid('selectRow', index);
							dg.datagrid('beginEdit', index);
					editIndex_pTable = index;
				//} else {
					//dg.datagrid('selectRow', editIndex_pTable);
				//}
			}
		}
</script>
 

 <table  class="easyui-datagrid"  
				name="pTable" id="pTable" 
				data-options="
				
				
				onClickRow: function (index, row) {
					$('#pTable').datagrid('uncheckAll');
					$('#pTable').datagrid('checkRow', index);
					
					
				},
				fit:true,
				rownumbers: true,
				singleSelect:true,
				selectOnCheck:false,checkOnSelect:true,
				pagination:true,
				pageSize:50,
				pageList:[50,100,200,400],
				nowrap:false,
				view:myview,
				sortName:'id',
				sortOrder:'desc'"> 
						<thead>
			<tr>
				<th data-options="field:'id',sortable:true,width:0,halign:'center',hidden:true">id</th>
				<th data-options="fitColumns:true,field:'view',halign:'center',width:70,align:'left',formatter:opFormatter_pTable">操作</th>
						<th data-options="field:'clCartonId',sortable:true,width:250,halign:'center'
						,align:'left'
						" 
					>包装箱号</th>
						<th data-options="field:'gmtCreate',sortable:true,width:200,halign:'center'
						,align:'center'
						" 
					>创建日期</th>
						<th data-options="field:'gmtUpdate',sortable:true,width:200,halign:'center'
						,align:'center'
						" 
					>修改日期</th>
			</tr>
		</thead>
</table> 
<div id="tb_pTable" style="padding:5px;height:auto">
	<div style="margin-bottom:0px">
	</div>
</div>


                                </div>
                                <div title="生成结果" class="js-panel" trigger="">
<script>
	function opFormatter_pCartonTable(val,row,index){
		GridButtonContent = "";
		var html = "";
		if(!"id"){
			console.log("grid可能没有配置Object属性或者Object没有主键？");
			var id='';
		} else {
			var id=row['id'];
		}
		
		if(row["ctlCartonPrint"]!=undefined) {
			GridButtonContent = row["ctlCartonPrint"];
			html += "<a class='common' href='javascript:setId(" + index + ",\""+id+"\");OnClick_pCartonTable_ctlCartonPrint_frmPcartonResultList();'><u>";
			html += GridButtonContent + "</u></a>    ";
		} 
		return html;
	}
	//用于拼接操作列链接时简化调用 。否则容易引起运行时刻的easyui解析错误（多空格等）造成的语句无法成功执行
	function setId(index,id){
		setStorage('EditIndex', index);
		setPanelId("pCartonTable", id);
	}
	
	
		var editIndex_pCartonTable = undefined;
		function endEditing_pCartonTable(dg){
			if (editIndex_pCartonTable == undefined){return true}
			if (dg.datagrid('validateRow', editIndex_pCartonTable)){
					dg.datagrid('endEdit', editIndex_pCartonTable);
				editIndex_pCartonTable = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickRow_pCartonTable(index, dg){
			//if (editIndex_pCartonTable != index){
				if (endEditing_pCartonTable(dg)){
					dg.datagrid('selectRow', index);
							dg.datagrid('beginEdit', index);
					editIndex_pCartonTable = index;
				//} else {
					//dg.datagrid('selectRow', editIndex_pCartonTable);
				//}
			}
		}
</script>
 

 <table  class="easyui-datagrid"  
				name="pCartonTable" id="pCartonTable" 
				data-options="
				
				
				onClickRow: function (index, row) {
					$('#pCartonTable').datagrid('uncheckAll');
					$('#pCartonTable').datagrid('checkRow', index);
					
					
				},
				fit:true,
				rownumbers: true,
				singleSelect:true,
				selectOnCheck:false,checkOnSelect:true,
				pagination:true,
				pageSize:50,
				pageList:[50,100,200,400],
				nowrap:false,
				view:myview,
				sortName:'id',
				sortOrder:'desc'"> 
						<thead>
			<tr>
				<th data-options="fitColumns:true,field:'view',halign:'center',width:80,align:'left',formatter:opFormatter_pCartonTable">操作</th>
						<th data-options="field:'clCartonId',sortable:true,width:0,halign:'center'
						,align:'left'
						" 
					>包装箱号</th>
			</tr>
		</thead>
</table> 
<div id="tb_pCartonTable" style="padding:5px;height:auto">
	<div style="margin-bottom:0px">
	</div>
</div>


                                </div>
                    </div>
                </div>
    </div>
</div>




		
	<!--end-->












	<#list hiddenPanel?keys as key> <#assign panelObj=hiddenPanel[key]>
	<#include "panelInclude.ftl"> </#list> <#list hiddenTable?keys as key>
	<#assign panelObj=hiddenTable[key]>
	<div style="display: none"><#include "datagrid/datagrid.ftl"></div>
	</#list>
</body>
<script>
    var frmDlg = parent.document.getElementById('frmDlg');
    if (frmDlg && '${resizeHeight}' == 'true') {
        var height = 0;
        $(frmDlg.contentWindow.document.body).find(".js-panel").each(function () {
            height = height + $(this).height();
        })
        parent.$("#tmpDlg").height(height);
        parent.$(".window-shadow").remove();
    }
    var clientContext = ${clientContext};
    //auth control
    $(document).ready(function () {
    ${initControl}
       // cus_resize_init_input();
        setReadonly();
    ${validatorRules}
        changeTab();
        // 记录路径 如果从菜单 快捷菜单 或者首页跳转记录路径
        if(!'${type}'){
	        var path='${from}'+$("#code").val();
	        var arr = new Array();
	        var arrTitle = new Array();
			arr.push($(".js-ui").attr("id"));
			arrTitle.push($(".js-ui").attr("title"));
			setStorage(path, arr);
			setStorage(path+"_name", arrTitle);
        }
        if('${type}'!='Popup'){
	        var path='${from}'+$("#code").val();
	        var text='<img src="${ctx}/view/common/images/hrefLogo.jpg" height="13" width="13" style="float:left;margin-top:8px" > </img>';
			for (var i=0;i<getStorage(path+"_name").length;i++){
				if(i==getStorage(path+"_name").length-1){
				  text+='<a>'+getStorage(path+"_name")[i]+'</a>';
				} else {
				  text+='<a>'+getStorage(path+"_name")[i]+'</a> &gt;';
				}
		  	}
	        $(".js-path").html(text);
        }
        
    });

    function changeTab(){

        if($('#tab').tabs(0).find("[class$='Chart']")) {
            $('#tab').tabs(0).find("[class$='Chart']").each(function(i,obj){
                var c = eval($(this).attr("id"));
                c.resize();
            });
        }
        $('#tab').tabs({
            onSelect: function (title, index) {
                $("[class$='Chart']").each(function(i,obj){
                   var chart = eval($(this).attr("id"));
                    chart.resize();
                });
            }

        });
    }

    function updateClientContext(c) {
        if (c == undefined) {
            return;
        }
        if(c.items){
       	 	clientContext = c;
        }else if(c.token && c.functionId){
        	clientContext.items[c.functionId] = c.token;
        	console.log("已更新"+c.functionId+"的token值");
        }
        if (c.filterStatement) {
            eval(c.filterStatement);
        }
    }

    //onLoad事件中调用方法时不加token
    function getToken(key) {
        var token = clientContext.items[key];
        if (token == undefined) {
        	token = "";
		}else{
        	delete clientContext.items[key];
        }
        return token;
    }

	//validateException...
    function restoreToken(key,value) {
        clientContext.items[key] = value;
    }
    
    //初始化生成input宽度
    function cus_resize_init_input() {
        $('.js-panel').each(function () {
        if($(this).children().eq(0).attr('class')=='js-group'){
            var this_width = $(this).width() - 20;
            $(this).find(".js-input").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data")) - 121;
                    $(n).textbox('resize',new_width);
            });
            $(this).find(".js-label").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data")) - 121;
                    $(n).width(new_width);
            });
        }
        });
    }

    // 初始化只读状态
    function setReadonly() {
        $('.js-panel').each(function () {
            $(this).find(".js-input").each(function (i, n) {
                if ($(this).attr("readonly") != "readonly") {
                	$(this).textbox('textbox').css('background','#fff');
                } else {
                	$(this).parent().find(".searchbox-button").remove();
                    $(this).parent().find(".combo-arrow").remove();
                    $(this).parent().find(".textbox-addon").remove();
                    $(this).next("span").unbind("click");
                }
                if ($(this).attr("class").indexOf("easyui-searchbox") > -1) {
                    var box = $(this).searchbox('textbox');//获取控件文本框对象
                    box.attr('readonly', true);//禁用输入
                }
            });
        });
    }
    ${js}

    var uiStorage = {};
    var panelProperty = ${panelPropery};//界面绘制时，计算每个panel对应的主键controlName
    //每个panel实现唯一标识值的setter getter
    function setPanelId(panelName, idVal) {
        setUiStorage(panelName + '_EditId', idVal);
    }

    function getPanelId(panelName) {
        var primaryControl = panelProperty[panelName];
        if (primaryControl) {
            return getPanelControlValue(panelName + "." + primaryControl,true);
        }
        return null;
    }
     function getEditMode(){
      return getUIMode('${ui.name}');
     }
    
</script>
</html>
