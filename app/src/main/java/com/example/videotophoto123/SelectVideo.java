package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.videotophoto123.Adapter.ListFolderAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectVideo extends AppCompatActivity implements ListFolderAdapter.Callback {
    List<String> listFile = new ArrayList<String>();
    List<File> dir = new ArrayList<>();
    ListFolderAdapter adapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = findViewById(R.id.gridView);
        dir.clear();
        //Duyệt danh sách file trong bộ nhớ
        ArrayList<File> myVideo = ListDir(Environment.getExternalStorageDirectory());
        for (int i=0;i<dir.size()-1;i++){
            for (int j=1;j<dir.size();j++){
                if (dir.get(i).getName().equals(dir.get(j).getName())){
                    dir.remove(j);
                }
            }
        }
        adapter = new ListFolderAdapter(dir, this, this);
        gridView.setAdapter(adapter);
    }
    public ArrayList<File> ListDir(File f) {

        ArrayList<File> temp = new ArrayList<>();
        File[] listFiles = f.listFiles();
        for (File file :listFiles) {
            if (file.isDirectory()) {
                temp.addAll(ListDir(file));
            }
            if (file.getName().endsWith(".mp4")) {
                dir.add(new File(file.getParent()));
                temp.add(file);
            }
        }
        return temp;
    }

    @Override
    public void onClickItem(String path) {
                Intent intent = new Intent(this, VideoList.class);
                intent.putExtra("PATH", path);
                savePathVideo(path);
                startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void  savePathVideo(String path){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pathVideo", path);
        editor.commit();
    }
}
