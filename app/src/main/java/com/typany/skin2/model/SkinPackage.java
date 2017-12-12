package com.typany.skin2.model;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import typany.keyboard.Skin;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinPackage {
    private Skin.AllSkins skinContent;
    private String resourcePath;
    private static final SkinPackage emptySkin = new SkinPackage(null, "");

    @WorkerThread
    public SkinPackage(final Skin.AllSkins as, final String resourcePath) {
        this.skinContent = as;
        this.resourcePath = resourcePath;
        if (skinContent == null)
            skinContent = Skin.AllSkins.newBuilder().build();
    }

    @WorkerThread
    public static SkinPackage buildPackage(final Skin.AllSkins as, final String resourcePath) {
        SkinPackage sp = new SkinPackage(as, resourcePath);
        return sp;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    @WorkerThread
    public static SkinPackage emptySkin() {
        return emptySkin;
    }

    @MainThread
    @Nullable
    public Skin.AllSkins getSkinContent() {
        return skinContent;
    }

    @MainThread
    public boolean isEmpty() {
        return skinContent == null;
    }

    @MainThread
    public Skin.KeyboardSkin getKeyboardSkin(int type) {
        if (type == 0)
            return skinContent.getFull();
        else
            return skinContent.getSoduku();
    }

    @MainThread
    public Skin.ClipboardSkin getClipboardSkin() {
        return skinContent.getClipboard();
    }

    @MainThread
    public Skin.SettingPanelSkin getSettingPanelSkin() {
        return skinContent.getSettingPanel();
    }
}
