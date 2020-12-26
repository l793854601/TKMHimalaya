package com.tkm.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.tkm.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackPlayerPagerAdapter extends PagerAdapter {

    private List<Track> mList = new ArrayList<>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View itemView = layoutInflater.inflate(R.layout.fragment_track_player_pager, container, false);
        container.addView(itemView);

        Track track = mList.get(position);
        ImageView ivBanner = itemView.findViewById(R.id.iv_banner);
        Picasso.get().load(track.getCoverUrlLarge()).into(ivBanner);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setList(List<Track> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
