package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;

import com.example.videotophoto123.Adapter.FragmentVideoPhotoAdapter;
import com.example.videotophoto123.Fragment.QuickCapture;
import com.example.videotophoto123.Fragment.TimeCapture;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class VideoPhoto extends AppCompatActivity {
  public   static String path;
    ViewPager viewPager;
    TabLayout tabLayout;
     public  static  Thread threadTime, threadQuick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        path = getIntent().getStringExtra("PATH_VIDEO");
        setupView();
    }

    private void setupView() {
        viewPager = findViewById(R.id.viewpagerVideoPhoto);
        viewPager.setAdapter(new FragmentVideoPhotoAdapter(getSupportFragmentManager(), this));
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              switch (position){
                  case 0:
                      TimeCapture.videoViewTime.pause();
                      break;
                  case 1:
                      QuickCapture.videoViewQick.pause();
                      QuickCapture.thread.interrupt();
                      QuickCapture.imagePlayVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                      QuickCapture.imagePlayVideo.setVisibility(View.VISIBLE);
                      break;
              }
          }
          @Override
          public void onPageSelected(int position) {

          }

          @Override
          public void onPageScrollStateChanged(int state) {

          }
      });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                QuickCapture.thread.interrupt();
                TimeCapture.videoViewTime.stopPlayback();
                QuickCapture.videoViewQick.stopPlayback();
                File file = new File(path);
                Intent intent = new Intent(VideoPhoto.this, VideoList.class);
                intent.putExtra("PATH", file.getParent());
                finish();
               startActivity(intent);
               break;
        }
        return super.onOptionsItemSelected(item);
    }
}