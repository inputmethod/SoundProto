package com.typany.ui.skinui.custom.fixcrash;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunhang on 17-6-9.
 */

public class SeekBarUtils {
//    public static SeekBarStateListDrawable getThumbDrawable(Context context) {
//        SeekBarStateListDrawable stateDrawable = new SeekBarStateListDrawable();
//        Drawable normal = ContextCompat.getDrawable(context, R.drawable.thumb_small);
//        Drawable pressed = ContextCompat.getDrawable(context, R.drawable.thumb_big);
//        Drawable disabled = ContextCompat.getDrawable(context, R.drawable.seek_thumb_disable);
//        stateDrawable.addState(State.STATE_PRESSED, pressed);
//        stateDrawable.addState(State.STATE_NORMAL, normal);
//        stateDrawable.addState(State.STATE_DISABLED, disabled);
//
//        return stateDrawable;
//   }

    public static SeekBarStateListDrawable getVolumeThumbDrawable(int color, int startColor, int endColor, int size) {
        SeekBarStateListDrawable stateDrawable = new SeekBarStateListDrawable();
        int colors[] = { startColor , endColor };
        GradientDrawable normal = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        normal.setSize(size, size);
        normal.setCornerRadius(size / 2);

        // TODO
        //int pressedColor = SoundPickerUtils.getAlphaColor(endColor, 0.8f);
        int pressedColor = 0;
        GradientDrawable pressed = new GradientDrawable();
        pressed.setSize(size + 4, size + 4);
        pressed.setCornerRadius(size / 2 + 2);
        pressed.setColor(pressedColor);

        GradientDrawable disabled = new GradientDrawable();
        disabled.setSize(size, size);
        disabled.setCornerRadius(size / 2);
        disabled.setColor(color);

        stateDrawable.addState(State.STATE_PRESSED, pressed);
        stateDrawable.addState(State.STATE_NORMAL, normal);
        stateDrawable.addState(State.STATE_DISABLED, disabled);

        return stateDrawable;
    }

   enum State {
       STATE_NORMAL,
       STATE_PRESSED,
       STATE_DISABLED
   }

   public static class SeekBarStateListDrawable {
       private State mCurrentState = State.STATE_NORMAL;
       private NullDrawable mNullDrawable = new NullDrawable();

       private HashMap<State, Drawable> mMap = new HashMap<>();

       public void addState(State state, Drawable drawable) {
           mMap.put(state, drawable);
       }

       public void setState(State state) {
           mCurrentState = state;
       }

       public Drawable getCurrent() {
           if (mCurrentState == null || mMap.get(mCurrentState) == null) {
               return mNullDrawable;
           }

           return mMap.get(mCurrentState);
       }

       public void setColorFilter(int color, PorterDuff.Mode mode) {
           for (Map.Entry<State, Drawable> entry : mMap.entrySet()) {
               entry.getValue().setColorFilter(color, mode);
           }
       }
   }



   public static class NullDrawable extends Drawable {
       @Override
       public void draw(@NonNull Canvas canvas) {}

       @Override
       public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {}

       @Override
       public void setColorFilter(@Nullable ColorFilter colorFilter) {}

       @Override
       public int getOpacity() {
           return PixelFormat.UNKNOWN;
       }
   }
}
