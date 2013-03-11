package com.kosenventure.sansan.others;

import com.kosenventure.sansan.topick.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class KeyPhraseListView extends LinearLayout {

	final static int MP = LinearLayout.LayoutParams.MATCH_PARENT;
	final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	
	private Activity mContext;
	private String[] mKeyPhrases;
	private AccessDb mAd;
	
	public KeyPhraseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = (Activity) context;
		mAd = new AccessDb(mContext);
		
		saveData();
		
		mKeyPhrases = new String[6];
		mKeyPhrases[0] = new String("����");
		mKeyPhrases[1] = new String("���]");
		mKeyPhrases[2] = new String("�t�x��");
		mKeyPhrases[3] = new String("����");
		
		inithializeView();
	}
	
	private saveData(){
		
	}
	
	private void inithializeView(){
		LinearLayout keyphraseLineView = null;
		LayoutInflater inflater = mContext.getLayoutInflater();
		LinearLayout keyphraseView;
		TextView keyphrase;
		ImageButton keyphraseBtn;
		
		for( String phrase : mKeyPhrases ){
			// �q�r���[���O�A�܂���View�̉����𒴂����ꍇ�V�������C����ǉ�����
			if( this.getChildCount() == 0 ){
				keyphraseLineView = new LinearLayout(mContext);
				addView(keyphraseLineView , params(MP, WC, 0));
			}
			
			// ���C���Ƀt���[�Y��ǉ�����
			keyphraseView = (LinearLayout) inflater.inflate(R.layout.keyphrase_layout, null);
			keyphrase = (TextView) keyphraseView.findViewById(R.id.keyphrase);
			keyphrase.setText(phrase);
			keyphraseBtn = (ImageButton) keyphraseView.findViewById(R.id.keyphrase_btn);
			keyphraseBtn.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
//					keyphraseLineView.removeView(keyphraseView);
				}
			});
			keyphraseLineView.addView(keyphraseView, params(WC, WC, 5));
		}
	}
	
	private LinearLayout.LayoutParams params(int width, int height, int right){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		if(right != 0)	params.setMargins(0, 5, right, 5);
		return params;
	}
}
