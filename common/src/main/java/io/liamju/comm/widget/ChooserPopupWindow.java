package io.liamju.comm.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 从底部滑动显示的 PopupWidow
 *
 * @author LiamJu
 * @version 1.0
 * @since 16/2/21
 */
public abstract class ChooserPopupWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;
    private View mContentView;
    private Context mContext;


    public ChooserPopupWindow(Context context) {
        mContext = context;
        mContentView = inflateContentView();

        setContentView(mContentView);
//        setAnimationStyle(R.style.ChooserPopupWindowStyle);

        calWidthAndHeight(context);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        initViews();
        initEvents();
    }

    private void lightOn() {
        if (mContext instanceof Activity) {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 1.0f;
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    }

    private void lightOff() {
        if (mContext instanceof Activity) {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.3f;
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    }

    protected abstract void initViews();

    protected abstract void initEvents();


    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        mWidth = dm.widthPixels;
        mHeight = (int) (dm.heightPixels * 0.5);
    }

    protected abstract View inflateContentView();

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {

        if (isShowing() || mContentView == null) {
            return;
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        lightOff();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (isShowing() || mContentView == null) {
            return;
        }
        super.showAtLocation(parent, gravity, x, y);
        lightOff();
    }
}
