package com.typany.sound.play;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

import com.typany.debug.SLog;
import com.typany.resource.IResourceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SoundHolder implements IResourceHolder {
    private static final String TAG = SoundHolder.class.getSimpleName();

    private static final String SOUND_TRACK_FILENAME = "tap_key.ogg";

    private SoundPool mSoundPool;
    private int mSoundTrackId = -1;
    private int mPlayTrackId = -1;
    private Thread releaseThread;

    public SoundHolder() {
    }

    @Override
    public void onCreate(Context appContext) {
        infoLog("onCreate, mSoundPool = " + mSoundPool);
        // modified by sunhang : change stream type from FX_KEYPRESS_STANDARD to STREAM_MUSIC.
        // this can fix a issue(http://10.134.74.226:880/browse/GIME-1429)
        // note : the pm don't want to use music channel. (20170920)
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        // TODO
        //AssetFileDescriptor descriptor = SoundPickerUtils.openFd(appContext, "sound", SOUND_TRACK_FILENAME);
        //mSoundTrackId = tryLoadAsset(descriptor);

        tryLoadPendingPreviewTrack(appContext);


        releaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                infoLog("release runner, mSoundPool = " + mSoundPool);
                if (mSoundPool != null) {
                    try {
                        if (mSoundTrackId != -1) {
                            mSoundPool.stop(mSoundTrackId);
                        }
                        mSoundPool.release();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        mSoundPool = null;
                    }
                }
            }
        });
    }

    public void stopCurrentSound() {
        if (mSoundPool != null && mPlayTrackId != -1) {
            try {
                //these two methods may lead to unexpcted error.
                mSoundPool.stop(mPlayTrackId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void unloadCurrentConf() {
//        try {
//            if (null != conf) {
//                if (mSoundPool != null) {
//                    List<Integer> trackIds = conf.getAllTrackIds();
//                    SLog.d(TAG, "unloadCurrentConf, try to unload track id size: " + trackIds.size());
//                    for (int id : trackIds) {
//
//                        mSoundPool.unload(id);
//                    }
//                }
//                conf.clearAllTracks();
//                conf = null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    //private SoundPackageConf conf = null;
    private String confName = null;
    public void reloadAssertFolder(Context context, String assertFolderName, String defaultTone) {
        infoLog("reloadAssertFolder, mSoundPool = " + mSoundPool);
        reloadDefaultTone(context, defaultTone);
        reloadAssertFolder(context, assertFolderName);
    }

    public void reloadAssertFolder(Context context, String assertFolderName) {
//        infoLog("reloadAssertFolder, mSoundPool = " + mSoundPool);
//        unloadCurrentConf();
//
//        confName = assertFolderName;
//        String downloadPath = SoundPickerUtils.getDownloadedFolder(context, assertFolderName);
//        conf = SoundPickerUtils.loadSound(context, assertFolderName);
//        if (SoundPackageConf.isEmpty(conf)) {
//            SLog.e(TAG, "reloadAssertFolder failed for " + assertFolderName);
//        } else {
//            List<String> fileNameList = conf.getAllFileNameList();
//            if (null != downloadPath) {
//                for (String fileName : fileNameList) {
//                    int trackId = mSoundPool.load(downloadPath + File.separator + fileName, 1);
//                    conf.addTrack(fileName, trackId);
//                }
//            } else {
//                String folderName = "sound/suite/" + assertFolderName;
//                for (String fileName : fileNameList) {
//                    AssetFileDescriptor fd = SoundPickerUtils.openFd(context, folderName, fileName);
//                    conf.addTrack(fileName, tryLoadAsset(fd));
//                }
//            }
//
//            SLog.d(TAG, "reloadAssertFolder " + confName + ", add track " + conf.trackFileNames.size() +
//                    ", name2track size " + conf.nameToTrackId.size());
//        }
    }

    public void playCurrentPreview(String previewFile, float vol) {
        if (null == previewFile) {
            SLog.w(TAG, "playCurrentPreview, ooops previewFile = " + previewFile);
        } else {
            Integer track = previewTrackMap.get(previewFile);
            if (null == track) {
                SLog.w(TAG, "playCurrentPreview, ooops not cached previewFile = " + previewFile);
            } else {
                playTone(track, vol);
                SLog.i(TAG, "playCurrentPreview, in cached preview track: " + track + ", for " + previewFile);
                return;
            }
        }

    }

    private void reloadDefaultTone(Context appContext, String filePath) {
//        stopCurrentSound();
//        try {
//            File newKeyToneFile = new File(filePath);
//            if (TextUtils.isEmpty(filePath) || !new File(filePath).exists() || newKeyToneFile.length() > 102400) {
//                AssetFileDescriptor descriptor = SoundPickerUtils.openFd(appContext, "sound", SOUND_TRACK_FILENAME);
//                mSoundTrackId = tryLoadAsset(descriptor);
//            } else if (mSoundPool != null) {
//                mSoundTrackId = mSoundPool.load(filePath, 1);
//            } else {
//                SLog.e(TAG, "reloadThemeFolder while sound pool is null.");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            mSoundTrackId = -1;
//        }
    }

//    public void reloadThemeFolder(Context appContext, String folder, SoundPackageConf conf, String defaultTone) {
//        infoLog("reloadThemeFolder, mSoundPool = " + mSoundPool);
//        unloadCurrentConf();
//        reloadDefaultTone(appContext, defaultTone);
//
//        confName = folder;
//        if (SoundPackageConf.isEmpty(conf)) {
//            SLog.e(TAG, "reloadThemeFolder failed for " + folder);
//        } else {
//            List<String> fileNameList = conf.getAllFileNameList();
//            for (String fileName : fileNameList) {
//                int trackId = mSoundPool.load(folder + File.separator + fileName, 1);
//                conf.addTrack(fileName, trackId);
//            }
//
//            SLog.d(TAG, "reloadThemeFolder " + confName + ", add track " + conf.trackFileNames.size() +
//                    ", name2track size " + conf.nameToTrackId.size());
//        }
//    }

    private int tryLoadAsset(AssetFileDescriptor descriptor) {
        try {
            return mSoundPool.load(descriptor, 1);
        } catch (Exception e) {
            return  -1;
        } finally {
            if (descriptor != null) {
                try {
                    descriptor.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mSoundPool != null && releaseThread!=null){
            releaseThread.start();
        }
    }

    public void playKeyTone(float vol) {
        playTone(mSoundTrackId, vol);
    }

    public void playKeyTone(String keyType, int keyCode, int row, int col, float vol) {
        final int trackId;
//        if (TextUtils.isEmpty(keyType)) {
//            trackId = SoundPackageConf.isEmpty(conf) ?
//                    mSoundTrackId : conf.getTrackIdByCode(keyCode, row, col);
//        } else {
//            trackId = SoundPackageConf.isEmpty(conf) ? mSoundTrackId : conf.getTrackIdByType(keyType);
//        }
//
//        playTone(trackId, vol);
    }

    public void playTone(int trackId, float vol) {
        SLog.d(TAG, "playTone trackId " + trackId + ", volume " + vol + ", mSoundPool = " + mSoundPool);
        if (mSoundPool != null) {
            if (trackId != -1) {
                mPlayTrackId = mSoundPool.play(trackId, vol, vol, 1, 0, 1);
            }
        } else {
            throw new RuntimeException("Use IME resource without IME service instance existing.");
        }
    }

    private void infoLog(String msg) {
        SLog.i(TAG, msg);
    }

    private HashMap<String, Integer> previewTrackMap = new HashMap<>();
    private ArrayList<String> pendingPreviewFileList = new ArrayList<>();
    private void tryLoadPendingPreviewTrack(Context context) {
//        for (String previewFile : pendingPreviewFileList) {
//            if (SoundPickerUtils.isAssertsPreviewFile(previewFile)) {
//                loadAssertPreviewTrack(context, previewFile);
//            } else {
//                loadDownloadedPreviewTrack(previewFile);
//            }
//        }
    }
    public void loadAssertPreviewTrack(Context context, String assertFile) {
//        if (null == mSoundPool) {
//            if (!pendingPreviewFileList.contains(assertFile)) {
//                pendingPreviewFileList.add(assertFile);
//            }
//            SLog.i(TAG, "loadAssertPreviewTrack, pending to load preview track of " + assertFile);
//        } else {
//            if (previewTrackMap.containsKey(assertFile)) {
//                // need to do nothing while it was cached
//            } else {
//                AssetFileDescriptor fd = SoundPickerUtils.openFd(context, assertFile);
//                int trackId = tryLoadAsset(fd);
//                if (trackId > 0) {
//                    previewTrackMap.put(assertFile, trackId);
//                    SLog.i(TAG, "loadAssertPreviewTrack, cached track " + trackId + " for " + assertFile);
//                } else {
//                    SLog.e(TAG, "loadAssertPreviewTrack, failed to load preview track: " + assertFile);
//                }
//            }
//        }
    }

    public void loadDownloadedPreviewTrack(String previewFile) {
        if (null == mSoundPool) {
            if (!pendingPreviewFileList.contains(previewFile)) {
                pendingPreviewFileList.add(previewFile);
            }
            SLog.i(TAG, "loadAssertPreviewTrack, pending to load preview track of " + previewFile);
        } else {
            if (previewTrackMap.containsKey(previewFile)) {
                // need to do nothing
            } else {
                int trackId = mSoundPool.load(previewFile, 1);
                if (trackId > 0) {
                    previewTrackMap.put(previewFile, trackId);
                    SLog.i(TAG, "loadAssertPreviewTrack, cached track " + trackId + " for " + previewFile);
                } else {
                    SLog.e(TAG, "loadAssertPreviewTrack, failed to load preview track: " + previewFile);
                }
            }
        }
    }
}