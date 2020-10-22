package com.example.videotophoto123.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.Adapter.RecyclerImageAdapter;
import com.example.videotophoto123.EditImage;
import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoList;
import com.example.videotophoto123.VideoPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class TimeCapture extends Fragment implements RecyclerImageAdapter.Callback {
    String path, type, quality, endWiths;
    View view;
    float value = 2;
    List<Bitmap> arrayBitmap;
    SeekBar seekBar;
    TextView txtCurrentTimeVideo, txtVideoLength, txtSnapSec;
    ImageView imagePlayVideo, imageEventCuts;
    long startLimit, endLimit;
    int position;
    MediaMetadataRetriever retriever;
    RangeSeekBar rangeSeekBar ;
   public static VideoView videoViewTime;
   RecyclerView recyclerView;
   RecyclerImageAdapter recyclerImageAdapter;
   ArrayList<File> arrayList = new ArrayList<>();
   Bitmap bitmap;
    long timeCut;
   private  testFrameCuts frameCuts = new testFrameCuts();
   private  cutFrame cutFrame ;
   SeekBar seekBarLoad;
   TextView txtLoad;
   boolean pause = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_capture, container, false);
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        path = VideoPhoto.path;
        readDataShareRe();
        seekBarLoad = view.findViewById(R.id.seekBarLoad);
        txtLoad = view.findViewById(R.id.txtLoad);
        setupView();
        stopAsyncTask();

    }

    private void stopAsyncTask() {
        imageEventCuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cutFrame.isCancelled()){
                    imageEventCuts.setImageResource(R.drawable.stop);
                  cutFrame = new cutFrame();
                  cutFrame.execute();

                }
                else {
                    cutFrame.cancel(true);

                    imageEventCuts.setImageResource(R.drawable.conti);
                }
//                setupRecycler();
//                if ( pause){
//                    imageEventCuts.setImageResource(R.drawable.stop);
//                    pause= false;
//                    cutFrame = new cutFrame();
//                    cutFrame.execute();
//                    cutFrame.onProgressUpdate();
//                }
//                else {
//                    imageEventCuts.setImageResource(R.drawable.startimage);
//                    cutFrame.cancel(true);
//                    pause = true;
//                }
            }
        });
    }
    @Override
    public void onClickItem(int position) {
        videoViewTime.pause();
        Intent intent = new Intent(getContext(), EditImage.class);
        intent.putExtra("PATH_IMAGE", arrayList.get(position)+"");
        startActivity(intent);
        cutFrame.cancel(true);
    }


    private void setupView() {
        imageEventCuts = view.findViewById(R.id.imageStop);
        arrayBitmap = new ArrayList<>();
        seekBar = view.findViewById(R.id.idSeekBarVideo);
        txtCurrentTimeVideo = view.findViewById(R.id.txtCurrentTimeVideo);
        txtVideoLength = view.findViewById(R.id.txtVideoLength);
        txtSnapSec = view.findViewById(R.id.txtSnapSec);
        imagePlayVideo = view.findViewById(R.id.imagePlayVideo);
        videoViewTime = view.findViewById(R.id.videoViewTimeCapture);
        rangeSeekBar = view.findViewById(R.id.rangerSeekBarTime);
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        startLimit =0;
        videoViewTime.setVideoURI(Uri.parse(path));
        videoViewTime.start();


        String timeVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        ;Long  duration = Long.parseLong(timeVideo);
        rangeSeekBar.setMax(Integer.parseInt(timeVideo));
        rangeSeekBar.setProgress(0, Integer.parseInt(timeVideo));
        endLimit = duration;
        startLimit =0;
        txtVideoLength.setText(MilliSecondsToTimer(duration));
        videoViewTime.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

            }
        });
        txtSnapSec.setText(getString(R.string.snap_every)+" "+ value +" "+ getString(R.string.sec));
        getVideoName();
//        setupSeekBar();
        listenToVideoClickEvent();
        rangeSeekBarChange();
//        checkSeekBar();
    }
    private  void  getVideoName(){
        TextView txtVideoName = view.findViewById(R.id.txtVideoName);
        File file = new File(path);
        String[] videoName =  file.getName().split(".mp4");
        txtVideoName.setText(videoName[0]);
    }
    private void listenToVideoClickEvent(){
        videoStatus();
//        videoViewTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imagePlayVideo.setVisibility(View.VISIBLE);
//            }
//        });
        videoViewTime.setOnTouchListener(new View.OnTouchListener() {
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
                if (videoViewTime.isPlaying()){
                    videoViewTime.pause();
                    imagePlayVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
                else {
                    videoViewTime.start();
                    imagePlayVideo.setImageResource(R.drawable.ic_baseline_pause_24);
                    Handler handler = new Handler();
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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_video_to_photo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuTakePhoto:
                cutFrame = new cutFrame();
                processCutFrame();
                imageEventCuts.setVisibility(View.VISIBLE);
                break;
            case R.id.menuSnap:
                dialogSettingSnap();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void  dialogSettingSnap(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_setting_snap);
        EditText edtSnapSec = dialog.findViewById(R.id.edtSnapSec);
        Button button = dialog.findViewById(R.id.btnGetValueSnap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snapSec = edtSnapSec.getText().toString().trim();
               float sec = Float.valueOf(snapSec);
               Log.e("SS", sec +" "+ videoViewTime.getDuration());
                 if (sec<=0){
                     value = 2;
                     dialog.dismiss();
                 }
                 else if (sec*1000<= videoViewTime.getDuration()){
                     value = sec;
                     dialog.dismiss();
                 }else {
                     txtSnapSec.setError(getString(R.string.waiting_snap));
                     edtSnapSec.setError(getString(R.string.waiting_snap));
                     edtSnapSec.setText("");
                 }
//                txtSnapSec.setText("Snap Every  "+ value +" sec");
                txtSnapSec.setText(getString(R.string.snap_every)+" "+ value +" "+ getString(R.string.sec));


            }
        });
        dialog.show();
    }
    private  void rangeSeekBarChange(){
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
               if (b){
                   startLimit = i;
                   endLimit = i1;
                   videoViewTime.seekTo(i);
                   txtCurrentTimeVideo.setText(MilliSecondsToTimer(i));
                   txtVideoLength.setText(MilliSecondsToTimer(i1));
               }
               else {
                   startLimit=0;
                   endLimit = videoViewTime.getDuration();
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
    private  int getTimesCapture(){
        long time = endLimit - startLimit;
        return (int) ((int) time/ (value*1000));
    }
    private  void processCutFrame(){
//        if ( cutFrame.isCancelled()){
//            new cutFrame().execute();
//        }
//        else {
//            cutFrame.execute();
//        }
        cutFrame = new cutFrame();
        cutFrame.execute();
        setupRecycler();
    }
    private  void setupRecycler(){
        recyclerView = view.findViewById(R.id.recyclerViewTimeCapture);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerImageAdapter  = new RecyclerImageAdapter(arrayBitmap, getActivity(), this);
        recyclerView.setAdapter(recyclerImageAdapter);
        recyclerImageAdapter.notifyDataSetChanged();
    }
    private  class  testFrameCuts extends  AsyncTask<Void, Integer, List<Bitmap>>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
         progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            seekBarLoad.setMax(getTimesCapture());
            txtLoad.setText(position+" / "+getTimesCapture());
//            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected List<Bitmap> doInBackground(Void... voids) {
             timeCut= startLimit/1000;
            for (int i = 0 ; i<= getTimesCapture(); i ++){
                bitmap = retriever.getFrameAtTime( timeCut*1000*1000,MediaMetadataRetriever.OPTION_CLOSEST);
                saveTest(bitmap);
                arrayBitmap.add(bitmap);
                timeCut += value;
                publishProgress(i);
                position = i;

            }
            return arrayBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage("OK "+ values);
            seekBarLoad.setProgress(position);
            txtLoad.setText(position+" / "+getTimesCapture());


        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            progressDialog.dismiss();
            setupRecycler();
        }
    }
    private  class cutFrame extends AsyncTask<Integer , Void , List<Bitmap> >{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          seekBarLoad.setMax(getTimesCapture());

        }
        @Override
        protected List<Bitmap> doInBackground(Integer... longs) {
                for (int i = position ; i<= getTimesCapture(); i ++){
                    bitmap = retriever.getFrameAtTime( timeCut*1000*1000,MediaMetadataRetriever.OPTION_CLOSEST);
                    saveTest(bitmap);
                    arrayBitmap.add(0, bitmap);
                    timeCut += value;
                    publishProgress();
                    position = i;
                    if (cutFrame.isCancelled()){
                        break;
                    }
                }
            return arrayBitmap;
        }
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate(voids);
            Log.e("CHECK", position+"");
            seekBarLoad.setProgress(position);
            txtLoad.setText(position+" / "+getTimesCapture());
            recyclerImageAdapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            imageEventCuts.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.e("CHECK", position+" cancel");
        }

        @Override
        protected void onCancelled(List<Bitmap> bitmaps) {
            super.onCancelled(bitmaps);
        }
    }
    private  void  readDataShareRe(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        type  =  sharedPreferences.getString("typeFile", getString(R.string.jpg));
        quality = sharedPreferences.getString("valueQuality", getString(R.string.high));
    }
    private String getTimeSys(){
        Calendar calendar = Calendar.getInstance();

        return String.valueOf(calendar.getTimeInMillis());
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
    private  void saveTest(Bitmap bitmap){

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
//                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fileName = String.format(System.currentTimeMillis()+endWiths);
        }
        else fileName = getTimeSys()+endWiths;
        File outFile = new File(file,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            bitmap.compress(compressFormat,getValueQuality(quality),fos);
            arrayList.add(outFile);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

