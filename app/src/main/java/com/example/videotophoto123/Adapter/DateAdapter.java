package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.EditImage;
import com.example.videotophoto123.MainGallery;
import com.example.videotophoto123.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder>  implements GalleryImageAdapter.Callback {
    List<File> fileArrayList;
    List<String> dateList ;
    Context context;
    boolean longClick = false;
    RecyclerView recyclerView;
//    Callback callback;
    ArrayList<File> arrayFile ;
    GalleryImageAdapter.Callback callbackImage;
    int layoutId ;
    File file;

    public DateAdapter(List<String> dateList, Context context, GalleryImageAdapter.Callback callbackImage) {
        this.dateList = dateList;
        this.context = context;
//        this.callback = callback;
        this.callbackImage = callbackImage;
    }


//    public DateAdapter(List<File> fileArrayList, List<String> dateList, Context context, Callback callback, ArrayList<File> arrayFile, GalleryImageAdapter.Callback callbackImage) {
//        this.fileArrayList = fileArrayList;
//        this.dateList = dateList;
//        this.context = context;
//        this.callback = callback;
//        this.arrayFile = arrayFile;
//        this.callbackImage = callbackImage;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        arrayFile  = new ArrayList<>();
        int layoutId = R.layout.item_image_gallery;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterToViews(view, position);
    }
    private void inflaterToViews(View view, int position){
        TextView textView = view.findViewById(R.id.txtDate);
        textView.setText(dateList.get(position));
        recyclerView = view.findViewById(R.id.idRecycleCover);
         file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Images");
        if(!file.exists()){
            file.mkdirs();
        }
        arrayFile.clear();
//        arrayFile= getVideoByPath(file, dateList.get(position));
        for (File item : MainGallery.allImages){
            if (getDate(item.getAbsolutePath()).equals(dateList.get(position))){
                arrayFile.add(item);
            }
        }
        Collections.sort(arrayFile, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return t1.compareTo(file);
            }
        });
        textView.setText(dateList.get(position)+ " ( "+ arrayFile.size()+" ) images");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        GalleryImageAdapter adapter = new GalleryImageAdapter(arrayFile, context, callbackImage);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        new readDateTest().execute(dateList.get(position));
    }
    private  void  setupRecycler(ArrayList<File> files){

    }
//    ArrayList<File> getVideoByPath(File file, String date){
//        ArrayList<File> temp =new ArrayList<>();
//        File[]  files = file.listFiles();
//        for(File f :files){
//            if(f.getName().endsWith(".jpg") || f.getName().endsWith(".png")){
//              if (getDate(f.getAbsolutePath()).equals(date)){
//                  temp.add(f);
//              }
//            }
//        }
//        return temp;
//    }
//
//
    @Override
    public int getItemCount() {
        return dateList.size();
    }
    private  String   getDate(String path){
        File file = new File(path);
        Date lastModDate = new Date(file.lastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(lastModDate.getTime());
        return  date;
    }

    @Override
    public void onClickItem(String path) {

    }

    @Override
    public void onDisChoose(File file) {

    }

    @Override
    public void onChoose(File file) {

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }



}
