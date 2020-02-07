package com.example.webtoon_downloader;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Room_DAO {
    @Insert
    void insert(Room_Data data);

    @Delete
    void delete(Room_Data data);

    @Query("SELECT * FROM Room_Data")
    List<Room_Data> getAll();

    @Query("SELECT * FROM Room_Data WHERE title=:title")
    Room_Data selectTitle(String title);

    @Query("SELECT * FROM Room_Data WHERE day = :day")
    List<Room_Data> selectDay(String day);

    @Query("DELETE FROM Room_Data")
    void deleteAll();


}
