package com.longway.safeshareperferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Created by longway on 16/11/10. Email:longway1991117@sina.com
 * a sharePreferences simple encapsulate
 */

public class SharePreferences extends SharePreferencesObservable {
    public static final String DEFAULT_SP_NAME = "common";
    private SharedPreferences mSharedPreferences;

    private SharePreferences() {

    }

    public static SharePreferences getInstance(Context context, String filename) {
        SharePreferences sharePreferences = new SharePreferences();
        sharePreferences.mSharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sharePreferences;
    }

    @Override
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    @Override
    public String getString(String key, @Nullable String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return mSharedPreferences.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    @Override
    public boolean putString(String key, @Nullable String value) {
        return mSharedPreferences.edit().putString(key, value).commit();
    }

    @Override
    public boolean putStringSet(String key, @Nullable Set<String> values) {
        return mSharedPreferences.edit().putStringSet(key, values).commit();
    }

    @Override
    public boolean putInt(String key, int value) {
        return mSharedPreferences.edit().putInt(key, value).commit();
    }

    @Override
    public boolean putLong(String key, long value) {
        return mSharedPreferences.edit().putLong(key, value).commit();
    }

    @Override
    public boolean putFloat(String key, float value) {
        return mSharedPreferences.edit().putFloat(key, value).commit();
    }

    @Override
    public boolean putBoolean(String key, boolean value) {
        return mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    @Override
    public boolean remove(String key) {
        return mSharedPreferences.edit().remove(key).commit();
    }

    @Override
    public boolean clear() {
        return mSharedPreferences.edit().clear().commit();
    }
}
