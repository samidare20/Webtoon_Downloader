package com.myapp.webtoon_viewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.myapp.bookmark.Bookmark;
import com.myapp.webtoon_downloader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class ViewerActivity extends AppCompatActivity {

    String rootPath;
    String nowPath;
    int where = 0;
    Boolean nowWatching = false;
    ArrayList<imageItems> filelist = new ArrayList<>();
    LinearLayout field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_activity_main);
        Toolbar tb = findViewById(R.id.toolbar);
        rootPath = getFilesDir().toString();
        nowPath = rootPath;
        tb.setTitle("웹툰 뷰어");
        setSupportActionBar(tb);
        makelist();
        setDrawer();
    }

    private void startImageViewer(String path) {
        Intent intent = new Intent(this, imageViewer.class);
        intent.putExtra("path", path);
        new Thread(() -> {
            nowWatching = true;
            startActivity(intent);

        }).start();
    }

    private void makelist() {
        getFileList();
        ArrayList<fileTiles> makinglist = new ArrayList<>();
        field = findViewById(R.id.fileField);
        for (imageItems i : filelist) {
            fileTiles t = new fileTiles(this);
            t.setData(i.getPath());
            t.setOnClickListener(v -> {
                where++;
                if (where == 2) {
                    where = 1;
                    startImageViewer(i.getPath());
                    filelist.clear();
                    return;
                }
                nowPath = i.getPath();
                filelist.clear();
                makelist();
            });
            makinglist.add(t);
        }
        field.removeAllViews();
        for (fileTiles i : makinglist) {
            field.addView(i);
        }
    }


    private void getFileList() {
        File thisfileList = new File(nowPath);
        File[] files = thisfileList.listFiles();

        if (files != null) {
            for (File i : files) {
                String t = String.valueOf(i);
                if ('.' != (t.charAt(t.lastIndexOf("/") + 1))) {
                    imageItems item;
                    if (i.getPath().contains("jpg"))
                        item = new imageItems(i.getPath(), true);
                    else
                        item = new imageItems(i.getPath(), false);
                    filelist.add(item);
                    Log.d("mdg",i.getPath());
                }
            }
        }
        Collections.sort(filelist);
    }

    void setDrawer() {
        NavigationView navi = findViewById(R.id.navi);

        navi.setNavigationItemSelectedListener(item -> {
            DrawerLayout drawer = findViewById(R.id.drawer);
            drawer.closeDrawer(GravityCompat.START);
            if (item.getItemId() == R.id.downloader) {
                finish();
            } else if (item.getItemId() == R.id.bookmarklist) {
                Intent intent = new Intent(this, Bookmark.class);
                startActivity(intent);
                finish();
            }

            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (rootPath.equals(nowPath))
            return;
        if (where == 0)
            return;
        where--;
        nowPath = nowPath.substring(0, nowPath.lastIndexOf("/"));
        LinearLayout field = findViewById(R.id.fileField);
        field.removeAllViews();
        filelist.clear();
        makelist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nowWatching) {
            makelist();
            nowWatching = false;
        }
    }
}
