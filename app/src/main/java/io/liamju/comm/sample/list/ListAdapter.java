package io.liamju.comm.sample.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.liamju.comm.sample.R;
import io.liamju.comm.sample.dummy.DummyContent;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/12
 */
public class ListAdapter extends BaseAdapter {

    private List<DummyContent.DummyItem> mItems;

    public ListAdapter(List<DummyContent.DummyItem> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, null);
        ((TextView)view.findViewById(R.id.id)).setText(mItems.get(position).id);
        ((TextView)view.findViewById(R.id.content)).setText(mItems.get(position).content);
        return view;
    }
}
