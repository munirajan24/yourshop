package com.dvishapps.yourshop.ui.adapters;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.ShopListAvailableItemBinding;
import com.dvishapps.yourshop.databinding.ShopListAvailableVerticalItemBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShopListAvailableAdapter extends RecyclerView.Adapter<ShopListAvailableAdapter.OrderViewHolder> implements Cloneable, Filterable {

    private List<Shop> shopList;
    private List<Shop> filterShops;
    private OnTransactionListener onOrderListener;
    private OnTransactionCallListener onTransactionCallListener;
    private OnQrCodeClickListener onQrCodeClickListener;

    public ShopListAvailableAdapter(List<Shop> shopList, OnTransactionListener onOrderListener, OnTransactionCallListener onTransactionCallListener, OnQrCodeClickListener onQrCodeClickListener) {
        this.shopList = shopList;
        this.onOrderListener = onOrderListener;
        this.onTransactionCallListener = onTransactionCallListener;
        this.onQrCodeClickListener = onQrCodeClickListener;
    }

    public ShopListAvailableAdapter(List<Shop> shopList, OnTransactionListener onOrderListener) {
        this.shopList = shopList;
        this.onOrderListener = onOrderListener;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Shop> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterShops);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Shop item : filterShops) {
                    if (item.getName().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NotNull FilterResults results) {
            if (results.values != null) {
                shopList.clear();
                shopList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };


    public void setItems(@NotNull List<Shop> items) {
        sortList(items);
        shopList = items;
        filterShops = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void addItems(@NotNull List<Shop> items) {
        if (shopList == null)
            shopList = new ArrayList<>();
        shopList.addAll(items);
        filterShops = new ArrayList<>(shopList);
        notifyItemInserted(shopList.size());
    }

    public void clear() {
        if (shopList == null)
            shopList = new ArrayList<>();
        shopList.clear();
        filterShops = new ArrayList<>(shopList);
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private ShopListAvailableVerticalItemBinding binding;
        private OnTransactionListener onOrderListener;
        private OnTransactionCallListener onTransactionCallListener;
        private OnQrCodeClickListener onQrCodeClickListener;

        public OrderViewHolder(@NonNull ShopListAvailableVerticalItemBinding binding, OnTransactionListener onOrderListener, OnTransactionCallListener onTransactionCallListener, OnQrCodeClickListener onQrCodeClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onOrderListener = onOrderListener;
            this.onTransactionCallListener = onTransactionCallListener;
            this.onQrCodeClickListener = onQrCodeClickListener;
        }

        public void bind(Shop shop) {
            binding.setShop(shop);

            if (shop.getIs_enable().equals("1")) {
                binding.txtOpen.setText("Available");
//                binding.txtOpen.setBackgroundResource(R.drawable.toggleon);
                binding.txtOpen.setTextColor(Color.GREEN);
            } else {
                binding.txtOpen.setText("Closed");
//                binding.txtOpen.setBackgroundResource(R.drawable.toggleon_red);
                binding.txtOpen.setTextColor(Color.RED);
            }

            if (shop.getStatus().equals("1")) {
                binding.txtOpen.setText("Available");
//                binding.txtOpen.setBackgroundResource(R.drawable.toggleon);
                binding.txtOpen.setTextColor(Color.GREEN);
                binding.shopName.setTextColor(Color.BLACK);
                binding.distance.setTextColor(Color.BLACK);
            } else {
                binding.txtOpen.setText("Closed");
//                binding.txtOpen.setBackgroundResource(R.drawable.toggleon_red);
                binding.txtOpen.setTextColor(Color.RED);
                binding.shopName.setTextColor(Color.GRAY);
                binding.distance.setTextColor(Color.GRAY);
            }

            binding.parentLayout.setOnClickListener(view -> {
                onOrderListener.onViewDetail(shop.getId());
            });
            try {
                if (shop.getStatus().equals("1")) {
                    if (shop.getDefault_photo() != null) {
                        Tools.setImage(Constants.IMG_URL, shop.getDefault_photo().getImg_path(), binding.imgShopImageNew);
                    }
                } else {
                    if (shop.getDefault_photo() != null) {
                        Tools.setShopImageBlackWhite(Constants.IMG_URL, shop.getDefault_photo().getImg_path(), binding.imgShopImageNew, binding.imgShopImageNew.getContext());
//                    Tools.setShopImageBlackWhite(items.get(position).getImageUrl(), "", holder.binding.cateImg, binding.imgShopImageNew);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                double distanceNew = SphericalUtil.computeDistanceBetween(
                        new LatLng(Double.parseDouble(shop.getLat()),
                                Double.parseDouble(shop.getLng())),
                        new LatLng(SessionData.getInstance().getLat(), SessionData.getInstance().getLng()));
                distanceNew = distanceNew / 1000;
                binding.distance.setText(Tools.round1(distanceNew) + "km");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ShopListAvailableVerticalItemBinding binding = ShopListAvailableVerticalItemBinding.inflate(inflater, parent, false);
        return new OrderViewHolder(binding, onOrderListener, onTransactionCallListener, onQrCodeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(shopList.get(position));
    }

    @Override
    public int getItemCount() {
        return shopList != null ? shopList.size() : 0;
    }

    public interface OnTransactionListener {
        void onViewDetail(String tran_id);
    }

    public interface OnTransactionCallListener {
        void onUserCall(String phone);
    }

    public interface OnQrCodeClickListener {
        void onClickQrCode(String qrCode);
    }

    public void sortList(List<Shop> modelList) {


        Collections.sort(modelList, new Comparator<Shop>() {
            @Override
            public int compare(Shop lhs, Shop rhs) {
                return Double.compare(lhs.getDistance(), rhs.getDistance());
            }
        });

    }
}
