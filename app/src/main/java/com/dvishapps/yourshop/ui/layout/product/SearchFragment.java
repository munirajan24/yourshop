package com.dvishapps.yourshop.ui.layout.product;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.FSearchBinding;
import com.dvishapps.yourshop.models.Search;
import com.dvishapps.yourshop.ui.common.SDialogFragment;
import com.dvishapps.yourshop.ui.interfaces.OnSearchListener;
import com.dvishapps.yourshop.utils.ViewUtils;

public class SearchFragment extends SDialogFragment {

    private int star;
    private OnSearchListener onSearchListener;
    private FSearchBinding binding;
    private View oldView;

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FSearchBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        setHasOptionsMenu(true);

        binding.itemOneStar.setOnClickListener(v -> {
            setSelectedStar(v, 1);
        });
        binding.itemTwoStar.setOnClickListener(v -> {
            setSelectedStar(v, 2);
        });
        binding.itemThreeStar.setOnClickListener(v -> {
            setSelectedStar(v, 3);
        });
        binding.itemFourStar.setOnClickListener(v -> {
            setSelectedStar(v, 4);
        });
        binding.itemFiveStar.setOnClickListener(v -> {
            setSelectedStar(v, 5);
        });

        binding.itemFilterBtn.setOnClickListener(v -> {
            initSearchData();
        });
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onSearchListener = (OnSearchListener) context;
        } catch (ClassCastException c) {

        }
    }

    private void setSelectedStar(View v, int str) {
        this.star = str;
        if (oldView == null || (oldView.getId() != v.getId())) {
            for (int i = 0; i < binding.statsButtonsContainer.getChildCount(); i++) {
                if (binding.statsButtonsContainer.getChildAt(i).getId() == v.getId()) {
                    ViewUtils.changeViewDrawable(
                            v, ContextCompat.getDrawable(this.getContext(), R.drawable.radius),
                            oldView, ContextCompat.getDrawable(this.getContext(), R.drawable.border)
                    );
                }
            }
            oldView = v;
        }
    }

    private void initSearchData() {
        Search search = new Search(
                binding.itemName.getText().toString(),
                binding.itemMinPrice.getText().toString(),
                binding.itemMaxPrice.getText().toString(),
                star,
                binding.itemFeatured.isChecked(),
                binding.itemDiscount.isChecked()
        );
        search.setSearchTerm("ICECREAM");
        onSearchListener.onSearch(search);
        this.dismiss();
    }
}
