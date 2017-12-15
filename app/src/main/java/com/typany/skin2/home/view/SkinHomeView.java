package com.typany.skin2.home.view;

import android.arch.lifecycle.LiveData;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.adapter.SkinEntityAdapter;
import com.typany.skin2.home.adapter.SkinHomeAdapter;
import com.typany.skin2.home.model.SkinViewModel;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinHomeView extends SkinBaseFragmentView {
    private static final String TAG = SkinHomeView.class.getSimpleName();

    @Override
    protected SkinEntityAdapter getAdapter(DisplayImageOptions options) {
        return new SkinHomeAdapter(options);
    }

    @Override
    protected int getColumnCount() {
        return 1;
    }

    @Override
    protected LiveData getLiveData() {
        return getViewModel(SkinViewModel.class).getHomePage();
    }
}
