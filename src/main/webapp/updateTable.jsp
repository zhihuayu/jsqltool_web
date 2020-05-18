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
		<title>DBMS</title>
		<script type="text/javascript" src="${ctx}/assets/lib/jquery/jquery1.9.1.min.js"></script>
		<script type="text/javascript" src="${ctx}/assets/lib/layer/layer.js"></script>
		<script type="text/javascript" src="${ctx}/assets/lib/zTree_v3/js/jquery.ztree.all.js"></script>
		<link rel="stylesheet" href="${ctx}/assets/lib/zTree_v3/css/metroStyle/metroStyle.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/color.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/common/css/common.css">
		<script type="text/javascript" src="${ctx}/assets/lib/easyUi/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${ctx}/assets/lib/template/template.js"></script>
		<script type="text/javascript" src="${ctx}/assets/common/js/request.js?version=1"></script>
		<script type="text/javascript" src="${ctx}/assets/common/js/datagridSetting.js"></script>
		<script type="text/javascript" src="${ctx}/assets/js/updateTable.js"></script>
	</head>
	<body>
         <div id="sqlWindow" class="easyui-layout"  data-options="fit:true">   
		    <div data-options="region:'north',title:'Run',collapsible:false,split:true" style="height:100px;">
		      <header> <a href="###" class="easyui-linkbutton c1 run" iconCls="icon-ok">Run</a></header>
		      <textarea class="sqlTextarea" style="width: 100%;height: 100%;margin: 0;border: none;display: block;" disabled="disabled"></textarea>
		    </div>   
		    <div class="resultContainer" data-options="region:'center',title:'center title',noheader:true,border:false">
		       <div  class="easyui-tabs sqlResultTabs" data-options="selected:1" style="width:100%;height:100%;">   
				  <div class="message" title="Message" data-options="closable:false" style="padding:20px;">   
				  </div> 
				  <div class="result" title="Result" data-options="closable:false" style="overflow:auto;">   
				     <table class="columnData"></table>  
				 </div>
				</div>  
		    </div>   
         </div>  
         <div id="MyConfirm" style="display: none">Dialog Content.</div>  
         
           <!-- 消息查看窗口 -->
		<div id="cellWindow"  class="easyui-window"  title="My Window" style="width:600px;height:400px"   
           data-options="modal:true,closed:'true'">
           <div class="cellText"></div>
        </div>
         
	</body>
    <script type="text/javascript">
        var ctx="${ctx}";
        var 	connectionName="${param.connectionName}";
	    var 	catalog="${param.catalog}";
	    var 	schema="${param.schema}";
	    var 	type="${param.type}";
	    var tableName="${param.tableName}";
	</script>
</html>