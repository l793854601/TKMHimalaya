package com.tkm.himalaya.fragments;

import android.graphics.Rect;
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
import com.tkm.himalaya.interfaces.IRecommendCallback;
import com.tkm.himalaya.interfaces.IRecommendPresenter;
import com.tkm.himalaya.presenters.RecommendListPresenter;
import com.tkm.himalaya.utils.UIUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements IRecommendCallback {

    private IRecommendPresenter mPresenter;

    private RecommendListAdapter mAdapter;

    private RecyclerView mRv;

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    protected View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mRv.setAdapter(mAdapter);

        //  获取Presenter层对象
        mPresenter = RecommendListPresenter.getInstance();
        //  将View绑定到Presenter中
        mPresenter.registerViewCallback(this);
        //  Presenter获取数据
        mPresenter.getRecommendList();

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
        //  Presenter回调View刷新UI
        mAdapter.setList(result);
    }
}
