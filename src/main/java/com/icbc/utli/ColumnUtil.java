package com.icbc.utli;

import java.util.List;

import com.github.jsqltool.result.SqlResult;
import com.github.jsqltool.result.SqlResult.Column;
import com.icbc.exception.NoThatColumnException;

public class ColumnUtil {

	private ColumnUtil() {

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

}
