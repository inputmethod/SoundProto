package com.typany.skin2.storage;

import android.content.res.AssetManager;
import android.support.annotation.WorkerThread;

import com.typany.ime.IMEApplicationContext;
import com.typany.skin2.model.SkinPackage;
import com.typany.skin2.upgrade.SkinUpgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import typany.keyboard.Skin;

/**
 * Created by wujian on 11/29/2017.
 */

public class SkinStorageEntry {
    private final String name;
    private final String zipFolder;
    private final String resolution;
    private final File[] possibleStoragePath;

    private static final String SKIN_PB = "skin.pb";
    private static final String[] presetSkinNames = {"1001001042", "1001001054", "Blue", "Dark2_0", "White2_0", "7000000864"};


    @WorkerThread
    public SkinStorageEntry(final String name, final String zipFolder, final String resolution, final File[] path) {
        this.name = name;
        this.zipFolder = zipFolder;
        this.resolution = resolution;
        this.possibleStoragePath = path;
    }

    @WorkerThread
    public SkinPackage load() throws Exception {
        // load directory
        File skinFolder = getSkinFolder(name);
        if (skinFolder == null)
            throw new Exception();

        // load protobuf
        File pb = new File(skinFolder, SKIN_PB);
        FileInputStream fis = new FileInputStream(pb);
        Skin.AllSkins as = Skin.AllSkins.parseFrom(fis);

        String resourcePath = getWritableFolder().getAbsolutePath()
                        + File.separator + name
                        + File.separator + resolution;

        SkinPackage sp = SkinPackage.buildPackage(as, resourcePath);
        return sp;
    }

    @WorkerThread
    private String getResourcePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(getWritableFolder().getAbsolutePath())
                .append('/')
                .append(name)
                .append("/")
                .append(resolution);
        return sb.toString();
    }

    @WorkerThread
    private File getSkinFolder(final String name) {
        // 先看SD卡下有没有
        File skinFolder = getSkinFolder(name, possibleStoragePath[0]);
        if (skinFolder == null) {
            // 没有就看data data
            skinFolder = getSkinFolder(name, possibleStoragePath[1]);
            if (skinFolder == null) {
                // 再没有就从assert里解压出来
                skinFolder = reloadSkinFromZip(name);
            }
        }
        return skinFolder;
    }

    @WorkerThread
    private File getSkinFolder(final String name, final File storage) {
        File skinFolder = null;
        if (storage != null) {
            skinFolder = new File(storage, name);
            if (!isValidSkinFolder(skinFolder)) {
                skinFolder.delete();
                skinFolder = null;
            }
        }
        return skinFolder;
    }

    @WorkerThread
    private boolean isValidSkinFolder(final File folder) {
        boolean valid = false;
        if (folder.exists() && folder.isDirectory()) {
            File pb = new File(folder, SKIN_PB);
            valid = pb.exists() && pb.isFile();
        }
        return valid;
    }

    @WorkerThread
    private File reloadSkinFromZip(final String name) {
        File skinFolder = null;
        for (int i = 0; i < presetSkinNames.length; i++) {
            if (name == presetSkinNames[i]) {
                skinFolder = unzipFromAssertToFolder(name, getWritableFolder());
                break;
            }
        }

        // try upgrade
        if (!SkinUpgrade.upgradeSkin(skinFolder, SKIN_PB, resolution+"/phoneSkin.ini"))
            skinFolder = null;

        // everything is ok, return folder
        // something like sd://skin/Blue
        return skinFolder;
    }

    @WorkerThread
    private File getWritableFolder() {
        return possibleStoragePath[0] != null ? possibleStoragePath[0] : possibleStoragePath[1];
    }

    @WorkerThread
    private File unzipToFolder(final String name, final File folder) {
        return SkinExtractor.extractSkin(name, new File(zipFolder, name + ".ssf"), folder);
    }

    @WorkerThread
    private File unzipFromAssertToFolder(final String name, final File destFolder) {
        AssetManager mgr = IMEApplicationContext.application.getAssets();
        InputStream in;
        try {
            in = mgr.open(zipFolder + File.separator +  name + ".ssf");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return SkinExtractor.extractSkin(name, in, destFolder);
    }


}
