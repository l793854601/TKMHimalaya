package com.tkm.himalaya.interfaces;

import com.tkm.himalaya.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailCallback> {

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
}
