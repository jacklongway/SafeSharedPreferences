package com.longway.sharepreferencesmultiprocessaccess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.longway.safeshareperferences.SharePreferencesManager;
import com.longway.safeshareperferences.SharePreferencesObservable;
import com.longway.safeshareperferences.SharePreferencesObserver;

/**
 * Created by longway on 16/11/11. Email:longway1991117@sina.com
 */

public class TestService extends Service {


    private static final String TAG = TestService.class.getSimpleName();

    private SharePreferencesObserver mSharePreferencesObserver = new SharePreferencesObserver() {
        @Override
        public void onSharePreferencesChange(SharePreferencesObservable sharePreferencesObservable, String key) {
            //Log.e(TAG, "key:" + key + ",value:" + mSharePreferencesManager.getBoolean(key, false));
            Log.e(TAG, "map result <<" + mSharePreferencesManager.getAll().toString());
        }
    };

    private SharePreferencesManager mSharePreferencesManager;

    @Override
    public void onCreate() {
        mSharePreferencesManager = SharePreferencesManager.getInstance(this);
        mSharePreferencesManager.registerOnSharePreferencesObserver(mSharePreferencesObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharePreferencesManager.unregisterOnSharePreferencesObserver(mSharePreferencesObserver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
