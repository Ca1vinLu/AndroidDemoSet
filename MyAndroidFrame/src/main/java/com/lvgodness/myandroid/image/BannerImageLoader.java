package com.lvgodness.myandroid.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by LYZ on 2017/7/24.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideApp.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }
}

