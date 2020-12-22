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

    /**
     * 网络错误
     */
    void onNetworkError();

    /**
     * 数据为空
     */
    void onListEmpty();

    /**
     * 数据正在加载
     */
    void onLoading();
}
