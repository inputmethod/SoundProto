package com.typany.skin2.home.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.model.SkinViewEntity;
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

        private RecyclerView recyclerView;
        private View titleLayout;
        private TextView titleView;
        private TextView moreView;

        public HomeViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView, options);
            this.imageOptions = options;

            titleLayout = itemView.findViewById(R.id.theme_title);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.theme_content);
            titleView = (TextView) titleLayout.findViewById(R.id.tv_title);
            moreView = (TextView) titleLayout.findViewById(R.id.tv_more);
        }

        public void bind(final SkinViewEntity viewEntity) {
            if (TextUtils.isEmpty(viewEntity.getBundleName()) && !viewEntity.isHasMore()) {
                titleLayout.setVisibility(View.GONE);
            } else {
                titleLayout.setVisibility(View.VISIBLE);
                titleView.setText(viewEntity.getBundleName());

                if (viewEntity.isHasMore()) {
                    moreView.setVisibility(View.VISIBLE);
                    moreView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SkinEntityAdapterFactory.onMoreItemClicked(v.getContext(), viewEntity);
                        }
                    });
                } else {
                    moreView.setVisibility(View.GONE);
                    moreView.setOnClickListener(null);
                }
            }

            SkinEntityAdapterFactory.fillSubList(recyclerView, viewEntity, imageOptions);
        }
    }
}
