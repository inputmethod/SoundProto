package com.typany.skin2.home.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangfeng on 2017/12/14.
 */

public class SkinBundle extends SkinViewEntity {
    private String fileUrl;
    private long fileSize;
    private boolean canShare;
    private boolean hotFlag;
    private final List<String> iconList = new ArrayList<>();
    private String apkUrl;
    private boolean adShow;
    private boolean select = false;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isCanShare() {
        return canShare;
    }

    public void setCanShare(boolean canShare) {
        this.canShare = canShare;
    }

    public boolean isHotFlag() {
        return hotFlag;
    }

    public void setHotFlag(boolean hotFlag) {
        this.hotFlag = hotFlag;
    }

    public List<String> getIconList() {
        return iconList;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public boolean isAdShow() {
        return adShow;
    }

    public void setAdShow(boolean adShow) {
        this.adShow = adShow;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
