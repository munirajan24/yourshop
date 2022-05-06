package com.dvishapps.yourshop.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.AProductItemBinding;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.ui.common.Select;
import com.dvishapps.yourshop.ui.interfaces.OnClickListener;
import com.dvishapps.yourshop.ui.interfaces.OnLongClickListener;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Product category adapter.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Cloneable, Filterable {
    public List<Product> products = new ArrayList<>();
    private List<Product> filterProducts;

    private OnClickListener productClickListener;
    private OnLongClickListener longClickListener;
    private FragmentManager fragmentManager;
    private Select select;
    Context mContext;

    /**
     * Instantiates a new Product category adapter.
     *
     * @param products             the products
     * @param fragmentManager      the fragment manager
     * @param productClickListener the product category click listener
     */
    public ProductAdapter(List<Product> products, FragmentManager fragmentManager, Context mContext, OnClickListener productClickListener, OnLongClickListener longClickListener) {
        this.products = products;
        this.productClickListener = productClickListener;
        this.longClickListener = longClickListener;
        this.fragmentManager = fragmentManager;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AProductItemBinding binding = AProductItemBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding, productClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), position);
//        imageUrl="@{StringUtil.concat(prod.default_photo.url,prod.default_photo.img_path)}"
//        Tools.setImage(Constants.IMG_URL, products.get(position).getDefault_photo().getImg_path(), holder.binding.pImage);
        holder.binding.checkoutItemDesc2.setTypeface(null, Typeface.ITALIC);
        holder.binding.checkoutItemDesc2.setPaintFlags(holder.binding.checkoutItemDesc2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.checkoutItemDescOffer.setText("Save Rs. " + Math.round(products.get(position).getOffer_price()-products.get(position).getUnit_price()) + "");

        if (products.get(position).getOffer_price() > products.get(position).getUnit_price()) {
//            if ((products.get(position).getOffer_price() - 1) == products.get(position).getUnit_price()) {
//                holder.binding.checkoutItemDesc2.setVisibility(View.GONE);
//                holder.binding.checkoutItemDescOffer.setVisibility(View.GONE);
//            } else {
//                holder.binding.checkoutItemDesc2.setText(Tools.setOfferPrice2(products.get(position).getUnit_price(), products.get(position).getOffer_price()));
                holder.binding.checkoutItemDesc2.setText( products.get(position).getOffer_price()+"");
                holder.binding.checkoutItemDesc2.setVisibility(View.VISIBLE);
                holder.binding.checkoutItemDescOffer.setVisibility(View.VISIBLE);
//            }
        } else {
            holder.binding.checkoutItemDesc2.setVisibility(View.GONE);
            holder.binding.checkoutItemDescOffer.setVisibility(View.GONE);
        }
        {
            if (products.get(position).getIs_enable().equals("0")) {

//                holder.binding.lnrAddLayout.setVisibility(View.GONE);
//                holder.binding.lnrAddLayoutDummy.setVisibility(View.VISIBLE);
                if (mContext != null) {
                    Tools.setShopImageBlackWhite(Constants.IMG_URL, products.get(position).getDefault_photo().getImg_path(), holder.binding.pImage, mContext);
                } else {
                    Tools.setImage(Constants.IMG_URL, products.get(position).getDefault_photo().getImg_path(), holder.binding.pImage);
                }
            } else {
                holder.binding.lnrAddLayout.setVisibility(View.VISIBLE);
                holder.binding.lnrAddLayoutDummy.setVisibility(View.GONE);
                holder.binding.pItemAddActionDummy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Console.toast("Product not available");
                    }
                });
                Tools.setImage(Constants.IMG_URL, products.get(position).getDefault_photo().getImg_path(), holder.binding.pImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public void setItems(@NotNull List<Product> items) {
        products = items;
        filterProducts = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void addItems(@NotNull List<Product> items) {
        if (products == null)
            products = new ArrayList<>();
        products.addAll(items);
        filterProducts = new ArrayList<>(products);
        notifyItemInserted(products.size());
    }

    public void clear() {
        if (products == null)
            products = new ArrayList<>();
        products.clear();
        filterProducts = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterProducts);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product item : filterProducts) {
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
                products.clear();
                products.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };


    /**
     * The type Product category view holder.
     */
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private AProductItemBinding binding;
        private OnClickListener onClickListener;

        public ProductViewHolder(@NonNull AProductItemBinding binding, OnClickListener onClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onClickListener = onClickListener;
        }

        public void bind(@NotNull Product prod, int pos) {
            binding.setCart(Config.cart);
//            Config.cart.setListener(prod.getId());
            if (prod.getCategory().getName() != null) {
                if (prod.getCategory().getName().length() == 0) {
                    prod.getCategory().setName(Config.sharedPreferences.getString(Constants.CATEGORY_NAME, "null"));
                }
            } else {
                prod.getCategory().setName(Config.sharedPreferences.getString(Constants.CATEGORY_NAME, "null"));
            }

            if (prod.getStatus().equals("0")) {
                binding.checkoutItemName.setTextColor(ContextCompat.getColor(binding.checkoutItemName.getContext(), R.color.md_red_400));
            }else {
                binding.checkoutItemName.setTextColor(ContextCompat.getColor(binding.checkoutItemName.getContext(), R.color.ms_black_87_opacity));
            }

            binding.setProd(prod);
            binding.pImage.setOnClickListener(view -> {
                onClickListener.onClick(pos);
            });

            binding.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onLongClick(pos);
                    return false;
                }
            });
//            binding.pItemMeasure.setOnClickListener(view -> {
//                List<SelectAdapter.SelectObj> selectObjs = new ArrayList<>();
//                selectObjs.add(new SelectAdapter.SelectObj("kk", "kk"));
//                select = new Select(selectObjs, "select", selectObj -> {
//                    select.dismiss();
//                });
//                select.show(fragmentManager, "");
//            });
        }

    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
