package com.icbc.jsqltool.model;

import java.util.List;
import java.util.Properties;

import com.github.jsqltool.entity.ConnectionInfo;
import com.github.jsqltool.model.IModel;
import com.github.jsqltool.model.ProfileModel;

public class IcbcModel implements IModel {

	private final ProfileModel model;

	public IcbcModel(Properties prop) {
		model = new ProfileModel(prop);
	}

	@Override
	public List<String> listConnection(String user) {
		return model.listConnection(user);
	}

	@Override
	public ConnectionInfo getConnectionInfo(String user, String connectionName) {
		return model.getConnectionInfo(user, connectionName);
	}

	@Override
	public boolean save(String user, String oldConnectionName, ConnectionInfo info) {
		return model.save(user, oldConnectionName, info);
	}

	@Override
	public boolean delete(String user, String connectionName) {
		return model.delete(user, connectionName);
	}

}
