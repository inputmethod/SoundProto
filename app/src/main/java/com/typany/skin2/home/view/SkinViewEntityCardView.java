package com.typany.skin2.home.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;

import java.util.List;


/**
 * Created by yangfeng on 2017/9/23.
 */

public class SkinViewEntityCardView extends RelativeLayout {
    private SkinViewEntity itemData = null;

    public SkinViewEntityCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        parseHolderViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = width; // Math.round(width * 0.7128514056224899f);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!changed) {
            return;
        }
    }

    public void setItem(SkinViewEntity entity) {
        // todo: render entity data to views.
        if (entity != itemData) {
            checkAndChangeLayout(entity);
            itemData = entity;
        }
    }

    private void checkAndChangeLayout(SkinViewEntity entity) {
        if (TextUtils.isEmpty(entity.getBundleName()) && !entity.isHasMore()) {
            titleLayout.setVisibility(GONE);
        } else {
            titleLayout.setVisibility(VISIBLE);
            titleView.setText(entity.getBundleName());
            moreView.setVisibility(entity.isHasMore() ? VISIBLE : GONE);
        }

        List<SkinViewEntity> entityList = entity.getBundleList();
        if (entityList.isEmpty()) {
            recyclerView.setVisibility(GONE);
            if (isAdStub(entity.getBundleName())) {
                // todo: setup for AD stub here.
                // 广告点位，通过固定的名字
            }
        } else {
            recyclerView.setVisibility(VISIBLE);
            // todo: apply entityList to recycler view.
        }
    }

    private boolean isAdStub(String bundleName) {
        return TextUtils.equals(bundleName, "ad_top_name") || TextUtils.equals(bundleName, "ad_name");
    }

    private DisplayImageOptions imageOptions;
//    private ImageView previewImageView;
    private RecyclerView recyclerView;
    private View titleLayout;
    private TextView titleView;
    private TextView moreView;

    private void attach(DisplayImageOptions options) {
        imageOptions = options;
    }

    public static void attach(View itemView, DisplayImageOptions options) {
        if (itemView instanceof SkinViewEntityCardView) {
            SkinViewEntityCardView view = (SkinViewEntityCardView) itemView;
            view.attach(options);
        } else {
            throw new IllegalArgumentException("Item view need to be instance of SkinViewEntityCardView, " + itemView.getClass().getSimpleName());
        }
    }

    protected void parseHolderViews() {
//        previewImageView = (ImageView) findViewById(R.id.iv_skin);
        titleLayout = findViewById(R.id.theme_title);
        recyclerView = (RecyclerView) findViewById(R.id.theme_content);
        titleView = (TextView) titleLayout.findViewById(R.id.tv_title);
        moreView = (TextView) titleLayout.findViewById(R.id.tv_more);
    }
}
