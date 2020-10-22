package com.example.videotophoto123.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.Adapter.RecyclerImageAdapter;
import com.example.videotophoto123.EditImage;
import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class QuickCapture extends Fragment implements RecyclerImageAdapter.Callback {
   public static VideoView videoViewQick;
    String path, type, quality, endWiths;
    View view;
    SeekBar seekBar;
    TextView txtCurrentTimeVideo, txtVideoLength;
  public  static    Thread thread;
    boolean threadRunning = true;
  public static   ImageView imagePlayVideo;
    ArrayList<Bitmap> arrayBitmap;
    MediaMetadataRetriever retriever;
    RecyclerImageAdapter adapter ;
    ArrayList<File> arrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quick_capture, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        path = VideoPhoto.path;
        Log.w("PATH", path +"Quick");
        setupView();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (videoViewQick != null){
                    try {
                        if (videoViewQick.isPlaying()){
                            Message message = new Message();
                            message.arg1 = videoViewQick.getCurrentPosition();
                            handler.sendMessage(message);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e){

                    }
                    if (!threadRunning){
                        return;
                    }
                }
            }
        });
        thread.start();
    }

    private void setupView() {
        arrayBitmap = new ArrayList<>();
        seekBar = view.findViewById(R.id.idSeekBarVideo);
        txtCurrentTimeVideo = view.findViewById(R.id.txtCurrentTimeVideo);
        txtVideoLength = view.findViewById(R.id.txtVideoLength);
        imagePlayVideo = view.findViewById(R.id.imagePlayVideo);

        videoViewQick = view.findViewById(R.id.videoViewCapture);
        videoViewQick.setVideoURI(Uri.parse(path));
        videoViewQick.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                txtVideoLength.setText(MilliSecondsToTimer(mediaPlayer.getDuration()));
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });
        getVideoName();
        setupSeekBar();
        listenToVideoClickEvent();

    }
    private  void  getVideoName(){
        TextView txtVideoName = view.findViewById(R.id.txtVideoName);
        File file = new File(path);
    String[] videoName =  file.getName().split(".mp4");
        txtVideoName.setText(videoName[0]);
}
    private  void setupSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    videoViewQick.pause();
                    videoViewQick.seekTo(i);
                    txtCurrentTimeVideo.setText(MilliSecondsToTimer(i));
                    imagePlayVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    imagePlayVideo.setVisibility(View.VISIBLE);
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
    private void listenToVideoClickEvent(){
        videoStatus();
       videoViewQick.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
               imagePlayVideo.setVisibility(View.VISIBLE);
               return false;
           }
       });
    }
    private  void  videoStatus(){
       imagePlayVideo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (videoViewQick.isPlaying()){
                   videoViewQick.pause();
                   imagePlayVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
               }
               else {
                   videoViewQick.start();
                   imagePlayVideo.setImageResource(R.drawable.ic_baseline_pause_24);
                   Handler  handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           imagePlayVideo.setVisibility(View.GONE);
                       }
                   }, 3000);
               }

           }
       });

    }
   private Handler handler = new Handler(new Handler.Callback() {
       @Override
       public boolean handleMessage(@NonNull Message message) {
           txtCurrentTimeVideo.setText(MilliSecondsToTimer(message.arg1));
           seekBar.setProgress(message.arg1);
           if (MilliSecondsToTimer(message.arg1).equals( MilliSecondsToTimer(videoViewQick.getDuration()))){
               videoViewQick.seekTo(0);
               seekBar.setProgress(0);
               videoViewQick.pause();
               imagePlayVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
               imagePlayVideo.setVisibility(View.VISIBLE);
           }
           return true;
       }
   });
    public     void cutFrame(){
        videoViewQick.pause();
        imagePlayVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        imagePlayVideo.setVisibility(View.VISIBLE);
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getActivity(), Uri.fromFile(new File(path)));
        Bitmap bmFrame = retriever.getFrameAtTime((long) videoViewQick.getCurrentPosition()* 1000);
        if (bmFrame!= null){
            saveFrameToFile(bmFrame);
            arrayBitmap.add(0, bmFrame);
        }
        setupRecyclerView(arrayBitmap);
    }
    private  void setupRecyclerView(ArrayList<Bitmap> arrayBitmap){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = view.findViewById(R.id.idRecyclerViewFrame);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerImageAdapter(arrayBitmap, getContext(), this);
        recyclerView.setAdapter(adapter);
    }
    private void  saveFrameToFile(Bitmap imageBitmap){
        readDataShareRe();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Images");
        if(!file.exists()){
            file.mkdirs();
        }
        DateTimeFormatter formatter = null;
        String fileName = "";
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        if(type.equals(getString(R.string.jpg))){
            endWiths = ".jpg";
            compressFormat = Bitmap.CompressFormat.JPEG;
        }
        if(type.equals(getString(R.string.png))){
            endWiths = ".png";
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fileName = String.format(formatter.format(now)+endWiths);
        }
        else fileName = getTimeSys()+endWiths;
        File outFile = new File(file,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            imageBitmap.compress(compressFormat,getValueQuality(quality),fos);
            arrayList.add(outFile);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.w("Error",e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaScannerConnection.scanFile(getContext(), new String[]{outFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(String s, Uri uri) {

                }
            });
        }catch (Exception e){

        }
    }
    private  int  getValueQuality(String quality){
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
    private String getTimeSys(){
        Calendar calendar = Calendar.getInstance();

        return String.valueOf(calendar.getTimeInMillis());
    }
    private  void  readDataShareRe(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        type  =  sharedPreferences.getString("typeFile", getString(R.string.jpg));
        quality = sharedPreferences.getString("valueQuality", getString(R.string.high));
    }
    @Override
    public void onClickItem(int position) {
        videoViewQick.stopPlayback();
        thread.interrupt();
        Intent intent = new Intent(getContext(), EditImage.class);
        intent.putExtra("PATH_IMAGE", arrayList.get(position)+"");
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_video_to_photo, menu);
        menu.findItem(R.id.menuSnap).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuTakePhoto){
            cutFrame();
        }
        return super.onOptionsItemSelected(item);
    }
}
