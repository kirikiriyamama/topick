package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends MyActivity {

	public static final String ACCESSTOKEN_PREFERENCE_KEY = "access_token";
	
	private Context mContext;
	private SharedPreferences mPreference;  
	private SharedPreferences.Editor mEditor;  
	
	private class Login{
		public void sendFacebookAccessToken(String token){
			log(token);
			mEditor.putString(getStr(R.string.facebook_access_token_set_key), token);
			mEditor.commit();
			Intent intent = new Intent();
			intent.putExtra("name", "Twitter");
			setResult(RESULT_OK, intent);
			finish();
		}
		public void sendTwitterAccessToken(String token, String token_secret){
			log(token);
			mEditor.putString(getStr(R.string.twitter_access_token_set_key), token);
			mEditor.putString(getStr(R.string.twitter_access_token_secret_set_key), token_secret);
			mEditor.commit();
			Intent intent = new Intent();
			intent.putExtra("name", "Facebook");
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view_layout);
		
		mContext = getApplicationContext();
		
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
		mEditor = mPreference.edit();
		
		WebView webView = (WebView) findViewById(R.id.login_webview);
        webView.setWebViewClient(new WebViewClient(){});
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new Login(), Login.class.getSimpleName());
        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setAppCacheEnabled(true);
		webView.loadUrl( getIntent().getStringExtra("login_url"));
	}

}
