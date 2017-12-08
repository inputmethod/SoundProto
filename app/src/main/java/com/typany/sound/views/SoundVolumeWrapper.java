package com.typany.sound.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.typany.debug.SLog;
import com.typany.settings.SettingField;
import com.typany.settings.SettingMgr;
import com.typany.skin.SkinConstants;
import com.typany.soundproto.R;
import com.typany.ui.skinui.custom.fixcrash.FixCrashSeekBar;
import com.typany.ui.skinui.custom.fixcrash.FlatCheckView;
import com.typany.ui.skinui.custom.fixcrash.SeekBarBackground;
import com.typany.ui.skinui.custom.fixcrash.SeekBarProgress;
import com.typany.ui.skinui.custom.fixcrash.SeekBarUtils;

/**
 * Created by yangfeng on 2017/9/23.
 */

public class SoundVolumeWrapper {
    private static final String TAG = SoundVolumeWrapper.class.getSimpleName();

    private SettingMgr mgr;
    private float mSoundVolume;
    private FixCrashSeekBar soundSeek;
    private FlatCheckView soundSwitch;

    public SoundVolumeWrapper(View hostView) {
        hostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing but intercept the pressing key event.
            }
        });

        soundSeek = (FixCrashSeekBar) hostView.findViewById(R.id.seekbar);
        soundSeek.setOnProgressChangeListener(mSeekBarChangeListener);
        soundSwitch = (FlatCheckView) hostView.findViewById(R.id.sv_switch);
        if (null != soundSwitch) {
            soundSwitch.setOnCheckedListener(checkListener);
        }

        initSeekBar(hostView.getContext());
    }

    private void initValues() {
        if (null != soundSwitch) {
            boolean keySound = Boolean.parseBoolean(initMgr().getValue(SettingField.TYPING_SOUND_ENABLE));
            SLog.d(TAG, "initValues sound " + keySound);
            soundSwitch.setChecked(keySound);
            soundSet(keySound);
        }

        setValue();
    }

    private void setEnable(int id, boolean value) {
        if (id == R.id.sv_switch) {
            if (value) {
                soundSet(true);
                setSoundClickable(true);
            } else {
                soundSet(false);
                setSoundClickable(false);
            }
        }
    }

    private void setSoundClickable(boolean value){
//        soundSelect.setEnabled(value);
//        soundSelect2.setEnabled(value);
//        soundSelect.setClickable(value);
//        soundSelect2.setClickable(value);
    }

    private void setValue() {
        mSoundVolume = Integer.parseInt(initMgr().getValue(SettingField.TYPING_SOUND_VOLUME));
        SLog.d(TAG, "setValue sound " + mSoundVolume);
        soundSeek.setProgress((int) mSoundVolume);
    }

    private void soundSet(boolean value) {
        if (value != soundSeek.isEnabled()) {
            soundSeek.setEnabled(value);
            soundSeek.invalidate();
        }
    }

    private void saveValue(int id, int value) {
        SLog.d(TAG, "saveValue id " + id + " value " + value);
        if (id == R.id.seekbar) {
            initMgr().setValue(SettingField.TYPING_SOUND_VOLUME, String.valueOf(value));
        } else {
            // should not be here
        }
    }

    private SettingMgr initMgr() {
        if (mgr == null) {
            mgr = SettingMgr.getInstance();
        }
        return mgr;
    }

    FixCrashSeekBar.OnProgressChangeListenerPlus mSeekBarChangeListener = new FixCrashSeekBar.OnProgressChangeListenerPlus() {
        @Override
        public void onProgressChanged(FixCrashSeekBar seekBar, int progress) {
            int id = seekBar.getId();
            saveValue(id, progress);
        }

        @Override
        public void onStopTrackingTouch(FixCrashSeekBar seekBar) {
            if (seekBar.getId() == R.id.seekbar) {
                // TODO
                //SoundPackageConf.playDefaultSound();
//                KeyPressEffectHelper.getInstance().performSoundFeedback();
//            } else if (seekBar.getId() == R.id.seekbar_vb) {
//                KeyPressEffectHelper.getInstance().performHapticFeedback();
            }
        }
    };

    FlatCheckView.OnCheckedListener checkListener = new FlatCheckView.OnCheckedListener() {
        @Override
        public void onCheckedChanged(FlatCheckView compoundButton, boolean b) {
            int id = compoundButton.getId();
            switch (id) {
                case R.id.sv_switch:
                    setEnable(id, b);
                    // TODO

                    //SoundPickerUtils.reportSoundSwitch();
                    SettingMgr.getInstance().setValue(SettingField.TYPING_SOUND_ENABLE, Boolean.toString(b));
                    break;
                default:
                    // should not be here.
                    break;
            }
        }
    };

    public void refreshUi() {
        initValues();
    }

    public void applyTheme(int normalColor, int highlightColor) {
        applySeekThumbColor(normalColor, highlightColor, highlightColor);
        applyProgressColor(normalColor, highlightColor);
        applyCompoundDrawable(normalColor, highlightColor);
        refreshUi();
    }

    private SeekBarProgress seekBarProgress;
    private SeekBarBackground seekBarBackground;

    void initSeekBar(Context context) {
        seekBarProgress = new SeekBarProgress(context);
        seekBarBackground = new SeekBarBackground(context);
        soundSeek.setMaxAndProgress(10, 0);
        soundSeek.setDrawableProgress(seekBarProgress);
        soundSeek.setDrawableBkg(seekBarBackground);

        int progressColor = ContextCompat.getColor(context, R.color.setting_text_summary);
        int highlightColor = ContextCompat.getColor(context, R.color.colorPrimary);
        applyTheme(progressColor, highlightColor);
    }

    private void applyCompoundDrawable(int normalColor, int highlightColor) {
        if (null == soundSwitch) {
            return;
        }

        int disableColor = Color.WHITE;
        int startColor = Color.WHITE;
        int endColor = Color.WHITE;

        Context context = soundSwitch.getContext();
        if (SkinConstants.isBlueSkin()) {
            disableColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_off_blue);
            startColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_on_blue);
            endColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_on_blue);
        } else if (SkinConstants.isWhiteSkin()) {
            startColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_on_start_white);
            endColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_on_end_white);
        } else if (SkinConstants.isDarkSkin()) {
            disableColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_off_dark);
            startColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_on_dark);
            endColor = ContextCompat.getColor(context, R.color.sound_switch_thumb_on_dark);
        } else {
        }

        soundSwitch.applyTrackColor(normalColor, highlightColor);
//        soundSwitch.applySeekThumbColor(normalColor, highlightColor, highlightColor);
        soundSwitch.applySeekThumbColor(disableColor, startColor, endColor);
    }

    private void applySeekThumbColor(int normalColor, int highlightStartColor, int highlightEndColor) {
        soundSeek.setThumb(SeekBarUtils.getVolumeThumbDrawable(normalColor, highlightStartColor, highlightEndColor, 34));
    }

    private void applyProgressColor(int normalColor, int highlightColor) {
        seekBarBackground.setColor(normalColor);
        seekBarProgress.setColor(highlightColor);
    }
}
