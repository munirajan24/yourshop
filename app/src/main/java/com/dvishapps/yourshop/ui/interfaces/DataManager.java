package com.dvishapps.yourshop.ui.interfaces;

public interface DataManager<T> {
    void saveData(T data);

    T getData();
}
