package com.kosenventure.sansan.topick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchTopicFragment extends Fragment implements OnClickListener {

	private Context mContext;
	private Button mSearchBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ��3������boolean��"container"��return����View��ǉ����邩�ǂ���  
        //true�ɂ���ƍŏI�I��layout�ɍēx�A����View group���\������Ă��܂��̂�false��OK�炵��  

		mContext = getActivity().getApplicationContext();
		return inflater.inflate(R.layout.fragment_search_topic_layout, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();

		mSearchBtn = (Button) getActivity().findViewById(R.id.search_btn);
		mSearchBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if ( v == mSearchBtn ) {
			Intent intent = new Intent(mContext, FoundAccountListActivity.class);
			startActivity(intent);
		}
	} 
	
}