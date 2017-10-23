package com.lvgodness.myandroid.utils;

import android.content.Context;
import android.os.Environment;

import com.nanchen.compresshelper.CompressHelper;

import java.io.File;

/**
 * Created by LYZ on 2017/10/11 0011.
 */

public class CompressUtils {

    public static File compressPic(Context context, String pic) {
        return compressPic(context, new File(pic));

    }

    public static File compressPic(Context context, File pic) {
        String filename = "changyun_" + pic.getName();
        String DirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChangYun/Pic/Compress/";
        return new CompressHelper.Builder(context)
                .setMaxWidth(720)  // 默认最大宽度为720
                .setMaxHeight(1280) // 默认最大高度为960
                .setQuality(80)    // 默认压缩质量为80
                .setDestinationDirectoryPath(DirectoryPath)
                .setFileName(filename)
                .build()
                .compressToFile(pic);


    }


}
