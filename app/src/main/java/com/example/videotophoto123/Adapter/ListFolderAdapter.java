package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFolderAdapter extends BaseAdapter {
    List<File> listFile;
    Context mContext;
    Callback callback;

    public ListFolderAdapter(List<File> listFile, Context mContext, Callback callback) {
        this.listFile = listFile;
        this.mContext = mContext;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return listFile.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.folder_video,parent,false);
//        ImageView imgFolder = view.findViewById(R.id.imgFolder);
        TextView textView = view.findViewById(R.id.txtFolderName);
        String path = listFile.get(position).getAbsolutePath();
        File file = new File(path);
        file = new File(file.getPath());
        int count = countFiles(file);
        textView.setText(listFile.get(position).getName()+" ("+count+" videos)");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onClickItem(listFile.get(position).getAbsolutePath());

            }
        });
        return view;
    }

    private int countFiles(File file) {
        ArrayList<File> list= new ArrayList<>();
        File[] f=file.listFiles();
        for(File files:f){
            if(files.getName().endsWith(".mp4")){
                list.add(files);
            }
        }
        return list.size();
    }
    public  interface  Callback{
        void  onClickItem(String path);
    }
}
