package com.dvishapps.yourshop.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringUtil {
    private static DecimalFormat format = new DecimalFormat("#0.00");

    @NotNull
    public static String convertNumber(BigDecimal num) {
        return String.valueOf(format.format(num));
    }

    @NotNull
    public static String convertNumber(float num) {
        return String.valueOf(format.format(num));
    }

    @NotNull
    public static String convertNumber(double num) {
        return String.valueOf(format.format(num));
    }

    @NotNull
    public static String convertNumber(int num) {
        return String.valueOf(num);
    }

    @Contract(pure = true)
    public static <T> T convertObject(T obj, Class<?> cls) {
        return obj;
    }

    @NotNull
    public static String concat(String str1, String str2) {
        return String.format("%s%s", str1, str2);
    }

    @NotNull
    public static String concatWithStar(String str1, String str2) {
        return String.format("%s%s*", str1, str2);
    }

    @NotNull
    public static String concat(String str1, int str2) {
        return String.format("%s %s", str1, str2);
    }

    @NotNull
    public static String concat(String str1, double str2) {
        return String.format("%s %s", str1, str2);
    }

    @NotNull
    public static String concat(float str1, String str2) {
        return String.format("%s %s", str1, str2);
    }

    @NotNull
    public static String setString(String str1, Object str2) {
        return String.format(str1, str2);
    }
    @NotNull
    public static String setString(String str1) {
        return str1;
    }

    @NotNull
    public static String setString(String str1, Object str2, Object str3) {
        return String.format(str1, str2, str3);
    }
    public static String convertDec(double number) {
        return String.format("%.2f", number);
    }
}
