package com.dvishapps.yourshop.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.DSelectBinding;
import com.dvishapps.yourshop.ui.adapters.SelectAdapter;

import java.util.List;

public class Select extends DialogFragment {
    RecyclerView select;
    private List<SelectAdapter.SelectObj> options;
    private SelectAdapter.OnSelectListener onSelectListener;
    private SelectAdapter adapter;
    private DSelectBinding binding;
    private String title;

    public Select(List<SelectAdapter.SelectObj> options, String title, SelectAdapter.OnSelectListener onSelectListener) {
        this.options = options;
        this.title = title;
        this.onSelectListener = onSelectListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreen);
        adapter = new SelectAdapter(options, onSelectListener);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.d_select, container, false);
        binding.setTitle(title);
        binding.selectClose.setOnClickListener(v -> {
            this.dismiss();
        });
        binding.select.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        binding.select.setAdapter(adapter);
        return binding.getRoot();
    }


}
