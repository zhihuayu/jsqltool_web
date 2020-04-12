<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String ctx = request.getContextPath();
	request.setAttribute("ctx", ctx);
	
   String connectionName=	request.getParameter("connectionName");
   System.out.println("connectionName:"+connectionName);
	
%>
<html>
	<head>
		<meta charset="UTF-8">
		<title>DBMS</title>
		<script type="text/javascript" src="${ctx}/assets/lib/jquery/jquery1.9.1.min.js"></script>
			<script type="text/javascript" src="${ctx}/assets/lib/layer/layer.js"></script>
		<script type="text/javascript" src="${ctx}/assets/lib/zTree_v3/js/jquery.ztree.all.js"></script>
		<link rel="stylesheet" href="${ctx}/assets/lib/zTree_v3/css/metroStyle/metroStyle.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/color.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/icon.css">
		<script type="text/javascript" src="${ctx}/assets/lib/easyUi/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${ctx}/assets/common/js/request.js?version=1"></script>
		<script type="text/javascript" src="${ctx}/assets/js/dbms.js"></script>
		<style>
		  #tt .tabs-panels{
		     overflow: auto;
		  }
		</style>
	</head>
	<body>
	  
	<div id="tt" class="easyui-tabs" style="width:100%;height: auto;" data-options="tools:'#tab-tools'">
			<div title="DBM[${empty param.schema?param.catalog:param.schema}]" style="padding:10px;">
				
				<table class="easyui-datagrid"  id="dbmTable" >   
				   
				</table>  
			</div>
			<div title="My Documents"  data-options="iconCls:'icon-help',closable:true" style="padding:10px">
                        sdfsdf

    
			</div>
			<div title="Help" data-options="iconCls:'icon-help',closable:true" style="padding:10px">
				This is the help content.
			</div>
			
		   
		</div>
		<div id="tab-tools">
		        <a id="tableAdd" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" >增加表</a>  
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" onclick="addPanel()">新建查询窗口</a>
		</div>
	  
	</body>
    <script type="text/javascript">
        var ctx="${ctx}";
        var 	connectionName="${param.connectionName}";
	    var 	catalog="${param.catalog}";
	    var 	schema="${param.schema}";
	    var 	type="${param.type}";
	</script>
	<script>
	
	
	
	  
	   $(function(){
		   window.resize=function(){
			   setWindow();
		   }
		   setWindow();
		   function setWindow(){
			   $("#tt .tabs-panels").css("height",$(window).height()-36);
			   }
	   });
	
	</script>
	
</html>