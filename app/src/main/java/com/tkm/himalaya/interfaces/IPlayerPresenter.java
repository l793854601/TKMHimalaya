package com.tkm.himalaya.interfaces;

import com.tkm.himalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {

    /**
     * 播放器是否正在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 是否有上一首
     * @return
     */
    boolean hasPrevious();

    /**
     * 播放上一首
     */
    void playPrevious();

    /**
     * 是否有下一首
     * @return
     */
    boolean hasNext();

    /**
     * 播放下一首
     */
    void playNext();

    /**
     * 切换播放模式
     * @param mode
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 设置播放列表
     * @param list
     */
    void setPlayList(List<Track> list, int startIndex);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据索引播放
     * @param index
     */
    void playIndex(int index);

    /**
     * 改变播放进度
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 获取当前播放内容的标题
     * @return
     */
    String getCurrentTrackTitle();

    /**
     * 获取当前播放进度
     * @return
     */
    int getCurrentPlayProgress();

    /**
     * 获取当前播放总进度
     * @return
     */
    int getCurrentPlayDuration();
}
