package com.typany.skin2.home.view;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.base.lifecycle.ProcessScopeViewModelProviders;
import com.typany.network.StatefulResource;
import com.typany.sound.adapter.SoundAdapter;
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.viewmodel.SoundViewModel;
import com.typany.views.RecyclerFragment;

import java.util.List;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinHomeView extends RecyclerFragment {
    private static final String TAG = SkinHomeView.class.getSimpleName();
    private SoundAdapter soundAdapter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RecyclerView.Adapter instanceAdapter(DisplayImageOptions options) {
        soundAdapter = new SoundAdapter(options,false);
        return soundAdapter;
    }

    @Override
    protected int getColumnCount() {
        return 2;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ProcessScopeViewModelProviders.of(getActivity().getApplication()).get(SoundViewModel.class)
                .loadFullRepository().observe(this, new Observer<StatefulResource<List<SoundBoundItem>>>() {
            @Override
            public void onChanged(@Nullable StatefulResource<List<SoundBoundItem>> soundRemoteRepositoryStatefulResource) {
                if (soundRemoteRepositoryStatefulResource.status == StatefulResource.Status.LOADING)
                    drawLoading();
                else if (soundRemoteRepositoryStatefulResource.status == StatefulResource.Status.SUCCESS)
                    soundAdapter.setSoundItemList(soundRemoteRepositoryStatefulResource.data);
                else
                    Log.e(TAG, "onChanged: status is " + soundRemoteRepositoryStatefulResource.status);
            }
        });
    }
}
