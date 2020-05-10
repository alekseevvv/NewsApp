package com.example.newsapp.dbase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "articles")
public class Articles {
    public Articles(Articles model) {
       this.author = model.getAuthor();
       this.description = model.getDescription();
       this.title = model.getTitle();
       this.url = model.getUrl();
       this.urlToImage = model.getUrlToImage();
       this.publishedAt = model.getPublishedAt();
       this.source = model.getSource();
        this.sourcename = model.getSourcenameOff();
    }
    public Articles(){

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose(serialize = false, deserialize = false)
    public long idArticle;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name="publishedAt")
    @SerializedName("publishedAt")
    private String publishedAt;

    @ColumnInfo(name="author")
    @SerializedName("author")
    private String author;

    @ColumnInfo(name="urlToImage")
    @SerializedName("urlToImage")
    private String urlToImage;

    @Ignore
    @ColumnInfo(name="source")
    @SerializedName("source")
    private Source source;

    @ColumnInfo(name="sourcename")
    @SerializedName("sourcename")
    private String sourcename;

    @ColumnInfo(name="description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name="url")
    @SerializedName("url")
    private String url;


    public String getPublishedAt ()    {
        return publishedAt;
    }

    public void setPublishedAt (String publishedAt)
    {
        this.publishedAt = publishedAt;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }


    public String getUrlToImage ()
    {
        return urlToImage;
    }

    public void setUrlToImage (String urlToImage)
    {
        this.urlToImage = urlToImage;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }


    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public Source getSource () {
        return source;
    }

    public void setSource (Source source) {
        this.source = source;
    }

    public String getSourcenameOff(){
        return sourcename;
    }
    public String getSourcename () {
        return source.getName();
    }

    public void setSourcename (String sourcename) {
        this.sourcename = sourcename;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [publishedAt = "+publishedAt+", author = "+author+", urlToImage = "+urlToImage+", title = "+title+",source = "+source+",sourcename = "+sourcename+",description = "+description+", url = "+url+"]";
    }

}

