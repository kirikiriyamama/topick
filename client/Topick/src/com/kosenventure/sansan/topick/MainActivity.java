package com.kosenventure.sansan.topick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kosenventure.sansan.others.OCRTask;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;

public class MainActivity extends MyActivity implements OnClickListener{

	private static final String TESSBASE_PATH = Environment.getExternalStorageDirectory().getPath(); 
	private static final File SDCARD_DIRECTORY = new File(TESSBASE_PATH+"/tessdata/");
	private static final int LAUNCH_CAMERA = 100;
	
	private Uri mImageUri;
	
	private LinearLayout mLaunchCameraBtn,mLaunchSettingBtn;
	private Button mSearchBtn;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);

		// 起動時にファイルをコピーする
		copyOCRLibrary();
		
		mLaunchSettingBtn = (LinearLayout) findViewById(R.id.btn_launch_setting);
		mLaunchSettingBtn.setOnClickListener(this);
		
		mLaunchCameraBtn = (LinearLayout) findViewById(R.id.btn_launch_camera);
		mLaunchCameraBtn.setOnClickListener(this);
		
		mSearchBtn = (Button) findViewById(R.id.btn_topic_search);
		mSearchBtn.setOnClickListener(this);

	}

	// assetsフォルダからライブラリファイルを読み込んでSDカード内にコピーする
	private void copyOCRLibrary(){
		// /sdcard/tessdataがあるかどうか確認する
		if( !SDCARD_DIRECTORY.exists() ){
			// なければ作成
			SDCARD_DIRECTORY.mkdirs();
			
			// assetsからライブラリファイルを読み込んでSDカード内にコピーする
			AssetManager assetManager = getResources().getAssets();
			InputStream input = null;
			OutputStream output = null;
			String[] files = null;
			try {
				files = assetManager.list("");
			} catch (IOException e) {
				log("Failed to get asset file list.");
			}
			for ( String file : files ) {
				try {
					input = assetManager.open(file);
					output = new FileOutputStream(TESSBASE_PATH + "/tessdata/" + file);
					copyFile(input, output);
					input.close();
					input = null;
					output.flush();
					output.close();
					output = null;
				} catch (IOException e) {
				  e.printStackTrace();
				}
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

	private void launchCamera(){
		// Resultで帰ってくるBitmapが小さいため一度ローカルに保存する
	    String filename = System.currentTimeMillis() + ".jpg";
	    
	    ContentValues values = new ContentValues();
	    values.put(MediaStore.Images.Media.TITLE, filename);
	    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
	    mImageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	    
	    Intent intent = new Intent();
	    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(intent, LAUNCH_CAMERA);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	if( requestCode == LAUNCH_CAMERA ){
    		if( resultCode == -1 ){
    			OCRTask ocrTask = new OCRTask(this, mImageUri);
    			ocrTask.execute(data);
    		}
    	}
    }
	
	@Override
	public void onClick(View v) {
		if ( v == mLaunchSettingBtn) {
			Intent intent = new Intent(mContext, SettingActivity.class);
			startActivity(intent);
		}
		if ( v == mLaunchCameraBtn ) {
			launchCamera();
		}else if ( v == mSearchBtn ) {
			Intent intent = new Intent(mContext, FoundAccountListActivity.class);
			startActivity(intent);
		}
	} 


}
