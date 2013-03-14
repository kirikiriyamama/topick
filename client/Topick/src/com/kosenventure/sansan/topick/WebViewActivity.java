package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	public static final String ACCESSTOKEN_PREFERENCE_KEY = "access_token";
	
	private Context mContext;
	private SharedPreferences mPreference;  
	private SharedPreferences.Editor mEditor;  
	
	private class Login{
		public void sendAccessToken(String token){
			log(token);
			mEditor.putString(getStr(R.string.facebook_access_token_set_key), token);
			mEditor.commit();
			finish();
		}
	}
	
	private String getStr(int resourceId){
		return getResources().getString(resourceId);
	}
	
	private void log(String msg){
		Log.d("WebViewActivity", msg);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view_layout);
		
		mContext = getApplicationContext();
		
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
		mEditor = mPreference.edit();
		
		WebView webView = (WebView) findViewById(R.id.login_webview);
//        webView.setWebViewClient(new WebViewClient(){});
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new Login(), Login.class.getSimpleName());
        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setAppCacheEnabled(true);
		webView.loadUrl( getIntent().getStringExtra("login_url"));
	}

}
