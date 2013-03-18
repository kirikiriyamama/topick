package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SettingActivity extends MyActivity implements OnClickListener{


	private static final int WEB_VIEW_ACTIVITY = 200;
	
	private ImageView mBackBtn;
	private Button mLaunchManagementKayPhraseBtn,mLoginTwitterBtn,mLoginFacebookBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_layout);

		mBackBtn = (ImageView) findViewById(R.id.btn_back_setting);
		mBackBtn.setOnClickListener(this);
		
		mLaunchManagementKayPhraseBtn = (Button) findViewById(R.id.btn_launch_management_kay_phrase);
		mLaunchManagementKayPhraseBtn.setOnClickListener(this);
		
		mLoginFacebookBtn = (Button) findViewById(R.id.btn_login_facebook);
		mLoginFacebookBtn.setOnClickListener(this);
		
		mLoginTwitterBtn = (Button) findViewById(R.id.btn_login_twitter);
		mLoginTwitterBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if ( v == mBackBtn ) {
			finish();
		}
		else if ( v == mLaunchManagementKayPhraseBtn ) {
			Intent intent = new Intent(mContext, ManagementKeyPhraseActivity.class);
			startActivity(intent);
		}
		else if ( v == mLoginFacebookBtn ) {
			Intent intent = new Intent(mContext, WebViewActivity.class);
			intent.putExtra("login_url", getStr(R.string.url_facebook_login));
			startActivityForResult(intent, WEB_VIEW_ACTIVITY);
		}
		else if ( v == mLoginTwitterBtn ) {
			Intent intent = new Intent(mContext, WebViewActivity.class);
			intent.putExtra("login_url", getStr(R.string.url_twitter_login));
			startActivityForResult(intent, WEB_VIEW_ACTIVITY);
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	if( requestCode == WEB_VIEW_ACTIVITY ){
    		if( resultCode == RESULT_OK ){
    			toast(data.getExtras().getString("name") + "ÇÃîFèÿÇ…ê¨å˜ÇµÇ‹ÇµÇΩÅB");
    		}
    	}
    }
}
