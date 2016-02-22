package io.liamju.simple.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.liamju.comm.widget.ChooserPopupWindow;
import io.liamju.simple.R;

public class MainActivity extends AppCompatActivity {

    private ChooserPopupWindow mChooserPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initPopWindow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mChooserPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPopWindow() {
        mChooserPopupWindow = new ChooserPopupWindow(this) {
            @Override
            protected void initViews() {

            }

            @Override
            protected void initEvents() {

            }

            @Override
            protected View inflateContentView() {
                return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_popup_chooser, null);
            }
        };
    }
}
