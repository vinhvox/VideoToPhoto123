package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.videotophoto123.Adapter.DateAdapter;
import com.example.videotophoto123.Adapter.FragmentGalleryAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

public class MainGallery extends AppCompatActivity  {
public  static boolean chooseFile = false;
public  static  ArrayList<File> allImages = new ArrayList<>();
File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViews();
        new loadAllImages().execute();

    }
    private void setupViews() {
        ViewPager viewPager = findViewById(R.id.viewPagerGallery);
        TabLayout tabLayout = findViewById(R.id.tablayoutGallery);
        viewPager.setAdapter(new FragmentGalleryAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
               onBackPressed();
               finish();
               break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    public  class loadAllImages extends AsyncTask<Void, Void, ArrayList<File>>{

        @Override
        protected ArrayList<File> doInBackground(Void... voids) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Images");
            if(!file.exists()){
                file.mkdirs();
            }
            ArrayList<File> temp =new ArrayList<>();
            File[]  files = file.listFiles();
            for(File f :files){
                if(f.getName().endsWith(".jpg") || f.getName().endsWith(".png")){
                    temp.add(f);
                }
            }
            return temp;
        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            super.onPostExecute(files);
            allImages.clear();
            allImages.addAll(files);
            Log.e("CHECK", files.size()+" okkok");
        }
    }

}