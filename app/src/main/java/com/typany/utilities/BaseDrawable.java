package com.typany.utilities;

import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

/**
 * Created by sunhang on 17-8-14.
 */

public abstract class BaseDrawable extends Drawable{

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {}

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
