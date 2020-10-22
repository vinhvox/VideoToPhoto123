package com.example.videotophoto123.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.Adapter.GalleryVideoAdapter;
import com.example.videotophoto123.Adapter.ListVideoAdapter;
import com.example.videotophoto123.BuildConfig;
import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoList;
import com.example.videotophoto123.VideoShow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentVideo extends Fragment implements GalleryVideoAdapter.Callback {
    View view;
    private static final int REQUEST_BACK = 1000 ;
    List<File> videoFile = new ArrayList<>();
    String path;
    GalleryVideoAdapter adapter;
    File filePath;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        setupViews();
        return view;
    }

    private void setupViews() {
        filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Videos");
        if (!filePath.exists()){
            filePath.mkdirs();
        }
        RecyclerView recyclerView  = view.findViewById(R.id.recyclerviewVIdeoGallery);
        filePath = new File(filePath.getPath());
        if (getVideoByPath(filePath)!= null){
            videoFile = getVideoByPath(filePath);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new GalleryVideoAdapter(videoFile, getContext(), this);
            recyclerView.setAdapter(adapter);
        }

    }
    ArrayList<File> getVideoByPath(File file){
        ArrayList<File> temp =new ArrayList<>();
        File[]  files = file.listFiles();
        for(File f :files){
            if(f.getName().endsWith(".mp4")){
                temp.add(f);
            }
        }
        return temp;
    }


    @Override
    public void onClick(File path) {
        dialogOption(path);
    }

    private void dialogOption(File file) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_video_option);
        Button buttonOpenVideo = dialog.findViewById(R.id.buttonOpenVideo);
        Button buttonRenameVideo = dialog.findViewById(R.id.buttonRenameVideo);
        Button buttonDeleteVideo = dialog.findViewById(R.id.buttonDeleteVideo);
        Button buttonShareVideo = dialog.findViewById(R.id.buttonShareVideo);
        Button buttonDetailsVideo = dialog.findViewById(R.id.buttonDetailsVideo);
        buttonOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VideoShow.class);
                Log.e("PATH", file+"");
                intent.putExtra("PATH", file+"");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        buttonRenameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openRename(file);

            }
        });
        buttonDeleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteFile(file);
            }
        });
        buttonShareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventShare(file);
            }
        });
        buttonDetailsVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showDetails(file);
            }
        });

        dialog.show();
    }
    private  void  openRename(File file){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_rename);
        EditText edtRenameFile = dialog.findViewById(R.id.edtRenameFile);
        Button btnRename = dialog.findViewById(R.id.btnRename);
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edtRenameFile.getText().toString().trim();
                if (newName.isEmpty()){
                    edtRenameFile.setError(getString(R.string.error));
                }
                else {
                    File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Videos/"+newName+".mp4");
                    if (file.renameTo(newFile)){
                        setupViews();
                        dialog.dismiss();
                    }
                    else {
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }
    private  void  deleteFile(File file){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete));
        builder.setIcon(R.drawable.icon_warning);
        builder.setMessage(getString(R.string.message_delete));
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ( file.delete()){
                    setupViews();
                    dialogInterface.dismiss();
                }
                else {
                    dialogInterface.dismiss();
                    Toast.makeText(getContext(), "Delete Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
    private  void eventShare(File file){
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//        Log.e("PATH", path);
//        File file = new File(path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("video/*");
        startActivity(Intent.createChooser(intent, "Share video"));
    }
    private  void  showDetails(File file){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_video_details);
        TextView txtVideoFileName = dialog.findViewById(R.id.txtVideoFileName);
        TextView txtVideoFileSize = dialog.findViewById(R.id.txtVideoFileSize);
        TextView txtVideoFileDate = dialog.findViewById(R.id.txtVideoFileDate);
        TextView txtVideoFilePath = dialog.findViewById(R.id.txtVideoFilePath);
        ImageButton imageButton = dialog.findViewById(R.id.imageButtonDismiss);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtVideoFilePath.setText(file.getPath());
        txtVideoFileName.setText(file.getName());
        txtVideoFileSize.setText(file.length()/(1000*1000)+"MB");
        txtVideoFileDate.setText(getDate(file));
        dialog.show();
    }
    private  String   getDate(File file){
        Date lastModDate = new Date(file.lastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(lastModDate.getTime());
        Log.e("TAG", simpleDateFormat.format(lastModDate.getTime())+"");
        return  date;
    }
}
