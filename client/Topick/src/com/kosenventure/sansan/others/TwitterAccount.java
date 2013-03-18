package com.kosenventure.sansan.others;

import java.io.Serializable;

public class TwitterAccount extends Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3971777162481125711L;
	public int id;
	public String screen_name,name,description,prof_link,picture_link;
	
	public TwitterAccount(int id, String screen_name, String name, String description, String prof_link, String picture_link) {
		this.id = id;
		this.screen_name = screen_name;
		this.name = name;
		this.description = description;
		this.prof_link = prof_link;
		this.picture_link = picture_link;
	}

}
