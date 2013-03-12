package com.kosenventure.sansan.topick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchTopicFragment extends Fragment implements OnClickListener {

	static final int LAUNCH_CAMERA = 100;
	
	private Context mContext;
	private Button mLaunchCameraBtn,mSearchBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 第3引数のbooleanは"container"にreturnするViewを追加するかどうか  
        //trueにすると最終的なlayoutに再度、同じView groupが表示されてしまうのでfalseでOKらしい  

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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	if( requestCode == LAUNCH_CAMERA ){
    		if( resultCode == -1 ){
    			log("callback from camera");
    		}
    	}
    }

	@Override
	public void onClick(View v) {
		if ( v == mLaunchCameraBtn ) {
			Intent intent = new Intent();
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, LAUNCH_CAMERA);
		}else if ( v == mSearchBtn ) {
			Intent intent = new Intent(mContext, FoundAccountListActivity.class);
			startActivity(intent);
		}
	} 
	
	private void log(String msg){
		Log.d("SearchTopicFragment", msg);
	}
}