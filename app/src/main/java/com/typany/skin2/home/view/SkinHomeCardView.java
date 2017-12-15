package com.typany.skin2.home.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.adapter.SkinEntityAdapter;
import com.typany.skin2.home.adapter.SkinEntityAdapterFactory;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.utils.SkinHelper;
import com.typany.soundproto.R;

import java.util.List;


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

        final List<SkinViewEntity> entityList = entity.getBundleList();
        if (entityList.isEmpty()) {
            recyclerView.setVisibility(GONE);
            if (SkinHelper.isAdStub(entity.getBundleName())) {
                // todo: setup for AD stub here.
                // 广告点位，通过固定的名字
            }
        } else {
            recyclerView.setVisibility(VISIBLE);
            int displayColumn = entity.getDisplayColumn();
            if (displayColumn > 0) {
                SkinEntityAdapter adapter = SkinEntityAdapterFactory.buildFrom(entity.getClass(), imageOptions);
                recyclerView.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), entity.getDisplayColumn());
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (SkinHelper.isAdStub(entityList.get(position).getBundleName())) {
                            return 2;
                        } else {
                            return 1;
                        }
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                adapter.setSkinItemList(entityList);
            } else if (displayColumn == 0){
                // todo: 显示单排小圆风格的图标
                SLog.e(TAG, "todo: display small circle icon, e.g. for collection");
            } else {
                // todo: 异常？不识别的数据集合
                SLog.e(TAG, "Unknown entity: " + entity.getBundleName() + ", " + displayColumn);
            }
        }
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
        if (itemView instanceof SkinHomeCardView) {
            SkinHomeCardView view = (SkinHomeCardView) itemView;
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
