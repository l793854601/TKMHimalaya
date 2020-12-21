package com.tkm.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * 推荐
 * Presenter 回调 View 接口
 */
public interface IRecommendCallback {

    /**
     * 获取推荐内容成功
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);
}
