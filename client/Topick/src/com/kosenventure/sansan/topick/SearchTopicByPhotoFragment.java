package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchTopicByPhotoFragment extends Fragment {

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        // ��3������boolean��"container"��return����View��ǉ����邩�ǂ���  
        //true�ɂ���ƍŏI�I��layout�ɍēx�A����View group���\������Ă��܂��̂�false��OK�炵��  
        return inflater.inflate(R.layout.fragment_search_topic_by_photo_layout, container, false);  
    } 
}