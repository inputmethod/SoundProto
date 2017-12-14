package com.typany.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.typany.debug.SLog;
import com.typany.soundproto.R;

/**
 * Created by liuzhuang on 2017/8/10.
 */

public class TtfTextView extends TextView {
    private static final String TAG ="TextView";
    Typeface tfRegular = null;
    Typeface tfMedium = null;
    Typeface tfLight = null;

    public TtfTextView(Context context) {
        super(context);
    }

    public TtfTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TtfTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TtfTextView);
        String customFont = a.getString(R.styleable.TtfTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);

        this.getPaint().setAntiAlias(true);
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;

        if(!Resources.getSystem().getConfiguration().locale.getLanguage().startsWith("en")){
            return false;
        }
        if (asset == null || asset.equalsIgnoreCase("null")) {
            asset = "fonts/Rubik-Regular.ttf";
        }
        String font;
        if (!asset.contains("fonts")){
            font = "fonts/" + asset;
        } else {
            font = asset;
        }

        if (font.contains("Regular")) {
            tf = getTfRegular(ctx, font);
        } else if (font.contains("Medium")) {
            tf = getTfMedium(ctx, font);
        } else if (font.contains("Light")) {
            tf = getTfLight(ctx, font);
        } else {
            try {
                tf = Typeface.createFromAsset(ctx.getAssets(), font);
            } catch (Exception e) {
                SLog.e(TAG,"Could not get typeface:"+e.getMessage() + " " + asset);
                return false;
            }
        }

        setTypeface(tf);
        return true;
    }

    public Typeface getTfRegular(Context ctx, String font) {
        if (tfRegular == null) {
            try {
                tfRegular = Typeface.createFromAsset(ctx.getAssets(), font);
            } catch (Exception e) {
                SLog.e(TAG,"Could not get typeface:"+e.getMessage() + " " + font);
            }
        }
        return tfRegular;
    }

    public Typeface getTfMedium(Context ctx, String font) {
        if (tfMedium == null) {
            try {
                tfMedium = Typeface.createFromAsset(ctx.getAssets(), font);
            } catch (Exception e) {
                SLog.e(TAG,"Could not get typeface:"+e.getMessage() + " " + font);
            }
        }
        return tfMedium;
    }

    public Typeface getTfLight(Context ctx, String font) {
        if (tfLight == null) {
            try {
                tfLight = Typeface.createFromAsset(ctx.getAssets(), font);
            } catch (Exception e) {
                SLog.e(TAG,"Could not get typeface:"+e.getMessage() + " " + font);
            }
        }
        return tfLight;
    }
}
