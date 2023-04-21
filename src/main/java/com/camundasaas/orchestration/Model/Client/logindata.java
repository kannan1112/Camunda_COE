package com.camundasaas.orchestration.Model.Client;

import lombok.Getter;
import lombok.Setter;

public class logindata {
	private String eMail;
	private String password;

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "logindata [eMail=" + eMail + ", password=" + password + "]";
	}

	// getters and setters
}