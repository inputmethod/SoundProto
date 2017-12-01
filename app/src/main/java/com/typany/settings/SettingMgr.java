package com.typany.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;

import com.typany.ime.IMEApplicationContext;
import com.typany.soundproto.BuildConfig;
import com.typany.soundproto.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SettingMgr {
//    private static final String LOG_TAG = SettingMgr.class.getSimpleName();

    private static SettingMgr sInstance = null;

    private static synchronized SettingMgr getSync() {
        if (sInstance == null) {
            final Context appContext = IMEApplicationContext.getAppContext();
            if (appContext != null) {
                sInstance = new SettingMgr(appContext);
            } else {
//                if (BuildConfig.THROW_DEBUG_EXCEPTION) {
//                    throw new RuntimeException("Ime application context is null!!!");
//                }
                return null;
            }
        }
        return sInstance;
    }

    public static SettingMgr getInstance() {
        if (sInstance == null) {
            sInstance = getSync();
        }

        return sInstance;
    }


    public interface ValueChangedListener {
        void onValueChanged(Class<?> type, String oldValue, String newValue);
    }

    private String mLastModifiedKey = "";

    private Map<String, List<ValueChangedListener>> mListeners;
    private SparseArray<String> mKeyCache;
    private Map<String, Pair<SettingField, String>> mSettings;
    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;

    private long mLastModifiedStamp;

    private final Context mAppContext;

    private SettingMgr(Context appContext) {
        mAppContext = appContext.getApplicationContext();
        mLastModifiedKey = mAppContext.getString(R.string.settings_last_modified);
        mSettings = new HashMap<>();
        mKeyCache = new SparseArray<>();
        for (SettingField field : SettingField.class.getEnumConstants()) {
            String key = mAppContext.getString(field.getId());
            mSettings.put(key, new Pair<>(field, field.getDefaultValue()));
            mKeyCache.put(field.getId(), key);
        }

        mListeners = new HashMap<>();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(mAppContext);

        mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (mSettings.containsKey(key)) {
                    SettingField field = mSettings.get(key).first;
                    Class<?> type = field.getValueType();
                    String oldValue = mSettings.get(key).second;
                    String newValue = getValue(type, key, field.getDefaultValue());
                    mSettings.put(key, new Pair<>(field, newValue));
                    if (mListeners.containsKey(key)) {
                        List<ValueChangedListener> list = mListeners.get(key);

                        for (ValueChangedListener listener : list) {
                            listener.onValueChanged(field.getValueType(), oldValue, newValue);
                        }
                    }
                }
            }
        };
        mPrefs.registerOnSharedPreferenceChangeListener(mPrefListener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                reload();
            }
        }).start();

    }

    public String getValue(SettingField field) {
        String key = mKeyCache.get(field.getId(), null);
        if (key != null) {
            if (mSettings.containsKey(key)) {
                return mSettings.get(key).second;
            } else {
                if (BuildConfig.DEBUG) {
                    throw new InvalidParameterException(field.name() + "is not in settings list.");
                }
                return field.getDefaultValue();
            }
        } else {
            if (BuildConfig.DEBUG) {
                throw new InvalidParameterException(field.name() + "is not in settings list.");
            }
            return field.getDefaultValue();
        }
    }

    public void setValue(SettingField field, String value) {
        String key = mKeyCache.get(field.getId(), null);
        if (key != null) {
            if (mSettings.containsKey(key)) {
                setValue(field.getValueType(), key, value);
                mSettings.put(key, new Pair<>(field, value));
            } else {
                throw new InvalidParameterException(field.name() + "is not in settings list.");
            }
        } else {
            throw new InvalidParameterException(field.name() + "is not in settings list.");
        }
    }

    public void addListener(SettingField field, ValueChangedListener listener) {
        addListener(field.getId(), listener);
    }

    public void removeListener(SettingField field, ValueChangedListener listener) {
        removeListener(field.getId(), listener);
    }

    public long getLastModifiedStamp() {
        return mLastModifiedStamp;
    }

    private String getValue(Class<?> type, String key, String defaultValue) {
        if (TextUtils.isEmpty(defaultValue)) {
            throw new InvalidParameterException("Default value should be a string.");
        }
        if (mPrefs.contains(key)) {
            if (type == boolean.class) {
                boolean newValue = mPrefs.getBoolean(key, Boolean.parseBoolean(defaultValue));
                return Boolean.toString(newValue);
            } else if (type == int.class) {
                int oldValue = Integer.parseInt(defaultValue);
                int newValue = mPrefs.getInt(key, oldValue);
                return Integer.toString(newValue);
            } else if (type == long.class) {
                long oldValue = Long.parseLong(defaultValue);
                long newValue = mPrefs.getLong(key, oldValue);
                return Long.toString(newValue);
            } else if (type == String.class) {
                return mPrefs.getString(key, defaultValue);
            } else if (type == float.class) {
                float oldValue = Float.parseFloat(defaultValue);
                float newValue = mPrefs.getFloat(key, oldValue);
                return Float.toString(newValue);
            } else {
                throw new InvalidParameterException("Unknown type = " + type.toString());
            }
        } else {
            setValue(type, key, defaultValue);
            return defaultValue;
        }
    }

    private void setValue(Class<?> type, String key, String newValue) {
        SharedPreferences.Editor e = mPrefs.edit();
        if (type == boolean.class) {
            e.putBoolean(key, Boolean.parseBoolean(newValue));
        } else if (type == int.class) {
            e.putInt(key, Integer.parseInt(newValue));
        } else if (type == long.class) {
            e.putLong(key, Long.parseLong(newValue));
        } else if (type == String.class) {
            e.putString(key, newValue);
        } else if (type == float.class) {
            e.putFloat(key, Float.parseFloat(newValue));
        } else {
            throw new InvalidParameterException("Unknown type = " + type.toString());
        }

        e.commit();
    }

    private void addListener(int id, ValueChangedListener listener) {

        if (listener == null) {
            throw new InvalidParameterException(
                    "\"listener\" should not be null.");
        }
        String key = mKeyCache.get(id, null);
        if (key != null) {
            if (TextUtils.isEmpty(key)) {
                throw new InvalidParameterException("Invalid id.");
            }
            if (!mSettings.containsKey(key)) {
                throw new InvalidParameterException(
                        "Invalid id, it is not a key of settings.");
            }

            List<ValueChangedListener> list = mListeners.get(key);
            if (list == null) {
                list = new ArrayList<>(1);
                mListeners.put(key, list);
            }
            list.add(listener);
        } else {
            throw new InvalidParameterException("Invalid id.");
        }
    }

    private void removeListener(int id, ValueChangedListener listener) {

        if (listener == null) {
            throw new InvalidParameterException("\"listener\" should not be null.");
        }
        String key = mKeyCache.get(id, null);
        if (key != null) {
            if (TextUtils.isEmpty(key)) {
                throw new InvalidParameterException("Invalid id.");
            }
            if (!mSettings.containsKey(key)) {
                throw new InvalidParameterException("Invalid id, it is not a key of settings.");
            }

            if (mListeners.containsKey(key)) {
                List<ValueChangedListener> list = mListeners.get(key);
                list.remove(listener);
                if (list.size() == 0) {
                    mListeners.remove(key);
                }
            }
        } else {
            throw new InvalidParameterException("Invalid id.");
        }
    }

    private void apply(Map<String, Pair<SettingField, String>> apply) {
        SharedPreferences.Editor e = mPrefs.edit();
        for (Map.Entry<String, Pair<SettingField, String>> entry : apply.entrySet()) {
            String val = entry.getValue().second;
            Class<?> type = entry.getValue().first.getValueType();

            if (type == boolean.class) {
                e.putBoolean(entry.getKey(), Boolean.parseBoolean(val));
            } else if (type == int.class) {
                e.putInt(entry.getKey(), Integer.parseInt(val));
            } else if (type == long.class) {
                e.putLong(entry.getKey(), Long.parseLong(val));
            } else if (type == String.class) {
                e.putString(entry.getKey(), val);
            } else if (type == float.class) {
                e.putFloat(entry.getKey(), Float.parseFloat(val));
            } else {
                throw new InvalidParameterException("Unknown type = " + type.toString());
            }
            mSettings.put(entry.getKey(), new Pair<>(entry.getValue().first, val));
        }
        e.commit();
    }

    private String transformValueToString(Class<?> type, Object val) {
        if (type == boolean.class) {
            boolean newValue = Boolean.parseBoolean(val.toString());
            return Boolean.toString(newValue);
        } else if (type == int.class) {
            int newValue = Integer.parseInt(val.toString());
            return Integer.toString(newValue);
        } else if (type == long.class) {
            long newValue = Long.parseLong(val.toString());
            return Long.toString(newValue);
        } else if (type == String.class) {
            return val.toString();
        } else if (type == float.class) {
            float newValue = Float.parseFloat(val.toString());
            return Float.toString(newValue);
        } else {
            throw new InvalidParameterException("Unknown type = " + type.toString());
        }
    }

    private void reload() {
        Map<String, ?> currentSettings = mPrefs.getAll();
        Map<String, Pair<SettingField, String>> resetItems = new HashMap<>();
        for (String key : mSettings.keySet()) {
            SettingField field = mSettings.get(key).first;
            if (currentSettings.containsKey(key)) {
                String val = transformValueToString(field.getValueType(), currentSettings.get(key));
                mSettings.put(key, new Pair<>(field, val));
            } else {
                resetItems.put(key, new Pair<>(field, field.getDefaultValue()));
            }
        }
        if (resetItems.size() > 0) {
            apply(resetItems);
        }

        if (currentSettings.containsKey(mLastModifiedKey)) {
            String lastSettingModified = currentSettings.get(mLastModifiedKey).toString();
            if (TextUtils.isEmpty(lastSettingModified)) {
                mLastModifiedStamp = System.currentTimeMillis();
            } else {
                mLastModifiedStamp = Long.parseLong(lastSettingModified);
            }
        } else {
            mLastModifiedStamp = System.currentTimeMillis();
        }
    }
}
