package com.longway.safeshareperferences;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by longway on 16/11/10. Email:longway1991117@sina.com sharePreferences manager 1.provide
 * data operator interface 2.observe delegate register 3.delegate notify observer
 */

public class SharePreferencesManager extends SharePreferencesObservable {
    private static final String TAG = SharePreferencesManager.class.getSimpleName();
    private static SharePreferencesManager sInstance;
    private ContentResolver mContentResolver;
    private Uri mUri;

    private ContentObserver mObserver;

    /**
     * initialize
     */
    private SharePreferencesManager(Context context) {
        mUri = SharePreferencesProvider.getAuthority(context);
        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                setChanged();
                String path = uri.getLastPathSegment();
                Log.e(TAG, "path:" + path);
                notifyObservers(path);
            }

            @Override
            public boolean deliverSelfNotifications() {
                return super.deliverSelfNotifications();
            }

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
            }
        };
        mContentResolver = context.getContentResolver();
        mContentResolver.registerContentObserver(SharePreferencesProvider.getNotifyAuthority(context), true, mObserver);
    }

    /**
     * destroy
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            mContentResolver.unregisterContentObserver(mObserver);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            super.finalize();
        }
    }

    /**
     * get instance
     *
     * @param context global {@link android.app.Application}
     */
    public static SharePreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SharePreferencesManager.class) {
                if (sInstance == null) {
                    sInstance = new SharePreferencesManager(context);
                }
            }
        }
        return sInstance;
    }

    public void registerOnSharePreferencesObserver(SharePreferencesObserver sharePreferencesObserver) {
        super.registerOnSharePreferencesObserver(sharePreferencesObserver);
    }

    public void unregisterOnSharePreferencesObserver(SharePreferencesObserver sharePreferencesObserver) {
        super.unregisterOnSharePreferencesObserver(sharePreferencesObserver);
    }

    /**
     * getAll
     */
    @Override
    public Map<String, ?> getAll() { // support
        return getMap();
    }

    /**
     * get value by type
     * @param cursor
     * @param ci
     * @return
     */
    private Object getValue(Cursor cursor, int ci) {
        int type = cursor.getType(ci);
        Object value = null;
        switch (type) {
            case Cursor.FIELD_TYPE_NULL:// default
                break;
            case Cursor.FIELD_TYPE_STRING:
                value = cursor.getString(ci);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                value = cursor.getInt(ci);
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                value = cursor.getFloat(ci);
                break;
            case Cursor.FIELD_TYPE_BLOB:
                value = CollectionUtils.byteConvertToSet(cursor.getBlob(ci), CollectionUtils.COMMA);
                break;
            default:
        }
        return value;
    }

    /**
     * delegate getAll
     * @return
     */
    private Map<String, ?> getMap() {
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(mUri, null, "", new String[]{Map.class.getName()}, null);
            if (cursor == null) {
                return Collections.EMPTY_MAP;
            }
            Map<String, Object> values = new HashMap<>();
            int columnCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    values.put(cursor.getColumnName(i), getValue(cursor, i));
                }
            }
            return values;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return Collections.EMPTY_MAP;
    }

    @Override
    public String getString(String key, @Nullable String defValue) {
        return getValue(key, defValue, String.class.getName());
    }


    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return CollectionUtils.stringConvertToSet(getValue(key, defValues, Set.class.getName()), CollectionUtils.COMMA);
    }

    @Override
    public int getInt(String key, int defValue) {
        return Integer.parseInt(getValue(key, defValue));
    }

    @Override
    public long getLong(String key, long defValue) {
        return Long.parseLong(getValue(key, defValue));
    }

    @Override
    public float getFloat(String key, float defValue) {
        return Float.parseFloat(getValue(key, defValue));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.parseBoolean(getValue(key, defValue));
    }

    @Override
    public boolean contains(String key) {
        String value = getValue(key, "");
        return !TextUtils.isEmpty(value);
    }

    /**
     * put delegate method
     */
    private boolean insert(String key, Object value) {
        ContentValues contentValues = new ContentValues();
        if (value instanceof String) {
            contentValues.put(key, (String) value);
        } else if (value instanceof Integer) {
            contentValues.put(key, (Integer) value);
        } else if (value instanceof Long) {
            contentValues.put(key, (Long) value);
        } else if (value instanceof Float) {
            contentValues.put(key, (Float) value);
        } else if (value instanceof Boolean) {
            contentValues.put(key, (Boolean) value);
        } else if (value instanceof byte[]) { // set
            contentValues.put(key, (byte[]) value);
        }
        return mContentResolver.insert(mUri, contentValues) != null;
    }

    @Override
    public boolean putString(String key, @Nullable String value) {
        return insert(key, value);
    }

    @Override
    public boolean putStringSet(String key, @Nullable Set<String> values) {
        return insert(key, CollectionUtils.setConvertToByte(values, CollectionUtils.COMMA));
    }

    @Override
    public boolean putInt(String key, int value) {
        return insert(key, value);
    }


    @Override
    public boolean putLong(String key, long value) {
        return insert(key, value);
    }

    @Override
    public boolean putFloat(String key, float value) {
        return insert(key, value);
    }

    @Override
    public boolean putBoolean(String key, boolean value) {
        return insert(key, value);
    }

    @Override
    public boolean remove(String key) {
        return mContentResolver.delete(mUri, key, null) == 1;
    }

    @Override
    public boolean clear() {
        return mContentResolver.delete(mUri, null, null) == 1;
    }

    /**
     * get delegate
     */
    private <T> String getValue(String key, T defaultValue, String... typeName) {
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(mUri, null, key, defaultValue != null ? new String[]{defaultValue.getClass().getName()} : typeName, defaultValue != null ? (CollectionUtils.isSet(defaultValue.getClass()) ? CollectionUtils.setConvertToString((Set<String>) defaultValue, CollectionUtils.COMMA) : String.valueOf(defaultValue)) : null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getString(0);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
