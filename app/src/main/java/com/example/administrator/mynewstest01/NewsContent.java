package com.example.administrator.mynewstest01;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/6/8.
 */public class NewsContent extends AppCompatActivity {
    private TextView text_title, textView_content;
    private ImageView imageView;
    private String title, content, image,id;
    private static final int SHOWRESPONSE = 0;
    String urls = "http://news-at.zhihu.com/api/4/news/";
    URL url = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thecontent);

        try {
            text_title = (TextView) findViewById(R.id.the_title);
            textView_content = (TextView) findViewById(R.id.the_content);
            imageView = (ImageView) findViewById(R.id.the_image);

            Intent intent = getIntent();
            title = intent.getStringExtra("content_title");
            id =intent.getStringExtra("content_id");
            url = new URL(urls+id);
            sendRequstWithHttpURLConnection(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SHOWRESPONSE:
                    String responsea = (String) msg.obj;
                    try {
                        JSONObject jsonobject = new JSONObject(responsea);
                        String shtml = jsonobject.getString("body");
                        Document doc = Jsoup.parse(shtml);
                        content = doc.body().text();

                        image = jsonobject.getString("image");

                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisc(true)
                                .showImageForEmptyUri(R.mipmap.ic_launcher)
                                .build();
                        ImageLoader.getInstance().displayImage(image, imageView, options);

                        text_title.setText(title);
                        textView_content.setText(content);
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
