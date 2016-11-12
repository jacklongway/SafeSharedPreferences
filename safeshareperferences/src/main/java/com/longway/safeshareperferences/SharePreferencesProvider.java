package com.longway.safeshareperferences;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.Set;

/**
 * Created by longway on 16/11/10. Email:longway1991117@sina.com data provider 1.all process share
 * 2.safe thread 3.common manager data 4 emit observer
 */

public class SharePreferencesProvider extends ContentProvider {
    private static final String TAG = SharePreferencesProvider.class.getSimpleName();
    private static final String CONTENT = "content://";
    private static final int SP = 0x1;
    private UriMatcher mUriMatcher;
    private SharePreferences mSharePreferences;
    private String mAuthority;

    /**
     * init
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mAuthority = context.getPackageName() + "." + TAG;
        Log.e(TAG, mAuthority);
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(mAuthority, "/sp/*", SP);
        mSharePreferences = SharePreferences.getInstance(context, SharePreferences.DEFAULT_SP_NAME);
        return true;
    }

    /**
     * get authority
     */
    public static Uri getAuthority(Context context) {
        if (context == null) {
            return null;
        }
        return Uri.parse(CONTENT + context.getPackageName() + "." + TAG + "/sp/1");

    }

    /**
     * get notify authority
     */
    public static Uri getNotifyAuthority(Context context) {
        if (context == null) {
            return null;
        }
        return Uri.parse(CONTENT + context.getPackageName() + "." + TAG + "/sp/");

    }

    /**
     * query interface
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String defaultValue) {
        int code = mUriMatcher.match(uri);
        String[] columnNames = {"result"};
        Object[] results = {null};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        switch (code) {
            case SP:
                if (selection != null) {
                    if (selectionArgs != null && selectionArgs.length > 0) {
                        String type = selectionArgs[0];
                        if (TextUtils.equals(type, String.class.getName())) {// string
                            results = new String[]{mSharePreferences.getString(selection, defaultValue)};
                        } else if (TextUtils.equals(type, int.class.getName()) || TextUtils.equals(type, Integer.class.getName())) {// int
                            int dv = 0;
                            try {
                                dv = Integer.parseInt(defaultValue);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                            results = new Object[]{mSharePreferences.getInt(selection, dv)};
                        } else if (TextUtils.equals(type, long.class.getName()) || TextUtils.equals(type, Long.class.getName())) {// long
                            long dv = 0;
                            try {
                                dv = Long.parseLong(defaultValue);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                            results = new Object[]{mSharePreferences.getLong(selection, dv)};

                        } else if (TextUtils.equals(type, boolean.class.getName()) || TextUtils.equals(type, Boolean.class.getName())) {// boolean
                            results = new Object[]{mSharePreferences.getBoolean(selection, TextUtils.equals("true", defaultValue))};
                        } else if (TextUtils.equals(type, float.class.getName()) || TextUtils.equals(type, Float.class.getName())) {
                            float dv = 0;
                            try {
                                dv = Float.parseFloat(defaultValue);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                            results = new Object[]{mSharePreferences.getFloat(selection, dv)};
                        } else if (CollectionUtils.isSet(type)) { // set
                            Set<String> ds = CollectionUtils.stringConvertToSet(defaultValue, CollectionUtils.COMMA);
                            ds = mSharePreferences.getStringSet(selection, ds);
                            results = new String[]{CollectionUtils.setConvertToString(ds, CollectionUtils.COMMA)};
                        } else if (TextUtils.equals(type, Map.class.getName())) { // map
                            Map<String, ?> all = mSharePreferences.getAll();
                            Log.e(TAG, all != null ? all.toString() : "empty map");
                            Set<String> keys = all.keySet();
                            if (keys != null && !keys.isEmpty()) {
                                int size = keys.size();
                                String[] k = new String[size];
                                Object[] v = new Object[size];
                                int index = 0;
                                for (String key : keys) {
                                    k[index] = key;
                                    Object value = all.get(key);
                                    if (CollectionUtils.isSet(value)) {
                                        v[index] = CollectionUtils.setConvertToByte((Set<String>) all.get(key), CollectionUtils.COMMA);
                                    } else {
                                        v[index] = value;
                                    }
                                    ++index;
                                }
                                cursor = new MatrixCursor(k);
                                cursor.addRow(v);
                                return cursor;
                            } else {
                                return null; // ?
                            }
                        }
                    } else {
                        results = new Object[]{mSharePreferences.getString(selection, defaultValue)};
                    }
                }
                break;
            default:
                break;
        }
        cursor.addRow(results);
        return cursor;
    }


    /**
     * get type
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        int code = mUriMatcher.match(uri);
        switch (code) {
            case SP:
                return "application/xml"; // assume contentType
            default:
                break;
        }
        return null;
    }

    /**
     * insert interface
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = mUriMatcher.match(uri);
        boolean success = false;
        switch (code) {
            case SP:
                Set<String> keys = values.keySet();
                for (String key : keys) {
                    Object o = values.get(key);
                    Log.e(TAG, "key:" + key);
                    String type = o != null ? o.getClass().getName() : "null";
                    if (TextUtils.equals(type, String.class.getName())) {
                        if (mSharePreferences.putString(key, values.getAsString(key))) {
                            success = true;
                        }
                    } else if (TextUtils.equals(type, int.class.getName()) || TextUtils.equals(type, Integer.class.getName())) {
                        if (mSharePreferences.putInt(key, values.getAsInteger(key))) {
                            success = true;
                        }
                    } else if (TextUtils.equals(type, long.class.getName()) || TextUtils.equals(type, Long.class.getName())) {
                        if (mSharePreferences.putLong(key, values.getAsLong(key))) {
                            success = true;
                        }
                    } else if (TextUtils.equals(type, boolean.class.getName()) || TextUtils.equals(type, Boolean.class.getName())) {
                        if (mSharePreferences.putBoolean(key, values.getAsBoolean(key))) {
                            success = true;
                        }
                    } else if (TextUtils.equals(type, Float.class.getName()) || TextUtils.equals(type, Float.class.getName())) {
                        if (mSharePreferences.putFloat(key, values.getAsFloat(key))) {
                            success = true;
                        }
                    } else if (TextUtils.equals(type, byte[].class.getName())) {// set
                        if (mSharePreferences.putStringSet(key, CollectionUtils.byteConvertToSet(values.getAsByteArray(key), CollectionUtils.COMMA))) {
                            success = true;
                        }
                    }
                    if (success) {
                        Uri newUri = Uri.withAppendedPath(uri, key);
                        Log.e(TAG, newUri.toString());
                        getContext().getContentResolver().notifyChange(newUri, null);
                    }
                }
                break;
            default:
                break;
        }
        return success ? uri : null;
    }

    /**
     * remove&clear interface
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int result = 0;
        switch (code) {
            case SP:
                if (selection != null) {
                    result = mSharePreferences.remove(selection) ? 1 : 0;
                } else {
                    result = mSharePreferences.clear() ? 1 : 0;
                }
                break;
            default:
                break;
        }
        if (result == 1) {
            getContext().getContentResolver().notifyChange(Uri.withAppendedPath(uri, selection), null);
        }
        return result;
    }

    /**
     * update replace to insert
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
