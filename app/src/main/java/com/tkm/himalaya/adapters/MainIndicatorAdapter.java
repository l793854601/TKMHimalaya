package com.tkm.himalaya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.tkm.himalaya.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * MainActivity中MagicIndicator数据适配器
 */
public class MainIndicatorAdapter extends CommonNavigatorAdapter {

    final private String[] mTitles;

    private OnIndicatorSelectedListener mIndicatorSelectedListener;

    /**
     * 构造方法
     * @param context
     */
    public MainIndicatorAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.main_indicator_titles);
    }

    /**
     * 指示器数量
     * @return
     */
    @Override
    public int getCount() {
        return mTitles.length;
    }

    /**
     * 获取当前指示器对应位置的控件
     * @param context
     * @param index
     * @return
     */
    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        SimplePagerTitleView titleView = new ColorTransitionPagerTitleView(context);
        titleView.setNormalColor(Color.GRAY);
        titleView.setSelectedColor(Color.WHITE);
        titleView.setTextSize(18);
        titleView.setText(mTitles[index]);
        titleView.setOnClickListener(view -> {
            if (mIndicatorSelectedListener != null) {
                mIndicatorSelectedListener.onIndicatorSelected(index);
            }
        });
        return titleView;
    }

    /**
     * 获取指示器下方的横线
     * @param context
     * @return
     */
    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setColors(Color.WHITE);
        return indicator;
    }

    /**
     * 设置Indicator点击监听器
     * @param indicatorSelectedListener
     */
    public void setIndicatorSelectedListener(OnIndicatorSelectedListener indicatorSelectedListener) {
        mIndicatorSelectedListener = indicatorSelectedListener;
    }


    public static interface OnIndicatorSelectedListener {
        void onIndicatorSelected(int position);
    }

}
