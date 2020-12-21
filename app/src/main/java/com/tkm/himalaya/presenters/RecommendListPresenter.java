package com.tkm.himalaya.presenters;

import com.tkm.himalaya.interfaces.IRecommendCallback;
import com.tkm.himalaya.interfaces.IRecommendPresenter;
import com.tkm.himalaya.utils.Constants;
import com.tkm.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐 Presenter层
 */
public class RecommendListPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendListPresenter";

    private static RecommendListPresenter sInstance = null;

    private List<IRecommendCallback> mCallbacks = new ArrayList<>();

    private RecommendListPresenter() {

    }

    public static synchronized RecommendListPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new RecommendListPresenter();
        }
        return sInstance;
    }

    @Override
    public void getRecommendList() {
        Map<String, String> params = new HashMap<>();
        params.put(DTransferConstants.LIKE_COUNT, String.valueOf(Constants.RECOMMEND_COUNT));
        CommonRequest.getGuessLikeAlbum(params, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "onSuccess: " + gussLikeAlbumList.getAlbumList().size());
                List<Album> albumList = gussLikeAlbumList.getAlbumList();
                //  通知View层（IRecommendCallback），数据获取完成
                if (mCallbacks != null && mCallbacks.size() > 0) {
                    for (IRecommendCallback callback : mCallbacks) {
                        callback.onRecommendListLoaded(albumList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError: " + s);
            }
        });
    }

    @Override
    public void registerViewCallback(IRecommendCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IRecommendCallback callback) {
        if (mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }
}
