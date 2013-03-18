package com.kosenventure.sansan.topick;

import com.kosenventure.sansan.others.AccessDb;
import com.kosenventure.sansan.others.PickUpKeyPhrasesTask;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;

public class ManagementKeyPhraseActivity extends MyActivity implements OnClickListener,OnKeyListener{

	final static String DUMMY_DATE = "1111-11-11 11:11:11";
	
	private AccessDb mAd;
	private KeyPhraseCursorAdapter mKeyPhraseCursorAdapter;
	
	LinearLayout mBackBtn,mShowAddKeyPhraseMenu;
	private EditText mEditSearchKeyPhrase;
	private Button mSearchKeyPhraseBtn,mShowAddKeyPhraseDialogBtn,mShowPickUpKeyPhraseDialogBtn;
	private ListView mKeyPhraseListView;

	AlertDialog selectWayToAddKeyPhraseDialog,addKeyPhraseDialog,pickUpKeyPhraseDialog;

	LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_management_key_phrase_layout);
		
		mAd = new AccessDb(mContext);
		inflater = getLayoutInflater();
//		saveData();
		
		mBackBtn = (LinearLayout) findViewById(R.id.btn_back_management_key_phrase);
		mBackBtn.setOnClickListener(this);
		
		mShowAddKeyPhraseMenu = (LinearLayout) findViewById(R.id.btn_show_add_key_phrase_menu);
		mShowAddKeyPhraseMenu.setOnClickListener(this);
		
		mEditSearchKeyPhrase = (EditText) findViewById(R.id.edit_search_key_phrase);
		mEditSearchKeyPhrase.setOnKeyListener(this);
		
		mSearchKeyPhraseBtn = (Button) findViewById(R.id.btn_search_key_phrase);
		mSearchKeyPhraseBtn.setOnClickListener(this);
		
		mKeyPhraseCursorAdapter = new KeyPhraseCursorAdapter(mContext, getKeyPhrasesFromDb(null, null), false);
		mKeyPhraseListView = (ListView) findViewById(R.id.list_key_phrase);
		mKeyPhraseListView.setAdapter(mKeyPhraseCursorAdapter);
		
	}
	
	
	// DBからキーフレーズを取得する
	private Cursor getKeyPhrasesFromDb(String where, String[] answer){
		return mAd.readDb(getStr(R.string.keyphrase_table), new String[]{"id as _id","phrase","date"}, where, answer, "id");
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
		mKeyPhraseCursorAdapter.swapCursor(getKeyPhrasesFromDb("phrase like ?", new String[]{"%"+searchKey+"%"}));
		mKeyPhraseCursorAdapter.notifyDataSetChanged();
	}

	private void closeIME(View v){
        //ソフトキーボードを閉じる
		InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	private void showAddKeyPhraseMenu(){
		View view = inflater.inflate(R.layout.dialog_select_way_to_add_key_phrase_dialog, null);
		
		mShowAddKeyPhraseDialogBtn = (Button) view.findViewById(R.id.btn_add_key_phrase);
		mShowPickUpKeyPhraseDialogBtn = (Button) view.findViewById(R.id.btn_pick_up_key_phrase);
		
		selectWayToAddKeyPhraseDialog = new AlertDialog.Builder(this)
											.setTitle("追加方法を選択")
											.setView(view)
											.setCancelable(false)
											.setNegativeButton("閉じる", null)
											.show();
		
		PickUpKeyPhrasesTask mPickUpKeyPhrasesTask = new PickUpKeyPhrasesTask(this);
		mPickUpKeyPhrasesTask.execute();
	}
	
	private void showAddKeyPhraseDialog() {
		
	}
	
	private void showPickUpKeyPhraseDialog(){
		
	}
	
	@Override
	public void onClick(View v) {
		if ( v == mBackBtn) {
			finish();
		}
		else if ( v == mShowAddKeyPhraseMenu) {
			showAddKeyPhraseMenu();
		}
		else if ( v == mSearchKeyPhraseBtn ) {
			searchKeyPhrase();
		}
		else if ( v == mShowAddKeyPhraseDialogBtn ) {
			selectWayToAddKeyPhraseDialog.dismiss();
			showAddKeyPhraseDialog();
		}
		else if ( v == mShowPickUpKeyPhraseDialogBtn ) {
			selectWayToAddKeyPhraseDialog.dismiss();
			showPickUpKeyPhraseDialog();
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
	
	private class KeyPhraseCursorAdapter extends CursorAdapter {


		private KeyPhraseCursorAdapter mMe = this;
		

		public KeyPhraseCursorAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			mContext = context;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final int id = cursor.getInt(cursor.getColumnIndex("_id"));
			
			TextView textPhrase = (TextView) view.findViewById(R.id.text_key_phrase);
			textPhrase.setText(cursor.getString(cursor.getColumnIndex("phrase")));
			
			ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.btn_delete_key_phrase);
			deleteBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// DBから削除
					deleteKeyPhraseFromDb(id);
					mMe.getCursor().requery();
				}
			});
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View v = getLayoutInflater().inflate(R.layout.list_key_phrase_layout, null);
			return v;
		}
	}
}
