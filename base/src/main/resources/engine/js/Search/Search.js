var ${name}=

function () {
    var className = $("#${Out}").attr("class");
    //datagrid组件
    if (className.indexOf("easyui-datagrid") > -1) {
        var param = {}
        param = $("#${In} :input,hidden").serializeJson();
        param['token'] = ${getToken};//查询类的可以多次使用，不必consumeToken
        param['queryParam'] = "${Sql}";
        var storageId = "backStorage_" + $(".js-ui").attr("id");
        var storage = eval('(' + getStorage(storageId) + ')');
        var gridData;
        if (storage) {
            gridData = storage.grid["${Out}"];
        }
        //点击返回时逻辑 判断内存中是否存在transition数据
        if (gridData) {
            $('#${Out}').datagrid({
                url: '${ctx}${server}',
                queryParams: param,
                pageNumber: gridData.pageNumber,
                pageSize: gridData.pageSize,
                emptyMsg: "empty result",
                onLoadSuccess: function (data) {
                    if (data.total == 0) {
                        showEmptyResultMsg();
                    }
                    if (gridData.selected) {
                        $('#${Out}').datagrid('selectRow', gridData.selected);
                    }
//					updateClientContext(data.clientContext);
                    ${ifyes}
                }, onBeforeLoad: function (data) {
                }
            });
            //加载完成后清楚内存中transition数据
            storage.grid['${Out}'] = null;
            setStorage(storageId, JSON.stringify(storage));
        } else {
            //页面首次加载逻辑
            $('#${Out}').datagrid({
                url: '${ctx}${server}',
                queryParams: param,
                pageNumber: 1,
                emptyMsg: "empty result",
                onLoadSuccess: function (data) {
                    if (data.total == 0) {
                        showEmptyResultMsg();
                    }
                    updateClientContext(data.clientContext);
                    ${ifyes}
                }, onBeforeLoad: function (data) {
                }
            });
        }
    }
    // 树组件
    if (className.indexOf("easyui-tree") > -1) {
        var storageId = "backStorage_" + $(".js-ui").attr("id");
        var storage = eval('(' + getStorage(storageId) + ')');
        var treeData;
        if (storage) {
            treeData = storage.tree["${Out}"];
        }
        //点击返回时逻辑 判断内存中是否存在transition数据
        if (treeData) {
            $('#${Out}').tree({
                url: '${ctx}${server}?queryParam=${Sql}&token=' +${getToken},
                loadFilter: function (data) {
                    return treeData.data;
                }
            });
            //加载完成后清楚内存中transition数据 延迟0.4秒执行
            setTimeout("var storageId='backStorage_'+$('.js-ui').attr('id');" +
                "var storage =eval('(' + getStorage(storageId) + ')');" +
                "var treeData=storage.tree['${Out}'];" +
                "var node = $('#${Out}').tree('find', treeData.selected);" +
                "if(node){$('#${Out}').tree('select', node.target);}" +
                "storage.tree['${Out}']=null;" +
                "setStorage(storageId, JSON.stringify(storage));", 400);
        } else {
            //首次加载逻辑
            $('#${Out}').tree({
                url: '${ctx}${server}?queryParam=${Sql}&token=' +${getToken},
                loadFilter: function (data) {
                    return data.rows;
                }
            });
        }
        ${ifyes}
    }



    // 线状图
    // 线状图
    if (className.indexOf("lineChart") > -1) {
        var param = {}
        param['token'] = ${getToken};
        param['queryParam'] = '${Sql}';

        // 基于准备好的dom，初始化echarts实例
        ${Out}.showLoading();    //数据加载完之前先显示一段简单的loading动画
        ajaxPost("${ctx}${server}", param, function (result) {
                var values = '${Columns}';
                var series = new Array();
                var names = new Array();
                if (values.indexOf('|')==-1) {
                    var seriesData = new Object();
                    var value = new Array();
                    var columnName = values.split(',');

                    $.each(result.data.rows, function (index, obj) {
                        names.push(obj.name);
                        value.push(obj[columnName[1]]);
                    });
                    seriesData.type='line';
                    seriesData.name=columnName[0];
                    seriesData.data =value;
                    series.push(seriesData);
                } else {
                    //多条折线
                    var columnNames = values.split('|');
                    $.each(columnNames,function (i, v) {
                        var columnName =v.split(',');
                        var seriesData = new Object();
                        var value = new Array();
                        $.each(result.data.rows, function (index, obj) {
                            names.push(obj.name);
                            value.push(obj[columnName[1]]);
                        });
                        seriesData.type='line';
                        seriesData.name=columnName[0];
                        seriesData.data =value;
                        series.push(seriesData);
                    });
                }
                if (result) {
                    ${Out}.hideLoading();    //隐藏加载动画
                    ${Out}.setOption({
                        xAxis: [
                            {
                                data: names
                            }
                        ],
                        series: series
                    });

                }
            }
            ,

            function (res) {//error
                parent.showError(res.responseText);
                ${Out}.hideLoading();
                ${ifno}
            }
        );
        ${ifyes}
    }

//饼图
    if (className.indexOf("pieChart") > -1) {
        var param = {}
        param['token'] = ${getToken};
        param['queryParam'] = '${Sql}';
        // 基于准备好的dom，初始化echarts实例
        ${Out}.showLoading();    //数据加载完之前先显示一段简单的loading动画
        ajaxPost("${ctx}${server}", param, function (result) {
                var legendData = new Array();
                $.each(result.data.rows, function (index, obj) {
                    legendData.push(obj.name);
                });

                if (result) {
                    ${Out}.hideLoading();    //隐藏加载动画
                    ${Out}.setOption({
                        legend: {
                            //动态图例
                            data: legendData
                        },
                        series: [{
                            // 根据名字对应到相应的系列
                            data: result.data.rows
                        }]
                    });

                }
            },
            function (res) {//error
                parent.showError(res.responseText);
                ${Out}
            .
                hideLoading();
                ${ifno}
            });
        ${ifyes}
    }

//柱状图
    if (className.indexOf("columnChart") > -1) {
        //根据
        var param = {}
        param['token'] = ${getToken};
        param['queryParam'] = '${Sql}';
        // 基于准备好的dom，初始化echarts实例
        ${Out}.showLoading();    //数据加载完之前先显示一段简单的loading动画
        ajaxPost("${ctx}${server}", param, function (result) {
                var values = '${Columns}';
                var series = new Array();
                var names = new Array();
                if (values.indexOf('|')==-1) {
                    var seriesData = new Object();
                    var value = new Array();
                    var columnName = values.split(',');

                    $.each(result.data.rows, function (index, obj) {
                        names.push(obj.name);
                        value.push(obj[columnName[1]]);
                    });
                    seriesData.name=columnName[0];
                    seriesData.data =value;
                    seriesData.type='bar';
                    seriesData.barWidth=30;
                    //设置第一柱图的颜色
                    var itemStyle = new Object();
                    var normal = new Object();
                    var color =function(params) {
                        // build a color map as your need.
                        var colorList = [
                            '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                            '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                            '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                        ];
                        return colorList[params.dataIndex]
                    }
                    normal.color=color;
                    itemStyle.normal= normal;
                    seriesData.itemStyle =itemStyle;

                    series.push(seriesData);

                } else {
                    //多个柱
                    var columnNames = values.split('|');
                    $.each(columnNames,function (i, v) {
                        var columnName =v.split(',');
                        var seriesData = new Object();
                        var value = new Array();
                        $.each(result.data.rows, function (index, obj) {
                            names.push(obj.name);
                            value.push(obj[columnName[1]]);
                        });
                        seriesData.type='bar';
                        seriesData.name=columnName[0];
                        seriesData.data =value;
                        series.push(seriesData);
                    });
                }
                if (result) {
                    ${Out}.hideLoading();    //隐藏加载动画
                    ${Out}.setOption({
                        xAxis: [
                            {
                                data: names
                            }
                        ],
                        series: series
                    });

                }
            },
            function (res) {//error
                parent.showError(res.responseText);
                ${Out}.hideLoading();
                ${ifno}
            });
        ${ifyes}
    }

//条状图
    if (className.indexOf("barChart") > -1) {
        var param = {}
        param['token'] = ${getToken};
        param['queryParam'] = '${Sql}';
        // 基于准备好的dom，初始化echarts实例
        ${Out}.showLoading();    //数据加载完之前先显示一段简单的loading动画
        ajaxPost("${ctx}${server}", param, function (result) {
                var values = '${Columns}';
                var series = new Array();
                var names = new Array();
                if (values.indexOf('|')==-1) {
                    var seriesData = new Object();
                    var value = new Array();
                    var columnName = values.split(',');

                    $.each(result.data.rows, function (index, obj) {
                        names.push(obj.name);
                        value.push(obj[columnName[1]]);
                    });
                    seriesData.name=columnName[0];
                    seriesData.data =value;
                    seriesData.type='bar';
                    seriesData.barWidth=30;
                    //设置第一柱图的颜色
                    var itemStyle = new Object();
                    var normal = new Object();
                    var color =function(params) {
                        // build a color map as your need.
                        var colorList = [
                            '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                            '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                            '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                        ];
                        return colorList[params.dataIndex]
                    }
                    normal.color=color;
                    itemStyle.normal= normal;
                    seriesData.itemStyle =itemStyle;
                    series.push(seriesData);
                } else {
                    //多个柱
                    var columnNames = values.split('|');
                    $.each(columnNames,function (i, v) {
                        var columnName =v.split(',');
                        var seriesData = new Object();
                        var value = new Array();
                        $.each(result.data.rows, function (index, obj) {
                            names.push(obj.name);
                            value.push(obj[columnName[1]]);
                        });
                        seriesData.type='bar';
                        seriesData.name=columnName[0];
                        seriesData.data =value;
                        series.push(seriesData);
                    });
                }
                if (result) {
                    ${Out}.hideLoading();    //隐藏加载动画
                    ${Out}.setOption({
                        yAxis: [
                            {
                                data: names
                            }
                        ],
                        series: series
                    });

                }
            },
            function (res) {//error
                parent.showError(res.responseText);
                ${Out}.hideLoading();
                ${ifno}
            });
        ${ifyes}
    }

};