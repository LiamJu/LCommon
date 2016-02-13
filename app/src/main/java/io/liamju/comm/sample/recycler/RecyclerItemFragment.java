package io.liamju.comm.sample.recycler;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.liamju.comm.sample.dummy.DummyContent;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/12
 */
public class RecyclerItemFragment extends RecyclerFragment<RecyclerView, ListAdapter> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected RecyclerView initRecycler(Activity activity) {
        return new RecyclerView(activity);
    }

    @Override
    public void onListItemClick(RecyclerView l, View v, int position, long id) {

    }
}
