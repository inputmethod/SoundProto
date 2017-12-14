package com.typany.skin2.home.model;

/**
 * Created by dingbei on 11/19/2017.
 */

// one item in home page level one
// could be category with items or item itself
public class SkinViewEntity {
    private String bundleName;
    private String previewUrl;

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
