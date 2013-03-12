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
	private static final String DEFAULT_LANGUAGE = "eng";	// ���{��̐��x�����܂�ɂ��Ⴂ�̂łƂ肠�����p��őΉ�
	
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
		// Result�ŋA���Ă���Bitmap�����������߈�x���[�J���ɕۑ�����
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
	
	// URI����Bitmap�𐶐�����
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
    			
    			// Xperia�̏ꍇ,data.getData()��Uri�����Ă��܂��炵��. �̂�,������ɓ����Ă����炻�����D�悵�Ďg��.
    	        final boolean existsData = data != null && data.getData() != null;
    	        Bitmap bitmap =  getBitmapFromUri( existsData ? data.getData() : mImageUri );
    	        
    	        // SH�J�����΍�.��������SH�J�����Ŗ߂��Ă���Uri���s����Bitmap���������擾�o���Ă��Ȃ�.
    	        // �̂�,�������摜�ŃC�}�C�`�Ȃ̂���Extras�ɓ����Ă���Uri���g�p.
    	        if (bitmap == null && data != null && data.getExtras() != null) {
    	            bitmap = (Bitmap) data.getExtras().getParcelable("data");
    	        }
    	        
    	        // ����ł��܂�Bitmap��null�̏ꍇ�����邩������Ȃ��̂ŔO�̈�. null�̏ꍇ�͉����G���[����������������������.
    	        if (bitmap == null) {
    	        	log("bitmap = null");
    	        	return;
    	        }
    			
    	        // �������ł͉�͂ł��Ȃ����߉�]����
    	        Matrix mat = new Matrix();  
    	        mat.postRotate(90);  
    	        
    	        // ��]�����r�b�g�}�b�v���쐬  
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