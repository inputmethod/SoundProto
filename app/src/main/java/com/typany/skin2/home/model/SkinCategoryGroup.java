package com.typany.skin2.home.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangfeng on 2017/12/14.
 * a group of category of skin view entity
 */
public class SkinCategoryGroup extends SkinViewEntity {
    private boolean hasMore;
    private final List<SkinViewEntity> bundleList = new ArrayList<>();
    private int displayColumn;
    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<SkinViewEntity> getBundleList() {
        return bundleList;
    }

    public int getDisplayColumn() {
        return displayColumn;
    }

    public void setDisplayColumn(int displayColumn) {
        this.displayColumn = displayColumn;
    }
}
