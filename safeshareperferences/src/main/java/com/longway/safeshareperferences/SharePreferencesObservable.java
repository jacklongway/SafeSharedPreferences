package com.longway.safeshareperferences;

import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * Created by longway on 16/11/10. Email:longway1991117@sina.com sharePreference observable
 */

public abstract class SharePreferencesObservable extends Observable {

    /**
     * get all current not support
     */
    public abstract Map<String, ?> getAll();


    /**
     * getString
     */
    public abstract String getString(String key, @Nullable String defValue);

    /**
     * getStringSet current not support
     */
    public abstract Set<String> getStringSet(String key, @Nullable Set<String> defValues);

    /**
     * getInt
     */
    public abstract int getInt(String key, int defValue);

    /**
     * getLong
     */
    public abstract long getLong(String key, long defValue);

    /**
     * getFloat
     */
    public abstract float getFloat(String key, float defValue);

    /**
     * getBoolean
     */
    public abstract boolean getBoolean(String key, boolean defValue);

    /**
     * contains
     */
    public abstract boolean contains(String key);

    /**
     * putString
     */
    public abstract boolean putString(String key, @Nullable String value);

    /**
     * putStringSet current not support
     */
    public abstract boolean putStringSet(String key, @Nullable Set<String> values);

    /**
     * putInt
     */
    public abstract boolean putInt(String key, int value);

    /**
     * putLong
     */
    public abstract boolean putLong(String key, long value);

    /**
     * putFloat
     */
    public abstract boolean putFloat(String key, float value);

    /**
     * putBoolean
     */
    public abstract boolean putBoolean(String key, boolean value);

    /**
     * remove
     */
    public abstract boolean remove(String key);

    /**
     * clear
     */
    public abstract boolean clear();

    /**
     * register observer {@link SharePreferencesObserver}
     */
    public void registerOnSharePreferencesObserver(SharePreferencesObserver sharePreferencesObserver) {
        addObserver(sharePreferencesObserver);
    }

    /**
     * unregister observer {@link SharePreferencesObserver}
     */
    public void unregisterOnSharePreferencesObserver(SharePreferencesObserver sharePreferencesObserver) {
        deleteObserver(sharePreferencesObserver);
    }

}
