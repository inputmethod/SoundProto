package com.typany.api.skin;

/**
 * Created by yangfeng on 2017/12/5.
 */

public class Skin {
    private int id;
    private String sn;
    private String ppu;
    private String sdu;
    private String md5;
    private String preview;
    private long fz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPpu() {
        return ppu;
    }

    public void setPpu(String ppu) {
        this.ppu = ppu;
    }

    public String getSdu() {
        return sdu;
    }

    public void setSdu(String sdu) {
        this.sdu = sdu;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public long getFz() {
        return fz;
    }

    public void setFz(long fz) {
        this.fz = fz;
    }
}
