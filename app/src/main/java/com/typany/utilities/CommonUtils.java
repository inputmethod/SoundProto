package com.typany.utilities;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;
import com.typany.debug.SLog;
import com.typany.soundproto.BuildConfig;
import com.typany.soundproto.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by sunhang on 16-4-20.
 */
public class CommonUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final String DEFAULT_TAG = "com.typany.ime";
    public static final String TAG_SUN = "sun";
    public static final String TAG_XUEZHENG = "xuezheng";
    public static final int DOWNLOAD_REQUEST_TIMEOUT_TIME_MS = 60000;
    public static final String DEBUG_ADS = "DEBUG_ADS";

    private static volatile DisplayMetrics metrics;

    public static Snackbar showMessage(View parent, String message) {
        return showMessage(parent, message, null, null);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        if (metrics == null) {
            metrics = context.getResources().getDisplayMetrics();
        }

        return metrics;
    }

    public static float convertDipToPixel(Context context, float dip) {
        return getDisplayMetrics(context).density * dip;
    }

    public static int convertDipToIntPixel(Context context, float dip) {
        return Math.round(convertDipToPixel(context, dip));
    }

    public static Snackbar showMessage(View parent, String message, String text, View.OnClickListener l){
        final Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_SHORT);
        if (text != null && l != null) {
            snackbar.setAction(text, l);
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }
        snackbar.setActionTextColor(Color.parseColor("#3BB398"));
        snackbar.show();

        return snackbar;
    }

//    public static void share(Activity activity, String skinId) {
//        activity.startActivity(generateShareIntent(skinId));
//    }
//
//    public static void share(Fragment fragment, String skinId) {
//        fragment.startActivity(generateShareIntent(skinId));
//    }

//    private static Intent generateShareIntent(String skinId) {
//        String id = skinId;
//        boolean customSkin = false;
//        if (skinId.startsWith(CustomSkinNames.PREFIX)) {
//            id = skinId.substring(CustomSkinNames.PREFIX.length());
//            customSkin = true;
//        }
//
//        String text;
//        if (customSkin) {
//            text = String.format(IMEApplicationContext.getAppContext().getText(R.string.share_skin).toString()
//                        +": %ssharetheme?theme_id=%s&referrer=%s", GlobalConfiguration.getBaseWebSiteUrl(null), id, CustomSkinNames.genSharedName(id));
//        } else {
//            text = String.format(IMEApplicationContext.getAppContext().getText(R.string.share_skin).toString()
//                    +": %ssharetheme?theme_id=%s", GlobalConfiguration.getBaseWebSiteUrl(null), id);
//        }
//
//        Intent sendIntent = new Intent();
//        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        sendIntent.setAction(Intent.ACTION_SEND);
////                sendIntent.putExtra(Intent.EXTRA_TEXT, "categoryId:" + mCategoryId + " themeId:" + v.getTag());
//        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
//        sendIntent.setType("text/plain");
//
//        return sendIntent;
//    }
//
//    public static void shareApk(Activity activity, String apkUrl) {
//        activity.startActivity(generateShareApkIntent(apkUrl));
//    }
//
//    public static void shareApk(Fragment fragment, String skinPkg) {
//        fragment.startActivity(generateShareApkIntent(skinPkg));
//    }
//
//    private static Intent generateShareApkIntent(String apkUrl) {
//        String replaceTag = "&referrer";
//        if (apkUrl.contains(replaceTag)){
//            apkUrl = apkUrl.substring(0,apkUrl.indexOf(replaceTag));
//        }
//        Intent sendIntent = new Intent();
//        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        sendIntent.setAction(Intent.ACTION_SEND);
////                sendIntent.putExtra(Intent.EXTRA_TEXT, "categoryId:" + mCategoryId + " themeId:" + v.getTag());
//
//        sendIntent.putExtra(Intent.EXTRA_TEXT,
//                String.format(IMEApplicationContext.getAppContext().getText(R.string.share_skin).toString()+": %s",
//                        apkUrl + "&referrer=utm_source%3Dtheme_share")
//        );
//        sendIntent.setType("text/plain");
//
//        return sendIntent;
//    }

    public static File getThemeBridgeDir(){
        //这里面有个比较强的顺序上的依赖, 因为加了加密皮肤包, 新的主题apk会删掉Bridge, 创建Bridge_v1, 并把
        //文件复制到v1下面.  因此, 这里需要先检查Bridge, 再检查Bridge_v1.
        File bridgeDir = new File(new File(Environment.getExternalStorageDirectory(), "Typany"), "Bridge");
        if(!bridgeDir.exists()){
            bridgeDir = new File(new File(Environment.getExternalStorageDirectory(), "Typany"), "Bridge_v1");
        }
        return bridgeDir;
    }

    public static boolean hasThemeFileInBridgeDir() {
        File bridgeDir = getThemeBridgeDir();
        File ssf = new File(bridgeDir, "skin.ssf");
        File jpg = new File(bridgeDir, "preview.jpg");
        File png = new File(bridgeDir, "preview.png");
        File img = jpg.exists() ? jpg : png;

        if (ssf.exists() && img.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static File getThemePreviewFile(){
        File bridgeDir = getThemeBridgeDir();
        File jpg = new File(bridgeDir, "preview.jpg");
        File png = new File(bridgeDir, "preview.png");
        File img = jpg.exists() ? jpg : png;

        if (img.exists()) {
            return img;
        } else {
            return null;
        }
    }


    /**
     * 把/sdcard/Typany/Bridge中的文件都删掉
     */
    public static void deleteBridgeFiles() {
        File bridgeDir = getThemeBridgeDir();
        if (bridgeDir.exists() == false) {
            return;
        }

        File[] files = bridgeDir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * "Permission denied"
     */
    private static String failMessage;

    public static String getFailMessage() {
        return failMessage;
    }

    /**
     * 从文件.info中读取id和name，文件内容是123@name
     * 03-21 add encrypt key for skin, so change this format to 123@name@key
     * @return
     */
    public static TriTuple<String, String,String> takeoutBridgeFileIdAndName() {
        File bridgeDir = getThemeBridgeDir();
        File info = new File(bridgeDir, ".info");
        if (info.exists() == false) {
            return null;
        }

        TriTuple<String, String,String> tuple = new TriTuple<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(info));
            String line = reader.readLine();
            if (TextUtils.isEmpty(line)) {
                throw new Exception("empty text in the file of .info");
            }
            String[] str = line.split("@");
            tuple.first = str[0];
            tuple.second = str[1];
            if(str.length>2){
                tuple.third = str[2];
            }

            failMessage = "";
            return tuple;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            failMessage = e.getMessage();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            failMessage = e.getMessage();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
//            failMessage = e.getCause().toString();
            failMessage = e.getMessage();
//            java.lang.NullPointerException
//            at com.typany.utilities.CommonUtils.takeoutBridgeFileIdAndName(CommonUtils.java:191)
//            at com.typany.ui.skinui.OnLineSkinActivity$1$1.run(OnLineSkinActivity.java:237)
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean keyboardNeverShown(Context cont) {
        try {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cont.getApplicationContext());
            return mPrefs.getBoolean(cont.getString(R.string.language_notice_setting), true);
        } catch (ClassCastException e){
            return true;
        } catch (NullPointerException e){
            return true;
        }
    }

//    public static boolean moveThemeFileFromBridgeToThemesDir(Context context, String id, String name, String extra) {
////        skin.ssf preview.jpg 或者preview.png
//        File themeDir = getThemeDirectory(context);
//        if (themeDir == null) {
//            return false;
//        }
//
//        File bridgeDir = getThemeBridgeDir();
//        File ssf = new File(bridgeDir, "skin.ssf");
//        File jpg = new File(bridgeDir, "preview.jpg");
//        File png = new File(bridgeDir, "preview.png");
//        File img = jpg.exists() ? jpg : png;
//        String postfix = jpg.exists() ? "jpg" : "png";
//
//        boolean b = moveFile(ssf, new File(themeDir, id + ".ssf"))
//                    && moveFile(img, new File(themeDir, id + "." + postfix));
//
//        if (b) {
//            SkinContext.getInstance().saveSingleThemeName(id, name,extra);
//        }
//
//        return b;
//    }

    /**
     * 得到Theme路径，假如不存在，则创建这个路径
     * @param context
     * @return
     */
    public static File getThemeDirectory(Context context) {
        boolean preferExternal = true;
        File appFilesDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appFilesDir = getExternalFilesDir(context);
        }

        File themeDir = null;
        // On nexus 5(android m),the path is '/data/user/0/com.typany.ime/files/.theme/download/'
        // or '/storage/emulated/0/Android/data/com.typany.ime/files/Theme/'
        if (appFilesDir == null) {
            appFilesDir = context.getFilesDir();
            themeDir = new File(new File(appFilesDir, ".theme"), "download");
        } else {
            themeDir = new File(appFilesDir, "Theme");
        }

        if (themeDir.exists() == false) {
            if (!themeDir.mkdirs()) {
                L.w("Unable to create theme directory");
                // Perhaps some other thread has created the dir at the same time.
                // So we check it again.
                if (themeDir.exists() == false) {
                    return null;
                }
            }
        }

        return themeDir;
    }

    /**
     * 判断sdcard是否被挂载
     * @return
     */
    private boolean isSdcardMounted() {
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }

        return MEDIA_MOUNTED.equals(externalStorageState);
    }

    private static File getExternalFilesDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appFilesDir = new File(new File(dataDir, context.getPackageName()), "files");
        if (!appFilesDir.exists()) {
            if (!appFilesDir.mkdirs()) {
                L.w("Unable to create external files directory");
                return null;
            }
            try {
                new File(appFilesDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                L.i("Can't create \".nomedia\" file in application external files directory");
            }
        }
        return appFilesDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean moveFile(File src, File desc) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(desc));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            return false;
        }

        byte[] buffer = new byte[1024 * 8];
        int len;
        try {
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public static boolean execCommand(String cmd) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                SLog.i("Xuezheng","exce output: " + line+ "\n");
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }


    public static boolean isTopActivity(Context context, String className) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

        SLog.i("TAG", "isTopActivity >> cn.getClassName: " + cn.getClassName() + " " + className);
        if (cn.getClassName().contains(className)) {
            isTop = true;
        }
        return isTop;
    }

    /**
     * change the color of edge effect,which is showing when RecycleView or ListView is pulled.
     * @param context
     * @return
     */
    public static void changeEdgeEffectColor(Context context) {
        if (context == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                int glowDrawableId = context.getResources().getIdentifier("overscroll_glow", "drawable", "android");
                Drawable androidGlow = context.getResources().getDrawable(glowDrawableId);
                androidGlow.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * check whether system activity can be found. In some case, root device has the ability to remove some system apps
     * @param context
     * @param intent
     * @param notice
     * @return
     */
    public static boolean isSystemActivityAvailable(final Context context, final Intent intent, final String notice) {
        final List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                intent, 0);
        boolean res = list != null && list.size() != 0;
//        if(!res) {
//            if(TextUtils.isEmpty(notice)) {
//                Toast.makeText(context.getApplicationContext(), R.string.msg_no_relative_activity, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context.getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
//            }
//        }
        return res;
    }

    public static List<String> queryAvaliableIntent(final Context context, final Intent intent, final String notice) {
        final List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                intent, 0);
        List<String> result = new ArrayList<>();
        for(ResolveInfo ri : list){
            if(ri.activityInfo!=null && !TextUtils.isEmpty(ri.activityInfo.packageName)) {
                result.add(ri.activityInfo.packageName);
            }
        }
        return result;
    }

    public static class Tuple<T, K> {
        public T first;
        public K second;
    }

    public static class TriTuple<T, K, M> {
        public T first;
        public K second;
        public M third;
    }

    public static boolean isPackageInstalled(String packageName,Context context){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(packageName, 0);
            if(packInfo!=null){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static final float NUMBER_ROW_SCALE = 130.0f / 166.0f;
    public static int enlargeKbdHeight(int srcKbdHeight) {
        int ave = srcKbdHeight / 4;
        int numRow = Math.round(ave * NUMBER_ROW_SCALE);
        return srcKbdHeight + numRow;
    }
    public static int numberRowHeight(int srcKbdHeight) {
        int ave = srcKbdHeight / 4;
        int numRow = Math.round(ave * NUMBER_ROW_SCALE);
        return numRow;
    }

    public static String readText(InputStream in) {
        BufferedInputStream bfin = new BufferedInputStream(in);
        byte[] buffer = new byte[1024 * 8];
        int len;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            while ((len = bfin.read(buffer)) != -1) {
                bao.write(buffer, 0, len);
            }
            bao.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = bao.toString();

        try {
            bfin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

//    public static boolean isNetworkAvailable(Context context) {
//        context = context.getApplicationContext();
//        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            android.net.Network networks[] = connectivityManager.getAllNetworks();
//            if (networks != null) {
//                for (android.net.Network n : networks) {
//                    NetworkInfo info = connectivityManager.getNetworkInfo(n);
//                    if (info.getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        } else {
//            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
//            if (networkInfos != null) {
//                for (NetworkInfo info : networkInfos) {
//                    if (info.getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//
//        return false;
//    }

    public static void loadImage(String uri, ImageView imageView, DisplayImageOptions options) {
        try {
            ImageLoader.getInstance().displayImage(uri, imageView, options);
        } catch (RuntimeException e) {
            if (BuildConfig.DEBUG) {
                throw e;
            } else {
                e.printStackTrace();
            }
        }
    }

    private static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * delete all files and dirs in the folder.
     * @param folder
     */
    public static void deleteAllInFolder(File folder) {
        if (folder.isFile()) {
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (File f : files) {
            deleteFile(f);
        }
    }

    public static String readContent(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

//    public static void fixMenuBuilder(final AppCompatActivity activity) {
//        if(activity==null) return;
//        final ActionBar actionBar = activity.getSupportActionBar();
//        if(actionBar==null) return;
//        try {
//            if(actionBar.getClass().getName().equals("android.support.v7.app.ToolbarActionBar"))
//            {
//                Method destory = actionBar.getClass().getDeclaredMethod("onDestroy");
//                destory.setAccessible(true);
//                destory.invoke(actionBar);
//                Field mi = actionBar.getClass().getDeclaredField("mMenuInvalidator");
//                mi.setAccessible(true);
//                mi.set(actionBar, new Runnable(){
//                    @Override
//                    public void run() {
//                    }
//                });
//                actionBar.invalidateOptionsMenu();
//
//                Field ftc = actionBar.getClass().getDeclaredField("mDecorToolbar");
//                ftc.setAccessible(true);
//                DecorToolbar dtc = (DecorToolbar) ftc.get(actionBar);
//                final Toolbar tb = (Toolbar) dtc.getViewGroup();
//
//                try {
//                    Field mTextViewField = tb.getClass().getDeclaredField("mTitleTextView");
//                    mTextViewField.setAccessible(true);
//
//                    TextView mTextView = (TextView) mTextViewField.get(tb);
//                    mTextView.setTypeface(FontsHelper.getInstance().getTfRegular(activity));
//                    mTextView.getPaint().setAntiAlias(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                tb.setNavigationOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
////                            activity.onSupportNavigateUp();
//                            if (activity instanceof QuitListener) {
//                                ((QuitListener)activity).requestQuit();
//                            } else {
//                                activity.finish();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * the new shared url is protected by some ways
//     * @param id id must is all digit
//     * @param customSkin denote whether the skin is custom skin.
//     */
//    public static void generateNewShareUrl(String id, boolean customSkin) {
//        if (TextUtils.isDigitsOnly(id) == false) {
//            if (BuildConfig.DEBUG) {
//                throw new RuntimeException("id must be digit only");
//            }
//        }
//
//        // add 8bit random digit before id
//        final StringBuilder randomDigit = new StringBuilder();
//        Random random = new Random();
//        for (int i = 0; i < 8; i ++) {
//            randomDigit.append(random.nextInt(10));
//        }
//
//        String seed = String.format(Locale.ENGLISH, "theme_id=%s%sreferrer=%sver=1.0", random.toString(), id, !customSkin ? id : CustomSkinNames.genSharedName(id));
//        String md5 = getMD5EncryptedString(seed);
//
//
//        String.format(Locale.ENGLISH,
//                IMEApplicationContext.getAppContext().getText(R.string.share_skin).toString() +": %ssharetheme?theme_id=%s%s&referrer=%s&ver=1.0&token=%s",
//                GlobalConfiguration.getBaseWebSiteUrl(null),
//                randomDigit.toString(),
//                id,
//                !customSkin ? id : CustomSkinNames.genSharedName(id),
//                md5);
//    }

    private static String getMD5EncryptedString(String encTarget) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }

    public static String getClipData(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            String label = cm.getPrimaryClipDescription().getLabel().toString();
            SLog.i(CommonUtils.TAG_SUN, "[clipboard][label:" + label + "]");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            ClipData data = cm.getPrimaryClip();
            ClipData.Item item = data.getItemAt(0);
            String text = item.getText().toString();
            return text;
        } catch (Exception e) {
            e.getStackTrace();
            SLog.i(CommonUtils.TAG_SUN, "[clipboard][exception:" + e.getMessage() + "]");
            return "";
        }
    }

//    public static void saveCrash(Context context, Exception e) {
//        RuntimeException ex = new RuntimeException("catched exception : " + e.getMessage());
//        ex.setStackTrace(e.getStackTrace());
//
//        CrashPersister persister = new CrashPersister(context.getApplicationContext());
//        BasicInfo basicInfo = new BasicInfo(context.getApplicationContext());
//        CrashReport report = CrashReport.create(null, ex, basicInfo);
//        persister.save(report.toString());
//    }

    private static Typeface sTfRegular = null;
    public static Typeface getTfRegular(Context ctx) {
        if (sTfRegular == null) {
            String asset = "fonts/Rubik-Regular.ttf";

            try {
                sTfRegular = Typeface.createFromAsset(ctx.getAssets(), asset);
            } catch (Exception e) {
                SLog.e(CommonUtils.TAG_SUN,"Could not get typeface:"+e.getMessage() + " " + asset);
            }
        }
        return sTfRegular;
    }

//    public static String getFullRequestUri(Context context, String api, boolean isIp){
//        String tail = "?" + "vername=" + BuildConfig.VERSION_NAME + "&" +
//                new BasicInfo(context).getDownLoadParam();
//        String host = null;
//        if(isIp){
//            host = GlobalConfiguration.getBaseWebSiteIp(context);
//        }
//        else{
//            host = GlobalConfiguration.getBaseWebSiteUrl(context);
//        }
//        return host + api + tail;
//    }

}
