package com.icbc.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.entity.ConnectionInfo;
import com.github.jsqltool.param.ExecutorSqlParam;
import com.github.jsqltool.result.SqlResult;
import com.github.jsqltool.result.SqlResult.Column;
import com.github.jsqltool.result.SqlResult.Record;
import com.icbc.utli.ExportUtil;
import com.icbc.utli.SqlResultUtil;
import com.icbc.vo.ProcedureExportVo;
import com.icbc.vo.Response;

/**
 * 和存储过程下载相关的action
 * @author yzh
 *
 * @date 2020年5月20日
 */
@RestController
@RequestMapping("/procedure")
public class ProcedureExportAction {
	private String user = "";

	@RequestMapping(path = "/get.action")
	public Response getProcedure(@RequestParam("connectionName") String connectionName) {
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			ConnectionInfo connectionInfo = builder.getConnectionInfo(user, connectionName);
			ExecutorSqlParam param = new ExecutorSqlParam();
			param.setNoPage(true);
			SqlResult sqlResult = null;
			if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "mysql")) {
				String sql = "show procedure status  where db !=\'sys\'";
//				String sql="select db,name,type from mysql.proc where `type` = 'PROCEDURE' ";
				param.setSql(sql);
				sqlResult = builder.executorSql(connectionName, param);
			} else if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "oracle")) {
				String sql = "select distinct '' DB,type TYPE,name NAME from user_source WHERE TYPE IN ('PACKAGE','PROCEDURE') order by type,name";
				param.setSql(sql);
				sqlResult = builder.executorSql(connectionName, param);
			} else {
				throw new RuntimeException("不支持该数据库查询存储过程：" + connectionInfo.getDriverClassName());
			}

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			if (sqlResult != null) {
				List<Record> records = sqlResult.getRecords();
				Map<String, Integer> keys = getProcedureKeyIndex(sqlResult.getColumns());
				if (records != null) {
					for (Record record : records) {
						Map<String, Object> r = new HashMap<String, Object>();
						// 获取所有的key
						for (String key : keys.keySet()) {
							r.put(key, record.getValues().get(keys.get(key)));
						}
						result.add(r);
					}
				}
			}
			return Response.OK(result);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	private Map<String, Integer> getProcedureKeyIndex(List<Column> columns) {
		Map<String, Integer> keys = new HashMap<String, Integer>();
		List<String> procedureColumn = getProcedureColumn();
		for (int i = 0; i < columns.size(); i++) {
			if (procedureColumn.size() > 0) {
				for (String key : procedureColumn) {
					if (key.equalsIgnoreCase(columns.get(i).getAlias())) {
						keys.put(key, i);
						procedureColumn.remove(key);
						break;
					}
				}
			}
		}
		return keys;
	}

	private List<String> getProcedureColumn() {
		List<String> result = new ArrayList<String>();
		result.addAll(Arrays.asList("db", "type", "name"));
		return result;
	}

	@RequestMapping(path = "/export.action")
	public void exportProcedure(HttpServletResponse response, @RequestParam("connectionName") String connectionName,
			ProcedureExportVo vo) {
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			ConnectionInfo connectionInfo = builder.getConnectionInfo(user, connectionName);
			StringBuilder content = new StringBuilder();
			ExecutorSqlParam param = new ExecutorSqlParam();
			param.setNoPage(true);
			if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "mysql")) {
				param.setSql(SqlResultUtil.getMySqlUserSourceSql(vo.getType(), vo.getDb(), vo.getName()));
				List<Object> procedure = SqlResultUtil.getSingleColumnData("Create Procedure",
						builder.executorSql(connectionName, param));
				if (!procedure.isEmpty()) {
					for (Object obj : procedure) {
						content.append(obj != null ? obj.toString() : "");
					}
				}
			} else if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "oracle")) {
				if (vo.getType().toUpperCase().contentEquals("PACKAGE")) {
					param.setSql(SqlResultUtil.getOralceUserSourceSql("PACKAGE", vo.getName()));
					List<Object> headPackage = SqlResultUtil.getSingleColumnData("text",
							builder.executorSql(connectionName, param));
					if (!headPackage.isEmpty()) {
						content.append("-- 包头开始\n");
						for (Object obj : headPackage) {
							content.append(obj != null ? obj.toString() : "");
						}
						content.append("-- 包头结束\n");
						param.setSql(SqlResultUtil.getOralceUserSourceSql("PACKAGE BODY", vo.getName()));
						List<Object> bodyPackage = SqlResultUtil.getSingleColumnData("text",
								builder.executorSql(connectionName, param));
						if (!bodyPackage.isEmpty()) {
							content.append("-- 包体开始\n");
							for (Object obj : bodyPackage) {
								content.append(obj != null ? obj.toString() : "");
							}
							content.append("-- 包体结束\n");
						}
					}
				} else {
					param.setSql(SqlResultUtil.getOralceUserSourceSql(vo.getType(), vo.getName()));
					List<Object> ct = SqlResultUtil.getSingleColumnData("text",
							builder.executorSql(connectionName, param));
					if (!ct.isEmpty()) {
						for (Object obj : ct) {
							content.append(obj != null ? obj.toString() : "");
						}
					}
				}
			} else {
				throw new RuntimeException("不支持该数据库查询存储过程：" + connectionInfo.getDriverClassName());
			}
			ExportUtil.exportPlain(vo.getName(), "sql", content, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
