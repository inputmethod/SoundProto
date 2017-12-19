package com.typany.soundproto;

import android.support.v4.app.Fragment;

import com.typany.common.RecyclerActivity;
import com.typany.skin2.home.adapter.SkinEntityAdapterFactory;
import com.typany.skin2.home.view.SkinCategoryGroupView;

public class SkinCategoryGroupActivity extends RecyclerActivity {
    @Override
    protected Fragment instanceFragment() {
        Fragment fragment = new SkinCategoryGroupView();
        SkinEntityAdapterFactory.parseArguments(fragment, getIntent());
        return fragment;
    }
}
