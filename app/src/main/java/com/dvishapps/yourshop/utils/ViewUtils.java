package com.dvishapps.yourshop.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

public class ViewUtils {

    public static void changeViewColor(@NotNull View view, String color) {
        view.setBackgroundColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(10);
        }
    }

    public static void changeViewColor(View view, int color, View oldView, int oldColor) {
        if (view != null) {
            view.setBackgroundColor(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(5);
            }
        }
        if (oldView != null)
            oldView.setBackgroundColor(oldColor);
    }

    public static void changeViewDrawable(View view, Drawable color, View oldView, Drawable oldColor) {
        if (view != null) {
            view.setBackground(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(5);
            }
        }
        if (oldView != null)
            oldView.setBackground(oldColor);
    }

    public static void removeView(@NotNull View view) {
        ViewGroup vg = (ViewGroup) (view.getParent());
        if (vg != null)
            vg.removeView(view);
    }

    public static void hideView(View view) {
        if (view != null)
            view.setVisibility(View.INVISIBLE);
    }

    public static void showView(View view) {
        if (view != null)
            view.setVisibility(View.VISIBLE);
    }
}
