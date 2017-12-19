package com.typany.soundproto;

import android.support.v4.app.Fragment;

import com.typany.common.RecyclerActivity;
import com.typany.skin2.home.view.SkinHomeView;

public class SkinHomeActivity extends RecyclerActivity {

    @Override
    protected Fragment instanceFragment() {
        return new SkinHomeView();
    }
}
