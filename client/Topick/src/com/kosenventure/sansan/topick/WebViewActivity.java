package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

	private class Login{
		public void sendAccessToken(String token){
			Log.d("WebViewActivity", token);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view_layout);
		
//		String accessUrl = "https://www.google.co.jp/";
		String accessUrl = "http://api.sansan-plus-plus.tk";
		
		WebView webView = (WebView) findViewById(R.id.login_webview);
        webView.setWebViewClient(new WebViewClient(){});
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new Login(), Login.class.getSimpleName());
        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setAppCacheEnabled(true);
		webView.loadUrl(accessUrl);
	}

}
