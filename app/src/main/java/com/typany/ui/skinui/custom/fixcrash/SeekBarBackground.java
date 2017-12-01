package com.typany.ui.skinui.custom.fixcrash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by sunhang on 17-6-9.
 */

public class SeekBarBackground extends Drawable {
    private Paint mPaint = new Paint();
    private float radius = 0f;
    private final RectF roundRect = new RectF();

    public SeekBarBackground(Context context) {
        mPaint.setColor(Color.RED);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect r = getBounds();
        if (radius > 0f) {
            roundRect.left = r.left;
            roundRect.top = r.top;
            roundRect.right = r.right;
            roundRect.bottom = r.bottom;
            canvas.drawRoundRect(roundRect, radius, radius, mPaint);
        } else {
            canvas.drawRect(r, mPaint);
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidateSelf();
    }
}
