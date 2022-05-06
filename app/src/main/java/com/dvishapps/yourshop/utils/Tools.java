package com.dvishapps.yourshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    @Contract("null -> false")
    public static boolean isEmpty(EditText input) {
        if (input != null)
            return input.getText().toString().trim().matches("");
        return false;
    }

    @NotNull
    public static String inputString(@NotNull EditText input) {
        return input.getText().toString();
    }

    /**
     * @param context
     * @param menuItem
     * @param count
     */
    public static void setBadgeCount(Context context, @NotNull MenuItem menuItem, String count) {
        LayerDrawable icon = null;
//        Drawable icon = null;

        try {
            icon = (LayerDrawable) menuItem.getIcon();
        } catch (Exception e) {
//            icon =  menuItem.getIcon();
//            icon = (androidx.core.graphics.drawable.WrappedDrawableApi21) menuItem.getIcon();
            e.printStackTrace();
            return;
        }

        CountDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }
        badge.setCount(count);
        try {
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String round(double d) {
        return String.format("%.2f", d);
    }

    public static String round1(double d) {
        return String.format("%.1f", d);
    }

    public static Bitmap getBitmapFromByteArray(byte[] bitmapdata) {
        if (bitmapdata == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    public static byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static boolean setOfferPriceVisibility(String offerPrice) {

        if (offerPrice != null) {
            if (offerPrice.length() == 0) {
                return false;
            } else {
                return !offerPrice.equals(0);
            }
        } else {
            return false;
        }
    }

    public static String setOfferPrice(String originalPrice, String offerPrice) {
        String oldPrice = "" + originalPrice;
        if (offerPrice.length() > 0) {
            oldPrice = Integer.parseInt(originalPrice) + Integer.parseInt(offerPrice) + "";
        }
        return oldPrice;
    }


    @NotNull
    public static String setOfferPrice2(float originalPrice, float offerPrice) {
        String oldPrice = "" + originalPrice;
        if (offerPrice > 0) {
            oldPrice = originalPrice + offerPrice + "";
        }
        return oldPrice;
    }


    @NotNull
    public static String concat(String str1, String str2) {
        return String.format("%s%s", str1, str2);
    }

    public static String getDate(String dateStamp) {

//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return "";
        }

//            SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat convetDateFormat = null;
//        try {
//            convetDateFormat = new SimpleDateFormat("E,d MMM YYYY");
//        } catch (Exception e) {
        convetDateFormat = new SimpleDateFormat("E,d MMM yyyy");
//            e.printStackTrace();
//        }

        return convetDateFormat.format(date);

    }
    public static String getDateAndTime(String dateStamp) {

//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return "";
        }

//            SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat convetDateFormat = null;
//        try {
//            convetDateFormat = new SimpleDateFormat("E,d MMM YYYY");
//        } catch (Exception e) {
        convetDateFormat = new SimpleDateFormat("E,d MMM yyyy\n HH:mm a");
//            e.printStackTrace();
//        }

        return convetDateFormat.format(date);

    }

    public static void setImage(String img_url, String img_name, ImageView imageView) {
        try {
            Picasso.get().load(img_url.concat(img_name))
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(imageView);
        } catch (Exception e) {
        }
    }

    public static Bitmap toGrayscale(Bitmap srcImage) {

        Bitmap bmpGrayscale = Bitmap.createBitmap(srcImage.getWidth(), srcImage.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(srcImage, 0, 0, paint);

        return bmpGrayscale;
    }

    public static void setShopImageBlackWhite(String img_url, String img_name, ImageView imageView, Context mContext) {
        try {
            if (mContext != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(img_url.concat(img_name))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(Tools.toGrayscale(resource));
                            }
                        });
            }
        } catch (Exception e) {
//            Console.error(""+e.getMessage());
            imageView.setImageResource(R.drawable.placeholder_image);
        }
    }

    public static void setShopImage(String img_url, String img_name, ImageView imageView) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            Picasso.get().load(img_url.concat(img_name))
                    .placeholder(R.drawable.ic_shop_placeholder_small_vector)
                    .error(R.drawable.ic_shop_placeholder_small_vector)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
//                imageView.setBackgroundResource(R.drawable.ic_shop_placeholder_small_vector);
                        }

                    })
            ;
        } catch (Exception e) {
//            Console.error(""+e.getMessage());
            imageView.setImageResource(R.drawable.ic_shop_placeholder_small_vector);
        }
    }

    public static void setImageWithSuccess(String img_url, String img_name, ImageView imageView) {
        try {
            Picasso.get().load(img_url.concat(img_name)).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                }
            })
            ;
        } catch (Exception e) {
        }
    }

    public static void setImageWithShimmerEffect(String img_url, String img_name, ImageView imageView, ShimmerFrameLayout shimmerViewContainer) {
        try {
            Picasso.get().load(img_url.concat(img_name)).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                }
            })
            ;
        } catch (Exception e) {
        }
    }

    public static void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) Config.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public static double distanceInKms(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 / 0.6213;
        float distance = Float.parseFloat(dist + "");


        double distanceNew = SphericalUtil.computeDistanceBetween(new LatLng(lat1, lon1), new LatLng(lat2, lon2));
        distanceNew = distanceNew / 1000;
        return (distanceNew);
    }

    public static double distanceInKmsSimple(double lat1, double lon1, double lat2, double lon2) {


        double distanceNew = SphericalUtil.computeDistanceBetween(new LatLng(lat1, lon1), new LatLng(lat2, lon2));
        distanceNew = distanceNew / 1000;
        return (distanceNew);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static Bitmap imageReSize(Bitmap shopImgBitmap, int maximumKb) {
//        maximumKb = 10;
        float compressPercentage = 100;
        Bitmap resizedBitmap = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        shopImgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        lengthbmp = lengthbmp / 1024;
        Console.logDebug("initial img size : " + lengthbmp + "kb");
//        Console.toast("img size : " + lengthbmp + "kb");

        try {
            for (long i = lengthbmp; i > maximumKb; compressPercentage = compressPercentage * 80 / 100) {
                resizedBitmap = Bitmap.createScaledBitmap(shopImgBitmap,
                        Math.round(shopImgBitmap.getWidth() * compressPercentage / 100),
                        Math.round(shopImgBitmap.getHeight() * compressPercentage / 100),
                        false);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                byte[] imageInByte2 = stream2.toByteArray();

                lengthbmp = imageInByte2.length;
                lengthbmp = lengthbmp / 1024;
                Console.logDebug("img size : " + lengthbmp + "kb");
                i = lengthbmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (long i = lengthbmp; i > maximumKb; compressPercentage = compressPercentage * 95 / 100) {
//            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
//            shopImgBitmap.compress(Bitmap.CompressFormat.JPEG, compressPercentage, stream2);
//            byte[] imageInByte2 = stream2.toByteArray();
//            Bitmap b = BitmapFactory.decodeByteArray(imageInByte2, 0, imageInByte2.length);
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
//                    shopImgBitmap, shopImgBitmap.getWidth(), shopImgBitmap.getHeight(), false);
//
//            lengthbmp = imageInByte2.length;
//            lengthbmp = lengthbmp / 1024;
//            Console.logDebug("img size : " + lengthbmp + "kb");
//            i = lengthbmp;
//        }

        Console.logDebug("final img size : " + lengthbmp + "kb");
//        Console.toast("final img size : " + lengthbmp + "kb");

        if (resizedBitmap != null) {
            return resizedBitmap;
        } else {
            return shopImgBitmap;
        }
    }

    public static String parseTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String parseDateTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static void setShopImageOffline(ImageView view, String shopId, String imgUrl, Context context) {
        ShopListDb dbOfflineImagesAdapter = new ShopListDb(context);
        if (dbOfflineImagesAdapter.CheckIsShopExist(shopId)) {
            view.setImageBitmap(dbOfflineImagesAdapter.getImageWithShopId(shopId));
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(imgUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            view.setImageBitmap(resource);
                            dbOfflineImagesAdapter.insertImage(shopId, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

    }

    public static void customAlertNew(Context mContext, String message, String title, String strOk, String strCancel) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);
        View view_cancel = (View) dialog.findViewById(R.id.view_cancel);

        ImageView closeImg = dialog.findViewById(R.id.close_img);

        txtTitle.setText(title);
        txtMessage.setText(message);
        ok.setText(strOk);
        cancel.setText(strCancel);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public static void customProgressNew(Context mContext) {
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);


        dialog.show();
    }

}
