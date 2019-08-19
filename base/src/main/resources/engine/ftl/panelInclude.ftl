<#if panelObj.type=='Toolbar'>
    <#include "toolbar/toolbar.ftl">
</#if>
<#if panelObj.type=='Group'>
<div name="${panelObj.name}" id="${panelObj.name}" style="padding-top:15px;"
     class="js-group">
    <#include "group/group.ftl">
</div>
</#if>
<#if panelObj.type=='Grid'>
    <#include "datagrid/datagrid.ftl">
</#if>
<#if panelObj.type=='Tree'>
    <#include "tree/tree.ftl">
</#if>
<#if panelObj.type=='Image'>
    <#include "image/image.ftl">
</#if>

<#if panelObj.type=='LineChart'||panelObj.type=='PieChart'||panelObj.type=='BarChart'||panelObj.type=='ColumnChart'>
    <#include "chart/chart.ftl">
</#if>
<#if panelObj.type=='GanttChart'>
    <#include "chart/gantt_chart.ftl">
</#if>
<#if panelObj.type=='WorkflowList'>
    <#include "workflow/workflowList.ftl">
</#if>
<#if panelObj.type=='Hidden'>
<div name="${panelObj.name}" id="${panelObj.name}" style="display:none" class="js-group">
    <#include "group/group.ftl">
</div>
</#if>
