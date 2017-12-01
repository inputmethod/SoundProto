package com.typany.ui.skinui.custom.fixcrash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.typany.utilities.CommonUtils;

/**
 * Created by sunhang on 17-6-9.
 */

public class FixCrashSeekBar extends View {
    private int mMax;
    private int mProgress;
    /**
     * [0,1]
     */
    private float mFloatProgress;
    private SeekBarUtils.SeekBarStateListDrawable mThumb;
    private Drawable mDrawableBkg;
    private Drawable mDrawableProgress;
    private int mStripMargin;
    private float mStripHeight;
    private OnProgressChangeListener mOnProgressChangeListener;

    public FixCrashSeekBar(Context context) {
        super(context);
        setUp(context);
    }

    public FixCrashSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    private void setUp(Context context) {
        mStripHeight = CommonUtils.convertDipToPixel(context, 2.7f);
        mStripMargin = CommonUtils.convertDipToIntPixel(context, 12f);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }

    public void setDrawableProgress(Drawable drawableProgress) {
        setupDrawableCorner(drawableProgress);
        mDrawableProgress = drawableProgress;
    }

    private void setupDrawableCorner(Drawable drawable) {
        if (drawable instanceof GradientDrawable) {
            float density = getResources().getDisplayMetrics().density;
            ((GradientDrawable) drawable).setCornerRadius(density * 1.666667f);
        }
    }

    public void setDrawableBkg(Drawable drawableBkg) {
        setupDrawableCorner(drawableBkg);
        mDrawableBkg = drawableBkg;
    }

    public SeekBarUtils.SeekBarStateListDrawable getThumb() {
        return mThumb;
    }

    public void setThumb(SeekBarUtils.SeekBarStateListDrawable drawable) {
        mThumb = drawable;
    }

    public void setStripMargin(int margin) {
        mStripMargin = margin;
    }

    public void setMaxAndProgress(int max, int progress) {
        mMax = max;

        setProgress(progress);
    }

    public void setProgress(int progress) {
        if (mMax == 0) {
            return;
        }

        mProgress = progress;
        mFloatProgress = (float)progress / (float)mMax;
    }

    public int getMax() {
        return mMax;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (mDrawableBkg != null) {
            int l = mStripMargin;
            int t = Math.round(height / 2 - mStripHeight / 2);
            int r = width - mStripMargin;
            int b = Math.round(height / 2 + mStripHeight / 2);
            mDrawableBkg.setBounds(l, t, r, b);
            mDrawableBkg.draw(canvas);
        }

        if (mDrawableProgress != null && isEnabled()) {
            int l = mStripMargin;
            int t = Math.round(height / 2 - mStripHeight / 2);
            int r = l + Math.round(mFloatProgress * (width - mStripMargin * 2));
            int b = Math.round(height / 2 + mStripHeight / 2);
            mDrawableProgress.setBounds(l, t, r, b);
            mDrawableProgress.draw(canvas);
        }

        if (mThumb != null) {
            int centerX = mStripMargin + Math.round(mFloatProgress * (width - mStripMargin * 2));
            int centerY = height / 2;
            Drawable drawable = mThumb.getCurrent();

            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();

            int l = centerX - drawableWidth / 2;
            int t = centerY - drawableHeight / 2;
            int r = centerX + drawableWidth / 2;
            int b = centerY + drawableHeight / 2;

            drawable.setBounds(l, t, r, b);
            drawable.draw(canvas);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
        } else {
            mThumb.setState(SeekBarUtils.State.STATE_DISABLED);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        getParent().requestDisallowInterceptTouchEvent(true);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                handleActionCancel(event);
                break;
        }

        return true;
    }

    private void manipulateProgress(MotionEvent event) {
        manipulateProgress(event, false);
    }

    private void manipulateProgress(MotionEvent event, boolean upEvent) {
        float x = event.getX() - mStripMargin;
        final float width = getWidth();

        if (x < 0) {
            x = 0;
        }
        if (x > width - mStripMargin * 2) {
            x = width - mStripMargin * 2;
        }


        float p = x / (width - mStripMargin * 2);
        mFloatProgress = p;
        mProgress = Math.round(mMax * mFloatProgress);

        if (mOnProgressChangeListener != null) {
            if (upEvent && mOnProgressChangeListener instanceof OnProgressChangeListenerPlus) {
                OnProgressChangeListenerPlus listenerPlus = (OnProgressChangeListenerPlus) mOnProgressChangeListener;
                listenerPlus.onStopTrackingTouch(this);
            } else {
                mOnProgressChangeListener.onProgressChanged(this, mProgress);
            }
        }
    }

    private void handleActionDown(MotionEvent event) {
        mThumb.setState(SeekBarUtils.State.STATE_PRESSED);
        manipulateProgress(event);
        invalidate();
    }

    private void handleActionMove(MotionEvent event) {
        manipulateProgress(event);
        invalidate();
    }

    private void handleActionUp(MotionEvent event) {
        manipulateProgress(event, true);
        mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
        invalidate();
    }

    private void handleActionCancel(MotionEvent event) {
        mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
        invalidate();
    }

    public interface OnProgressChangeListener {
        void onProgressChanged(FixCrashSeekBar seekBar, int progress);
    }

    public interface OnProgressChangeListenerPlus extends OnProgressChangeListener {
        void onStopTrackingTouch(FixCrashSeekBar seekBar);
    }
}
