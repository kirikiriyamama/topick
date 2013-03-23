package com.kosenventure.sansan.others;

import java.io.Serializable;

public class Topic implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2884989690855075449L;
	public String keyphrase;
	public String[] shared_lists,shared_summary;
	public String summary,page_link,picture_link;
	
	// Twitter
	public Topic(String keyphrase, String summary, String page_url, String[] shared_links) {
		this.keyphrase = keyphrase;
		this.summary = summary;
		this.page_link = page_url;
		this.shared_lists = shared_links;
	}
	
}
