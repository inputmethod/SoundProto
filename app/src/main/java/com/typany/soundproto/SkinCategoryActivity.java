package com.typany.soundproto;

import android.support.v4.app.Fragment;

import com.typany.common.RecyclerActivity;
import com.typany.skin2.home.adapter.SkinEntityAdapterFactory;
import com.typany.skin2.home.view.SkinCategoryView;

public class SkinCategoryActivity extends RecyclerActivity {

    @Override
    protected Fragment instanceFragment() {
        SkinCategoryView fragment = new SkinCategoryView();
        SkinEntityAdapterFactory.parseArguments(fragment, getIntent());
        return fragment;
    }
}
