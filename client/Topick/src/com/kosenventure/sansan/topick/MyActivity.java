package com.kosenventure.sansan.topick;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyActivity extends Activity {

	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
	}
	protected void log(String msg){
		Log.d("Tag", msg);
	}

	protected void toast(String text){
		Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		toast.show();
	}
	
	protected void toast(String text, int gravity){
		Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}
	
	protected String getStr(int resourceId) {
		return getResources().getString(resourceId);
	}

}
