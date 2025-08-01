package com.myapp.webtoon_downloader;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Room_Data {
    public String title;//만화제목
    public String ThumbnailLink;//섬네일 링크
    public String EpisodeLink;//회차 링크
    public String day;//요일
    public String update;
    public boolean bookmark;
    @PrimaryKey(autoGenerate = true)
    public int id;
}
