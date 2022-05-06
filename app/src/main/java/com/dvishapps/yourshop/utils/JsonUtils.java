package com.dvishapps.yourshop.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.dvishapps.yourshop.app.Config;

public class JsonUtils {
    public static String toJsonString(Object o) {
        return Config.gson.toJson(o);
    }

    public static <T> T fromJsonString(String json, Class<T> dt) {
        return Config.gson.fromJson(json, dt);
    }

    public static JsonElement fromJsonString(String json) {
        return JsonParser.parseString(json);
    }
}
