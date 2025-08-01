package com.myapp.webtoon_downloader;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Room_DAO {
    @Insert
    void insert(Room_Data data);

    @Query("SELECT * FROM Room_Data")
    List<Room_Data> getAll();

    @Query("SELECT * FROM Room_Data WHERE title=:title")
    Room_Data selectTitle(String title);

    @Query("SELECT * FROM Room_Data WHERE day = :day")
    List<Room_Data> selectDay(String day);

    @Query("SELECT * FROM Room_Data WHERE bookmark")
    List<Room_Data> selectBookmark();

    @Query("SELECT * FROM Room_Data WHERE id=:id")
    Room_Data selectId(int id);

    @Update
    void update(Room_Data data);
}
