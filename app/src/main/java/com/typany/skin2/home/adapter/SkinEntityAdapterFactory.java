package com.typany.skin2.home.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinCategory;
import com.typany.skin2.home.model.SkinViewEntity;

/**
 * Created by yangfeng on 2017/12/15.
 */

public class SkinEntityAdapterFactory {
    private static final String TAG = SkinEntityAdapterFactory.class.getSimpleName();

    public static SkinEntityAdapter buildFrom(Class<? extends SkinViewEntity> cls, DisplayImageOptions imageOptions) {
        if (cls == SkinCategory.class) {
            return new SkinBundleEntityAdapter(imageOptions);
        } else {
            SLog.i(TAG, "buildFrom return default SkinCategoryEntityAdapter for " + cls);
            return new SkinCategoryEntityAdapter(imageOptions);
        }
    }
}
