package com.dvishapps.yourshop.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.CheckoutChildItemBinding;
import com.dvishapps.yourshop.databinding.CheckoutParentItemBinding;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutParentAdapter extends RecyclerView.Adapter<CheckoutParentAdapter.CheckoutParentHolder> {

    private HashMap<String, List<Product>> products;
    private List<String> categories;
    private OnParentListener onParentListener;
    private int child_position = -1;

    public CheckoutParentAdapter(@NotNull HashMap<String, List<Product>> products, OnParentListener onParentListener) {
        this.products = products;
        this.categories = new ArrayList<>(products.keySet());
        this.onParentListener = onParentListener;
    }

    @NonNull
    @Override
    public CheckoutParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CheckoutParentItemBinding binding = CheckoutParentItemBinding.inflate(inflater, parent, false);
        return new CheckoutParentHolder(binding);
    }

    public void notifyChangedItems(@NotNull HashMap<String, List<Product>> products, int parentPosition, int childPosition) {
        this.products = products;
        this.categories = new ArrayList<>(products.keySet());

    }

    public void notifyChangedItems(int parentPosition, int childPosition) {
        this.categories = new ArrayList<>(products.keySet());
        child_position = childPosition;
        if (parentPosition == 0 && childPosition == 0)
            this.notifyDataSetChanged();
        else
            this.notifyItemChanged(parentPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutParentHolder holder, int parentPosition) {
        String cate_id = categories.get(parentPosition);
        List<Product> childDataSet = products.get(cate_id);
        try {
            holder.binding.setCategory(childDataSet.get(0).getCategory().getName());
            holder.binding.setItems(childDataSet.size());
            holder.binding.executePendingBindings();
            holder.checkoutChildAdapter = new CheckoutChildAdapter(childDataSet, (action, childPosition) -> {
                if (childDataSet.size() > 0)
                    onParentListener.onAction(action, childDataSet.get(childPosition), parentPosition, childPosition);
            });
            holder.child_checkout_list.setLayoutManager(new LinearLayoutManager(Config.context, RecyclerView.VERTICAL, false));
            holder.child_checkout_list.setAdapter(holder.checkoutChildAdapter);
            if (child_position != -1)
                holder.checkoutChildAdapter.notifyItemChanged(child_position);
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public static class CheckoutParentHolder extends RecyclerView.ViewHolder {
        CheckoutParentItemBinding binding;
        CheckoutChildAdapter checkoutChildAdapter;
        RecyclerView child_checkout_list;

        public CheckoutParentHolder(@NonNull CheckoutParentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            child_checkout_list = binding.childCheckoutList;
        }
    }

    public interface OnParentListener {
        void onAction(Cart.Actions action, Product prod, int parentPosition, int childPosition);
    }
}

//child
class CheckoutChildAdapter extends RecyclerView.Adapter<CheckoutChildAdapter.CheckoutChildHolder> {

    private List<Product> childProducts;
    private OnChildListener onChildListener;

    CheckoutChildAdapter(List<Product> products, OnChildListener onChildListener) {
        this.childProducts = products;
        this.onChildListener = onChildListener;
    }

    @NonNull
    @Override
    public CheckoutChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CheckoutChildItemBinding binding = CheckoutChildItemBinding.inflate(inflater, parent, false);
        return new CheckoutChildHolder(binding, onChildListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutChildHolder holder, int childPosition) {
        holder.bind(childProducts.get(childPosition), childPosition);
//                                    imageUrl="@{StringUtil.concat(product.default_photo.url,product.default_photo.img_path)}"
        Tools.setImage(Constants.IMG_URL, childProducts.get(childPosition).getDefault_photo().getImg_path(), holder.childBinding.imgChild);

    }

    @Override
    public int getItemCount() {
        return childProducts != null ? childProducts.size() : 0;
    }

    static class CheckoutChildHolder extends RecyclerView.ViewHolder {
        private CheckoutChildItemBinding childBinding;
        private OnChildListener onChildListener;

        CheckoutChildHolder(@NonNull CheckoutChildItemBinding binding, OnChildListener onChildListener) {
            super(binding.getRoot());
            childBinding = binding;

            this.onChildListener = onChildListener;
        }

        public void bind(@NotNull Product prod, int pos) {
            childBinding.setProduct(prod);
            childBinding.executePendingBindings();
            childBinding.setCart(Config.cart);
            childBinding.checkoutItemActionAdd.setOnClickListener(view -> {
                onChildListener.onClick(Cart.Actions.ADD, pos);
            });
            childBinding.checkoutItemActionMinus.setOnClickListener(view -> {
                onChildListener.onClick(Cart.Actions.REMOVE, pos);
            });
            childBinding.deleteProductAction.setOnClickListener(v -> {
                onChildListener.onClick(Cart.Actions.DELETE, pos);
            });
        }
    }

    interface OnChildListener {
        void onClick(Cart.Actions action, int childPosition);
    }
}

