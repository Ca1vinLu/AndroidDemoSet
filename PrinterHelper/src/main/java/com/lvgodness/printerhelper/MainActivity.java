package com.lvgodness.printerhelper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> mSelected = new ArrayList<>();
    private Button choosePhoto;
    private Button printPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choosePhoto = (Button) findViewById(R.id.choose_photo);
        printPhoto = (Button) findViewById(R.id.printPhoto);

        choosePhoto.setOnClickListener(this);
        printPhoto.setOnClickListener(this);
    }

    private void printPhoto() {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.droids);
        Bitmap bitmap = BitmapFactory.decodeFile(mSelected.get(0));
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
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
}
