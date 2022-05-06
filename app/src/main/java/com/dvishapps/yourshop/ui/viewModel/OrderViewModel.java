package com.dvishapps.yourshop.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dvishapps.yourshop.models.Order;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;

public class OrderViewModel extends SViewModel {
    private LiveData<Order> orderData;

    public OrderViewModel() {
        //test
        MutableLiveData<Order> response = new MutableLiveData<>();
        orderData = response;
    }

    public LiveData<Order> getOrderData() {
        return orderData;
    }

    public void setOrderData(LiveData<Order> orderData) {
        this.orderData = orderData;
    }
}
