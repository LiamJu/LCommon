package io.liamju.comm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.liamju.comm.R;
import io.liamju.comm.view.LceRecyclerView;
import io.liamju.comm.view.LceRefreshView;
import io.liamju.comm.interfaces.OnItemListClickListener;
import io.liamju.comm.widget.DividerItemDecoration;

/**
 *
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public abstract class RefreshRecyclerFragment<RV extends RecyclerView, RVA extends RecyclerView.Adapter, M>
        extends Fragment implements LceRefreshView<M>, LceRecyclerView<RV, RVA> {

    private static final String TAG = RefreshRecyclerFragment.class.getSimpleName();

    static final int INTERNAL_EMPTY_ID = R.id.id_empty;
    static final int INTERNAL_PROGRESS_CONTAINER_ID = R.id.id_progress_container;
    /**
     * 这里的List容器是 {@link SwipeRefreshLayout}
     */
    static final int INTERNAL_LIST_CONTAINER_ID = R.id.id_list_container;

    final private Handler mHandler = new Handler();

    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    final private OnItemListClickListener mOnItemListClickListener = new OnItemListClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            onListItemClick(mList, view, position, view.getId());
        }
    };

    RVA mAdapter;
    RV mList;
    LinearLayoutManager mLayoutManager;
//    SwipeRefreshLayout mRefresh;
    View mEmptyView;
    TextView mStandardEmptyView;
    View mProgressContainer;
    SwipeRefreshLayout mListContainer;
    CharSequence mEmptyText;
    boolean mListShown;

//    protected static final int STATE_NONE = 1;
//    protected static final int STATE_REFRESH = 2;
//    protected static final int STATE_LOAD_MORE = 3;
//
//    @Retention(RetentionPolicy.CLASS)
//    @IntDef({STATE_NONE, STATE_REFRESH, STATE_LOAD_MORE})
//    public @interface RefreshState {
//    }
//
//    protected
//    @RefreshState
//    int mState = STATE_NONE;

    public RefreshRecyclerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = getActivity();

        FrameLayout root = new FrameLayout(context);

        // ------------------------------------------------------------------

        LinearLayout pframe = new LinearLayout(context);
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(context, null,
                android.R.attr.progressBarStyleLarge);
        pframe.addView(progress, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        FrameLayout eframe = new FrameLayout(context);
        TextView tv = new TextView(context);
        tv.setId(INTERNAL_EMPTY_ID);
        tv.setGravity(Gravity.CENTER);
        eframe.addView(tv, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        root.addView(eframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        SwipeRefreshLayout lframe = new SwipeRefreshLayout(context);
        lframe.setId(INTERNAL_LIST_CONTAINER_ID);
        RV rv = initRecycler(context);
        rv.addItemDecoration(initItemDecoration());
        rv.setLayoutManager(mLayoutManager = getLayoutManager(context));
        rv.setId(android.R.id.list);
        lframe.addView(rv, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        root.addView(lframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setupRefreshListener(lframe, rv);

        // ------------------------------------------------------------------

        root.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }

    /**
     * Attach to list view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureList();
    }

    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mRequestFocus);
        mList = null;
        mListShown = false;
        mEmptyView = mProgressContainer = null;
        mListContainer = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    /**
     * 默认返回 {@link LinearLayoutManager}, 需要使用其他的 LayoutManger， 覆盖此方法即可
     *
     * @return 返回 {@link RecyclerView.LayoutManager}
     */
    protected LinearLayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
    }

    protected RV initRecycler(Context context) {
        return (RV) new RecyclerView(context);
    }

    @Override
    public void onListItemClick(RV l, View v, int position, long id) {

    }

    @Override
    public void setListAdapter(RVA adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mList != null) {
            mList.setAdapter(adapter);
            if (!mListShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }
        }
    }

    @Override
    public void setSelection(int position) {
        ensureList();
        throw new UnsupportedOperationException(TAG + "don't support setSelection");
    }

    @Override
    public int getSelectedItemPosition() {
        ensureList();
        throw new UnsupportedOperationException(TAG + "don't support getSelectedItemPosition");
    }

    @Override
    public long getSelectedItemId() {
        ensureList();
        throw new UnsupportedOperationException(TAG + "don't support getSelectedItemId");
    }

    @Override
    public RV getListView() {
        ensureList();
        return mList;
    }

    @Override
    public void setEmptyText(CharSequence text) {
        ensureList();
        if (mStandardEmptyView == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        mStandardEmptyView.setText(text);
        mEmptyText = text;
    }

    @Override
    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    @Override
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    @Override
    public RVA getListAdapter() {
        return mAdapter;
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (pullToRefresh) {
            setSwipeRefreshLoadingState();
        } else {
            setSwipeRefreshLoadedState();
        }
    }

    @Override
    public void showContent() {
        ensureList();
        throw new UnsupportedOperationException("Show content, please call setListShown(boolean)");
    }

//    @Override
//    public void loadData(boolean pullToRefresh) {
//
//    }

    /**
     * 为 {@link SwipeRefreshLayout} 设置监听器
     *
     * @param lframe
     * @param rv
     */
    protected void setupRefreshListener(SwipeRefreshLayout lframe, RV rv) {
        lframe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                showLoading(true);
                loadData(true);
            }
        });
    }

    /**
     * 设置顶部正在加载的状态
     */
    protected void setSwipeRefreshLoadingState() {
        ensureList();
        if (!mListContainer.isRefreshing()) {
            mListContainer.setRefreshing(true);
            // 防止多次重复刷新
            mListContainer.setEnabled(false);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void setSwipeRefreshLoadedState() {
        ensureList();
        if (mListContainer.isRefreshing()) {
            mListContainer.setRefreshing(false);
            mListContainer.setEnabled(true);
        }
    }

    private void setListShown(boolean shown, boolean animate) {
        ensureList();
        if (mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        }
    }

    protected void ensureList() {
        if (mList != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        if (root instanceof RecyclerView) { // 除非自定义布局，否则不会执行
            mList = (RV) root;
        } else {
            mStandardEmptyView = (TextView) root.findViewById(INTERNAL_EMPTY_ID);
            if (mStandardEmptyView == null) {
                mEmptyView = root.findViewById(android.R.id.empty);
            } else {
                mStandardEmptyView.setVisibility(View.GONE);
            }
            mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            mListContainer = (SwipeRefreshLayout) root.findViewById(INTERNAL_LIST_CONTAINER_ID);
            View rawListView = root.findViewById(android.R.id.list);
            if (!(rawListView instanceof RecyclerView)) {
                if (rawListView == null) {
                    throw new RuntimeException(
                            "Your content must have a RecyclerView whose id attribute is " +
                                    "'android.R.id.list'");
                }
                throw new RuntimeException(
                        "Content has view with id attribute 'android.R.id.list' "
                                + "that is not a ListView class");
            }
            mList = (RV) rawListView;
        }
        mListShown = true;
        if (mAdapter != null) {
            RVA adapter = mAdapter;
            mAdapter = null;
            setListAdapter(adapter);
        } else {
            // We are starting without an adapter, so assume we won't
            // have our data right away and start with the progress indicator.
            if (mProgressContainer != null) {
                setListShown(false, false);
            }
        }
        mHandler.post(mRequestFocus);
    }
}
