package com.typany.skin.skinssfloader;

/**
 * Created by sunhang on 16-1-19.
 */
public interface IWidgets {
    /**
     * online
     */
    int STATUS_NORMAL = 0;
    /**
     * is downloading
     */
    int STATUS_DOWNLOADING = 1;
    /**
     * retry to download
     */
    int STATUS_RETRY = 2;
    /**
     * is applied
     */
    int STATUS_APPLED = 3;
    /**
     * has been downloaded
     */
    int STATUS_LOCAL = 4;

    void updateStatus(Status status);
    void updateProgress(float progress);

    class Status {
        public int status;
    }
}
