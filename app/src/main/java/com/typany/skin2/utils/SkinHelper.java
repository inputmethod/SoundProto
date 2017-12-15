package com.typany.skin2.utils;

import android.text.TextUtils;

/**
 * Created by yangfeng on 2017/12/15.
 */

public class SkinHelper {
    public static boolean isAdStub(String bundleName) {
        return TextUtils.equals(bundleName, "ad_top_name") || TextUtils.equals(bundleName, "ad_name");
    }
}
