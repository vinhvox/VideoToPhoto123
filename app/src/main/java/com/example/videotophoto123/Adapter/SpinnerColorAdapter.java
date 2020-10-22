package com.example.videotophoto123.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.videotophoto123.R;

import java.util.List;

public class SpinnerColorAdapter extends BaseAdapter {
    List<Integer> color;
    Context context;


    public SpinnerColorAdapter(List<Integer> color, Context context) {
        this.color = color;
        this.context = context;
    }

    @Override
    public int getCount() {
        return color.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int layoutId = R.layout.item_emojs;
        LayoutInflater inflater= LayoutInflater.from(context);
        view = inflater.inflate(layoutId, viewGroup, false);
        ConstraintLayout button = view.findViewById(R.id.itemColor);
        button.setBackgroundColor(color.get(i));
        return view;
    }

}
