package com.example.newsapp.dbase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "source")
public class Source {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id1")
    @Expose(serialize = false, deserialize = false)
    public long idSource;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    @ColumnInfo(name = "category")
    @SerializedName("category")
    private String category;

    @ColumnInfo(name = "language")
    @SerializedName("language")
    private String language;

    @ColumnInfo(name = "country")
    @SerializedName("country")
    private String country;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", name = "+name+",description = "+description+",url="+url+",category="+category+",language = "+language+",country = "+ country+"]";
    }
}