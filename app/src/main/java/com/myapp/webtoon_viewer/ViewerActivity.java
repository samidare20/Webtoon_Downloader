package com.myapp.webtoon_viewer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.webtoon_downloader.R;

public class ViewerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_main);

    }

}
