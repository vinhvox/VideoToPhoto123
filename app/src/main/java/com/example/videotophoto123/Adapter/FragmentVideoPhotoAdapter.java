package com.example.videotophoto123.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.videotophoto123.Fragment.QuickCapture;
import com.example.videotophoto123.Fragment.TimeCapture;
import com.example.videotophoto123.R;
import com.example.videotophoto123.VideoShow;

import java.util.ArrayList;

public class FragmentVideoPhotoAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> title = new ArrayList<>();

    QuickCapture quickCapture;
    TimeCapture timeCapture;
    public FragmentVideoPhotoAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        title.add(context.getString(R.string.quick_capture));
        title.add(context.getString(R.string.time_capture));
        quickCapture = new QuickCapture();
        timeCapture = new TimeCapture();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  quickCapture;
            case 1:
                return  timeCapture;
        }
        return null;
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
