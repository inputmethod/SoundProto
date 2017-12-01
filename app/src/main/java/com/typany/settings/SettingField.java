package com.typany.settings;

import android.text.TextUtils;

import com.typany.ime.GlobalConfiguration;
import com.typany.soundproto.R;

import java.security.InvalidParameterException;

public enum SettingField {
    LAST_VERSION(R.string.settings_version, "n/a")
    , EN_DICT_VERSION(R.string.en_builtin_dict_version, "0")
    , DICT_VERSION(R.string.builtin_dict_version, GlobalConfiguration.BUILTIN_DICT_VER)
    , NUMBERROW(R.string.numberrow_key, false)
    , FASTINPUT(R.string.fastinput_key, true)
    , SPACE_MODE_B(R.string.space_mode_b_key, true) // change space mode selection to checkbox
    , KEYPRESS_EFFECT(R.string.keypress_effect_key, true)
    , EMOJI_PREDICTION_ENABLE(R.string.emoji_prediction_enable, true)
    , TYPING_SOUND_ENABLE(R.string.typingsound_key, true)
    , TYPING_SOUND_VOLUME(R.string.typing_sound_volume, 9) // 1-9, default = 2
    , TYPING_SOUND_DEFAULT(R.string.sound_default_key, false)
    , TYPING_VIBRATE_ENABLE(R.string.typingvibration_key, true)
    , TYPING_VIBRATE_DURATION(R.string.typing_vibrate_duration, 40)   // ms, 1 ~ 200, default = 40
    , TYPING_VIBRATE_DEFAULT(R.string.vibrate_default_key, true)
    , IMPROGRESS(R.string.improgress_key, true)
    , ABBREVIATION_SHOW(R.string.initialinput_key, false) // for super abbreviation show
    , INPUT_BUBBLE_SHOW(R.string.input_bubble_show_key, true)
    // for engine feature
    , AUTO_MATCH_PAIR_SYMBOL(R.string.auto_match_pair_symbol, false)
    , AUTO_CAP_SENTENCE_ENABLE(R.string.capitalization_sentence_enable, true)
    , APPEND_SPACE_ENABLE(R.string.append_space_enable, true)
    , SENTENCE_HEAD_PREDICTION(R.string.sentence_head_prediction_enable, false)
    , DOUBLE_SPACE_INSERT_PERIOD(R.string.double_space_key, false)
    // for input language
    , LANGUAGE_APPLY(R.string.language_current, "null")
    , LANGUAGE_LIST(R.string.language_list, "null")
    , LANGUAGE_CONFIGS(R.string.language_config, "null")

    , CURRENT_LANGUAGE_TOKEN(R.string.current_language_token, "null") //in use
    , CORRECTION_POSITION(R.string.correction_position_key, true)
    , VALID_LANGUAGES(R.string.current_valid_language_tokens, "null") // not in use
    , DELTA_LANGUAGES(R.string.current_delta_language_tokens, "null")
    , VALID_STICKERS(R.string.current_valid_stickers, "null")
    , PENDING_LANGUAGES(R.string.current_pending_language_tokens, "null")
    , CURRENT_EMOJI_PAGE(R.string.current_emoji_page, "null")
    , VALID_KEYBOARDS(R.string.current_valid_keyboards, "null") // in use
    , CURRENT_KEYBOARD_TOKEN(R.string.current_keyboard_token, "null")  // not in use
    //other config infoes
    , LAST_GET_CONFIG_TIME(R.string.last_get_config_time, "0")
    , LAST_GET_LANG_CONFIG_TIME(R.string.last_get_lang_config_time, "0")
    , LAST_UPLOAD_DICT_TIME(R.string.last_up_dict_time, "0")
    , CURRENT_KEYBOARD_VERSION(R.string.current_keyboard_version, "0")
    , CURRENT_EMOJI_VERSION(R.string.current_emoji_version, "0")
    , LAST_UPLOAD_EMOJI_TIME(R.string.last_up_emoji_time, "0")
    , LAST_FEEDBACK_EMAIL(R.string.last_feedback_email, "null")
    , NIGHT_MODE(R.string.entry_night_mode, false)
    , RATE_DIALOG_COUNT(R.string.rate_dialog_count, "false-0;0")
    , LAST_THEME_ENTER(R.string.last_theme_check_time, "0")
    , THEME_NOTIFY_NEEDED(R.string.theme_notify_needed, "0")
    , LAST_CHECK_THEME_TIME(R.string.last_check_theme_time, "0")
    , CURRENT_KEYBOARD_LAYOUT(R.string.current_keyboards_layout, "null")  // in use
    , DEBUG_MODE(R.string.debug_mode, false)
    , OLDER_VERSION(R.string.settings_older_version, "null")
    , LAST_RATE_CLOSE_TIME(R.string.last_rate_close_time, "0")
    , DO_SETTING_ENTRY_ABTEST(R.string.do_setting_entry_abtest, "false;false;-1")
    , CURRENT_IME_HEIGHT(R.string.current_ime_height, "null")
    , CURRENT_IME_HOR_HEIGHT(R.string.current_ime_hor_height, "null")
    , THEME_USING_TIMES(R.string.theme_using_times, "0")
    , FORBIDDEN_EMOJI_LIST(R.string.forbidden_emoji_list, "null")
    , SLIDE_GUIDE_PAGE(R.string.slide_guide_page, false)
    , LAST_GET_PATCH_TIME(R.string.last_get_patch_time, "0")
    , LAST_HOTEMOJI_UPDATE_TIME(R.string.last_get_hotemoji_time, "0")
    , HOT_EMOJI_VERSION(R.string.hot_emoji_version, "0")
    , SPACE_GUIDE_PAGE(R.string.space_guide_page, false)
    , BANNER_PAGE_TIME(R.string.banner_page_time, "0")
    , LAST_EMOJIMAKER_UPDATE_TIME(R.string.last_get_emojimaker_time, "0")
    , EMOJI_MAKER_VERSION(R.string.emoji_maker_version, "0")
    , CUSTOM_SKIN_CLICK(R.string.custom_skin_click, false)
    , DICT_TMP_DATA(R.string.dict_detector_result, "null")
    , FB_BANNER_TYPE(R.string.fb_banner_type, "0")
    , DICT_DETECTOR_SHOW(R.string.dict_detector_show, false)
    , EMOJIMAKER_SHOW_NOTICE(R.string.emoji_maker_show_notice, false)
    , EMOJIMAKER_SHOW_GUIDE(R.string.emoji_maker_show_guide, false)
    , LAST_GIFLIST_UPDATE_TIME(R.string.last_get_giflist_time, "0")
    , GIFLIST_VERSION(R.string.gif_list_version, "0")
    , GIFLIST_HOT_VERSION(R.string.gif_hot_version, "0")
    , LAST_RECEIVED_NOTICE(R.string.last_received_notice_time, "0")
    , GIF_NOTIFY_NEVER_AGAIN(R.string.gif_notify_never_again, false)
    , SKIN_NOTIFY_SHOWN(R.string.skin_notify_shown, false)
    , SHOW_CANDIDATE_ADS(R.string.show_candidate_ads, true)
    , CANDIDATE_ADS_INTERVAL(R.string.candidate_ads_interval, (long)(3600 * 24 * 1000))
//    , CANDIDATE_ADS_INTERVAL(R.string.candidate_ads_interval, (long)(60 * 1000))
    , LAST_CHANGE_CANDIDATE_ADS_ICON_TIME(R.string.last_change_candidate_ads_icon_time, 0l)
    , CURRENT_CANDI_ADS_INDEX(R.string.current_candi_ads_index, 0)
    , LAST_VOICE_INPUT_CONFIG(R.string.last_voice_input_config, "1;0")
    , VOICE_INPUT_TOKEN(R.string.voice_input_token, "null;0")
    , LAST_NEW_MESSAGE_CHECKING(R.string.last_new_message_checking, "1")
    , LAST_GET_NEW_MESSAGE_CHECKING(R.string.last_get_new_message_checking, "0")
    , EMOJIMAKER_ADD_NOTICE(R.string.emoji_maker_add_notify, false)
    , EMOJIMAKER_ADDED_NOTICE_SHOWN(R.string.emoji_maker_added_notify_shown, false)
    , EMOJIMAKER_RECENT_LIST(R.string.emoji_maker_recent_list, "null")
    , TRANSLATE_FROM_TO(R.string.translate_from_to, "en;en")
    ;

    private final int mId;
    private final String mDefaultValue;
    private final Class<?> mValueType;

    SettingField(int id, boolean defaultValue) {
        this(id, Boolean.toString(defaultValue), boolean.class);
    }

    SettingField(int id, int defaultValue) {
        this(id, Integer.toString(defaultValue), int.class);
    }

    SettingField(int id, float defaultValue) {
        this(id, Float.toString(defaultValue), float.class);
    }

    SettingField(int id, long defaultValue) {
        this(id, Long.toString(defaultValue), long.class);
    }

    SettingField(int id, String defaultValue) {
        this(id, defaultValue, String.class);
    }

    SettingField(int id, String defaultValue, Class<?> valueType) {
        if (TextUtils.isEmpty(defaultValue)) {
            throw new InvalidParameterException(
                    "Default value should be a string.");
        }
        mId = id;
        mDefaultValue = defaultValue;
        mValueType = valueType;
    }

    public int getId() {
        return mId;
    }

    public String getDefaultValue() {
        return mDefaultValue;
    }

    public Class<?> getValueType() {
        return mValueType;
    }
}
