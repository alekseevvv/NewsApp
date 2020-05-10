package com.example.newsapp;

import android.app.Application;

import androidx.room.Room;

import com.example.newsapp.dbase.AppDataBase;

public class App extends Application {
    private static App instance;
    private AppDataBase db;


    public static App getInstance() {
        return instance;
    }

    public AppDataBase getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(
                instance,
                AppDataBase.class,
                "news-database.db").build();
    }
}