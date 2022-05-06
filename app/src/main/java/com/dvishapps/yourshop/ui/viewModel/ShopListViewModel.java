package com.dvishapps.yourshop.ui.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;

import java.util.List;

public class ShopListViewModel extends SViewModel {

    public MutableLiveData<List<RecycleItem>> getRecycleItems() {
        return recycleItems;
    }

    public void setRecycleItems() {
        recycleItems.postValue(recycleItemsList);
    }
}


