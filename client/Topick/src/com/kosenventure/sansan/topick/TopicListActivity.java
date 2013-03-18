package com.kosenventure.sansan.topick;

import com.kosenventure.sansan.others.Topic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class TopicListActivity extends Activity implements OnItemClickListener{
	
	Context mContext;
	private ListView mTopicList;
	private TopicAdapter mTopicAdapter;
	
	private Topic[] mTopics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_list_layout);
		
		mContext = getApplicationContext();
		
		mTopics = new Topic[3];
		mTopics[0] = new Topic(new String[]{"高専"}, null, "高専楽しい！");
		mTopics[1] = new Topic(new String[]{"高専","島根"}, null, "高専楽しくない！");
		mTopics[2] = new Topic(new String[]{"高専","石川"}, null, "高専は就職が楽！");
		
		mTopicList = (ListView) findViewById(R.id.topic_list);
		mTopicAdapter = new TopicAdapter(this, R.layout.list_topic_layout, mTopics);
		mTopicList.setAdapter( mTopicAdapter );
		mTopicList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, TopicListActivity.class);
		startActivity(intent);
	}
	
	
	public class TopicAdapter extends ArrayAdapter<Topic> {

		Activity mContext;
		private Topic[] mObjects;
		
		public TopicAdapter(Activity context, int textViewResourceId, Topic[] objects) {
			super(context, textViewResourceId, objects);
			
			mContext = context;
			mObjects = objects;
		}

		@Override
		public Topic getItem(int position) {
			return mObjects[position];
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_topic_layout, null);
			}
			
//			ImageView photo = (ImageView) convertView.findViewById(R.id.Topic_photo);
//			photo.setImageURI(uri);
			
			TextView name = (TextView) convertView.findViewById(R.id.match_keyphrase);
			name.setText(mObjects[position].getKeyPhrase());
			
			TextView bio = (TextView) convertView.findViewById(R.id.topic_content);
			bio.setText(mObjects[position].content);
			
			return convertView;
		}

	}
}
