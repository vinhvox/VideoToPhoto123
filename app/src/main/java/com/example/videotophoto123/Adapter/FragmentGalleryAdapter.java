package com.example.videotophoto123.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.videotophoto123.Fragment.FragmentImage;
import com.example.videotophoto123.Fragment.FragmentVideo;
import com.example.videotophoto123.R;

import java.util.ArrayList;

public class FragmentGalleryAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> title = new ArrayList<>();
    FragmentVideo  fragmentVideo;
    FragmentImage fragmentImage;
    public FragmentGalleryAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        title.add(context.getString(R.string.image));
        title.add(context.getString(R.string.video));
        fragmentVideo = new FragmentVideo();
        fragmentImage = new FragmentImage();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragmentImage;

            case 1:
                return  fragmentVideo;
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
