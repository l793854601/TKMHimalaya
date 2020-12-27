package com.tkm.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tkm.himalaya.R;
import com.tkm.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, Track track);
    }

    private OnItemClickListener mOnItemClickListener;

    private List<Track> mList = new ArrayList<>();

    private int mIndex = -1;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = mList.get(position);
        holder.bindHolder(track, position);

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, track);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Track> list) {
        setList(list, -1);
    }

    public void setList(List<Track> list, int index) {
        mIndex = index;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        mIndex = index;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIv;
        private TextView mTvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            mIv = itemView.findViewById(R.id.iv);
            mTvTitle = itemView.findViewById(R.id.tv_title);
        }

        public void bindHolder(Track track, int position) {
            mTvTitle.setText(track.getTrackTitle());
            mTvTitle.setTextColor(BaseApplication.getAppContext().getResources().getColor(position == mIndex ? R.color.main_color : R.color.black));
            mIv.setVisibility(position == mIndex ? View.VISIBLE : View.GONE);
        }
    }
}
