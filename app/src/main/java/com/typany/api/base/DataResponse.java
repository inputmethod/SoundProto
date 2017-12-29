package com.typany.api.base;

/**
 * Created by yangfeng on 2017/12/28.
 */

public abstract class DataResponse<T> {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
