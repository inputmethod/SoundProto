package com.typany.skin2.storage;

import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.InputStream;

/**
 * Created by wujian on 11/29/2017.
 */

public class SkinExtractor {
    public static File extractSkin(final String skinName, final File zipFile, final File destFolder) {
        File skinfolder = getSkinFolder(skinName, destFolder);

        ZipUtil.unpack(zipFile, skinfolder);

        return skinfolder;
    }

    public static File extractSkin(final String skinName, final InputStream in, final File destFolder) {
        File skinFolder = getSkinFolder(skinName, destFolder);

        ZipUtil.unpack(in, skinFolder);
        return skinFolder;
    }

    private static File getSkinFolder(String skinName, File destFolder) {
        File skinFolder = new File(destFolder, skinName);
        if (!skinFolder.exists()) skinFolder.mkdirs();

        return skinFolder;
    }
}
