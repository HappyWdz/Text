package com.example.meizigirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.list_view)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mListView = (ListView) findViewById(R.id.list_view);
        ButterKnife.bind(this); //绑定Activity
        sendSyncRequest();
        sendAsyncRequest();
    }

    private void sendAsyncRequest() {
        OkHttpClient okHttpClient =new OkHttpClient();
        //创建一个网络请求
        String url ="http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
        Request request =new Request.Builder().get().url(url).build();
       okHttpClient.newCall(request).enqueue(new Callback() {
           //异步请求 ,不需要等到网络结果返回,就执行后面的代码,okhttply内部会在子线程执行网络请求,返回结果
           @Override
           public void onFailure(Call call, IOException e) {

           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String result = response.body().string();
               Log.d(TAG, "sendAsyncRequest: "+result);
           }
       });

    }

    private void sendSyncRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient =new OkHttpClient();
                //创建一个网络请求
                String url ="http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
                Request request =new Request.Builder().get().url(url).build();
                try {
                    //同步请求
                    Response response = okHttpClient.newCall(request).execute();
                    //同步请求,要等到网络请求返回回来之后,你才能走后面的代码
                    //string方法只能调用一次
                    Log.d(TAG, "sendSyncRequest: "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
