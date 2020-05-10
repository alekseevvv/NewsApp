package com.example.newsapp.dbase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SourceDao {

    @Query("SELECT * FROM source")
    Flowable<List<Source>> getall();

    @Query("SELECT * FROM source WHERE id = :Id")
    Single<Source> getById(String Id);

    @Insert
    void insertAll(Source... source);

    @Delete
    void delete(Source source);
}
