package com.typany.skin2.home.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;

/**
 * Created by yangfeng on 2017/12/15.
 *
 * 在collection/categories含有的列表，每个列表元素是一个category, 布局显示可为小圆或者圆角卡片，
 * 点击时，跳转到该category页面，显示它所包含的皮肤列表。
 */

public class SkinCategoryEntityAdapter extends SkinEntityAdapter {
    private final boolean horizontal;
    public SkinCategoryEntityAdapter(DisplayImageOptions options, boolean horizontal) {
        super(options);
        this.horizontal = horizontal;
    }

    @Override

    protected @LayoutRes int getItemLayoutResourceId() {
        if (horizontal) {
            return R.layout.item_skin_category_horizontal;
        } else {
            return super.getItemLayoutResourceId();
        }
    }


    @Override
    protected void onItemViewClicked(Context context, SkinViewEntity viewEntity) {
        // todo: show skin entity list of the category.
        if (viewEntity.getClass() == SkinViewEntity.class) {
//            Toast.makeText(context, "onItemViewClicked to show category: " + viewEntity.getBundleName(), Toast.LENGTH_SHORT).show();
            SkinEntityAdapterFactory.startCategoryActivity(context, viewEntity.getBundleName());
        } else {
            Toast.makeText(context, "onItemViewClicked error type of " + viewEntity.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }
}
