package com.typany.skin2.home.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by yangfeng on 2017/10/17.
 *
 * 为基类提供具体的View Holder实例以及相应的item view的layout id.
 */

public class SkinHomeAdapter extends SkinEntityAdapter {
    private static final String TAG = SkinHomeAdapter.class.getSimpleName();

    public SkinHomeAdapter(DisplayImageOptions options) {
        super(options);
    }

    @Override
    protected RecyclerViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new RecyclerViewHolder(view, displayImageOptions);
    }

    @Override

    protected @LayoutRes int getItemLayoutResourceId() {
        return RecyclerViewHolder.layoutResourceId();
    }
}
