package com.kosenventure.sansan.topick;

import com.kosenventure.sansan.others.KeyPhraseListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManagementKeyPhraseFragment extends Fragment implements OnClickListener,OnKeyListener{

	private Activity mActivity;
	private Context mContext;
	
	private KeyPhraseListView mKeyPhraseListView;
	private EditText mEditSearchKeyPhrase,mEditAddKeyPhrase;
	private Button mPickUpKeyPhraseBtn,mSearchKeyPhraseBtn,mAddKeyPhraseBtn,mSaveKeyPhraseChangeBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		mContext = mActivity.getApplicationContext();
		
		return inflater.inflate(R.layout.fragment_management_key_phrase_layout, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		mEditSearchKeyPhrase = (EditText) mActivity.findViewById(R.id.edit_search_key_phrase);
		mEditSearchKeyPhrase.setOnKeyListener(this);
		mEditAddKeyPhrase = (EditText) mActivity.findViewById(R.id.edit_add_key_phrase);
		mEditAddKeyPhrase.setOnKeyListener(this);
		
		mPickUpKeyPhraseBtn = (Button) mActivity.findViewById(R.id.btn_pick_up_key_phrase);
		mPickUpKeyPhraseBtn.setOnClickListener(this);
		mSearchKeyPhraseBtn = (Button) mActivity.findViewById(R.id.btn_search_key_phrase);
		mSearchKeyPhraseBtn.setOnClickListener(this);
		mAddKeyPhraseBtn = (Button) mActivity.findViewById(R.id.btn_add_key_phrase);
		mAddKeyPhraseBtn.setOnClickListener(this);
		mSaveKeyPhraseChangeBtn = (Button) mActivity.findViewById(R.id.btn_save_key_phrase_changes);
		mSaveKeyPhraseChangeBtn.setOnClickListener(this);
		
		mKeyPhraseListView = (KeyPhraseListView) mActivity.findViewById(R.id.keyphrases_list);
	}
	
	@Override
	public void onPause () {
		super.onPause();
		log("pouse");
		mKeyPhraseListView.closeDb();
	}
	
	private void searchKeyPhrase(){
		String searchKey = mEditSearchKeyPhrase.getText().toString();
		// âΩÇ‡ì¸óÕÇ≥ÇÍÇƒÇ¢Ç»Ç¢èÍçá
		if( searchKey.equals("") ){
			toast(getString(R.string.alert_search_key_phrase));
			return;
		}
		mKeyPhraseListView.addKeyPhrasesBySearch(searchKey);
	}

	private void addKeyPhrase(){
		String searchKey = mEditSearchKeyPhrase.getText().toString();
		// âΩÇ‡ì¸óÕÇ≥ÇÍÇƒÇ¢Ç»Ç¢èÍçá
		if( searchKey.equals("") ){
			toast(getString(R.string.alert_search_key_phrase));
			return;
		}
		mKeyPhraseListView.addKeyPhrasesBySearch(searchKey);
	}
	
	private void log(String msg){
		Log.d("ManagementKeyPhrases", msg);
	}
	
	private void toast(String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		if ( v == mSearchKeyPhraseBtn ) {
			searchKeyPhrase();
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){	// äÆóπÉ{É^ÉìÇâüÇµÇΩéû
			if ( v == mEditSearchKeyPhrase ) {
				searchKeyPhrase();
			}
			else if ( v == mAddKeyPhraseBtn ) {
				
			}
		}
		return false;
	}
	
}