package com.typany.resource;

import android.content.Context;

public interface IResourceHolder {
    void onCreate(Context appContext);
    void onDestroy();
}