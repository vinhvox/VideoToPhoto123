package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GalleryVideoAdapter extends RecyclerView.Adapter<GalleryVideoAdapter.ViewHolder> {
    List<File> fileList;
    Context context;
    Callback callback;

    public GalleryVideoAdapter(List<File> fileList, Context context, Callback callback) {
        this.fileList = fileList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_test, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflateToViews(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(fileList.get(position));
            }
        });


    }
    private  void inflateToViews(View view, int position){
        VideoView videoView = view.findViewById(R.id.videoViewItemGallery);
        TextView txtTimeItemGallery = view.findViewById(R.id.txtTimeItemGallery);
        TextView txtNameItemGallery = view.findViewById(R.id.txtNameItemGallery);
        TextView txtCapacityItemGallery = view.findViewById(R.id.txtCapacityItemGallery);
        TextView txtDateItemGallery = view.findViewById(R.id.txtDateItemGallery);
        videoView.setVideoURI(Uri.fromFile(fileList.get(position)));
        videoView.seekTo(100);
        getVideoName(fileList.get(position), txtNameItemGallery);
       videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
           @Override
           public void onPrepared(MediaPlayer mediaPlayer) {
               txtTimeItemGallery.setText(MilliSecondsToTimer(mediaPlayer.getDuration()));
           }
       });
       txtCapacityItemGallery.setText(String.valueOf( fileList.get(position).length()/(1000*1000))+ "MB");
       txtDateItemGallery.setText(getDate(fileList.get(position)));
    }
    @Override
    public int getItemCount() {
        return fileList.size();
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
    private  void  getVideoName(File file, TextView textView){
        String[] videoName =  file.getName().split(".mp4");
        textView.setText(videoName[0]);
    }
    private  String   getDate(File file){
        Date lastModDate = new Date(file.lastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(lastModDate.getTime());
        Log.e("TAG", simpleDateFormat.format(lastModDate.getTime())+"");
        return  date;
    }
    private  String MilliSecondsToTimer(long millSec){
        String finalTimerString = "";
        String hoursString = "";
        String secondString;
        String minuteString;
//        int seconds = (int) millSec % 60;
//        int minutes = (int) millSec / 60;
//        int hours = (int) millSec / (60 * 100);
        int seconds = (int) (millSec / 1000) % 60 ;
        int minutes = (int) ((millSec / (1000*60)) % 60);
        int hours   = (int) ((millSec / (1000*60*60)) % 24);
        if (hours > 0) {
            hoursString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else secondString = "" + seconds;
        if (minutes < 10) {
            minuteString = "0" + minutes;
        } else minuteString = "" + minutes;
        finalTimerString = hoursString + minuteString + ":" + secondString;
        return finalTimerString;
    }
    public  interface  Callback{
        void onClick(File path);
    }
}
