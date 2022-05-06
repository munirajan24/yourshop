package com.dvishapps.yourshop.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.databinding.PackSizeBinding;
import com.dvishapps.yourshop.models.Product;

import java.util.List;

public class PackListAdapter extends RecyclerView.Adapter<PackListAdapter.PackListViewHolder> {
    private List<Product> list;

    public PackListAdapter(List<Product> list) {
        this.list = list;
    }


    public class PackListViewHolder extends RecyclerView.ViewHolder {
        private PackSizeBinding binding;

        public PackListViewHolder(@NonNull PackSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.setProduct(product);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public PackListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PackSizeBinding binding = PackSizeBinding.inflate(inflater, parent, false);
        return new PackListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PackListViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

}
