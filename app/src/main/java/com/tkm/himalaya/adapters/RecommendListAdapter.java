package com.tkm.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tkm.himalaya.R;
import com.tkm.himalaya.utils.StringUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, Album album);
    }

    private List<Album> mList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommend, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = mList.get(position);
        holder.bindHolder(album);

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, album);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<Album> albumList) {
        mList.clear();
        mList.addAll(albumList);
        notifyDataSetChanged();
    }

    public void addList(List<Album> albumList) {
        mList.addAll(albumList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvBanner;
        private TextView mTvTitle;
        private TextView mTvInfo;
        private TextView mTvPlayCount;
        private TextView mTvEpisode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View itemView) {
            mIvBanner = itemView.findViewById(R.id.iv_banner);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvInfo = itemView.findViewById(R.id.tv_info);
            mTvPlayCount = itemView.findViewById(R.id.tv_play_count);
            mTvEpisode = itemView.findViewById(R.id.tv_episode_count);
        }

        public void bindHolder(Album album) {
            mTvTitle.setText(album.getAlbumTitle());
            mTvInfo.setText(album.getAlbumIntro());
            mTvPlayCount.setText(StringUtil.formatNumberFriendly(album.getPlayCount()));
            mTvEpisode.setText(album.getIncludeTrackCount() + "集");

            Picasso.get()
                    .load(album.getCoverUrlLarge())
                    .into(mIvBanner);
        }
    }
}
