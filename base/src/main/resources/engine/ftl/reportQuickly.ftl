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
<!-- 根据不同显示器的分辨率 动态引用css样式 -->
<#if screenWidth !='' >
<%-- <link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui_${theme}_${screenWidth }.css?${date}"
			rel="stylesheet" media="screen" />
		<link href="${ctx}/view/common/css/style_${theme}_${screenWidth }.css?${date}"
			rel="stylesheet" media="screen" /> --%>

<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui_${theme}.css?${date}"
	rel="stylesheet" media="screen" />
<link href="${ctx}/view/common/css/style_${theme}.css?${date}"
	rel="stylesheet" media="screen" />

</#if> <#else>
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

<script>
var scre = screen.width;
function loadjscssfile(filename,filetype,scre){
	   if(filetype == "js"){
	      var fileref = document.createElement('script');
	      fileref.setAttribute("type","text/javascript");
	      fileref.setAttribute("src",filename);
	   }else if(filetype == "css"){
	      var fileref = document.createElement('link');
	      fileref.setAttribute("rel","stylesheet");
	      fileref.setAttribute("type","text/css");
	      fileref.setAttribute("href",filename);
	   }
	   if(typeof fileref != "undefined"){
	      document.getElementsByTagName("head")[0].appendChild(fileref);
	   }
	}
</script>

<link href="${ctx}/view/common/css/imageView.css?${date}"
	rel="stylesheet" media="screen" />
<link
	href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/icon.css?${date}"
	rel="stylesheet" media="screen" />
<script type="text/javascript">var ctx = "${ctx}";</script>
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
</#if> <#if data=='GanttChart'>
<script src="${ctx}/view/common/js/echarts.common.min.js?${date}"></script>
<script src="${ctx}/view/common/js/ganttchart.js?${date}"></script>
</#if> <#if data=='Hidden'>
<script src="${ctx}/view/common/js/hidden.js?${date}"></script>
</#if> </#list>
<script>
        function closes() {
            $("#Loading").fadeOut("normal", function () {
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
(function (global) {
	if(typeof (global) === "undefined")
	{
		throw new Error("window is undefined");
	}

    var _hash = "!";
    var noBackPlease = function () {
        global.location.href += "#";

		// making sure we have the fruit available for juice....
		// 50 milliseconds for just once do not cost much (^__^)
        global.setTimeout(function () {
            global.location.href += "!";
        }, 50);
    };
	
	// Earlier we had setInerval here....
    global.onhashchange = function () {
        if (global.location.hash !== _hash) {
            global.location.hash = _hash;
        }
    };

    global.onload = function () {
    	/* //分辨率 动态掉用css样式
    	var scre1 = screen.width;
    	console.log("当前分辨率是："+scre1);
		loadjscssfile("${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui_${theme}_"+scre1+".css?${date}","css",scre1);
		loadjscssfile("${ctx}/view/common/css/style_${theme}_"+scre1+".css?${date}","css",scre1); */
		
    	foo();
		noBackPlease();
		
		// disables backspace on page except on input fields and textarea..
		document.body.onkeydown = function (e) {
            var elm = e.target.nodeName.toLowerCase();
            if (e.which === 8 && (elm !== 'input' && elm  !== 'textarea')) {
                e.preventDefault();
            }
            // stopping event bubbling up the DOM tree..
            e.stopPropagation();
        };
		
    };

})(window);

    //禁止浏览器后退
    function foo(){  
    var items=$('.textbox-text');  
    var item=null;  
    for(var i=0;i<items.length;i++){  
         item=items[i];  
        (function () {
           var next=(i+1) < items.length ? i+1 : 0 ;  
           item.onkeydown=function(event){
             //debugger  
             var eve=event ? event : window.event;
             if((eve.keyCode==13) && (event.ctrlKey)){  //ctrl+ enter 换行
                   var oldV = $(items[next-1]).val();
                   $(items[next-1]).val(oldV +'\n');
             }else if(eve.keyCode==13){  //enter 回车
             		//$(items[next]).focus(); 
             } 
         }  
         })();  
    }     
}  
</script>
</head>
<body class="easyui-layout js-ui" title="${ui.title}" id="${ui.name}"
	style="width: 100%; height: 100%; background-color: white; font-family:"
	Helvetica Neue", Helvetica, Arial, "PingFang SC", "Hiragino Sans
	GB", "Heiti SC", "MicrosoftYaHei", "WenQuanYiMicroHei", sans-serif;">

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
.input-group-inline {
	display: inline-block;
	padding: 3px 0px;
}

.input-group-inline span {
	border: none;
	border-bottom: #95B8E7 1px solid !important;
}

.readgrey {
	border: 1px solid #eaeaea;
	background: #f9f9f9
}


.input-group-inline label {
	width: 180px;
}

.input-group-inline .textbox .textbox-text, .textbox,
	.validatebox-invalid, .textbox-invalid {
	width: 200px !important;
}

#pForm1_QRCode+span, #pForm1_QRCode+span .textbox-text {
	width: 550px !important;
}

.info .textbox {
	width: 550px !important;
}

.js-group .info span.textbox-readonly, .js-group .info span.textbox {
	width: 100% !important;
	border: none;
}

.js-group .info .textbox-readonly textarea {
	width: 100% !important;
	height: 100px !important;
	resize: none;
	padding: 0;
}

 #pForm_item + span{	
		width: 100% !important;
		height: 90%!important;
	
	}

	#_easyui_textbox_input9{
	width: 100%!important;
	height: 90%!important;
	} 	

.body-scroll {
	overflow-x: hidden;
	overflow-y: auto;
	color: #000;
	font-size: .7rem;
	font-family: "\5FAE\8F6F\96C5\9ED1", Helvetica, "黑体", Arial, Tahoma;
	height: 100%;
}

.body-scroll::-webkit-scrollbar { /*滚动条整体样式*/
	width: 8px; /*高宽分别对应横竖滚动条的尺寸*/
	height: 8px;
}

.body-scroll::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
	border-radius: 5px;
	-webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
	background: #fafafa;
}

.body-scroll::-webkit-scrollbar-track { /*滚动条里面轨道*/
	-webkit-box-shadow: inset 0 0 5px #00ABE1;
	border-radius: 0;
	background: #00ABE1;
}
</style>


	<!--begin-->
	<!--North begin-->
	<div data-options="region:'north'" style="">
		<div class="easyui-layout" data-options="fit:true"></div>
	</div>
	<!--North end-->
	<!--Center begin-->
	<div data-options="region:'center'" style="">
		<div class="easyui-layout" data-options="fit:true">

			<div data-options="border:false,region:'north'"
				style="overflow: hidden;height:70px;" class="js-panel" trigger="">
				<div name="pForm1" id="pForm1" style="padding-top: 1px;"
					class="js-group">
					<div class="input-group-inline">
						<label style="margin-left: 3px; color: red; font-weight: bold;">内箱二维码</label>
						<input type="text" data-options="prompt:'',
 	    	
 	    	"
							id="pForm1_QRCode" name="QRCode"
							class="easyui-textbox js-input textbox11"
							style="width: 2px; height: 22px; text-align: top" required="true"
							tipPosition="bottom" width-data="1" />
						<script>
/* $('#pForm1_QRCode').textbox({
        inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
            keydown: function test(e) {
                if(e.keyCode == 13){
                	OnEnter_pForm1_QRCode_reportQuickly();
				}  
            }
        })
    }); */
    
$(function(){
	 $('#pForm1_QRCode').textbox('textbox').bind('keypress', function(e) {
	 // console.log("注册事件1111111");  
	  //debugger;    
	  if(e.keyCode==13){          
	  	if('OnEnter_pForm1_QRCode_reportQuickly'){
		    eval('OnEnter_pForm1_QRCode_reportQuickly()')
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
<!-- <div data-options="region:'west'"
     style="width:55%;">
    <div class="easyui-layout" data-options="fit:true"> -->
                    <div data-options="border:false,region:'north'" style="
                        overflow:hidden;
                            " class="js-panel" trigger="">
				<div name="pTable" id="pTable" style="padding-top: 15px;"
					class="js-group">
					<div class="input-group-inline">
						<label style="margin-left: 3px;">箱号</label> <input type="text"
							readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_cartonId" name="cartonId"
							class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />

					</div>

					<div class="input-group-inline">
						<label style="margin-left: 3px;">本箱数量</label> <input type="text"
							readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_thisNum" name="thisNum"
							class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />

					</div>
					<br>
					<div class="input-group-inline">
						<label style="margin-left: 3px;">订单号</label> <input type="text"
							readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_ppId" name="ppId" class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />

					</div>

					<div class="input-group-inline">
						<label style="margin-left: 3px;">订单已报工数量</label> <input
							type="text" readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_ordAlreadyNum" name="ordAlreadyNum"
							class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />

					</div>
					<br>
					<div class="input-group-inline">
						<label style="margin-left: 3px;">物料号</label> <input type="text"
							readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_materialId" name="materialId"
							class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />

					</div>

					<div class="input-group-inline">
						<label style="margin-left: 3px;">订单剩余报工数量</label> <input
							type="text" readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_ordShengyu" name="ordShengyu"
							class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />

					</div>
					<br>
					<div class="input-group-inline">
						<label style="margin-left: 3px;">物料描述</label> <input type="text"
							readonly="true"
							data-options="prompt:'',cls:'readgrey',"
							id="pTable_materialDesc" name="materialDesc"
							class="easyui-textbox js-input"
							style="width: 2px; height: 22px; text-align: top"
							tipPosition="bottom" width-data="0.5" />
					</div>
					<br> <input type="hidden" name="qty" id="pTable_qty">
					<input type="hidden" name="isAllowNum" id="pTable_isAllowNum">
				</div>
			</div>
	<!-- 	</div>
		</div> -->

			<!-- <div data-options="region:'east'"
     style="width:45%;">
    <div class="easyui-layout" data-options="fit:true"> -->
        
                    <div data-options="border:false,region:'south'" style="height:160px;"
                        overflow:hidden;" class="js-panel" trigger="">
				<div name="pForm" id="pForm" style="width: 100%; height: 70%;"
					class="js-group">
					<input type="hidden" name="item" id="pForm_item">
					<div class="input-group-inline" style="width: 100%; height: 70%;">
						<!-- <label style='margin-left :3px;vertical-align:top;'></label> -->
						<label class='js-label'style="width:100%;height:22px;text-align:left;font-size:24px;color:green;" width-data="1" id="pForm_QRLabel" name="QRLabel"/>
						</label>
					</div><br>
					<div class="input-group-inline" >
					<!-- <label style='margin-left :3px;vertical-align:top;'></label> -->
					<label class='js-label'style="width:100%;height:22px;text-align:left;font-size:24px;color:red;" width-data="1" id="pForm_QRLabelRed" name="QRLabelRed"/></label>
					</div> 	<br>
						<input type="hidden" name="isRed" id="pForm_isRed">
					
					<!-- <div class="input-group-inline" style="width: 100%; height: 70%;">
						<label style="margin-left: 3px; vertical-align: top;">提示</label>
						 <input
							type="text" readonly="true"
							data-options="prompt:'',cls:'readgrey',multiline:true,"
							id="pForm_item" name="item" class="easyui-textbox js-input"
							style="width: 100%; height: 70%; text-align: top"
							tipPosition="bottom" width-data="1" /> 
					</div> -->
					
					
					<br>
				</div>
			</div>
			<!-- </div>
			</div> -->
		</div>
	</div>
	<!--Center end-->
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
    	//分辨率
       /*  var screenWidth = ScreenWidth();
      	console.log("screenWidth:"+screenWidth); */
      	
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
    //pButton.changeOnClick开始
function OnClick_pButton_change_frmPwipHistoryAddPO(){
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
	if(eval("clearStorage('pAmount','frmPwipHistoryAddPO')")){
		
	}else{
		
	}
 }

jsclear();
}

//pButton.changeOnClick开始
function OnClick_pButton_change_frmPwipHistoryAddPO(){
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
	if(eval("clearStorage('pAmount','frmPwipHistoryAddPO')")){
		
	}else{
		
	}
 }

jsclear();
}
//pButton.changeOnClick结束

//pButton.changeOnClick结束

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
