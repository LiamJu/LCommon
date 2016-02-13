package io.liamju.comm.sample.recycler;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.liamju.comm.adapter.BaseFooterAdapter;
import io.liamju.comm.fragment.FooterLceRecyclerFragment;
import io.liamju.comm.fragment.RefreshRecyclerFragment;
import io.liamju.comm.sample.R;
import io.liamju.comm.sample.dummy.DummyContent;
import io.liamju.comm.view.FooterView;

/**
 * 为了使模板类更具通用性,实现一个{@link RefreshRecyclerFragment}的子类。
 * 功能：给 {@link android.support.v7.widget.RecyclerView.Adapter}末尾添加一个 FootView，
 * 具体的实现可见 {@link io.liamju.comm.adapter.BaseFooterAdapter}
 *
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public class RefreshLoadMoreRecyclerFragment
        extends FooterLceRecyclerFragment<RecyclerView, RefreshLoadMoreRecyclerFragment.Adapter, DummyContent.DummyItem> {

    int mCurrentPage = 0;
    static final int PAGE_SIZE = 15;

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(DummyContent.DummyItem data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (pullToRefresh) {
            setSwipeRefreshLoadingState();
            mCurrentPage = 0;
        }
        new ItemTask().execute();
    }

    private class ItemTask extends AsyncTask<Void, Void, List<DummyContent.DummyItem>> {

        public ItemTask() {

        }

        @Override
        protected List<DummyContent.DummyItem> doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int first = mCurrentPage * PAGE_SIZE;
            int end = (mCurrentPage + 1) * PAGE_SIZE - 1;
            List<DummyContent.DummyItem> items = DummyContent.ITEMS.subList(first, end);
            return items;
        }

        @Override
        protected void onPostExecute(List<DummyContent.DummyItem> dummyItems) {
            super.onPostExecute(dummyItems);
            if (mCurrentPage == 0) {
                setListAdapter(new Adapter());
            }
            mCurrentPage++;
            getListAdapter().addItems(dummyItems);
        }
    }

    public static class Adapter extends BaseFooterAdapter<DummyContent.DummyItem> {

        @Override
        protected RecyclerView.ViewHolder onCreateOtherViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, null);
            return new ViewHolder(view);
        }

        @Override
        protected void onBindOtherViewHolder(RecyclerView.ViewHolder holder, int position) {
            DummyContent.DummyItem item = getItem(position);
            if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).id.setText(item.id);
                ((ViewHolder) holder).content.setText(item.content);
            }
        }

        @Override
        protected int getOtherItemViewType(int position) {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView id;
            TextView content;

            public ViewHolder(View itemView) {
                super(itemView);
                id = (TextView) itemView.findViewById(R.id.id);
                content = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }
}
