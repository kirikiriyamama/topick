package com.kosenventure.sansan.topick;

import java.util.ArrayList;

import com.kosenventure.sansan.others.Topic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowTopicListActivity extends MyActivity implements OnClickListener,OnItemClickListener{

	private ArrayList<Topic> mTopicList;
	private TopicAdapter mTopicAdapter;
	
	private ImageView mBackBtn,mHomeBtn;
	private ListView mTopicListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_topic_list_layout);
		
		mTopicList = createTopicList((Object[]) getIntent().getSerializableExtra("topic"));
		
		mBackBtn = (ImageView) findViewById(R.id.btn_back_show_topic_list);
		mBackBtn.setOnClickListener(this);
		
		mHomeBtn = (ImageView) findViewById(R.id.btn_back_to_home);
		mHomeBtn.setOnClickListener(this);
		
		mTopicListView = (ListView) findViewById(R.id.list_topic);
		mTopicAdapter = new TopicAdapter(mContext, mTopicList);
		mTopicListView.setAdapter(mTopicAdapter);
		mTopicListView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("login_url", mTopicAdapter.getItem(position).page_link);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if ( v == mBackBtn ) {
			finish();
		}
		else if ( v == mHomeBtn ) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private ArrayList<Topic> createTopicList(Object[] obj){
		ArrayList<Topic> aclist = new ArrayList<Topic>();
		for(int i=0;i<obj.length;i++){
			if(obj[i] == null) break;
			aclist.add((Topic)obj[i]);
		}
		
		return aclist;
	}

	private class TopicAdapter extends BaseAdapter {

		Context mContext;
		ArrayList<Topic> mTopicList;
		
		public TopicAdapter(Context context, ArrayList<Topic> topics){
			mContext = context;
			mTopicList = topics;
		}
		
		@Override
		public int getCount() {
			return mTopicList.size();
		}

		@Override
		public Topic getItem(int position) {
			return mTopicList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int p = position;
			Topic t = getItem(position);
			if(convertView == null){
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_topic_layout, null);
			}
			
			TextView summary = (TextView) convertView.findViewById(R.id.text_topic_summary);
			summary.setText( t.summary == null ? "‚È‚µ" : t.summary+"...");
			
			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_topic_links);
			if (t.shared_lists.length == 0) {
				TextView url = new TextView(mContext);
				url.setText("‚È‚µ");
				url.setTextColor(Color.BLACK);
				url.setTextSize(15f);
				layout.addView(url);
			}
			for ( String link : t.shared_lists ){
				final String l = link;
				TextView url = new TextView(mContext);
				url.setText(link);
				url.setTextSize(15f);
				if( l.indexOf("http", 0) != -1){
					url.setTextColor(Color.BLUE);
					url.getPaint().setUnderlineText(true);
					url.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							openBrowser(l);
						}
					});
				}else{
					url.setTextColor(Color.BLACK);
				}
				layout.addView(url);
			}
			
			return convertView;
		}
		
		private void openBrowser(String link){
			Intent intent = new Intent(mContext, WebViewActivity.class);
			intent.putExtra("login_url", link);
			startActivity(intent);
		}
	}
}
