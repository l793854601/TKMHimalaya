package com.tkm.himalaya.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tkm.himalaya.R;

public class PlayListDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "PlayListDialogFragment";

    public static PlayListDialogFragment newInstance() {
        PlayListDialogFragment fragment = new PlayListDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list_dialog, container, false);
        return view;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }
}
