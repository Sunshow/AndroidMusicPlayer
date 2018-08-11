package org.example.android.amp.util.pref;

import android.content.SharedPreferences;

import org.example.android.amp.app.AmpApp;

public class PrefUtils {

    private static final String PREF_DB = "com.example.android.uamp";

    public static void setUserLogin(boolean isLogin) {
        SharedPreferences user_info = AmpApp.getInstance().getSharedPreferences(PREF_DB, 0);
        SharedPreferences.Editor editor = user_info.edit();

        editor.putBoolean("isLogged", isLogin);
        editor.apply();
    }

    public static boolean isUserLogin() {
        SharedPreferences user_info = AmpApp.getInstance().getSharedPreferences(PREF_DB, 0);
        return user_info.getBoolean("isLogged", false);
    }

}
