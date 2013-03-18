package com.kosenventure.sansan.others;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.kosenventure.sansan.topick.R;
import com.kosenventure.sansan.topick.SelectPickUpKeyPhraseActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SearchUserTask extends AsyncTask<String[], Void, Intent> {

	public static final String ACCESSTOKEN_PREFERENCE_KEY = "access_token";
	
	private Activity mActivity;
	private Context mContext;
	private SharedPreferences mPreference;  
	private SharedPreferences.Editor mEditor;  
	private ProgressDialog mProgressDialog;
	
	public SearchUserTask(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
		mEditor = mPreference.edit();
	}
	
	@Override
	protected void onPreExecute(){
//		mProgressDialog = new ProgressDialog(mActivity);
//        mProgressDialog.setMessage(getStr(R.string.dialog_ocr_mes));
//        mProgressDialog.setIndeterminate(false);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
	}
	
	@Override
	protected Intent doInBackground(String[]... params) {
		String[] array = params[0];
		String fbData = null,twData = null;
		// まずFacebookから抽出する
		String url = getStr(R.string.url_facebook_search_user) + mPreference.getString(getStr(R.string.facebook_access_token_set_key), "");
		String purl = url;
		if( !array[0].equals("") && !array[1].equals("") ){
			url += "&first_name_en=" + array[0] + "&last_name_en=" + array[1];
		}
		if( !array[2].equals("") && !array[3].equals("") ){
			url += "&first_name_kana=" + array[2] + "&last_name_kana=" + array[3];
		}		
		if( !array[4].equals("") && !array[5].equals("") ){
			url += "&first_name_kanji=" + array[4] + "&last_name_kanji=" + array[5];
		}

		if( !url.equals(purl) ) {
			log(url);
			fbData = getJsonFromAPI(url);
			log(fbData);
		}
		
		// Twitterから抽出
		if( !array[6].equals("") ){
			url = getStr(R.string.url_twitter_search_user) + mPreference.getString(getStr(R.string.twitter_access_token_set_key), "") + "&access_token_secret=" + mPreference.getString(getStr(R.string.twitter_access_token_secret_set_key), "");
			url += array[6];
			log(url);
			twData = getJsonFromAPI(url);
			log(twData);
		}
		
		return null;
	}	

	@Override
	protected void onPostExecute(Intent result) {
		super.onPostExecute(result);
		
		// ダイアログを消す
//        mProgressDialog.dismiss();
        
//        if ( result == null ) Toast.makeText(mContext, "キーフレーズが見つかりませんでした", Toast.LENGTH_SHORT).show();
//        else{
//	        Intent intent = new Intent(mContext, SelectPickUpKeyPhraseActivity.class);
//	        intent.putExtra("key_phrase", result);
//	        intent.putExtra("date", getDate());
//	        mActivity.startActivityForResult(intent, 200);
//        }
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
	
	private void log(String msg){
		Log.d("SearchUserTask", msg);
	}

	private String getStr(int resourceId){
		return mContext.getResources().getString(resourceId);
	}
	
}
