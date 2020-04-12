package com.icbc.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.entity.ConnectionInfo;
import com.github.jsqltool.enums.DBType;
import com.github.jsqltool.param.DropTableParam;
import com.github.jsqltool.param.IndexParam;
import com.github.jsqltool.param.TableColumnsParam;
import com.github.jsqltool.param.DBObjectParam;
import com.github.jsqltool.result.ColumnInfo;
import com.github.jsqltool.result.SimpleTableInfo;
import com.github.jsqltool.result.SqlResult;
import com.github.jsqltool.result.TableColumnInfo;
import com.github.jsqltool.result.TypeInfo;
import com.github.jsqltool.utils.JdbcUtil;
import com.github.jsqltool.vo.Index;
import com.github.jsqltool.vo.IndexColumn;
import com.github.jsqltool.vo.Primary;
import com.github.jsqltool.vo.UpdateResult;
import com.icbc.vo.Response;

@RestController
@RequestMapping("/db")
public class DatabaseAction {

	private String user = "";

	/**
	 * 获取链接
	 * 
	 * @author yzh
	 * @date 2019年6月17日
	 */
	@RequestMapping(path = "/listConnection.action")
	public Response listAllConnectionName(HttpServletRequest request) {
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			List<String> data = builder.listAllConnectionName(user);
			return Response.OK(data);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL("500", e.getMessage());
		}
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月7日
	* @Description: 保存连接
	 */
	@RequestMapping(path = "/getConnectionInfo.action")
	public Response getConnectionInfo(@RequestParam("connectionName") String connectionName) {
		Response response = Response.OK("");
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			ConnectionInfo connectionInfo = builder.getConnectionInfo(user, connectionName);
			if (connectionInfo == null) {
				response.setCode(Response.DATA_ERROR);
				response.setMsg("没有找到对应的值");
			} else {
				response.setData(connectionInfo);
			}
		} catch (Exception e) {
			response.setCode(Response.SERVER_ERROR);
			response.setMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月7日
	* @Description: 保存连接
	 */
	@RequestMapping(path = "/saveConnectionInfo.action")
	public Response saveConnectionInfo(
			@RequestParam(name = "connectionName", defaultValue = "null") String connectionName, ConnectionInfo info) {
		Response response = Response.OK("");
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			boolean saveConnectionInfo = builder.saveConnectionInfo(user, connectionName, info);
			if (!saveConnectionInfo) {
				response.setCode(Response.SERVER_ERROR);
				response.setMsg("保存失败！");
			}
		} catch (Exception e) {
			response.setCode(Response.SERVER_ERROR);
			response.setMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月7日
	* @Description: 删除连接
	 */
	@RequestMapping(path = "/deleteConnectionInfo.action")
	public Response deleteConnectionInfo(@RequestParam("connectionName") String connectionName) {
		Response response = Response.OK("");
		try {
			JsqltoolBuilder builder = JsqltoolBuilder.builder();
			boolean deleteConnection = builder.deleteConnectionInfo(user, connectionName);
			if (!deleteConnection) {
				response.setCode(Response.SERVER_ERROR);
				response.setMsg("删除失败！");
			}
		} catch (Exception e) {
			response.setCode(Response.SERVER_ERROR);
			response.setMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	* @author yzh
	* @date 2019年6月30日
	*  获取数据库类型
	 */
	@RequestMapping(path = "/getDbType.action")
	public Response getDbType(@RequestParam("connectionName") String connectionName) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			DBType dbType = builder.getDbType(connect);
			return Response.OK(dbType);
		} catch (Exception e) {
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/lisCatalog.action")
	public Response listCatalog(@RequestParam("connectionName") String connectionName) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			List<String> listCatalog = builder.listCatalog(connect);
			return Response.OK(listCatalog);
		} catch (Exception e) {
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/lisSchema.action")
	public Response listSchema(@RequestParam("connectionName") String connectionName,
			@RequestParam(name = "catalog", required = false) String catalog) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			List<String> listSchema = builder.listSchema(connect, catalog);
			return Response.OK(listSchema);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/listTables.action")
	public Response listTables(@RequestParam("connectionName") String connectionName, DBObjectParam table) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			String type = StringUtils.trim(table.getType());
			if (type.equalsIgnoreCase("TABLE") || type.equalsIgnoreCase("VIEW")) {
				List<SimpleTableInfo> listTable = builder.listTable(connect, table);
				return Response.OK(listTable);
			} else if (type.equalsIgnoreCase("function") || type.equalsIgnoreCase("procedure")) {
				List<String> listProcedure = builder.listProcedure(connect, table);
				return Response.OK(listProcedure);
			} else {
				return Response.FAIL(Response.SERVER_ERROR, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "/listProcedureInfo.action")
	public Response listProcedureInfo(@RequestParam("connectionName") String connectionName, DBObjectParam table) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			String type = StringUtils.trim(table.getType());
			if (type.equalsIgnoreCase("function") || type.equalsIgnoreCase("procedure")) {
				SqlResult listProcedureInfo = builder.listProcedureInfo(connect, table);
				return Response.OK(listProcedureInfo);
			} else {
				return Response.FAIL(Response.SERVER_ERROR, "参数有误");
			}
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

	@RequestMapping(path = "/getPrimayKeyInfo.action")
	public Response getPrimaryInfo(@RequestParam("connectionName") String connectionName, IndexParam param) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			Primary primary = builder.getPrimayInfo(connect, param);
			return Response.OK(primary);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月12日
	* @Description: 获取索引信息
	 */
	@RequestMapping(path = "/listIndexInfo.action")
	public Response listIndexInfo(@RequestParam("connectionName") String connectionName, IndexParam param) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			List<Index> indexInfo = builder.listIndexInfo(connect, param);
			List<Map<String, Object>> result = new ArrayList<>();
			if (indexInfo != null && !indexInfo.isEmpty()) {
				for (Index ind : indexInfo) {
					Map<String, Object> map = new HashMap<>();
					map.put("indexName", ind.getIndexName());
					if (ind.getIsPrimary() != null && ind.getIsPrimary()) {
						map.put("type", "primary");
					} else if (ind.getNonUnique() != null && !ind.getNonUnique()) {
						map.put("type", "unique");
					} else {
						map.put("type", "normal");
					}
					StringBuilder sb = new StringBuilder();
					for (IndexColumn col : ind.getColumns()) {
						sb.append(col.getColumnName() + ",");
					}
					sb.setLength(sb.length() - 1);
					map.put("fields", sb.toString());
					result.add(map);
				}
			}
			return Response.OK(result);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月12日
	* @Description: 获取创建表的语句
	 */
	@RequestMapping(path = "/getCreateTableView.action")
	public Response getCreateTableView(@RequestParam("connectionName") String connectionName, DBObjectParam param) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			String createTableView = builder.getCreateTableView(connect, param);
			return Response.OK(createTableView);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月11日
	* @Description: 删除表信息
	 */
	@RequestMapping(path = "/dropTable.action")
	public Response dropTable(@RequestParam("connectionName") String connectionName,
			@RequestParam("dropTable") String dropTableStr) {
		DropTableParam dropTableParam = JSONObject.parseObject(dropTableStr, DropTableParam.class);
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			UpdateResult updateResult = builder.dropTable(connect, dropTableParam);
			return Response.OK(updateResult);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月11日
	* @Description: 获取具体表的列信息
	 */
	@RequestMapping(path = "/getColumnInfo.action")
	public Response getColumnInfo(@RequestParam("connectionName") String connectionName, TableColumnsParam param) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			List<ColumnInfo> columnInfo = builder.getColumnInfo(connect, param);
			return Response.OK(columnInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	* @author yzh
	* @date 2019年7月11日
	* @Description: 获取数据库支持的数据类型
	 */
	@RequestMapping(path = "/getDatabaseDataType.action")
	public Response getDatabaseDataType(@RequestParam("connectionName") String connectionName) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connect = JdbcUtil.connect(user, connectionName);) {
			Set<TypeInfo> databaseTypeInfo = builder.getDatabaseDataTypeInfo(connect);
			return Response.OK(databaseTypeInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.FAIL(Response.SERVER_ERROR, e.getMessage());
		}
	}

}
