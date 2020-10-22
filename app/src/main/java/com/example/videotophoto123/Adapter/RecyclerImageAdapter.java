package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoToPhoto;

import java.util.List;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {
    List<Bitmap> listImage ;
    Context context;
    Callback callback;

    public RecyclerImageAdapter(List<Bitmap> listImage, Context context, Callback callback) {
        this.listImage = listImage;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_image_recycler;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerImageAdapter.ViewHolder holder, final int position) {
        View view = holder.getView();
        inflateDataToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }
    public  void  inflateDataToView(View view, int position){
        ImageView imageItem = view.findViewById(R.id.imageItem);
        imageItem.setImageBitmap(listImage.get(position));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
        public  View getView(){
            return view;
        }
    }
    public interface Callback{
        void  onClickItem(int position);
    }
}
