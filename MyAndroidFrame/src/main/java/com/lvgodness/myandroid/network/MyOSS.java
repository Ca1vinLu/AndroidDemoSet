package com.lvgodness.myandroid.network;

import android.content.Context;
import android.os.Environment;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.lvgodness.myandroid.utils.CompressUtils;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by LYZ on 2017/3/25 0025.
 */

public class MyOSS {
    private static final String TAG = "MyOSS";

    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    private static final String accessKeyId = "zlN0arcl6DMQpBTf";
    private static final String accessKeySecret = "STDVRcq9CmVa7J6Iv2Y4l4LMweG48A";
    private static final String Bucket = "lizi-img";
    private static final String downloadObject = "sampleObject";

    private static OSS oss;
    private Context context;
    private static OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
    ;
    private static ClientConfiguration conf = new ClientConfiguration();
    ;

    private MyOSS() {

        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
    }

    public static String UplaodImages(final Context context, ArrayList<ImageItem> images) {
        if (images.size() == 0)
            return "";
        boolean uploadSuccess;
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
        ArrayList<String> uploadImages = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Random random = new Random(System.currentTimeMillis());
        String date = formatter.format(curDate);
        String uploadPath = "Uploads/" + date + "/";
        String DirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyAndroid/Pic/Compress/";
        for (int i = 0; i < images.size(); i++) {
            String path = images.get(i).path;
            random.setSeed(System.currentTimeMillis());
            String filename = String.valueOf(curDate.getTime() / 1000) + "_" + String.valueOf(random.nextInt(899999) + 100000);
            File oldFile = new File(path);
            File newFile = CompressUtils.compressPic(context, oldFile);


//            String fileUrl = oss.presignPublicObjectURL(Bucket, uploadPath + filename)+"?H=1280&W=720";
//            String fileUrl = "http://img.lizi123.cn/" + uploadPath + newFile.getName();
            uploadImages.add("http://img.lizi123.cn/" + uploadPath + newFile.getName());
            uploadSuccess = new OSSUploadUtils(oss, Bucket, uploadPath + newFile.getName(), newFile.getPath()).putObjectFromLocalFile(context);
            if (!uploadSuccess)
                return "0";
        }
        String temp = uploadImages.toString();
        temp = temp.substring(1, temp.length() - 1);
        return temp;
    }

    public static String UploadImage(final Context context, String image) {
        if (image.equals(""))
            return "";
        boolean uploadSuccess;
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Random random = new Random(System.currentTimeMillis());
        String date = formatter.format(curDate);
        String uploadPath = "Uploads/" + date + "/";
        String filename = String.valueOf(curDate.getTime() / 1000) + "_" + String.valueOf(random.nextInt(899999) + 100000);
        String DirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyAndroid/Pic/Compress/";
        File oldFile = new File(image);
        File newFile = CompressUtils.compressPic(context, oldFile);


//            String fileUrl = oss.presignPublicObjectURL(Bucket, uploadPath + filename)+"?H=1280&W=720";
        String fileUrl = "http://img.lizi123.cn/" + uploadPath + newFile.getName();

        uploadSuccess = new OSSUploadUtils(oss, Bucket, uploadPath + newFile.getName(), newFile.getPath()).putObjectFromLocalFile(context);

        if (uploadSuccess)
            return fileUrl;
        else return "0";
    }
}
