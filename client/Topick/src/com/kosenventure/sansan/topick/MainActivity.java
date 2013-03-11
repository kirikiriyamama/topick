package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener{

	Context mContext;
	Button mSearchTopicBtn,mManagementKayPhraseBtn,mSettingBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		
		mContext = getApplicationContext();
		
		mSearchTopicBtn = (Button) findViewById(R.id.search_topic_btn);
		mSearchTopicBtn.setOnClickListener(this);

		mManagementKayPhraseBtn = (Button) findViewById(R.id.management_keyphrase_btn);
		mManagementKayPhraseBtn.setOnClickListener(this);
		
		mSettingBtn = (Button) findViewById(R.id.setting_btn);
		mSettingBtn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if ( v == mSearchTopicBtn ) {
			Intent intent = new Intent(mContext, SearchTopicActivity.class);
			startActivity(intent);			
		}else if (v == mManagementKayPhraseBtn ) {
			Intent intent = new Intent(mContext, ManagementKeyPhraseActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(mContext, SearchTopicActivity.class);
			startActivity(intent);
		}
	}

}
