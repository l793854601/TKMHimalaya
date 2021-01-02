package com.tkm.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tkm.himalaya.R;
import com.tkm.himalaya.adapters.PlayListAdapter;
import com.tkm.himalaya.base.BaseApplication;
import com.tkm.himalaya.utils.PlayModeUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayListPopupWindow extends PopupWindow {

    public interface PlayListActionListener {

        void onChangeNextPlayMode();

        void onChangePlayIndex(int playIndex);
    }

    private PlayListActionListener mPlayListActionListener;

    private PlayListAdapter mAdapter;

    private ImageView mIvPlayMode;

    private TextView mTvPlayMode;

    private RecyclerView mRv;

    private Button mBtnClose;

    public PlayListPopupWindow() {
        //  通过父类构造器，设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View contentView = LayoutInflater.from(BaseApplication.getAppContext())
                .inflate(R.layout.window_play_list, null, false);
        //  设置ContentView
        setContentView(contentView);
        //  设置Background（如果不设置，则setOutsideTouchable无效）
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  设置外部可点击消失
        setOutsideTouchable(true);
        //  设置Window进入/退出动画
        setAnimationStyle(R.style.pop_window_animation);

        initViews(contentView);
        initListeners();
    }

    private void initViews(View contentView) {
        mIvPlayMode = contentView.findViewById(R.id.iv_play_mode);
        mTvPlayMode = contentView.findViewById(R.id.tv_play_mode);
        mRv = contentView.findViewById(R.id.rv);
        mBtnClose = contentView.findViewById(R.id.btn_close);

        mRv.setHasFixedSize(true);
        //  设置播剧管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(contentView.getContext());
        mRv.setLayoutManager(layoutManager);
        //  设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(contentView.getContext(), DividerItemDecoration.VERTICAL);
        mRv.addItemDecoration(itemDecoration);
        //  设置数据适配器
        mAdapter = new PlayListAdapter();
        mRv.setAdapter(mAdapter);
    }

    private void initListeners() {

        mTvPlayMode.setOnClickListener(v -> {
            if (mPlayListActionListener != null) {
                mPlayListActionListener.onChangeNextPlayMode();
            }
        });

        mAdapter.setOnItemClickListener((position, track) -> {
            if (mPlayListActionListener != null) {
                mPlayListActionListener.onChangePlayIndex(position);
            }
            dismiss();
        });

        mBtnClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    public void setPlayListActionListener(PlayListActionListener playListActionListener) {
        mPlayListActionListener = playListActionListener;
    }

    public void setPlayList(List<Track> list) {
        setPlayList(list, -1);
    }

    public void setPlayIndex(int index) {
        if (mAdapter != null) {
            mAdapter.setIndex(index);
        }
        scrollToPosition(index);
    }

    public void setPlayList(List<Track> list, int index) {
        if (mAdapter != null) {
            mAdapter.setList(list, index);
        }
        scrollToPosition(index);
    }

    public void setCurrentPlayMode(XmPlayListControl.PlayMode playMode) {
        if (mIvPlayMode != null && mTvPlayMode != null) {
            mIvPlayMode.setImageDrawable(
                    BaseApplication
                            .getAppContext()
                            .getResources().
                            getDrawable(PlayModeUtil.getPlayModeRes(playMode)));
            mTvPlayMode.setText(PlayModeUtil.getPlayModeDesc(playMode));
        }
    }

    private void scrollToPosition(int position) {
        if (mRv != null && mAdapter != null && position < mAdapter.getItemCount()) {
            mRv.post(new Runnable() {
                @Override
                public void run() {
                    mRv.scrollToPosition(position);
                }
            });
        }
    }
}
