package com.tkm.himalaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tkm.himalaya.adapters.AlbumDetailAdapter;
import com.tkm.himalaya.interfaces.IAlbumDetailCallback;
import com.tkm.himalaya.presenters.AlbumDetailPresenter;
import com.tkm.himalaya.utils.ImageBlur;
import com.tkm.himalaya.utils.LogUtil;
import com.tkm.himalaya.utils.UIUtil;
import com.tkm.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity implements IAlbumDetailCallback {

    private static final String TAG = "AlbumDetailActivity";

    private AlbumDetailPresenter mAlbumDetailPresenter;

    private AlbumDetailAdapter mAdapter;

    private int mCurrentPage = 1;

    private int mCurrentAlbumId = -1;

    /**
     * 大封面
     */
    private ImageView mIvCover;

    /**
     * 作者头像
     */
    private ImageView mIvAvatar;

    /**
     * 标题
     */
    private TextView mTvTitle;

    /**
     * 作者
     */
    private TextView mTvAuthor;

    /**
     * 订阅
     */
    private TextView mTvSubscription;

    /**
     * 播放/暂停
     */
    private ImageView mIvPlayControl;

    /**
     * 选集
     */
    private ImageView mIvArrow;

    /**
     * 选集列表
     */
    private RecyclerView mRvList;

    /**
     * 列表容器
     */
    private FrameLayout mFlListContainer;

    /**
     * 网络加载UI
     */
    private UILoader mUILoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        //  设置状态栏为透明，并且使布局向上延展到状态栏顶部
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        initView();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.unregisterViewCallback(this);
        }
    }

    private void initView() {

        if (mUILoader == null) {
            mUILoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };

            mUILoader.setRetryClickListener(new UILoader.OnRetryClickListener() {
                @Override
                public void onRetryClick() {
                    getAlbumDetail(mCurrentAlbumId);
                }
            });
        }

        mIvCover = findViewById(R.id.iv_cover);
        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvTitle = findViewById(R.id.tv_title);
        mTvAuthor = findViewById(R.id.tv_author);
        mTvSubscription = findViewById(R.id.tv_subscription);
        mIvPlayControl = findViewById(R.id.iv_play_control);
        mIvArrow = findViewById(R.id.iv_arrow);
        mFlListContainer = findViewById(R.id.fl_list_container);

        mFlListContainer.removeAllViews();
        mFlListContainer.addView(mUILoader);
    }

    private void initPresenter() {
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }

    private View createSuccessView(ViewGroup container) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.fragment_album_detail_list, container, false);

        mRvList = view.findViewById(R.id.rv_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);

        mRvList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.px2dip(AlbumDetailActivity.this, 5);
                outRect.bottom = UIUtil.px2dip(AlbumDetailActivity.this, 5);
                outRect.left = UIUtil.px2dip(AlbumDetailActivity.this, 5);
                outRect.right = UIUtil.px2dip(AlbumDetailActivity.this, 5);
            }
        });

        mAdapter = new AlbumDetailAdapter();
        mAdapter.setOnItemClickListener((position, track) -> {
            LogUtil.d(TAG, "setOnItemClick: " + position + ", track: " + track.getTrackTitle());
            Intent intent = new Intent(AlbumDetailActivity.this, TrackPlayerActivity.class);
            startActivity(intent);
        });
        mRvList.setAdapter(mAdapter);

        return view;
    }

    private void getAlbumDetail(int id) {
        mUILoader.setStatus(UILoader.UILoaderStatus.LOADING);
        mAlbumDetailPresenter.getAlbumDetail(id, mCurrentPage);
    }

    @Override
    public void onTrackListLoaded(List<Track> tracks) {
        if (tracks == null || tracks.size() == 0) {
            mUILoader.setStatus(UILoader.UILoaderStatus.EMPTY);
        } else {
            mAdapter.setData(tracks);
            mUILoader.setStatus(UILoader.UILoaderStatus.SUCCESS);
        }
    }

    @Override
    public void onAlbumLoaded(Album album) {

        mCurrentAlbumId = (int) album.getId();

        if (mIvCover != null) {
            Picasso.get().load(album.getCoverUrlLarge()).into(mIvCover, new Callback() {
                @Override
                public void onSuccess() {
                    LogUtil.d(TAG, "get cover success");
                    //  将封面高斯模糊处理，此方法一定要在ImageView有内容时调用
                    ImageBlur.makeBlur(mIvCover, AlbumDetailActivity.this);
                }

                @Override
                public void onError(Exception e) {
                    LogUtil.d(TAG, "get cover failed: " + e);
                }
            });
        }

        if (mIvAvatar != null) {
            Picasso.get().load(album.getAnnouncer().getAvatarUrl()).into(mIvAvatar);
        }

        if (mTvTitle != null) {
            mTvTitle.setText(album.getAlbumTitle());
        }

        if (mTvAuthor != null) {
            mTvAuthor.setText(album.getAnnouncer().getNickname());
        }

        //  加载Album详情
        getAlbumDetail((int) album.getId());
    }

    @Override
    public void onNetworkError(int errorCode, String errorMessage) {
        mUILoader.setStatus(UILoader.UILoaderStatus.NETWORK_ERROR);
    }
}