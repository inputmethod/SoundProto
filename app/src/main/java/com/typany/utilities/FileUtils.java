package com.typany.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v4.content.ContextCompat;
import android.util.Xml;

import com.typany.debug.SLog;
import com.typany.skin.SkinConstants;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by feixuezheng on 2015/12/8.
 */
public class FileUtils {

    private static final String TAG =  FileUtils.class.getSimpleName();
    static char hexdigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static ExecutorService mMoveCrashFileService;

    public static boolean removeSingleFile(File file) {
        try {
            if (file.exists()) {
                File targFile = new File(file.getAbsolutePath() + "tmp");
                file.renameTo(targFile);
                targFile.delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {
                return true;
            }
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }

        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String getMD5String(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return "";
        }
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            long s = System.currentTimeMillis();
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            //32
            byte[] b = md.digest();
            String md5 = byteToHexString(b);
            SLog.i("FileUtils", "get md5 for file :" + md5);
            return md5;

            // 16
            // return buf.toString().substring(8, 24);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean copyAssertFile(Context context, String assertFilePath, String targetPath, String targetName) {
        InputStream is = null;
        AssetManager am = context.getAssets();
        try {
            is = am.open(assertFilePath);
            ZipUtils.outputFile(is, targetPath, targetName);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void copyFileToFolder(String oldPath, String targetFolder, String newName, boolean overwrite) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                File targetFolderFile = new File(targetFolder);
                if (!targetFolderFile.exists()) {
                    targetFolderFile.mkdirs();
                }
                String newPath = targetFolder + File.separator + newName;
                File targetFile = new File(newPath);
                if (targetFile.exists()) {
                    if (overwrite == false) {
                        return;
                    } else {
                        targetFile.delete();
                    }
                }
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[10240];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public static String readFileContent(File file){
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = null;
        byte[] content = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fis = new FileInputStream(file);
            fis.read(content);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return new String(content);
    }

    public static void enableLangDictionary(Context context, String langId) {

    }

//    public static void unzipDictionary(Context context, Boolean overwrite) {
//        if (DictConstants.COPY_BUILTIN_DICT || context == null) {
//            return;
//        }
//        String dictZipPath = DictConstants.ASSERT_DICT_PATH + DictConstants.BUILT_IN_DICT_NAME;
//        File targetDictFolder = StoragePathProvider.getDictionaryFolder(context.getApplicationContext());
//        // new File(context.getApplicationContext().getFilesDir(), GlobalConfiguration.DICTIONARY_FOLDER_NAME);
//        DictConstants.COPY_BUILTIN_DICT = true;
//        boolean needUnzip = false;
//        if (!targetDictFolder.exists()) {
//            SLog.i("UnzipDict", "not found target dict folder, create it:" + targetDictFolder.getAbsolutePath());
//            targetDictFolder.mkdirs();
//            needUnzip = true;
//        } else {
//            String[] curVer = SettingMgr.getInstance().getValue(SettingField.DICT_VERSION).split(";");
//            String[] builtin = GlobalConfiguration.BUILTIN_DICT.split(";");
//            String verion = GlobalConfiguration.BUILTIN_DICT_VER;
//            for (int i = 0; i < builtin.length; i++) {
//                //if target dictionary exist, skip copy.
//                File dictFile = new File(StoragePathProvider.getDictionaryFolder(context), StoragePathProvider.makeDictionaryFileName(builtin[i]));
//
//                if (dictFile.exists()) {
//                    int currentDictVersion = 0;
//                    try {
//                        currentDictVersion = CoreUtil.getDictionaryVersion(dictFile.getAbsolutePath());
//                    }
//                    catch (Exception ex){
//                        ex.printStackTrace();
//                    }
//                    SLog.i("UnzipDict","get current built in dict "+builtin[i]+"'s dict version: "+currentDictVersion);
//                    if (Integer.parseInt(verion) > currentDictVersion) {
//                        SLog.i("UnzipDict","need replace built in dict");
//                        FileUtils.removeSingleFile(dictFile);
//                        needUnzip = true;
//                    }
////                    else {
////                        for (String record : curVer) {
////                            if (record.startsWith(builtin[i])) {
////                                String curVerison = record.split("=")[1];
////                                String recordVersion = verion;
////                                if (Integer.parseInt(curVerison) < Integer.parseInt(recordVersion)) {
////                                    needUnzip = true;
////                                    break;
////                                }
////                            }
////                        }
////                    }
//                } else {
//                    SLog.i("UnzipDict", "skip unzip dict file.");
//                    needUnzip = true;
//                    break;
//                }
//            }
//        }
//        if (needUnzip) {
//            SLog.i("UnzipDict", "Copy built in english dict to dictionary folder.");
//            ZipUtils.extract(dictZipPath, targetDictFolder.getAbsolutePath() + File.separator, true, context);
//            SettingMgr.getInstance().setValue(SettingField.EN_DICT_VERSION, GlobalConfiguration.BUILTIN_DICT_VER);
//            final AppRuntime ar = AppRuntime.getInstance();
//            if (ar != null) {
//                ar.sendMessage(Messages.MSG_BUILTIN_EXTRACTED, null);
//            }
//        }
//        DictConstants.COPY_BUILTIN_DICT = false;
//    }

    public static String joinFilePath(String...params) {
        if (params == null && params.length == 0)
            throw new RuntimeException();

        StringBuilder builder = new StringBuilder(params[0]);
        for (int i = 1; i < params.length; i++) {
            builder.append(File.separator).append(params[i]);
        }

        return builder.toString();
    }

    public static File openFile(String ... params) {
        if (params == null && params.length == 0)
            throw new RuntimeException();

        // TODO [WuJian] are these two ifs necessary?
        if (params.length == 1)
            return new File(params[0]);
        if (params.length == 2)
            return new File(params[0], params[1]);

        return new File(joinFilePath(params));
    }

    public static File getChildFile(File folder, String fileName) {
        File file = null;
        try {
            file = new File(folder, fileName);
        } catch (Exception e) {
            file = null;
        }
        return file;
    }

    public interface IParseXMLCallback {
        Object parseNodeToModel(XmlPullParser parser, InputStream content);
    }

    public static List<Object> parseXMLModels(IParseXMLCallback func, InputStream content, String node) {
        List<Object> models = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(content, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals(SkinConstants.THEME_ATTR_INFO_NODE)) {
                        SkinConstants.BASE_RES_HOST_URL = parser.getAttributeValue(null, SkinConstants.THEME_ATTR_HOST_URL_ATTR);
                    }
                    if (name.equals(node)) {
                        Object model = func.parseNodeToModel(parser, content);
                        if (model != null && !models.contains(model)) {
                            models.add(model);
                        }
                    }
                }
                eventType = parser.next();
            }
            //saveNameInfoIntoFile(models);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                content.close();
                content = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return models;
    }

    public interface IParseXMLVolleyCallback {
        Object parseNodeToModel(XmlPullParser parser);
    }

    public static List<Object> parseXMLVolleyModels(IParseXMLVolleyCallback func, XmlPullParser parser, String node) {
        List<Object> models = new ArrayList<>();
//        XmlPullParser parser = Xml.newPullParser();
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals(SkinConstants.THEME_ATTR_INFO_NODE)) {
                        SkinConstants.BASE_RES_HOST_URL = parser.getAttributeValue(null, SkinConstants.THEME_ATTR_HOST_URL_ATTR);
                    }
                    if (name.equals(node)) {
                        Object model = func.parseNodeToModel(parser);
                        if (model != null && !models.contains(model)) {
                            models.add(model);
                        }
                    }
                }
                eventType = parser.next();
            }
            //saveNameInfoIntoFile(models);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return models;
    }

//    public static String parseXMLVolleyKeyboard(XmlPullParser parser) {
//        String result = null;
//        try {
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_TAG) {
//                    String name = parser.getName();
//                    if (name.equals(KeyboardChecker.KEYBOARD_VERSION_NODE)) {
//                        result = parser.getAttributeValue(null, KeyboardChecker.KEYBOARD_VERSION_VERSION) + "," +
//                                parser.getAttributeValue(null, KeyboardChecker.KEYBOARD_VERSION_PATH);
//                    }
//                }
//                eventType = parser.next();
//            }
//            //saveNameInfoIntoFile(models);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    public static String parseXMLVolleyUpdate(XmlPullParser parser) {
//        String result = null;
//        try {
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_TAG) {
//                    String name = parser.getName();
//                    if (name.equals(UpdateChecker.UPDATE_VERSION_NODE)) {
//                        result = parser.getAttributeValue(null, UpdateChecker.UPDATE_VERSION_VERSION) + "=" +
//                                parser.getAttributeValue(null, UpdateChecker.UPDATE_VERSION_LABEL) + "=" +
//                                parser.getAttributeValue(null, UpdateChecker.UPDATE_VERSION_DES) + "=" +
//                                parser.getAttributeValue(null, UpdateChecker.UPDATE_VERSION_PATH);
//                    }
//                }
//                eventType = parser.next();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    public static boolean ifFileExsits(String filePath) {
        File jpgFile = new File(filePath);
        if (jpgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private static String byteToHexString(byte[] tmp) {
        String s;
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexdigits[byte0 >>> 4 & 0xf];
            str[k++] = hexdigits[byte0 & 0xf];
        }
        s = new String(str);
        return s;
    }

    public static Map<String, String> loadKeyValueFromFile(Context context, String path, boolean isInAssert, String seperator) {
        HashMap<String, String> resultSet = new HashMap<>();
        InputStream is = null;
        try {
            if (isInAssert) {
                is = context.getAssets().open(path);
            } else {
                File file = new File(path);
                is = new FileInputStream(file);
            }
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int sepIndex;
            while ((line = bufferedReader.readLine()) != null) {
                sepIndex = line.indexOf(seperator);
                resultSet.put(line.substring(0, sepIndex), line.substring(sepIndex + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultSet;
    }

    public static String getSharedPreferenceValue(Context context, String sharedFileName, String key, String defValue) {
        Context appContext = context.getApplicationContext();
        SharedPreferences prefs = appContext.getSharedPreferences(
                sharedFileName, Context.MODE_PRIVATE);
        return prefs
                .getString(key, defValue);
    }

    public static void setSharedPreferenceValue(Context context, String sharedFileName, String key, String value) {
        Context appContext = context.getApplicationContext();
        SharedPreferences prefs = appContext.getSharedPreferences(
                sharedFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readResData(Context cont, int resId) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = cont.getResources().openRawResource(resId);
            byte[] buf = new byte[1024];
            int i;
            while ((i = inputStream.read(buf)) > 0) {
                byteArrayOutputStream.write(buf, 0, i);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res = byteArrayOutputStream.toString();
        if (res.isEmpty()) {
            res = "[]";
        }
        //SLog.d(TAG, "readResData id " + res);
        return res;
    }

    public static String readResData(String resName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = new FileInputStream(resName);
            byte[] buf = new byte[1024];
            int i;
            while ((i = inputStream.read(buf)) > 0) {
                byteArrayOutputStream.write(buf, 0, i);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res = byteArrayOutputStream.toString();
        //SLog.d(TAG, "readResData String " + res);

        // added by sunhang : close the stream
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    static AssetManager assetManager;
    public static void copyAssets(Context context, String path, String outPath) {
        if (assetManager == null){
            assetManager = context.getAssets();
        }
        String assets[];
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(context, path, outPath);
            } else {
                String fullPath = outPath + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    if (!dir.mkdir()) SLog.e(TAG, "No create external directory: " + dir );
                for (String asset : assets) {
                    copyAssets(context, path + "/" + asset, outPath);
                }
            }
        } catch (IOException ex) {
            SLog.e(TAG, "I/O Exception", ex);
        }
    }
    private static void copyFile(Context context, String filename, String outPath) {
        if (assetManager == null){
            assetManager = context.getAssets();
        }

        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(filename);
            String newFileName = outPath + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            SLog.e(TAG, e.getMessage());
        }
    }

    public static void copyFile(String oldFile, String newFile) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(oldFile);
//            in = new FileInputStream(oldFile.getAbsolutePath());
//            String newFileName = outPath + "/" + filename;
            out = new FileOutputStream(newFile);
//            out = new FileOutputStream(newFile.getAbsolutePath());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            SLog.e(TAG, e.getMessage());
        }
    }

    public static boolean saveContentToFile(String response, File targetFile) {
        SLog.v(TAG, "save strategy to file");
        try {
            File parentFolder = new File(targetFile.getParent());
            if(!parentFolder.exists()){
                parentFolder.mkdirs();
            }

            FileWriter writer = new FileWriter(targetFile);
            writer.write(response);
            writer.flush();
            writer.close();
            SLog.v(TAG, "save strategy file succeed.");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //filter sticker files
//    public static File[] getFilterFiles(String fileDir) {
//        final File dir = new File(fileDir);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//
//        File[] files = dir.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                if (pathname.getName().endsWith(".part")) {
//                    return false;
//                } else if (pathname.getName().contains(StickerSender.STICK_TAIL_NAME)) {
//                    return false;
//                }
//                return true;
//            }
//        });
//        return files;
//    }

    public static ArrayList<File> resortFiles(File[] files) {
        ArrayList<File> arrayFiles = new ArrayList<File>(Arrays.asList(files));
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(arrayFiles, new Comparator<File>() {
                public int compare(File o2, File o1) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
        } catch (Exception e) {
            SLog.d("Liu", "Collections.sort error " + e);
        }
        return arrayFiles;
    }

    // get the root file of android external storage for context, which might be null or
    // look like "xxxx/Android/com.typany.ime/files/
    // See to: https://developer.android.com/guide/topics/data/data-storage.html#filesExternal
    public static File getExternalFilesDirs(Context context) throws IOException {
        File[] dirs = ContextCompat.getExternalFilesDirs(context, null);
        if (null != dirs) {
            return dirs[0];
        } else {
            throw new IOException("getExternalFilesDirs failed to return external root path.");
        }
    }

    public static File ensureFolder(File parent, String folder) throws IOException {
        if (null != parent) {
            File subFile = new File(parent, folder);
            if (!subFile.exists()) {
                if (!subFile.mkdir()) {
                    SLog.w(TAG, "ensureFolder, failed to make dir: " + subFile.getAbsolutePath());
                    throw new IOException("Failed to make dir: " + subFile.getAbsolutePath());
                }
            }

            return subFile;
        }

        throw new IOException("Failed to make dir " + folder + " within " + parent);
    }

    public static File getDownloadedAssetRoot(Context context, final String tag) throws IOException {
        File dir = FileUtils.getExternalFilesDirs(context);
        return FileUtils.ensureFolder(dir, tag);
    }

    public static void ensureDirFile(File path) {
        if (!path.exists()) {
            if (!path.mkdirs()) {
                SLog.w(TAG, "Unable to create external files directory");
            }
        }
    }
}
