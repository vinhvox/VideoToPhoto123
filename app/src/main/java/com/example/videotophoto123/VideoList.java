package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.videotophoto123.Adapter.ListVideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoList extends AppCompatActivity implements ListVideoAdapter.Callback {
    private static final int REQUEST_BACK = 1000 ;
    List<File> videoFile = new ArrayList<>();
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        path = getIntent().getStringExtra("PATH");
        GridView gvListView = findViewById(R.id.gridViewVideolist);
        File file = new File(path);
        file = new File(file.getPath());
        videoFile = getVideoByPath(file);
        gvListView.setAdapter(new ListVideoAdapter(videoFile, VideoList.this, this));
    }
    ArrayList<File> getVideoByPath(File file){
        ArrayList<File> temp =new ArrayList<>();
        File[]  files = file.listFiles();
        for(File f :files){
            if(f.getName().endsWith(".mp4")){
                temp.add(f);
            }
        }
        return temp;
    }
    @Override
    public void onClickItem(String path) {
        Intent intent = new Intent(VideoList.this, VideoPhoto.class);
        intent.putExtra("PATH_VIDEO",path);
        startActivityForResult(intent, REQUEST_BACK);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}