package com.kosenventure.sansan.others;

import java.io.Serializable;

public class TwitterAccount extends Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3971777162481125711L;
	public String screen_name,description;
	
	public TwitterAccount(int id, String screen_name, String name, String description, String prof_link, String picture_link) {
		super(id, name, prof_link, picture_link);
		this.screen_name = screen_name;
		this.description = description;
	}

}
