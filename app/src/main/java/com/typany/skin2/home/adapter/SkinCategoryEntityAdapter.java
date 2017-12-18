package com.typany.skin2.home.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.soundproto.R;

/**
 * Created by yangfeng on 2017/12/15.
 *
 * 在collection/categories含有的列表，每个列表元素是一个category, 布局显示可为小圆或者圆角卡片，
 * 点击时，跳转到该category页面，显示它所包含的皮肤列表。
 */

public class SkinCategoryEntityAdapter extends SkinEntityAdapter {
    private @LayoutRes int layoutResourceId;
    public SkinCategoryEntityAdapter(DisplayImageOptions options, boolean horizontal) {
        super(options);
        layoutResourceId = horizontal ? R.layout.item_skin_category_horizontal : PreviewViewHolder.layoutResourceId();
    }

    @Override
    protected ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new PreviewViewHolder(view, displayImageOptions);
    }

    @Override
    protected @LayoutRes int getItemLayoutResourceId() {
        return layoutResourceId;
    }

    @Override
    protected boolean isEntityClickable () {
        return true;
    }
}
