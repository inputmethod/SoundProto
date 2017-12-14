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
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.viewmodel.SoundViewModel;
import com.typany.views.RecyclerFragment;

import java.util.List;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinHomeView extends RecyclerFragment {
    private static final String TAG = SkinHomeView.class.getSimpleName();
    private SkinAdapter soundAdapter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RecyclerView.Adapter instanceAdapter(DisplayImageOptions options) {
        soundAdapter = new SkinAdapter(options,false);
        return soundAdapter;
    }

    @Override
    protected int getColumnCount() {
        return 2;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startObserver(observer);
    }

    @Override
    protected LiveData getLiveData() {
        return getViewModel(SoundViewModel.class).loadFullRepository();
    }

    private final Observer<StatefulResource<List<SoundBoundItem>>> observer = new Observer<StatefulResource<List<SoundBoundItem>>>() {
        @Override
        public void onChanged(@Nullable StatefulResource<List<SoundBoundItem>> soundRemoteRepositoryStatefulResource) {
            if (soundRemoteRepositoryStatefulResource.status == StatefulResource.Status.LOADING)
                drawLoading();
            else if (soundRemoteRepositoryStatefulResource.status == StatefulResource.Status.SUCCESS)
                soundAdapter.setSoundItemList(soundRemoteRepositoryStatefulResource.data);
            else
                Log.e(TAG, "onChanged: status is " + soundRemoteRepositoryStatefulResource.status);
        }
    };
}
