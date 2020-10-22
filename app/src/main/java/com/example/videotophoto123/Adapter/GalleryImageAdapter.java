package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.IslamicCalendar;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.MainGallery;
import com.example.videotophoto123.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {
    List<File> fileList;
    Context context;
    Callback callback;
    List<File> fileListChoose;

    public GalleryImageAdapter(List<File> fileList, Context context, Callback callback) {
        this.fileList = fileList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_image_long_click;
        fileListChoose = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterToViews(view, position);
//        view.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        });


    }
    private  void inflaterToViews(View view, int position){
        ImageView imageView = view.findViewById(R.id.imageItem);
        CheckBox checkBox = view.findViewById(R.id.checkBoxList);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth= 100;
        options.outHeight=100;
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(fileList.get(position)), options);
       imageView.setImageBitmap(bitmap);
        CardView cardViewItem = view.findViewById(R.id.cardViewItem);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                checkBox.setChecked(true);
                checkBox.setVisibility(View.VISIBLE);
                callback.onChoose(fileList.get(position));
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    checkBox.setChecked(false);
                    fileListChoose.remove(fileList.get(position));
                    callback.onDisChoose(fileList.get(position));
                    checkBox.setVisibility(View.GONE);
                }
                else {
                    callback.onClickItem(fileList.get(position).getAbsolutePath());

                }
            }
        });

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
   public   interface  Callback{
        void  onClickItem(String path);
        void  onDisChoose(File file);
        void  onChoose( File file);
    }

}
