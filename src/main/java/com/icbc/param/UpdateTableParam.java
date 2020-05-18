package com.icbc.param;

import java.util.List;

import com.github.jsqltool.param.UpdateParam;

public class UpdateTableParam {

	private String connectionName;
	private List<UpdateParam> updates;
	private Boolean force;

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public List<UpdateParam> getUpdates() {
		return updates;
	}

	public void setUpdates(List<UpdateParam> updates) {
		this.updates = updates;
	}

	public Boolean getForce() {
		return force;
	}

	public void setForce(Boolean force) {
		this.force = force;
	}

}
