$(function(){
	
	  setWindow();
	var westSetting={
			title:"数据库管理",
			region:"west",
			split:true,
			onResize:function(){
			  setWindow();
			},
			tools:[
				{
					iconCls:'icon-reload',
					attrs:[
						{name:'title',value:"刷新当前节点"}
					],
					handler:function(){
						var node=getSelectNode();
						if(node==null || node.length==0){
							loadTree();
						}else if(node[0].level==1){
							removeChildrenNode(node);
							tree_level1_click(node[0]);
						}else if(node[0].level==3){
							removeChildrenNode(node);
							tree_level3_click(node[0]);
						}
					}
				},
				{
					iconCls:'icon-add',
					attrs:[
						{name:'title',value:"添加连接信息"}
					],
					handler:function(){
						$("#addConnectionWindow input[name=connectionName]").val("");
						connectWindow("新建连接");
					}
				},{
					iconCls:'icon-edit',
					attrs:[
						{name:'title',value:"编辑连接信息"}
					],
					handler:function(){
						$("#addConnectionWindow input[name]").val("")
						var nodes=getSelectNode();
						if(nodes && nodes.length==1){
							ajaxSubmit(ctx+"/db/getConnectionInfo.action",false,true,{
								connectionName:nodes[0].name
						    	},function(data){
						    		if(data.code == 200){
						    			$("#addConnectionWindow input[name]").each(function(){
						    				$(this).val(data.data[$(this).attr("name")]);
						    			});
						    			$("#addConnectionWindow input[name=connectionName]").val(nodes[0].name);
						    			connectWindow("修改连接");
						    		}else{
						    			alert(data.msg);
						    		}
						    		
						    	});
							
							
						}
						
						
					}
				},
				{
					iconCls:'icon-remove',
					attrs:[
						{name:'title',value:"删除连接信息"}
					],
					handler:function(){
						var nodes=getSelectNode();
						if(nodes && nodes.length==1){
							
							$("#confirmDialog").dialog(
							  {
								  title:"是否删除？",
								   resizable:true,
								    modal:true,
								    buttons:[{
										text:'确定',
										handler:function(){
											ajaxSubmit(ctx+"/db/deleteConnectionInfo.action",false,true,{
												connectionName:nodes[0].name
										    	},function(data){
													if(data.code == 200){
														loadTree();
													}else{
														alert("删除失败！");
													}
											});
											$("#confirmDialog").dialog("close");
										}
								    },
								    {
										text:'取消',
										handler:function(){
											$("#confirmDialog").dialog("close");
										}
								    
								    }
								    
								    ]
							  }	
							);
							
							
							
						}else if(nodes.length==0){
							alert("请先选择要删除的连接！");
						}
						
						
					}
				}]
	}
	$("#main_body").layout({
		onCollapse:function(region){
			if(region=='west'){
				 setWindow();
			}
		},
		onExpand:function(region){
             if(region=='west'){
            	 setWindow();
			}
		}
		
	});
	
	var west=$("#main_body").layout("panel","west")
	$(west).panel(westSetting);
	$(west).panel("options");
	
	var setting={
			 view: {
			      showLine: false,
			      showIcon: true,
			      selectedMulti: false,
			       dblClickExpand: false,
			      addDiyDom: false
			    },
			    callback: {
			      onClick:onClick,
			    }
	};
	
	function onClick(ev,treeId,treeNode,clickFlag){
  		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
  		var childrenNodes=zTree.getNodesByFilter(function(node){return true},false,treeNode);
		if(treeNode.level==1 &&childrenNodes.length==0){
			tree_level1_click(treeNode);
		} else if(treeNode.level==2 &&childrenNodes.length==0){
			tree_level2_click(treeNode);
		}else if(treeNode.level==3 &&childrenNodes.length==0){
			tree_level3_click(treeNode);
		}else if(treeNode.level==4 ){
			tree_level4_click(treeNode);
		}
		
		if(treeNode.level==3){
			if(treeNode.name == "TABLE" || treeNode.name == "VIEW")
			 $("#mainFrame").attr("src",ctx+"/dbms.jsp?connectionName="+treeNode.connectionName+"&type="+treeNode.name+"&catalog="+treeNode.catalog+"&schema="+treeNode.schema);
			else 
				$("#mainFrame").attr("src",ctx+"/procedure.jsp?connectionName="+treeNode.connectionName+"&type="+treeNode.name+"&catalog="+treeNode.catalog+"&schema="+treeNode.schema);	
		}
		
	}
	
	function tree_level4_click(treeNode){
		$("#mainFrame").attr("src",ctx+"/updateTable.jsp?connectionName="+treeNode.connectionName+"&type="+treeNode.getParentNode().name+"&catalog="+treeNode.catalog+"&schema="+treeNode.schema+"&tableName="+treeNode.name);
	}
	
	 function tree_level3_click(treeNode){
		 var param={
				    type:treeNode.name,
					connectionName:treeNode.connectionName,
					catalog:treeNode.catalog,
					schema:treeNode.schema,
			};
				$.ajax({
					url:ctx+"/db/listTables.action",
					type:"POST",
					timeout:30000,
					dataType:"json",
					data:param,
					success:function(data){
						if(data.code==200){
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							var newNode =[];
							$.each(data.data,function(v,i){
								var name='';
								if(this.tableName){
									name=this.tableName;
								}else{
									name=this.toString();
								}
								newNode.push({	
									connectionName:treeNode.connectionName,
									catalog:treeNode.catalog,
									schema:treeNode.schema,
									iconSkin:'table',
									name:name
									});
							});
							treeObj.addNodes(treeNode, newNode);
						}else{
							alert(data.msg);
						}
						
					}
					});
	 }
	
	function tree_level2_click(treeNode){
		var childNode=[
			{
				name:"TABLE",
				iconSkin:'table',
				connectionName:treeNode.connectionName,
				catalog: treeNode.catalog,
				schema:treeNode.schema?treeNode.schema:"",
			},{
				name:"VIEW",
				iconSkin:'view',
				connectionName:treeNode.connectionName,
				catalog: treeNode.catalog,
				schema:treeNode.schema?treeNode.schema:"",
			},{
				name:"PROCEDURE",
				iconSkin:'view',
				connectionName:treeNode.connectionName,
				catalog: treeNode.catalog,
				schema:treeNode.schema?treeNode.schema:"",
			},{
				name:"FUNCTION",
				iconSkin:'view',
				connectionName:treeNode.connectionName,
				catalog: treeNode.catalog,
				schema:treeNode.schema?treeNode.schema:"",
			}
		];
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.addNodes(treeNode, childNode);
	}
	
	
	function tree_level1_click(treeNode){
		var param={
				connectionName:treeNode.name,
		};
		$.ajax({
			url:ctx+"/db/lisCatalog.action",
			type:"POST",
			timeout:30000,
			dataType:"json",
			data:param,
			success:function(data){
				if(data.code == 200){
					if(data.data.length>0){
						var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
						var newNode =[];
						$.each(data.data,function(v,i){
							newNode.push({connectionName:treeNode.name,name:i,catalog:i,	iconSkin:'database'});
						});
						treeObj.addNodes(treeNode, newNode);
					}else{
						var param={connectionName:treeNode.name};
						$.ajax({
							url:ctx+"/db/lisSchema.action",
							type:"POST",
							timeout:30000,
							dataType:"json",
							data:param,
							success:function(data){
								if(data.code == 200){
									$.each(data.data,function(v,i){
										var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
										var newNode =[];
											newNode.push({connectionName:treeNode.name,name:i,schema:i,iconSkin:'database',});
										treeObj.addNodes(treeNode, newNode);
									});
								}else{
									alert(data.msg);
								}
							}});
					}
				}else{
					alert(data.msg);
				}
				
			}
		});
	}
	
	
	function getSelectNode(){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getSelectedNodes();
		return nodes;
	}
	
	function removeChildrenNode(nodes){
		if(nodes&& nodes.length>0){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			treeObj.removeChildNodes(nodes[0]);
		}
	}
	
	
	loadTree();
	function loadTree(){
		$.ajax({
			url:ctx+"/db/listConnection.action",
			type:"POST",
			timeout:30000,
			dataType:"json",
			success:function(data){
				var childrens=[];
				var zNodes=[{
					name:"连接池", open:true, children:childrens
				}];
				if(data.code == 200){
					$.each(data.data,function(v,i){
						childrens.push({name:i,iconSkin:'connection'});
					});
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				}
			
			}
		});
	}
	
	function connectWindow(title){
		
		$("#addConnectionWindow").dialog({    
			title:title,
		    width:392,    
		    height:275,
		    resizable:true,
		    modal:true,
		    buttons:[{
				text:'保存',
				handler:function(){
					var validateSuccess=true;
					var param={};
					$("#addConnectionWindow .easyui-validatebox").each(function(){
						$(this).validatebox("validate");
						if($(this).hasClass("validatebox-invalid")){
							validateSuccess=false;
							return false;
						}
						if($(this).val()){
							param[$(this).attr("name")]=$(this).val();
						}
					});
					if($("#addConnectionWindow input[name=connectionName]").val()){
						param.connectionName	=$("#addConnectionWindow input[name=connectionName]").val();		
					}
					if(validateSuccess){
						ajaxSubmit(ctx+"/db/saveConnectionInfo.action",false,true,param,function(data){
							if(data.code == 200){
								$("#addConnectionWindow").dialog("close");
								loadTree();
							}else{
								alert(data.msg);
							}
						});
					}else{
						alert("请先填写表单！");
					}
				}
			},{
				text:'关闭',
				handler:function(){
					$("#addConnectionWindow").dialog("close");
				}
			}]
		});  
	}
	
	  $(window).resize(function(){
		  setWindow();
		});
	  
	  var time=null;
	  function setWindow(){
		  if(time==null){
			  time=setTimeout(function(){
				  $("#treeDemo").css("height",($("#treeContainer").height()-10));
				  $("#mainFrame").css("width",($("#containerMainFrame").width()));
				  $("#mainFrame").css("height",($("#containerMainFrame").height()-10));
				  time=null;
			  },300);
		  }
	  }	
	
	

});