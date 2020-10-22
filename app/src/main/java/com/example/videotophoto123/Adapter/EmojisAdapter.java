package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto123.R;

import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EmojisAdapter extends RecyclerView.Adapter<EmojisAdapter.ViewHoler> {
    List<String> list;
    Context context;
    Callback callback;

    public EmojisAdapter(List<String> list, Context context, Callback callback) {
        this.list = list;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_emojs;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        View view = holder.getView();
        inflateToViews(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.choseSticker(list.get(position));
            }
        });

    }
    private  void inflateToViews( View view, int position){
//        PhotoEditorView emoji = view.findViewById(R.id.photoEditorViewItem);
//        PhotoEditor photoEditor = new PhotoEditor.Builder(context, emoji).build();
//        photoEditor.addEmoji(list.get(position));
        TextView textView = view.findViewById(R.id.txtx);
        textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        View view;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }
    public  interface Callback{
        void choseSticker(String path);
    }
}
