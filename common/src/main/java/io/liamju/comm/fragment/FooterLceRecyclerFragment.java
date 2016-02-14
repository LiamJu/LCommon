package io.liamju.comm.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.liamju.comm.adapter.BaseFooterAdapter;
import io.liamju.comm.view.FooterView;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public abstract class FooterLceRecyclerFragment<RV extends RecyclerView, RVA extends BaseFooterAdapter<D>, D> extends RefreshRecyclerFragment<RV, RVA, D> {
    private static final String TAG = FooterLceRecyclerFragment.class.getSimpleName();

    private int lastVisibleItem;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(false);
    }

    /**
     * 为 {@link RecyclerView} 添加监听器
     *
     * @param lframe
     * @param rv
     */
    @Override
    protected void setupRefreshListener(SwipeRefreshLayout lframe, RV rv) {
        super.setupRefreshListener(lframe, rv);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (FooterView.LOADING == mAdapter.getFooterState()) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mAdapter.setFooterState(FooterView.LOADING);
                    loadData(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem =
                        mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
