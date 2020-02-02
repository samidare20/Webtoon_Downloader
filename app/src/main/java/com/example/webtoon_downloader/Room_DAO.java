package com.example.webtoon_downloader;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface Room_DAO {
    @Insert
    void insert(Room_Todo data);

    @Update
    void update(Room_Todo data);

    @Delete
    void delete(Room_Todo data);


}
