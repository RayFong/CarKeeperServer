package org.judking.carkeeper.src.model;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Serializable {
	private String user_id;
	private Date reg_time;
	private String user_name;
	private String user_passwd;
	private String private_token;
	
	public UserModel()		{
		
	}
	
	public UserModel(String username, String passwd)			{
		this.user_name = username;
		this.user_passwd = passwd;
	}
	
	@Override
	public String toString() {
		return "UserModel [user_id=" + user_id + ", reg_time=" + reg_time
				+ ", user_name=" + user_name + ", user_passwd=" + user_passwd
				+ ", private_token=" + private_token + "]";
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getReg_time() {
		return reg_time;
	}
	public void setReg_time(Date reg_time) {
		this.reg_time = reg_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_passwd() {
		return user_passwd;
	}
	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}
	public String getPrivate_token() {
		return private_token;
	}
	public void setPrivate_token(String private_token) {
		this.private_token = private_token;
	}

	
}
