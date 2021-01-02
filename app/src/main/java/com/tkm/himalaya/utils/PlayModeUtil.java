package com.tkm.himalaya.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tkm.himalaya.R;
import com.tkm.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public class PlayModeUtil {
    public static int getPlayModeRes(XmPlayListControl.PlayMode playMode) {
        /*
           单曲循环：PLAY_MODEL_SINGLE_LOOP,
           列表播放：PLAY_MODEL_LIST,
           列表循环：PLAY_MODEL_LIST_LOOP,
           随机播放：PLAY_MODEL_RANDOM;
         */
        int playModeResId = -1;

        switch (playMode) {
            case PLAY_MODEL_SINGLE_LOOP:
                playModeResId = R.mipmap.play_mode_loop_one_normal;
                break;
            case PLAY_MODEL_LIST:
                playModeResId = R.mipmap.sort_descending_normal;
                break;
            case PLAY_MODEL_LIST_LOOP:
                playModeResId = R.mipmap.play_mode_loop_normal;
                break;
            case PLAY_MODEL_RANDOM:
                playModeResId = R.mipmap.play_mode_random_normal;
                break;
            default:
                break;
        }

        return playModeResId;
    }

    public static String getPlayModeDesc(XmPlayListControl.PlayMode playMode) {
        /*
           单曲循环：PLAY_MODEL_SINGLE_LOOP,
           列表播放：PLAY_MODEL_LIST,
           列表循环：PLAY_MODEL_LIST_LOOP,
           随机播放：PLAY_MODEL_RANDOM;
         */
        String playModeDesc = null;

        switch (playMode) {
            case PLAY_MODEL_SINGLE_LOOP:
                playModeDesc = "单曲循环";
                break;
            case PLAY_MODEL_LIST:
                playModeDesc = "列表播放";
                break;
            case PLAY_MODEL_LIST_LOOP:
                playModeDesc = "列表循环";
                break;
            case PLAY_MODEL_RANDOM:
                playModeDesc = "随机播放";
                break;
            default:
                break;
        }
        return playModeDesc;
    }

    public static XmPlayListControl.PlayMode getNextPlayMode(XmPlayListControl.PlayMode currentMode) {
        /*
           单曲循环：PLAY_MODEL_SINGLE_LOOP,
           列表播放：PLAY_MODEL_LIST,
           列表循环：PLAY_MODEL_LIST_LOOP,
           随机播放：PLAY_MODEL_RANDOM;
         */
        if (currentMode == XmPlayListControl.PlayMode.PLAY_MODEL_LIST) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
        } else if (currentMode == XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
        } else if (currentMode == XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;
        } else if (currentMode == XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
        }
        //  默认返回PLAY_MODEL_LIST
        return XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
    }

    public static void savePlayModeLocal(XmPlayListControl.PlayMode playMode) {
        SharedPreferences.Editor editor = BaseApplication.
                getAppContext().
                getSharedPreferences(Constants.PLAY_MODEL, Context.MODE_MULTI_PROCESS).
                edit();
        editor.putString(Constants.PLAY_MODEL, playMode.name());
        editor.commit();
    }

    public static XmPlayListControl.PlayMode getLocalPlayMode() {
        SharedPreferences sp =  BaseApplication.
                getAppContext().
                getSharedPreferences(Constants.PLAY_MODEL, Context.MODE_MULTI_PROCESS);
        String playModeString = sp.getString(Constants.PLAY_MODEL, "PLAY_MODEL_LIST");
        /*
           单曲循环：PLAY_MODEL_SINGLE_LOOP,
           列表播放：PLAY_MODEL_LIST,
           列表循环：PLAY_MODEL_LIST_LOOP,
           随机播放：PLAY_MODEL_RANDOM;
         */
        if (playModeString.equals("PLAY_MODEL_SINGLE_LOOP")) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;
        }
        if (playModeString.equals("PLAY_MODEL_LIST")) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
        }
        if (playModeString.equals("PLAY_MODEL_SINGLE_LOOP")) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;
        }
        if (playModeString.equals("PLAY_MODEL_LIST_LOOP")) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;
        }
        if (playModeString.equals("PLAY_MODEL_RANDOM")) {
            return XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
        }
        //  默认返回PLAY_MODEL_LIST
        return XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
    }
}

