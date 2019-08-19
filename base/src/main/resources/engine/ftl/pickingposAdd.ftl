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
						//$(items[next]).focus();
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
			border: none;
  			border-bottom: #95B8E7 1px solid;
		}
		
		.input-group-inline textarea {
			font-family: Arial;
		}
		
		.readgrey {
			/* border: 1px solid #eaeaea; */
			background: #f9f9f9
		}
		
		#pForm_item + span {
			width: 100% !important;
			height: 100% !important;
		}

		.datagrid-empty {
		    top: 87px !important;
		}

		#pForm1_kanbanId + span, #pForm1_serialno + span {
			width: 235px !important;
		}

		#pForm1_kanbanId + span input, #pForm1_serialno + span input {
			width: 235px !important;
		}
		
		.datagrid-header-rownumber {
    		width: 7px !important;
		}

		#pAmount_getAmount + span .textbox-text{
			text-align:left !important;
			font-size:31px !important;
			margin-left:4px !important;
		}
	</style>
	<!--begin-->

	<div data-options="region:'north'" data-options="fit:true" style="height: 250px">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="border:false,region:'center'" class="js-panel"
				trigger="" id="pForm1Wrap">
				<div name="pForm1" id="pForm1" class="js-group">
					<div class="input-group-inline"
						style="width: 32%; text-align: right">
						<label style="color: red;">产品订单号</label>
							     <input type="text"
 	    	
	    	data-options="prompt:'',events:{onkeypress:function(){setTimeout('eval(\'OnEnter_pForm1_ppId_pickingposAdd()\')',50);}},
 	    	
 	    	" 
	     id="pForm1_ppId" name="ppId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	 required="true" 
    	tipPosition="bottom" width-data="0.333" value="1234567"/>
    	<script>
			$(function(){
			 $('#pForm1_ppId').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
			  //debugger;    
			  if(e.keyCode==13){          
			  	if('OnEnter_pForm1_ppId_pickingposAdd'){
			  	console.log("aaaaa");
				    eval('OnEnter_pForm1_ppId_pickingposAdd()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    //$(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			}); 
	</script>
    	
	</div>
					<div class="input-group-inline"
						style="width: 32%; text-align: right">
						<label style="color: red;">设备（工位）</label> <input type="text"
 	    	
	    	data-options="prompt:'',
 	    	
 	    	" 
	     id="pForm1_equStationID" name="equStationID" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	 required="true" 
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_equStationID').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
			  //debugger;    
			  if(e.keyCode==13){          
			  	if('OnEnter_pForm1_equStationID_pickingposAdd'){
				    eval('OnEnter_pForm1_equStationID_pickingposAdd()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			}); 
	</script>
    	
	</div>
					<div class="input-group-inline"
						style="width: 32%; text-align: right">
						<label>产品描述</label>  <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pForm1_productDesc" name="productDesc" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_productDesc').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
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
					<!-- <div class="input-group-inline"
						style="width: 32%; text-align: right">
						<label>订单描述</label> <input type="text" data-options="prompt:'',
"
							id="pForm1_ppDesc" name="ppDesc"
							class="easyui-textbox js-input
		 "
							style=""
							tipPosition="bottom" width-data="0.333" />
					</div> -->
						<div class="input-group-inline" style="width: 32%; text-align: right">
					    <label style="margin-left :3px;">旧物料号</label> 
					    <input type="text"
 	    	
	    	data-options="prompt:'',
 	    	
 	    	" 
	     id="pForm1_oldmaterialId" name="oldmaterialId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_oldmaterialId').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
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
	
					<div class="input-group-inline" style="display: none;">
						<label style="margin-left: 3px;">订单类型</label> <input
							type="text" data-options="prompt:''," id="pForm1_ppType"
							name="ppType" class="easyui-textbox js-input"
							style="height: 22px; text-align: top" tipPosition="bottom"
							width-data="0.25" />
					</div>

					<!-- <div class="input-group-inline"
						style="width: 32%; text-align: right">
						<label>当前工序</label> <input type="text" readonly="true"
							data-options="prompt:'',
	    	 cls:'readgrey',
"
							id="pForm1_nowProcess" name="nowProcess"
							class="easyui-textbox js-input
		 "
							style=""
							tipPosition="bottom" width-data="0.333" />
					</div> -->
					    <input type="hidden" name="nowProcess" id="pForm1_nowProcess">
						     <div class="input-group-inline"
						      style="width: 32%; text-align: right">
						      <label>当前工序</label>  <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pForm1_nowProcess2" name="nowProcess2" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_nowProcess2').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
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
     
					<div class="input-group-inline"
						style="width: 32%; text-align: right">
						<label>产品族</label> <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pForm1_proFamily" name="proFamily" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_proFamily').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
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
					<!-- <hr style="width:94%;display: block;margin: 0 auto;"> -->
					<div id="tb_pTable" style="padding: 0px; height: auto">
						<div style="margin-bottom: 5px"></div>
					</div>
					
					<input type="hidden" name="productId" id="pForm1_productId">
					<div class="input-group-inline"
						style="width: 45%; text-align: right;">
						<label style="color: red;">看板号</label>  <input type="text"
 	    	
	    	data-options="prompt:'',
 	    	
 	    	" 
	     id="pForm1_kanbanId" name="kanbanId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_kanbanId').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
			  //debugger;    
			  if(e.keyCode==13){          
			  	if('OnEnter_pForm1_kanbanId_pickingposAdd'){
				    eval('OnEnter_pForm1_kanbanId_pickingposAdd()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			}); 
	</script>
    	
	</div>
					<div class="input-group-inline"
						style="width: 45%; text-align: right;">
						<label style="color: red;">产品序列号</label>  <input type="text"
 	    	
	    	data-options="prompt:'',
 	    	
 	    	" 
	     id="pForm1_serialno" name="serialno" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	 required="true" 
    	tipPosition="bottom" width-data="0.333"/>
    	<script>
			$(function(){
			 $('#pForm1_serialno').textbox('textbox').bind('keypress', function(e) {
			  console.log("注册事件1111111");  
			  //debugger;    
			  if(e.keyCode==13){          
			  	if('OnEnter_pForm1_serialno_pickingposAdd'){
				    eval('OnEnter_pForm1_serialno_pickingposAdd()')
			  	}
			    //debugger
			    //OnEnter_pCondition_lk_empName_frmEmpList();
			    $(this).parents('.input-group-inline').next().find('.textbox-text').focus(); 
			  }
			 });
			}); 
	</script>
    	
	</div>
					<br> <input type="hidden" name="pbomId" id="pForm1_pbomId">
					<input type="hidden" name="ctlDel" id="pForm1_ctlDel">
					<input type="hidden" name="equStationID1" id="pForm1_equStationID1">
					<input type="hidden" name="isColle" id="pForm1_isColle">
					
				</div>

			</div>
			<div data-options="border:false,region:'north'"
				style="overflow: hidden;" class="js-panel" trigger="">
				<div class="btn-group" style="text-align: right;">
					<div style="float: left; line-height: 29px;" class="js-path"></div>
					<!--OnClick="OnClick_pToolbar_btnDetail_pickingposAdd()"  -->

					<a class="easyui-linkbutton" href="javascript:void(0)"
						id="pToolbar_btnDetail" name="btnDetail"
						style="margin-right: 10px;">领料详情信息</a>
					<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_saveLocalStorage_pickingposAdd()" 
		id="pToolbar_saveLocalStorage" name="saveLocalStorage"
		style="margin-right:10px;">暂存</a>
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_btnChange_pickingposAdd()" 
		id="pToolbar_btnChange" name="btnChange"
		style="margin-right:10px;">换班</a>
					
					
				</div>

			</div>
		</div>
	</div>


	<div data-options="region:'center'">
		<div class="easyui-layout" data-options="fit:true">
			 <div data-options="border:false,region:'west'" style="
                        overflow:hidden;width:;border-right:1px solid #95B8E7;" 
                        class="js-panel" trigger="">
				<script>
					function opFormatter_pTable(val, row, index) {
						GridButtonContent = "";
						var html = "";
						if (!"id") {
							console.log("grid可能没有配置Object属性或者Object没有主键？");
							var id = '';
						} else {
							var id = row['id'];
						}
				
						if (row["ctlDel"] != undefined) {
							GridButtonContent = row["ctlDel"];
							html += "<a class='common' href='javascript:setId(" + index + ",\"" + id + "\");OnClick_pTable_ctlDel_pickingposAdd();'><u>";
							html += GridButtonContent + "</u></a>    ";
						}
						return html;
					}
					//用于拼接操作列链接时简化调用 。否则容易引起运行时刻的easyui解析错误（多空格等）造成的语句无法成功执行
					function setId(index, id) {
						setStorage('EditIndex', index);
						setPanelId("pTable", id);
					}
				
				
					var editIndex_pTable = undefined;
					function endEditing_pTable(dg) {
						if (editIndex_pTable == undefined) {
							return true
						}
						if (dg.datagrid('validateRow', editIndex_pTable)) {
							dg.datagrid('endEdit', editIndex_pTable);
							editIndex_pTable = undefined;
							return true;
						} else {
							return false;
						}
					}
					function onClickRow_pTable(index, dg) {
						//if (editIndex_pTable != index){
						if (endEditing_pTable(dg)) {
							dg.datagrid('selectRow', index);
							dg.datagrid('beginEdit', index);
							editIndex_pTable = index;
						//} else {
						//dg.datagrid('selectRow', editIndex_pTable);
						//}
						}
					}
				</script>
				<table class="easyui-datagrid" name="pTable" id="pTable"
					data-options="
				toolbar:'#tb_pTable',
				onClickRow: function (index, row) {
					$('#pTable').datagrid('uncheckAll');
					$('#pTable').datagrid('checkRow', index);
				},
				fit:true,
				rownumbers: true,
				singleSelect:true,
				selectOnCheck:false,checkOnSelect:true,
				pagination:false,
				nowrap:false,
				view:myview,
				sortName:'',
				sortOrder:''">
					<thead>
						<tr>
							<th
								data-options="fitColumns:true,field:'view',halign:'center',width:0,align:'left',formatter:opFormatter_pTable">操作</th>
							<th
								data-options="field:'kanbanId',sortable:true,width:295,halign:'center'
						,align:'left'
						">看板号</th>
							<th
								data-options="field:'materialId',sortable:true,width:180,halign:'center'
						,align:'left'
						">物料号</th>
							<th
								data-options="field:'materialDesc',sortable:true,width:0,halign:'center'
						,align:'left'
						">物料描述</th>
							<th
								data-options="field:'piLot',sortable:true,width:0,halign:'center'
						,align:'left'
						">批次</th>
							<th
								data-options="field:'shengyuqty',sortable:true,width:0,halign:'center'
						,align:'center'
						">剩余数</th>
							<th
								data-options="field:'baseQty',sortable:true,width:0,halign:'center',hidden:true">子/基本</th>
							<th
								data-options="field:'serialno',sortable:true,width:0,halign:'center',hidden:true">产品序列号</th>
							<th
								data-options="field:'parentMaterialId',sortable:true,width:0,halign:'center',hidden:true">父物料id</th>
						</tr>
					</thead>
				</table>
</div>
<div data-options="border:false,region:'east'" style="
                        overflow:hidden;
                        width:8%;border-left:1px solid #95B8E7;" class="js-panel" trigger="">
<div name="pAmount" id="pAmount" style="padding-top:15px;"
     class="js-group">
			<div class="input-group-inline" >
	    <label style="margin-left :1px;vertical-align:top;">产量</label> 
	     <input type="text"
	    	data-options="prompt:''," 
	     id="pAmount_getAmount" name="getAmount" 
	     class="easyui-textbox js-input" style="width:2px;height: 66px; text-align: top" 
    	
    	tipPosition="bottom" width-data="1"/>

<script>
/* $('#pAmount_getAmount').textbox({
        inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
            keydown: function test(e) {
                if(e.keyCode == 13){
                	();
				}  
            }
        })
    }); */
    
$(function(){
	 $('#pAmount_getAmount').textbox('textbox').bind('keypress', function(e) {
	 // console.log("注册事件1111111");  
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

</div>

                    </div>

			 <div data-options="border:false,region:'south'"
				style="overflow: hidden;" class="js-panel" trigger="">
				<div name="pForm" id="pForm" style="height: 140px;padding-top: 10px;"
					class="js-group">
					<input type="hidden" name="ctlDel" id="pForm_ctlDel">
					<div class="info">
						<input type="text" readonly="true"
							data-options="prompt:'',cls:'readgrey',	multiline:true,"
							id="pForm_item" name="item" class="easyui-textbox js-input"
							style="margin: 0px; height: 140px; width: 1000px; text-align: top"
							tipPosition="bottom" width-data="0.667" />
					</div>
					<br> <input type="hidden" name="serialno" id="pForm_serialno">
					<input type="hidden" name="item2" id="pForm_item2"> 
					<input type="hidden" name="item3" id="pForm_item3"> <input
						type="hidden" name="id" id="pForm_id"> <input
						type="hidden" name="materialId" id="pForm_materialId"> <input
						type="hidden" name="materialDesc" id="pForm_materialDesc">
					<input type="hidden" name="kanbanId" id="pForm_kanbanId"> <input
						type="hidden" name="baseQty" id="pForm_baseQty"> <input
						type="hidden" name="parentMaterialId" id="pForm_parentMaterialId">
					<input type="hidden" name="piLot" id="pForm_piLot">

				</div>

			</div> 
			
			<div data-options="region:'south'" style="height: 85px">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="border:false,region:'center'" style=""
				class="js-panel" trigger="">
	<!--			<div name="pCondition" id="pCondition" class="js-group">
					<div class="info" style="border: none; padding: 15px; height: 55px; line-height: 24px; box-sizing: border-box;">
						<!-- <label style="margin-left: -60px;"></label> -->
						<input type="text" readonly="true"
							data-options="prompt:'',cls:'readgrey',multiline:true,"
							id="pCondition_item" name="item"
							class="easyui-textbox js-input"
							style="margin: 0px; height: 80px; width: 1000px; text-align: top"
							tipPosition="bottom"
							width-data="1" />
					</div> 
					
					<div name="pForm2" id="pForm2" style="padding-top:15px;"
     class="js-group">
	<input type="hidden" name="serialno" id="pForm2_serialno">
	<input type="hidden" name="ppId" id="pForm2_ppId">
	<input type="hidden" name="productId" id="pForm2_productId">
</div>
					
					
					<br> <input type="hidden" name="wipSerialno"
						id="pCondition_wipSerialno">
				</div>

			</div>
		</div>
	</div>
	
		</div>
	</div>
	<div data-options="region:'south'" style="">
		<div class="easyui-layout" data-options="fit:true"></div>
	</div>


<!--	<div name="pCondition" id="pCondition" style="display:none" class="js-group">
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">取消</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_ctlDel" name="ctlDel" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>

			<div class="input-group-inline" >
	    <label style="margin-left :3px;">看板号</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_kanbanId" name="kanbanId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>

			<div class="input-group-inline" >
	    <label style="margin-left :3px;vertical-align:top;">物料号</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	multiline:true," 
	     id="pCondition_materialId" name="materialId" 
	     class="easyui-textbox js-input" style="width:2px;height: 44px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">物料描述</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_materialDesc" name="materialDesc" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">剩余数量</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_shengyuqty" name="shengyuqty" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>

			<div class="input-group-inline" >
	    <label style="margin-left :3px;">子/基本</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_baseQty" name="baseQty" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>

			<div class="input-group-inline" >
	    <label style="margin-left :3px;">判断</label> 
	     <input type="text"
 	    	
	    	data-options="prompt:'',
 	    	
 	    	" 
	     id="pCondition.YorN" name="YorN" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">工厂ID</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_plantId" name="plantId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">产品序列号</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_serialno" name="serialno" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">父级物料代码</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_parentMaterialId" name="parentMaterialId" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
			<div class="input-group-inline" >
	    <label style="margin-left :3px;">批次</label> 
	     <input type="text"
 	    	readonly="true"
	    	data-options="prompt:'',
 	    	cls:'readgrey',
 	    	" 
	     id="pCondition_piLot" name="piLot" 
	     class="easyui-textbox js-input" style="width:2px;height: 22px; text-align: top" 
    	
    	tipPosition="bottom" width-data="0"/>




	</div>
<br>
</div> -->



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
    
    //pToolbar.btnChangeOnClick开始
function OnClick_pToolbar_btnChange_pickingposAdd(){
//Branch判断逻辑
var jsclear=function (){
	console.log('Branch 逻辑开始');
	debugger;
	if(eval("$('#pAmount_getAmount').textbox('setValue','0');")){
		clearLocalStorage();
	}else{
		
	}
 }

//Branch判断逻辑
var clearLocalStorage=function (){
	console.log('Branch 逻辑开始');
	debugger;
	if(eval("clearStorage('pAmount','pickingposAdd')")){
		
	}else{
		
	}
 }

jsclear();
}
//pToolbar.btnChangeOnClick结束

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
