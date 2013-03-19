package com.kosenventure.sansan.others;

import java.io.Serializable;

public class FacebookAccount extends Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1418872356951672502L;
	public String locale,gender;
	
	public FacebookAccount(long id, String name, String locale, String gender, String prof_link, String picture_link) {
		super(id, name, prof_link, picture_link);
		this.locale = locale;
		this.gender = gender;
	}

}
