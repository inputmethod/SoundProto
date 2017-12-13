package com.typany.sound.views;

import android.arch.lifecycle.Observer;
import android.content.Context;
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
import com.typany.ui.skinui.interfaces.IVolumeEditor;
import com.typany.views.RecyclerFragment;

import java.util.List;

/**
 * Created by yangfeng on 2017/9/18.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SoundFragment extends RecyclerFragment {
    private static final String TAG = SoundFragment.class.getSimpleName();
    private SoundAdapter soundAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IVolumeEditor) {
            IVolumeEditor editor = (IVolumeEditor) context;
            volumeEditorFragment = editor.getVolumeEditorFragment();
        }
    }

    @Override
    protected RecyclerView.Adapter instanceAdapter(DisplayImageOptions options) {
        soundAdapter = new SoundAdapter(options,false);
        return soundAdapter;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        showRecyclerView();
        update();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            update();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        if (null != getEditorFragment() && getEditorFragment().isShow()) {
            getEditorFragment().refreshUi();
        }

        if (null != soundAdapter) {
            soundAdapter.notifyDataSetChanged();
        }
    }

    private VolumeEditorFragment volumeEditorFragment;

    private VolumeEditorFragment getEditorFragment() {
        return volumeEditorFragment;
    }
    private void showSoundItemOption() {
        if (getEditorFragment() != null && !getActivity().isFinishing()) {
            getEditorFragment().show();
        }
    }
}
