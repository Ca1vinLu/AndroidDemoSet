package me.lyz.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    public static final String RX1 = "RxJava1.x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fun1();
        Fun2();
    }

    private void Fun1() {

        Observable switcher = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("On");
                subscriber.onNext("Off");
                subscriber.onNext("On");
                subscriber.onNext("On");
                subscriber.onCompleted();
            }
        });


        Subscriber light = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                //被观察者的onCompleted()事件会走到这里;
                Log.d("Fun1", "结束观察...\n");
            }

            @Override
            public void onError(Throwable e) {
                //出现错误会调用这个方法
            }

            @Override
            public void onNext(String s) {
                //处理传过来的onNext事件
                Log.d("Fun1", "handle this---" + s);
            }
        };
//        Observable.from()
        switcher.subscribe(light);

    }

    private void Fun2() {
        Observable.just("On", "Off", "On", null, "On")
                //这就是在传递过程中对事件进行过滤操作
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.d("Fun2", String.valueOf(s != null));
                        return s != null;
                    }
                })
                //实现订阅
                .subscribe(
                        //创建观察者，作为事件传递的终点处理事件    
                        new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                Log.d("Fun2", "结束观察...\n");
                            }

                            @Override
                            public void onError(Throwable e) {
                                //出现错误会调用这个方法
                            }

                            @Override
                            public void onNext(String s) {
                                //处理事件
                                Log.d("Fun2", "handle this---" + s);
                            }
                        }
                );

    }

}
