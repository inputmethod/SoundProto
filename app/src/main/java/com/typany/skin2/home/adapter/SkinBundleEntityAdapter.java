package com.typany.skin2.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinBundle;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;

/**
 * Created by yangfeng on 2017/12/15.
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
    protected void onItemViewClicked(Context context, final SkinViewEntity viewEntity) {
        // todo: show skin bundle detail.
        if (viewEntity instanceof SkinBundle) {
            Toast.makeText(context, "onItemViewClicked with " + viewEntity.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "onItemViewClicked error type of " + viewEntity.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new BundleViewHolder(view, displayImageOptions);
    }

    static class BundleViewHolder extends ViewHolder {
        private final LinearLayout indicatorView;
        public BundleViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView, options);
            indicatorView = (LinearLayout) itemView.findViewById(R.id.indicator_group);
        }
        public void bind(final SkinViewEntity viewEntity) {
             super.bind(viewEntity);

            if (viewEntity instanceof SkinBundle) {
                SkinBundle bundle = (SkinBundle) viewEntity;
                if (bundle.getIconList().isEmpty()) {
                    indicatorView.setVisibility(View.VISIBLE);
                    // todo: add icon into the
                }
            } else {
                SLog.e(TAG, "The item type should be SkinBundle: " + viewEntity.getClass().getCanonicalName());
            }
        }
    }
}
