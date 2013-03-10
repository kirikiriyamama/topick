package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

public class ManagementKeyPhraseActivity extends Activity{

	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_management_key_phrase_layout);
		
		mContext = getApplicationContext();
		
	}
	
}
