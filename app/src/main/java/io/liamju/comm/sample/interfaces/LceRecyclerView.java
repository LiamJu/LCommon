package io.liamju.comm.sample.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/12
 */
public interface LceRecyclerView<RV extends RecyclerView, RVA extends RecyclerView.Adapter> {

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param l The RecyclerView where the click happened
     * @param v The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     */
    void onListItemClick(RV l, View v, int position, long id);

    void setListAdapter(RVA adapter);

    void setSelection(int position);

    int getSelectedItemPosition();

    long getSelectedItemId();

    RV getListView();

    void setEmptyText(CharSequence text);

    void setListShown(boolean shown);

    void setListShownNoAnimation(boolean shown);

    RVA getListAdapter();
}
