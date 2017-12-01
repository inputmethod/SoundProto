package com.typany.settings;

import android.content.Context;
import android.os.Build;

import com.typany.debug.SLog;
import com.typany.ime.IMEApplicationContext;

/**
 * Created by liuzhuang on 2015/12/16.
 */
public class RunningStatus {

    private static boolean mChangeLanguageDialog;
    private static boolean mPwdMode;
    private static boolean mNumKeyBoardMode;
    private static boolean mCalendarVisible;
    private static boolean mPortMode;
    private static boolean mSmallTextEnabled;
    private static boolean mSymbolMode;
    private Context mAppContext;
    private static boolean mStickUpdated;
    private static boolean mStickDeleted;
    private static boolean mHasLocalXml;
    private static int mLatestVersion;
    private Boolean mNightMode;
    private static boolean mRateDialog;
    private static boolean mUpdateDialog;
    private static boolean isGPinstalled;

    private static long creatTime;
    private static long showTime;
    private static boolean isKeyboardShown;

    private static boolean mNumKeyboardShown;
    private boolean mResizing;
    private boolean mExtractViewShown;
    private static boolean mEditorShown;
    private static boolean mShowStickerKeyboard;

    private static boolean updateEmojiMakers;
    private static boolean updateStickers;
    private static boolean updateGifs;

    private static boolean gifSearchMode;
    private static boolean gifToemoji;
    private static int emojiPage = -1;
    private static boolean gifSearchShare;
    private static boolean gifShowing;
    private static boolean gifCalled;
    private static String gifResult;

    private static final String LOG_TAG = SettingMgr.class.getSimpleName();

    private static RunningStatus sInstance = null;

    public static void create(Context context) {
        if (sInstance == null) {
            sInstance = new RunningStatus(context);
        }
        initValue();
    }

    private static void initValue() {
        mChangeLanguageDialog = false;
        mPwdMode = false;
        mCalendarVisible = false;
        mEditorShown = false;
    }

    public static void destroy() {
//        resetAllRequest();
        if (sInstance != null) {
            sInstance.onDestroy();
            sInstance = null;
        }
    }

    public static RunningStatus getInstance() {
        if (sInstance == null) {
            create(IMEApplicationContext.getAppContext());
        }

        return sInstance;
    }

    private RunningStatus(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static boolean isEditorShown() {
        return mEditorShown;
    }

    public static void setEditorShown(boolean mEditorShown) {
        RunningStatus.mEditorShown = mEditorShown;
    }

    public static boolean ismShowStickerKeyboard() {
        return mShowStickerKeyboard;
    }

    public static void setmShowStickerKeyboard(boolean mShowStickerKeyboard) {
        RunningStatus.mShowStickerKeyboard = mShowStickerKeyboard;
    }

    public static boolean isUpdateEmojiMakers() {
        return updateEmojiMakers;
    }

    public static void setUpdateEmojiMakers(boolean updateEmojiMakers) {
        RunningStatus.updateEmojiMakers = updateEmojiMakers;
    }

    public static boolean isUpdateStickers() {
        return updateStickers;
    }

    public static void setUpdateStickers(boolean updateStickers) {
        RunningStatus.updateStickers = updateStickers;
    }

    public static long gifShowingTime = 0L;

    public static boolean isGifSearchMode(boolean reset) {
        SLog.d("Gif Search ", "isGifSearchMode " + gifSearchMode);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if (gifShowingTime == 0) {
                return false;
            } else {
                if (System.currentTimeMillis() - gifShowingTime < 1500) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return gifSearchMode;
        }
//        SLog.d("Gif Search ", "isGifSearchMode " + gifSearchMode + " reset " + reset);
//        if (reset) {
//            gifSearchMode--;
//        }
//        SLog.d("Gif Search ", "isGifSearchMode " + gifSearchMode + " reset " + reset);
//        return (gifSearchMode > 0);
    }

    public static void setGifSearchMode(boolean gifSearchMode) {
        SLog.d("Gif Search ", "setGifSearchMode " + gifSearchMode);
        if (gifSearchMode) {
            gifShowingTime = System.currentTimeMillis();
        }
        RunningStatus.gifSearchMode = gifSearchMode;
    }

    public static void setShowingTime(long time) {
        gifShowingTime = time;
    }

    public static int getEmojiPage() {
        return emojiPage;
    }

    public static void setEmojiPage(int emojiPage) {
        RunningStatus.emojiPage = emojiPage;
    }

    public static boolean isUpdateGifs() {
        return updateGifs;
    }

    public static void setUpdateGifs(boolean updateGifs) {
        RunningStatus.updateGifs = updateGifs;
    }

    public static boolean isGifToemoji() {
        return gifToemoji;
    }

    public static void setGifToemoji(boolean gifToemoji) {
        RunningStatus.gifToemoji = gifToemoji;
    }

    public static boolean isGifSearchShare() {
        return false;
//        return gifSearchShare;
    }

    public static void setGifSearchShare(boolean gifSearchShare) {
        RunningStatus.gifSearchShare = gifSearchShare;
    }

    public static boolean isGifShowing() {
        return gifShowing;
    }

    public static void setGifShowing(boolean gifShowing) {
        RunningStatus.gifShowing = gifShowing;
    }

    public static boolean isGifCalled() {
        return gifCalled;
    }

    public static void setGifCalled(boolean gifCalled) {
        RunningStatus.gifCalled = gifCalled;
    }

    public static String isGifResult() {
        return gifResult;
    }

    public static void setGifResult(String gifResult) {
        RunningStatus.gifResult = gifResult;
    }

    public boolean isExtractViewShown() {
        return mExtractViewShown;
    }

    public void setExtractViewShown(boolean extractViewShown) {
        mExtractViewShown = extractViewShown;
    }

    public boolean isResizing() {
        return mResizing;
    }

    public void setResizing(boolean resizing) {
        mResizing = resizing;
    }

    public static boolean ismHasLocalXml() {
        return mHasLocalXml;
    }

    public static int getLatestVersion() {
        return mLatestVersion;
    }

    public static void setLatestVersion(int lastestVersion) {
        mLatestVersion = lastestVersion;
    }

    public static long getCreatTime() {
        return creatTime;
    }

    public static void setCreatTime(long creatTime) {
        RunningStatus.creatTime = creatTime;
    }

    public static long getShowTime() {
        return showTime;
    }

    public static void setShowTime(long showTime) {
        RunningStatus.showTime = showTime;
    }

    public static boolean isKeyboardShown() {
        return isKeyboardShown;
    }

    public static void setIsKeyboardShown(boolean isKeyboardShown) {
        RunningStatus.isKeyboardShown = isKeyboardShown;
    }

    public static boolean ismNumKeyboardShown() {
        return mNumKeyboardShown;
    }

    public static void setmNumKeyboardShown(boolean mNumKeyboardShown) {
        RunningStatus.mNumKeyboardShown = mNumKeyboardShown;
    }

    public void setmHasLocalXml(boolean b) {
        mHasLocalXml = b;
    }

    public static boolean ismStickDeleted() {
        return mStickDeleted;
    }

    public void setmStickDeleted(boolean b) {
        mStickDeleted = b;
    }

    public boolean ismStickUpdated() {
        return mStickUpdated;
    }

    public void setmStickUpdated(boolean b) {
        mStickUpdated = b;
    }


    public boolean ismSmallTextEnabled() {
        return mSmallTextEnabled;
    }

    public void setmSmallTextEnabled(boolean b) {
        mSmallTextEnabled = b;
    }

    public void setmPwdMode(boolean b) {
        mPwdMode = b;
    }

    public boolean ismPwdMode() {
        return mPwdMode;
    }

    public void setmSymbolMode(boolean b) {
        mSymbolMode = b;
    }

    public boolean ismSymbolMode() {
        return mSymbolMode;
    }

    public boolean ismNumKeyBoardMode() {
        return mNumKeyBoardMode;
    }

    public void setmNumKeyBoardMode(boolean mNumKeyBoardMode) {
        RunningStatus.mNumKeyBoardMode = mNumKeyBoardMode;
    }

    public void setmChangeLanguageDialog(boolean b) {
        mChangeLanguageDialog = b;
    }

    public boolean ismChangeLanguageDialog() {
        return mChangeLanguageDialog;
    }

    public void setmRateDialog(boolean b) {
        mRateDialog = b;
    }

    public boolean ismRateDialog() {
        return mRateDialog;
    }

    public void setmUpdateDialog(boolean b) {
        mUpdateDialog = b;
    }

    public boolean ismUpdateDialog() {
        return mUpdateDialog;
    }

    public void onDestroy() {
        mAppContext = null;
    }

    public void setmCalendarVisible(boolean b) {
        mCalendarVisible = b;
    }

    public boolean ismCalendarVisible() {
        return mCalendarVisible;
    }

    public void setScreenPortMode(boolean b) {
        mPortMode = b;
    }

    public boolean getScreenPortMode() {
        return mPortMode;
    }

    public void setNightMode(boolean b) {
        mNightMode = b;
    }

    public boolean getNightMode() {
        if (mNightMode == null) {
            mNightMode = Boolean.parseBoolean(SettingMgr.getInstance().getValue(SettingField.NIGHT_MODE));
        }
        return mNightMode;
    }

    public void setGPinstalled(boolean b) {
        isGPinstalled = b;
    }

    public boolean isGPinstalled() {
        return isGPinstalled;
    }

//    public static final int CURSOR_STATE_EMPTY = 0;
//    public static final int CURSOR_STATE_TAP_KEY = 1;
//    public static final int CURSOR_STATE_MOVE_IN_KBD = 2;
//    public static final int CURSOR_STATE_MOVE_IN_EDIT = 3;
//    public static final int CURSOR_STATE_DELETE_KEY = 4;
//
//    private int mCursorState = CURSOR_STATE_EMPTY;
//
//    public void setCursorState(int state) {
//        this.mCursorState = state;
//    }
//
//    public int getCursorState() {
//        return this.mCursorState;
//    }

    @Deprecated
    private boolean mIsHandlingEditor = false;
    @Deprecated
    private boolean mIsMovingCursor = false;
    @Deprecated
    private boolean mDelMark = false;

    @Deprecated
    public boolean isHandlingEditor() {
        return mIsHandlingEditor;
    }

    @Deprecated
    public void setHandlingEditor(boolean handlingEditor) {
        mIsHandlingEditor = handlingEditor;
    }

    @Deprecated
    public boolean isMovingCursor() {
        return mIsMovingCursor;
    }

    @Deprecated
    public void setMovingCursor(boolean movingCursor) {
        mIsMovingCursor = movingCursor;
    }

    @Deprecated
    public boolean isDelMark() {
        return mDelMark;
    }

    @Deprecated
    public void setDelMark(boolean delMark) {
        mDelMark = delMark;
    }
}
