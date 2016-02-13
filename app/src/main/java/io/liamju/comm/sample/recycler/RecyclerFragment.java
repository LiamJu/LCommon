/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.liamju.comm.sample.recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.liamju.comm.sample.R;
import io.liamju.comm.sample.interfaces.LceRecyclerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Static library support version of the framework's {@link android.app.ListFragment}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 */
public abstract class RecyclerFragment<RV extends RecyclerView, RVA extends RecyclerView.Adapter>
        extends Fragment implements LceRecyclerView<RV, RVA> {

    private static final int INTERNAL_EMPTY_ID = R.id.id_empty;
    private static final int INTERNAL_PROGRESS_CONTAINER_ID = R.id.id_progress_container;
    private static final int INTERNAL_LIST_CONTAINER_ID = R.id.id_list_container;

    final private Handler mHandler = new Handler();

    private OnItemListClickListener mOnItemListClickListener;

    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    public interface OnItemListClickListener {
        void onItemClick(View view, int position);
    }

    RVA mAdapter;
    RV mList;
    View mEmptyView;
    TextView mStandardEmptyView;
    View mProgressContainer;
    View mListContainer;
    CharSequence mEmptyText;
    boolean mListShown;

    public RecyclerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = getActivity();

        FrameLayout root = new FrameLayout(context);

        // -----------------------------------------

        LinearLayout pframe = new LinearLayout(context);
        pframe.setId(R.id.id_progress_container);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        pframe.addView(progress, new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        // ---------------------------------------------

        FrameLayout lframe = new FrameLayout(context);
        lframe.setId(R.id.id_list_container);

        TextView tv = new TextView(context);
        tv.setId(R.id.id_empty);
        tv.setGravity(Gravity.CENTER);
        lframe.addView(tv, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        RV rv = initRecycler(getActivity());
        rv.setId(android.R.id.list);
        rv.setLayoutManager(getLayoutManager());
        lframe.addView(rv, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        root.addView(lframe, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        // ------------------------------------------------------------

        root.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        return root;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected abstract RV initRecycler(Activity activity);

    /**
     * Attach to list view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        mEmptyView = mProgressContainer = mListContainer = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    @Override
    public void setListAdapter(RVA adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mList != null) {
            mList.setAdapter(adapter);
            // 切换 mListContainer 的 mList 和 mStandardEmptyView 的可见性
            showEmptyView(adapter != null);
            if (!mListShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }
        }
    }

    private void showEmptyView(boolean hadAdapter) {
        if (!hadAdapter) {
            mList.setVisibility(View.GONE);
            if (mStandardEmptyView != null) {
                mStandardEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
            }
        } else {
            mList.setVisibility(View.VISIBLE);
            if (mStandardEmptyView != null) {
                mStandardEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setSelection(int position) {
        ensureList();
        if ( mList.getLayoutManager() instanceof LinearLayoutManager) {
           ((LinearLayoutManager) mList.getLayoutManager()).scrollToPositionWithOffset(position, 0);
        } else {
            throw new UnsupportedOperationException("Only LinearLayoutManager support");
        }
    }

    @Override
    public int getSelectedItemPosition() {
        ensureList();
        throw new UnsupportedOperationException("getSelectedItemId");
    }

    @Override
    public long getSelectedItemId() {
        ensureList();
        throw new UnsupportedOperationException("getSelectedItemId");
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
        if (mEmptyText == null) {

        }
        mEmptyText = text;
    }

    @Override
    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    @Override
    public RVA getListAdapter() {
        return mAdapter;
    }

    @Override
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    public void setOnItemListClickListener(OnItemListClickListener onItemListClickListener) {
        mOnItemListClickListener = onItemListClickListener;
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
        if (root instanceof RecyclerView) {
            mList = (RV) root;
        } else { // onCreateView 默认实现
            mStandardEmptyView = (TextView) root.findViewById(INTERNAL_EMPTY_ID);
            if (mStandardEmptyView == null) {
                mEmptyView = root.findViewById(android.R.id.empty);
            } else { // 默认不为空
                mStandardEmptyView.setVisibility(View.GONE);
            }
            mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
            View rawListView = root.findViewById(android.R.id.list);
            if (!(rawListView instanceof RecyclerView)) {
                if (rawListView == null) {
                    throw new RuntimeException(
                            "Your content must have a ListView whose id attribute is " +
                                    "'android.R.id.list'");
                }
                throw new RuntimeException(
                        "Content has view with id attribute 'android.R.id.list' "
                                + "that is not a ListView class");
            }
            mList = (RV) rawListView;
            /*
            RecyclerView 不需要和 EmptyView绑定在一起，逻辑可以在 setListAdapter 中实现
            if (mEmptyView != null) {
                mList.setEmptyView(mEmptyView);
            } else if (mEmptyText != null) {
                mStandardEmptyView.setText(mEmptyText);
                mList.setEmptyView(mStandardEmptyView);
            }*/

            if (mEmptyText != null && mStandardEmptyView != null) {
                mStandardEmptyView.setText(mEmptyText);
            }
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
