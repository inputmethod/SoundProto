package com.typany.ime;


import android.content.Context;

import com.typany.soundproto.BuildConfig;

public final class GlobalConfiguration {
    public static final String UPDATE_TAG = GlobalConfiguration.class.getPackage().getName() + ".update_notification";
    public static final int UPDATE_ID = 132306;
    //use this to check if matched with dict.
    public static final int MAIN_BUILD_VERSION = 1;
    public static final int EN_BUILDIN_DICT_VERSION = 1;


    public static final int DEBUG_MODE_CLICK_BEFORE_TIP = 3;
    public static final int DEBUG_MODE_CLICK_NEED = 5;
    public static final int DEBUG_MODE_CLICK_TOTAL = DEBUG_MODE_CLICK_BEFORE_TIP + DEBUG_MODE_CLICK_NEED;

    public static final String DEFAULT_EMOJI_FILE_PATH = "raw/emoji.txt";
    public static final String DEFAULT_YANWENZI_FILE_PATH = "raw/ywz.txt";
    public static final String DEFAULT_DOMAIN_PART_FILE_PATH = "raw/domain_part.txt";


    public static boolean DISABLE_CORRECTION = false;

    // common
    public static final int ONE_DAY_IN_SECOND = 24 * 60 * 60;
    public static final int ONE_DAY_IN_MS = ONE_DAY_IN_SECOND * 1000;

    public static final int ONE_HOUR_IN_SECOND = 60 * 60;
    public static final int ONE_HOUR_IN_MS = ONE_HOUR_IN_SECOND * 1000;

    public static final String FILENAME_SEPARATOR = "_";
    // ping back url
    public static final String PING_BACK_URL = "http://get.pinyin.sogou.com/q";
    // crash report
    public static final boolean CRASH_SAVE_TO_EXTERNAL = false;
    public static final String CRASH_FOLDER_NAME = "crash";
    public static final String NATIVE_CRASH_FILE_NAME = "native_crash.txt";
    public static final String NATIVE_ANR_FILE_NAME = "native_anr.txt";
    public static final String CRASH_FILE_EXT = ".crp";
    public static final String CRASH_FILE_NAME_PREFIX = "crash_";
    public static final int CRASH_MAX_KEEP_DAYS = 30;
    // typing log
    public static final boolean TYPING_LOG_SAVE_TO_EXTERNAL = BuildConfig.DEBUG;
    public static final String TYPING_LOG_FILE_NAME = "improve";
    public static final String TYPING_LOG_FILE_EXT = ".gz";
    public static final String TYPING_LOG_SENT_FILE_EXT = TYPING_LOG_FILE_EXT; //".zg";
    public static final String TYPING_LOG_FOLDER_NAME = "log";

    public static final int TYPING_LOG_MAX_FILE_SIZE = 100 * 1024;
    public static final int TYPING_LOG_MIN_UPLOAD_SIZE = 1024;

    public static final int TYPING_LOG_MAX_KEEP_DAYS = 7;

    public static final String TINKER_FOLDER_NAME = "tinker";

    public static final String KEYBOARD_FOLDER_NAME = "keyboard";

    public static final String CONFIG_FOLDER_NAME = "langconfig";

    public static final String STICKER_FOLDER_NAME = "stickers";

    public static final String EMOJIMAKER_FOLDER_NAME = "emojimakers";

    public static final String HTML_FOLDER_NAME = "html";

    public static final String ENGINE_DETECTOR_WORKSPACE = ".detector";
    public static final String ENGINE_DETECTOR_FILENAME = "detection.bin";

    public static final String VOICE_RESULT_FOLDER_NAME = "voice";

    public static long MOCK_DEVICE_ID = 0L;

    // pass how many neighbour keys to engine.
    public static boolean PASS_ALL_NEIGHBOUR_KEYS = true;

    private GlobalConfiguration() {
        throw new AssertionError("Should not be called.");
    }


    // to switch web site config
    public static int sUsingWebsiteIndex = 0;

    private static String[] sUploadWebsite = new String[]{
            "http://upload.typany.com/api/",
            "http://10.152.102.239:8080/api/",
            "http://bbs.typany.com/api/"
    };

    private static String[] sUploadSiteIP = new String[]{
            "http://34.196.97.163/api/",
            "http://10.152.102.239:8080/api/",
            "http://13.112.250.139/api/"
    };

    private static String[] sBackEndWebsite = new String[]{
            "http://www.typany.com/api/",
            "http://10.152.102.239:8080/api/",
            "http://bbs.typany.com/api/"
    };
    private static String[] sBackEndWebsiteIP = new String[]{
            "http://34.196.136.247/api/",
            "http://10.152.102.239:8080/api/",
            "http://52.196.134.181/api/"
    };
    private static boolean sMultiCandiEnabled = false;

    private static boolean sPrintKeysBounds = false;

    private static boolean sAdMobTestId = false;

    public static final boolean SUN_TEST = true;

    public static String getBaseWebSiteIp(Context appContext) {
        return sBackEndWebsiteIP[sUsingWebsiteIndex];
    }

    public static String getBaseWebSiteUrl(Context appContext) {
        return sBackEndWebsite[sUsingWebsiteIndex];
    }

    public static String getUploadBaseWebSiteUrl(Context appContext) {
        return sUploadWebsite[sUsingWebsiteIndex];
    }

    public static String getUploadBaseWebSiteIp(Context appContext) {
        return sUploadSiteIP[sUsingWebsiteIndex];
    }

//    public static boolean isDictTestUser(){
//        InfoCollector ic = new InfoCollector(IMEApplicationContext.getAppContext());
//        String aid = ic.getAndroidID();
//        if(aid.endsWith("1") || aid.endsWith("2") || aid.endsWith("5") ||aid.endsWith("a")){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//
//    public static boolean isCanSeeTestDictUser(){
//        try {
//            InfoCollector ic = new InfoCollector(IMEApplicationContext.getAppContext());
//            String aid = ic.getAndroidID();
//            if (TextUtils.isEmpty(aid)) {
//                return false;
//            }
//            int lastChar = Integer.parseInt(String.valueOf(aid.charAt(aid.length() - 1)), 16);
//            if (lastChar % 2 == 0) {
//                return true;
//            }
//            return false;
//        }
//        catch (Exception ex){
//            return false;
//        }
//    }

    public static boolean isUsingOfficialWebSite() {
        return sUsingWebsiteIndex == 0;
    }

    public static boolean isMultiCandiEnabled() {
        return sMultiCandiEnabled;
    }

    public static void setMultiCandiEnabled(boolean multiCandiEnabled) {
        sMultiCandiEnabled = multiCandiEnabled;
    }

    public static void switchWebSiteConfig(boolean useOfficial) {
        if(useOfficial){
            sUsingWebsiteIndex = 0;
        }else{
            sUsingWebsiteIndex = 2;
        }
    }

    public static boolean isPrintKeysBounds() {
        return sPrintKeysBounds;
    }

    public static void setPrintKeysBounds(boolean printKeysBounds) {
        GlobalConfiguration.sPrintKeysBounds = printKeysBounds;
    }

    public static boolean isAdMobTestId() {
        return sAdMobTestId;
    }

    public static void setAdMobTestId(boolean b) {
        sAdMobTestId = b;
    }

    public static final String BUILTIN_DICT = "en";
    public static final String BUILTIN_DICT_VER = "800170804";
    public static final String BUILTIN_DICT_MD5 = "09b94433ec0911af8b5880483738e0f1";

    public static boolean isBuiltinDictionary(String token) {
        String[] builtin = BUILTIN_DICT.split(";");
        for (String b : builtin) {
            if (b.contentEquals(token)) {
                return true;
            }
        }
        return false;
    }

    public static String getBuiltinLanguage() {
        String[] builtin = BUILTIN_DICT.split(";");
        return builtin[0];
    }

    public static String[] listBuiltinLanguages() {
        return BUILTIN_DICT.split(";");
    }

    public static String getMD5(String token) {
        String[] builtin = GlobalConfiguration.BUILTIN_DICT.split(";");
        String[] md5 = GlobalConfiguration.BUILTIN_DICT_MD5.split(";");

        for (int i = 0; i < builtin.length; i++) {
            if (builtin[i].contentEquals(token)) {
                return md5[i];
            }
        }

        return "";
    }

}
