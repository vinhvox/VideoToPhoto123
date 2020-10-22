package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.videotophoto123.Adapter.GalleryImageAdapter;
import com.example.videotophoto123.Adapter.ImagesSelectAdapter;
import com.example.videotophoto123.Adapter.ImagesSlideShowAdapter;
import com.example.videotophoto123.Adapter.ShowFolderImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class SlideShows extends AppCompatActivity implements ShowFolderImageAdapter.Callback , ImagesSlideShowAdapter.Callback, ImagesSelectAdapter.CallBack {
    List<File> dir = new ArrayList<>();
    List<File> listSelectImages  = new ArrayList<>();
    Button btnClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_shows);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViews();
    }

    private void setupViews() {
        btnClear = findViewById(R.id.btnClear);
        RecyclerView recyclerViewShowFolderImage = findViewById(R.id.recyclerViewShowFolderImage);
        RecyclerView recyclerViewShowImage = findViewById(R.id.recyclerViewShowImage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SlideShows.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewShowFolderImage.setLayoutManager(linearLayoutManager);

        ArrayList<File> myVideo = ListDir(Environment.getExternalStorageDirectory());
        List<File> files = new ArrayList<>();
        for (File element : dir){
            if (!files.contains(element)){
                files.add(element);
            }
        }
        ShowFolderImageAdapter adapter = new ShowFolderImageAdapter(files, this, this);
        recyclerViewShowFolderImage.setAdapter(adapter);

    }
    public ArrayList<File> ListDir(File f) {
        ArrayList<File> temp = new ArrayList<>();
        File[] listFiles = f.listFiles();
        for (File file :listFiles) {
            if (file.isDirectory()) {
                temp.addAll(ListDir(file));
            }
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                dir.add(new File(file.getParent()));
                temp.add(file);
            }
        }
        return temp;
    }

    @Override
    public void onClick(File file) {
        setupRecyclerViewImages(file);
    }
    private  void  setupRecyclerViewImages(File file){
        ArrayList<File> files = new ArrayList<>();
        files = readData(file);
        RecyclerView recyclerViewShowImage = findViewById(R.id.recyclerViewShowImage);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerViewShowImage.setLayoutManager(gridLayoutManager);
        ImagesSlideShowAdapter adapter = new ImagesSlideShowAdapter(files, this, this);
        recyclerViewShowImage.setAdapter(adapter);
    }
    private ArrayList<File> readData(File file) {
        ArrayList<File> temp = new ArrayList<>();
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) {

                    temp.add(f);

            }
        }
        return temp;
    }

    @Override
    public void inSelectImages(File file) {
        listSelectImages.add(file);
        setupRecyclerViewImagesSelect(listSelectImages);
        eventBtnClear(listSelectImages);
    }

    private void eventBtnClear(List<File> file) {
        if (file.size()>0){
            btnClear.setVisibility(View.VISIBLE);
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listSelectImages.clear();
                    setupRecyclerViewImagesSelect(listSelectImages);
                    eventBtnClear(listSelectImages);
                }
            });
        }
        else {
            btnClear.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerViewImagesSelect(List<File> listSelectImages) {

        RecyclerView recyclerView = findViewById(R.id.recyclerViewImagesSelect);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getLayoutManager().scrollToPosition(listSelectImages.size() -1);
        ImagesSelectAdapter adapter = new ImagesSelectAdapter(listSelectImages, this, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickItem() {

    }

    @Override
    public void onDeleteItem(File file) {
        listSelectImages.remove(file);
        setupRecyclerViewImagesSelect(listSelectImages);
        eventBtnClear(listSelectImages);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_edit_image, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menuSaveImage:
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    public String softwareMakeVideo(List<File> images,
//                                    String name) {
//        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Videos");
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        File file = new File(directory, name + ".mp4");
//        try {
//            Sequen encoder = new SequenceEncoder(file);
//            for (Iterator<File> iterator = images.iterator(); iterator.hasNext(); ) {
//                File image = iterator.next();
//                if (!image.exists() || image.length() == 0) {
//                    continue;
//                }
//                Bitmap frame = BitmapFactory.decodeFile(image.getAbsolutePath());
//                try {
//                    encoder.encodeNativeFrame(this.fromBitmap(frame));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            encoder.finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file.getAbsolutePath();
//    }

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
}