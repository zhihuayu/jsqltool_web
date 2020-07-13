package com.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.param.ChangeValue;
import com.github.jsqltool.param.UpdateParam;
import com.github.jsqltool.sql.update.impl.UpdateDataHandlerContent;
import com.github.jsqltool.vo.UpdateResult;

public class TestExecuteSqlAction {
	
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
