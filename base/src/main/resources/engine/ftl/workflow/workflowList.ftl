<script src="${ctx}/view/common/js/workflowList.js"></script>
 <div class="btn-group" style="text-align: right;">
<div style="float: left;line-height: 29px;border:1" class="js-path"></div>

        <a style="margin-right: 10px" class="easyui-linkbutton" href="javascript:void(0)"
           onClick="OnClick_${panelObj.flow.trigger}_${ui.name}()">刷新</a>

</div>



<div id="${panelObj.name}" class="workflowList js-group" style="width:100%;height:100%">
    <div id="workflowTab" class="easyui-tabs" data-options="closable:false,fit:true,plain:true,border:true">
    <#list panelObj.controlList as c>
        <#if c.type=='Link'>
            <#assign opTitle='${c.title}'>
        </#if>
        <#if c.property=='doingTitle'>
            <#assign doingTitle='${c.title}'>
        </#if>
        <#if c.property=='doneTitle'>
            <#assign doneTitle='${c.title}'>
        </#if>
        <#if c.property=='completeTitle'>
            <#assign completeTitle='${c.title}'>
        </#if>
        <#if c.property=='title'>
            <#assign title='${c.title}'>
        </#if>
        <#if c.property=='startTime'>
            <#assign startTime='${c.title}'>
        </#if>
        <#if c.property=='endTime'>
            <#assign endTime='${c.title}'>
        </#if>
        <#if c.property=='assignee'>
            <#assign assignee='${c.title}'>
            <#if c.initial!=''>
             <input name="assignee" id="assignee" value="1" type="hidden"/>
            </#if>
        </#if>
    </#list>

        <div title="${doingTitle}" class="js-panel">
            <table id="undoTaskList" class="easyui-datagrid" title=""
                   data-options="rownumbers: true,singleSelect:true,fit:true">
                <thead>
                <tr>
                    <th data-options="field:'operation',width:50,align:'left',formatter:opFormatter_${panelObj.name}">
                        操作
                    </th>
                    <th data-options="field:'title',width:300,align:'center'">${title}</th>
                    <th data-options="field:'createTime',width:150,align:'center'">${startTime}</th>
                    <th data-options="field:'assignee',width:100,align:'center'">${assignee}</th>
                </tr>
                </thead>
            </table>
        </div>
        <div title="${doneTitle}" class="js-panel">
            <table id="doneTaskList" class="easyui-datagrid" title=""
                   data-options="rownumbers: true,singleSelect:true,fit:true,nowrap:false,pageSize:'50'">
                <thead>
                <tr>
                    <th data-options="field:'operation',width:50,align:'left',formatter:opFormatter_${panelObj.name}">
                        操作
                    </th>

                    <th data-options="field:'title',width:300,align:'center'">${title}</th>
                    <th data-options="field:'createTime',width:150,align:'center'">${startTime}</th>
                    <th data-options="field:'assignee',width:100,align:'center'">${assignee}</th>
                </tr>
                </thead>
            </table>
            <script>

            </script>

        </div>
        <div title="${completeTitle}" class="js-panel">
            <table id="completeTaskList" class="easyui-datagrid" title=""
                   data-options="singleSelect:true,fit:true,pagination:true,rownumbers:true,nowrap:false,pageSize:'50'">
                <thead>
                <tr>
                    <th data-options="field:'operation',width:50,align:'left',formatter:opFormatter_${panelObj.name}">
                        操作
                    </th>
                    <th data-options="field:'title',width:300,align:'center'">${title}</th>
                    <th data-options="field:'createTime',width:150,align:'center'">${startTime}</th>
                    <th data-options="field:'assignee',width:100,align:'center'">${assignee}</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script>
    function opFormatter_${panelObj.name}(val, row, index) {

        return "<a href='#' onclick='javascript:goView(this,\"" + row.id + "\",\"" + row.viewUrl + "\",\"" + row.toTitle + "\")' >查看</a>";
    }

    function goView(a, id, url, toTitle) {
        setStorage('EditId', id);
        a.href = getTransitionUrl('', 'Transition', $("#uiid").val(), 'Readonly', $("#from").val(), $("#code").val(), toTitle, "ui/" + url, '${ctx}${server}/');

    }
</script>