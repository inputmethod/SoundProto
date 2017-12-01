package com.typany.sound.play;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.media.SoundPool;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.typany.base.lifecycle.ProcessScopeViewModelProviders;
import com.typany.ime.IMEApplicationContext;
import com.typany.network.StatefulResource;
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.service.SoundBundle;
import com.typany.sound.viewmodel.SoundViewModel;

/**
 * Created by dingbei on 11/12/2017.
 */

public class SoundPlayer implements LifecycleOwner {
    private SoundBoundItem currentSelected;
    private LifecycleRegistry lifecycleRegistry;

    private SoundPool soundPool;
    private int currentStreamId = -1;
    private static final int defaultPriority = 1;
    private static final int defaultRate = 1;

    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    public void play(int vol) {
        if (currentStreamId != -1)
            soundPool.play(currentStreamId, vol, vol, defaultPriority, 0, defaultRate);
    }

    public SoundPlayer() {
        currentSelected = ProcessScopeViewModelProviders.of(IMEApplicationContext.getApp())
                .get(SoundViewModel.class)
                .getSelectedItem();
        currentSelected.getAsLiveData().observe(this, new Observer<StatefulResource<SoundBundle>>() {
            @MainThread
            @Override
            public void onChanged(@Nullable StatefulResource<SoundBundle> soundBundleStatefulResource) {
                switch (soundBundleStatefulResource.status) {
                    case SUCCESS:
                        loadSound(soundBundleStatefulResource.data.getSoundFile());
                        break;
                    case ERROR:
                        // TODO
                        break;
                }
            }
        });
    }

    @MainThread
    public void loadSound(final String soundPath) {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                if (i1 == 0)
                    currentStreamId = i;
            }
        });

        // TODO how does load work...
        // TODO cache loaded sounds
        soundPool.load(soundPath, 0);
    }
}
