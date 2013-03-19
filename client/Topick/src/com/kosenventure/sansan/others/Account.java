package com.kosenventure.sansan.others;

import java.io.Serializable;

public class Account implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6972914505489359562L;
	public long id;
	public String name,prof_link,picture_link;
	
	public Account(long id, String name, String prof_link, String picture_link){
		this.id = id;
		this.name = name;
		this.prof_link = prof_link;
		this.picture_link = picture_link;
	}
	
}
