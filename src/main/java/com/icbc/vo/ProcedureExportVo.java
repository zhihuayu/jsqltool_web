package com.icbc.vo;

import com.github.jsqltool.result.SqlResult.Column;
import com.github.jsqltool.result.SqlResult.Record;

/**
 * 用于存储过程导出相关的VO
 * @author yzh
 *
 * @date 2020年5月20日
 */
public class ProcedureExportVo {

	private String db;
	private String type;
	private String name;

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ProcedureExportVo getVo(Column column, Record record) {

		return null;
	}

}
