package com.ltbrew.brewbeer;

import android.app.Application;
import android.graphics.Typeface;

import com.ltbrew.brewbeer.presenter.util.DBManager;

/**
 * Created by 151117a on 2016/5/2.
 */
public class BrewApp extends Application {

    private static BrewApp app;
    public Typeface textFont;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        DBManager.initDB(this);
        textFont = Typeface.createFromAsset(getAssets(), "fonts/blesd.otf");
    }

    public static BrewApp getInstance(){
        return app;
    }

    public String getCurrentDev() {
        return null;
    }

}
