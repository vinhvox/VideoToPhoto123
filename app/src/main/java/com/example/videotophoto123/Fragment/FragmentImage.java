package com.example.videotophoto123.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.videotophoto123.Adapter.DateAdapter;
import com.example.videotophoto123.Adapter.GalleryImageAdapter;
import com.example.videotophoto123.Adapter.RecyclerImageAdapter;
import com.example.videotophoto123.BuildConfig;
import com.example.videotophoto123.EditImage;
import com.example.videotophoto123.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class FragmentImage extends Fragment implements  GalleryImageAdapter.Callback {
    View view;
    RecyclerView recycleViewGalleryImage;
    ArrayList<File> fileArrayList;
    DateAdapter dateAdapter;
    ArrayList<String> dateList ;
    ArrayList<String> date;
    ArrayList<File> listChoose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dateList = new ArrayList<>();
        date = new ArrayList<>();
        setupView();
    }


    private void setupView() {
        listChoose = new ArrayList<>();
        recycleViewGalleryImage = view.findViewById(R.id.recycleViewGalleryImage);
        fileArrayList = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Images");
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(file.getPath());
        dateList = new ArrayList<>();
        new readDataTest().execute(file);
//        if (readData(file)!= null){
//            fileArrayList = readData(file);
//            arrayFormat();
//        }
//        else {
//            recycleViewGalleryImage.setVisibility(View.GONE);
//        }


    }
    private  void  setupRecycleView(List<String> arrayList){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycleViewGalleryImage.setLayoutManager(linearLayoutManager);
        dateAdapter = new DateAdapter(arrayList, getContext(), this);
        recycleViewGalleryImage.setAdapter(dateAdapter);
        dateAdapter.notifyDataSetChanged();
    }
    private class readDataTest extends AsyncTask<File, Void, List<File>>{
        @Override
        protected List<File> doInBackground(File... file) {
            ArrayList<File> temp = new ArrayList<>();
            File[] files = file[0].listFiles();
            for (File f : files) {
                if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) {
                    temp.add(f);
//                    getDate(f.getAbsolutePath());
                }
            }
            return temp;
        }

        @Override
        protected void onPostExecute(List<File> strings) {
            super.onPostExecute(strings);

            for (File file: strings){
                Date lastModDate = new Date(file.lastModified());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(lastModDate.getTime());
                Log.e("TAG", simpleDateFormat.format(lastModDate.getTime())+"");
                dateList.add(date);
            }
            for (String item :dateList){
                if (!date.contains(item)){
                    date.add(item);
                }
            }
            Collections.sort(date, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {

                    return   t1.compareTo(s);
                }
            });
            setupRecycleView(date);
        }
    }
    private  void   getDate(String path){
        File file = new File(path);
        Date lastModDate = new Date(file.lastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(lastModDate.getTime());
        Log.e("TAG", simpleDateFormat.format(lastModDate.getTime())+"");
        dateList.add(date);
        }

    @Override
    public void onClickItem(String path) {
        if (listChoose.size()>0){
            dialogIsChoose(path);
        }
        else {
            Intent intent = new Intent(getContext(), EditImage.class);
            intent.putExtra("PATH_IMAGE", path);
            startActivity(intent);
        }
    }
    private  void  dialogIsChoose(String path){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.icon_warning);
        builder.setMessage(R.string.notify);
        builder.setMessage(R.string.message_delete);
        builder.setPositiveButton("GRANTED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), EditImage.class);
                intent.putExtra("PATH_IMAGE", path);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("DENIED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onDisChoose(File file) {
        if (listChoose.size()>0){
            listChoose.remove(file);
            setTitleActionbar(listChoose.size());
        }
    }

    @Override
    public void onChoose(File file) {
        if (listChoose.contains(file)){
            return;
        }
        listChoose.add(file);
        Log.e("CHECKCALL", listChoose.size()+ "");
        setTitleActionbar(listChoose.size());


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
           inflater.inflate(R.menu.menu_gallery_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuDeleteChooseImage:
                dialogDeleteFile();
                break;
            case  R.id.menuShareImageChoose:
                eventShare();
                break;
            case R.id.menuReLoad:
                listChoose.clear();
               dateAdapter.notifyDataSetChanged();
               setTitleActionbar(0);

        }
        return super.onOptionsItemSelected(item);
    }
    private  void  setTitleActionbar(int size){
        if (size>0){
            getActivity().setTitle("Select "+size+" image");
        }
        else {
            getActivity().setTitle(" image");
        }
    }
    private  void dialogDeleteFile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle(getString(R.string.delete));
        builder.setMessage("Delete "+ listChoose.size()+" image");
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (File file : listChoose){
                    if (file.delete()){
                    }
                    else {
                    }
                    dateAdapter.notifyDataSetChanged();

                }
                listChoose.clear();
                dialogInterface.dismiss();
                setTitleActionbar(0);
            }
        });
        builder.show();
    }
    private void eventShare() {
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//        Log.e("PATH", path);
//        File file = new File(path);
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : listChoose){
            Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            uris.add(photoURI);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "myimages"));
    }
}

