package com.tkm.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tkm.himalaya.R;
import com.tkm.himalaya.utils.StringUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, Track track, List<Track> tracks);
    }

    private List<Track> mList = new ArrayList<>();

    private SimpleDateFormat mYMDDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat mMSDateFormat = new SimpleDateFormat("mm:ss");

    private OnItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public AlbumDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_album_detail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailAdapter.ViewHolder holder, int position) {
        Track track = mList.get(position);
        holder.bindHolder(track);

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, track, mList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Track> tracks) {
        mList.clear();
        mList.addAll(tracks);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvOrder;
        private TextView mTvTitle;
        private TextView mTvPlayCount;
        private TextView mTvDuration;
        private TextView mTvCreateDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvOrder = itemView.findViewById(R.id.tv_order);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvPlayCount = itemView.findViewById(R.id.tv_play_count);
            mTvDuration = itemView.findViewById(R.id.tv_duration);
            mTvCreateDate = itemView.findViewById(R.id.tv_create_date);
        }

        public void bindHolder(Track track) {
            mTvOrder.setText(String.valueOf(track.getOrderNum() + 1));
            mTvTitle.setText(track.getTrackTitle());
            mTvPlayCount.setText(StringUtil.formatNumberFriendly(track.getPlayCount()));
            mTvDuration.setText(mMSDateFormat.format(track.getDuration() * 1000));
            mTvCreateDate.setText(mYMDDateFormat.format(track.getCreatedAt()));
        }
    }
}
