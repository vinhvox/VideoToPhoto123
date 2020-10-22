package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.R;

import java.io.File;
import java.util.List;

public class ImagesSlideShowAdapter extends RecyclerView.Adapter<ImagesSlideShowAdapter.ViewHolder> {
    List<File> fileList;
    Context context;
    Callback callback;

    public ImagesSlideShowAdapter(List<File> fileList, Context context, Callback callback) {
        this.fileList = fileList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_image_recycler;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.inSelectImages(fileList.get(position));
            }
        });

    }
    private  void  inflaterToView(View view, int position){
        ImageView imageView = view.findViewById(R.id.imageItem);
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
    public  interface  Callback{
        void  inSelectImages(File file);
    }
}
