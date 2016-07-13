package com.example.administrator.mynewstest01;

/**
 * Created by Administrator on 2016/6/5.
 */
public class News {
    private String title;
    private String content;
    private String image;
    private String id;

    public News(String title,String image,String id) {
        this.title = title;
        this.image= image;
        this.id = id;
    }
    public News(String title,String image) {
        this.title = title;
        this.image= image;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
