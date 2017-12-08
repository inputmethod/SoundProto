package com.typany.sound.service;


import android.support.annotation.NonNull;

import com.typany.sound.SoundPersistentRepository;

import java.util.Map;

/**
 * Created by yangfeng on 2017/9/22.
 */
public class SoundBundle {
    private static final String TAG = SoundBundle.class.getSimpleName();

    private String bundleName;
    private String soundFile;
    private String previewUrl;
    private Status status = Status.INFO_LOADED;
    private Map<Integer, Integer> vocalPosition;
    private boolean select = false;
    private boolean toLoadRemoteData = false;

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        INFO_LOADED, DATA_LOADED
    }

    public SoundBundle(final String bundleName, final boolean select, final String previewUrl) {
        this.bundleName = bundleName;
        this.select = select;
        this.previewUrl = previewUrl;
    }

    public Status getStatus() {
        return status;
    }
    public String getSoundFile() {
        return soundFile;
    }
    public String bundleName() {
        return bundleName;
    }
    public String previewUrl() { return previewUrl; }

    public boolean isToLoadRemoteData() {
        return toLoadRemoteData;
    }

    public void setToLoadRemoteData(boolean toLoadRemoteData) {
        this.toLoadRemoteData = toLoadRemoteData;
    }

    public void setBundleContent(final @NonNull SoundPersistentRepository.PositionInfo data) {
        if (null == data) {
            // do nothing while it is remote sound bundle, which will load and save
            // data later
            toLoadRemoteData = true;
        } else {
            SoundPersistentRepository.VocalPosition vp = data.getVocalPosition();
            vocalPosition = vp.getFunctionKeyMap();
            status = Status.DATA_LOADED;
            toLoadRemoteData = false;
        }
    }

    public boolean shouldFetch() {
        return status == Status.INFO_LOADED && toLoadRemoteData;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean getSelect() {
        return select;
    }
}
