package com.kosenventure.sansan.topick;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchTopicFragment extends Fragment implements OnClickListener {

	private static final int LAUNCH_CAMERA = 100;
	private static final String TESSBASE_PATH = Environment.getExternalStorageDirectory().getPath(); 
	private static final String DEFAULT_LANGUAGE = "eng";	// 日本語の精度があまりにも低いのでとりあえず英語で対応
	
	private static Context mContext;
	private Uri mImageUri;
	
	private Button mLaunchCameraBtn,mSearchBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		return inflater.inflate(R.layout.fragment_search_topic_layout, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();

		mLaunchCameraBtn = (Button) getActivity().findViewById(R.id.btn_launch_camera);
		mLaunchCameraBtn.setOnClickListener(this);
		
		mSearchBtn = (Button) getActivity().findViewById(R.id.btn_topic_search);
		mSearchBtn.setOnClickListener(this);
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
	
	// URIからBitmapを生成する
	private Bitmap getBitmapFromUri(Uri imageUri){
		BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inSampleSize = 2;
	    Bitmap resizeBitmap = null;
	    ContentResolver conReslv = mContext.getContentResolver();
	    InputStream iStream;
	    
		try {
			iStream = conReslv.openInputStream(imageUri);
		    resizeBitmap = BitmapFactory.decodeStream(iStream, null, opts);
		    iStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    return resizeBitmap;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	if( requestCode == LAUNCH_CAMERA ){
    		if( resultCode == -1 ){
    			
    			// Xperiaの場合,data.getData()でUriが取れてしまうらしい. ので,そちらに入っていたらそちらを優先して使う.
    	        final boolean existsData = data != null && data.getData() != null;
    	        Bitmap bitmap =  getBitmapFromUri( existsData ? data.getData() : mImageUri );
    	        
    	        // SHカメラ対策.そもそもSHカメラで戻ってくるUriが不正でBitmapが正しく取得出来ていない.
    	        // ので,小さい画像でイマイチなのだがExtrasに入っているUriを使用.
    	        if (bitmap == null && data != null && data.getExtras() != null) {
    	            bitmap = (Bitmap) data.getExtras().getParcelable("data");
    	        }
    	        
    	        // これでもまだBitmapがnullの場合があるかもしれないので念の為. nullの場合は何かエラー処理をした方がいいかも.
    	        if (bitmap == null) {
    	        	log("bitmap = null");
    	        	return;
    	        }
    			
    	        // 横方向では解析できないため回転する
    	        Matrix mat = new Matrix();  
    	        mat.postRotate(90);  
    	        
    	        // 回転したビットマップを作成  
    	        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);  
    	        bitmap = bitmap.copy(Config.ARGB_8888, true);
    	        
                TessBaseAPI baseApi = new TessBaseAPI();
                baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
                baseApi.setImage(bitmap);
                String readText = baseApi.getUTF8Text();
                log(readText);
                baseApi.end();
    		}
    	}
    }
	
	@Override
	public void onClick(View v) {
		if ( v == mLaunchCameraBtn ) {
			launchCamera();
		}else if ( v == mSearchBtn ) {
			Intent intent = new Intent(mContext, FoundAccountListActivity.class);
			startActivity(intent);
		}
	} 
	
	private void log(String msg){
		Log.d("SearchTopicFragment", msg);
	}
}