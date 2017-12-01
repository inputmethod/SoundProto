package com.typany.utilities.universalimageloader;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.utilities.UtilsForSh;

/**
 * Created by yangfeng on 2017/11/17.
 */

public class ImageLoaderHelper {
    public static DisplayImageOptions newOptions(Context context, boolean cacheOnDisk, Bitmap.Config config) {
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(cacheOnDisk)
                .showImageOnFail(UtilsForSh.generatePlaceHolderDrawable(context))
                .showImageOnLoading(UtilsForSh.generatePlaceHolderDrawable(context))
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
    }
}
