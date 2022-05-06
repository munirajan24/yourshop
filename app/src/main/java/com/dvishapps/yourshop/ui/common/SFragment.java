package com.dvishapps.yourshop.ui.common;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.ui.interfaces.OnToolbarChange;

public class SFragment extends Fragment {
    protected OnToolbarChange onToolbarChange;
    protected final GridLayoutManager verticalGridLayout = new GridLayoutManager(this.getContext(), 3, RecyclerView.VERTICAL, false);
    protected boolean isScroll = false;
    protected int childCount, itemCount, visibleItemPosition;

    protected GridLayoutManager getVerticalGridLayout() {
        return new GridLayoutManager(this.getContext(), 3, RecyclerView.VERTICAL, false);
    }

    protected LinearLayoutManager getLinearLayout() {
        return new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
    }

    protected LinearLayoutManager getHorizontalLinearLayout() {
        return new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
    }

    protected GridLayoutManager getGridLayout() {
        return new GridLayoutManager(this.getContext(), 3);
    }

    protected void setTitle(String title) {
        onToolbarChange.changeTitle(title);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onToolbarChange = (OnToolbarChange) context;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
