package com.myapp.webtoon_viewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.webtoon_downloader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class imageViewer extends AppCompatActivity {
    String path = "";
    Context mcontext = this;
    Display display;
    Point size = new Point();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_image_viewer);
        Intent intent = getIntent();
        path = intent.getStringExtra("path") + "/";
        display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        Thread a=new Thread(() -> {
            makefield();
        });
        a.start();
        setScorllview();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setScorllview(){
        ScrollView scrollview=findViewById(R.id.scrollView);
        LinearLayout field=findViewById(R.id.imageField);
        int bottomY=field.getBottom();
        Log.d("mdg",Integer.toString(bottomY));
        scrollview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            Log.d("mdg",Integer.toString(scrollY));
            if(scrollY==bottomY)
                Log.d("mdg","it's bottom! it's bottom!  it's bottom! it's bottom! it's bottom! it's bottom!");
            else
                setScorllview();
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void makefield() {
        File fileList = new File(path);
        File[] files = fileList.listFiles();
        ArrayList<imageItems> filelist = new ArrayList<>();
        if (files != null) {
            for (File i : files) {
                imageItems item;
                item = new imageItems(i.getPath(), true);
                filelist.add(item);
            }
        }
        Collections.sort(filelist);
        runOnUiThread(() -> {
            LinearLayout field = findViewById(R.id.imageField);
            for (imageItems i : filelist) {
                ImageView image = new ImageView(this);
                File file = new File(i.getPath());
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                image.setImageBitmap(bm);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    image.setAdjustViewBounds(true);
                }
                field.addView(image);
                image.getLayoutParams().width = size.x;
            }
            Log.d("mdg","activity complete");
        });

    }
}
