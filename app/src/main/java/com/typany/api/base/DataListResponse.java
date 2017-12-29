package com.typany.api.base;

/**
 * Created by yangfeng on 2017/12/28.
 */

public abstract class DataListResponse extends DataResponse {
    private String baseResUrl;
    private int total_page;

    public String getBaseResUrl() {
        return baseResUrl;
    }

    public void setBaseResUrl(String baseResUrl) {
        this.baseResUrl = baseResUrl;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
}
