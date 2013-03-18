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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class PickUpKeyPhrasesTask extends AsyncTask<boolean[], Void, Void> {


	public static final String ACCESSTOKEN_PREFERENCE_KEY = "access_token";
	
	private Activity mActivity;
	private Context mContext;
	private SharedPreferences mPreference;  
	private SharedPreferences.Editor mEditor;  
	private ProgressDialog mProgressDialog;
	
	public PickUpKeyPhrasesTask(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
		mEditor = mPreference.edit();
	}
	
	@Override
	protected void onPreExecute(){
		mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage(getStr(R.string.dialog_ocr_mes));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
	}

	@Override
	protected Void doInBackground(boolean[]... params) {
		String data = null;
		// まずFacebookから抽出する
		if ( params[0][0] ){
			data = getJsonFromAPI(getStr(R.string.url_facebook_pick_up_key_phrase) + mPreference.getString(getStr(R.string.facebook_access_token_set_key), ""));
			if( data != null ){
				log(data);
			}
		}
		else if (params[0][1] ){
			// Twitterから抽出
			data = getJsonFromAPI(getStr(R.string.url_twitter_pick_up_key_phrase) + mPreference.getString(getStr(R.string.twitter_access_token_set_key), "") + "&secret_access_token=" + mPreference.getString(getStr(R.string.twitter_access_token_secret_set_key), ""));
			if( data != null ){
				log(data);
			}
		}
		
		return null;
//		return data;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		// ダイアログを消す
        mProgressDialog.dismiss();
	}
	
	private void log(String msg){
		Log.d("PickUpKeyPhraseTask", msg);
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
