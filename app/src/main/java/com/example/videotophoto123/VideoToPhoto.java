package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.videotophoto123.Adapter.RecyclerImageAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class VideoToPhoto extends AppCompatActivity implements RecyclerImageAdapter.Callback {
VideoView videoView;
TextView txtSnap , txtCurrentTime, txtEndTime;
    String path;
    ArrayList<Bitmap> captureImageList;
    ImageView imageEventVideo;
    SeekBar seekBar;
    MediaMetadataRetriever retriever;
    long timeInMilliSec, timeMill;
    boolean isThreadRunning = true;
    String endWiths, type, quality;
    RecyclerView recyclerView;
    RecyclerImageAdapter adapter;
    Thread thread;
    String snap, timeVideo;
    int currentTime;
    RangeSeekBar rangeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_to_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        readSharedReferent();
        retriever = new MediaMetadataRetriever();
        captureImageList = new ArrayList<>();
         path= getIntent().getStringExtra("PATH_VIDEO");
        setupViews();
        eventCheckRadio();
        rangeSeekBar();
      thread=   new Thread(new Runnable() {
            @Override
            public void run() {
                while (videoView != null){
                    if(videoView.isPlaying()) {
                        Message message = new Message();
                        message.arg1 = videoView.getCurrentPosition() ;
                        handle.sendMessage(message);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(!isThreadRunning){
                    return;
                }
            }

        });
      thread.start();
        changeSeekBar();

    }
    private  void getVideoName(){
        TextView txtVideoName = findViewById(R.id.txtVideoName);
        File file = new File(path);
        String[] name = file.getName().split(".mp4");
        txtVideoName.setText(name[0]);

    }
    private void eventCheckRadio() {
        RadioButton radioQuickCapture = findViewById(R.id.radioQuickCapture);
        RadioButton radioTimeCapture = findViewById(R.id.radioTimeCapture);
        radioQuickCapture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    txtSnap.setVisibility(View.GONE);
                    rangeView.setVisibility(View.GONE);
                    seekBar.setVisibility(View.VISIBLE);
                    videoView.pause();
                    imageEventVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }

            }
        });
        radioTimeCapture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    txtSnap.setVisibility(View.VISIBLE);
                    rangeView.setVisibility(View.VISIBLE);
                    videoView.pause();
                    imageEventVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
    }
});
        }
private void setupViews() {
        imageEventVideo = findViewById(R.id.imageEventVideo);
         seekBar = findViewById(R.id.idSeekBarVideo);
         txtCurrentTime= findViewById(R.id.txtCurrentTime);
          txtEndTime = findViewById(R.id.txtEndTime);
        txtSnap = findViewById(R.id.txtCaptureEvery);
        videoView = findViewById(R.id.videoViewPlay);
        videoView.setVideoURI(Uri.parse(path));
        rangeView = findViewById(R.id.rangeSeekBar);
        getVideoName();
        checkEventVideo();
    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            txtEndTime.setText(MiliSecondsToTimer(videoView.getDuration() / 1000));
            seekBar.setMax(videoView.getDuration());


        }
    });
  videoView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          checkEventVideo();
      }
  });
//        retriever.setDataSource(this, Uri.fromFile(new File(path)));
//        timeVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        seekBar.setMax(Integer.parseInt(timeVideo));
//        timeInMilliSec = Long.parseLong(timeVideo);
//        retriever.release();
    }
        private  Handler handle = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                txtCurrentTime.setText(MiliSecondsToTimer(message.arg1 / 1000));
                currentTime = message.arg1;
                if (MiliSecondsToTimer(message.arg1 / 1000).equals(MiliSecondsToTimer(videoView.getDuration() / 1000))){
                    videoView.stopPlayback();
                    seekBar.setProgress(0);
                    txtCurrentTime.setText("00:00");
                }else {
                    seekBar.setProgress(message.arg1);
                }
                return true;
            }
        });
private  void  changeSeekBar(){
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b){
                videoView.pause();
                videoView.seekTo(i);
                seekBar.setProgress(i);
                txtCurrentTime.setText(MiliSecondsToTimer(i / 1000));
                checkEventVideo();
                imageEventVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    });
}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_to_photo, menu);
        return super.onCreateOptionsMenu(menu);
        }
@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
        case android.R.id.home:
            onBackPressed();
        break;
        case R.id.menuTakePhoto:
            takenPhoto();
        break;
        case R.id.menuSnap:
        dialogSettingSnap();
        break;
        }
        return super.onOptionsItemSelected(item);
        }
private  void dialogSettingSnap(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_setting_snap);
        final EditText edtSnapSec = dialog.findViewById(R.id.edtSnapSec);
        Button btnGetValueSnap = dialog.findViewById(R.id.btnGetValueSnap);
        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelDialog);
        btnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnGetValueSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 snap = edtSnapSec.getText().toString().trim();
                int timeInt = Integer.parseInt(snap)*1000;
                timeMill = Long.parseLong(timeInt+"");
                if (timeMill> timeInMilliSec){
                    Toast.makeText(VideoToPhoto.this, "Vui lòng chọn thời lượng nhỏ hơn thời lượng video", Toast.LENGTH_LONG).show();
                }
                else{
                    txtSnap.setText("Snap every " + snap +" second");
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private String MiliSecondsToTimer(long milisec) {
        String finalTimerString = "";
        String hoursString = "";
        String secondString;
        String minuteString;
        int seconds = (int) milisec % 60;
        int minutes = (int) milisec / 60;
        int hours = (int) milisec / (60 * 60);
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
    private  void checkEventVideo(){
       imageEventVideo  = findViewById(R.id.imageEventVideo);
        imageEventVideo.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageEventVideo.setVisibility(View.GONE);
            }
        },3000);
    imageEventVideo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (videoView.isPlaying()){
                videoView.pause();
                isThreadRunning=false;
                imageEventVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            else {
                videoView.start();
                isThreadRunning=true;
                imageEventVideo.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        }
    });
}
private void takenPhoto(){
    retriever = new MediaMetadataRetriever();
    retriever.setDataSource(this, Uri.fromFile(new File(path)));
    videoView.pause();
    Bitmap bmFrame = retriever.getFrameAtTime((long) videoView.getCurrentPosition()* 1000);
    if (bmFrame!= null){
        createFileImage(bmFrame);
        captureImageList.add(bmFrame);
    }
    setupRecycleViewImage();
}
private  void setupRecycleViewImage(){
    recyclerView = findViewById(R.id.recyclerViewImage);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
    recyclerView.setLayoutManager(linearLayoutManager);
    adapter = new RecyclerImageAdapter(captureImageList,this, this);
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
}
    private void createFileImage(Bitmap bimap) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScreenShotsVinh");
        if(!file.exists()){
            file.mkdir();
        }
        Log.e("TAG", file.getPath() +"fff");
        DateTimeFormatter formatter = null;
        String fileName = "";
        Bitmap.CompressFormat bitmap = Bitmap.CompressFormat.JPEG;
        if(type.equals(getString(R.string.jpg))){
            endWiths = ".jpg";
            bitmap = Bitmap.CompressFormat.JPEG;
        }
        if(type.equals(getString(R.string.png))){
            endWiths = ".png";
            bitmap = Bitmap.CompressFormat.PNG;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fileName = String.format(formatter.format(now)+endWiths);
        }
        else fileName = System.currentTimeMillis()+endWiths;
        File outFile = new File(file,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            bimap.compress(bitmap,setQuality(quality),fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int setQuality(String quality){
        if(quality.equals(getResources().getString(R.string.best))){
            return 100;
        }
        else if(quality.equals(getResources().getString(R.string.very_high))){
            return 85;
        }
        else if(quality.equals(getResources().getString(R.string.high))){
            return 75;
        }
        else if(quality.equals(getResources().getString(R.string.medium))){
            return 65;
        }
        else if(quality.equals(getResources().getString(R.string.low))){
            return 50;
        }
        else return 75;
    }
    private  void readSharedReferent(){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        type  =  sharedPreferences.getString("typeFile", getString(R.string.jpg));
        quality = sharedPreferences.getString("valueQuality", getString(R.string.high));
    }
    @Override
    public void onClickItem(int position) {
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
      thread.stop();
    }
    // time capture
    private  void  rangeSeekBar(){
    rangeView.setProgress(currentTime, videoView.getDuration());
rangeView.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
    @Override
    public void onProgressChanged(RangeSeekBar rangeSeekBar, int i, final int i1, boolean b) {
      if (b){
          videoView.pause();
          videoView.seekTo(i);

          txtCurrentTime.setText(MiliSecondsToTimer(i / 1000));
          txtEndTime.setText(MiliSecondsToTimer(i1/1000));

          rangeSeekBar.setProgress(i, i1);
      }
    }

    @Override
    public void onStartTrackingTouch(RangeSeekBar rangeSeekBar) {

    }

    @Override
    public void onStopTrackingTouch(RangeSeekBar rangeSeekBar) {

    }
});
    }


}
