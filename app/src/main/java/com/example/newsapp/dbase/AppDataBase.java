package com.example.newsapp.dbase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Source.class, Articles.class}, version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract ArticlesDao articlesDao();
    public abstract SourceDao sourceDao();
}