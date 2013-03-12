package com.kosenventure.sansan.others;

public class Topic {
	
	public String[] keyphrases;
	public String image;
	public String content;
	
	public Topic(String[] keyphrase, String image, String content) {
		this.keyphrases = keyphrase;
		this.image = image;
		this.content = content;
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
