package com.kosenventure.sansan.others;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.kosenventure.sansan.topick.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class PickUpKeyPhrasesTask extends AsyncTask<Void, Object, Object> {


	public static final String ACCESSTOKEN_PREFERENCE_KEY = "access_token";
	
	private Activity mActivity;
	private Context mContext;
	private SharedPreferences mPreference;  
	private SharedPreferences.Editor mEditor;  
	
	public PickUpKeyPhrasesTask(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
		mEditor = mPreference.edit();
	}

	@Override
	protected Object doInBackground(Void... params) {
		// まずFacebookから抽出する
		String data = getJsonFromAPI(getStr(R.string.url_facebook_pick_up_key_phrase));
		if( data != null ){
			
		}
		// Twitterから抽出
		
		
		return null;
	}

	private String getJsonFromAPI(String url){
        HttpClient httpClient = new DefaultHttpClient();
        StringBuilder uri = new StringBuilder(url);
        HttpGet request = new HttpGet(uri.toString());
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(request);
        } catch (Exception e) {
            Log.d("JSONSampleActivity", "Error Execute");
            return null;
        }
         
        int status = httpResponse.getStatusLine().getStatusCode();
        String data = null;
         
        if (HttpStatus.SC_OK == status) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(outputStream);
                data = outputStream.toString(); // JSONデータ
            } catch (Exception e) {
                  Log.d("JSONSampleActivity", "Error");
            }
        } else {
            Log.d("JSONSampleActivity", "Status" + status);
            return null;
        }
        
        return data;
	}
	
	private String getStr(int resourceId){
		return mContext.getResources().getString(resourceId);
	}
}
