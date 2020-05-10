package com.example.newsapp.api;


import com.example.newsapp.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("sources")
    Call<News> getNews(
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<News> getNewsArt(
            @Query("apiKey") String apiKey,
            @Query("sources") String keyword
    );

    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") CharSequence keyword,
            @Query("apiKey") String apiKey
    );
}