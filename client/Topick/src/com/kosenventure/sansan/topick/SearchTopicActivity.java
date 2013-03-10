package com.kosenventure.sansan.topick;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.TabSpec;

public class SearchTopicActivity extends FragmentActivity {

	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_search_topic_layout);
		
		mContext = getApplicationContext();
		
		FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), R.id.content);
		
		TabSpec tabSpec1 = tabHost.newTabSpec("tab1");
	    tabSpec1.setIndicator("", getResources().getDrawable(R.drawable.ic_tab_icon_pencile_selected));
	    tabHost.addTab(tabSpec1, SearchTopicByInputFragment.class, null);

		TabSpec tabSpec2 = tabHost.newTabSpec("tab2");
	    tabSpec2.setIndicator("", getResources().getDrawable(R.drawable.ic_tab_icon_camera_selected));
	    tabHost.addTab(tabSpec2, SearchTopicByPhotoFragment.class, null);
	    
	}
}
