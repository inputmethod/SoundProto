package com.typany.soundproto;

import android.support.v4.app.Fragment;

import com.typany.skin2.home.view.SkinHomeView;

public class SkinActivity extends FragmentActivity {

    @Override
    protected Fragment instanceFragment() {
        return new SkinHomeView();
    }
}
