package com.icbc.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.entity.ConnectionInfo;
import com.github.jsqltool.param.CreateParam;
import com.github.jsqltool.param.TableColumnsParam;
import com.github.jsqltool.sql.create.DefaultCreateStatement;
import com.github.jsqltool.sql.create.ICreateStatement;
import com.github.jsqltool.utils.JdbcUtil;

/**
 * 测试生成create的SQL语句
 * @author yzh
 * @date 2019年7月7日
 */
public class CreateDataTest {

	public static void main(String[] args) {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		ConnectionInfo connectionInfo = builder.getConnectionInfo("", "测试MySql");
		Connection connect = JdbcUtil.connect(connectionInfo);
		ICreateStatement statement = new DefaultCreateStatement();
		List<String> createTable = statement.createTable(connect, getMySqlTable(), getTestCreateParam());
		System.out.println(createTable);
		JdbcUtil.close(connect);
	}

	private static TableColumnsParam getMySqlTable() {
		TableColumnsParam table = new TableColumnsParam();
		table.setCatalog("test");
		table.setComment("测试自动生成语句");
		table.setTableName("t1");
		return table;
	}

	private static TableColumnsParam getOracle() {
		TableColumnsParam table = new TableColumnsParam();
		table.setSchema("SCOTT");
		table.setComment("测试自动生成语句");
		table.setTableName("t1");
		return table;
	}

	private static List<CreateParam> getTestCreateParam() {
		List<CreateParam> list = new ArrayList<>();
		CreateParam id = new CreateParam();
		id.setAutoIncrement(true);
		id.setColumnName("id");
		id.setComment("测试id");
		id.setType("int");
		id.setLength(10);
		id.setPrimaryKey(true);
		list.add(id);

		CreateParam name = new CreateParam();
		name.setAutoIncrement(false);
		name.setColumnName("name");
		name.setComment("测试name");
		name.setType("varchar");
		name.setLength(30);
		list.add(name);
		return list;
	}

}
