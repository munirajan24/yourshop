package com.dvishapps.yourshop.ui.viewModel.common;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dvishapps.yourshop.models.RecycleItem;

import java.util.ArrayList;
import java.util.List;

public class SViewModel extends ViewModel {

    public final List<RecycleItem> recycleItemsList = new ArrayList<>();
    public final MutableLiveData<List<RecycleItem>> recycleItems = new MutableLiveData<>();


    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public int offset = 0;
    public int limit;

    public boolean forceEndLoading = false;
    public boolean loadMore = true;


    public void setLoadingState(Boolean state) {
        loadMore = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

}
