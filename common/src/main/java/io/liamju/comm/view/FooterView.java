package io.liamju.comm.view;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public interface FooterView<VH extends RecyclerView.ViewHolder> {

    int DRAGGING     = 0x122302;
    int NO_MORE      = 0x122303;
    int NONE         = 0x122304;
    int LOADING      = 0x122305;
    int EMPTY_DATA   = 0x122306;

    @IntDef({EMPTY_DATA, DRAGGING, NO_MORE, NONE, LOADING})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
    }

    void setFooterState(@State int state);

    @State int getFooterState();

    void setFooterView(VH vh);
}
