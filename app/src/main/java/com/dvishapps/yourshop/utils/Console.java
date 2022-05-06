package com.dvishapps.yourshop.utils;

import android.util.Log;
import android.widget.Toast;

import com.dvishapps.yourshop.app.Config;

public class Console {

    public static void toast(String str) {
        Toast.makeText(Config.context, str, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String str) {
        Toast.makeText(Config.context, str, Toast.LENGTH_LONG).show();
    }

    public static void logError(String str) {
        Log.e("shop_log", "" + str);
    }

    public static void logDebug(String str) {
        Log.d("shop_log", "" + str);
    }

    public static void log(String tag, String str) {
        Log.e(tag, tag + " : "+ str);
    }

    public static void error(String str) {
        Toast.makeText(Config.context, str, Toast.LENGTH_SHORT).show();
    }
}
