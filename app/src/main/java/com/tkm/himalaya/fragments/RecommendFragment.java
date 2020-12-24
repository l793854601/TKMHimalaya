package com.tkm.himalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tkm.himalaya.AlbumDetailActivity;
import com.tkm.himalaya.R;
import com.tkm.himalaya.adapters.RecommendListAdapter;
import com.tkm.himalaya.base.BaseFragment;
import com.tkm.himalaya.interfaces.IRecommendCallback;
import com.tkm.himalaya.interfaces.IRecommendPresenter;
import com.tkm.himalaya.presenters.AlbumDetailPresenter;
import com.tkm.himalaya.presenters.RecommendListPresenter;
import com.tkm.himalaya.utils.LogUtil;
import com.tkm.himalaya.views.UILoader;
import com.tkm.himalaya.utils.UIUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements IRecommendCallback, UILoader.OnRetryClickListener {

    private static final String TAG = "RecommendFragment";

    private IRecommendPresenter mPresenter;

    private RecommendListAdapter mAdapter;

    private UILoader mUILoader;

    private RecyclerView mRv;

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    protected View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        mUILoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(inflater, container);
            }
        };

        mUILoader.setRetryClickListener(this);

        //  获取Presenter层对象
        mPresenter = RecommendListPresenter.getInstance();
        //  将View绑定到Presenter中
        mPresenter.registerViewCallback(this);
        //  Presenter获取数据
        mPresenter.getRecommendList();

        //  避免重新加载，如果UILoader已经添加，则移除掉
        if (mUILoader.getParent() != null && mUILoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUILoader.getParent()).removeView(mUILoader);
        }

        return mUILoader;
    }

    private View createSuccessView(LayoutInflater inflater, ViewGroup container) {
        View contentView = inflater.inflate(R.layout.fragment_recommend, container, false);

        //  找到控件
        mRv = contentView.findViewById(R.id.rv);
        //  优化RecyclerView
        mRv.setHasFixedSize(true);

        //  设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRv.setLayoutManager(layoutManager);

        //  设置item上下左右边距
        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.px2dip(getContext(), 5);
                outRect.bottom = UIUtil.px2dip(getContext(), 5);
                outRect.left = UIUtil.px2dip(getContext(), 5);
                outRect.right = UIUtil.px2dip(getContext(), 5);
            }
        });

        //  设置数据适配器
        mAdapter = new RecommendListAdapter();
        //  设置item点击监听器
        mAdapter.setOnItemClickListener((position, album) -> {
            LogUtil.d(TAG, "setOnItemClickListener: " + position + ", title: " + album.getAlbumTitle());

            AlbumDetailPresenter.getInstance().setTargetAlbum(album);
            Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
            startActivity(intent);
        });
        mRv.setAdapter(mAdapter);

        //  返回
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //  取消Presenter与View的绑定关系，避免内存泄漏（绑定与解绑成对儿出现）
        if (mPresenter != null) {
            mPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        LogUtil.d(TAG, "onRecommendListLoaded: " + result.size());
        //  Presenter回调View刷新UI
        mAdapter.setList(result);
        mUILoader.setStatus(UILoader.UILoaderStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        LogUtil.d(TAG, "onNetworkError: ");
        mUILoader.setStatus(UILoader.UILoaderStatus.NETWORK_ERROR);
    }

    @Override
    public void onListEmpty() {
        LogUtil.d(TAG, "onListEmpty: ");
        mUILoader.setStatus(UILoader.UILoaderStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        LogUtil.d(TAG, "onLoading: ");
        mUILoader.setStatus(UILoader.UILoaderStatus.LOADING);
    }

    @Override
    public void onRetryClick() {
        mPresenter.getRecommendList();
    }
}
