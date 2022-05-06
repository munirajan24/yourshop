package com.dvishapps.yourshop.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.DFullScreenBinding;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.ui.adapters.SlideAdapter;

import java.util.List;

public class FullScreen extends DialogFragment {
    private List<Photo> images;
    private int position;

    private DFullScreenBinding binding;
    private SlideAdapter slideAdapter;

    public FullScreen(List<Photo> images, int position) {
        this.images = images;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreen);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.d_full_screen, container, false);

        slideAdapter = new SlideAdapter(images, binding.fullImageView, (photo, position) -> {

        });
        slideAdapter.setDisplay_bottom(false);
        slideAdapter.setPagerIcons(binding.viewPagerDot);


        binding.fullImageView.setAdapter(slideAdapter);
        binding.fullImageView.setCurrentItem(position);

        binding.returnBtn.setOnClickListener(v -> {
            this.dismiss();
        });
        return binding.getRoot();
    }
}