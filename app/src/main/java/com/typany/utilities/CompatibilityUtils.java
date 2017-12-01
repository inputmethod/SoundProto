package com.typany.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.MainThread;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public final class CompatibilityUtils {
    private static RectF mTempRectF = new RectF();

    public static final void callViewSetBackground(View view,
                                                   Drawable background) {
        if (Build.VERSION.SDK_INT < 16) {
            oldSetBackground(view, background);
        } else {
            newSetBackground(view, background);
        }
    }

    public static final int callViewGetPaddingLeft(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getPaddingStart();
        } else {
            return view.getPaddingLeft();
        }
    }

    public static final int callViewGetPaddingRight(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getPaddingEnd();
        } else {
            return view.getPaddingRight();
        }
    }

    @MainThread
    public static final void callCanvasDrawRoundRect(Canvas canvas, int l, int t, int r, int b, float r0, float r1, Paint p) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(l, t, r, b, r0, r1, p);
        } else {
            mTempRectF.left = l;
            mTempRectF.top = t;
            mTempRectF.right = r;
            mTempRectF.bottom = b;
            canvas.drawRoundRect(mTempRectF, r0, r1, p);
        }

    }

    @SuppressWarnings("deprecation")
    private static final void oldSetBackground(View view, Drawable background) {
        view.setBackgroundDrawable(background);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static final void newSetBackground(View view, Drawable background) {
        view.setBackground(background);
    }

    public static Point getScreenSizeInPixel(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        if (Build.VERSION.SDK_INT < 13) {
            return getScreenSizeInPixel13Down(display);
        }
        Point p = getScreenSizeInPixel13Up(display);
        if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            int w = p.y;
            int h = p.x;
            p.set(w, h);
        }
        return p;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static Point getScreenSizeInPixel13Up(Display display) {
        Point p = new Point();
        display.getSize(p);
        return p;
    }

    @SuppressWarnings("deprecation")
    private static Point getScreenSizeInPixel13Down(Display display) {
        Point p = new Point();
        p.x = display.getWidth();
        p.y = display.getHeight();
        return p;
    }

    public static int getVersionCode(Context context, String packageName) {
        int versionCode = -1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVersionName(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
