package io.sodaoud.heretest.app.view;

/**
 * Created by sofiane on 12/16/16.
 */

public interface ListView<T> {

    void showProgress(boolean b);

    void setItems(T[] items);

    void showError(String error);

}
