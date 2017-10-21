package com.lvgodness.bluetoothdemo;

import android.app.Presentation;
import android.content.Context;
import android.media.MediaRouter;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (GLSurfaceView) findViewById(R.id.surface_view);
        button = (Button) findViewById(R.id.button);

        surfaceView.setRenderer(new CubeRenderer());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPresent();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        showPresent();
    }

    private void showPresent() {

        MediaRouter mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
        MediaRouter.RouteInfo route = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        if (route != null) {
            Display presentationDisplay = route.getPresentationDisplay();
            if (presentationDisplay != null) {
                Presentation presentation = new MyPresentation(this, presentationDisplay);
                presentation.show();
            }
        }
    }
}
