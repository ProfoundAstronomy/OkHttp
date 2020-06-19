package com.android.study.okhttpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button btn_get_syn;

    private Button btn_get_asy;

    private Button btn_post_syn;

    private Button btn_post_asy;

    //Log日志 TAG
    private static final String TAG = "MainActivity";

    //URL访问地址
    private static final String URL = "http://wwww.baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化UI
        initUI();

        // 同步发送get请求
        getBySynchronized();

        // 异步发送get请求
        getByAsynchronized();

        // 同步发送post请求
        postBySynchronized();

        // 异步发送post请求
        postByAsynchronized();

    }




    /**
     * 初始化控件UI
     */
    private void initUI() {
        btn_get_syn = findViewById(R.id.btn_get_syn);
        btn_get_asy = findViewById(R.id.btn_get_asy);
        btn_post_syn = findViewById(R.id.btn_post_syn);
        btn_post_asy = findViewById(R.id.btn_post_asy);
    }

    /**
     * 同步发送get请求
     */

    private void getBySynchronized() {
        btn_get_syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、构造Client对象
                OkHttpClient client = new OkHttpClient();
                //2、采用构造者模式和链式调用构建Request对象
                final Request request = new Request.Builder()
                        .url(URL)//请求的URL
                        .get()//默认get请求
                        .build();
                //3、通过1和2产生的Client和Request对象生成Call对象
                final Call call = client.newCall(request);
                //4、同步发送get请求需要使用execute()方法，为了防止线程阻塞，要在子线程中运行
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //5、构建response对象  响应
                        try {
                            Response response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    /**
     * 异步发送get请求
     */
    private void getByAsynchronized() {
        btn_get_asy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、构建Client对象
                OkHttpClient client = new OkHttpClient();
                //2、采用构造者模式和链式调用构建Request对象
                final Request request = new Request.Builder()
                        .url(URL)
                        .get()
                        .build();
                //3、通过1和2产生的Client和Request对象生成Call对象
                final Call call = client.newCall(request);
                //4、调用Call对象的enqueue()方法，实现一个回调实现类
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d(TAG, "异步发送get请求失败！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d(TAG, "异步发送get请求成功！请求到的信息为：" + response.body().string());
                    }
                });
            }
        });

    }


    /**
     * 同步发送post请求
     */
    private void postBySynchronized() {
        btn_post_syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、构建Client对象
                OkHttpClient client = new OkHttpClient();
                //2、采用【建造者】模式和链式调用构建键值对对象
                FormBody formBody = new FormBody.Builder()
                        .add("username","quanchi")
                        .add("password","11111111")
                        .build();
                //3、采用【建造者】模式和链式调用构建Request对象
                final  Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)  //post  ,get就为get请求
                        .build();
                //4、通过1和3产生的Client和Request对象生成Call对象
                final Call call = client.newCall(request);
                //5、同步发送post请求，使用execute()方法
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                     //6、构建request对象
                        try {
                            Response response = call.execute();
                            Log.d(TAG, "同步发送post请求成功！请求到的信息为：" + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });


    }
    /**
     * 异步发送post请求
     */
    private void postByAsynchronized() {
        btn_post_asy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.构建Client对象
                OkHttpClient client = new OkHttpClient();
                // 2.采用建造者模式和链式调用构建键值对对象
                FormBody formBody = new FormBody.Builder()
                        .add("username","quanchi")
                        .add("password","11111111")
                        .build();
                // 3.采用建造者模式和链式调用构建Request对象
                final Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                // 4.通过1和3产生的Client和Request对象生成Call对象
                final Call call = client.newCall(request);
                // 5.调用Call对象的enqueue()方法，并且实现一个回调实现类
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d(TAG, "异步发送post请求失败！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d(TAG,"异步发送post请求成功！请求到的信息为："+response.body().string());
                    }
                });
            }
        });
    }
}
