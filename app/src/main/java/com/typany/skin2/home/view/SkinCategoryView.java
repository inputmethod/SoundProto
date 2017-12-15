package com.typany.skin2.home.view;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.adapter.SkinBundleEntityAdapter;
import com.typany.skin2.home.adapter.SkinEntityAdapter;
import com.typany.skin2.home.model.SkinViewModel;

/**
 * Created by yangfeng on 2017/12/15.
 */

public class SkinCategoryView extends SkinBaseFragmentView {
    private static final String TAG = SkinCategoryView.class.getSimpleName();

    private String categoryName;

    @Override
    protected SkinEntityAdapter getAdapter(DisplayImageOptions options) {
        return new SkinBundleEntityAdapter(options);
    }

    @Override
    protected int getColumnCount() {
        return 2;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoryName = updateActivityTitle();
    }

    @Override
    protected LiveData getLiveData() {
        return getViewModel(SkinViewModel.class).getCategoryPage(categoryName);
    }
}
