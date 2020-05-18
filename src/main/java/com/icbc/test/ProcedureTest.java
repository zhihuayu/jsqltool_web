package com.icbc.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.jsqltool.config.JsqltoolBuilder;
import com.github.jsqltool.enums.JdbcType;
import com.github.jsqltool.param.ProcedureParam;
import com.github.jsqltool.param.ProcedureParam.P_Param;

/**
 * 存储过程测试
 * @author yzh
 * @date 2019年8月6日
 */
public class ProcedureTest {

	public static void main(String[] args) throws SQLException {
		JsqltoolBuilder builder = JsqltoolBuilder.builder();
		try (Connection connection = builder.connect("", "测试Oracle")) {
			String executeCall = builder.executeCall(connection, getTestParam());
			System.out.println(executeCall);
		}

	}

	private static ProcedureParam getTestParam() {
		ProcedureParam p = new ProcedureParam();
		p.setProcedureName("p_test");
		List<P_Param> params = new ArrayList<>();
		P_Param p1=new P_Param();
		p1.setParamName("p_n");
		p1.setType("IN");
		p1.setValue(12);
		p1.setDataType(JdbcType.NUMERIC);
		params.add(p1);
		P_Param p2=new P_Param();
		p2.setParamName("p_out");
		p2.setType("IN OUT");
		p2.setValue(16);
		p2.setDataType(JdbcType.NUMERIC);
		params.add(p2);
		p.setParams(params);

		return p;
	}

}
