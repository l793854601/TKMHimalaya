package com.tkm.himalaya.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tkm.himalaya.R;

/**
 * 网络加载状态加载器
 */
public abstract class UILoader extends FrameLayout {

    /**
     * 网络加载的状态
     */
    public enum UILoaderStatus {
        LOADING, SUCCESS, NETWORK_ERROR, EMPTY, NONE
    }

    public interface OnRetryClickListener {
        void onRetryClick();
    }

    /**
     * 当前网络状态
     */
    private UILoaderStatus mStatus = UILoaderStatus.NONE;

    private OnRetryClickListener mRetryClickListener;

    /**
     * 网络加载View
     */
    private View mLoadingView;

    /**
     * 网络加载成功View
     */
    private View mSuccessView;

    /**
     * 网络加载失败View
     */
    private View mNetworkErrorView;

    /**
     * 数据为空View
     */
    private View mEmptyView;

    public UILoader(@NonNull Context context) {
        this(context, null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化UI
     */
    private void init() {
        //  初始化View
        mLoadingView = getLoadingView();
        mSuccessView = getSuccessView(this);
        mNetworkErrorView = getNetworkErrorView();
        mEmptyView = getEmptyView();
        //  添加View
        addView(mLoadingView);
        addView(mSuccessView);
        addView(mNetworkErrorView);
        addView(mEmptyView);
    }

    /**
     * 设置当前网络加载状态
     * @param status
     */
    public void setStatus(UILoaderStatus status) {
        mStatus = status;

        //  确保在主线程中更新UI
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    public void setRetryClickListener(OnRetryClickListener retryClickListener) {
        mRetryClickListener = retryClickListener;
    }

    private void switchUIByCurrentStatus() {
        //  加载中
        mLoadingView.setVisibility(mStatus == UILoaderStatus.LOADING ? VISIBLE : GONE);
        //  成功
        mSuccessView.setVisibility(mStatus == UILoaderStatus.SUCCESS ? VISIBLE : GONE);
        //  网络错误
        mNetworkErrorView.setVisibility(mStatus == UILoaderStatus.NETWORK_ERROR ? VISIBLE : GONE);
        //  数据为空
        mEmptyView.setVisibility(mStatus == UILoaderStatus.EMPTY ? VISIBLE : GONE);
    }

    /**
     * 正在加载
     * @return
     */
    protected View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view, this, false);
    }

    /**
     * 加载成功，真正显示数据的View
     * @param container
     * @return
     */
    protected abstract View getSuccessView(ViewGroup container);

    /**
     * 网络错误
     * @return
     */
    protected View getNetworkErrorView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_network_error_view, this, false);
        view.setOnClickListener(v -> {
            handleOnRetryClick();
        });
        return view;
    }

    /**
     * 数据为空
     * @return
     */
    protected View getEmptyView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
        view.setOnClickListener(v -> {
            handleOnRetryClick();
        });
        return view;
    }

    private void handleOnRetryClick() {
        if (mRetryClickListener != null) {
            mRetryClickListener.onRetryClick();
        }
    }
}
