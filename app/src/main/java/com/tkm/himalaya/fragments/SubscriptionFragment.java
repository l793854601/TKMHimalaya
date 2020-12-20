package com.tkm.himalaya.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tkm.himalaya.R;
import com.tkm.himalaya.base.BaseFragment;

/**
 * 订阅
 */
public class SubscriptionFragment extends BaseFragment {

    public static SubscriptionFragment newInstance() {
        SubscriptionFragment fragment = new SubscriptionFragment();
        return fragment;
    }

    @Override
    protected View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscription, container, false);
    }
}
