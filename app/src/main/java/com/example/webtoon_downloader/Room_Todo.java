package com.example.webtoon_downloader;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Room_Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String link;
    private String ThumbnailLink;
    private String EpisodeLink;


}
