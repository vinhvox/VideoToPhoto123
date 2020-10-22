package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.R;

import java.io.File;
import java.util.List;

public class ImagesSelectAdapter extends RecyclerView.Adapter<ImagesSelectAdapter.ViewHolder> {
    List<File> fileList;
    Context context;
    CallBack callBack;

    public ImagesSelectAdapter(List<File> fileList, Context context, CallBack callBack) {
        this.fileList = fileList;
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.images_select, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        ImageView imageView = view.findViewById(R.id.imageItem);
        ImageButton imageButton = view.findViewById(R.id.imageButtonDelete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onDeleteItem(fileList.get(position));
            }
        });

        imageView.setImageURI(Uri.fromFile(fileList.get(position)));

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
    public  interface  CallBack{
        void  onClickItem();
        void  onDeleteItem(File file);
    }
}
