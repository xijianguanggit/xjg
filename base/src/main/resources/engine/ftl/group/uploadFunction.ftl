 <#if g['jsEvent'] != '' >
	onClickButton:function(){eval('${g.jsEvent}()');},
</#if>
onChange: function(a,b){
				var out = outParam.split('.');
				var path = pathParam == ''?'':pathParam.split('.');
				var module=moduleParam;
					if(out == null || out.length != 2 ){
						console.log('Out参数有误，请检查配置');
					}else if(path != null && path!='' && path.length != 2){
						console.log('Url参数有误，请检查配置');
					}else{
					if($(this).attr('type')=='grid'){
				    	var fileIdx=$(this).attr('index');
					} else {
						var fileIdx='${fileIdx}';
					}
		    		var upfile = document.getElementById('filebox_file_id_'+fileIdx).files[0];
		    		var size = ''; size = fileSize;
		    		var type = ''; type = fileType;
		    		var accessType = ''; accessType = fileAccessType;
		    		var uploadUrl = ''; uploadUrl = fileUploadUrl;
		    		var token = ''; token = upToken;
		    		
		    		var pointPos = upfile.name.lastIndexOf('.');
					var fileSuffix=upfile.name.substring(pointPos+1,upfile.name.length);//后缀名
		    		 
		    		if(size == ''){
		    			$.messager.alert('文件上传','请配置文件上传大小参数(单位:M)');
		    		}else if(parseInt(size)*1024<=upfile.size){//单位M
		    			var msg = '上传文件不能超过'+size+'KB，请重新上传';
		    			$.messager.alert('文件上传',msg);
		    		}else if(type!='' && type.toLowerCase().indexOf(fileSuffix.toLowerCase())<0){
		    			$.messager.alert('文件上传','文件类型不在允许范围内(' + type + ')');
		    		}else{
						$.messager.confirm('文件上传', '确定要上传吗?', function(r){
							if (r){
							  	 $.messager.progress({ 
									           title: '提示', 
									           msg: '正在上传，请稍后……', 
									           text: '' 
									        });
					    		$.ajaxFileUpload({  
					                 url:'${ctx}/localUpload?token='+token,  
					                 data:{accessType:accessType,allowFile:type,maxSize:size,uploadUrl:uploadUrl,module:module},
					                 secureuri:false,  
					                 fileElementId:'filebox_file_id_'+fileIdx,//file标签的id  
					                 dataType: 'json',//返回数据的类型
					                 contentType:'application/json',
					                 success: function (data) {
					                 	$.messager.progress('close'); 
					                 	if(data.status!=0){
	         			    				if($('#'+out[0]).attr('class').indexOf('js-group')>-1){
							    		    	//赋值给指定input
							    		    	$('#'+out[0]+'_'+out[1]).val(data.status);
							    		    	if(path != null){
							    		    		$('#'+path[0]+'_'+path[1]).val(data.data.url);
							    		    	}
						    				} else if($('#'+out[0]).attr('class').indexOf('easyui-datagrid')>-1){
						    					//赋值给指定列
						    					$('#'+out[0]).datagrid('getSelected')[out[1]]=data.status;
						    					if(path != null){
							    		    		$('#'+path[0]).datagrid('getSelected')[path[1]]=data.data.url;
							    		    	}
						    				}
		    								  try {
												eval('${panelObj.name}_${g.name}_OnSelect()');
											  } catch(e) {}
					                 	}
				                     	$.messager.alert('文件上传',data.msg);
					                 },
					                 error: function (data) {  
					                     $.messager.alert('文件上传失败，请重新上传');  
					                 }  
			             		});
							}
						});
		    		}
	    		}
	    	}