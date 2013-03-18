package com.kosenventure.sansan.topick;

import com.kosenventure.sansan.others.Account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FoundAccountListActivity extends Activity implements OnItemClickListener{

	Context mContext;
	private ListView mAccountList;
	private FoundAccountAdapter mFoundAccountAdapter;
	
	private Account[] mAccounts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_account_list_layout);
		
		mContext = getApplicationContext();
		
		mAccounts = new Account[3];
		mAccounts[0] = new Account("Ç‚Ç‹Ç‹", "í∑ñÏçÇêÍ", null);
		mAccounts[1] = new Account("Ç»ÇÌÇƒÇ°Å[", "èºç]çÇêÍ", null);
		mAccounts[2] = new Account("Ç∑Ç‹Ç±", "êŒêÏçÇêÍ", null);
		
		mAccountList = (ListView) findViewById(R.id.found_account_list);
		mFoundAccountAdapter = new FoundAccountAdapter(this, R.layout.list_found_account_layout, mAccounts);
		mAccountList.setAdapter( mFoundAccountAdapter );
		mAccountList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, TopicListActivity.class);
		startActivity(intent);
	}
	
	
	public class FoundAccountAdapter extends ArrayAdapter<Account> {

		Activity mContext;
		private Account[] mObjects;
		
		public FoundAccountAdapter(Activity context, int textViewResourceId, Account[] objects) {
			super(context, textViewResourceId, objects);
			
			mContext = context;
			mObjects = objects;
		}

		@Override
		public Account getItem(int position) {
			return mObjects[position];
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_found_account_layout, null);
			}
			
//			ImageView photo = (ImageView) convertView.findViewById(R.id.account_photo);
//			photo.setImageURI(uri);
			
			TextView name = (TextView) convertView.findViewById(R.id.account_name);
			name.setText(mObjects[position].name);
			
			TextView bio = (TextView) convertView.findViewById(R.id.account_bio);
			bio.setText(mObjects[position].state);
			
			return convertView;
		}

	}
}