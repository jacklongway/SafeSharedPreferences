package com.longway.safeshareperferences;

import android.text.TextUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by longway on 16/11/12. Email:longway1991117@sina.com collections base utils
 */

public class CollectionUtils {
    public static final String COMMA = ",";

    private CollectionUtils() {
        throw new AssertionError("CollectionUtils not instance.");
    }

    /**
     * set convert to byte[]
     */
    public static byte[] setConvertToByte(Set<String> values, String separate) {
        if (values == null) {
            return null;
        }
        if (values.isEmpty()) {
            return new byte[0];
        }
        String sp = TextUtils.isEmpty(separate) ? COMMA : separate;
        StringBuilder sb = new StringBuilder(64);
        int _size = values.size() - 1;
        int count = 0;
        for (String value : values) {
            sb.append(value);
            if (count != _size) {
                sb.append(sp);
            }
            count++;
        }
        return sb.toString().getBytes();
    }

    /**
     * byte[] convert to set
     */
    public static Set<String> byteConvertToSet(byte[] blob, String separate) {
        if (blob == null) {
            return null;
        }
        if (blob.length == 0) {
            return Collections.EMPTY_SET;
        }
        String sp = TextUtils.isEmpty(separate) ? COMMA : separate;
        String values = new String(blob);
        String[] sV = values.split(sp);
        Set<String> set = new HashSet<>();
        for (String v : sV) {
            set.add(v);
        }
        return set;
    }

    /**
     * string convert to set
     */
    public static Set<String> stringConvertToSet(String values, String separate) {
        if (values == null) {
            return null;
        }
        if (values.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        String sp = TextUtils.isEmpty(separate) ? COMMA : separate;
        String[] sV = values.split(sp);
        Set<String> set = new HashSet<>();
        for (String v : sV) {
            set.add(v);
        }
        return set;
    }

    /**
     * set convert to string
     */
    public static String setConvertToString(Set<String> values, String separate) {
        if (values == null) {
            return null;
        }
        if (values.isEmpty()) {
            return "";
        }
        String sp = TextUtils.isEmpty(separate) ? COMMA : separate;
        StringBuilder sb = new StringBuilder(64);
        int _size = values.size() - 1;
        int count = 0;
        for (String value : values) {
            sb.append(value);
            if (count != _size) {
                sb.append(sp);
            }
            count++;
        }
        return sb.toString();
    }

    /**
     * is set class
     */
    public static boolean isSet(String type) {
        if (type == null) {
            return true;
        }
        try {
            Class<?> c = Class.forName(type);
            if (Set.class.isAssignableFrom(c)) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * override is set class
     */
    public static boolean isSet(Class<?> type) {
        if (Set.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    /**
     * override is set class
     */
    public static boolean isSet(Object obj) {
        if (obj == null) {
            return false;
        }
        if (Set.class.isAssignableFrom(obj.getClass())) {
            return true;
        }
        return false;
    }


}
