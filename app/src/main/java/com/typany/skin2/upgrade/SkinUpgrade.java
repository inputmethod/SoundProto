package com.typany.skin2.upgrade;

import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import org.ini4j.Wini;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import typany.keyboard.Skin;


/**
 * Created by dingbei on 11/19/2017.
 */

@WorkerThread
public class SkinUpgrade {
    private static final String emptyImg = "";
    private static final String OPTION_PRESSED = "PRESSED";
    private static final String OPTION_NORMAL = "NORMAL";
    private static final String OPTION_TEXT_COLOR = "TEXT_COLOR";
    private static final String OPTION_TEXT_COLOR_PRESSED = "TEXT_COLOR_PRESSED";
    private static final String GENERAL_SECTION_NAME = "General";

    private static final String[] NORMAL_KEY_SECTION_NAMES = new String[] {"MainKbdRow0", "MainKbdRow1", "MainKbdRow2"};

    private static final Skin.Img NULL_IMG;

    static {
        Skin.Img.Builder builder = Skin.Img.newBuilder();
        builder.setName("");
        builder.setColor(0);
        NULL_IMG = builder.build();
    }

    private static class SecAndOpt {
        String section;
        String option;

        public SecAndOpt() {
            section = option = "";
        }

        public SecAndOpt(String section, String option) {
            this.section = section;
            this.option = option;
        }
    }

    public static boolean upgradeSkin(final File skinFolder, final String pbName, final String iniName) {
        boolean success = false;
        File pb = new File(skinFolder, pbName);
        if (pb.exists()) {
            success = true;
        } else {
            File ini = new File(skinFolder, iniName);
            try {
                InputStream is = new FileInputStream(ini);
                Skin.AllSkins as = allSkinsFromINI(is);
                OutputStream os = new FileOutputStream(pb);
                as.writeTo(os);
                success = true;
            } catch (Exception e) {
                // TODO
            }
        }
        return success;
    }

    public static Skin.AllSkins allSkinsFromINI(final InputStream is) {
        Skin.AllSkins as;
        Wini ini = null;
        try {
            ini = new Wini(is);
        } catch (Exception e) {
            // TODO
            as = Skin.AllSkins.newBuilder().build();
        }

        Skin.AllSkins.Builder builder = Skin.AllSkins.newBuilder();

        builder.setFull(buildFullKeyboardSkin(ini));
        builder.setSoduku(buildSodukuSkin(ini));
        builder.setToolbar(buildToolbarSkin(ini));
        builder.setCandidateBar(buildCandidateBarSkin(ini));
        builder.setSettingPanel(buildSettingPanel(ini));
        builder.setClipboard(buildClipboard(ini));

        as = builder.build();

        return as;
    }

    private static Skin.KeyboardSkin buildFullKeyboardSkin(final Wini ini) {
        final Skin.KeyboardSkin.Builder builder = Skin.KeyboardSkin.newBuilder();

        // keyboard bg
        builder.setKeyboardBg(buildKeyboardBackground(ini));

        // function keys
        buildFunctionKeys(builder, ini);

        // letter keys
        buildLetterKeys(builder, ini);

        // short-pressed popup
        buildShortPressedPopup(builder, ini);

        // long-pressed popup
        buildLongPressedPopup(builder, ini);

        return builder.build();
    }

    private static void buildFunctionKeys(final Skin.KeyboardSkin.Builder builder, final Wini ini) {
        final SecAndOpt fallBack = new SecAndOpt("KeyBg_Function_Qwerty", "");
        builder.setFuncShift(
                buildFunctionKey(ini, new SecAndOpt[] {new SecAndOpt("Key_Shift_Qwerty", "BG_IMAGES"), fallBack}, null));
        builder.setFuncBackspace(
                buildFunctionKey(ini, new SecAndOpt[] {new SecAndOpt("Key_Backspace_Qwerty", "BG_IMAGES"), fallBack}, null));
        builder.setFuncEnter(
                buildFunctionKey(ini, new SecAndOpt[] {new SecAndOpt("Key_Enter_Qwerty", "BG_IMAGES"), fallBack}, null));
        builder.setFuncExpression(
                buildFunctionKey(ini, new SecAndOpt[] {new SecAndOpt("Key_Symbols_Qwerty", "BG_IMAGES"), fallBack}, null));
        builder.setFuncSpace(
                buildFunctionKey(ini, new SecAndOpt[] {new SecAndOpt("KeyBg_Space_Qwerty", ""), fallBack}, null));
        builder.setFuncSymbol(
                buildFunctionKey(ini, new SecAndOpt[] {new SecAndOpt("Key_SymbolTable_Qwerty", "BG_IMAGES"), fallBack}, null));
    }

    private static void buildLetterKeys(final Skin.KeyboardSkin.Builder builder, final Wini ini) {
        boolean succ = false;
        if (ini.containsKey(NORMAL_KEY_SECTION_NAMES[0])) {
            succ = buildCustomLetterKeys(builder, ini);
        }
        if (!succ)
            buildUniformLetterKeys(builder, ini);
    }

    private static boolean buildCustomLetterKeys(final Skin.KeyboardSkin.Builder builder, final Wini ini) {
        boolean succ = false;
        for (int i = 0; i < NORMAL_KEY_SECTION_NAMES.length; i++) {
            succ = buildCustomLetterKeyRow(builder, ini, NORMAL_KEY_SECTION_NAMES[i]);
            if (!succ)
                break;
        }
        if (!succ)
            builder.clearLetterKeys();
        return succ;
    }

    private static boolean buildCustomLetterKeyRow(final Skin.KeyboardSkin.Builder builder, final Wini ini, final String rowSection) {
        boolean succ = false;
        Skin.KeyRowSkin.Builder keyRowbuilder = Skin.KeyRowSkin.newBuilder();
        int count = takeInt(ini, rowSection, "TEXT_COLOR_COUNT");
        if (count > 0) {
            for (int i = 0; i < count; i++){
                Skin.KeySkin.Builder keyBuilder = Skin.KeySkin.newBuilder();
                keyBuilder.setNormalFontColor(takeIntHex(ini, rowSection, "NORMAL_COLOR"+i));
                keyBuilder.setPressedFontColor(takeIntHex(ini, rowSection, "PRESSED_COLOR"+i));
                keyBuilder.setNormalBackgournd(buildImg(ini, rowSection, "NORMAL"+i, "", ""));
                keyBuilder.setPressedBackground(buildImg(ini, rowSection, "PRESSED_BKG"+i, "", ""));
                keyRowbuilder.addRow(keyBuilder.build());
            }
            builder.addLetterKeys(keyRowbuilder.build());
            succ = true;
        } else {
            succ = false;
        }

        return succ;
    }

    // 这种情况下就一行一个，整个键盘区就用这个
    private static void buildUniformLetterKeys(final Skin.KeyboardSkin.Builder builder, final Wini ini) {
        Skin.KeyRowSkin.Builder keyRowbuilder = Skin.KeyRowSkin.newBuilder();
        Skin.KeySkin.Builder keyBuilder = Skin.KeySkin.newBuilder();
        keyBuilder.setNormalFontColor(takeIntHex(ini, "TextStyle", OPTION_TEXT_COLOR));
        keyBuilder.setPressedFontColor(takeIntHex(ini, "TextStyle", "TEXT_COLOR_PRESSED"));
        keyBuilder.setNormalBackgournd(buildImg(ini, "KeyBg_Default_Latin", "NORMAL", "", ""));
        keyBuilder.setPressedBackground(buildImg(ini, "KeyBg_Default_Latin", "PRESSED", "", ""));
        keyRowbuilder.addRow(keyBuilder.build());
        builder.addLetterKeys(keyRowbuilder.build());
    }

    private static void buildShortPressedPopup(final Skin.KeyboardSkin.Builder builder, final Wini ini) {
        Skin.PopupSkin.Builder popupBuilder = Skin.PopupSkin.newBuilder();
        // background
        final String realImgSection = imgNameDereference(ini, "PopupBG", "BG_IMAGE");
        popupBuilder.setBackground(buildBorderImg(ini, new SecAndOpt(realImgSection, OPTION_NORMAL), new SecAndOpt(GENERAL_SECTION_NAME, "POPUP_BG_COLOR"),
                new SecAndOpt[]{
                        new SecAndOpt("POPUP_BG_BORDER", "COLOR"),
                        new SecAndOpt("TextStyle", "TEXT_COLOR")
                }
        ));

        // key
        Skin.KeySkin.Builder keyBuilder = Skin.KeySkin.newBuilder();
        keyBuilder.setNormalFontColor(takeIntHex(ini, "TextStyle_Popup", OPTION_TEXT_COLOR));
        popupBuilder.setText(keyBuilder.build());

        builder.setShortPressingPopup(popupBuilder.build());
    }

    private static void buildLongPressedPopup(final Skin.KeyboardSkin.Builder builder, final Wini ini) {
        Skin.PopupSkin.Builder popupBuilder = Skin.PopupSkin.newBuilder();

        // 对于长按气泡,气泡本身有背景
        popupBuilder.setBackground(buildBorderImg(ini, new SecAndOpt(), new SecAndOpt(GENERAL_SECTION_NAME, "POPUP_BG_COLOR"),
                new SecAndOpt[]{
                        new SecAndOpt("POPUP_BG_BORDER", "COLOR"),
                        new SecAndOpt("TextStyle", "TEXT_COLOR")
                }
        ));

        // text
        Skin.KeySkin.Builder keyBuilder = Skin.KeySkin.newBuilder();
        keyBuilder.setNormalBackgournd(NULL_IMG); // Currently pm has not designed normal background.
        keyBuilder.setPressedBackground(buildImg(ini, "", "", GENERAL_SECTION_NAME, "BG_PRESSED_COLOR"));
        keyBuilder.setNormalFontColor(takeIntHex(ini, "TextStyle_PopupCandidate", OPTION_TEXT_COLOR));
        keyBuilder.setPressedFontColor(takeIntHex(ini, "TextStyle_Candidate_Highlight", OPTION_TEXT_COLOR));
        popupBuilder.setText(keyBuilder.build());

        builder.setLongPressingPopup(popupBuilder.build());
    }

    private static Skin.BorderImg buildBorderImg(final Wini ini, SecAndOpt imgPic, SecAndOpt imgColor, SecAndOpt[] borders) {
        Skin.BorderImg.Builder builder = Skin.BorderImg.newBuilder();
        builder.setBackground(buildImg(ini, imgPic.section, imgPic.option, imgColor.section, imgColor.option));

        builder.setBorderColor(takeIntHex(ini, borders));

        return builder.build();
    }

    // 工具栏
    private static Skin.ToolBarSkin buildToolbarSkin(final Wini ini) {
        Skin.ToolBarSkin.Builder builder = Skin.ToolBarSkin.newBuilder();

        builder.setBackground(buildTopViewBackground(ini));

        // 由于旧皮肤不支持4个按钮不一样，所以这不需要考虑这个问题
        Skin.PosAdjustableButtonSkin buttonSkin = buildPAButtonSkin(ini);
        builder.setToolButton(buttonSkin);
        builder.setSkinButton(buttonSkin);
        builder.setHandDrawButton(buttonSkin);
        builder.setAdButton(buttonSkin);
        builder.setHideButton(buttonSkin);

        return builder.build();
    }

    private static Skin.PosAdjustableButtonSkin buildPAButtonSkin(final Wini ini) {
        SecAndOpt[] secAndOpts;
        Skin.PosAdjustableButtonSkin.Builder builder = Skin.PosAdjustableButtonSkin.newBuilder();

        builder.setNormalBackground(buildTopViewBackground(ini)); // normal background
        builder.setPressedBackground(buildImg(ini, "", "", "SettingBoard", "BG_COLOR")); // pressed background

        // normal color
        secAndOpts = new SecAndOpt[] {
                new SecAndOpt("SettingBoard", "ICON_COLOR"),
                new SecAndOpt("TextStyle_Candidate", "TEXT_COLOR")};

        builder.setNormalButtonIcon(buildImgWithColor(ini, secAndOpts));

        // pressed color
        secAndOpts = new SecAndOpt[] {
                new SecAndOpt("SettingBoard", "ICON_HIGHLIGHT_COLOR"),
                new SecAndOpt("ICON_HIGHLIGHT_COLOR", "ICON_HIGHLIGHT_COLOR")};

        builder.setPressedButtonIcon(buildImgWithColor(ini, secAndOpts));
        return builder.build();
    }

    private static Skin.Img buildImgWithColor(final Wini ini, SecAndOpt[] secAndOpts) {
        for (SecAndOpt secAndOpt : secAndOpts) {
            Skin.Img img = buildImg(ini, "", "", secAndOpt.section, secAndOpt.option);
            if (img != NULL_IMG) {
                return img;
            }
        }

        return NULL_IMG;
    }

    private static Skin.Img buildTopViewBackground(final Wini ini) {
        final String realImgSection = imgNameDereference(ini, "CandidateView", "BG_IMAGE");
        return buildImg(ini, realImgSection, OPTION_NORMAL, "", "");
    }

    // 候选栏
    private static Skin.CandidateBarSkin buildCandidateBarSkin(final Wini ini) {
        Skin.CandidateBarSkin.Builder builder = Skin.CandidateBarSkin.newBuilder();

        builder.setBackground(buildTopViewBackground(ini));

        Skin.KeySkin.Builder keyBuilder = Skin.KeySkin.newBuilder();
        keyBuilder.setNormalFontColor(takeIntHex(ini, "TextStyle_Candidate", OPTION_TEXT_COLOR));
        keyBuilder.setPressedFontColor(takeIntHex(ini, "TextStyle_Candidate_Highlight", OPTION_TEXT_COLOR));
        // where is normal background color? .Currently pm has not define and use normal background color.
        keyBuilder.setPressedBackground(buildImg(ini, "", "", GENERAL_SECTION_NAME, "CAND_PRESSED_COLOR"));

        builder.setText(keyBuilder.build());
        return builder.build();
    }

    private static Skin.KeyboardSkin buildSodukuSkin(final Wini ini) {
        // TODO
        return Skin.KeyboardSkin.newBuilder().build();
    }

    private static Skin.Img buildKeyboardBackground(final Wini ini) {
        return buildImg(ini, "Keyboard", "BG_IMAGE", "Keyboard", "BG_COLOR");
    }

    private static Skin.KeySkin buildFunctionKey(final Wini ini, final SecAndOpt[] secOpts, final String[] colorSections) {
        Skin.KeySkin.Builder builder = Skin.KeySkin.newBuilder();
        for (SecAndOpt secAndOpt : secOpts) {
            String imgSection = secAndOpt.section;
            String imgOption = secAndOpt.option;
            final String realImgSection = imgNameDereference(ini, imgSection, imgOption);

            Skin.Img imgNormal = buildImg(ini, realImgSection, OPTION_NORMAL, "", "");
            Skin.Img imgPressed = buildImg(ini, realImgSection, OPTION_PRESSED, "", "");

            if (imgNormal != NULL_IMG && imgPressed != NULL_IMG) {
                builder.setNormalBackgournd(imgNormal);
                builder.setPressedBackground(imgPressed);

                break;
            }
        }

        if (colorSections != null) {
            for (String colorSection : colorSections) {
                int normalColor = takeIntHex(ini, colorSection, OPTION_TEXT_COLOR);
                int pressedColor = takeIntHex(ini, colorSection, OPTION_TEXT_COLOR_PRESSED);

                if (normalColor != 0 && pressedColor != 0) {
                    builder.setNormalFontColor(normalColor);
                    builder.setPressedFontColor(pressedColor);

                    break;
                }
            }
        }

        return builder.build();
    }

    private static String imgNameDereference(final Wini ini, final String imgSection, final String imgOption) {
        String realImgSection = imgSection;

        if (!imgOption.isEmpty()) {
            String imgName = takeString(ini, imgSection, imgOption);
            if (ini.containsKey(imgName)) {
                // this is a reference node
                realImgSection = imgName;
            }
        }

        return realImgSection;
    }

    private static Skin.Img buildImg(final Wini ini, final String section, final String option, final String colorSection, final String colorOption) {
        Skin.Img.Builder builder = Skin.Img.newBuilder();
        // img & 9-patch
        String imgInfo = takeString(ini, section, option);
        if (imgInfo != null) {
            String[] token = imgInfo.split(",");
            if (token.length > 0) {
                builder.setName(token[0]);
                if (token.length == 5) {
                    builder.setNinePatch(buildNinePatch(token));
                }
            }
        }

        int color = takeIntHex(ini, colorSection, colorOption);
        builder.setColor(color);

        if (!isValid(builder)) {
            return NULL_IMG;
        } else {
            return builder.build();
        }
    }

    private static boolean isValid(Skin.Img.Builder builder) {
        return !TextUtils.isEmpty(builder.getName()) || builder.getColor() != 0;
    }

    private static Skin.NinePatch buildNinePatch(String[] data) {
        Skin.Line.Builder lBuilder = Skin.Line.newBuilder();
        Skin.NinePatch.Builder builder = Skin.NinePatch.newBuilder();
        
        lBuilder.setStart(Integer.parseInt(data[1]));
        lBuilder.setEnd(Integer.parseInt(data[2]));
        builder.setTop(lBuilder.build());

        lBuilder.setStart(Integer.parseInt(data[3]));
        lBuilder.setEnd(Integer.parseInt(data[4]));
        builder.setLeft(lBuilder.build());
        return builder.build();
    }


    // basic value
    private static String takeImg(final Wini ini, final String section, final String option) {
        String value = null;
        value = ini.get(section, option, String.class);
        String img = emptyImg;
        if (value != null) {
            String[] token = value.split(",");
            if (token.length > 0)
                img = token[0];
            if (token.length == 5) {

            }
        }
        
        return img;
    }

    private static String takeString(final Wini ini, final String section, final String option) {
        return ini.get(section, option, String.class);
    }

    private static int takeInt(final Wini ini, final String section, final String option) {
        int value = 0;
        value = ini.get(section, option, int.class);
        return value;
    }

    private static int takeIntHex(final Wini ini, final SecAndOpt[] secAndOpts) {
        int color = 0;
        for (SecAndOpt secAndOpt : secAndOpts) {
            color = takeIntHex(ini, secAndOpt.section, secAndOpt.option);
            if (color != 0) {
                break;
            }
        }

        return color;
    }

    private static int takeIntHex(final Wini ini, final String section, final String option) {
        String hex = ini.get(section, option, String.class);
        int value = 0;
        if (hex != null) {
            value = Long.decode(hex).intValue();
        }

        return value;
    }

    private static Skin.SettingPanelSkin buildSettingPanel(final Wini ini) {
        Skin.SettingPanelSkin.Builder builder = Skin.SettingPanelSkin.newBuilder();
        // icon color 在phone类型的皮肤包中没有定义，在typany中自己定义了SettingBoard:ICON_COLOR。icon的高亮颜色不需要
        builder.setItemIconLowerColor(takeIntHex(ini, "SettingBoard","ICON_COLOR"));
        builder.setItemIconUpperColor(takeIntHex(ini, "SettingBoard","ICON_COLOR"));
        // 在phone类型的皮肤包中，取候选栏背景图中间位置的颜色
        builder.setItemPressedBackground(buildImg(ini, "", "", "SettingBoard", "BG_COLOR"));
        builder.setItemTitleColor(takeIntHex(ini, "SettingBoard", "ICON_COLOR"));
        builder.setPanelBackgroundColor(takeIntHex(ini, "SettingBoard", "BG_COLOR"));

        return builder.build();
    }

    private static Skin.ClipboardSkin buildClipboard(final Wini ini) {
        Skin.ClipboardSkin.Builder builder = Skin.ClipboardSkin.newBuilder();
        builder.setDeleteIconColor(takeIntHex(ini, "SettingBoard", "ICON_COLOR"));
        builder.setDeleteNormalBackgroundColor(applyAlpha(takeIntHex(ini, "SettingBoard", "ICON_COLOR"), 0x33));
        builder.setDeletePressedBackgroundColor(applyAlpha(takeIntHex(ini, "SettingBoard", "ICON_COLOR"), 0x4c));
        builder.setTextColor(takeIntHex(ini, "SettingBoard", "ICON_COLOR"));
        builder.setTipsColor(applyAlpha(takeIntHex(ini, "SettingBoard", "ICON_COLOR"), 0x89));
        builder.setTitleBarBackground(buildImg(ini, "CandidateView/BG_IMAGE", OPTION_NORMAL, "", ""));
        builder.setTitleBarTextColor(takeIntHex(ini, "SettingBoard", "ICON_COLOR"));
        builder.setTitleBarItemPressedBkg(buildImg(ini, "SettingBoard", "BG_COLOR", "", ""));
        // The color of back-icon varies in two states(normal,pressed)
        builder.setTitleBarIconNormalColor(takeIntHex(ini, "SettingBoard", "ICON_COLOR"));
        builder.setTitleBarIconPressedColor(takeIntHex(ini, "SettingBoard", "ICON_HIGHLIGHT_COLOR"));


        // pressed background
        Skin.Img.Builder pressedBkgbuilder = Skin.Img.newBuilder();
        pressedBkgbuilder.setColor(multiplyAlpha(takeIntHex(ini, "SettingBoard", "BG_PRESSED_COLOR"),0.54f));
        builder.setItemPressedBackground(pressedBkgbuilder.build());

        return builder.build();
    }

    private static int applyAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    private static int multiplyAlpha(int color, float multiplier) {
        int a  = color >>> 24;
        a = (int)(a * multiplier + 0.5f);

        return applyAlpha(color, a);
    }
}
