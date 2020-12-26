package com.tkm.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerCallback {

    /**
     * 播放开始
     */
    void onPlayStart();

    /**
     * 播放暂停
     */
    void onPlayPause();

    /**
     * 播放停止
     */
    void onPlayStop();

    /**
     * 播放错误
     */
    void onPlayError();

    /**
     * 播放上一首
     */
    void onPlayPrevious(Track track);

    /**
     * 播放下一首
     */
    void onPlayNext(Track track);

    /**
     * 播放列表加载完成
     * @param list
     */
    void onTrackListLoaded(List<Track> list);

    /**
     * 播放模式改变
     * @param mode
     */
    void onPlayModeChanged(XmPlayListControl.PlayMode mode);

    /**
     * 播放进度改变
     * @param progress
     * @param total
     */
    void onPlayProgressChanged(long progress, long total);

    /**
     * 广告正在加载
     */
    void onAdLoading();

    /**
     * 广告加载完成
     */
    void onAdFinished();

    /**
     * Track更新
     * @param track
     * @param index
     */
    void onTrackUpdated(Track track, int index);
}
