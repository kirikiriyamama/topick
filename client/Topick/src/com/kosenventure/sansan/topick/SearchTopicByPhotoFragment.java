package com.kosenventure.sansan.topick;

import android.os.Bundle;
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchTopicByPhotoFragment extends Fragment {

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        // 第3引数のbooleanは"container"にreturnするViewを追加するかどうか  
        //trueにすると最終的なlayoutに再度、同じView groupが表示されてしまうのでfalseでOKらしい  
        return inflater.inflate(R.layout.fragment_search_topic_by_photo_layout, container, false);  
    } 
}