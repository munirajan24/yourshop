package com.dvishapps.yourshop.ui.layout.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.AProductDetailBinding;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.ui.adapters.RecycleAdapter;
import com.dvishapps.yourshop.ui.adapters.SlideAdapter;
import com.dvishapps.yourshop.ui.common.FullScreen;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.interfaces.OnSlideListener;
import com.dvishapps.yourshop.ui.viewModel.ProductViewModel;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.StringUtil;
import com.dvishapps.yourshop.utils.ViewUtils;

public class DetailActivity extends SFragment implements OnSlideListener, OnRecycleListener {

    private AProductDetailBinding binding;
    private ProductViewModel productViewModel;
    private SlideAdapter slideAdapter;
    private RecycleAdapter recycleAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
//        Config.cart.setListener(Config.sharedPreferences.getString(Constants.PRODUCT_ID, "null"));
        productViewModel.fetchProductDetail(Config.sharedPreferences.getString(Constants.PRODUCT_ID, "null"));
        setTitle(Config.sharedPreferences.getString(Constants.PRODUCT_NAME, "null"));


        productViewModel.getProductDetails().observe(this, product -> {
            binding.setProduct(product);
            productViewModel.fetchProductDetailPhotos(product.getDefault_photo().getImg_parent_id());
        });

        productViewModel.getProductSubPhotos().observe(this, photos -> {
            if (photos!=null) {
                slideAdapter.setPhotos(photos);
                slideAdapter.setPagerIcons(binding.viewPagerDot);
                photos.forEach(photo -> {
                    productViewModel.recycleItemsList.add(new RecycleItem(photo.getImg_id(), StringUtil.concat(photo.getUrl(), photo.getImg_path())));
                });
                productViewModel.setRecycleItems();
            }
        });
        productViewModel.getRecycleItems().observe(this, recycleItems -> {
            recycleAdapter.setItems(recycleItems);
            ViewUtils.removeView(binding.pdPb);
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.a_product_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setCart(Config.cart);

        init();
        return binding.getRoot();
    }

    private void init() {
        ViewPager pd_slide_view = binding.pdSlideView;
        slideAdapter = new SlideAdapter(productViewModel.getProductSubPhotos().getValue(), pd_slide_view, this);
        slideAdapter.setDisplay_bottom(false);
        pd_slide_view.setAdapter(slideAdapter);

        RecyclerView pd_image_list = binding.pDImgList;
        recycleAdapter = new RecycleAdapter(productViewModel.getRecycleItems().getValue(), this);
        recycleAdapter.setImageSize(100, 95);
        recycleAdapter.setItemSize(105, ViewGroup.LayoutParams.MATCH_PARENT);
        pd_image_list.setLayoutManager(getHorizontalLinearLayout());
        pd_image_list.setAdapter(recycleAdapter);
    }

    @Override
    public void onPhotoClick(Photo photo, int position) {
        FullScreen fullScreenImage = new FullScreen(productViewModel.getProductSubPhotos().getValue(), position);
        fullScreenImage.show(getChildFragmentManager(), "tag");
    }

    @Override
    public void onItemClick(RecycleItem category, int position) {
        slideAdapter.slide(position);
    }
}
