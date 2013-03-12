package com.kosenventure.sansan.topick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class SettingsFragment extends Fragment implements OnClickListener{

	private static final int WEB_VIEW_ACTIVITY = 200;
	
	Context mContext;
	Button mLoginFacebookBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		return inflater.inflate(R.layout.fragment_settings_layout, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();

		mLoginFacebookBtn = (Button) getActivity().findViewById(R.id.btn_login_facebook);
		mLoginFacebookBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		startActivityForResult(intent, WEB_VIEW_ACTIVITY);
	}
	
}
