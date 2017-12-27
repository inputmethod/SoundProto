package com.typany.api.skin;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/5.
 */

public class SkinWrapper {
    private int code;
    private String baseResUrl;
    private int total_page;
    private List<Skin> sounds;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public List<Skin> getSounds() {
        return sounds;
    }

    public void setSounds(List<Skin> sounds) {
        this.sounds = sounds;
    }
}
