package com.example.meizigirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    //数据集合
    private List<ResultBean.ResultsBean> mListData =new ArrayList<ResultBean.ResultsBean>();

    private Gson mGson =new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mListView = (ListView) findViewById(R.id.list_view);
        ButterKnife.bind(this); //绑定Activity
        mListView.setAdapter(mBaseAdapter);
        //sendSyncRequest();
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

           /**
            *
            * 在子线程被调用
            */
           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String result = response.body().string();
               ResultBean resultBean = mGson.fromJson(result, ResultBean.class);
               //Log.d(TAG, "onResponse: "+resultBean.getResults().get(0).getUrl());

               //将网络结果加入数据集合
               mListData.addAll(resultBean.getResults());
               //在主线程刷新
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       //通知Adapter刷新列表
                       mBaseAdapter.notifyDataSetChanged();
                   }
               });

           }
       });
        Log.d(TAG, "sendAsyncRequest: ");
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

    private BaseAdapter mBaseAdapter =new BaseAdapter() {
        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            //当没有可回收利用的item
            if (convertView ==null){
                convertView =View.inflate(MainActivity.this,R.layout.view_list_item,null);
                viewHolder =new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //绑定视图
            ResultBean.ResultsBean resultsBean = mListData.get(position);//拿到对应位置的数据
            //更新发布时间
            viewHolder.mTextView.setText(resultsBean.getPublishedAt());
            return convertView;
        }
    };

    public class  ViewHolder{
        ImageView mImageView;
        TextView mTextView;

        public ViewHolder(View root){
            mImageView = (ImageView) root.findViewById(R.id.image);
            mTextView = (TextView) root.findViewById(R.id.publish_time);
        }
    }
}
