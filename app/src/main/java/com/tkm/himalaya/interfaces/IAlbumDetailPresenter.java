package com.tkm.himalaya.interfaces;

public interface IAlbumDetailPresenter {

    /**
     * 下拉刷新
     */
    void pullToRefresh();

    /**
     * 上拉加载更多
     */
    void loadMore();

    /**
     * 获取专辑详情（列表）
     * @param id
     * @param page
     */
    void getAlbumDetail(int id, int page);

    /**
     * 绑定View
     * @param callback
     */
    void registerViewCallback(IAlbumDetailCallback callback);

    /**
     * 解绑View
     * @param callback
     */
    void unregisterViewCallback(IAlbumDetailCallback callback);

}
