package com.typany.ui.sticker;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.typany.ime.IMEApplicationContext;
import com.typany.skin.skinssfloader.IWidgets;
import com.typany.soundproto.R;

/**
 * Created by sunhang on 16-1-21.
 */
public class DictWidgets implements IWidgets {
    private ProgressBar mProgressBar;
    private Button mBtnCancel;
    private TextView mTvStatus;
//    private RadioButton mRadioButton;

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public void setCancelButton(Button btn) {
        mBtnCancel = btn;
    }

    public void setStatusTextView(TextView tv) {
        mTvStatus = tv;
    }

//    public void setRadioButton(RadioButton radioButton) {
//        this.mRadioButton = radioButton;
//    }

    @Override
    public void updateStatus(Status status) {
        final int s = status.status;
        switch (s) {
            case STATUS_RETRY:
            case STATUS_NORMAL:
                mProgressBar.setVisibility(View.INVISIBLE);
                mBtnCancel.setVisibility(View.INVISIBLE);
                mTvStatus.setVisibility(View.VISIBLE);
//                mRadioButton.setVisibility(View.INVISIBLE);
                mTvStatus.setText(IMEApplicationContext.getAppContext().getText(R.string.select_to_download));
                break;
            case STATUS_DOWNLOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
                mTvStatus.setVisibility(View.INVISIBLE);
//                mRadioButton.setVisibility(View.INVISIBLE);
                break;
            case STATUS_APPLED:
                mProgressBar.setVisibility(View.INVISIBLE);
                mBtnCancel.setVisibility(View.INVISIBLE);
                mTvStatus.setVisibility(View.VISIBLE);
//                mRadioButton.setVisibility(View.VISIBLE);
                mTvStatus.setText(IMEApplicationContext.getAppContext().getText(R.string.wizard_first_step_button_text));
                break;
            case STATUS_LOCAL:
                mProgressBar.setVisibility(View.INVISIBLE);
                mBtnCancel.setVisibility(View.INVISIBLE);
                mTvStatus.setVisibility(View.VISIBLE);
//                mRadioButton.setVisibility(View.VISIBLE);
                mTvStatus.setText(IMEApplicationContext.getAppContext().getText(R.string.wizard_first_step_button_text));
                break;
        }
    }

    @Override
    public void updateProgress(float progress) {
        mProgressBar.setProgress((int) (progress * 100));
    }
}
