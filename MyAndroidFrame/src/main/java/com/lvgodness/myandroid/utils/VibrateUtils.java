package com.lvgodness.myandroid.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;


/**
 * Created by LYZ on 2017/7/30 0030.
 */

public class VibrateUtils {
    private static MediaPlayer mPlayer;
    private static Handler handler = new Handler();
    private static Task task = new Task();

    private static int warningNum = 0;

    //震动milliseconds毫秒
    public static void vibrate(final Context activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    //以pattern[]方式震动
    public static void vibrate(final Context activity, long[] pattern, int repeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
//        mPlayer = MediaPlayer.create(activity, R.raw.beep);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(false);

        if (warningNum == 0) {
            vib.vibrate(pattern, repeat);
            task.start();
        }
        warningNum++;
    }

    //取消震动
    public static void virateCancel(final Context activity) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);

        warningNum--;
        if (warningNum == 0) {
            task.stop();
            vib.cancel();
        }
    }

    private static class Task implements Runnable {
        void start() {
            //移除所有消息
            handler.removeCallbacks(task);
            handler.postDelayed(this, 200);
        }

        void stop() {
            handler.removeCallbacks(task);

        }

        @Override
        public void run() {
            handler.postDelayed(this, 1000);
            mPlayer.start();


        }
    }


}
