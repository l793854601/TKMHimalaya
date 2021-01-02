package com.tkm.himalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tkm.himalaya.adapters.TrackPlayerPagerAdapter;
import com.tkm.himalaya.interfaces.IPlayerCallback;
import com.tkm.himalaya.presenters.TrackPlayerPresenter;
import com.tkm.himalaya.utils.LogUtil;
import com.tkm.himalaya.utils.PlayModeUtil;
import com.tkm.himalaya.views.PlayListPopupWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class TrackPlayerActivity extends AppCompatActivity implements IPlayerCallback {

    private static final String TAG = "TrackPlayerActivity";

    private TrackPlayerPresenter mPlayerPresenter = TrackPlayerPresenter.getInstance();

    private SimpleDateFormat mHMSDateFormat = new SimpleDateFormat("hh:mm:ss");

    private SimpleDateFormat mMSDateFormat = new SimpleDateFormat("mm:ss");

    private int mCurrentProgress = 0;

    private int mCurrentPage = -1;

    private boolean mIsUserTouchProgress = false;

    private TrackPlayerPagerAdapter mPlayerPagerAdapter;

    private ValueAnimator mWindowPopInAnimator;

    private ValueAnimator mWindowPopOutAnimator;


    /**
     * 标题
     */
    private TextView mTvTitle;

    /**
     * ViewPager
     */
    private ViewPager mVp;

    /**
     * 开始时间
     */
    private TextView mTvStartTime;

    /**
     * 结束时间
     */
    private TextView mTvEndTime;

    /**
     * 播放进度
     */
    private SeekBar mSbProgress;

    /**
     * 播放模式
     */
    private ImageView mIvPlayMode;

    /**
     * 上一曲
     */
    private ImageView mIvPrevious;

    /**
     * 播放/暂停
     */
    private ImageView mIvPlayOrPause;

    /**
     * 下一曲
     */
    private ImageView mIvNext;

    /**
     * 播放列表
     */
    private ImageView mIvList;

    private PlayListPopupWindow mPlayListPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        mPlayerPresenter.registerViewCallback(this);

        initViews();
        initEvents();
        initAnimators();
        resetPlayStatusUI();

        mPlayerPresenter.getPlayList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updatePlayStatusUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerPresenter.unregisterViewCallback(this);

        //  如果此时Window正在显示，则手动关闭，防止内存泄露
        dismissPopupWindow();
    }

    @Override
    public void onBackPressed() {
        if (!dismissPopupWindow()) {
            super.onBackPressed();
        }
    }

    private void initViews() {
        mTvTitle = findViewById(R.id.tv_title);
        mVp = findViewById(R.id.vp);
        mTvStartTime = findViewById(R.id.tv_start_time);
        mTvEndTime = findViewById(R.id.tv_end_time);
        mSbProgress = findViewById(R.id.sb_progress);
        mIvPlayMode = findViewById(R.id.iv_play_mode);
        mIvPrevious = findViewById(R.id.iv_previous);
        mIvPlayOrPause = findViewById(R.id.iv_play_or_pause);
        mIvNext = findViewById(R.id.iv_next);
        mIvList = findViewById(R.id.iv_list);

        mPlayListPopupWindow = new PlayListPopupWindow();

        mPlayerPagerAdapter = new TrackPlayerPagerAdapter();
        mVp.setAdapter(mPlayerPagerAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.d(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.d(TAG, "onPageSelected: " + position);
                mCurrentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.d(TAG, "onPageScrollStateChanged: " + state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //  停止滑动时，才播放，避免卡顿
                    mPlayerPresenter.playIndex(mCurrentPage);
                }
            }
        });
    }

    private void initEvents() {
        //  上一曲点击事件
        mIvPrevious.setOnClickListener(v -> {
            if (!mPlayerPresenter.hasPrevious()) {
                Toast.makeText(TrackPlayerActivity.this, "没有上一曲", Toast.LENGTH_SHORT).show();
                return;
            }
            resetPlayStatusUI();
            mPlayerPresenter.playPrevious();
        });
        //  播放/暂停点击事件
        mIvPlayOrPause.setOnClickListener(v -> {
            mPlayerPresenter.playOrPause();
        });
        //  下一曲点击事件
        mIvNext.setOnClickListener(v -> {
            if (!mPlayerPresenter.hasNext()) {
                Toast.makeText(TrackPlayerActivity.this, "没有下一曲", Toast.LENGTH_SHORT).show();
                return;
            }
            resetPlayStatusUI();
            mPlayerPresenter.playNext();
        });
        //  进度条事件
        mSbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //  如果是用户手动改变进度，则保存被改变的进度
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //  标记用户正在手动改变播放进度
                mIsUserTouchProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  标记用户结束手动改变播放进度
                mIsUserTouchProgress = false;
                //  手动更新播放进度
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });
        //  播放模式点击事件
        mIvPlayMode.setOnClickListener(v -> {
            XmPlayListControl.PlayMode nextPlayMode = mPlayerPresenter.getNextPlayMode();
            mPlayerPresenter.switchPlayMode(nextPlayMode);
            showPlayModeToast(nextPlayMode);
        });
        //  播放列表点击事件
        mIvList.setOnClickListener(v -> {
            //  TODO: 先用PopupWindow实现，日后改为BottomSheetFragment实现
            showPopupWindow(v);
        });

        //  PopupWindow消失监听
        mPlayListPopupWindow.setOnDismissListener(() -> {
            LogUtil.d(TAG, "window dismiss");
            //  恢复Window的alpha
            mWindowPopOutAnimator.start();
        });

        //  PopupWindow内部交互事件
        mPlayListPopupWindow.setPlayListActionListener(new PlayListPopupWindow.PlayListActionListener() {
            @Override
            public void onChangeNextPlayMode() {
                XmPlayListControl.PlayMode nextPlayMode = mPlayerPresenter.getNextPlayMode();
                mPlayerPresenter.switchPlayMode(nextPlayMode);
            }

            @Override
            public void onChangePlayIndex(int playIndex) {
                //  选中的如果是当前正在播放的内容，则不处理
                if (playIndex == mPlayerPresenter.getPlayingIndexInCurrentPlayList()) return;
                mPlayerPresenter.playIndex(playIndex);
            }
        });
    }

    private void initAnimators() {
        mWindowPopInAnimator = ValueAnimator.ofFloat(1, 0.8f);
        mWindowPopInAnimator.addUpdateListener(animator -> {
            float alpha = (float) animator.getAnimatedValue();
            updateWindowAlpha(alpha);
        });

        mWindowPopOutAnimator = ValueAnimator.ofFloat(0.8f, 1);
        mWindowPopOutAnimator.addUpdateListener(animator -> {
            float alpha = (float) animator.getAnimatedValue();
            updateWindowAlpha(alpha);
        });
    }

    private boolean dismissPopupWindow() {
        if (mPlayListPopupWindow.isShowing()) {
            mPlayListPopupWindow.dismiss();
            mWindowPopOutAnimator.start();
            return true;
        }
        return false;
    }

    private void showPopupWindow(View view) {
        //  设置数据
        List<Track> playList = mPlayerPresenter.getCurrentPlayList();
        int playIndex = mPlayerPresenter.getPlayingIndexInCurrentPlayList();
        mPlayListPopupWindow.setPlayList(playList, playIndex);
        //  设置循环模式
        mPlayListPopupWindow.setCurrentPlayMode(mPlayerPresenter.getCurrentPlayMode());
        //  展示弹框
        mPlayListPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //  修改Window的alpha（动画）
        mWindowPopInAnimator.start();
    }

    private void updateWindowAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }

    private void resetPlayStatusUI() {
        mTvTitle.setText("");
        mTvStartTime.setText("00:00");
        mTvEndTime.setText("00:00");
        mSbProgress.setMax(0);
        mSbProgress.setProgress(0);
        mIvPrevious.setImageDrawable(getResources().getDrawable(R.mipmap.previous_normal));
        mIvPrevious.setClickable(true);
        mIvNext.setImageDrawable(getResources().getDrawable(R.mipmap.next_normal));
        mIvNext.setClickable(true);
    }

    private void updatePlayStatusUI() {
        mIvPlayOrPause.setSelected(mPlayerPresenter.isPlaying());
        mTvTitle.setText(mPlayerPresenter.getCurrentTrackTitle());

        int total = mPlayerPresenter.getCurrentPlayDuration();
        int progress = mPlayerPresenter.getCurrentPlayProgress();
        updatePlayProgressUI(progress, total);

        updatePlayModeUI(mPlayerPresenter.getCurrentPlayMode());
    }

    private void updatePlayProgressUI(int progress, int total) {
        //  更新播放进度/总进度
        mSbProgress.setMax(total);
        mSbProgress.setProgress(progress); //  此处progress返回的是毫秒
        //  更新播放时长
        if (total > 60 * 60 * 1000) {
            //  超过一小时，显示时分秒
            mTvEndTime.setText(mHMSDateFormat.format(total));
            mTvStartTime.setText(mHMSDateFormat.format(progress));
        } else {
            //  否则显示分秒
            mTvEndTime.setText(mMSDateFormat.format(total));
            mTvStartTime.setText(mMSDateFormat.format(progress));
        }
    }

    private void updatePlayModeUI(XmPlayListControl.PlayMode playMode) {
        int playModeResId = PlayModeUtil.getPlayModeRes(playMode);
        if (playModeResId != -1) {
            mIvPlayMode.setImageDrawable(getResources().getDrawable(playModeResId));
        }

        if (mPlayListPopupWindow.isShowing()) {
            mPlayListPopupWindow.setCurrentPlayMode(playMode);
        }
    }

    private void showPlayModeToast(XmPlayListControl.PlayMode playMode) {
        String playModeDesc = PlayModeUtil.getPlayModeDesc(playMode);
        if (!TextUtils.isEmpty(playModeDesc)) {
            Toast.makeText(this, playModeDesc, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPlayStart() {
        mIvPlayOrPause.setSelected(true);
    }

    @Override
    public void onPlayPause() {
        mIvPlayOrPause.setSelected(false);
    }

    @Override
    public void onPlayStop() {
        mIvPlayOrPause.setSelected(false);
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onPlayPrevious(Track track) {

    }

    @Override
    public void onPlayNext(Track track) {

    }

    @Override
    public void onTrackListLoaded(List<Track> list) {
        LogUtil.d(TAG, "onTrackListLoaded: " + list);
        mPlayerPagerAdapter.setList(list);
    }

    @Override
    public void onPlayModeChanged(XmPlayListControl.PlayMode mode) {
        updatePlayModeUI(mode);
    }

    @Override
    public void onPlayProgressChanged(long progress, long total) {
        //  如果不是用户手动触发的，才会更新进度
        if (!mIsUserTouchProgress) {
            updatePlayProgressUI((int) progress, (int) total);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdated(Track track, int index) {
        mTvTitle.setText(track.getTrackTitle());

        if (index != mCurrentPage) {
            mVp.setCurrentItem(index, false);
            mCurrentPage = index;
        }

        if (mPlayListPopupWindow.isShowing()) {
            mPlayListPopupWindow.setPlayIndex(index);
        }
    }
}