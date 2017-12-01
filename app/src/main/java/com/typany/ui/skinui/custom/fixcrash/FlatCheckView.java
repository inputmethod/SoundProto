package com.typany.ui.skinui.custom.fixcrash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 */

public class FlatCheckView extends View {
    private boolean checked;
    private SeekBarUtils.SeekBarStateListDrawable mThumb;
    private SeekBarBackground mDrawableBkg;
    private SeekBarProgress mDrawableProgress;
    private int mStripMargin;
    private float trackWidth;
    private int thumbSize;
    private OnCheckedListener mOnProgressChangeListener;

    public FlatCheckView(Context context) {
        super(context);
        setUp(context);
    }

    public FlatCheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    private void setUp(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        trackWidth = density * 14f;
        thumbSize = (int) (density * 20f);
        mStripMargin = 0; //Math.round(density * 10f);

        mDrawableBkg = new SeekBarBackground(context);
        mDrawableProgress = new SeekBarProgress(context);
        mDrawableBkg.setRadius(trackWidth / 2);
        mDrawableProgress.setRadius(trackWidth / 2);
    }

    public void setOnCheckedListener(OnCheckedListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }

    public void setDrawableProgress(SeekBarProgress drawableProgress) {
        mDrawableProgress = drawableProgress;
    }

    public void setDrawableBkg(SeekBarBackground drawableBkg) {
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

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (this.checked) {
            mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
        } else {
            mThumb.setState(SeekBarUtils.State.STATE_DISABLED);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int l = mStripMargin;
        int t = Math.round(height / 2 - trackWidth / 2);
        int r = width - mStripMargin;
        int b = Math.round(height / 2 + trackWidth / 2);

        if (checked) {
            if (mDrawableProgress != null) {
                mDrawableProgress.setBounds(l, t, r, b);
                mDrawableProgress.draw(canvas);
            }
        } else {
            if (mDrawableBkg != null) {
                mDrawableBkg.setBounds(l, t, r, b);
                mDrawableBkg.draw(canvas);
            }
        }

        if (mThumb != null) {
            int centerY = height / 2;
            Drawable drawable = mThumb.getCurrent();

            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();

            if (checked) {
                l = width - mStripMargin - drawableWidth;
                r = width - mStripMargin;
            } else {
                l = mStripMargin;
                r = mStripMargin + drawableWidth;
            }

            t = centerY - drawableHeight / 2;
            b = centerY + drawableHeight / 2;

            drawable.setBounds(l, t, r, b);
            drawable.draw(canvas);
        }
    }

//    @Override
//    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
//        if (enabled) {
//            mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
//        } else {
//            mThumb.setState(SeekBarUtils.State.STATE_DISABLED);
//        }
//    }

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

    private void handleActionDown(MotionEvent event) {
        mThumb.setState(SeekBarUtils.State.STATE_PRESSED);
        invalidate();
    }

    private void handleActionMove(MotionEvent event) {
        invalidate();
    }

    private void handleActionUp(MotionEvent event) {
        checked = !checked;
        mOnProgressChangeListener.onCheckedChanged(this, checked);

        mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
        invalidate();
    }

    private void handleActionCancel(MotionEvent event) {
        mThumb.setState(SeekBarUtils.State.STATE_NORMAL);
        invalidate();
    }

    public interface OnCheckedListener {
        void onCheckedChanged(FlatCheckView seekBar, boolean check);
    }

//    public void initSeekBar(Context context) {
//        mDrawableBkg = new SeekBarBackground(context);
//        mDrawableBkg.setRadius(trackWidth / 2);
//        mDrawableProgress = new SeekBarProgress(context);
//    }

    public void applySeekThumbColor(int normalColor, int highlightStartColor, int highlightEndColor) {
        setThumb(SeekBarUtils.getVolumeThumbDrawable(normalColor, highlightStartColor, highlightEndColor, thumbSize));
    }

    public void applyTrackColor(int normalColor, int highlightColor) {
        mDrawableBkg.setColor(normalColor);
        mDrawableProgress.setColor(highlightColor);
    }
}
