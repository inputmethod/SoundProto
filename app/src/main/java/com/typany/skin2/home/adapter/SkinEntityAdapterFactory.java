package com.typany.skin2.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinBundle;
import com.typany.skin2.home.model.SkinCategory;
import com.typany.skin2.home.model.SkinCategoryGroup;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.common.RecyclerActivity;
import com.typany.soundproto.SkinCategoryActivity;
import com.typany.soundproto.SkinCategoryGroupActivity;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/15.
 */

public class SkinEntityAdapterFactory {
    private static final String TAG = SkinEntityAdapterFactory.class.getSimpleName();

    // 计算recycler view某个位置占span数时，默认只占自己1列，广告位将占满全部列。
    private static final int DEFAULT_SPAN_SIZE = 1;

    // 广告位占满全部列，否则只占默认列数
    public static int calculateSpanSize(SkinViewEntity viewEntity, int columns) {
        if (isAdStub(viewEntity.getBundleName())) {
            return columns;
        } else {
            return DEFAULT_SPAN_SIZE;
        }
    }

    // 分类列表和皮肤列表元素被点击
    // todo: 广告位占位，运营占位，more占位的统一处理
    public static void onItemClicked(Context context, SkinViewEntity viewEntity) {
        if (viewEntity instanceof SkinBundle) {
            Toast.makeText(context, "onItemViewClicked with " + viewEntity.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        } else if (viewEntity.getClass() == SkinViewEntity.class) {
            String bundleName = viewEntity.getBundleName();
            if (isAdStub(bundleName)) {
                // todo: respond to ad click.
                Toast.makeText(context, "onItemViewClicked AD name " + bundleName, Toast.LENGTH_SHORT).show();
            } else {
                startCategoryActivity(context, bundleName);
            }
        } else {
            Toast.makeText(context, "onItemViewClicked error type of " + viewEntity.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class LayoutSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private final List<SkinViewEntity> entityList;
        private final int totalSpanSize;
        public LayoutSpanSizeLookup(List<SkinViewEntity> entityList, int totalSpanSize) {
            this.entityList = entityList;
            this.totalSpanSize = totalSpanSize;
        }

        @Override
        public int getSpanSize(int position) {
            return calculateSpanSize(entityList.get(position), totalSpanSize);
        }
    }

    public static SkinEntityAdapter buildAdapterFrom(Class<? extends SkinViewEntity> cls, DisplayImageOptions imageOptions, boolean horizontal) {
        if (cls == SkinCategory.class) {
            return new SkinBundleEntityAdapter(imageOptions);
        } else {
            SLog.i(TAG, "buildAdapterFrom return default SkinCategoryEntityAdapter for " + cls);
            return new SkinCategoryEntityAdapter(imageOptions, horizontal);
        }
    }

    private static LayoutManager buildGridLayoutManager(Context context, List<SkinViewEntity> entityList,int displayColumn) {
        if (displayColumn == 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            return layoutManager;
        } else if (displayColumn > 0) {
            LayoutSpanSizeLookup lookup = new LayoutSpanSizeLookup(entityList, displayColumn);
            GridLayoutManager layoutManager = new GridLayoutManager(context, displayColumn);
            layoutManager.setSpanSizeLookup(lookup);
            return layoutManager;
        } else {
            // 负数，不认识的显示风格
            SLog.e(TAG, "buildGridLayoutManager, unknown display column: " + displayColumn);
            return null;
        }
    }

    public static boolean isAdStub(String bundleName) {
        return TextUtils.equals(bundleName, "ad_top_name") || TextUtils.equals(bundleName, "ad_name");
    }

    public static void fillSubList(RecyclerView recyclerView, SkinViewEntity entity, DisplayImageOptions imageOptions) {
        final List<SkinViewEntity> entityList = entity.getBundleList();
        if (entityList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            if (isAdStub(entity.getBundleName())) {
                // todo: setup for AD stub here.
                // 广告点位，通过固定的名字
            }
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            int displayColumn = entity.getDisplayColumn();
            RecyclerView.LayoutManager layoutManager = buildGridLayoutManager(recyclerView.getContext(),
                    entityList, displayColumn);
            if (null == layoutManager) {
                // 不识别的数据集合, 合法数据不应该走到这。
                SLog.e(TAG, "Unknown entity: " + entity.getBundleName() + ", " + displayColumn);
            } else {
                recyclerView.setLayoutManager(layoutManager);
                SkinEntityAdapter adapter = buildAdapterFrom(entity.getClass(), imageOptions, displayColumn == 0);
                recyclerView.setAdapter(adapter);
                adapter.setSkinItemList(entityList);
            }
        }
    }

    // 点击皮肤首页的collection, category头部的"更多"， 显示下一级列表
    public static void onMoreItemClicked(Context context, SkinViewEntity entity) {
        Class cls = entity.getClass();
        if (SkinCategory.class == cls) {
            startCategoryActivity(context, entity.getBundleName());
        } else if (SkinCategoryGroup.class == cls) {
            startActivityFor(context, SkinCategoryGroupActivity.class, entity.getBundleName());
        } else {
            SLog.e(TAG, "onMoreItemClicked, unexpected More for type: " + cls);
        }
    }

    public static void startCategoryActivity(Context context, String bundleName) {
        startActivityFor(context, SkinCategoryActivity.class, bundleName);
    }

    private static void startActivityFor(Context context, Class<? extends RecyclerActivity> target, String bundleName) {
        Intent intent = new Intent(context, target);
        intent.putExtra("bundleName", bundleName);
        context.startActivity(intent);
    }

    public static <T extends Fragment> void parseArguments(T fragment, Intent intent) {
        String bundleName = intent.getStringExtra("bundleName");
        Bundle bundle = new Bundle();
        bundle.putString("bundleName", bundleName);
        fragment.setArguments(bundle);
    }
}
