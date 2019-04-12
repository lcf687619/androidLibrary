package com.core.lib_core.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.core.lib_core.widgets.CircleImageView;

/**
 * @author 李澄锋<br>
 * 2018/9/26
 */
public class GlideUtils {

    /**
     * 加载网络、本地图片,直接显示
     *
     * @param context     当前的上下文
     * @param url         图片的地址
     * @param placeholder 显示的默认图
     * @param imageView   需要显示的控件
     */
    public static void loadImage(Context context, String url, int placeholder, final ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        if (placeholder == 0) {
            requestOptions.centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL);
            if (imageView instanceof CircleImageView) {
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                imageView.setImageDrawable(resource);
                            }
                        });
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(imageView);
            }
        } else {
            requestOptions.centerCrop().placeholder(placeholder).error(placeholder).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL);
            if (imageView instanceof CircleImageView) {
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                imageView.setImageDrawable(resource);
                            }
                        });
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(imageView);
            }
        }
    }

    /**
     * 加载圆形的图片
     *
     * @param context
     * @param url
     * @param placeholder
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, int placeholder, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(placeholder).error(placeholder).circleCrop()
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ColorDrawable placeholder, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(placeholder).error(placeholder)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, int placeholder, int width, int height, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(placeholder).error(placeholder).override(width, height)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 使用加载的图片的bitmap,到显示的控件中
     *
     * @param context      当前的上下文
     * @param url          图片的地址
     * @param placeholder  显示的默认图
     * @param simpleTarget 加载完bitmap的回调
     */
    public static void loadImage(Context context, String url, int placeholder, SimpleTarget simpleTarget) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(placeholder).error(placeholder)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(simpleTarget);
    }

    /**
     * 使用加载的图片的bitmap,到显示的控件中
     *
     * @param context      当前的上下文
     * @param url          图片的地址
     * @param simpleTarget 加载完bitmap的回调
     */
    public static void loadImage(Context context, String url, ColorDrawable placeColor, SimpleTarget simpleTarget) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(placeColor).error(placeColor)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(simpleTarget);
    }

    public static void loadImage(Context context, String url, int width, int height, SimpleTarget<Bitmap> simpleTarget) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop()
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(width, height);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(simpleTarget);
    }

    public static void loadImageNoCrop(Context context, String url, ColorDrawable placeColor, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(placeColor).error(placeColor);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }


    public static void loadImageNoCropST(Context context, String url, ColorDrawable placeColor, SimpleTarget<Bitmap> simpleTarget) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(placeColor).error(placeColor);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(simpleTarget);
    }

}
