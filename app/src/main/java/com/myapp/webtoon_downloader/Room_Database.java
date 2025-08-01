package com.myapp.webtoon_downloader;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Room_Data.class}, version = 1)
public abstract class Room_Database extends RoomDatabase {
    private static Room_Database DB;

    public static Room_Database getInstance(Context context) {
        if (DB == null) {
            DB = Room.databaseBuilder(context, Room_Database.class, "Database")
                    .build();
        }
        return DB;
    }

    public abstract Room_DAO Room_DAO();
}
