package com.tkm.himalaya.interfaces;

/**
 * 推荐
 * View 调用 Presenter 交互接口
 */
public interface IRecommendPresenter {

    /**
     * 获取推荐内容
     */
    void getRecommendList();

    /**
     * 绑定Presenter与View
     * @param callback
     */
    void registerViewCallback(IRecommendCallback callback);

    /**
     * 解绑Presenter与View
     * @param callback
     */
    void unregisterViewCallback(IRecommendCallback callback);
}
