package com.typany.skin2.home.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinCategory;
import com.typany.skin2.home.model.SkinViewEntity;

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
}
