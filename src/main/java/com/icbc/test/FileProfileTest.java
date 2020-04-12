package com.icbc.test;

import java.io.IOException;
import java.util.Properties;

import com.github.jsqltool.entity.ConnectionInfo;
import com.github.jsqltool.profile.FileProfile;

public class FileProfileTest {

	public static void main(String[] args) throws IOException {

		ConnectionInfo builderDefaultOracleInfo = ConnectionInfo.builderDefaultOracleInfo();
		builderDefaultOracleInfo.setName("测试Oracle数据库2");
		Properties prop = new Properties();
		prop.setProperty("remarksReporting", "true");
		builderDefaultOracleInfo.setProp(prop);

		FileProfile fileProfile = new FileProfile(prop);
		fileProfile.saveConnectionInfo("", null, builderDefaultOracleInfo);

		fileProfile.delete("", "测试Oracle数据库2");

	}

}
