package io.liamju.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import io.liamju.comm.imageloader.ImageLoader;
import io.liamju.simple.provider.Images;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView mGridView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.id_grid_view);
        ImagesAdapter adapter;
        mGridView.setAdapter(adapter = new ImagesAdapter(getActivity(), Images.imageThumbUrls));
        mGridView.setOnScrollListener(adapter);

    }

    private static class ImagesAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

        private final Context mContext;
        private String[] mImageThumbUrls;
        private boolean mIsGridViewIdel = true;

        public ImagesAdapter(Context context, String[] imageThumbUrls) {
            mImageThumbUrls = imageThumbUrls;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mImageThumbUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return mImageThumbUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) convertView.findViewById(R.id.id_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = viewHolder.image;
            final String tag = (String) imageView.getTag();
            final String uri = (String) getItem(position);
            if (!uri.equals(tag)) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
            if (mIsGridViewIdel) {
                ImageLoader.getInstance(mContext).bindBitmap(uri, imageView);
            }
            return convertView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                mIsGridViewIdel = true;
            } else {
                mIsGridViewIdel = false;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }

        private class ViewHolder {
            private ImageView image;
        }
    }
}
