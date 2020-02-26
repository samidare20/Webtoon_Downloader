package com.myapp.webtoon_viewer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
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
    String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String nowPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    ArrayList<imageItems> filelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_activity_main);
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("웹툰 뷰어");
        setSupportActionBar(tb);
        makelist();
        setDrawer();
    }

    private void makelist() {
        getFileList();
        LinearLayout field = findViewById(R.id.fileField);
        for (imageItems i : filelist) {

            if (i.getNumber() != -1) {
                new Thread(() -> {
                    makeImagewindow(field);
                }).start();
                break;
            } else {
                fileTiles t = new fileTiles(this);
                t.setData(i.getPath());
                t.setOnClickListener(v -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    field.removeAllViews();
                    nowPath = i.getPath();
                    filelist.clear();
                    makelist();
                });
                field.addView(t);
            }
        }
    }

    private void makeImagewindow(LinearLayout field) {
        for (imageItems i : filelist) {
            if (i.getNumber() != -1) {
                ImageView image = new ImageView(this);
                File file = new File(i.getPath());
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                image.setImageBitmap(bm);
                runOnUiThread(() -> {
                    field.addView(image);
                });
            }
        }
    }

    private void getFileList() {
        File fileList = new File(nowPath);
        File[] files = fileList.listFiles();

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
        if (!rootPath.equals(nowPath)) {
            nowPath = nowPath.substring(0, nowPath.lastIndexOf("/"));
            LinearLayout field = findViewById(R.id.fileField);
            field.removeAllViews();
            filelist.clear();
            makelist();
        } else
            super.onBackPressed();
    }
}
