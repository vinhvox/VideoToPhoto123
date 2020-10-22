package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowFolderImageAdapter extends RecyclerView.Adapter<ShowFolderImageAdapter.ViewHolder> {
    List<File> fileList;
    Context context;
    Callback callback;

    public ShowFolderImageAdapter(List<File> fileList, Context context, Callback callback) {
        this.fileList = fileList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.folder_video;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
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
    private  void  inflateToViews(View view, int position){
        TextView txtFolderName = view.findViewById(R.id.txtFolderName);
        txtFolderName.setText(fileList.get(position).getName()  +" ("+ countFiles(fileList.get(position))+") images ");
    }
    private int countFiles(File file) {
        ArrayList<File> list= new ArrayList<>();
        File[] f=file.listFiles();
        for(File files:f){
            if(files.getName().endsWith(".jpg")|| files.getName().endsWith(".png")){
                list.add(files);
            }
        }
        return list.size();
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
    public  interface  Callback{
        void  onClick(File file);
    }
}
