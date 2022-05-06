package com.dvishapps.yourshop.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.User;

/**
 * The type Config.
 * Declare all global variables and configuration of application
 */
@SuppressLint("StaticFieldLeak")
public class Config {

    public static Context context;
    public static Cart cart;
    public static SharedPreferences.Editor editPreferences;
    public static SharedPreferences sharedPreferences;
    public static Gson gson;
    public static User currentUser;

    public static int TAX_VALUE = 3;
    public static int TAX_SHIPPING_VALUE = 7;
    public static int LIMIT = 10;
    public static int LIMIT_0 = 0;
    public static int OFFSET = 10;
    public static final int CALL_BACKGROUND_SETTINGS = 122;

    public static RequestQueue requestQueue;
    public static final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(1000);
}
