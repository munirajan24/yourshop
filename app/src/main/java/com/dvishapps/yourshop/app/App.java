package com.dvishapps.yourshop.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.android.volley.toolbox.Volley;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.google.gson.GsonBuilder;

public class App extends Application {
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        Config.context = getApplicationContext();
        Config.sharedPreferences = getSharedPreferences(Constants.PREFERENCE, 0);
        Config.editPreferences = Config.sharedPreferences.edit();
        Config.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        if (Config.sharedPreferences.getString(Constants.CART, "null").matches("null")) {
            Config.cart = new Cart();
            Config.editPreferences.putString(Constants.CART, JsonUtils.toJsonString(Config.cart));
        } else {
            Config.cart = new Cart();
            Config.cart.initCart(JsonUtils.fromJsonString(Config.sharedPreferences.getString(Constants.CART, "null"), Cart.class));
        }

        if (!Config.sharedPreferences.getString(Constants.LOGGED_USER, "null").matches("null")) {
            Config.currentUser = JsonUtils.fromJsonString(Config.sharedPreferences.getString(Constants.LOGGED_USER, "null"), User.class);
        }

        Config.requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
