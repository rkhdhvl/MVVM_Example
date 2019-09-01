package com.example.mvvm_java.util;

import android.content.Context;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm_java.R;

public class ImageUtils {
    public static void loadImage(ImageView imageView, String url, CircularProgressDrawable circularProgressDrawable)
    {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
              .setDefaultRequestOptions(requestOptions)
              .load(url)
              .into(imageView);
    }

    public static CircularProgressDrawable getProgressDrawable(Context context)
    {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();
        return  circularProgressDrawable;
    }

}
