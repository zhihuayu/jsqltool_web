<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String ctx = request.getContextPath();
	request.setAttribute("ctx", ctx);
%>
<html>
<head>
<meta charset="UTF-8">
<script>
  var ctx="${ctx}";
</script>
<script type="text/javascript" src="${ctx}/assets/lib/jquery/jquery1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/assets/lib/layer/layer.js"></script>

<script type="text/javascript" src="${ctx}/assets/lib/zTree_v3/js/jquery.ztree.all.js"></script>
<link rel="stylesheet" href="${ctx}/assets/lib/zTree_v3/css/metroStyle/metroStyle.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/assets/css/index.css">
<script type="text/javascript" src="${ctx}/assets/lib/easyUi/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/assets/common/js/request.js?version=1"></script>
<script src="${ctx }/assets/js/index.js"></script>
<title>LookDBs</title>
</head>
<body  class="easyui-layout" id="main_body">
		<div data-options="region:'west',split:true" title="数据库管理" style="width:286px;" id="treeContainer">
		   <ul id="treeDemo" class="ztree" style="overflow: auto;"></ul>
		</div>
		<div data-options="region:'center',title:'主窗口',iconCls:'icon-ok'"  id="containerMainFrame">
		     <iframe src="javascript:" id="mainFrame" frameborder="0"  frameborder="0" scrolling="auto" marginwidth="0" marginheight="0"  ></iframe>
		</div>
		
		<div id="addConnectionWindow" style="display: none">
		   <div class="addContainer">
				<div class="input_g">
				  <input type="text" name="connectionName"  style="display: none"/>
				  <span class="input_span"> 连接名：</span><input type="text" name="name" class="easyui-validatebox" data-options="required:true,validateOnCreate:false,missingMessage:'连接名不能为空！'" /> 
				 </div>
				 <div class="input_g">
				 <span class="input_span">  driverClassName：</span><input type="text"  name="driverClassName" class="easyui-validatebox" data-options="required:true,validateOnCreate:false,missingMessage:'driverClassName不能为空！'" />
				  </div> 
				 <div class="input_g">
				  <span class="input_span">  url：</span><input type="text"  name="url" class="easyui-validatebox" data-options="required:true,validateOnCreate:false,missingMessage:'url不能为空！'" />  
				 </div> 
				  <div class="input_g">  
				   <span class="input_span"> userName：</span><input type="text"  name="userName" class="easyui-validatebox" data-options="required:true,validateOnCreate:false,missingMessage:'userName不能为空！'" /> 
				 </div>
				 <div class="input_g">
				  <span class="input_span"> password：</span><input type="password"  name="password" class="easyui-validatebox" data-options="required:true,validateOnCreate:false,missingMessage:'password不能为空！'" />   
				 </div>
			 </div>
		</div>  
		<div id="confirmDialog" style="display: none;">确定删除该数据库连接信息？</div>  
</body>
</html>