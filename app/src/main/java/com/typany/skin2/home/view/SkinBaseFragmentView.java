package com.typany.skin2.home.view;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.network.StatefulResource;
import com.typany.skin2.home.adapter.SkinEntityAdapter;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.views.RecyclerFragment;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/15.
 */

abstract public class SkinBaseFragmentView extends RecyclerFragment {
    private static final String TAG = SkinBaseFragmentView.class.getSimpleName();

    private SkinEntityAdapter skinBundleAdapter;

    @Override
    // 这个基类保存了observer和它的子类生成的adapter, 用final禁止子类重载此方法，并强制通过getAdapter来
    // 提供adapter实例，然后observer收到数据变化时往adapter里设置在本类完成，对所有子类透明。另一方面，
    // 上层基类实现更抽象的功能，不允许把皮肤相关的类信息(如adapter, observer，及data model)往上暴露，
    // 以免污染其它类空间(如音效的)。
    protected final SkinEntityAdapter instanceAdapter(DisplayImageOptions options) {
        skinBundleAdapter = getAdapter(options);
        return skinBundleAdapter;
    }

    protected abstract SkinEntityAdapter getAdapter(DisplayImageOptions options);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startObserver(observer);
    }

    private final Observer<StatefulResource<List<SkinViewEntity>>> observer = new Observer<StatefulResource<List<SkinViewEntity>>>() {
        @Override
        public void onChanged(@Nullable StatefulResource<List<SkinViewEntity>> skinResource) {
            if (skinResource.status == StatefulResource.Status.LOADING)
                drawLoading();
            else if (skinResource.status == StatefulResource.Status.SUCCESS)
                skinBundleAdapter.setSkinItemList(skinResource.data);
            else
                SLog.e(TAG, "onChanged: status is " + skinResource.status);
        }
    };
}
