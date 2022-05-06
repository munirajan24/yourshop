package com.dvishapps.yourshop.ui.adapters;


import com.dvishapps.yourshop.databinding.ItemViewPagerAdapterBinding;

public class ViewPagerAdapter/* extends PagerAdapter */{
//
//    private List<Product> featuredProducts;
//    private androidx.databinding.DataBindingComponent dataBindingComponent;
//    private ItemClick callback;
//
//    public ViewPagerAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, ItemClick callback) {
//        this.dataBindingComponent = dataBindingComponent;
//        this.callback = callback;
//    }
//
//    public void replaceFeaturedList(List<Product> featuredProductList) {
//        this.featuredProducts = featuredProductList;
//        this.notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//
//        if (featuredProducts != null && featuredProducts.size() != 0) {
//            return featuredProducts.size();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//
//        return view == object;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//
//        ItemViewPagerAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.item_view_pager_adapter, container, false, dataBindingComponent);
//
//        binding.setProduct(featuredProducts.get(position));
//
//        if (featuredProducts.get(position).getIs_discount().equals(Constants.ONE)) {
//
//            changeVisibilityOfDiscountTextView(binding, View.VISIBLE);
//            binding.oldDiscountPriceTextView.setPaintFlags(binding.oldDiscountPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//            int discountValue = (int) featuredProducts.get(position).getDiscount_percent();
//            String discountValueStr = "-" + discountValue + "%";
//            binding.discountPercentTextView.setText(discountValueStr);
//
//
//        } else {
//            changeVisibilityOfDiscountTextView(binding, View.GONE);
//        }
//
//        if (featuredProducts.get(position).getIs_featured().equals(Constants.ONE)) {
//            binding.featuredIconImageView.setVisibility(View.VISIBLE);
//        } else {
//            binding.featuredIconImageView.setVisibility(View.GONE);
//        }
//
//        binding.ratingBar.setRating(featuredProducts.get(position).getRating_details().getTotal_rating_value());
//
//        binding.newDiscountPriceTextView.setText(String.valueOf(featuredProducts.get(position).getCurrency_symbol() + " " + StringUtil.convertNumber(featuredProducts.get(position).getUnit_price())));
//        binding.oldDiscountPriceTextView.setText(String.valueOf(featuredProducts.get(position).getCurrency_symbol() + " " + StringUtil.convertNumber(featuredProducts.get(position).getOriginal_price())));
//
//
//        binding.ratingValueTextView.setText(binding.getRoot().getResources().getString(R.string.discount__rating5,
//                String.valueOf(featuredProducts.get(position).getRating_details().getTotal_rating_value()),
//                String.valueOf(featuredProducts.get(position).getRating_details().getTotal_rating_count())));
//
//        binding.getRoot().setOnClickListener(view -> callback.onClick(featuredProducts.get(position)));
//
//        container.addView(binding.getRoot());
//
//        return binding.getRoot();
//    }
//
//    public interface ItemClick {
//        void onClick(Product product);
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//
//        container.removeView((View) object);
//    }
//
//    private void changeVisibilityOfDiscountTextView(ItemViewPagerAdapterBinding binding, int status) {
//
//        binding.discountPercentTextView.setVisibility(status);
//
//        binding.oldDiscountPriceTextView.setVisibility(status);
//    }
}
