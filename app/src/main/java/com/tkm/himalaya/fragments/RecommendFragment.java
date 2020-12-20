package com.tkm.himalaya.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tkm.himalaya.R;
import com.tkm.himalaya.adapters.RecommendListAdapter;
import com.tkm.himalaya.base.BaseFragment;
import com.tkm.himalaya.utils.Constants;
import com.tkm.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐
 */
public class RecommendFragment extends BaseFragment {

    private static final String TAG = "RecommendFragment";

    private RecommendListAdapter mAdapter;

    private RecyclerView mRv;

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    protected View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_recommend, container, false);

        mRv = contentView.findViewById(R.id.rv);
        mRv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRv.setLayoutManager(layoutManager);

        mAdapter = new RecommendListAdapter();
        mRv.setAdapter(mAdapter);

        getRecommendData();
        return contentView;
    }

    /**
     * 获取推荐数据
     */
    private void getRecommendData() {
        Map<String, String> params = new HashMap<>();
        params.put(DTransferConstants.LIKE_COUNT, String.valueOf(Constants.RECOMMEND_COUNT));
        CommonRequest.getGuessLikeAlbum(params, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "onSuccess: " + gussLikeAlbumList.getAlbumList().size());
                List<Album> albumList = gussLikeAlbumList.getAlbumList();
                updateRecommendUI(albumList);
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError: " + s);
            }
        });
    }

    /**
     * 更新UI
     * @param albumList
     */
    private void updateRecommendUI(List<Album> albumList) {
        mAdapter.setList(albumList);
    }
}
