var dbType=null;

$(function(){
	   init();
	   $(".run").off("click").on("click",function(){
		   var $this=$(this).parents(".easyui-layout");
		   var sql=  $this.find("textarea").val();
		   if(!sql){
			   alert("没有SQL语句");
			   return false;
		   }
		   //excuteSql();
		   datagridPage();
		   setWindow();
	   });
	
	ajaxSubmit(ctx+"/db/getDbType.action",false,true,{connectionName:connectionName},function(data){
		if(data.code==200){
			dbType=data.data;
			if(data.data.indexOf("ORACLE")>-1){
				var sql="select t.*,t.rowid from "+getTableName() +" t";
				$("textarea.sqlTextarea").val(sql);
			}else{
				var sql="select * from "+getTableName();
				$("textarea.sqlTextarea").val(sql);
			}
			$(".run").click();
		}
	});
	
});




function init(){
	$(".sqlResultTabs").tabs({
		onSelect:function(title,index){
			if(index==1 ){
			   	$(this).find(".result .columnData").datagrid("resize");
			}
		},
		onLoadSuccess:function(data){
			
		}
	});
}


function	getTableName(){
	 var str="";
	 if(getCataLog()){
		 str=str+getCataLog()+".";
	 }
	 if(getSchema()){
		 str=str+getSchema()+".";
	 }
	 str+=tableName;
	 return str;
	}

function getCataLog(){
	 if(catalog && catalog!=undefined && catalog != "undefined"){
		return catalog;
	 }
	 return "";
}
function getSchema(){
	 if(schema && schema!=undefined && schema != "undefined"){
		return schema;
	 }
	 return "";
}

function datagridPage(){
	var cls=[];
	var $this=$("#sqlWindow");
	var sql=  $this.find("textarea").val();
	var $table=$this.find(".result .columnData");
	var $page=null;
	var isCount=true;
	var isAdd=false,editing=null,beforeValue=null;
	  var param={
			  connectionName:connectionName,
			  catalog:catalog,
			  schema:schema,
			  type:type,
			  tableName:tableName
	  };
		var defaults={
				onSelectPage:function(pageNumber, pageSize){
			    	var page=	$this.find(".result .columnData").datagrid("getPager");
					$(page).pagination('loading');
					// 分页参数
					param.page=pageNumber;
					param.pageSize=pageSize;
					param.isCount=isCount;
					param.count=$(page).pagination('options').total;
					isCount=false;
					ajaxSubmit(ctx+"/execute/selectTable.action",false,true,param,function(data){
						if(data.code == 200){
							var v=$this.find(".message").length;
							$this.find(".message").text(data.data.message);
							if(data.data.columns){
								var cd=[];
								if(data.data.records){
									$.each(data.data.records,function(){
										var d={};
										var curD=this;
										$.each(cls,function(v,i){
											d[i.field]=curD.values[v];
										});
										d.allData=curD;
										cd.push(d);
									});
								}
								// 数据表格
								$this.find(".result .columnData").datagrid("loadData", cd);
								$(page).pagination('loaded');
								$this.find(".result .columnData").datagrid("resize");
								// 分页数据
								$(page).pagination('refresh',{	// 改变选项并刷新分页栏信息
									total: data.data.count,
									pageNumber:pageNumber,
									pageSize:pageSize,
									displayMsg:"Displaying {from} to {to} of {total} items,"+data.data.message
								});
							}	else{
								alert(data.msg);
							}
							
						}
						
					});
					
		
				}
		 }
	  
		 init();
		function init(){
			ajaxSubmit(ctx+"/execute/selectTable.action",false,true,param,function(data){
				if(data.code == 200){
					var v=$this.find(".message").length;
					$this.find(".message").text(data.data.message);
					if(data.data.columns){
						cls=getColumnsSetting(data.data.columns,dbType);
						var cd=getColumnsData(data.data.records,cls);
						// 数据表格
						$this.find(".result .columnData").datagrid({
							singleSelect:true,
							 fit:true,
							striped:true,
							data:cd,
							pagination:true,
							total:1000,
							pageNumber:1,
							pageSize:100,
							pageList: [10,30,50,100,150,200],
						    columns:[cls],
						    toolbar:[{
						    	text:"新增",
						    	iconCls:"icon-add",
						    	handler: function(){
						    		if(!editing && editing!=0){
						    			editing=0;
						    			isAdd=true;
						    			$table.datagrid("insertRow",{index:editing,row:{}});
							    		$table.datagrid("beginEdit",editing);
						    		}else{
						    			alert("请先保存");
						    		}
						    		//$this.find(".result .columnData").datagrid("appendRow",{});
						    	}
						    },
						    {
						    	text:"删除",
						    	iconCls:"icon-remove",
						    	handler: function(){
						    		if(isAdd){
						    			alert("目前为添加数据状态，不能删除！");
						    		}
						    		if( editing >= 0 && editing != null){
						    		     var beg=JSON.parse(beforeValue);
						    				layer.confirm('确定要删除该记录吗?', {
							    				  btn: ['确认','取消'] //按钮
							    				}, function(index){
							    					deleteRow(beg,false);
							    					layer.close(index);
							    				});
						    		     
						    		     
						    		}else if(editing == null){
						    			var selectRows=$table.datagrid("getSelections");
							    		if(selectRows.length==0){
							    			alert("没有选中行!");
							    		}else{
								    		layer.confirm('确定要删除该记录吗?', {
							    				  btn: ['确认','取消'] //按钮
							    				}, function(index){
							    					deleteRow(selectRows[0],false);
							    					layer.close(index);
							    				});
							    		}
						    		} else{
						    			alert("请先选择数据再进行删除！");
						    		}
						    	}
						    },
						    {
						    	text:"修改",
						    	iconCls:"icon-edit",
						    	handler: function(){
						    		var selectRows=$table.datagrid("getSelections");
						    		if(selectRows.length==0){
						    			alert("没有选中行!");
						    		}else{
						    		var index=	$table.datagrid("getRowIndex",selectRows[0]);
						    			if(!editing && editing!=0){
								    	    setEditing(index);
								    		$table.datagrid("beginEdit",editing);
								    	}else{
								    		$table.datagrid('endEdit',editing);  
								    		return false;
								    	}
						    		}
						    	}
						    	},
						    	 {
							    	text:"保存",
							    	iconCls:"icon-save",
							    	handler: function(){
							    		if(!editing && editing!=0){
							    	    	alert("没有修改数据");
							    		}else{
							    			$this.find(".result .columnData").datagrid('endEdit',editing);  
							    			editing=null;
							    		}
							    	}
							    },
							    {
							    	text:"取消",
							    	iconCls:"icon-cancel",
							    	handler: function(){
							    		$table.datagrid('rejectChanges');  
							    		editing=null;
							    		isAdd=false;
							    		beforeValue=null;
							    	}
							    },
						    ],
						    onLoadSuccess:function(data){
						    },
						    onBeforeSelect:function(index,data){
						    },
						    onBeforeEdit:function(index, row){
                                
						    },
						    onEndEdit:function(index, row, changes){
                               if(isAdd){
                            	   isSuccess=  insert(row).done(function(){
                            		   $table.datagrid('acceptChanges');  
       						        	alert("添加成功");
                            	   }).fail(function(){
                            		   $table.datagrid("rejectChanges");
                            	   }).always(function(){
                            		   editing=null;
        						    	isAdd=false;
                            	   });
                               }else{
                            	   var beg=JSON.parse(beforeValue);
                            	   updateRows(beg,row,false).done(function(){
                            		   $table.datagrid('acceptChanges');  
       						        	alert("保存成功");
                            	   }).fail(function(){
                            		   $table.datagrid("rejectChanges");
                            	   }).always(function(){
                            		   editing=null;
                            		   isAdd=false;
                            	   });
                               }
						    },
						    onAfterEdit:function(index, row, changes){
						    },
							onDblClickCell:function(index, field, value){
				    			$("#cellWindow .cellText").html(value);
				    			$("#cellWindow").window("setTitle",field);
				    			$("#cellWindow").window("open");
				    			return false;
						}
						});
						// 分页数据
						$page=$($table.datagrid("getPager"));
						$page.pagination("refresh",defaults);
						$page.pagination('refresh',{	// 改变选项并刷新分页栏信息
							total: data.data.count,
							displayMsg:"Displaying {from} to {to} of {total} items,"+data.data.message
						});
						$this.find(".result .columnData").datagrid("resize");
						
					}	
				}else{
					alert(data.msg);
				}	
			});	  
		}
		
		// 删除数据
		function deleteRow(row,force){
			var opt=$table.datagrid("options").columns;
			var allParam={connectionName:connectionName,force:force},changeValues=[],updateParam={
					 catalog:getCataLog(),
					 schema:getSchema(),
					 tableName:tableName,
					 values:changeValues
			};
			var updates=[updateParam];
			$.each(opt[0],function(){
				var allData=this.allData;
				changeValues.push(
				    {
				    	columnName:allData.columnName,
				    	dataType:allData.dataType,
				    	oldValue:row[allData.alias],
				    	newValue:""
				    }		
				);
			});
			
		   var isSuccess=false;
			allParam.updates=JSON.stringify(updates);
			ajaxSubmit(ctx+"/execute/delete.action",false,true,allParam,function(data){
				if(data.code==200){
					if(data.data.code==300){
						easyUiConfirm($("#MyConfirm"),function(){
							deleteRow(row,true);
							waiting=false;
						},null,{title:"确认？",content:data.data.msg});
					}else{
						 isAdd=null;
	    		    	 editing=null;
	    		    	 $page.pagination("select");
	    		    	 alert("删除成功！")
					}
					isSuccess=true;
				}else{
					alert(data.msg);
				}
			});
			return isSuccess;
		}
		
		// 更新数据
		function insert(changes){
			var defered=$.Deferred();
			var opt=$table.datagrid("options").columns;
			var allParam={connectionName:connectionName},changeValues=[],updateParam={
					 catalog:getCataLog(),
					 schema:getSchema(),
					 tableName:tableName,
					 values:changeValues
			};
			var updates=[updateParam];
			$.each(opt[0],function(){
				var allData=this.allData;
				changeValues.push(
				    {
				    	columnName:allData.columnName,
				    	dataType:allData.dataType,
				    	oldValue:'',
				    	newValue:changes[allData.alias]
				    }		
				);
			});
			allParam.updates=JSON.stringify(updates);
			ajaxSubmit(ctx+"/execute/insert.action",false,true,allParam,function(data){
				if(data.code==200){
					defered.resolve();
				}else{
					alert(data.msg);
					defered.reject(data.msg);
				}
			});
			return defered.promise();
		}
		
	
		// 更新数据
		function updateRows(beg,changes,force){
			var deferred=$.Deferred();
			var opt=$table.datagrid("options").columns;
			if(!force)
				force=false;
			var allParam={connectionName:connectionName,force:force},changeValues=[],updateParam={
					 catalog:getCataLog(),
					 schema:getSchema(),
					 tableName:tableName,
					 values:changeValues
			};
			var updates=[updateParam];
			$.each(opt[0],function(){
				var allData=this.allData;
				changeValues.push(
				    {
				    	columnName:allData.columnName,
				    	dataType:allData.dataType,
				    	oldValue:beg[allData.alias],
				    	newValue:changes[allData.alias]
				    }		
				);
			});
			var isChange=false;
		   // 检查是否已经有修改的值
			$.each(changeValues,function(){
			   if(this.oldValue!=this.newValue){
				   isChange=true;
				   return;
			   }
		   });
		   if(!isChange){
			 isAdd=null;
		     editing=null;
		 	deferred.reject();
			return deferred.promise();
		   }
			
		  var isSuccess=false;
			allParam.updates=JSON.stringify(updates);
			ajaxSubmit(ctx+"/execute/updateTable.action",false,true,allParam,function(data){
				if(data.code==200){
					if(data.data.code==300){
						easyUiConfirm($("#MyConfirm"),function(){
							updateRows(beg,changes,true)
						},function(){
							deferred.reject();
						},{title:"确认？",content:data.data.msg});
					}else{
						 isAdd=null;
	    		    	 editing=null;
	    		    	 //$page.pagination("select");
	    		    	// 更新成功
	    		    	 deferred.resolve();
					}
				}else{
					alert(data.msg);
					deferred.reject(data.msg);
				}
			});
			return deferred.promise();
		}
		
		function setEditing(index){
			editing=index;
    		beforeValue=JSON.stringify($table.datagrid("getRows")[index]);
		}
	  	
}



function easyUiConfirm($obj,ok,cancel,opts){
	var defaults={
		    title: "确认窗口",    
		    content:"确认?",
		    width: 400,    
		    height: 200,    
		    closed: true,    
		    cache: false,    
		    modal: true,
		    buttons:[{
				text:'确认',
				handler:function(){
					if(typeof ok === "function"){
						ok.call(this)
					}
					$obj.dialog("close",true);
				}
			},{
				text:'关闭',
				handler:function(){
					if(typeof cancel === "function"){
						cancel.call(this)
					}
					$obj.dialog("close",true);
				}
			}]
	};
	var opt=$.extend(defaults,opts);
	$obj.dialog(opt);    
	$obj.dialog("open",true);
}

 
function setWindow(){
//	   $("#tt .tabs-panels").css("height",$(window).height()-36);
	   $("#sqlWindow .easyui-layout").css("height",$(window).height()-66);
	   //datagrid-wrap  -38
	 //  $("#sqlWindow .easyui-tabs  .panel-htop").css("width",$("#sqlWindow.easyui-layout").outerWidth()-38);
	  // $("#sqlWindow .easyui-tabs  .panel-htop .panel-body").css("width",$("#sqlWindow.easyui-layout").outerWidth()-58);
	  
}


$(window.top).resize(function(){
	 setWindow();
	 //$('#tt').datagrid('resize');
	 });

