package com.typany.skin2.home.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.home.view.SkinHomeCardView;
import com.typany.soundproto.R;

/**
 * Created by yangfeng on 2017/10/17.
 */

public class SkinHomeAdapter extends SkinEntityAdapter {
    private static final String TAG = SkinHomeAdapter.class.getSimpleName();

    public SkinHomeAdapter(DisplayImageOptions options) {
        super(options);
    }

    @Override
    protected HomeViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new HomeViewHolder(view, displayImageOptions);
    }

    @Override

    protected @LayoutRes
    int getItemLayoutResourceId() {
        return R.layout.item_skin_home_card_view;
    }

    @Override
    protected void onItemViewClicked(Context context, SkinViewEntity viewEntity) {
        // need to do nothing
    }

    static class HomeViewHolder extends ViewHolder {
        private final DisplayImageOptions imageOptions;
        public HomeViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView, options);
            this.imageOptions = options;
        }

        public void bind(final SkinViewEntity viewEntity) {
            if (itemView instanceof SkinHomeCardView) {
                ((SkinHomeCardView) itemView).setItem(viewEntity, imageOptions);
            } else {
                SLog.e(TAG, "HomeViewHolder bind item with unexpected type: " + itemView.getClass().getCanonicalName());
            }
        }
    }
}
