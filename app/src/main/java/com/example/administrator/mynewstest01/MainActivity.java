package com.example.administrator.mynewstest01;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int SHOWRESPONSE = 0;
    private String content_id = null;
    private String title = null;
    private String imageurl = null;

    private String urls = "http://news-at.zhihu.com/api/4/news/";//请求接口地址
    private List<News> newslist = new ArrayList<News>();
    NewsAdapter adapter;
    URL urlb = null, urla = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            urla = new URL(urls + "latest");
            sendRequstWithHttpURLConnection(urla);

            adapter = new NewsAdapter(MainActivity.this, R.layout.news_layout, newslist);
            ListView listView = (ListView) findViewById(R.id.news_list);
            //设置listview的点击事件
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        urlb = new URL(urls+newslist.get(position).getId());
                        Intent intent = new Intent(MainActivity.this, NewsContent.class);
                        intent.putExtra("content_id", newslist.get(position).getId());
                        intent.putExtra("content_title", newslist.get(position).getTitle());
                        startActivity(intent);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWRESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject jsonobject = new JSONObject(response);
                        JSONArray numblist = jsonobject.getJSONArray("stories");
                        for (int i = 0; i < numblist.length(); i++) {
                            title = numblist.getJSONObject(i).getString("title");
                            content_id = numblist.getJSONObject(i).getString("id");
                            JSONArray jsonimage = numblist.getJSONObject(i).getJSONArray("images");

                            for (int j = 0; j < jsonimage.length(); j++) {
                                imageurl = jsonimage.getString(j);
                            }
                            News news = new News(title, imageurl,content_id);
                            newslist.add(news);//将新闻内容加入到list队列中
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    private void sendRequstWithHttpURLConnection(final URL url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    InputStream in = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    //message 的唯一标识
                    message.what = SHOWRESPONSE;
                    //将服务器返回的结果放到messa中
                    message.obj = response.toString();
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
