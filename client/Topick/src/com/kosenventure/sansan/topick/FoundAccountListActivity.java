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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.content.Context;
import android.content.Intent;

public class FoundAccountListActivity extends MyActivity implements OnClickListener,OnItemClickListener,OnItemLongClickListener{

	final static private int SHOW_TOPIC_LIST = 500;
	
	Context mContext;
	private ListView mAccountListView;
	private FoundAccountAdapter mFoundAccountAdapter;
	
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
		
		mAccountListView = (ListView) findViewById(R.id.list_found_account);
		mFoundAccountAdapter = new FoundAccountAdapter(mContext, mFacebookAccounts, mTwitterAccount);
		mAccountListView.setAdapter(mFoundAccountAdapter);
		mAccountListView.setOnItemClickListener(this);
		mAccountListView.setOnItemLongClickListener(this);
		
		mLaunchPickUpTopic = (Button) findViewById(R.id.btn_launch_pick_up_topick);
		mLaunchPickUpTopic.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	if( requestCode == SHOW_TOPIC_LIST ){
    		if( resultCode == RESULT_OK ){
    			finish();
    		}
    	}
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("login_url", mFoundAccountAdapter.getItem(position).prof_link);
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		mFoundAccountAdapter.selectAccount(position, view);
		return true;
	}

	public class FoundAccountAdapter extends BaseAdapter {

		private int mTwitterCount,mFacebookCount;
		private ArrayList<Account> mAccountList;
		
		
		private boolean isTwitterSelect;
		private int mSelectFacebookPosition = -1;
		private View mSelectFacebookView;
		
		public FoundAccountAdapter(Context context, ArrayList<FacebookAccount> fblist, TwitterAccount twac) {
			mContext = context;
			mAccountList = new ArrayList<Account>();
			if ( twac != null ) {
				mAccountList.add(twac);
				mTwitterCount = 1;
			}
			if( fblist != null ){
				for( Account ac : fblist ){
					mAccountList.add(ac);
				}
				mFacebookCount = fblist.size();
			}
		}
		
		public void selectAccount(int position, View view){
			Account ac = getItem(position);
			if ( ac instanceof TwitterAccount ){
				getBackgroundView(view).setBackgroundResource( isTwitterSelect ? R.drawable.account_background : R.drawable.account_background_selected );
				isTwitterSelect = !isTwitterSelect;
			}
			else if( ac instanceof FacebookAccount ){
				if ( mSelectFacebookPosition != -1 ) {
					getBackgroundView(mSelectFacebookView).setBackgroundResource(R.drawable.account_background);
				}
				
				if( mSelectFacebookPosition == position ){
					getBackgroundView(view).setBackgroundResource(R.drawable.account_background);
					mSelectFacebookPosition = -1;
					mSelectFacebookView = null;
					return;
				}
				
				getBackgroundView(view).setBackgroundResource(R.drawable.account_background_selected); 
				mSelectFacebookPosition = position;
				mSelectFacebookView = view;
			}
		}
		
		public String[] getSelectedAccount(){
			String[] array = new String[2];
			
			if ( mSelectFacebookPosition == -1 && !isTwitterSelect ) return null;

			if( mSelectFacebookPosition != -1 )
				array[0] = String.valueOf(getItem(mSelectFacebookPosition).id);
			
			if( isTwitterSelect )
				array[1] = String.valueOf(mTwitterAccount.id);
			
			return array;
		}

		@Override
		public Account getItem(int position) {
			return mAccountList.get(position);
		}

		@Override
		public int getCount() {
			return mAccountList.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Account ac = getItem(position);
			
			if(convertView == null){
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_found_account_layout, null);
			}
			
			ImageView prof = (ImageView) convertView.findViewById(R.id.image_prof);
			ImageDownloadTask task = new ImageDownloadTask(prof);  
			task.execute(ac.picture_link); 
			
			TextView text1 = (TextView) convertView.findViewById(R.id.text_accont_1);
			text1.setText(ac.name);

			ImageView icon = (ImageView) convertView.findViewById(R.id.image_sns_icon);
			TextView text2 = (TextView) convertView.findViewById(R.id.text_accont_2);
			TextView text3 = (TextView) convertView.findViewById(R.id.text_accont_3);
			if ( ac instanceof TwitterAccount ){
				getBackgroundView(convertView).setBackgroundResource( isTwitterSelect ? R.drawable.account_background_selected : R.drawable.account_background);
				icon.setImageResource(R.drawable.icon_twitter);
				icon.setVisibility(View.VISIBLE);
				text2.setText(((TwitterAccount) ac).screen_name);
				text3.setText(((TwitterAccount) ac).description);
			}
			else if ( ac instanceof FacebookAccount ){
				getBackgroundView(convertView).setBackgroundResource( position - mTwitterCount > 0 && position == mSelectFacebookPosition ? R.drawable.account_background_selected : R.drawable.account_background);
				icon.setImageResource(R.drawable.icon_facebook);
				icon.setVisibility( position - mTwitterCount > 0 ? View.INVISIBLE : View.VISIBLE);
				text2.setText(((FacebookAccount) ac).gender);
				text3.setText(((FacebookAccount) ac).locale);
			}
			
			return convertView;
		}
		
		private View getBackgroundView(View view){
			ViewGroup views = (ViewGroup) view;
			return ((ViewGroup) views.getChildAt(0)).getChildAt(1);
		}
		
	}
}