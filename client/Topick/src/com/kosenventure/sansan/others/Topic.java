package com.kosenventure.sansan.others;

import java.io.Serializable;

public class Topic implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2884989690855075449L;
	public String[] keyphrases;
	public String[] shared_lists,shared_summary;
	public String summary,page_link,picture_link;
	
	// Facebook
	public Topic(String summary, String page_url, String[] shared_links, String[] shared_summary) {
		this.summary = summary;
		this.page_link = page_url;
		this.shared_lists = shared_links;
		this.shared_summary = shared_summary;
	}
	
	
	// Twitter
	public Topic(String summary, String page_url, String[] shared_links) {
		this.summary = summary;
		this.page_link = page_url;
		this.shared_lists = shared_links;
	}
	
	public String getKeyPhrase(){
		String keyPhrases = "";
		
		for( String key : this.keyphrases ){
			if( !keyPhrases.equals("") ) keyPhrases += ",";
			keyPhrases += "Åu"+key+"Åv";
		}
		
		if( keyPhrases.equals("") ) keyPhrases = null;
		
		return keyPhrases;
	}

}
