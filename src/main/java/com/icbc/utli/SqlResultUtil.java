package com.icbc.utli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.jsqltool.result.SqlResult;
import com.github.jsqltool.result.SqlResult.Column;
import com.github.jsqltool.result.SqlResult.Record;
import com.icbc.exception.NoThatColumnException;

public class SqlResultUtil {

	private SqlResultUtil() {

	}

	public static String getMySqlUserSourceSql(String type, String dbName, String name) {
		if (StringUtils.isNoneBlank(type, dbName, name)) {
			StringBuilder sqlBuild = new StringBuilder();
			sqlBuild.append("show create ");
			sqlBuild.append(type);
			sqlBuild.append(" " + dbName + "." + name);
			return sqlBuild.toString();
		} else {
			throw new RuntimeException("参数type和name以及dbName不能为空");
		}
	}

	public static String getOralceUserSourceSql(String type, String name) {
		if (StringUtils.isNoneBlank(type, name)) {
			String sql = "select text from user_source WHERE TYPE ='" + type.toUpperCase().trim() + "' and name='"
					+ name.toUpperCase().trim() + "'";
			return sql;
		} else {
			throw new RuntimeException("参数type和name不能为空");
		}
	}

	public static int getColumnIndex(String column, SqlResult executorSql) {
		int index = -1;
		if (executorSql != null) {
			List<Column> columns = executorSql.getColumns();
			for (int i = 0; i < columns.size(); i++) {
				if (column.equalsIgnoreCase(columns.get(i).getAlias())) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	public static List<Object> getSingleColumnData(String column, SqlResult executorSql) throws NoThatColumnException {
		List<Object> result = new ArrayList<Object>();
		int index = getColumnIndex(column, executorSql);
		if (index == -1) {
			throw new NoThatColumnException("当前列：" + column + "不存在");
		}
		List<Record> records = executorSql.getRecords();
		if (records != null && !records.isEmpty()) {
			for (Record record : records) {
				result.add(record.getValues().get(index));
			}
		}
		return result;
	}

}
