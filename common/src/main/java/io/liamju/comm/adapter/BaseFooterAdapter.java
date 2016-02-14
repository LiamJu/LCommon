package io.liamju.comm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.liamju.comm.R;
import io.liamju.comm.view.FooterView;
import io.liamju.comm.view.ItemsView;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public abstract class BaseFooterAdapter<D> extends RecyclerView.Adapter
        implements FooterView<BaseFooterAdapter.FooterViewHolder>, ItemsView<D> {

    private final static int TYPE_FOOTER = 0x12301;

    private List<D> mItems = new ArrayList<>();
    private @State int mState;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_FOOTER == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_view, parent, false);
            return new FooterViewHolder(view);
        }
        return onCreateOtherViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TYPE_FOOTER != getItemViewType(position)) {
            onBindOtherViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mItems.isEmpty()) {
            return mItems.size();
        }
        // 加上 FooterView
        return mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.size() > 0) {
            if (position == mItems.size()) {
                return TYPE_FOOTER;
            }
        }
        return getOtherItemViewType(position);
    }

    @Override
    public void setFooterState(@State int state) {
        if (mState == state) {
            return;
        }
        mState = state;
    }

    @Override
    public int getFooterState() {
        return mState;
    }

    @Override
    public void setFooterView(FooterViewHolder vh) {

    }

    @Override
    public void setItems(Collection<D> items) {
        mItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public void remove(int position) {
        mItems.remove(position);
    }

    @Override
    public void addItems(Collection<D> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void setItem(int position, D item) {
        mItems.set(position, item);
    }

    @Override
    public D getItem(int position) {
        return mItems.get(position);
    }

    protected abstract RecyclerView.ViewHolder onCreateOtherViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindOtherViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract int getOtherItemViewType(int position);

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView indicator;
        ProgressBar progressBar;
        public FooterViewHolder(View itemView) {
            super(itemView);
            indicator = (TextView) itemView.findViewById(R.id.text_indicator);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }

    }
}
