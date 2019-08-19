<!DOCTYPE HTML >
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="no-cache">
    <meta http-equiv="expires" content="-1">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
	<!-- 动态引用easyui样式 -->
          <#if theme?? && theme!="" && theme!="default">
          	   	<link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui_${theme}.css" rel="stylesheet" media="screen"/>
         		<link href="${ctx}/view/common/css/style_${theme}.css?${date}" rel="stylesheet" media="screen"/>
          <#else>
	           <!-- 标准 easyui / style 样式1 -->
			   <link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui.css?${date}" rel="stylesheet" media="screen"/>
			   <link href="${ctx}/view/common/css/style.css?${date}" rel="stylesheet" media="screen"/>
         </#if>
    <link href="${ctx}/view/common/css/imageView.css?${date}" rel="stylesheet" media="screen"/>
    <link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/icon.css?${date}" rel="stylesheet" media="screen"/>
    <script type="text/javascript">var ctx = "${ctx}";</script>
    <script src="${ctx}/view/common/js/jquery-1.8.3.min.js?${date}"></script>
    <script src="${ctx}/view/common/css/plugins/easyui-1.5.2/jquery.easyui.min.js?${date}"></script>
    <script src="${ctx}/view/common/css/plugins/easyui-1.5.2/easyui.plugins.js?${date}"></script>
    <script src="${ctx}/view/common/js/Common.js?${date}"></script>
    <!-- 引用EasyUI的国际化文件,让它显示中文 -->
    <script src="${ctx}/view/common/css/plugins/easyui-1.5.2/locale/easyui-lang-zh_CN.js?${date}" type="text/javascript"></script>
    <script src="${ctx}/view/common/js/base.js?${date}"></script>
    <script src="${ctx}/view/common/js/MD5.js?${date}"></script>
    <script src="${ctx}/view/common/js/jsExpression.js?${date}"></script>

    <!-- js本地存储-->
    <script src="${ctx }/view/common/js/store/myStorage.js?${date}" charset="utf-8"></script>
    <script src="${ctx }/view/common/js/store/json2.js?${date}" charset="utf-8"></script>
    <script src="${ctx }/view/common/js/store/localDB.js?${date}" charset="utf-8"></script>
    <!-- ajax请求-->
    <script src="${ctx}/view/common/js/ajaxUtil.js?${date}"></script>
	
	
         
	<#assign rlist=ui.panelSet>
    <#list rlist as data>
    	<#assign clist=ui.controlSet>
	    <#list clist as data1>
		 <#if data1=='KindEditor'>
			<!--kindEditor-->
			<script src="${ctx}/view/common/css/plugins/kindEditor/kindeditor-all.js"></script>
			<script charset="utf-8" src="${ctx}/view/common/css/plugins/kindEditor/lang/zh-CN.js"></script>
         </#if>
		 <#if data1=='UEditor'>
			<script src="${ctx}/view/common/js/ueditor/ueditor.config.js"></script>
			<script src="${ctx}/view/common/js/ueditor/ueditor.all.min.js"></script>
			<script src="${ctx}/view/common/js/ueditor/lang/zh-cn/zh-cn.js"></script>
         </#if>         
		 <#if data1=='ImageView'>
		    <script src="${ctx}/view/common/js/carousel-v0.7.js?${date}"></script>
         </#if>
		 <#if data1=='FileBox'>
		      <link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/jquery.Jcrop.css?${date}" rel="stylesheet" media="screen"/>
		    <script src="${ctx}/view/common/css/plugins/easyui-1.5.2/plugins/jquery.Jcrop.min.js?${date}"></script>
		    <script src="${ctx}/view/common/css/plugins/easyui-1.5.2/plugins/imgCropUpload.js?${date}"></script>
	     	<script src="${ctx}/view/common/css/plugins/easyui-1.5.2/plugins/jquery.filebox.js?${date}"></script>
		 	<script src="${ctx}/view/common/js/ajaxfileupload.js?${date}"></script>
         </#if>
    	</#list>
         <#if data=='Grid'>
         <script src="${ctx}/view/common/js/datagrid.js?${date}"></script>
         </#if>
          <#if data=='Group'>
         <script src="${ctx}/view/common/js/group.js?${date}"></script>
         </#if>
          <#if data=='Tree'>
         <script src="${ctx}/view/common/js/tree.js?${date}"></script>
         </#if>
          <#if data=='LineChart'||data=='PieChart'||data=='ColumnChart'||data=='BarChart'>
		    <script src="${ctx}/view/common/js/echarts.common.min.js?${date}"></script>
		    <script src="${ctx}/view/common/js/chart.js?${date}"></script>
         </#if>
          <#if data=='GanttChart'>
            <script src="${ctx}/view/common/js/echarts.common.min.js?${date}"></script>
		    <script src="${ctx}/view/common/js/ganttchart.js?${date}"></script>
         </#if>
          <#if data=='Hidden'>
             <script src="${ctx}/view/common/js/hidden.js?${date}"></script>
         </#if>
	</#list>
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
    	foo();
		noBackPlease();

		// disables backspace on page except on input fields and textarea..
		document.body.onkeydown = function (e) {
            var elm = e.target.nodeName.toLowerCase();
            if (e.which === 8 && (elm !== 'input' && elm  !== 'textarea')) {
                e.preventDefault();
            }

        	var theEvent = e || window.event;    
	        var code = theEvent.keyCode || theEvent.which || theEvent.charCode;    
	        if (code == 13) {    
	          	${defaultEnter}
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
             var eve=event ? event : window.event;
             if((eve.keyCode==13) && (event.ctrlKey)){  //ctrl+ enter 换行
				event.srcElement.value += "\n";
             }else if(eve.keyCode==13){  //enter 回车
				//$(items[next]).focus(); 
             } 
         }  
         })();  
    }     
}  
</script>
    
    
</head>
<body class="easyui-layout js-ui" title="${ui.title}" id="${ui.name}" style="width:100%;height:100%;background-color:white;">

<div id='Loading'
     style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#FFFFFF ;text-align:center;padding-top: 10%;">
    <h1 style="display: inline-block;border: 1px solid #95b9e7;font-size: 16px;padding: 5px;">
        <image src='${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/images/loading.gif'/>
        <font color="#15428B" size="2">正在处理，请稍等···</font></h1>
</div>

<input type="hidden" id="from" value="${from}">
<input type="hidden" id="code" value="${code}">
<input type="hidden" id="uiid" value="${uiid}">
<input type="hidden" id="puid" value="${puid}">
<#assign rlist=ui.layout.regionList>
<#assign base=request.contextPath />
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
<#list rlist as r>
<!--${r.location} begin-->
<div data-options="region:'${r.location?lower_case}'"
     style="<#if '${r.location}' == "East"|| '${r.location}' == "West">width:${r.scale};</#if>">
    <div class="easyui-layout" data-options="fit:true">
        <#assign subregionList=r.subregionList>
        <#assign fileIdx=0>
        <#list subregionList as sub>
        
            <#assign tabList=sub.listSubregion>
            <#if tabList?? && (tabList?size gt 1)>
            <#assign chartFlg=false>
            <#assign gridFlg=false>
                <div data-options="border:true,region:'${sub.location?lower_case}'" style="
                    <#if '${r.location}' != "Center">overflow:hidden;</#if>
                     <#list tabList as sr>
                        <#if '${sr.panelObj.type}'=="GanttChart"||'${sr.panelObj.type}'=="LineChart"||'${sr.panelObj.type}'=="PieChart"||'${sr.panelObj.type}'=="BarChart"||'${sr.panelObj.type}'=="ColumnChart">
                        <#assign chartFlg=true>
						</#if>
                    	<#if ('${sr.location}' == "North"|| '${sr.location}' == "South")&&'${sr.panelObj.type}'=="Grid">
                        <#assign gridFlg=true>
						</#if>
                     </#list>
                    <#if '${tabList[0].location}' == "East"|| '${tabList[0].location}' == "West">width:${tabList[0].scale};</#if>
                    <#if gridFlg>height:${tabList[0].scale};
                    <#elseif chartFlg>
                    height:${r.scale};</#if>
                        ">
                    <div id="tab" class="easyui-tabs" data-options="closable:false,fit:true,plain:true,border:true">
                        <#list tabList as sr>
                            <#list sr.listSubregion as sr>
                                <div title="${sr.panelObj.title}" class="js-panel" trigger="${sr.panelObj.flow}">
                                    <#assign panelObj=sr.panelObj>
								<#include "panelInclude.ftl">
                                </div>
                            </#list>
                        </#list>
                    </div>
                </div>
            <#else>
                <#list tabList as sr>
                    <div data-options="border:false,region:'${sr.location?lower_case}'" style="
                        <#if '${sr.location}' != "Center">overflow:hidden;</#if><#-- 区域为中时可以出滚动条 -->
                        <#if '${sr.location}' == "East">width:${sr.scale};border-left:1px solid #95B8E7;
                        </#if><#-- 解决第二层为东西的时候 没有分割线-->
                        <#if '${sr.location}' == "West">width:${sr.scale};border-right:1px solid #95B8E7;
                        </#if><#-- 解决第二层为东西的时候 没有分割线-->
                        <#if ('${sr.location}' == "North"|| '${sr.location}' == "South")&&'${sr.panelObj.type}'=="Grid">height:${sr.scale};</#if>
                        <#if '${sr.panelObj.type}'=="GanttChart"||'${sr.panelObj.type}'=="LineChart"||'${sr.panelObj.type}'=="PieChart"||'${sr.panelObj.type}'=="BarChart"||'${sr.panelObj.type}'=="ColumnChart">height:${r.scale};</#if>
                            " class="js-panel" trigger="${sr.panelObj.flow}">
                        <#assign panelObj=sr.panelObj>
							<#include "panelInclude.ftl">
                    </div>
                </#list>
            </#if>
        </#list>
    </div>
</div>
<!--${r.location} end-->
</#list>
<!--end-->
<#list hiddenPanel?keys as key>
    <#assign panelObj=hiddenPanel[key]>
    <#include "panelInclude.ftl">
</#list>
<#list hiddenTable?keys as key>
    <#assign panelObj=hiddenTable[key]>
    <div style="display:none">
    <#include "datagrid/datagrid.ftl">
    </div>
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
		cus_resize_init_input();//comment out this line if your wanna use a special style
    
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
	        var len = getStorage(path+"_name")==null?0:getStorage(path+"_name").length;
			for (var i=0;i<len;i++){
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
            var this_width = ($(this).width() - 20);
            $(this).find(".js-input").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data"))-121 ;
                    $(n).textbox('resize',new_width);
            });
            $(this).find(".js-label").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data"))-121 ;
                    $(n).width(new_width);
            });
           $(this).find("input:text, textarea").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data"))-121 ;
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

	function antiClick(id){
		$('#'+id).linkbutton('disable');
		setTimeout(function() { $('#'+id).delay(3000).linkbutton('enable'); }, 2000);
	}    
</script>
</html>
