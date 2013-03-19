package com.kosenventure.sansan.others;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kosenventure.sansan.topick.FoundAccountListActivity;
import com.kosenventure.sansan.topick.R;

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
	private ProgressDialog mProgressDialog;
	
	public SearchUserTask(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
	}
	
	@Override
	protected void onPreExecute(){
		mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("検索中");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
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
		}
		
		// Twitterから抽出
		if( !array[6].equals("") ){
			url = getStr(R.string.url_twitter_search_user) + mPreference.getString(getStr(R.string.twitter_access_token_set_key), "") + "&access_token_secret=" + mPreference.getString(getStr(R.string.twitter_access_token_secret_set_key), "");
			url += "&screen_name="+array[6];
			log(url);
			twData = getJsonFromAPI(url);
		}
		
		return createAccountIntent(fbData, twData);
	}	

	private Intent createAccountIntent(String fb, String tw){
		if ( fb == null && tw == null ) return null;
		
		Intent intent = null;
		JSONArray fbArray = null,twArray = null;
		FacebookAccount[] fbAc = null;
		TwitterAccount twAc = null;
		try {
			if(fb != null) fbArray = new JSONArray(fb);
//			if(tw != null) twArray = new JSONArray(tw);
			if(fb != null){
				fbAc = new FacebookAccount[fbArray.length()];
				for (int i = 0; i < fbArray.length(); i++){
					JSONObject ac = fbArray.getJSONObject(i);
					fbAc[i] = new FacebookAccount(ac.getInt("id"), ac.getString("name"), ac.getString("locale"), ac.getString("gender"), ac.getString("link"), ac.getString("picture"));
				}
			}
//			if(tw != null) for (int j = 0; j < twArray.length(); j++,i++) keyPhraseArray[i] = twArray.getString(j);
			
			intent = new Intent(mContext, FoundAccountListActivity.class);
			intent.putExtra("facebook", fbAc);
			intent.putExtra("twitter", twAc);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return intent;
	}
	
	@Override
	protected void onPostExecute(Intent result) {
		super.onPostExecute(result);
		
		// ダイアログを消す
        mProgressDialog.dismiss();
        
        if ( result == null ) Toast.makeText(mContext, "ユーザが見つかりませんでした。", Toast.LENGTH_SHORT).show();
        else{
	        mActivity.startActivity(result);
        }
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
