package com.typany.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.typany.soundproto.R;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by sunhang on 17-8-17.
 */

public class UtilsForSh {
    public static final String TAG_EMOJI_UI = "TAG_EMOJI_UI";
    public static final String TAG_ADS_SCROLL = "TAG_ADS_SCROLL";
    public static final String TAG_REWARDS_ADS = "TAG_REWARDS_ADS";
    public static final String TAG_ADVANCED_ADS = "TAG_ADVANCED_ADS";

    public static void closeIO(Closeable io) {
        if (io == null) {
            return;
        }

        try {
            io.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean extThemeZipDir(final File source, final File targetDir) {
        try {
            return extThemeZipDir(new FileInputStream(source), targetDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean extThemeZipDir(final InputStream is, final File targetDir) {
        ZipInputStream in = null;
        ZipEntry entry;
        boolean result = true;
        try {
            in = new ZipInputStream(is);
            while ((entry = in.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                String entryName = entry.getName();
                entryName = entryName.substring(entryName.lastIndexOf('/') + 1);
                String fullName = new File(targetDir, entryName).getAbsolutePath();
                ZipUtils.doOutputFile(in, fullName);
                in.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * get the width of screen in portrait mode.
     * @param context
     * @return
     */
    public static int getPortraitScreenWidth(Context context) {
        Resources res = context.getResources();
        int w = res.getDisplayMetrics().widthPixels;
        int h = res.getDisplayMetrics().heightPixels;

        return w < h ? w : h;
    }

    public static FrameLayout initiateAdsFrameLayout(Context context) {
        FrameLayout fl = new InterceptFocusRequestFrameLayout(context);
        fl.setClickable(false);

        return fl;
    }

    public static class InterceptFocusRequestFrameLayout extends FrameLayout {

        public InterceptFocusRequestFrameLayout(@NonNull Context context) {
            super(context);
        }

        @Override
        protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
//            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
            return false;
        }
    }


    public static PlaceHolderDrawable generatePlaceHolderDrawable(Context context) {
        return new PlaceHolderDrawable(context);
    }

    public static class PlaceHolderDrawable extends BaseDrawable {
        private Drawable base;

        public PlaceHolderDrawable(Context context) {
            base = ContextCompat.getDrawable(context, R.drawable.skin_default);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            base.draw(canvas);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);

            base.setBounds(bounds);
        }
    }

    public static class BaseAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    public static class StateDrawable extends BaseDrawable {
        public enum KeyState {
            NORMAL,
            PRESSED
        }
        private Map<KeyState, Drawable> mDrawables = new HashMap<>();
        private KeyState mKeyState;

        @Override
        public void draw(@NonNull Canvas canvas) {
            Drawable drawable = mDrawables.get(mKeyState);
            if (drawable == null) {
                return;
            }

            drawable.draw(canvas);
        }

        public void setState(KeyState state) {
            mKeyState = state;
        }

        public void addState(KeyState keyState, Drawable drawable) {
            mDrawables.put(keyState, drawable);
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            Drawable drawable = mDrawables.get(mKeyState);
            if (drawable == null) {
                return;
            }

            drawable.setBounds(left, top, right, bottom);
        }
    }
}
