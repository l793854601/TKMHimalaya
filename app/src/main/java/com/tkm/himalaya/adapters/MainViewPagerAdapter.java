package com.tkm.himalaya.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tkm.himalaya.fragments.HistoryFragment;
import com.tkm.himalaya.fragments.RecommendFragment;
import com.tkm.himalaya.fragments.SubscriptionFragment;

/**
 * MainActivity中ViewPager的数据适配器
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * 推荐
     */
    private static final int RECOMMEND_FRAGMENT_INDEX = 0;

    /**
     * 订阅
     */
    private static final int SUBSCRIPTION_FRAGMENT_INDEX = 1;

    /**
     * 历史
     */
    private static final int HISTORY_FRAGMENT_INDEX = 2;

    /**
     * 上下文
     */
    private final Context mContext;

    /**
     * 构造方法
     * @param context
     * @param fm
     */
    public MainViewPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * 返回对应位置的Fragment
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == RECOMMEND_FRAGMENT_INDEX) {
            //  推荐
            return RecommendFragment.newInstance();
        }
        else if (position == SUBSCRIPTION_FRAGMENT_INDEX) {
            //  订阅
            return SubscriptionFragment.newInstance();
        }
        else if (position == HISTORY_FRAGMENT_INDEX) {
            //  历史
            return HistoryFragment.newInstance();
        }

        return null;
    }

    /**
     * 返回Fragment数量
     * @return
     */
    @Override
    public int getCount() {
        return 3;
    }
}
