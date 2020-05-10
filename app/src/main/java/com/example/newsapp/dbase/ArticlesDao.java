package com.example.newsapp.dbase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ArticlesDao {

    @Query("SELECT * FROM articles")
    Flowable<List<Articles>> getall();

    @Query("SELECT * FROM articles WHERE url = :Url")
    Single<Articles> getArticleByUrl(String Url);

    @Insert
    void insertAll(Articles... articles);

    @Delete
    void delete(Articles articles);
}
