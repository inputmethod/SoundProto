package com.typany.skin2.home.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by yangfeng on 2017/12/15.
 *
 * 为基类提供具体的View Holder实例以及相应的item view的layout id.
 *
 * 在一个皮肤列表(category)里，每个列表元素是一个皮肤项，布局显示为圆角卡片，点击时，弹窗显示皮肤
 * 信息并等待进一步步下载应用操作.当一个列表元素是广告占位时，显示的是广告内容，点击时，跳转到广告落地页。
 * 当一个列表元素是运营位占位时，点击跳转到相应的落地页。（TODO）
 */

public class SkinBundleEntityAdapter extends SkinEntityAdapter {
    private static final String TAG = SkinBundleEntityAdapter.class.getSimpleName();

    public SkinBundleEntityAdapter(DisplayImageOptions options) {
        super(options);
    }

    @Override
    protected @LayoutRes int getItemLayoutResourceId() {
        return BundleViewHolder.layoutResourceId();
    }

    @Override
    protected boolean isEntityClickable () {
        return true;
    }

    @Override
    protected ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new BundleViewHolder(view, displayImageOptions);
    }
}
