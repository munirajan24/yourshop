package com.dvishapps.yourshop.api.services;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageRequest;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;

import org.jetbrains.annotations.NotNull;

public class ImageService extends AsyncTask<Void, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    private ImageView imageView;
    private String url;
    private ImageRequest request;

    public ImageService(@NotNull ImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        if (Config.mCache.get(url) != null) {
            return Config.mCache.get(url);
        } else {
            request = new ImageRequest(url,
                    response -> {
                        Config.mCache.put(url, response);
                        imageView.setImageBitmap(response);
                    }, 150, 150,
                    ImageView.ScaleType.CENTER,
                    Bitmap.Config.RGB_565,
                    error -> {
//                        Log.e("TAG", "doInBackground: ", error);
                        imageView.setImageResource(R.drawable.placeholder_image);
                    }
            );
            Config.requestQueue.add(request);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
//        else imageView.setImageResource(R.drawable.image_loader);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        imageView.setImageResource(R.drawable.image_loader);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
        Log.e("TAG", "onCancelled: ");
    }

    interface OnImageLoad {
        void onFinishLoading();
    }
}
