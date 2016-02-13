package io.liamju.comm.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.liamju.comm.sample.dummy.DummyContent;
import io.liamju.comm.sample.recycler.ListAdapter;
import io.liamju.comm.sample.recycler.RecyclerFragment;
import io.liamju.comm.sample.recycler.RecyclerItemFragment;

public class MainActivity extends AppCompatActivity {

    private RecyclerItemFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new RecyclerItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void setEmptyText(View view) {
        fragment.setEmptyText("error");
    }

    public void nullAdapter(View view) {
        fragment.setListAdapter(null);
    }


    public void selectItem(View view) {
        fragment.setSelection(10);
    }

    public void fillItem(View view) {
        fragment.setListAdapter(new ListAdapter(DummyContent.ITEMS, new RecyclerFragment.OnItemListClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("onItemClick", "pos:" + position);
            }
        }));
    }
}
