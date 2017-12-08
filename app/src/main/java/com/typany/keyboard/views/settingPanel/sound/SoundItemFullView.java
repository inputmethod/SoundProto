package com.typany.keyboard.views.settingPanel.sound;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.typany.sound.service.SoundBundle;
import com.typany.sound.views.ProgressBarView;
import com.typany.soundproto.R;
import com.typany.ui.sticker.DictWidgets;

/**
 * Created by yangfeng on 2017/12/8.
 */

public class SoundItemFullView extends SoundItemView {
    private View downloadingMask;
    private View remoteIndicator;
    private ProgressBarView progressBarView;

    public SoundItemFullView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void parseHolderViews() {
        super.parseHolderViews();

        downloadingMask = findViewById(R.id.downloading_layout);
        progressBarView = (ProgressBarView) findViewById(R.id.view);
        remoteIndicator = findViewById(R.id.sound_remote_indicator);
    }

    @Override
    protected void drawSoundBundle(SoundBundle item) {
        super.drawSoundBundle(item);

        remoteIndicator.setVisibility(isRemotePreviewUrl(item.previewUrl()) ? View.VISIBLE : View.GONE);
    }

    private boolean isRemotePreviewUrl(String url) {
        return null != url && url.startsWith("http://");
    }

    public void startLoading(SoundBundle model) {
        downloadingMask.setVisibility(View.VISIBLE);
        remoteIndicator.setVisibility(View.GONE);
    }

    public void updateProgress(int schedule) {
        progressBarView.setCurrentProgress(schedule);
    }

    public void onDownloadCompleted() {
        downloadingMask.setVisibility(View.GONE);
    }

    public void updateProgressState(int state) {
        if (state == DictWidgets.STATUS_DOWNLOADING) {
            downloadingMask.setVisibility(View.VISIBLE);
        } else {
            downloadingMask.setVisibility(View.GONE);
        }
    }

    public void onDownloadFailed() {
        downloadingMask.setVisibility(View.GONE);
        remoteIndicator.setVisibility(View.VISIBLE);
    }
}
