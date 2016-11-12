package com.longway.sharepreferencesmultiprocessaccess;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.longway.safeshareperferences.SharePreferencesManager;
import com.longway.safeshareperferences.SharePreferencesObservable;
import com.longway.safeshareperferences.SharePreferencesObserver;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SharePreferencesObserver mSharePreferencesObserver = new SharePreferencesObserver() {
        @Override
        public void onSharePreferencesChange(SharePreferencesObservable sharePreferencesObservable, String key) {
            //Log.e(TAG, "key:" + key + ",value:" + mSharePreferencesManager.getBoolean(key, false));
            Log.e(TAG, "map result <<" + mSharePreferencesManager.getAll().toString());

        }
    };

    private SharePreferencesManager mSharePreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, TestService.class));
        setContentView(R.layout.activity_main);
        mSharePreferencesManager = SharePreferencesManager.getInstance(this);
        mSharePreferencesManager.registerOnSharePreferencesObserver(mSharePreferencesObserver);
        long start = SystemClock.currentThreadTimeMillis();
        Log.e(TAG, mSharePreferencesManager.putBoolean("isExpire", true) + "");
        Log.e(TAG, mSharePreferencesManager.contains("isExpire") + "");
        Log.e(TAG, mSharePreferencesManager.getBoolean("isExpire", false) + "");
        Log.e(TAG, mSharePreferencesManager.putInt("int", 3) + "");
        Log.e(TAG, mSharePreferencesManager.getInt("int", -1) + "");
        Log.e(TAG, mSharePreferencesManager.putFloat("float", 3.9f) + "");
        Log.e(TAG, mSharePreferencesManager.getFloat("float", 0.0f) + "");
        Log.e(TAG, mSharePreferencesManager.putLong("long", 40L) + "");
        Log.e(TAG, mSharePreferencesManager.getLong("long", 0L) + "");
        Log.e(TAG, mSharePreferencesManager.putString("string", "小马哥") + "");
        Log.e(TAG, mSharePreferencesManager.getString("string", "马云"));
        Log.e(TAG, mSharePreferencesManager.remove("string") + "");
        Set<String> set = new HashSet<>();
        set.add("hello1");
        set.add("world1");
        Log.e(TAG, mSharePreferencesManager.putStringSet("set", set) + "");
        Log.e(TAG, mSharePreferencesManager.getStringSet("set", null) + "");
        Log.e(TAG, "map result <<" + mSharePreferencesManager.getAll().toString());
        long end = SystemClock.currentThreadTimeMillis();
        Log.e(TAG, "consume time:" + (end - start) + "ms"); //10ms very fast haha
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSharePreferencesManager.unregisterOnSharePreferencesObserver(mSharePreferencesObserver);
    }

    public void put(View view) {
        boolean success = mSharePreferencesManager.clear();
        Log.e(TAG, Boolean.toString(success));
        Log.e(TAG, "map result <<" + mSharePreferencesManager.getAll().toString());

    }

    @Override
    protected void onNewIntent(Intent intent) {
    }
}
