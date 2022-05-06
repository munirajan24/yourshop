package com.dvishapps.yourshop.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.RecycleItemBinding;
import com.dvishapps.yourshop.databinding.RecycleItemFavShopBinding;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleLongClickListener;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterFavoriteShops extends RecyclerView.Adapter<RecycleAdapterFavoriteShops.CViewHolder> implements Filterable {

    private List<RecycleItem> items;
    private List<RecycleItem> filterItems;
    private OnRecycleListener onRecycleListener;
    private OnRecycleLongClickListener onRecycleLongClickListener;
    private boolean whiteLabel = false;

    Context mContext;
    private int img_height,
            img_width, item_width, item_height;


    public RecycleAdapterFavoriteShops(List<RecycleItem> items, OnRecycleListener onRecycleListener) {
        this.items = items;
        this.onRecycleListener = onRecycleListener;
    }

    public RecycleAdapterFavoriteShops(List<RecycleItem> items, OnRecycleListener onRecycleListener, boolean whiteLabel) {
        this.items = items;
        this.onRecycleListener = onRecycleListener;
        this.whiteLabel = whiteLabel;
    }

    public RecycleAdapterFavoriteShops(List<RecycleItem> items, OnRecycleListener onRecycleListener, OnRecycleLongClickListener onRecycleLongClickListener) {
        this.items = items;
        this.onRecycleListener = onRecycleListener;
        this.onRecycleLongClickListener = onRecycleLongClickListener;
    }

    public RecycleAdapterFavoriteShops(List<RecycleItem> items, OnRecycleListener onRecycleListener, OnRecycleLongClickListener onRecycleLongClickListener, Context mContext) {
        this.items = items;
        this.onRecycleListener = onRecycleListener;
        this.onRecycleLongClickListener = onRecycleLongClickListener;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecycleItemFavShopBinding itemBinding = RecycleItemFavShopBinding.inflate(inflater, parent, false);
        return new CViewHolder(itemBinding);
    }

    public void setImageSize(Integer width, Integer height) {
        this.img_height = height;
        this.img_width = width;
    }

    public void setItemSize(Integer width, Integer height) {
        this.item_width = width;
        this.item_height = height;
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        holder.setImageSize(img_width, img_height);
        holder.setItemSize(item_width, item_height);
        holder.bind(items.get(position), position);


        if (whiteLabel) {
            if (items.get(position).getImageUrl().length() == 0) {
                holder.binding.cateImg.setImageResource(R.drawable.ic_shop_placeholder_small_vector);
            } else {
                Tools.setShopImage(items.get(position).getImageUrl(), "", holder.binding.cateImg);
            }

//            holder.binding.cateTitle.setTextColor(ContextCompat.getColor(holder.binding.cateTitle.getContext(), R.color.ms_white));
        } else {
            if (items.get(position).getIs_enable().equals("0")) {

                if (mContext != null) {
                    Tools.setShopImageBlackWhite(items.get(position).getImageUrl(), "", holder.binding.cateImg, mContext);
                } else {
                    Tools.setImage(items.get(position).getImageUrl(), "", holder.binding.cateImg);
                }
            } else {
                Tools.setImage(items.get(position).getImageUrl(), "", holder.binding.cateImg);
            }
        }

        holder.binding.lnrItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                try {
                    onRecycleLongClickListener.onItemLongClick(items.get(position), position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setItems(List<RecycleItem> items) {
        this.items = items;
        this.filterItems = new ArrayList<>(items);
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RecycleItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterItems);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (RecycleItem item : filterItems) {
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
                items.clear();
                items.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };

    public class CViewHolder extends RecyclerView.ViewHolder {
        private final RecycleItemFavShopBinding binding;

        public CViewHolder(@NotNull RecycleItemFavShopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RecycleItem item, int pos) {
            binding.setItem(item);
            binding.setListener(onRecycleListener);
            binding.setPosition(pos);
            binding.executePendingBindings();
        }

        public void setImageSize(Integer width, Integer height) {
            binding.setImgHeight(height);
            binding.setImgWidth(width);
        }

        public void setItemSize(Integer width, Integer height) {
            binding.setWidth(width);
        }
    }


}
