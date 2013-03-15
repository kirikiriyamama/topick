package com.kosenventure.sansan.topick;

import java.util.ArrayList;
import java.util.List;

import com.kosenventure.sansan.others.AccessDb;
import com.kosenventure.sansan.others.KeyPhrase;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

public class ManagementKeyPhraseActivity extends MyActivity implements OnClickListener,OnKeyListener{

	final static String DUMMY_DATE = "1111-11-11 11:11:11";
	
	private AccessDb mAd;
	KeyPhraseArrayAdapter mKeyPhraseArrayAdapter;

	private EditText mEditSearchKeyPhrase;
	private Button mSearchKeyPhraseBtn;
	private ListView mKeyPhraseListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_management_key_phrase_layout);
		
		mAd = new AccessDb(mContext);
		
		mEditSearchKeyPhrase = (EditText) findViewById(R.id.edit_search_key_phrase);
		mEditSearchKeyPhrase.setOnKeyListener(this);
		
		mSearchKeyPhraseBtn = (Button) findViewById(R.id.btn_search_key_phrase);
		mSearchKeyPhraseBtn.setOnClickListener(this);
		
		mKeyPhraseArrayAdapter = new KeyPhraseArrayAdapter(mContext, R.layout.list_key_phrase_layout, getKeyPhrasesFromDb(null, null));
		mKeyPhraseListView = (ListView) findViewById(R.id.list_key_phrase);
		mKeyPhraseListView.setAdapter(mKeyPhraseArrayAdapter);
		
	}
	
	
	// DBからキーフレーズを取得する
	private ArrayList<KeyPhrase> getKeyPhrasesFromDb(String where, String[] answer){
		int id;
		String phrase,date;
		Cursor cursor = mAd.readDb(getStr(R.string.keyphrase_table), null, where, answer, "id");
		ArrayList<KeyPhrase> mKeyPhraseList = new ArrayList<KeyPhrase>();
		if(cursor != null){
			do {
				id = cursor.getInt(cursor.getColumnIndex("id"));
				phrase = cursor.getString(cursor.getColumnIndex("phrase"));
				date = cursor.getString(cursor.getColumnIndex("date"));
				
				mKeyPhraseList.add(new KeyPhrase(id, phrase, date));
			} while (cursor.moveToNext());
			cursor.close();
		}
		return mKeyPhraseList;
	}
	
	// DBからキーフレーズを削除する
	private void deleteKeyPhraseFromDb(int id) {
		mAd.deleteDb(getStr(R.string.keyphrase_table), String.valueOf(id));
	}
	
	// デバッグ用。DBにダミーデータを追加
	private void saveData(){
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
		mAd.writeDb(getStr(R.string.keyphrase_table), "キーフレーズキーフレーズキーフレーズキーフレーズ", "2009-08-24 23:10:15");
		mAd.writeDb(getStr(R.string.keyphrase_table), "テストテストテスト", "2009-08-24 23:10:15");	
	}
	
	// デバッグ用。DBのデータをすべて削除
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
	
	// DBをcloseする
	public void closeDb(){
		mAd.closeDb();
	}

	// キーフレーズの検索
	public void searchKeyPhrase(){
		// 検索フレーズを取得してIMEを閉じる
		String searchKey = mEditSearchKeyPhrase.getText().toString();
		mEditSearchKeyPhrase.clearFocus();
		closeIME(mEditSearchKeyPhrase);
		
		// リストを再構成してアダプターを更新する
		mKeyPhraseArrayAdapter = new KeyPhraseArrayAdapter(mContext, R.layout.list_key_phrase_layout, getKeyPhrasesFromDb("phrase like ?", new String[]{"%"+searchKey+"%"}));
		mKeyPhraseListView.setAdapter(mKeyPhraseArrayAdapter);
	}
	

	private void closeIME(View v){
        //ソフトキーボードを閉じる
		InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
		
	@Override
	public void onClick(View v) {
		if ( v == mSearchKeyPhraseBtn ) {
			searchKeyPhrase();
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){	// 完了ボタンを押した時
			if ( v == mEditSearchKeyPhrase ) {
				searchKeyPhrase();
			}
		}
		return false;
	}
	
	private class KeyPhraseArrayAdapter extends ArrayAdapter<KeyPhrase> {

		private Context mContext;
		private KeyPhraseArrayAdapter mMe = this;
		
		public KeyPhraseArrayAdapter(Context context, int textViewResourceId, List<KeyPhrase> objects) {
			super(context, textViewResourceId, objects);
			mContext = context;
		}
		
		@Override
		public View getView (final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_key_phrase_layout, null);
			}
			
			TextView textPhrase = (TextView) convertView.findViewById(R.id.text_key_phrase);
			textPhrase.setText(getItem(position).phrase);
			
			ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.btn_delete_key_phrase);
			deleteBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteKeyPhraseFromDb(mMe.getItem(position).id);
					mMe.remove(mMe.getItem(position));
					mMe.notifyDataSetChanged();
				}
			});
			
			return convertView;
		}
	}
}
