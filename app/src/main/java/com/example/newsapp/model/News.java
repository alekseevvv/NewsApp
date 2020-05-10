package com.example.newsapp.model;

import com.example.newsapp.dbase.Articles;
import com.example.newsapp.dbase.Source;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("sources")
    @Expose
    private List<Source> sources;

    @SerializedName("articles")
    @Expose
    private List<Articles> article;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSource() {
        return sources;
    }
    public List<Articles> getArticle() {
        return article;
    }

    public void setArticle(List<Articles> article) {
        this.article = article;
    }

    public void setSource(List<Source> sources) {
        this.sources = sources;
    }
}