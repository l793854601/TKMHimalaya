package com.tkm.himalaya.presenters;

import com.tkm.himalaya.base.BaseApplication;
import com.tkm.himalaya.interfaces.IPlayerCallback;
import com.tkm.himalaya.interfaces.IPlayerPresenter;
import com.tkm.himalaya.utils.LogUtil;
import com.tkm.himalaya.utils.PlayModeUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class TrackPlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private static final String TAG = "TrackPlayerPresenter";

    private static TrackPlayerPresenter sInstance = null;

    private List<IPlayerCallback> mCallbacks = new ArrayList<>();

    private XmPlayerManager mPlayerManager;

    private boolean isPlayListSet = false;

    private TrackPlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //  添加播放器广告监听
        mPlayerManager.addAdsStatusListener(this);
        //  添加播放器状态监听
        mPlayerManager.addPlayerStatusListener(this);
    }

    public static synchronized TrackPlayerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new TrackPlayerPresenter();
        }
        return sInstance;
    }

    public void playOrPause() {
        //  如果正在播放广告，则不处理播放/暂停事件
        if (mPlayerManager.isAdPlaying()) return;

        if (mPlayerManager.isPlaying()) {
            //  如果正在播放，则暂停
            mPlayerManager.pause();
        } else {
            //  否则，就播放
            mPlayerManager.play();
        }
    }

    @Override
    public boolean isPlaying() {
        return mPlayerManager.isPlaying();
    }

    @Override
    public void play() {
        if (!isPlayListSet) return;
        mPlayerManager.play();
        //  播放时就回调一次，保证第一次进入播放界面，仍然可以正常获取标题
        PlayableModel playableModel =mPlayerManager.getCurrSound();
        if (playableModel instanceof Track) {
            Track track = (Track) playableModel;
            LogUtil.d(TAG, "onSoundSwitch: " + track.getTrackTitle());

            if (mCallbacks.size() > 0) {
                for (IPlayerCallback callback : mCallbacks) {
                    callback.onTrackUpdated(track, mPlayerManager.getCurrentIndex());
                }
            }
        }
    }

    @Override
    public void pause() {
        mPlayerManager.pause();
    }

    @Override
    public void stop() {
        mPlayerManager.stop();
    }

    @Override
    public boolean hasPrevious() {
        return mPlayerManager.hasPreSound();
    }

    @Override
    public void playPrevious() {
        if (!mPlayerManager.hasPreSound()) return;
        mPlayerManager.stop();
        mPlayerManager.playPre();
    }

    @Override
    public boolean hasNext() {
        return mPlayerManager.hasNextSound();
    }

    @Override
    public void playNext() {
        if (!mPlayerManager.hasNextSound()) return;
        mPlayerManager.stop();
        mPlayerManager.playNext();
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        mPlayerManager.setPlayMode(mode);
        PlayModeUtil.savePlayModeLocal(mode);
        if (mCallbacks.size() > 0) {
            for (IPlayerCallback callback : mCallbacks) {
                callback.onPlayModeChanged(mode);
            }
        }
    }

    @Override
    public XmPlayListControl.PlayMode getCurrentPlayMode() {
        return mPlayerManager.getPlayMode();
    }

    @Override
    public XmPlayListControl.PlayMode getNextPlayMode() {
        /*
           单曲循环：PLAY_MODEL_SINGLE_LOOP,
           列表播放：PLAY_MODEL_LIST,
           列表循环：PLAY_MODEL_LIST_LOOP,
           随机播放：PLAY_MODEL_RANDOM;
         */
        XmPlayListControl.PlayMode currentMode = mPlayerManager.getPlayMode();
        return PlayModeUtil.getNextPlayMode(currentMode);
    }

    @Override
    public void setPlayList(List<Track> list, int startIndex) {
        isPlayListSet = true;
        mPlayerManager.setPlayList(list, startIndex);
    }

    @Override
    public void getPlayList() {
        List<Track> playList = mPlayerManager.getPlayList();
        if (mCallbacks.size() > 0) {
            for (IPlayerCallback callback : mCallbacks) {
                callback.onTrackListLoaded(playList);
            }
        }
    }

    @Override
    public List<Track> getCurrentPlayList() {
        List<Track> result = new ArrayList<>();
        result.addAll(mPlayerManager.getPlayList());
        return result;
    }

    @Override
    public int getPlayingIndexInCurrentPlayList() {
        return mPlayerManager.getCurrentIndex();
    }

    @Override
    public void playIndex(int index) {
        if (mPlayerManager.isPlaying()) {
            mPlayerManager.stop();
        }

        //  越界判断
        if (index >= mPlayerManager.getPlayList().size()) {
            return;
        }
        mPlayerManager.play(index);
    }

    @Override
    public void seekTo(int progress) {
        mPlayerManager.seekTo(progress);
    }

    @Override
    public String getCurrentTrackTitle() {
        PlayableModel playableModel = mPlayerManager.getCurrSound();
        if (playableModel != null && playableModel instanceof Track) {
            Track track = (Track) playableModel;
            return track.getTrackTitle();
        }
        return "";
    }

    @Override
    public int getCurrentPlayProgress() {
        return mPlayerManager.getPlayCurrPositon();
    }

    @Override
    public int getCurrentPlayDuration() {
        return mPlayerManager.getDuration();
    }

    @Override
    public void registerViewCallback(IPlayerCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IPlayerCallback callback) {
        if (mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    //********************************IXmAdsStatusListener广告相关回调 start**********************************
    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(TAG, "onStartGetAdsInfo: ");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(TAG, "onGetAdsInfo: " + advertisList.getMsg());

    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(TAG, "onAdsStartBuffering: ");
    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(TAG, "onAdsStopBuffering: ");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        LogUtil.d(TAG, "onStartPlayAds: " + advertis.getLinkUrl() + ", " + i);
    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(TAG, "onCompletePlayAds: ");
    }

    @Override
    public void onError(int i, int i1) {
        LogUtil.d(TAG, "onError: " + i + ", " + i1);
    }
    //********************************IXmAdsStatusListener广告相关回调 end**********************************

    //********************************IXmPlayerStatusListener播放器状态相关回调 start**********************************
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG, "onPlayStart");
        if (mCallbacks.size() > 0) {
            for (IPlayerCallback callback : mCallbacks) {
                callback.onPlayStart();
            }
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG, "onPlayPause");
        if (mCallbacks.size() > 0) {
            for (IPlayerCallback callback : mCallbacks) {
                callback.onPlayPause();
            }
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG, "onPlayStop");
        if (mCallbacks.size() > 0) {
            for (IPlayerCallback callback : mCallbacks) {
                callback.onPlayStop();
            }
        }
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG, "onSoundPlayComplete");
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG, "onSoundPrepared");
        if (mPlayerManager.getPlayerStatus() == PlayerConstants.STATE_PREPARED) {
            //  手动设置播放模式
            switchPlayMode(PlayModeUtil.getLocalPlayMode());
            mPlayerManager.play();
        }
    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        LogUtil.d(TAG, "onSoundSwitch: " );

        if (playableModel1 instanceof Track) {
            Track track = (Track) playableModel1;
            LogUtil.d(TAG, "onSoundSwitch: " + track.getTrackTitle());

            if (mCallbacks.size() > 0) {
                for (IPlayerCallback callback : mCallbacks) {
                    callback.onTrackUpdated(track, mPlayerManager.getCurrentIndex());
                }
            }
        }
    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG, "onBufferingStart");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG, "onBufferingStop");
    }

    @Override
    public void onBufferProgress(int i) {
        LogUtil.d(TAG, "onBufferProgress: " + i);
    }

    @Override
    public void onPlayProgress(int i, int i1) {
        LogUtil.d(TAG, "onPlayProgress: " + i + ", " + i1);
        if (mCallbacks.size() > 0) {
            for (IPlayerCallback callback : mCallbacks) {
                callback.onPlayProgressChanged(i, i1);
            }
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG, "onError: " + e);
        return false;
    }
    //********************************IXmPlayerStatusListener播放器状态相关回调 end**********************************
}
