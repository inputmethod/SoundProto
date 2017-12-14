package com.typany.skin2.home.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.network.StatefulResource;
import com.typany.skin2.home.adapter.SkinAdapter;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.home.model.SkinViewModel;
import com.typany.views.RecyclerFragment;

import java.util.List;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinHomeView extends RecyclerFragment {
    private static final String TAG = SkinHomeView.class.getSimpleName();
    private SkinAdapter skinAdapter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RecyclerView.Adapter instanceAdapter(DisplayImageOptions options) {
        skinAdapter = new SkinAdapter(options);
        return skinAdapter;
    }

    @Override
    protected int getColumnCount() {
        return 1;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startObserver(observer);
    }

    @Override
    protected LiveData getLiveData() {
        return getViewModel(SkinViewModel.class).getHomePage();
    }

    private final Observer<StatefulResource<List<SkinViewEntity>>> observer = new Observer<StatefulResource<List<SkinViewEntity>>>() {
        @Override
        public void onChanged(@Nullable StatefulResource<List<SkinViewEntity>> skinResource) {
            if (skinResource.status == StatefulResource.Status.LOADING)
                drawLoading();
            else if (skinResource.status == StatefulResource.Status.SUCCESS)
                skinAdapter.setSkinItemList(skinResource.data);
            else
                Log.e(TAG, "onChanged: status is " + skinResource.status);
        }
    };
}
