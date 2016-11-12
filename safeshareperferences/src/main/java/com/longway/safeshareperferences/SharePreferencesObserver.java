package com.longway.safeshareperferences;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by longway on 16/11/10. Email:longway1991117@sina.com
 * data observer
 */

public abstract class SharePreferencesObserver implements Observer {

    public abstract void onSharePreferencesChange(SharePreferencesObservable sharePreferencesObservable, String key);

    @Override
    public void update(Observable o, Object arg) {
        onSharePreferencesChange((SharePreferencesObservable) o, (String) arg);
    }
}
