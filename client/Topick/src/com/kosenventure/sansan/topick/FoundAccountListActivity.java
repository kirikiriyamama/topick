package com.kosenventure.sansan.topick;

import java.util.ArrayList;

import com.kosenventure.sansan.others.Account;
import com.kosenventure.sansan.others.FacebookAccount;
import com.kosenventure.sansan.others.PickUpTopicTask;
import com.kosenventure.sansan.others.TwitterAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FoundAccountListActivity extends MyActivity implements OnClickListener{

	Context mContext;
	private ListView mAccountList;
	private FoundAccountAdapter mFoundAccountAdapter;
	
	private Account[] mAccounts;
	private ArrayList<FacebookAccount> mFacebookAccounts;
	private TwitterAccount mTwitterAccount;
	
	private ImageView mBackBtn;
	private Button mLaunchPickUpTopic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_account_list_layout);
		
		mContext = getApplicationContext();
		Bundle bundle = getIntent().getExtras();
		mFacebookAccounts = createFacebookAccountList((Object[]) getIntent().getSerializableExtra("facebook"));
		mTwitterAccount = (TwitterAccount) bundle.getSerializable("twitter");

		mBackBtn = (ImageView) findViewById(R.id.btn_back_found_account_list);
		mBackBtn.setOnClickListener(this);
		
		mAccountList = (ListView) findViewById(R.id.list_found_account);
		mFoundAccountAdapter = new FoundAccountAdapter(mContext, mFacebookAccounts, mTwitterAccount);
		mAccountList.setAdapter(mFoundAccountAdapter);
		
		mLaunchPickUpTopic = (Button) findViewById(R.id.btn_launch_pick_up_topick);
		mLaunchPickUpTopic.setOnClickListener(this);
	}
	
	private ArrayList<FacebookAccount> createFacebookAccountList(Object[] obj){
		ArrayList<FacebookAccount> aclist = new ArrayList<FacebookAccount>();
		for(int i=0;i<obj.length;i++){
			if(obj[i] == null) break;
			aclist.add((FacebookAccount)obj[i]);
		}
		
		return aclist;
	}

	@Override
	public void onClick(View v) {
		if ( v == mBackBtn ) {
			finish();
		}
		else if ( v == mLaunchPickUpTopic ) {
			String[] array = mFoundAccountAdapter.getSelectedAccount();
			if( array == null ) {
				toast("‚Ç‚ê‚©‚ð‘I‘ð‚µ‚Ä‚­‚¾‚³‚¢B");
				return;
			}
			
			PickUpTopicTask mPickUpTopicTask = new PickUpTopicTask(this);
			mPickUpTopicTask.execute(array);
		}
	}
	
	public class FoundAccountAdapter extends BaseAdapter {

		private Context mContext;
		private Account[] mObjects;
		
		private ArrayList<FacebookAccount> mFacebookAccounts;
		private TwitterAccount mTwitterAccount;
		
		private boolean isTwitterSelecte;
		private int mSelectFacebookAccount = -1;
		private View mSelectFacebookView;
		
		public FoundAccountAdapter(Context context, ArrayList<FacebookAccount> fblist, TwitterAccount twac) {
			mContext = context;
			mFacebookAccounts = fblist;
			mTwitterAccount = twac;
		}
		
		public String[] getSelectedAccount(){
			String[] array = new String[2];
			
			if ( mSelectFacebookAccount == -1 && !isTwitterSelecte ) return null;

			if( mSelectFacebookAccount != -1 )
				array[0] = String.valueOf(mFacebookAccounts.get(mSelectFacebookAccount).id);
			
			if( isTwitterSelecte )
				array[1] = mTwitterAccount.screen_name;
			
			return array;
		}

		@Override
		public Account getItem(int position) {
			if (position == 0 && mTwitterAccount != null) {
				return mTwitterAccount;
			}
			else if (position > 0 && mTwitterAccount != null) {
				position--;
			}

			return mObjects[position];
		}

		@Override
		public int getCount() {
			return mFacebookAccounts.size() + (mTwitterAccount != null ? 1 : 0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0 && mTwitterAccount != null) {
				return createTwitterView(convertView);
			}
			else if (position > 0 && mTwitterAccount != null) {
				position--;
			}

			return createFacebookView(position, convertView);
		}
		
		private View createTwitterView(View convertView){
			
			if(convertView == null){
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_found_twitter_account_layout, null);
			}
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					v = ((ViewGroup)v).getChildAt(1);
					v.setBackgroundResource(isTwitterSelecte ? R.drawable.account_background : R.drawable.account_background_selected);
					isTwitterSelecte = !isTwitterSelecte;
					return false;
				}
			});
			
			((ViewGroup)convertView).getChildAt(1).setBackgroundResource( isTwitterSelecte ? R.drawable.account_background : R.drawable.account_background_selected );
			
			TextView name = (TextView) convertView.findViewById(R.id.twitter_account_name);
			name.setText(mTwitterAccount.name);
			
			TextView sname = (TextView) convertView.findViewById(R.id.twitter_account_screen_name);
			sname.setText(mTwitterAccount.screen_name);
			
			TextView bio = (TextView) convertView.findViewById(R.id.twitter_account_biography);
			bio.setText(mTwitterAccount.description);
			
			Button btn = (Button) convertView.findViewById(R.id.btn_show_detail_twitter_account);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, WebViewActivity.class);
					intent.putExtra("login_url", mTwitterAccount.prof_link);
					startActivity(intent);					
				}
			});
			
			return convertView;
		}
		
		private View createFacebookView(int position, View convertView){
			final int p = position;
			if(convertView == null){
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_found_facebook_account_layout, null);
			}
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					v = ((ViewGroup)v).getChildAt(1);
					if( mSelectFacebookAccount == p ){
						mSelectFacebookAccount = -1;
						mSelectFacebookView = null;
						v.setBackgroundResource(R.drawable.account_background);
					}
					else{
						if(mSelectFacebookView != null) mSelectFacebookView.setBackgroundResource(R.drawable.account_background);
						mSelectFacebookAccount = p;
						v.setBackgroundResource(R.drawable.account_background_selected);
						mSelectFacebookView = v;
					}
					return false;
				}
			});

			if( position == mSelectFacebookAccount ) ((ViewGroup)convertView).getChildAt(1).setBackgroundResource(R.drawable.account_background_selected);
			else ((ViewGroup)convertView).getChildAt(1).setBackgroundResource(R.drawable.account_background);
			
			FacebookAccount fac = mFacebookAccounts.get(position);

			ImageView icon = (ImageView) convertView.findViewById(R.id.icon_facebook);
			if( position > 0 ){
				icon.setVisibility(View.INVISIBLE);
			}else{
				icon.setVisibility(View.VISIBLE);
			}
			
			TextView name = (TextView) convertView.findViewById(R.id.facebook_account_name);
			name.setText(fac.name);
			
			TextView gender = (TextView) convertView.findViewById(R.id.facebook_account_gender);
			gender.setText(fac.gender);
			
			TextView locale = (TextView) convertView.findViewById(R.id.facebook_account_locale);
			locale.setText(fac.locale);

			Button btn = (Button) convertView.findViewById(R.id.btn_show_detail_facebook_account);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, WebViewActivity.class);
					intent.putExtra("login_url", mFacebookAccounts.get(p).prof_url);
					startActivity(intent);					
				}
			});
			
			return convertView;
		}
	}
}