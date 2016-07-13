package com.example.administrator.mynewstest01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.List;

/**
 * Created by Administrator on 2016/6/5.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    private int Respurceid;
    private ImageView news_image;
    private TextView news_title;
    public ImageLoader imageLoader = ImageLoader.getInstance();

    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        Respurceid = resource;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(Respurceid, null);
        news_image = (ImageView) view.findViewById(R.id.news_image);
        news_title = (TextView) view.findViewById(R.id.news_title);


        //加载图片
        String str = news.getImage();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .build();

        ImageLoader.getInstance().displayImage(str, news_image, options);

        news_title.setText(news.getTitle());
        return view;
    }
}
