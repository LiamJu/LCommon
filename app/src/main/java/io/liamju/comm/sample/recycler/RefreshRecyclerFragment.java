package io.liamju.comm.sample.recycler;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.liamju.comm.sample.interfaces.LceRecyclerView;
import io.liamju.comm.sample.interfaces.LceRefreshView;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public class RefreshRecyclerFragment<RV extends RecyclerView, RVA extends RecyclerView.Adapter>
        extends Fragment implements LceRefreshView, LceRecyclerView<RV, RVA> {

    @Override
    public void onListItemClick(RV l, View v, int position, long id) {

    }

    @Override
    public void setListAdapter(RVA adapter) {

    }

    @Override
    public void setSelection(int position) {

    }

    @Override
    public int getSelectedItemPosition() {
        return 0;
    }

    @Override
    public long getSelectedItemId() {
        return 0;
    }

    @Override
    public RV getListView() {
        return null;
    }

    @Override
    public void setEmptyText(CharSequence text) {

    }

    @Override
    public void setListShown(boolean shown) {

    }

    @Override
    public void setListShownNoAnimation(boolean shown) {

    }

    @Override
    public RVA getListAdapter() {
        return null;
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
