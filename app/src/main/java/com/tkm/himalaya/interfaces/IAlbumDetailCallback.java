package com.tkm.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailCallback {

    /**
     * 专辑详情列表加载成功
     * @param tracks
     */
    void onTrackListLoaded(List<Track> tracks);

    /**
     * Album加载成功
     * 此方法的意义在于，将Model提供给View进行刷新
     * @param album
     */
    void onAlbumLoaded(Album album);
}
