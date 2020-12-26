package com.tkm.himalaya.base;

public interface IBasePresenter<T> {

    /**
     * View
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册ViewCallBack
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
