package com.dvishapps.yourshop.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.FCategoryBinding;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

import java.util.List;

public class SlideShowAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Product> productList;
    private ViewPager viewPager;
    private RelativeLayout slideshow_bottom;
    private boolean display_bottom = true;
    FCategoryBinding binding;

    private int dotsCount = 0;
    private ImageView[] dotsImg;
    int position = 0;
//    ProductAdapter.ProductListener onProductListener;

    public SlideShowAdapter(Context context, FCategoryBinding binding, List<Product> products, ViewPager viewPager) {
        this.context = context;
        this.productList = products;
        this.viewPager = viewPager;
        this.binding = binding;
//        this.onProductListener = onProductListener;
    }

    public void setDisplay_bottom(boolean toggle_bottom) {
        this.display_bottom = toggle_bottom;
    }

    @Override
    public int getCount() {
        return this.productList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        ImageView imageView = view.findViewById(R.id.slide_cate_img);
        TextView txtTitle = view.findViewById(R.id.slide_cate_title);
        TextView txtPrice = view.findViewById(R.id.slide_cate_price);
//        TextView textView = view.findViewById(R.id.slide_cate_title);
        Tools.setImageWithShimmerEffect(Constants.IMG_URL, productList.get(position).getDefault_photo().getImg_path(), imageView, binding.shimmerViewContainerSlide);
        txtTitle.setText(productList.get(position).getName().trim());
        if (productList.get(position).getUnit_price()==0){
            txtPrice.setVisibility(View.INVISIBLE);
        }else {
            txtPrice.setText(productList.get(position).getCurrency_symbol() + " " + productList.get(position).getUnit_price());
        }
        slideshow_bottom = view.findViewById(R.id.slideshow_bottom);

        imageView.setOnClickListener(v -> {
//            Config.editPreferences.putString(Constants.PRODUCT_ID, productList.get(position).getId());
//            Config.editPreferences.putString(Constants.PRODUCT_NAME, productList.get(position).getName());
//            Config.editPreferences.apply();
//            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_home_to_detailActivity);
        });

        if (display_bottom == false) {
            slideshow_bottom.setVisibility(View.INVISIBLE);
        }
        if (dotsCount == 0) {
            setPagerIcons(binding.viewPagerDot);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    public void setPagerIcons(LinearLayout view_pager_dot) {
        dotsCount = this.getCount();
        dotsImg = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dotsImg[i] = new ImageView(viewPager.getContext());
            dotsImg[i].setImageResource(R.drawable.dot_no_active);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2, 0, 2, 0);
            view_pager_dot.addView(dotsImg[i], params);
        }
        dotsImg[0].setImageResource(R.drawable.dot_active);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dotsImg[i].setImageResource(R.drawable.dot_no_active);
                }
                dotsImg[position].setImageResource(R.drawable.dot_active);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
