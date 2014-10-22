package ch.hsr.inte.model;

import java.io.Serializable;

public class User implements Serializable {


	private static final long serialVersionUID = 1L;

	private String alias;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String password;
}
