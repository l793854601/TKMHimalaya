package com.tkm.himalaya.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.tkm.himalaya.R;
import com.tkm.himalaya.utils.LogUtil;

public class LoadingView extends AppCompatImageView {

    private static final String TAG = "LoadingView";

    private ObjectAnimator mAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(1250);
    }

    /**
     * 当ViewVisibility状态改变时调用
     * 可以在此方法中启动/停止动画
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            LogUtil.d(TAG, "LoadingView is VISIBLE");
            startLoadingAnimation();
        } else {
            LogUtil.d(TAG, "LoadingView is not INVISIBLE");
            stopLoadingAnimation();
        }
    }

    /**
     * View被添加到Window时调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * View从Window移除时调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void startLoadingAnimation() {
        LogUtil.d(TAG, "startLoadingAnimation");
        setRotation(0);
        mAnimator.start();
    }

    public void stopLoadingAnimation() {
        LogUtil.d(TAG, "stopLoadingAnimation");
        mAnimator.cancel();
    }
}
