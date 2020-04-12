package com.icbc.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.enums.JdbcType;
import com.github.jsqltool.param.ChangeValue;
import com.github.jsqltool.param.ExecutorSqlParam;
import com.github.jsqltool.param.ProcedureParam;
import com.github.jsqltool.param.ProcedureParam.P_Param;
import com.github.jsqltool.param.SelectTableParam;
import com.github.jsqltool.param.TableColumnsParam;
import com.github.jsqltool.param.UpdateParam;
import com.github.jsqltool.result.SqlResult;
import com.github.jsqltool.result.TableColumnInfo;
import com.github.jsqltool.sql.update.impl.UpdateDataHandlerContent;
import com.github.jsqltool.utils.JdbcUtil;
import com.github.jsqltool.vo.UpdateResult;
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

	public static void main(String[] args) throws SQLException {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		Connection connect = builder.connect("测试MySQL");
		UpdateDataHandlerContent handler =  UpdateDataHandlerContent.builder();
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
