package io.liamju.comm.view;

import java.util.Collection;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/13
 */
public interface ItemsView<D> {

    void setItems(Collection<D> items);

    void remove(int position);

    void setItem(int position, D item);

    void addItems(Collection<D> items);

    D getItem(int position);

}
