package com.dvishapps.yourshop.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.SlideBinding;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.ui.interfaces.OnSlideListener;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlideAdapter extends PagerAdapter {
    private List<Photo> photos;
    private ViewPager viewPager;
    private LinearLayout slideshow_bottom;
    private boolean display_bottom = true;


    private int dotsCount;
    private ImageView[] dotsImg;
    OnSlideListener onSlideListener;

    public SlideAdapter(List<Photo> images,
                        ViewPager viewPager,
                        OnSlideListener onSlideListener) {
        this.photos = images;
        this.viewPager = viewPager;
        this.onSlideListener = onSlideListener;
    }

    public void setDisplay_bottom(boolean toggle_bottom) {
        this.display_bottom = toggle_bottom;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        this.notifyDataSetChanged();
    }

    public void slide(int pos) {
        viewPager.setCurrentItem(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos != null ? this.photos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SlideBinding binding = SlideBinding.inflate(inflater, container, false);
        binding.setPhoto(photos.get(position));
        binding.setShowBottom(display_bottom);
        Tools.setImage(Constants.IMG_URL, photos.get(position).getImg_path(), binding.slideCateImg);
        binding.slideContent.setOnClickListener(view -> {
            onSlideListener.onPhotoClick(photos.get(position), position);
        });
        container.addView(binding.getRoot());
        return binding.getRoot();
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
        if (dotsImg.length > 0)
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
