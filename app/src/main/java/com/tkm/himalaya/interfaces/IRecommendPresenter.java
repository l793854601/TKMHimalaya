package com.tkm.himalaya.interfaces;

import com.tkm.himalaya.base.IBasePresenter;

/**
 * 推荐
 * View 调用 Presenter 交互接口
 */
public interface IRecommendPresenter extends IBasePresenter<IRecommendCallback> {

    /**
     * 获取推荐内容
     */
    void getRecommendList();
}
