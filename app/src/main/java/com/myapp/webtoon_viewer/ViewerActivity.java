package com.myapp.webtoon_viewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.webtoon_downloader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ViewerActivity extends AppCompatActivity {
    String rootPath= Environment.getExternalStorageDirectory().getAbsolutePath();
    String nowPath= Environment.getExternalStorageDirectory().getAbsolutePath();
    ArrayList<imageItems> filelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_activity_main);
        makelist();
}

    private void makelist() {
        getFileList();
        LinearLayout field = findViewById(R.id.fileField);
        for (imageItems i : filelist) {
            fileTiles t = new fileTiles(this);
            if(i.getNumber()!=-1){
                Log.d("mdg",i.getPath());
                ImageView image=new ImageView(this);
                File file=new File(i.getPath());
                Bitmap bm= BitmapFactory.decodeFile(file.getAbsolutePath());
                image.setImageBitmap(bm);
                field.addView(image);
            }
            else {
                t.setData(i.getPath());
                t.setOnClickListener(v -> {
                    field.removeAllViews();
                    nowPath = i.getPath();
                    filelist.clear();
                    makelist();
                });
                field.addView(t);
            }
        }
    }
    private void getFileList()  {
        File fileList= new File(nowPath);
        File[] files = fileList.listFiles();

        if (files != null) {
            for(File i : files) {
                String t=String.valueOf(i);
                if('.'!=(t.charAt(t.lastIndexOf("/")+1))) {
                    imageItems item;
                    if(i.getPath().contains("jpg"))
                        item = new imageItems(i.getPath(), true);
                    else
                        item = new imageItems(i.getPath(), false);
                    filelist.add(item);
                }
            }
        }
        Collections.sort(filelist);
    }
    @Override
    public void onBackPressed() {
        if(!rootPath.equals(nowPath)){
            nowPath=nowPath.substring(0,nowPath.lastIndexOf("/"));
            LinearLayout field = findViewById(R.id.fileField);
            field.removeAllViews();
            filelist.clear();
            makelist();
        }
        else
            super.onBackPressed();
    }
}
