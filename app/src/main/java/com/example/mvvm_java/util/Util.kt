package com.example.mvvm_java.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvvm_java.R

fun getProgressDrawable(context:Context) : CircularProgressDrawable
{
    return  CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }

    fun ImageView.loadImage(uri : String? , progressDrawable: CircularProgressDrawable)
    {
       val options:RequestOptions = RequestOptions()
               .placeholder(progressDrawable)
               .error(R.mipmap.ic_launcher)

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(uri)
                .into(this)
    }

}