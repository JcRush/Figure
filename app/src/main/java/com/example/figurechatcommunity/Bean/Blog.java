package com.example.figurechatcommunity.Bean;

import cn.bmob.v3.BmobObject;

public class Blog extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String brief;
	
	private UserInfo user;

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
}
