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

import com.kosenventure.sansan.topick.R;
import com.kosenventure.sansan.topick.ShowTopicListActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class PickUpTopicTask extends AsyncTask<String[], Void, Intent>{


	final static private int SHOW_TOPIC_LIST = 500;
	
	private Activity mActivity;
	private Context mContext;
	private SharedPreferences mPreference;  
	private ProgressDialog mProgressDialog;
	
	public PickUpTopicTask(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mPreference = mContext.getSharedPreferences(getStr(R.string.access_preference_key) , Activity.MODE_PRIVATE);  
	}

	@Override
	protected void onPreExecute(){
		mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("抽出中");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
	}
	
	@Override
	protected Intent doInBackground(String[]... params) {
		String[] array = params[0];
		
		String fbData = null,twData = null;
		String url;
		// Facebook
		if( array[0] != null ){
			url = getStr(R.string.url_facebook_pick_up_topic) + mPreference.getString(getStr(R.string.facebook_access_token_set_key), "");
			url += "&target_user=" + array[0] + "&keyphrase=" + getKeyPhrases();
			log(url);
			fbData = getJsonFromAPI(url);
		}
		
		if ( array[1] != null ) {
			url = getStr(R.string.url_twitter_pick_up_topic) + mPreference.getString(getStr(R.string.twitter_access_token_set_key), "") + "&access_token_secret=" + mPreference.getString(getStr(R.string.twitter_access_token_secret_set_key), "");
			url += "&target_user=" + array[1] + "&keyphrase=" + getKeyPhrases();
			log(url);
			twData = getJsonFromAPI(url);
		}
		
		return createTopicIntent(fbData, twData);
	}
	
	private Intent createTopicIntent(String fb, String tw){
		if( ( fb == null || fb.length() == 2 ) && ( tw == null || tw.length() == 2 )) return null;
		Intent intent = null;
		JSONArray fbArray = null,twArray = null;
		Topic[] topicList;
		try {
			if(fb != null) fbArray = new JSONArray(fb);
			if(tw != null) twArray = new JSONArray(tw);
			topicList = new Topic[ ( fb != null ? fbArray.length() : 0 ) + ( tw != null ? twArray.length() : 0 ) ];
			int i = 0;
			if(fb != null){
				for (i = 0; i < fbArray.length(); i++){
					JSONObject to = fbArray.getJSONObject(i);
					topicList[i] = new Topic(to.getString("summary"), to.getString("link"), createShare(to.getJSONObject("shared_link")));
				}
			}
			if(tw != null){
				for (int j = 0; j < twArray.length(); j++,i++){
					JSONObject to = twArray.getJSONObject(j);
					topicList[i] = new Topic(to.getString("summary"), to.getString("link"), createShares(to.getJSONArray("shared_links")));
				}
			}
			
			intent = new Intent(mContext, ShowTopicListActivity.class);
			intent.putExtra("topic", topicList);
			
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
        
        if ( result == null ) Toast.makeText(mContext, "話題が見つかりませんでした。", Toast.LENGTH_SHORT).show();
        else{
	        mActivity.startActivityForResult(result, SHOW_TOPIC_LIST);
        }
	}
	
	private String[] createShare(JSONObject obj){
		String[] links = null;
		try {
			String summary = obj.getString("summary");
			if(summary != null){
				links = new String[2];
				links[0] = summary;
			}else{
				links = new String[1];
			}
			links[links.length-1] = obj.getString("link");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return links;
	}
	
	private String[] createShares(JSONArray array){
		String[] ar = new String[array.length()];
		for(int i=0;i<array.length(); i++){
			try {
				ar[i] = array.getJSONObject(i).getString("link");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ar;
	}
	
	private String getKeyPhrases(){
		AccessDb mAd = new AccessDb(mContext);
		Cursor cursor = mAd.readDb(getStr(R.string.keyphrase_table), null, null, null, "id");
		String phrase = "";
		int i = 0;
		if(cursor != null){
			do {
				if(i > 20) break;
				if (!phrase.equals("")) phrase += ",";
				phrase += cursor.getString(cursor.getColumnIndex("phrase"));
				i++;
			} while (cursor.moveToNext());
			cursor.close();
		}
		mAd.closeDb();
		phrase = phrase.replace(" ", "%20");
		phrase = phrase.replace("　", "%20");
		return phrase;
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
