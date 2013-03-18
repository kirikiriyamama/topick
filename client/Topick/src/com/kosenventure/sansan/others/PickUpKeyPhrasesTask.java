package com.kosenventure.sansan.others;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

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

public class PickUpKeyPhrasesTask extends AsyncTask<boolean[], Void, String[]> {


	public static final String ACCESSTOKEN_PREFERENCE_KEY = "access_token";
	
	private Activity mActivity;
	private Context mContext;
	private SharedPreferences mPreference;  
	private ProgressDialog mProgressDialog;
	
	public PickUpKeyPhrasesTask(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mPreference = mContext.getSharedPreferences(ACCESSTOKEN_PREFERENCE_KEY , Activity.MODE_PRIVATE);  
	}
	
	@Override
	protected void onPreExecute(){
		mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("���o��");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
	}

	@Override
	protected String[] doInBackground(boolean[]... params) {
		String fbData = null,twData = null;
		// �܂�Facebook���璊�o����
		if ( params[0][0] ){
			String url = getStr(R.string.url_facebook_pick_up_key_phrase) + mPreference.getString(getStr(R.string.facebook_access_token_set_key), "");
			fbData = getJsonFromAPI(url);
		}
		
		if (params[0][1] ){
			// Twitter���璊�o
			String url = getStr(R.string.url_twitter_pick_up_key_phrase) + mPreference.getString(getStr(R.string.twitter_access_token_set_key), "") + "&access_token_secret=" + mPreference.getString(getStr(R.string.twitter_access_token_secret_set_key), "");
			twData = getJsonFromAPI(url);
		}
		
		return createKeyPhraseArray(fbData, twData);
//		return data;
	}
	
	private String[] createKeyPhraseArray(String fb, String tw){
		JSONArray fbArray = null,twArray = null;
		String[] keyPhraseArray = null;
		try {
			if(fb != null) fbArray = new JSONArray(fb);
			if(tw != null) twArray = new JSONArray(tw);
			keyPhraseArray = new String[ (fb != null ? fbArray.length() : 0) + (tw != null ? twArray.length() : 0)];
			int i = 0;
			if(fb != null) for ( i = 0; i < fbArray.length(); i++) keyPhraseArray[i] = fbArray.getString(i);
			if(tw != null) for (int j = 0; j < twArray.length(); j++,i++) keyPhraseArray[i] = twArray.getString(j);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return keyPhraseArray;
	}

	@Override
	protected void onPostExecute(String[] result) {
		super.onPostExecute(result);
		
		// �_�C�A���O������
        mProgressDialog.dismiss();
        
        if ( result == null ) Toast.makeText(mContext, "�L�[�t���[�Y��������܂���ł���", Toast.LENGTH_SHORT).show();
        else{
	        Intent intent = new Intent(mContext, SelectPickUpKeyPhraseActivity.class);
	        intent.putExtra("key_phrase", result);
	        intent.putExtra("date", getDate());
	        mActivity.startActivityForResult(intent, 200);
        }
	}
	
	private String getDate(){
		// ���݂̎������擾
		Date date = new Date();
		// �\���`����ݒ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd kk':'mm':'ss");
		return sdf.format(date);
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
                data = outputStream.toString(); // JSON�f�[�^
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
