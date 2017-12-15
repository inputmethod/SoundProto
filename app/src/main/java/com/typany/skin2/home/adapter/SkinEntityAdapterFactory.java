package com.typany.skin2.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinCategory;
import com.typany.skin2.home.model.SkinCategoryGroup;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.FragmentActivity;
import com.typany.soundproto.SkinCategoryActivity;
import com.typany.soundproto.SkinCategoryGroupActivity;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/15.
 */

public class SkinEntityAdapterFactory {
    private static final String TAG = SkinEntityAdapterFactory.class.getSimpleName();

    public static SkinEntityAdapter buildAdapterFrom(Class<? extends SkinViewEntity> cls, DisplayImageOptions imageOptions) {
        if (cls == SkinCategory.class) {
            return new SkinBundleEntityAdapter(imageOptions);
        } else {
            SLog.i(TAG, "buildAdapterFrom return default SkinCategoryEntityAdapter for " + cls);
            return new SkinCategoryEntityAdapter(imageOptions);
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
            if (displayColumn > 0) {
                SkinEntityAdapter adapter = SkinEntityAdapterFactory.buildAdapterFrom(entity.getClass(), imageOptions);
                recyclerView.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), entity.getDisplayColumn());
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (isAdStub(entityList.get(position).getBundleName())) {
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

    // 点击皮肤首页的collection, category头部的"更多"， 显示下一级列表
    public static void onMoreItemClicked(Context context, SkinViewEntity entity) {
        Class cls = entity.getClass();
        if (SkinCategory.class == cls) {
            startActivityFor(context, SkinCategoryActivity.class, entity.getBundleName());
        } else if (SkinCategoryGroup.class == cls) {
            startActivityFor(context, SkinCategoryGroupActivity.class, entity.getBundleName());
        } else {
            SLog.e(TAG, "onMoreItemClicked, unexpected More for type: " + cls);
        }
    }
    private static void startActivityFor(Context context, Class<? extends FragmentActivity> target, String bundleName) {
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
