package com.example.webtoon_downloader;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Room_DAO {
    @Insert
    void insert(Room_Todo data);

    @Delete
    void delete(Room_Todo data);

    @Query("SELECT * FROM Room_Todo")
    List<Room_Todo> getAll();

    @Query("SELECT * FROM Room_Todo WHERE title=:title")
    Room_Todo selectTitle(String title);

    @Query("SELECT * FROM room_todo WHERE day=:day")
    List<Room_Todo> selectDay(String day);


}
