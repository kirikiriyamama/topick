package com.kosenventure.sansan.others;

import com.kosenventure.sansan.topick.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class KeyPhraseListView extends ScrollView {

	final static int MP = LinearLayout.LayoutParams.MATCH_PARENT;
	final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	
	private boolean mSortFlag = false;
	
	private Activity mContext;
	private KeyPhrase[] mKeyPhrases;
	private KeyPhrase[] mCancelKeyPhrases;
	private AccessDb mAd;
	
	private RelativeLayout mKeyPhraseListView;
	
	public KeyPhraseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = (Activity) context;
		mAd = new AccessDb(mContext);

//		deleteData();
//		saveData();
		getKeyPhrasesFromDb();
		
		mKeyPhraseListView = new RelativeLayout(mContext);
		addView(mKeyPhraseListView, new ViewGroup.LayoutParams(MP, MP));
		
		addKeyPhrases();
	}
	
	private void saveData(){
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.cancel_keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");	
	}
	
	private void deleteData(){
		Cursor cursor = mAd.readDb(getStr(R.string.keyphrase_table), null, null, null, "id");
		int id;
		if(cursor != null){
			do {
				id = cursor.getInt(cursor.getColumnIndex("id"));
				mAd.deleteDb(getStr(R.string.keyphrase_table), String.valueOf(id));
			} while (cursor.moveToNext());
			cursor.close();
		}
		
		cursor = mAd.readDb(getStr(R.string.cancel_keyphrase_table), null, null, null, "id");
		if(cursor != null){
			do {
				id = cursor.getInt(cursor.getColumnIndex("id"));
				mAd.deleteDb(getStr(R.string.cancel_keyphrase_table), String.valueOf(id));
			} while (cursor.moveToNext());
			cursor.close();
		}
	}
	
	public void closeDb(){
		mAd.closeDb();
	}
	
	private void getKeyPhrasesFromDb(){
		int i = 0;
		int id;
		String phrase,date;
		Cursor cursor = mAd.readDb(getStr(R.string.keyphrase_table), null, null, null, "id");
		if(cursor != null){
			mKeyPhrases = new KeyPhrase[cursor.getCount()];
			
			do {
				id = cursor.getInt(cursor.getColumnIndex("id"));
				phrase = cursor.getString(cursor.getColumnIndex("phrase"));
				date = cursor.getString(cursor.getColumnIndex("date"));
				
				mKeyPhrases[i++] = new KeyPhrase(id, phrase, date);
			} while (cursor.moveToNext());
			cursor.close();
		}
		
		i = 0;
		cursor = mAd.readDb(getStr(R.string.cancel_keyphrase_table), null, null, null, "id");
		if(cursor != null){
			mCancelKeyPhrases = new KeyPhrase[cursor.getCount()];
			do {
				id = cursor.getInt(cursor.getColumnIndex("id"));
				phrase = cursor.getString(cursor.getColumnIndex("phrase"));
				date = cursor.getString(cursor.getColumnIndex("date"));
				
				mCancelKeyPhrases[i++] = new KeyPhrase(id, phrase, date);
			} while (cursor.moveToNext());
			cursor.close();
		}
	}
	
	private String getStr(int resourceId) {
		return mContext.getResources().getString(resourceId);
	}
	
	private void addKeyPhrases(){
		int id = 1;
		if( mKeyPhrases != null ) for( KeyPhrase phrase : mKeyPhrases ) addKeyPhraseView(phrase, id++);
		if( mCancelKeyPhrases != null ) for( KeyPhrase phrase : mCancelKeyPhrases ) addKeyPhraseView(phrase, id++);
	}
	
	private void addKeyPhraseView(KeyPhrase phrase, int id){
		LayoutInflater inflater = mContext.getLayoutInflater();
		LinearLayout keyphraseView;
		TextView keyphrase;
		ImageButton keyphraseBtn;
		
		// ラインにフレーズを追加する
		keyphraseView = (LinearLayout) inflater.inflate(R.layout.keyphrase_layout, null);
		keyphraseView.setId(id);
		keyphrase = (TextView) keyphraseView.findViewById(R.id.keyphrase);
		keyphrase.setText(phrase.phrase);
		keyphraseBtn = (ImageButton) keyphraseView.findViewById(R.id.keyphrase_btn);
		keyphraseBtn.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
//				keyphraseLineView.removeView(keyphraseView);
			}
		});
		mKeyPhraseListView.addView(keyphraseView);
		
	}
	
	private void sortKeyPhrases(){
		mSortFlag = true;
		
		int count = mKeyPhraseListView.getChildCount();
		if( count == 0 ) return;
		
	    int margin = 10;
	    View pline = mKeyPhraseListView.getChildAt(0);
	    pline.setLayoutParams( params(WC, WC, null, 0));
	    int total = pline.getWidth() + margin;
	    View view;
	    RelativeLayout.LayoutParams prm;
	    for (int i = 1; i < count; i++) {
	    	view = mKeyPhraseListView.getChildAt(i);
	    	int w = view.getWidth() + margin;
	    	
	    	// 横幅を超えないなら前のボタンの右に出す
	    	if ( mKeyPhraseListView.getWidth() > total + w) {
	    		total += w;
	    		prm = params(WC, WC, new int[]{ RelativeLayout.ALIGN_TOP, RelativeLayout.RIGHT_OF}, i);
	    	}
	    	// 超えたら下に出す
	    	else {	
	    		prm = params(WC, WC, new int[]{ RelativeLayout.BELOW}, pline.getId());
	    		// 基点を変更
	    		pline = mKeyPhraseListView.getChildAt(i);
	   	     	// 長さをリセット
	    		total = pline.getWidth() + margin;
	      	}
	    	view.setLayoutParams(prm);
	    }
	}
	
	private RelativeLayout.LayoutParams params(int width, int height, int[] verbs, int anchor){
		RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(width, height);
		params.setMargins(5, 0, 5, 5);
		if( anchor > 0 ) for( int verb : verbs ) params.addRule(verb, anchor);
		return params;
	}
	
	@Override
	protected void onDraw (Canvas canvas) {
		super.onDraw(canvas);
		
		if( mSortFlag ) return;
		sortKeyPhrases();
	}
}
