package com.kosenventure.sansan.topick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.content.Context;
import android.content.res.AssetManager;

public class MainActivity extends SherlockFragmentActivity implements TabListener, FragmentManager.OnBackStackChangedListener{

	private static final String TESSBASE_PATH = Environment.getExternalStorageDirectory().getPath(); 
	private static final File SDCARD_DIRECTORY = new File(TESSBASE_PATH+"/tessdata/");
	
	private Context mContext;
    private Fragment mFragment;
    private int mSelectedTabPosition = -1; // 何番目のタブが選択されているか
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
		setContentView(R.layout.activity_main_layout);
		
		mContext = getApplicationContext();
		
		// 起動時にファイルをコピーする
		copyOCRLibrary();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        // ActionBar
        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        actionBar.addTab(actionBar.newTab().setText("話題検索").setIcon(R.drawable.tab_topic_search).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("キーフレーズ管理").setIcon(R.drawable.tab_key_phrase_ctrl).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("設定").setIcon(R.drawable.tab_set).setTabListener(this));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.getTabAt(0).select(); // 0番目のタブを選択しておく
	}

	// assetsフォルダからライブラリファイルを読み込んでSDカード内にコピーする
	private void copyOCRLibrary(){
		// /sdcard/tessdataがあるかどうか確認する
		if( !SDCARD_DIRECTORY.exists() ){
			// なければ作成
			SDCARD_DIRECTORY.mkdirs();
			
			// assetsからライブラリファイルを読み込んでSDカード内にコピーする
			AssetManager assetManager = getResources().getAssets();
			InputStream input = null;
			OutputStream output = null;
			String[] files = null;
			try {
				files = assetManager.list("");
			} catch (IOException e) {
				Log.e("tag", "Failed to get asset file list.", e);
			}
			for ( String file : files ) {
				try {
					input = assetManager.open(file);
					output = new FileOutputStream(TESSBASE_PATH + "/tessdata/" + file);
					copyFile(input, output);
					input.close();
					input = null;
					output.flush();
					output.close();
					output = null;
				} catch (IOException e) {
				  e.printStackTrace();
				}
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	@Override
	public void onBackStackChanged() {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		int tabPosition = tab.getPosition();
		if (mSelectedTabPosition == tabPosition) {
			return;
		}
		mSelectedTabPosition = tabPosition;
		switch (tabPosition) {
		case 0:
			mFragment = Fragment.instantiate(this, SearchTopicFragment.class.getName());
			transaction.add(R.id.container, mFragment);
			break;
		case 1:
			mFragment = Fragment.instantiate(this, ManagementKeyPhraseFragment.class.getName());
			transaction.add(R.id.container, mFragment);
			break;
		case 2:
			mFragment = Fragment.instantiate(this, SettingsFragment.class.getName());
			transaction.add(R.id.container, mFragment);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
		transaction.remove(mFragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {}
}
