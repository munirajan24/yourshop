package com.dvishapps.yourshop.ui.binding;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import com.dvishapps.yourshop.api.services.ImageService;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

public class Binding {

    @BindingAdapter(value = {"img_url", "img_name"}, requireAll = false)
    public static void setImage(ImageView imageView, String img_url, String img_name) {
        if (img_url == null)
            img_url = Constants.IMG_URL;
        if (img_name != null && !img_name.isEmpty())
            new ImageService(imageView, StringUtil.concat(img_url, img_name)).execute();
    }

    @BindingAdapter({"imageUrl"})
    public static void setImage(ImageView imageView, String img_url) {
        if (img_url != null && !img_url.isEmpty())
            new ImageService(imageView, img_url).execute();
//        imageView.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Picasso.get().load(img_url)
//                            .placeholder(R.drawable.image_loader)
////                            .networkPolicy(NetworkPolicy.OFFLINE)
//                            .resize(150, 150)
//                            .into(imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                }
//
//                                @Override
//                                public void onError(Exception e) {
//                                    Picasso.get()
//                                            .load(img_url)
//                                            .resize(150, 150)
//                                            .transform(new RoundedCornersTransformation(10, 0))
//                                            .placeholder(R.drawable.image_loader)
//                                            .noFade().into(imageView);
//                                }
//                            });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @BindingAdapter("android:custom_height")
    public static void setLayoutHeight(View view, Integer commentNumber) {
        if (commentNumber != 0)
            view.getLayoutParams().height = commentNumber;
    }

    @BindingAdapter("android:custom_width")
    public static void setLayoutWidth(View view, Integer commentNumber) {
        if (commentNumber != 0)
            view.getLayoutParams().width = commentNumber;
    }

    @InverseBindingAdapter(attribute = "android:custom_visibility")
    public static boolean getVisibility(@NotNull View view) {
        return true;
    }

    @BindingAdapter("android:custom_visibility")
    public static void setVisibility(@NotNull View view, Boolean v) {
        view.setVisibility(v ? View.VISIBLE : View.GONE);
    }

}
