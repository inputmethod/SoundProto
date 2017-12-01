package com.typany.utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.typany.debug.SLog;
import com.typany.skin.SkinConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static final String TAG = ZipUtils.class.getSimpleName();
    private static final int BUFFER_SIZE = 8192 * 16;


    public static byte[] compress(String string) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(string.getBytes());
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(data);
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    public static String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

    public static void ZipMultiFile(String filepath, String zippath) {
        try {
            File file = new File(filepath);// 要被压缩的文件夹
            File zipFile = new File(zippath);
            InputStream input = null;
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if (file.isDirectory()) {
                byte[] buffer = new byte[2048];
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    input = new FileInputStream(files[i]);
                    zipOut.putNextEntry(new ZipEntry(files[i].getName()));
                    int temp = 0;
                    while ((temp = input.read(buffer, 0, 2048)) != -1) {
                        zipOut.write(buffer, 0, temp);
                    }
                    input.close();
                }
            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param srcFileString 要压缩的文件/文件夹名字
     * @param zipFileString 指定压缩的目的和名字
     * @throws Exception
     */
    public static boolean ZipFolder(String srcFileString, String zipFileString) {
        SLog.v(TAG, "ZipFolder(String, String)");
        ZipOutputStream outZip = null;
        try {
            outZip = new ZipOutputStream(new FileOutputStream(zipFileString));

            File file = new File(srcFileString);
            ZipFiles(file.getParent() + File.separator, file.getName(), outZip);

            outZip.finish();
            outZip.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception{
        SLog.v(TAG, "ZipFiles(String, String, ZipOutputStream)");

        if(zipOutputSteam == null)
            return;

        File file = new File(folderString + fileString);

        if (file.isFile()) {

            ZipEntry zipEntry =  new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while((len=inputStream.read(buffer)) != -1)
            {
                zipOutputSteam.write(buffer, 0, len);
            }

            zipOutputSteam.closeEntry();
        }
        else {
            String fileList[] = file.list();

            if (fileList.length <= 0) {
                ZipEntry zipEntry =  new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }

            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
            }
        }
    }

    public static boolean extract(String sourcePath, String targetPath, boolean inAssert, @Nullable Context context, String suffix) {
        SLog.i(TAG, "start to unzip file:" + sourcePath);

        InputStream is = null;
        ZipInputStream in = null;
        ZipEntry entry = null;

        boolean result = false;
        try {
            if (inAssert) {
                is = context.getAssets().open(sourcePath);
            } else {
                File file = new File(sourcePath);

                is = new FileInputStream(file);
            }
            in = new ZipInputStream(is);
            while ((entry = in.getNextEntry()) != null) {

                if (entry.isDirectory()) {
                    in.closeEntry();
                    continue;
                } else {
                    String fullName = targetPath
                            + entry.getName().substring(entry.getName().lastIndexOf(File.separatorChar) + 1);
                    if (!TextUtils.isEmpty(suffix)) {
                        fullName = fullName + suffix;
                    }
                    ZipUtils.doOutputFile(in, fullName);
                    result = true;
                }
                in.closeEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            entry = null;
            try {
                if (in != null) {
                    in.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            in = null;
        }
        SLog.i(TAG, "unzip file finished");
        return result;
    }

    public static boolean extract(String sourcePath, String targetPath, boolean inAssert, @Nullable Context context) {
        return extract(sourcePath, targetPath, inAssert, context, null);
    }

//    public static Map<String, byte[]> extract(InputStream is, List<String> files)
//            throws IOException {
//        Map<String, byte[]> data = new HashMap<String, byte[]>(files.size());
//        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
//        ZipEntry currentZipEntry = null;
//
//        try {
//            while ((currentZipEntry = zis.getNextEntry()) != null) {
//                String fileName = currentZipEntry.getName();
//                if (currentZipEntry.isDirectory()) {
//                    // SLog.w(LOG_TAG, "Unsupport sub-dictionary.");
//                    continue;
//                } else {
//                    String targetName = isInFilter(files, fileName);
//                    if (TextUtils.isEmpty(targetName)) {
//                        continue;
//                    }
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[1024];
//                    int count;
//                    while ((count = zis.read(buffer)) != -1) {
//                        baos.write(buffer, 0, count);
//                    }
//
//                    data.put(targetName, baos.toByteArray());
//                }
//            }
//            return data;
//        } finally {
//            if (zis != null) {
//                try {
//                    zis.close();
//                } catch (IOException e) {
//                    //SLog.w(LOG_TAG, LoggerInfo.getMethodName() + "->", e);
//                }
//            }
//        }
//    }

    private static String isInFilter(List<String> filter, String target) {
        for (String name : filter) {
            if (target.startsWith(name)) {
                return name;
            }
        }
        return null;
    }

    public static final boolean deleteDir(File dir) {
        dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
                return false;
            }
        });
        dir.delete();
        return true;
    }

    public static boolean checkFile(String dir) {
        File dirObj = new File(dir);
        if (dirObj.exists()) {
            if (!dirObj.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkDirectory(String dir) {
        File dirObj = new File(dir);
        if (dirObj.exists()) {
            if (!dirObj.isDirectory()) {
                dirObj.delete();
            }
            return false;
        }
        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
        return true;
    }

    public static boolean outputFile(InputStream is, String targetPath, String filename) throws IOException {
        if (is == null || targetPath == null || targetPath.equals("")
                || filename == null || filename.equals("")) {
            return false;
        }
        try {
            checkDirectory(targetPath);
            doOutputFile(is, new File(targetPath, filename).getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean isZipFile(Context context, String filePath) {
        ZipFile zipfile = null;
        try {
            if (SkinConstants.isBuildInTheme) {
                return true;
            }
            zipfile = new ZipFile(filePath);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }

    public static void doOutputFile(InputStream is, String filename)
            throws IOException {
        FileOutputStream os = new FileOutputStream(filename);
        BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        byte buf[] = new byte[BUFFER_SIZE];
        int len;
        while ((len = is.read(buf, 0, BUFFER_SIZE)) > 0) {
            bos.write(buf, 0, len);
        }
        bos.flush();
        bos.close();
        os.close();
    }


    /**
     * 含子目录的文件压缩 
     *
     * @throws Exception
     */
    // 第一个参数就是需要解压的文件，第二个就是解压的目录  
    public static boolean upZipFile(String zipFile, String folderPath) {
        ZipFile zfile = null;
        try {
            // 转码为GBK格式，支持中文  
            zfile = new ZipFile(zipFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            // 列举的压缩文件里面的各个文件，判断是否为目录  
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                SLog.i(TAG, "dirstr=" + dirstr);
                dirstr.trim();
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File  
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                SLog.e(TAG, e.getMessage());
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (Exception e) {
                SLog.e(TAG, e.getMessage());
                return false;
            }
            int readLen = 0;
            // 进行一些内容复制操作  
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                SLog.e(TAG, e.getMessage());
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                SLog.e(TAG, e.getMessage());
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            SLog.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名. 
     *
     * @param baseDir
     *            指定根目录 
     * @param absFileName
     *            相对路径名，来自于ZipEntry中的name 
     * @return java.io.File 实际的文件 
     */
    public static File getRealFileName(String baseDir, String absFileName) {
//        SLog.i(TAG, "baseDir=" + baseDir + "------absFileName="
//                + absFileName);
        absFileName = absFileName.replace("\\", "/");
//        SLog.i(TAG, "absFileName=" + absFileName);
        String[] dirs = absFileName.split("/");
//        SLog.i(TAG, "dirs=" + dirs);
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                ret = new File(ret, substr);
            }

            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            ret = new File(ret, substr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }

    public static void unzip_encrypt_zip(File zipFile, String dest, String passwd){
        try {
            net.lingala.zip4j.core.ZipFile zFile = new net.lingala.zip4j.core.ZipFile(zipFile);  // 首先创建ZipFile指向磁盘上的.zip文件

            //zFile.setFileNameCharset("GBK");       // 设置文件名编码，在GBK系统中需要设置
            if (!zFile.isValidZipFile()) {   // 验证.zip文件是否合法，包括文件是否存在、是否为zip文件、是否被损坏等
                SLog.i(TAG,"not valide zip file, return");
                return;
            }
            File destDir = new File(dest);     // 解压目录
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }
            if (zFile.isEncrypted()) {
                zFile.setPassword(passwd.toCharArray());  // 设置密码
            }
            zFile.extractAll(dest);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    
}
