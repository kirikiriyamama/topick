package com.kosenventure.sansan.topick;

public class Topic {
	
	String[] keyphrases;
	String image,content;
	
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
