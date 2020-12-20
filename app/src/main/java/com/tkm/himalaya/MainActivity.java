package com.tkm.himalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.tkm.himalaya.adapters.MainIndicatorAdapter;
import com.tkm.himalaya.adapters.MainViewPagerAdapter;
import com.tkm.himalaya.utils.LogUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MagicIndicator mIndicator;

    private ViewPager mVpContent;

    private ImageView mIvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mIndicator = findViewById(R.id.mi);
        mIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));

        //  设置MagicIndicator适配器
        MainIndicatorAdapter indicatorAdapter = new MainIndicatorAdapter(this);
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdjustMode(true);
        navigator.setAdapter(indicatorAdapter);

        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager());
        mVpContent = findViewById(R.id.vp_content);
        mVpContent.setOffscreenPageLimit(3);
        mVpContent.setAdapter(viewPagerAdapter);

        //  将MagicIndicator与ViewPager绑定在一起
        mIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mIndicator, mVpContent);

        //  设置设置MagicIndicator点击监听
        indicatorAdapter.setIndicatorSelectedListener(position -> {
            mVpContent.setCurrentItem(position);
        });

        mIvSearch = findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(view -> {
            LogUtil.d(TAG, "onSearchImageViewClicked");
        });
    }
}