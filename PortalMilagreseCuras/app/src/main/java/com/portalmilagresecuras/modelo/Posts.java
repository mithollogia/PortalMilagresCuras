package com.portalmilagresecuras.modelo;

import com.google.gson.annotations.SerializedName;

public class Posts {
    @SerializedName("id")
    private int id;

    @SerializedName("post_url")
    private String url;

    @SerializedName("title")
    private String title;

    @SerializedName("thumbnail")
    private String urlToImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
