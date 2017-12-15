package com.typany.skin2.home.view;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.typany.skin2.home.model.SkinViewModel;

/**
 * Created by yangfeng on 2017/12/15.
 */

public class SkinCategoryGroupView extends SkinHomeView {
    private static final String TAG = SkinCategoryView.class.getSimpleName();

    private String categoryName;

//    @Override
//    protected SkinEntityAdapter getAdapter(DisplayImageOptions options) {
//        return new SkinCategoryEntityAdapter(options);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        categoryName = updateActivityTitle();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected LiveData getLiveData() {
        return getViewModel(SkinViewModel.class).getCategoryGroupPage(categoryName);
    }
}
