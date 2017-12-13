package com.typany.sound.views;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.base.lifecycle.ProcessScopeViewModelProviders;
import com.typany.network.StatefulResource;
import com.typany.sound.adapter.SoundAdapter;
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.viewmodel.SoundViewModel;
import com.typany.soundproto.R;
import com.typany.ui.skinui.LoadingFragment;
import com.typany.ui.skinui.interfaces.IVolumeEditor;
import com.typany.utilities.universalimageloader.ImageLoaderHelper;
import com.typany.views.RecyclerFragment;

import java.util.List;

/**
 * Created by yangfeng on 2017/9/18.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SoundFragment extends RecyclerFragment {
    private static final String TAG = SoundFragment.class.getSimpleName();

    private DisplayImageOptions mOptions;
    private LoadingFragment mLoadingFragment;
    private SoundAdapter soundAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IVolumeEditor) {
            IVolumeEditor editor = (IVolumeEditor) context;
            volumeEditorFragment = editor.getVolumeEditorFragment();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mOptions = ImageLoaderHelper.newOptions(getContext(), true, Bitmap.Config.ARGB_8888);
        mLoadingFragment = new LoadingFragment();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected RecyclerView.Adapter instanceAdapter() {
        soundAdapter = new SoundAdapter(mOptions,false);
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

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fl_container, mLoadingFragment);
        ft.commit();
    }

    public void drawLoading() {
        // TODO
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showRecyclerView();
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


    private void showRefreshingViews() {
        if (mLoadingFragment != null && mLoadingFragment.isHidden()) {
            mLoadingFragment.show(true);
        }
    }

    private void showRecyclerView() {
        if (mLoadingFragment != null) {
            mLoadingFragment.hide();
        }
    }
}
