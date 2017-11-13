package com.lvgodness.printerhelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private List<String> mSelected = new ArrayList<>();
    private Button choosePhoto;
    private Button printPhoto;
    private PrintHelper photoPrinter;
    private Context mActivity;

    private PrintAttributes attributes = new PrintAttributes.Builder()
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4.asLandscape())
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choosePhoto = (Button) findViewById(R.id.choose_photo);
        printPhoto = (Button) findViewById(R.id.printPhoto);

        choosePhoto.setOnClickListener(this);
        printPhoto.setOnClickListener(this);
        photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        mActivity = this;
    }

    private void printPhoto() {
        if (mSelected.size() > 0) {
//            for (int i = 0; i < 4; i++) {
//                photoPrinter.printBitmap("androids_test_print", bitmap);
//            }


//            try {
            Bitmap bitmap = BitmapFactory.decodeFile(mSelected.get(0));
//                bitmap = Glide.with(getApplicationContext())
//                        .load("http://luyiapp-img.heiyou.net/ProductDetial/2017-11-02/1509613022_442347.jpg")
//                        .asBitmap()
//                        //                    .override(819, 580)
//                        .into(100, 100)
//                        .get();

            Glide.with(getApplicationContext())
                    .load("http://luyiapp-img.heiyou.net/ProductDetial/2017-11-02/1509613022_442347.jpg")
                    .asBitmap()
                    .override(819, 580)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Log.d(TAG, "onLoadFailed: ");
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.d(TAG, "onResourceReady: ");
                            List<Bitmap> data = new ArrayList<>();
                            data.add(resource);
                            data.add(resource);
                            data.add(resource);
                            data.add(resource);
                            doPrint(data);
                        }
                    });

//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }


//            List<Bitmap> data = new ArrayList<>();
//            data.add(bitmap);
//            data.add(bitmap);
//            data.add(bitmap);
//            data.add(bitmap);
//            doPrint(data);
        }

//        String epsonPrintApkPackageName = "epson.print";
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.SEND");
//        intent.setPackage(epsonPrintApkPackageName);
//        intent.setClassName(epsonPrintApkPackageName, "epson.print.ActivityDocsPrintPreview");
//
//        Uri photoOutputUri = FileProvider.getUriForFile(
//                this,
//                getPackageName() + ".fileprovider",
//                new File(mSelected.get(0)));
//
//        intent.putExtra("android.intent.extra.STREAM", photoOutputUri);
//        intent.setType("image/jpeg");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        getBaseContext().startActivity(intent);
    }

    private void choosePhoto() {
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(1)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(0);
//        Intent toGallery = new Intent(Intent.ACTION_GET_CONTENT);
//        toGallery.setType("image/*");
//        toGallery.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(toGallery, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);

            Toast.makeText(this, mSelected.get(0), Toast.LENGTH_SHORT).show();
            Log.d("Matisse", "mSelected: " + mSelected);
            printPhoto.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_photo:
                choosePhoto();
                break;
            case R.id.printPhoto:
                printPhoto();
                break;
        }
    }

    private void doPrint(List<Bitmap> data) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = getString(R.string.app_name) + " Document";

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        if (printManager != null) {
            printManager.print(jobName, new MyPrintDocumentAdapter(this, data),
                    attributes); //
        }
    }
}
