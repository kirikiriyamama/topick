package com.kosenventure.sansan.topick;

import com.kosenventure.sansan.others.KeyPhraseListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ManagementKeyPhraseFragment extends Fragment {

	private KeyPhraseListView mKeyPhraseListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_management_key_phrase_layout, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		mKeyPhraseListView = (KeyPhraseListView) getActivity().findViewById(R.id.keyphrases_list);
	}
	
	@Override
	public void onPause () {
		super.onPause();
		log("pouse");
		mKeyPhraseListView.closeDb();
	}

	@Override
	public void onStop () {
		super.onStop();
		log("stop");
	}

	@Override
	public void onDestroyView () {
		super.onDestroyView();
		log("destroy view");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		log("destroy");
	}
	
	private void log(String msg){
		Log.d("ManagementKeyPhrases", msg);
	}
}