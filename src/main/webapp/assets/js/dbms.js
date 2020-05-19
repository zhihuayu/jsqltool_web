
var codeDefaultStyle={
	    mode: "text/x-mysql",
	    indentWithTabs: true,
	    smartIndent: true,
	    lineNumbers: true,
	    matchBrackets : true,
	    autofocus: true,
//	    lineWrapping: true, // 自动换行
	    extraKeys: {"Ctrl-Space": "autocomplete"},
	    hintOptions: {tables: {
	      users: ["name", "score", "birthDate"],
	      countries: ["name", "population", "size"]
	    }}
	  };

var staticData={
		
		sqlDataType:function(){
			if(staticData.tempSqlDataType){
				return staticData.tempSqlDataType;
			}
			ajaxSubmit(ctx+"/execute/sqlTtype.action",false,false,{},function(data){
				if(data.code == 200){
					staticData.tempSqlDataType=data.data;
				}else{
					alert(data.msg);
				}
			});
			return staticData.tempSqlDataType;
		}
		
}


function addPanel(){
	var database=schema==""?catalog+"":schema+"";
	var title="SQL["+database+"]";
	var body = template('sqlWindow',{});
	$("#tt").tabs("add",{ 
		bodyCls:"sqlWindow",
	    title:title,
	    content:body,    
	    closable:true
	});
	var tabs=$("#tt").tabs("tabs");
	var lastTabs=tabs[tabs.length-1];
	
	var edit=CodeMirror.fromTextArea($(lastTabs).find("textarea.sql")[0],codeDefaultStyle);
	edit.on("change",function($this){
			$this.save();
		});
	edit.on("keyup",function($this,e){
		    var keyCode = e.keyCode || e.which || e.charCode;
	        var ctrlKey = e.ctrlKey || e.metaKey;
	        if(ctrlKey && keyCode==119){
	        	if(!$this.getSelection()||$this.getSelection().trim().length<=0){
	        		alert("请选择要执行的代码！");
	        		return false;
	        	}
	        	 executeSql($($this.getInputField()).parents(".panel-htop"),$this.getSelection());
	        	return false;
	        }
		
		});
	$(lastTabs).find(".result .columnData").data("edit",edit);
	$(lastTabs).find(".result .columnData").datagrid({
		singleSelect:true,
		striped:true,
		minimized:true,
		pagination:true,
		total:1000,
		pageNumber:1,
		pageSize:100,
		pageList: [50,100,150,200],
		onDblClickCell:function(index, field, value){
    			$("#cellWindow .cellText").html(value);
    			$("#cellWindow").window("setTitle",field);
    			$("#cellWindow").window("open");
    			return false;
		},
	    onLoadSuccess:function(data){
	    },
	    onSelect:function(index, row){
	    	
	    }
	});
}

function addProcedureExport(){
	var database=schema==""?catalog+"":schema+"";
	var title="存储过程导出";
	var body = template('procedureExportWindow',{});
	$("#tt").tabs("add",{ 
		bodyCls:"procedureExportWindow",
	    title:title,
	    content:body,    
	    closable:true
	});
	var tabs=$("#tt").tabs("tabs");
	var lastTabs=tabs[tabs.length-1];
	
	$(lastTabs).find(".cloumnTable").datagrid({ 
		rownumbers:true,
		singleSelect:true,
		striped:true,
		autoRowHeight:true,
		fit:true,
		toolbar:[{
	    	text:"导出",
	    	iconCls:"icon-save",
	    	handler: function(){
	    		var selected=$(this).parents(".datagrid").find(".cloumnTable").datagrid("getSelected");
	    		if(selected){
	    			selected.connectionName=connectionName;
	    			ajaxSubmit(ctx+"/execute/exportProcedure.action",false,false,selected,function(data){
	    				if(data.code == 200){
	    					console.log(data);
	    					}else{
	    						alert(data.msg);
	    					}
	    				});
	    		}else{
	    			alert("请先选择要导出的存储过程");
	    		}

	    	}
	    }],
	    columns:[[    
	        {field:'db',enable:true,title:'数据库',width:'20%',resizable:true},    
	        {field:'type',title:'类型',width:'20%',resizable:true},    
	        {field:'name',title:'名称',width:'20%',resizable:true,},  
	        {field:'manage',title:'操作',width:'40%',resizable:true,}    
	    ]],
	    onLoadSuccess:function(data){
	    	$(".text-linkbutton1").addClass("c1").css("margin-left","16px");
	    	$(".text-linkbutton1").linkbutton({width:51,height:23});
	    	$(this).parents(".datagrid-wrap:eq(0)").find(".text-linkbutton1").click(function(){
	    		console.log($(this).attr("data-row"));
	    		var param=JSON.parse($(this).attr("data-row"));
	    		param.connectionName=connectionName
	    		jump("/execute/exportProcedure.action",param);
	    	});
	    	$(this).datagrid("resize");
	    },
	    onSelect:function(title,index){
	    },
	});
	
	 var param={
			  connectionName:connectionName
	   };
	ajaxSubmit(ctx+"/execute/getProcedure.action",false,false,param,function(data){
		if(data.code == 200){
			var td=[];
			$.each(data.data,function(){
				var itemD={};
				itemD=this;
				itemD.manage="<div><a  href='#' class='text-linkbutton1' data-row='"+JSON.stringify(itemD)+"' >导出</a></div>"
				td.push(itemD);
			});
			$(lastTabs).find(".cloumnTable").datagrid('loadData',td);
			}else{
				alert(data.msg);
			}
		});
}


function addProcedure(){
	var database=schema==""?catalog+"":schema+"";
	var title="procedure["+database+"]";
	var body = template('procedureWindow',{});
	$("#tt").tabs("add",{ 
		bodyCls:"procedureWindow",
	    title:title,
	    content:body,    
	    closable:true
	});
	var tabs=$("#tt").tabs("tabs");
	var lastTabs=tabs[tabs.length-1];
	$(lastTabs).find("input[name='procedureName']").textbox({required:true});
	var t=staticData.sqlDataType();
	t.push({label:"无返回值",value:"无返回值"});
	var c=$(lastTabs).find("select[name='returnType']").combobox({multivalue:false,editable:false,panelHeight:200,textField:"label",valueField:"label",data:t});
	c.combobox("setValue","无返回值");
	return false;
}

function addProcedureParam(dom){
	var body=template("procedureParam",{});
	$(dom).parents(".layout-body").find(".procedureParamContainer").append(body);
	
	$(dom).parents(".layout-body").find(".procedureParamContainer .procedureParam:last .easyui-textbox").each(function(){
		$(this).textbox();
	});
	$(dom).parents(".layout-body").find(".procedureParamContainer .procedureParam:last .easyui-numberbox").each(function(){
		$(this).numberbox({min:1,max:10});
	});
	$(dom).parents(".layout-body").find(".procedureParamContainer .procedureParam:last .easyui-combobox").each(function(){
		if($(this).attr("name")=="dataType"){
			$(this).combobox({multivalue:false,editable:false,panelHeight:200,textField:"label",valueField:"label",data:staticData.sqlDataType()});
			$(this).combobox("setValue","VARCHAR");
		}else{
			$(this).combobox({multivalue:false,editable:false,panelHeight:100});		
		}
	});
	return false;
}

function deleteParam(dom){
	$(dom).parents(".procedureParam").remove();
	return false;
}

function procedureRun(dom){
	var $body=$(dom).parents(".layout-panel-north");
	  var param={
			  connectionName:connectionName,
			  catalog:getCataLog(),
			  schema:getSchema()
	   };
	  var procedureName=$body.find("input[name='procedureName']").val();
	  var returnType=$body.find("select[textboxname='returnType']").combobox("getValue");
	  if(!procedureName){
		  addValidateBox($body.find("input[textboxname='procedureName']"));
		  return;
	  }
	  param.procedureName=procedureName;
	  param.returnType=returnType;
	  var params=[];
	  var isSuccess=true;
	  $body.find(".procedureParamContainer .procedureParam").each(function(){
		  var p={};
		  $(this).find("input[name]").each(function(){
			  if($(this).val()){
				  p[$(this).attr("name")]=$(this).val();
			  }
		  });
		  $(this).find("select").each(function(){
			  if($(this).val()){
				  p[$(this).attr("textboxname")]=$(this).combobox("getValue");
			  }
		  });
		  if(!p.paramName && !p.paramIndex){
			  addValidateBox($(this).find("input[name='paramName']"));
			  addValidateBox($(this).find("input[name='paramIndex']"));
			  isSuccess=false;
		  }else{
			  removeValidateBox($(this).find("input[name='paramName']"));
			  removeValidateBox($(this).find("input[name='paramIndex']"));
		  }
		  params.push(p);
	  });
	  if(!isSuccess){
		  return false;
	  }
	  if(params.length>0){
		  param.params=JSON.stringify(params);
	  }
	  ajaxSubmit(ctx+"/execute/excuteCall.action",false,false,param,function(data){
			if(data.code == 200){
				  $(dom).parents(".easyui-layout.layout").find(".messageContainer pre").html(data.data);
				}else{
					alert(data.msg);
				}
			});
	return false;
}


function onAdd(){
	   setWindow();
	   $(".sqlWindowTabs").each(function(){
		   $(this).tabs({
			   onSelect:function(title,index){
				   if(index==1 ){
//					   if($(this).find(".result .columnData").datagrid("options").columns.length>0){
//						   $(this).find(".result .columnData").datagrid({ minimized:false  });
						   $(this).find(".result .columnData").datagrid("resize");
//					   }else{
//						   $(this).find(".result .columnData").datagrid({ minimized:true});
//					   }
				   }
			   }
		   });
	   });
	   
	   $(".run").off("click").on("click",function(){
		   var $this=$(this).parents(".panel-htop");
		   var sql=  $this.find("textarea").val();
		   var edit= $this.find(".result .columnData").data("edit");
		  if(edit.getSelection()&&edit.getSelection().length>0){
			  sql=edit.getSelection();
		  }
		   if(!sql){
			   alert("没有SQL语句");
			   return false;
		   }
		   executeSql($this,sql)
		   return;
	   });
}



function executeSql($this,sql){
	  var param={
			  connectionName:connectionName,
			  catalog:catalog,
			  schema:schema,
			  sql:sql
	   };
		ajaxSubmit(ctx+"/execute/sql.action",false,true,param,function(data){
			if(data.code == 200){
				var $table=$this.find(".result .columnData");
				var isCount=true;
				$this.find(".message pre").html(data.data.message);
				if(data.data.columns){
					var cls=getColumnsSetting(data.data.columns);
					var cd=getColumnsData(data.data.records,cls);
					$this.find(".result .columnData").datagrid({
						fit:true,
						minimized:false,
						data:cd,
					    columns:[cls],
					});
					// ---pageSet
					var defaults={
							onSelectPage:function(pageNumber, pageSize){
						    	var page=	$table.datagrid("getPager");
								$(page).pagination('loading');
								// 分页参数
								param.page=pageNumber;
								param.pageSize=pageSize;
								param.isCount=isCount;
								param.count=$(page).pagination('options').total;
								isCount=false;
								ajaxSubmit(ctx+"/execute/sql.action",false,true,param,function(data){
									if(data.code == 200){
										var v=$this.find(".message").length;
										$this.find(".message pre").html(data.data.message);
										if(data.data.columns){
											 cls=[];
											var cd=[];
											$.each(data.data.columns,function(v,i){
												var c={
												  resizable:true
												}
												c.field=this.alias;
												c.title=this.alias;
												c.allData=this;
												cls.push(c);
											});
											
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
											$table.datagrid("loadData", cd);
											$(page).pagination('loaded');
											// 分页数据
											$(page).pagination('refresh',{	// 改变选项并刷新分页栏信息
												total: data.data.count,
												pageNumber:pageNumber,
												pageSize:pageSize,
												displayMsg:"Displaying {from} to {to} of {total} items,"+data.data.message.substr(data.data.message.indexOf(",")+1)
											});
										}	else{
											alert(data.msg);
										}
									}
								});
							}
					 }
					$page=$($table.datagrid("getPager"));
					$page.pagination("refresh",defaults);
					$page.pagination('refresh',{	// 改变选项并刷新分页栏信息
						total: data.data.count,
						displayMsg:"Displaying {from} to {to} of {total} items,"+data.data.message.substr(data.data.message.indexOf(",")+1)
					});
				}else{
					$this.find(".result .columnData").datagrid({
						fit:true,
						minimized:false,
						data:[{msg:data.data.message}],
					    columns:[[{field:'msg',title:'消息'}]],
					});
				}
			}else{
				alert(data.msg);
			}
			
		});
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

function setWindow(){
	   resize();
 }
function resize(){
	if($("#sqlWindowColumnsTable").length>0){
		$("#sqlWindowColumnsTable").datagrid("resize");
		$("#sqlWindowIndexTable").datagrid("resize");
	}
	$("#tt").tabs("resize");
	$("#dbmTable").datagrid("resize");
}




$(function(){
	init(); // 初始化
	loadTable();
	function loadTable(){
		 var param={
				    type:type,
				    connectionName:connectionName,
					catalog:getCataLog(),
					schema:getSchema(),
			};
		 var columns=[];
		ajaxSubmit(ctx+"/db/listTables.action",false,true,param,function(data){
			if(data.code == 200){
				var td=[];
				$.each(data.data,function(){
					var itemD={};
					itemD=this;
					itemD.manage="<div><a  href='#' class='text-linkbutton' data-manager='select'  data-tableName='"+itemD.tableName+"' >查询</a><a  href='#' class='text-linkbutton' data-tableName='"+itemD.tableName+"' data-manager='delete'>删除</a></div>"
						td.push(itemD);
				});
				$("#dbmTable").datagrid("loadData",td);
				$("#dbmTable").datagrid("resize");
			}else{
				alert(data.msg);
			}
		});
	}
	
	function init(){
		$("#tt").tabs({
			plain:true,
			fit:true,
			tools:[
				{
					id:"procedureAdd",
					 plain:true,
					 iconCls:'icon-add' ,
					 text:"存储过程调用"
				},
				{
					id:"procedureExport",
					 plain:true,
					 iconCls:'icon-save' ,
					 text:"存储过程导出"
				},
				{
					id:"sqlSelectWindowAdd",
					 plain:true,
					 iconCls:'icon-search' ,
					 text:"新建查询窗口"
				}
			],
			onAdd:onAdd,
			onSelect:function(title,index){
				if(index==0){
					$("#dbmTable").datagrid("resize");
				}
			}
		});
		
		 $('#tableAdd').bind('click', function(){    
		        alert('easyui');    
		    });  
		 
		 $("#sqlSelectWindowAdd").bind('click',addPanel);
		 $("#procedureAdd").bind('click',addProcedure);
		 $("#procedureExport").bind("click",addProcedureExport);
		
		var t2=null;
		$("#sqlSelectWindow").window({
			onMaximize:function(){
				 resize();
			},
			onRestore:function(){
				 resize();
			},
			onResize:function(){
				 resize();
			},
			onResize:function(width, height){
				if(t2==null){
					t2=setTimeout(function(){
						resize();
						t2=null;
			 		  },500);
				}
			}
		});
		$("#sqlSelectWindowTabs").tabs({
			onSelect:function(title,index){
			   if(index==0){
				   $("#sqlWindowColumnsTable").datagrid("resize");
				}else if(index==1){
			    	if($(this).attr("data-tablename") && type=="TABLE")
			    	   loadIndex($(this).attr("data-tablename"));
			     }else if(index == 2){
			    	 if($(this).attr("data-tablename"))
			           loadTableComment($(this).attr("data-tablename"));
			     }else if(index == 3){
			    		if($(this).attr("data-tablename"))
					    	   loadCreateSqlView($(this).attr("data-tablename"));
			     }
			     resize();
			}
		});
		var codeSetting=$.extend({},codeDefaultStyle,{readOnly:true});
		var edit=CodeMirror.fromTextArea($("#sqlWindowCreateTableSql")[0],codeSetting);
		$("#sqlWindowCreateTableSql").data("edit",edit);
		
		$('#dbmTable').datagrid({ 
			rownumbers:true,
			singleSelect:true,
			striped:true,
			fit:true,
		    columns:[[    
		        {field:'tableName',enable:true,title:'表名',width:'30%',resizable:true},    
		        {field:'comment',title:'描叙',width:'30%',resizable:true},    
		        {field:'manage',title:'操作',width:'40%',resizable:true,}    
		    ]],
		    onLoadSuccess:function(data){
		    	$(".text-linkbutton").addClass("c1").css("margin-left","16px");
		    	$(".text-linkbutton").linkbutton({width:51,height:23});
		    	$(".text-linkbutton").off("click").on("click",function(){
		    		var fun=$(this).attr("data-manager");
		    		var tableName=$(this).attr("data-tableName");
		    		// 数据删除
		    		if(fun=="delete"){
		    			var dropTable={
			    				catalog:getCataLog(),
			 					schema:getSchema(),
			 					infos:[{
						    			type:type,
						    			tableName:tableName
						    		}]
			    		}, mp={
			 				    connectionName:connectionName,
			 				    dropTable:JSON.stringify(dropTable)
			 			};
		    			layer.confirm('确定要删除表['+tableName+"]?", {
		    				  btn: ['确认','取消'] //按钮
		    				}, function(index){
		    					ajaxSubmit(ctx+"/db/dropTable.action",false,true,mp,function(data){
				    				if(data.code==200){
				    					loadTable();
				    				}else{
				    					alert(data.msg);
				    				}
				    			});
		    					layer.close(index);
		    				}, function(){
		    				  
		    				});
		    			return false;
		    			
		    		}
		    		// 表查询
		    		if(fun=="select"){
		    			var param={
		    					connectionName:connectionName,
		    					catalog:getCataLog(),
			 					schema:getSchema(),
			 					tableName:tableName
		    			};
		    			
		    			ajaxSubmit(ctx+"/db/getColumnInfo.action",false,true,param,function(data){
		    				if(data.code==200){
		    					$("#sqlWindowColumnsTable").datagrid("loadData",data.data);
		    					$("#sqlSelectWindowTabs").attr("data-tablename",tableName);
		    					$("#sqlSelectWindow").window("setTitle",tableName);
		    					$("#sqlSelectWindow").window("open");
		    					$("#sqlSelectWindowTabs").tabs("select",0);
		    					$("#sqlWindowColumnsTable").datagrid("resize");
		    				}else{
		    					alert(data.msg);
		    				}
		    			});
		    		}
		    	});
		    	$('#dbmTable').datagrid("resize");
		    },
		    onSelect:function(title,index){
		    },
		});  
		
		
		$("#sqlWindowIndexTable").datagrid({
			checkOnSelect:false,
			selectOnCheck:false,
			scrollOnSelect:false,
			singleSelect:true,
			 columns:[[    
			        {field:'indexName',title:'索引名称'},    
			        {field:'fields',title:'索引字段'},    
			        {field:'type',title:'索引类型'}
			    ]],
			    onBeforeSelect:function(){
			    	return false;
			    }
		});
		
		$("#sqlWindowColumnsTable").datagrid({
			checkOnSelect:false,
			selectOnCheck:false,
			scrollOnSelect:false,
			singleSelect:true,
			 columns:[[    
			        {field:'columnName',title:'列名称'},    
			        {field:'typeName',title:'类型'},    
			        {field:'columnSize',title:'长度',align:'right'},
			        {field:'decimalDigits',title:'精度',align:'right'},    
			        {field:'isNullable',title:'允许空',align:'center'},    
			        {field:'remarks',title:'注释'}    
			    ]],
			    onBeforeSelect:function(){
			    	return false;
			    }
		});
		
	}
	
	
	function loadIndex(tableName){
		if(tableName){
			var param={
					connectionName:connectionName,
					catalog:getCataLog(),
					schema:getSchema(),
					table:tableName
			}
			ajaxSubmit(ctx+"/db/listIndexInfo.action",false,true,param,function(data){
				if(data.code == 200){
					$("#sqlWindowIndexTable").datagrid("loadData",data.data);
					$("#sqlWindowIndexTable").datagrid("resize");
				}else{
					alert(data.msg);
				}
			});
			
		}
		
		
	}
	
	function loadTableComment(tableName){
		var param={
				connectionName:connectionName,
				catalog:getCataLog(),
				schema:getSchema(),
				table:tableName,
				type:type
		}
		ajaxSubmit(ctx+"/db/listTables.action",false,true,param,function(data){
			if(data.code == 200){
				$("#sqlWindowCommentTable input[name='tableName']").val(data.data[0].tableName);
				$("#sqlWindowCommentTable textarea[name='comment']").val(data.data[0].comment);
			}else{
				alert(data.msg);
			}
		});
		
	}
	
	function loadCreateSqlView(tableName){
		
		if(tableName){
			var param={
					connectionName:connectionName,
					catalog:getCataLog(),
					schema:getSchema(),
					name:tableName,
					type:type
			}
			ajaxSubmit(ctx+"/db/getCreateTableView.action",false,true,param,function(data){
				if(data.code == 200){
					$("#sqlWindowCreateTableSql").data("edit").setValue(data.data);
				}else{
					alert(data.msg);
				}
			});
			
		}
		
	}
	 setWindow();
	 var time=null;
	 $(window).resize(function(){
	 	  if(time==null){
	 		  time=setTimeout(function(){
	 			  setWindow();
	 			  time=null;
	 		  },300);
	 	  }
	 	});

	
	 
});