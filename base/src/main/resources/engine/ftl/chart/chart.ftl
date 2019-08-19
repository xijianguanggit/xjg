<#if panelObj.type=='LineChart'>
    <#assign className = "lineChart" >
</#if>
<#if panelObj.type=='PieChart'>
    <#assign className = "pieChart" >
</#if>
<#if panelObj.type=='BarChart'>
    <#assign className = "barChart" >
</#if>
<#if panelObj.type=='ColumnChart'>
    <#assign className = "columnChart" >
</#if>

<#list panelObj.controlList as c>
    <#if c_index==0>
        <#assign xAxisName='${c.title}'>
        <#assign chartProperty ='\'${c.property}\''>
        <#assign chartAllProperty ='\'${c.property}\''>
    </#if>
    <#if c_index==1>
        <#assign yAxisName='${c.title}'>
        <#assign lengend ='\'${c.title}\''>
    </#if>
    <#if (c_index >1)&& c.type != 'Hidden'>
        <#assign lengend = lengend+","+'\'${c.title}\''>
    </#if>

    <#if (c_index >0)&& c.type != 'Hidden'>
        <#assign chartProperty = chartProperty+","+'\'${c.property}\''>
    </#if>
    <#if (c_index >0)>
        <#assign chartAllProperty = chartAllProperty+","+'\'${c.property}\''>
    </#if>
</#list>

<div id="${panelObj.name}" class="${className}" style="width: 100%;height:100%;">
    <div id="${panelObj.name}_chart" style="width: 100%;height:100%;">

    </div>
    <div id="${panelObj.name}_chartControl">
    <#assign chartHiddenProperty ="">
    <#list panelObj.controlList as g>
        <#if g.type== 'Hidden' ||g.type== 'DataLink' >
            <input type="hidden" name="${g.name}" id="${panelObj.name}_${g.name}">
            <#if chartHiddenProperty =''>
                <#assign chartHiddenProperty ='\'${g.property}\''>
            </#if>
            <#if chartHiddenProperty != ''&&chartHiddenProperty!='\'${g.property}\''>
                <#assign chartHiddenProperty =chartHiddenProperty+","+'\'${g.property}\''>
            </#if>
        </#if>
    </#list>
    </div>
</div>


<script>
    var ${panelObj.name}_data = [];
    var chartProperty = [${chartProperty}];

    var chartAllProperty = [${chartAllProperty}];
    var ${panelObj.name}_chartAllProperties = [];

    $.each(chartAllProperty, function (i) {
        ${panelObj.name}_chartAllProperties.push(chartAllProperty[i]);
    });

    //设置图表全部Key
    ${panelObj.name}_data.allKey = ${panelObj.name}_chartAllProperties;

    var ${panelObj.name}_chartProperties = [];

    $.each(chartProperty, function (i) {
        ${panelObj.name}_chartProperties.push(chartProperty[i]);
    });
    //设置图表显示Key
    ${panelObj.name}_data.showKey = ${panelObj.name}_chartProperties;

    var ${panelObj.name}_chartHiddenProperties = [];
    var chartHiddenProperty = [${chartHiddenProperty}];

    $.each(chartHiddenProperty, function (i) {
        ${panelObj.name}_chartHiddenProperties.push(chartHiddenProperty[i]);
    });

    //设置图表隐藏Key
    ${panelObj.name}_data.hiddenKey = ${panelObj.name}_chartHiddenProperties;

</script>

<script>
    var ${panelObj.name} =echarts.init(document.getElementById('${panelObj.name}_chart'));
    var chartOption_${panelObj.name} = {
        chartName:'${panelObj.name}',
        title:'${panelObj.title}',
        xName:'${xAxisName}',
        yName:'${yAxisName}'
    }
    <#if panelObj.type=='LineChart'>
    var lengendStr_${panelObj.name} =[${lengend}];
    var lineSeries_${panelObj.name} = [];
    $.each(lengendStr_${panelObj.name}, function (i) {
        lineSeries_${panelObj.name}.push({
            type: 'line',
            name:[lengendStr_${panelObj.name}[i]],
            data:[]
        });
    });
    chartOption_${panelObj.name}.lengend=lengendStr_${panelObj.name};
    chartOption_${panelObj.name}.series=lineSeries_${panelObj.name};
    var ${panelObj.name}_chartInitOption = getLineChartInitOption(chartOption_${panelObj.name});
    </#if>
    <#if panelObj.type=='PieChart'>
    var ${panelObj.name}_chartInitOption = getPieChartInitOption(chartOption_${panelObj.name});
    </#if>
    <#if panelObj.type=='BarChart'>
    var lengendStr_${panelObj.name} = [${lengend}];
    var barSeries_${panelObj.name} = [];
    $.each(lengendStr_${panelObj.name}, function (i) {
        barSeries_${panelObj.name}.push({
            type: 'bar',
            barGap: '30%',
            barCategoryGap : '20%',
            name: [lengendStr_${panelObj.name}[i]],
            data: [],
            hiddenData:[]
        });
    });
    chartOption_${panelObj.name}.lengend=lengendStr_${panelObj.name};
    chartOption_${panelObj.name}.series=barSeries_${panelObj.name};
    var ${panelObj.name}_chartInitOption = getBarChartInitOption(chartOption_${panelObj.name});
    </#if>
    <#if panelObj.type=='ColumnChart'>
    var lengendStr_${panelObj.name} =[${lengend}];
    var columnSeries_${panelObj.name} = [];
    $.each(lengendStr_${panelObj.name}, function (i) {
        columnSeries_${panelObj.name}.push({
            type: 'bar',
            barGap: '30%',            // 柱间距离，默认为柱形宽度的30%，可设固定值
            barCategoryGap : '20%',
            name:[lengendStr_${panelObj.name}[i]],
            data:[]
        });
    });
    chartOption_${panelObj.name}.lengend=lengendStr_${panelObj.name};
    chartOption_${panelObj.name}.series=columnSeries_${panelObj.name};
    var ${panelObj.name}_chartInitOption = getColumnChartInitOption(chartOption_${panelObj.name});
    </#if>

    ${panelObj.name}.setOption(${panelObj.name}_chartInitOption);
    ${panelObj.name}.resize();

    ${panelObj.name}.on('click', function (params) {
        setStorage('selectChartDate', params);
    <#list panelObj.controlList as g1>
        <#if g1.flow??>
            ${g1.jsEvent}();
        </#if>
    </#list>
    });
</script>