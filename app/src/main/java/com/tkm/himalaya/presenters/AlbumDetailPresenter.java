package com.tkm.himalaya.presenters;

import android.util.Log;

import com.tkm.himalaya.interfaces.IAlbumDetailCallback;
import com.tkm.himalaya.interfaces.IAlbumDetailPresenter;
import com.tkm.himalaya.utils.Constants;
import com.tkm.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.CommonTrackList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG = "AlbumDetailPresenter";

    public static AlbumDetailPresenter sInstance;

    private Album mAlbum;

    private List<IAlbumDetailCallback> mCallbacks = new ArrayList<>();

    private AlbumDetailPresenter() {

    }

    public static synchronized AlbumDetailPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new AlbumDetailPresenter();
        }
        return sInstance;
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int id, int page) {
        Map<String, String> query = new HashMap<>();
        query.put(DTransferConstants.ALBUM_ID, String.valueOf(id));
        query.put(DTransferConstants.SORT, "asc");
        query.put(DTransferConstants.PAGE, String.valueOf(page));
        query.put(DTransferConstants.PAGE_SIZE, String.valueOf(Constants.PAGE_SIZE_DEFAULT));
        CommonRequest.getTracks(query, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                LogUtil.d(TAG, "load track list success: " + trackList.getTracks().size());
                handleOnTrackListLoaded(trackList.getTracks());
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "load track list failed: " + i + ", message: " + s);

            }
        });
    }

    @Override
    public void registerViewCallback(IAlbumDetailCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }

        if (mAlbum != null) {
            callback.onAlbumLoaded(mAlbum);
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailCallback callback) {
        if (mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    /**
     * 设置当前专辑
     * @param album
     */
    public void setTargetAlbum(Album album) {
        mAlbum = album;
    }

    private void handleOnTrackListLoaded(List<Track> tracks) {
        if (mCallbacks.size() > 0) {
            for (IAlbumDetailCallback callback : mCallbacks) {
                callback.onTrackListLoaded(tracks);
            }
        }
    }
}
