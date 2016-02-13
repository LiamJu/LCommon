package io.liamju.comm.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;

import io.liamju.comm.sample.recycler.RefreshLoadMoreRecyclerFragment;

public class MainActivity extends AppCompatActivity {

    private TextView textLog;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLog = (TextView) findViewById(R.id.text_log);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RefreshLoadMoreRecyclerFragment())
                .commit();
    }

    private StringBuilder mLog = new StringBuilder("");

    //    private ListFragment fragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        fragment = new ListFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
//                .commit();
//    }
//
//    public void setEmptyText(View view) {
//        fragment.setEmptyText("error");
//    }
//
//    public void nullAdapter(View view) {
//        fragment.setListAdapter(null);
//    }
//
//
//    public void selectItem(View view) {
//        fragment.setSelection(10);
//    }
//
//    public void fillItem(View view) {
//
//    }
}
