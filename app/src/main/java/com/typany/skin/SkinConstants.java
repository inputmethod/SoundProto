package com.typany.skin;


/**
 * Created by feixuezheng on 2015/11/16.
 */
public class SkinConstants {

    private SkinConstants(){}

    public static int requestBuildType=1;
    public static int ANIMATNION_CORE_VERSION=1;
    public static boolean isUseDefaultBuildType = true;

    public static boolean isCustomRequest = false;
    public static boolean isAnimalKB = false;
    public static String AnimalResPath;

    /**
     * A variable represents the skin' type.It can be assigned at runtime.
     */
    public static SkinPackType SKIN_TYPE = SkinPackType.PHONE;
    public static boolean SKIN_PACKAGE_ENCRYPED = false;
    public static String SkinPackEncode = "GB2312";
    public static String SkinCommonINIEncode = "UTF-8";
    public final static String ENCRYPT_KEY = "68e85117ee51484e9811eeb17efe567c";
    //skin, dict, config use this value all, when use this, please make sure they are uniform.
    public static String BASE_RES_HOST_URL;
    public static Boolean HIDE_CANDIDATE_ICON = false;
    public static final String NOTSET_SKIN = "notset";
    public static boolean isBuildInTheme = false;
    public final static float ANIMATION_BASE_WIDTH = 1080f;
    public final static float ANIMATION_BASE_HEIGHT = 670f;//664f;

    public static final int DOWNLOAD_SKININFO_SUCCEED = 0;
    public static final int DOWNLOAD_SKININFO_FAILED = 1;
    public static final int DOWNLOAD_SKINCATEGORY_SUCCEED = 4;
    public static final int DOWNLOAD_SKINCATEGORY_FAILED = 5;
    public static final int PARSE_LOCAL_SKININFO_SUCCEED = 2;
    public static final int PARSE_LOCAL_SKININFO_FAILED = 3;

    public static final String REQUEST_PAGE_KEY = "page";
    public static final String REQUEST_PCOUNT_KEY = "pcount";

    //public static String THEME_PACKAGE_IN_SD = Environment.getExternalStoragePublicDirectory("Typany")+File.separator+"Theme";
    /**
     * the path of sdcard typany data.It may be assigned to "/data/data...",which equals to {@link SkinConstants#THEME_PACKAGE_IN_INTERNAL}
     */
    public static String THEME_PACKAGE_IN_SD = "";
    /**
     * the path of typany data if sdcard is unavailable. for example, "/data/data"
     */
    public static String THEME_PACKAGE_IN_INTERNAL = "";
    public static final String THEME_FOLDER_IN_INTERNAL = ".theme";
    public static final String THEME_ID_NAME_FILE = ".themename";
    public static final String BUILT_IN_THEME = "0";
    public static final String DOWNLOAD_THEME = "1";
    public static final String CUSTOM_THEME = "2";
    public static final String THEME_PACKGE_CATEID = "999";
    public static final String THEME_PACKGE_IMPORT_CATEID = "998";
    public static final String THEME_ID_NAME_SPERATOR = "@";
    public static final String DICT_ID_NAME_SPERATOR = "=";
    public static final String QUERY_STRING_MORE_THEME = "cmd=theme_main_list";
    public static final String QUERY_STRING_THEME_CATE = "cateid";
    public static final String CHARSET_UTF_16 = "UTF-16";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CHARSET_GB_2312 = "GB2312";
    public static final String NINEPATCH_SUBFIX = ".9.png";
    public static final String SKIN_PREVIEW_SUBFIX1 = ".jpg";
    public static final String SKIN_PREVIEW_SUBFIX2 = ".png";
    public static final String SKIN_PREVIEW_SUBFIX3 = ".webp";
    public static final String SKIN_PACKAGE_SUBFIX = ".ssf";
    public final static String THEME_COMMON_CONFIG_FILE = "phoneTheme.ini";
    public static final String THEME_CONFIG_FILE = "phoneSkin.ini";
    public static final String THEME_PC_CONFIG_FILE = "skin.ini";
    public static final String DEFAULT_SKIN_PACKAGE1 = "White2_0";
    public static final String DEFAULT_SKIN_NAME1 = "White2_0";
    public static final String DEFAULT_SKIN_PACKAGE2 = "Dark2_0";
    public static final String DEFAULT_SKIN_NAME2 = "Dark2_0";

    public static final String DEFAULT_SKIN_PACKAGE3 = "1001001042";
    public static final String DEFAULT_SKIN_NAME3 = "Bubble Love";
    public static final String DEFAULT_SKIN_PACKAGE4 = "1001001054";
    public static final String DEFAULT_SKIN_NAME4 = "Howl Wolf";

    public static final String DEFAULT_SKIN_PACKAGE5 = "Blue";
    public static final String DEFAULT_SKIN_NAME5 = "Blue";

    public static final String BUILTIN_SKIN_PATH = "builtintheme";
    public static final String PREVIEW_SKIN_PATH = "preview" ;

    public static final String LIB_SKINPACKER_NAME = "skinpacker";

    public static final String LIB_SKINPACKER_PATH = "raw/skinpacker" ;

    public static final String SKIN_SECTION_SEPERATOR = "/";
    public static final String SKIN_PROPERTY_SEPERATOR = ":";
    public static final String CONFIG_THEME_TYPE_NODE = "theme_type";
    public final static String CONFIG_THEME_TYPE_VALUE = "phone_wallpaper";
    public static final String CONFIG_THEME_TYPE_CUSTOM_WP = "custom_wallpaper";

    public static final String THEME_DOWNLOAD_TYPE_GOOGLE = "2";
    public static final String THEME_DOWNLOAD_TYPE_COMMEN = "1";

    /** Key state */
    public final static String DISABLED = "DISABLED";
    public final static String NORMAL = "NORMAL";
    public final static String PRESSED = "PRESSED";
    public static final String FOCUSED = "FOCUSED";
    public static final String SELECTED = "SELECTED";
    public static final String SELECTED_PRESSED = "SELECTED_PRESSED";

    public final static String NORMAL_ON = "NORMAL_ON";
    public final static String PRESSED_ON = "PRESSED_ON";
    public final static String NORMAL_OFF = "NORMAL_OFF";
    public final static String PRESSED_OFF = "PRESSED_OFF";

    public final static String TEXT_COLOR_NORMAL = "TEXT_COLOR";
    public final static String TEXT_COLOR_PRESSED = "TEXT_COLOR_PRESSED";

    /* PC ini info */
    public final static String PC_KEYBOARD_SECTION = "Scheme_V2";
    public final static String PC_KEYBOARD_RESOURCE_PIC = "zhongwen_pic";
    public final static String PC_KEYBOARD_SECTION_BACKUP = "Scheme_V1";
    public final static String PC_KEYBOARD_RESOURCE_PIC_BACKUP = "pic";

    public final static String PC_CANDIDATE_SECTION = "Scheme_H1";
    public final static String PC_CANDIDATE_RESOURCE_PIC = "pic";
    public final static String PC_CANDIDATE_SECTION_BACKUP = "Scheme_H2";
    public final static String PC_CANDIDATE_RESOURCE_PIC_BACKUP = "pinyin_pic";

    /* parse downloaded skin config node */
    public final static String THEME_ATTR_SKIN_NODE = "theme";
    public final static String THEME_ATTR_INFO_NODE = "info";
    public final static String THEME_ATTR_SKIN_CATE_NODE = "category";
    public final static String THEME_ATTR_HOST_URL_ATTR = "baseResUrl";
    public final static String THEME_ATTR_SKIN_ID = "skin_id";
    public final static String THEME_ATTR_SHOW_NAME = "show_name";
    public final static String THEME_ATTR_AUTHOR = "author";
    public final static String THEME_ATTR_COLOR = "default_bg";
    public final static String THEME_ATTR_SIZE = "filesize";
    public final static String THEME_ATTR_PREVIEW_PIC_URL = "preview_square_pic_url";
    public final static String THEME_ATTR_PREVIEW_CANDIDATE_PIC_URL = "preview_candidate_pic_url";
    public final static String THEME_ATTR_DOWNLOAD_URL = "ssf_download_url";
    public final static String THEME_ATTR_2_DOWNLOAD_URL = "ssf_path";

    public final static String THEME_ATTR_TAG_TYPE = "theme_tag_type";
    public static final String THEME_ATTR_TAG_DL_TYPE = "theme_type";
    public static final String THEME_ATTR_RECOMMENT = "recommend";

    public final static String THEME_ATTR_CATE_ID = "cate_id";
    public final static String THEME_ATTR_CATE_TITLE = "title";
    public final static String THEME_ATTR_CATE_DESC = "description";
    public final static String THEME_ATTR_CATE_ICON = "icon";
    public final static String THEME_ATTR_CATE_WEIGHT = "weight";
    public final static String THEME_ATTR_CATE_LABEL = "label";

    public final static String THEME_APK_PACKAGE_NAME = ".typany.u%s";
    public final static String THEME_APK_LIB_SO_PATH = "/lib/";

    public enum SkinPackType{
        PC,PHONE,WALLPAPER,CUSTOM_WALLPAPER
    }

    private static volatile String sCurrentSkinName="notset";

    public static String getCurrentSkinName() {
        return sCurrentSkinName;
    }

    public static void setCurrentSkinName(String sCurrentSkinName) {
        SkinConstants.sCurrentSkinName = sCurrentSkinName;
    }

    public static boolean isWhiteSkin() {
        return sCurrentSkinName != null && sCurrentSkinName.contentEquals(DEFAULT_SKIN_NAME1);
    }

    public static boolean isDarkSkin() {
        return sCurrentSkinName != null && sCurrentSkinName.contentEquals(DEFAULT_SKIN_NAME2);
    }

    public static boolean isBubbleLoveSkin() {
        return sCurrentSkinName != null && sCurrentSkinName.contentEquals(DEFAULT_SKIN_NAME3);
    }

    public static boolean isHowlWolfSkin() {
        return sCurrentSkinName != null && sCurrentSkinName.contentEquals(DEFAULT_SKIN_NAME4);
    }

    public static boolean isBlueSkin() {
        return sCurrentSkinName != null && sCurrentSkinName.contentEquals(DEFAULT_SKIN_NAME5);
    }
}
