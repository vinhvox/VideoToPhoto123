package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoToPhoto;

import java.io.File;
import java.util.List;

public class ListVideoAdapter extends BaseAdapter {
    List<File> videoList;
    Context mContext;
    Callback callback ;

    public ListVideoAdapter(List<File> videoList, Context mContext, Callback callback) {
        this.videoList = videoList;
        this.mContext = mContext;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return videoList.isEmpty()?0:videoList.size();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_list,parent,false);
        final VideoView vdThumpnail=view.findViewById(R.id.videoViewPlay);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        final TextView txtTime = view.findViewById(R.id.txtVideoTime);
        txtTitle.setText(videoList.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = videoList.get(position).getAbsolutePath();
               callback.onClickItem(path);

            }
        });
        Uri uri = Uri.parse(videoList.get(position).getAbsolutePath());
        vdThumpnail.setVideoURI(uri);
        vdThumpnail.seekTo(100);
        vdThumpnail.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                txtTime.setText(MilliSecondsToTimer(vdThumpnail.getDuration()));
            }
        });

        return view;
    }
    public  interface  Callback{
        void  onClickItem(String path);
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
}
