package com.kosenventure.sansan.others;

import java.io.Serializable;

public class FacebookAccount extends Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1418872356951672502L;
	public int id;
	public String name,locale,gender,prof_url,picture_url;
	
	public FacebookAccount(int id, String name, String locale, String gender, String prof_url, String picture_url) {
		this.id = id;
		this.name = name;
		this.locale = locale;
		this.gender = gender;
		this.prof_url = prof_url;
		this.picture_url = picture_url;
	}

}
