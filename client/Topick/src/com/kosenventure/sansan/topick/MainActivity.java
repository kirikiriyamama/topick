package com.kosenventure.sansan.topick;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;

public class MainActivity extends SherlockFragmentActivity implements TabListener, FragmentManager.OnBackStackChangedListener{

	private Context mContext;
    private Fragment mFragment;
    private int mSelectedTabPosition = -1; // ���Ԗڂ̃^�u���I������Ă��邩
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
		setContentView(R.layout.activity_main_layout);
		
		mContext = getApplicationContext();
		

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        // ActionBar
        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        actionBar.addTab(actionBar.newTab().setText("�b�茟��").setIcon(R.drawable.ic_tab_search_selected).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("�L�[�t���[�Y�Ǘ�").setIcon(R.drawable.ic_tab_topic_selected).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("�ݒ�").setIcon(R.drawable.ic_tab_settings_selected).setTabListener(this));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.getTabAt(0).select(); // 0�Ԗڂ̃^�u��I�����Ă���
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
