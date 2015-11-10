package com.kim.weibao.content;

import android.app.Application;

/**
 * Created by 伟阳 on 2015/10/29.
 */
public class App extends Application {
    public static String USERID = "";
    public static String ROLE = "";

    public static String getUSERID() {
        return USERID;
    }

    public static void setUSERID(String USERID) {
        App.USERID = USERID;
    }

    public static String getROLE() {
        return ROLE;
    }

    public static void setROLE(String ROLE) {
        App.ROLE = ROLE;
    }
}
