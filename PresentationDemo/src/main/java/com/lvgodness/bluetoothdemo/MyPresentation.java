package com.lvgodness.bluetoothdemo;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by LYZ on 2017/9/14 0014.
 */

public class MyPresentation extends Presentation {
    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
