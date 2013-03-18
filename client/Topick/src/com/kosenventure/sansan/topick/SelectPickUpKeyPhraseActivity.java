package com.kosenventure.sansan.topick;

import java.util.ArrayList;

import org.w3c.dom.Text;

import com.kosenventure.sansan.others.AccessDb;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectPickUpKeyPhraseActivity extends MyActivity implements OnClickListener{

	LinearLayout mBackBtn;
	ListView mListView;
	Button mAddBtn;
	
	PickUpKeyPhraseAdapter mPickUpKeyPhraseAdapter;
	String date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pick_up_key_phrase_layout);
		
		Bundle bundle = getIntent().getExtras();
		String[] result = bundle.getStringArray("key_phrase");
		date = bundle.getString("date");
		
		mBackBtn = (LinearLayout) findViewById(R.id.btn_back_select_pick_up_key_phrase);
		mBackBtn.setOnClickListener(this);
		
		mPickUpKeyPhraseAdapter = new PickUpKeyPhraseAdapter(mContext, result);
		mListView = (ListView) findViewById(R.id.list_pick_up_key_phrase);
		mListView.setAdapter(mPickUpKeyPhraseAdapter);
		
		mAddBtn = (Button) findViewById(R.id.btn_add_checked_pick_up_key_phrase);
		mAddBtn.setOnClickListener(this);
	}

	private View createSelectPhraseView(){
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		ArrayList<String> selected = mPickUpKeyPhraseAdapter.getCheckedPhrase();
		for ( String phrase : selected ){
			TextView text  = new TextView(mContext);
			text.setText(phrase);
			layout.addView(text);
		}
		return layout;
	}
	
	@Override
	public void onClick(View v) {
		if ( v == mBackBtn ) {
			finish();
		}
		else if ( v == mAddBtn ) {
			new AlertDialog.Builder(this)
						   .setMessage("以下のキーフレーズが選択されています")
						   .setView(createSelectPhraseView())
						   .setCancelable(false)
						   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									// キーフレーズをDBに追加
									AccessDb ad = new AccessDb(mContext);
									ArrayList<String> list = mPickUpKeyPhraseAdapter.getCheckedPhrase();
									for ( String phrase : list ){
										ad.writeDb(getStr(R.string.keyphrase_table), phrase, date);
									}
									setResult(RESULT_OK);
									finish();
								}
							})
							.setNegativeButton("選びなおす", null)
							.show();
		}
	}
	
	public class PickUpKeyPhraseAdapter extends BaseAdapter{

		private String[] objects;
		private boolean[] mCheckArray;
		
		public PickUpKeyPhraseAdapter(Context context, String[] objects) {
			super();
			this.objects = objects;
			mCheckArray = new boolean[objects.length];
		}
		
		public ArrayList<String> getCheckedPhrase(){
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i<objects.length; i++){
				if(mCheckArray[i]) list.add(objects[i]);
			}
			return list;
		}
		
		@Override
		public int getCount() {
			return objects.length;
		}

		@Override
		public String getItem(int position) {
			return objects[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			final int index = position;
			if(convertView == null){
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_pick_up_key_phrase_layout, null);
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ViewGroup viewG = (ViewGroup) v;
					CheckBox check = (CheckBox) ((ViewGroup) viewG.getChildAt(0)).getChildAt(1);
					check.setChecked(!check.isChecked());		
					mCheckArray[index] = check.isChecked();		
				}
			});
			
			TextView textPhrase = (TextView) convertView.findViewById(R.id.text_pick_up_key_phrase);
			textPhrase.setText(getItem(position));
			
			CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb_pick_up_key_phrase);
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mCheckArray[index] = isChecked;
				}
			});
			cb.setChecked(mCheckArray[index]);
			
			return convertView;
		}
	}
}
