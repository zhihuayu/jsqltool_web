package com.icbc.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.entity.ConnectionInfo;
import com.github.jsqltool.enums.JdbcType;
import com.github.jsqltool.param.ChangeValue;
import com.github.jsqltool.param.ExecutorSqlParam;
import com.github.jsqltool.param.ProcedureParam;
import com.github.jsqltool.param.ProcedureParam.P_Param;
import com.github.jsqltool.param.SelectTableParam;
import com.github.jsqltool.param.TableColumnsParam;
import com.github.jsqltool.param.UpdateParam;
import com.github.jsqltool.result.SqlResult;
import com.github.jsqltool.result.SqlResult.Column;
import com.github.jsqltool.result.SqlResult.Record;
import com.github.jsqltool.result.TableColumnInfo;
import com.github.jsqltool.sql.update.impl.UpdateDataHandlerContent;
import com.github.jsqltool.utils.JdbcUtil;
import com.github.jsqltool.vo.UpdateResult;
import com.icbc.utli.ColumnUtil;
import com.icbc.utli.ExportUtil;
import com.icbc.vo.ProcedureExportVo;
import com.icbc.vo.Response;

@RestController
@RequestMapping("/execute")
public class ExecuteSqlAction {

	private String user = "";

	@RequestMapping(path = "/sql.action")
	public Response excuteSql(@RequestParam("connectionName") String connectionName, ExecutorSqlParam param) {
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			SqlResult executorSql = builder.executorSql(user, connectionName, param);
			return Response.OK(executorSql);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/selectTable.action")
	public Response selectTable(@RequestParam("connectionName") String connectionName, SelectTableParam param) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = builder.connect(user, connectionName);) {
			SqlResult executorSql = builder.selectTable(connect, param);
			return Response.OK(executorSql);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/getTableColumn.action")
	public Response getTableColumn(@RequestParam("connectionName") String connectionName, TableColumnsParam table) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			List<TableColumnInfo> listTable = builder.getTableColumnInfo(connect, table);
			return Response.OK(listTable);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/updateTable.action")
	public Response updateTable(@RequestParam("connectionName") String connectionName,
			@RequestParam(name = "force", defaultValue = "false") Boolean force, HttpServletRequest request) {
		String parameter = request.getParameter("updates");
		List<UpdateParam> updates = JSONArray.parseArray(parameter, UpdateParam.class);
		System.out.println(parameter);
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			UpdateResult updateData = builder.updateData(connect, updates, force);
			return Response.OK(updateData);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/delete.action")
	public Response delete(@RequestParam("connectionName") String connectionName,
			@RequestParam(name = "force", defaultValue = "false") Boolean force, HttpServletRequest request) {
		String parameter = request.getParameter("updates");
		List<UpdateParam> updates = JSONArray.parseArray(parameter, UpdateParam.class);
		System.out.println(parameter);
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			UpdateResult updateData = builder.delete(connect, updates, force);
			return Response.OK(updateData);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/insert.action")
	public Response insert(@RequestParam("connectionName") String connectionName, HttpServletRequest request) {
		String parameter = request.getParameter("updates");
		List<UpdateParam> updates = JSONArray.parseArray(parameter, UpdateParam.class);
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			UpdateResult updateData = builder.insert(connect, updates);
			return Response.OK(updateData);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/sqlTtype.action")
	public Response sqlTtype(HttpServletRequest request) {
		try {
			JdbcType[] values = JdbcType.values();

			List<Map<String, Object>> result = new ArrayList<>();
			for (JdbcType type : values) {
				Map<String, Object> map = new HashMap<>();
				map.put("label", type.name());
				map.put("value", type.TYPE_CODE);
				result.add(map);
			}
			return Response.OK(result);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/excuteCall.action")
	public Response excuteCall(@RequestParam("connectionName") String connectionName, HttpServletRequest request) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			ProcedureParam procedureParam = new ProcedureParam();
			procedureParam.setProcedureName(request.getParameter("procedureName"));
			procedureParam.setCatalog(request.getParameter("catalog"));
			procedureParam.setSchema(request.getParameter("schema"));
			procedureParam.setReturnType(JdbcType.forName(request.getParameter("returnType")));
			List<P_Param> parseArray = JSONArray.parseArray(request.getParameter("params"), P_Param.class);
			procedureParam.setParams(parseArray);
			String result = builder.executeCall(connect, procedureParam);
			return Response.OK(result);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/getProcedure.action")
	public Response getProcedure(@RequestParam("connectionName") String connectionName) {
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			ConnectionInfo connectionInfo = builder.getConnectionInfo(user, connectionName);
			ExecutorSqlParam param = new ExecutorSqlParam();
			SqlResult executorSql = null;
			if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "mysql")) {
				String sql = "show procedure status  where db !=\'sys\'";
//				String sql="select db,name,type from mysql.proc where `type` = 'PROCEDURE' ";
				param.setSql(sql);
				executorSql = builder.executorSql(connectionName, param);
			} else if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "oracle")) {
				String sql = "select distinct '' DB,type TYPE,name NAME from user_source WHERE TYPE IN ('PACKAGE','PROCEDURE')";
				param.setSql(sql);
				executorSql = builder.executorSql(connectionName, param);
			} else {
				throw new RuntimeException("不支持该数据库查询存储过程：" + connectionInfo.getDriverClassName());
			}

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			if (executorSql != null) {
				List<Record> records = executorSql.getRecords();
				Map<String, Integer> keys = getProcedureKeyIndex(executorSql.getColumns());
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

	@RequestMapping(path = "/exportProcedure.action")
	public void exportProcedure(HttpServletResponse response, @RequestParam("connectionName") String connectionName,
			ProcedureExportVo vo) {
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			ConnectionInfo connectionInfo = builder.getConnectionInfo(user, connectionName);
			ExecutorSqlParam param = new ExecutorSqlParam();
			SqlResult executorSql = null;
			if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "mysql")) {
				StringBuilder sqlBuild = new StringBuilder();
				sqlBuild.append("show create ");
				sqlBuild.append(vo.getType());
				sqlBuild.append(" " + vo.getDb() + "." + vo.getName());
				param.setSql(sqlBuild.toString());
				executorSql = builder.executorSql(connectionName, param);
			} else if (StringUtils.containsIgnoreCase(connectionInfo.getDriverClassName(), "oracle")) {
				String sql = null;
				if (vo.getType().toUpperCase().contentEquals("PACKAGE")) {
					sql = "select text from user_source WHERE TYPE ='" + vo.getType().toUpperCase().trim()
							+ "' and name='" + vo.getName().toUpperCase().trim() + "'";
					param.setSql(sql);
					executorSql = builder.executorSql(connectionName, param);
					int columnIndex = ColumnUtil.getColumnIndex("text", executorSql);
					

				} else {
					sql = "select text from user_source WHERE TYPE ='" + vo.getType().toUpperCase().trim()
							+ "' and name='" + vo.getName().toUpperCase().trim() + "'";
				}
				param.setSql(sql);
				executorSql = builder.executorSql(connectionName, param);
			} else {
				throw new RuntimeException("不支持该数据库查询存储过程：" + connectionInfo.getDriverClassName());
			}
			System.out.println(executorSql);
			StringBuilder sb = new StringBuilder();
			sb.append("\"哈哈，这是内容\"");
			ExportUtil.ExportTxt("测试导出", sb, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		Connection connect = builder.connect("测试MySQL");
		UpdateDataHandlerContent handler = UpdateDataHandlerContent.builder();
		List<UpdateParam> updates = new ArrayList<>();
		UpdateParam tb1 = new UpdateParam();
		tb1.setCatalog("test");
		tb1.setSchema("");
		tb1.setTableName("t1");
		ChangeValue id = new ChangeValue();
		id.setColumnName("id");
		id.setNewValue(1);
		id.setDataType(Types.BIGINT);
		id.setOldValue(1);
		tb1.addValue(id);
		ChangeValue name = new ChangeValue();
		name.setColumnName("name");
		name.setDataType(Types.VARCHAR);
		name.setNewValue("测试是否修改成功!");
		name.setOldValue("你好啊");
		tb1.addValue(name);
		// 日期类型
		ChangeValue date = new ChangeValue();
		date.setColumnName("td2");
		date.setDataType(Types.DATE);
		date.setNewValue("2019-06-24");
		date.setOldValue("2019-06-23");
		tb1.addValue(date);

		// datetime类型
		ChangeValue datetime = new ChangeValue();
		datetime.setColumnName("td3");
		datetime.setDataType(Types.TIMESTAMP);
		datetime.setNewValue("2019-06-24 08:32:37");
		datetime.setOldValue("2019-06-23 12:32:37");
		tb1.addValue(datetime);

		// time类型
		ChangeValue time = new ChangeValue();
		time.setColumnName("td4");
		time.setDataType(Types.TIME);
		time.setNewValue("09:32:40");
		time.setOldValue("12:32:40");
		tb1.addValue(time);
		updates.add(tb1);

		UpdateResult update = handler.update(connect, updates, false);
		System.out.println(update);

	}

}
