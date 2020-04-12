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
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
		<title>DBMS</title>
		<script type="text/javascript" src="${ctx}/assets/lib/jquery/jquery1.9.1.min.js"></script>
		<script type="text/javascript" src="${ctx}/assets/lib/layer/layer.js"></script>
		<script src="${ctx}/assets/lib/codemirror/lib/codemirror.js"></script>
		<link rel="stylesheet" href="${ctx}/assets/lib/codemirror/lib/codemirror.css">
		<script src="${ctx}/assets/lib/codemirror/mode/sql/sql.js"></script>
			
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/color.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/lib/easyUi/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/assets/common/css/common.css">
		<script type="text/javascript" src="${ctx}/assets/lib/easyUi/jquery.easyui.min.js"></script>
	
		<script type="text/javascript" src="${ctx}/assets/lib/template/template.js"></script>
		<script type="text/javascript" src="${ctx}/assets/common/js/request.js?version=1"></script>
	    <script type="text/javascript" src="${ctx}/assets/common/js/datagridSetting.js"></script>
		<script type="text/javascript" src="${ctx}/assets/js/procedure.js?version=7"></script>
		<script id="sqlTabsHead" type="text/html">
                 <div class="sqlWindow" title="SQL[${empty param.schema?param.catalog:param.schema}]"  data-options="closable:true" >
                 </div>
        </script>
		<script id="sqlWindow" type="text/html">
                <div class="easyui-layout"  data-options="fit:true">   
					    <div data-options="region:'north',title:'Run',collapsible:false,split:true" style="height:100px;">
					      <header > <a href="###" class="easyui-linkbutton c1 run" iconCls="icon-ok">Run</a></header>
					      <textarea class="sql" style="width: 100%;height: 100%;margin: 0;border: none;display: block;">  select * from xxx;</textarea>
					    </div>   
					    <div data-options="region:'center',title:'center title',noheader:true,border:false">
					       <div class="easyui-tabs sqlWindowTabs" data-options="selected:1,fit:true">   
							    <div class="message" title="Message" data-options="closable:false" style="display:none;">   
							        <pre></pre>
							    </div>   
							    <div class="result" title="Result" data-options="closable:false" style="overflow:auto;display:none;">   
                                    <table class="columnData"></table>  
							    </div>   
							</div>  
					    </div>   
               </div>  
        </script>
        
        <script id="procedureParam" type="text/html">
             <div class="procedureParam">
                <div class="input_g" style="vertical-align:middle;text-align:center;">
                            <span class="input_span" style="font-weight:bold;margin-right:26px;">参数设置</span>
                </div>
 				<div class="input_g">
                            <span class="input_span">参数名称：</span><input type="text"  name="paramName" class="easyui-textbox" />
                </div>
                <div class="input_g">
                            <span class="input_span">参数位置：</span><input type="text"  name="paramIndex" class="easyui-numberbox"  /> <span class="input_span" style="width:148px;">参数位置和名称二选一</span>
                 </div>
                 <div class="input_g">
                            <span class="input_span">数据类型：</span>
                             <select name="dataType" class="easyui-combobox" style="width:171px;"></select>
                  </div>
                   <div class="input_g">
                            <span class="input_span">参数类型：</span>
                             <select name="type" class="easyui-combobox" style="width:171px;">
                               <option value="IN">IN</option>
                               <option value="OUT">OUT</option>
                               <option value="IN OUT">IN OUT</option>
                             </select>
                  </div>
                 <div class="input_g">
                            <span class="input_span">参数值：</span><input type="text"  name="value" class="easyui-textbox" />
                  </div>
                  <span class="icon icon-remove" title="删除该参数" onclick="deleteParam(this)"></span>

			 </div>
        </script>
        
        <script id="procedureWindow" type="text/html">
          <div class="easyui-layout"  data-options="fit:true">   
					    <div data-options="region:'north',title:'Run',collapsible:false,split:true" style="height:85%;">
					      <header > <a href="###" class="easyui-linkbutton c1 procedureRun" onclick="procedureRun(this)" iconCls="icon-ok">Run</a></header>
					      <div class="input_g">
                            <span class="input_span">存储过程名称：</span><input type="text"  name="procedureName" class="easyui-textbox" data-options="required:true,validateOnCreate:false,missingMessage:'存储过程名称不能为空！'" />
                             <span class="icon icon-add" title="添加参数" onclick="addProcedureParam(this)"></span>
                          </div>
                           <div class="input_g">
                            <span class="input_span">返回值类型：</span><select name="returnType" style="width:171px;"></select><span class="input_span" style="width:98px;">默认无返回值</span>
                           </div>
                           <div class="procedureParamContainer"></div>
                        </div>   
					    <div data-options="region:'center',title:'center title',noheader:true,border:false">
					       <div class="messageContainer">
                             <pre></pre>
                           </div>
					    </div>   
               </div>  
       </script>
        
		<style>
		       .input_g{
				    margin: 12px auto;;
				  }
			  .input_g .input_span,.input_g  .control-label{
			     display: inline-block;
			     text-align: right; 
			      width: 36px;
			      vertical-align: top;
			    }
			    .input_g .form-control{
			      display: inline-block;
			    }
			     .procedureWindow  .input_span {width:178px;height: 28px;line-height: 28px;}
			    .procedureWindow .icon{cursor: pointer;display: inline-block;width: 26px;height: 16px;position: relative;top:3px;}
			    .procedureWindow .procedureParamContainer .input_span {width:101px;height: 28px;line-height: 28px;}
			    .procedureWindow .procedureParam{position: relative;display: inline-block;float: left;margin-left: 12px;margin-top: 12px;width: 435px;vertical-align: middle;background-color: #ccc;border: 1px solid #eee;border-radius: 15px }
			    .procedureWindow .procedureParam .icon{position: absolute;top:12px;right: 12px;}
			    
			    
			  #tt .tabs-panels{
			     overflow: auto;
			  }
		</style>
	</head>
	<body >
	   <div id="tt" class="easyui-tabs"   >
			<div title="DBM[${empty param.schema?param.catalog:param.schema}]"  data-options="closable:false">
				<table  id="dbmTable" class="easy-table"> </table>  
			</div>
		</div>
		<!-- SQL查询窗口 -->
		<div id="sqlSelectWindow" class="easyui-window" title="My Window" style="width:600px;height:400px;"   
            data-options="iconCls:'icon-ok',modal:true,closed:'true'">   
		     <div id="sqlSelectWindowTabs" class="easyui-tabs" style="height: 100%;">   
			    <div title="columns"   style="padding:20px;display:none;">   
			        <table id="sqlWindowColumnsTable"></table>  
			    </div>   
			    <div title="index" data-options="closable:false" style="overflow:auto;padding:20px;display:none;">   
			         <table id="sqlWindowIndexTable"></table>  
			    </div>   
			    <div title="comment" data-options="closable:false" style="overflow:auto;padding:20px;display:none;">   
			        <div id="sqlWindowCommentTable">
				         <div class="input_g">
				           <label  class="control-label">表名:</label>
				           <input name="tableName" type="text" class="form-control" disabled="disabled"/>
				         </div>
				          <div class="input_g">
				           <label  class="control-label">注释:</label>
				           <textarea name="comment"  id="" cols="30" rows="10" class="form-control"  style="margin: 0px; width: 384px; height: 76px;" disabled="disabled"></textarea>
				         </div>
			         </div>
			    </div>   
			    <div title="DDL" data-options="closable:false" style="display:none;">   
			           <textarea id="sqlWindowCreateTableSql" class="sql sqlContent"  style="width: 100%;height: 100%;padding:6px;margin: 0;border: none;display: block;"></textarea>
			    </div>   
			</div>  
        </div>  
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
	</script>
</html>