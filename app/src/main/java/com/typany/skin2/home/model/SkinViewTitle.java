package com.typany.skin2.home.model;

/**
 * Created by yangfeng on 2017/12/19.
 */

public class SkinViewTitle extends SkinViewEntity {
    private boolean hasMore;
    private boolean isSmall;
    private String titleOf;

    @Override
    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setSmall(boolean small) {
        isSmall = small;
    }

    // todo: collection和trending是41, all theme, categories, 和feature要35，MMP
    public boolean isSmall() {
        return isSmall;
    }

    public String getTitleOf() {
        return titleOf;
    }

    public void setTitleOf(String titleOf) {
        this.titleOf = titleOf;
    }
}
