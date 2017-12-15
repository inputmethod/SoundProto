package com.typany.skin2.home.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.adapter.SkinEntityAdapterFactory;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;


/**
 * Created by yangfeng on 2017/9/23.
 *
 * 皮肤首页的各组视图，如collection, categories, feature, trending, all theme, 每组是一个SkinHomeCardView,
 * 可显示一条标题栏加More按钮和一个skin和skin类组元素，当列表某个元素的名称是特定的字符时，可表示该元素为
 * 广告占位符，在相应视图插入显示广告。
 */

public class SkinHomeCardView extends RelativeLayout {
    private static final String TAG = SkinHomeCardView.class.getSimpleName();

    private SkinViewEntity itemData = null;

    public SkinHomeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        parseHolderViews();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int width = MeasureSpec.getSize(widthMeasureSpec);
//        final int height = width; // Math.round(width * 0.7128514056224899f);
//        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!changed) {
            return;
        }
    }

    public void setItem(SkinViewEntity entity, DisplayImageOptions imageOptions) {
        if (entity != itemData) {
            checkAndChangeLayout(entity, imageOptions);
            itemData = entity;
        }
    }

    private void checkAndChangeLayout(final SkinViewEntity entity, DisplayImageOptions imageOptions) {
        if (TextUtils.isEmpty(entity.getBundleName()) && !entity.isHasMore()) {
            titleLayout.setVisibility(GONE);
        } else {
            titleLayout.setVisibility(VISIBLE);
            titleView.setText(entity.getBundleName());
            moreView.setVisibility(entity.isHasMore() ? VISIBLE : GONE);

            // todo: set the click listener to the whole bar or more view only?
            moreView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SkinEntityAdapterFactory.onMoreItemClicked(v.getContext(), entity);
                }
            });
        }

        SkinEntityAdapterFactory.fillSubList(recyclerView, entity, imageOptions);
    }

    private RecyclerView recyclerView;
    private View titleLayout;
    private TextView titleView;
    private TextView moreView;

    protected void parseHolderViews() {
        titleLayout = findViewById(R.id.theme_title);
        recyclerView = (RecyclerView) findViewById(R.id.theme_content);
        titleView = (TextView) titleLayout.findViewById(R.id.tv_title);
        moreView = (TextView) titleLayout.findViewById(R.id.tv_more);
    }
}
