package com.typany.base.storage;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by dingbei on 11/11/2017.
 */

public class ProtobufBasedStorage<ProtobufType> {
    private ProtobufType pb = null;
    private String pbPath = null;
    @Nullable
    @WorkerThread
    public ProtobufType load(final String localPath) {
        pbPath = localPath;
        try {
            FileInputStream fis = new FileInputStream(localPath);

//            pb = ProtobufType.parseFrom(fis);

        } catch (Exception e) {
            // TODO log it
//            pb = ProtobufType.newBuilder().build();
        }
        return pb;
    }

    @WorkerThread
    public void save(ProtobufType pb) {
        if (pb != null && pbPath != null) {
            try {
                FileOutputStream fos = new FileOutputStream(pbPath);
//                pb.writeTo(fos);
            } catch (Exception e) {
                // TODO log
            }
        }
    }
}
