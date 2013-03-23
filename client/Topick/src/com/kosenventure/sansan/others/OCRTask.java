package com.kosenventure.sansan.others;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.kosenventure.sansan.topick.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OCRTask extends AsyncTask<Intent, Object, String> implements OnClickListener{

	private static final String TESSBASE_PATH = Environment.getExternalStorageDirectory().getPath(); 
	private static final String DEFAULT_LANGUAGE = "eng";	// 日本語の精度があまりにも低いのでとりあえず英語で対応
	
	private Activity mActivity;
	private Context mContext;
	private LayoutInflater mInflater;
	private ProgressDialog mProgressDialog;

	private Uri mImageUri;
	private ArrayList<String> mPickUpWordList = new ArrayList<String>();
	private String mSelectKanaName, mSelectRomajiName,mSelectKanjiName,mSelectTwitterId;
 	
	private AlertDialog mSelectKanaNameDialog,mSelectRomajiNameDialog,mSelectKanjiNameDialog,mSelectTwitterIdDialog;
	
	public OCRTask(Activity activity, Uri imageUri) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mImageUri = imageUri;
		mInflater = LayoutInflater.from(mContext);
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
	protected String doInBackground(Intent... data) {
		
		// Xperiaの場合,data.getData()でUriが取れてしまうらしい. ので,そちらに入っていたらそちらを優先して使う.
        final boolean existsData = data[0] != null && data[0].getData() != null;
        Bitmap bitmap =  getBitmapFromUri( existsData ? data[0].getData() : mImageUri );
        
        // SHカメラ対策.そもそもSHカメラで戻ってくるUriが不正でBitmapが正しく取得出来ていない.
        // ので,小さい画像でイマイチなのだがExtrasに入っているUriを使用.
        if (bitmap == null && data[0] != null && data[0].getExtras() != null) {
            bitmap = (Bitmap) data[0].getExtras().getParcelable("data");
        }
        
        // これでもまだBitmapがnullの場合があるかもしれないので念の為. nullの場合は何かエラー処理をした方がいいかも.
        if (bitmap == null) {
        	log("bitmap = null");
        	return null;
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
        baseApi.end();
		return readText;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		// ダイアログを消す
        mProgressDialog.dismiss();
		// 結果がnullなら解析失敗
		if( result == null ) Toast.makeText(mContext, getStr(R.string.toast_pick_up_failure_msg), Toast.LENGTH_SHORT).show();
		// resultに\nで区切られて格納されているため
		createArrayToList(result.split("\n"));
		showSelectKanaDialog();
	}

	private void createArrayToList(String[] words){
		for( String word : words ){
			mPickUpWordList.add(word);
		}
	}

	private void showSelectKanaDialog(){
    	final View pickUpWordLayout = mInflater.inflate(R.layout.dialog_pick_up_word_layout, null);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPickUpWordList);
    	ListView pickUpWordList = (ListView) pickUpWordLayout.findViewById(R.id.list_pick_up_word);
    	pickUpWordList.setAdapter(adapter);
    	
    	mSelectKanaNameDialog = new AlertDialog.Builder(mActivity)
		   .setTitle(R.string.dialog_pick_up_word_list_title_kana_name)
		   .setView(pickUpWordLayout)
		   .setCancelable(false)
		   .setNeutralButton(R.string.dialog_pick_up_word_list_end_btn, this)
		   .setNegativeButton(R.string.dialog_pick_up_word_list_next_btn, this)
		   .show();
    	
    	pickUpWordList.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			mSelectKanaNameDialog.dismiss();
    			mSelectKanaName = mPickUpWordList.remove(position);
    			showSelectRomajiDialog();
    		}
		});
	}
	
	private void showSelectRomajiDialog(){
    	final View pickUpWordLayout = mInflater.inflate(R.layout.dialog_pick_up_word_layout, null);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPickUpWordList);
    	ListView pickUpWordList = (ListView) pickUpWordLayout.findViewById(R.id.list_pick_up_word);
    	pickUpWordList.setAdapter(adapter);
    	
    	mSelectRomajiNameDialog = new AlertDialog.Builder(mActivity)
		   .setTitle(getStr(R.string.dialog_pick_up_word_list_title_romaji_name))
		   .setView(pickUpWordLayout)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_pick_up_word_list_back_btn, this)
		   .setNeutralButton(R.string.dialog_pick_up_word_list_end_btn, this)
		   .setNegativeButton(getStr(R.string.dialog_pick_up_word_list_next_btn), this)
		   .show();
    	
    	pickUpWordList.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			mSelectRomajiNameDialog.dismiss();
    			mSelectRomajiName = mPickUpWordList.remove(position);
    			showSelectKanjiDialog();
    		}
		});
	}
	
	private void showSelectKanjiDialog(){
    	final View pickUpWordLayout = mInflater.inflate(R.layout.dialog_pick_up_word_layout, null);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPickUpWordList);
    	ListView pickUpWordList = (ListView) pickUpWordLayout.findViewById(R.id.list_pick_up_word);
    	pickUpWordList.setAdapter(adapter);
    	
    	mSelectKanjiNameDialog = new AlertDialog.Builder(mActivity)
		   .setTitle(R.string.dialog_pick_up_word_list_title_kanji_name)
		   .setView(pickUpWordLayout)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_pick_up_word_list_back_btn, this)
		   .setNeutralButton(R.string.dialog_pick_up_word_list_end_btn, this)
		   .setNegativeButton(getStr(R.string.dialog_pick_up_word_list_next_btn), this)
		   .show();
    	
    	pickUpWordList.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			mSelectKanjiNameDialog.dismiss();
    			mSelectKanjiName = mPickUpWordList.remove(position);
    			showSelectTwitterIdDialog();
    		}
		});
	}
	
	private void showSelectTwitterIdDialog(){
    	final View pickUpWordLayout = mInflater.inflate(R.layout.dialog_pick_up_word_layout, null);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPickUpWordList);
    	ListView pickUpWordList = (ListView) pickUpWordLayout.findViewById(R.id.list_pick_up_word);
    	pickUpWordList.setAdapter(adapter);
    	
    	mSelectTwitterIdDialog = new AlertDialog.Builder(mActivity)
		   .setTitle(getStr(R.string.dialog_pick_up_word_list_title_twitter_id))
		   .setView(pickUpWordLayout)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_pick_up_word_list_back_btn, this)
		   .setNeutralButton(getStr(R.string.dialog_pick_up_word_list_end_btn), this)
		   .show();
    	
    	pickUpWordList.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			mSelectTwitterIdDialog.dismiss();
    			mSelectTwitterId = mPickUpWordList.remove(position);
    			setSelectPickUpWord();
    		}
		});
	}
	
	// EditTextに選択したテキストをセットする
	private void setSelectPickUpWord(){
		String[] name;
		if( mSelectKanaName != null ) {
			name = mSelectKanaName.split(" ");
			((EditText) mActivity.findViewById(R.id.edit_kana_first_name)).setText(name[0]);
			if( name.length > 0 )((EditText) mActivity.findViewById(R.id.edit_kana_last_name)).setText(name[1]);
		}
		if( mSelectRomajiName != null ) {
			name = mSelectRomajiName.split(" ");
			((EditText) mActivity.findViewById(R.id.edit_romaji_first_name)).setText(name[0]);
			if( name.length > 0 )((EditText) mActivity.findViewById(R.id.edit_romaji_last_name)).setText(name[1]);
		}
		if( mSelectKanjiName != null ) {
			name = mSelectKanjiName.split(" ");
			((EditText) mActivity.findViewById(R.id.edit_kanji_first_name)).setText(name[0]);
			if( name.length > 0 )((EditText) mActivity.findViewById(R.id.edit_kanji_last_name)).setText(name[1]);
		}
		if( mSelectTwitterId != null ) ((EditText) mActivity.findViewById(R.id.edit_search_twitter_id)).setText(mSelectTwitterId);
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

	private String getStr(int resourceId) {
		return mContext.getResources().getString(resourceId);
	}
	
	private void log(String msg){
		Log.d("SearchTopicFragment", msg);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		
		if ( which == DialogInterface.BUTTON_NEUTRAL ) {
			setSelectPickUpWord();
		}
		else if ( dialog == mSelectKanaNameDialog ) {
			switch (which) {
			case DialogInterface.BUTTON_NEGATIVE:
				showSelectRomajiDialog();
				break;
			}
		}else if ( dialog == mSelectRomajiNameDialog ) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				mPickUpWordList.add(mSelectKanaName);
				mSelectKanaName = null;
				showSelectKanaDialog();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				showSelectKanjiDialog();
				break;
			}
		}else if ( dialog == mSelectKanjiNameDialog ) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				mPickUpWordList.add(mSelectRomajiName);
				mSelectKanjiName = null;
				showSelectRomajiDialog();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				showSelectTwitterIdDialog();
				break;
			}
		}else if ( dialog == mSelectTwitterIdDialog ) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				mPickUpWordList.add(mSelectKanjiName);
				mSelectTwitterId = null;
				showSelectKanjiDialog();
				break;
			}
		}
	}

}
