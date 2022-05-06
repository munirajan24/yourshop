package com.dvishapps.yourshop.ui.layout.checkout;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.BasketProduct;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutResultFailure extends DialogFragment {
    private static String KEYUPLOAD = "transactionHeaderUpload";
    private static String PRODUCTSBYCATEGORY = "prod";

    private RecyclerView trans_items;
    private TextView total_item_text_view;
    private TextView trans_amount;
    private AppCompatButton trans_done_btn;
    private Transaction transactionHeaderUpload;
    private HashMap<String, List<BasketProduct>> prods;

    private double delivery_charge;
    private double shipping_tax_amount;
    private double tax_amount;
    private double final_total;
    private double shipping_cost = 0;

    private TextView totalItemCountValue;
    private TextView totalValue;
    private TextView subtotalValue;
    private TextView taxValue;
    private TextView deliveryChargeValue;
    private TextView finalTotalValue;
    private TextView overAllTax;



    private TransParentAdapter transaction_parent_adapter;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity(), RecyclerView.VERTICAL, false);

    public static CheckoutResultFailure newInstance(Transaction transactionHeaderUpload, HashMap<String, List<BasketProduct>> prods) {
        Bundle args = new Bundle();
        args.putSerializable(KEYUPLOAD, transactionHeaderUpload);
        args.putSerializable(PRODUCTSBYCATEGORY, prods);
        CheckoutResultFailure fragment = new CheckoutResultFailure();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreen);
        if (getArguments() != null) {
            transactionHeaderUpload = (Transaction) getArguments().getSerializable(KEYUPLOAD);
            prods = (HashMap<String, List<BasketProduct>>) getArguments().getSerializable(PRODUCTSBYCATEGORY);
        }
        transaction_parent_adapter = new TransParentAdapter(prods, transactionHeaderUpload);

    }

    @Override
    public void onResume() {
        super.onResume();
//        Config.cart.clearCart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transation_details_failure, container, false);
        trans_items = root.findViewById(R.id.trans_items);
        total_item_text_view = root.findViewById(R.id.total_item_text_view);
        trans_amount = root.findViewById(R.id.trans_amount);
        trans_done_btn = root.findViewById(R.id.trans_done_btn);

        totalItemCountValue = root.findViewById(R.id.totalItemCountValue);
        totalValue = root.findViewById(R.id.totalValue);
        subtotalValue = root.findViewById(R.id.subtotalValue);
        taxValue = root.findViewById(R.id.taxValue);
        deliveryChargeValue = root.findViewById(R.id.deliveryChargeValue);
        finalTotalValue = root.findViewById(R.id.finalTotalValue);
        overAllTax = root.findViewById(R.id.overAllTax);


//        binding.setOverAllTaxValue(SessionData.getInstance().getOverAllTaxValue());
        if (SessionData.getInstance().getOverAllTaxValue() == 0) {
            overAllTax.setVisibility(View.GONE);
            taxValue.setVisibility(View.GONE);
        }

//        total_item_text_view.setText(StringUtil.setString(getString(R.string.transaction_success_for), transactionHeaderUpload.getDetails().size()));
        if (Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {
            trans_amount.setText(StringUtil.setString("%s %s", transactionHeaderUpload.getCurrencySymbol(), transactionHeaderUpload.getTotal_item_amount()));
        } else {
            trans_amount.setText(StringUtil.setString("%s %s", transactionHeaderUpload.getCurrencySymbol(), Double.parseDouble(transactionHeaderUpload.getTotal_item_amount()) - Config.cart.getDelivery_charge()));
        }

//        binding = DataBindingUtil.inflate(inflater, R.layout.checkout_form_2, container, false);
        tax_amount = (Config.cart.getTotal_price() * SessionData.getInstance().getOverAllTaxValue()) / 100;
//        shipping_tax_amount = 20;

//        delivery_charge = 20;
//        final_total = (Config.cart.getTotal_price() + shipping_cost + tax_amount + shipping_tax_amount);


//        if (Config.cart.getTotal_save() > 0)
//            discountValue.setText(String.valueOf(Config.cart.getTotal_save()));

        totalItemCountValue.setText(String.valueOf(Config.cart.getTotal_qty()));
        totalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getTotal_price());
        subtotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getTotal_price());
        taxValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + tax_amount);

//        shippingCostValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + shipping_cost);
        if (Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {
            deliveryChargeValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getDelivery_charge());
            finalTotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getFinalPrice());
        } else {
            deliveryChargeValue.setText("Free");
            finalTotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " "
                    + (Double.parseDouble(Config.cart.getFinalPrice())
                    - Config.cart.getDelivery_charge()));
        }
//        shippingTaxValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + shipping_tax_amount);

//        couponDiscountValue.setText("-");
//        couponDiscountValueInput.setText("");

        overAllTax.setText(String.format(getString(R.string.tax), SessionData.getInstance().getOverAllTaxValue() + ""));
//        shippingTax.setText(String.format(getString(R.string.shipping_tax), Config.TAX_SHIPPING_VALUE));


        trans_items.setLayoutManager(linearLayoutManager);
        trans_items.setAdapter(transaction_parent_adapter);

        trans_done_btn.setOnClickListener(v -> {
            this.dismiss();
        });

        return root;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
//        getActivity().finish();
//        SessionData.getInstance().getBottomNavigation().setCount(R.id.nav_order, ((int) (SessionData.getInstance().getOrdersCount()) + 1) + "");
    }

    public class TransParentAdapter extends RecyclerView.Adapter<TransParentAdapter.TransParentViewHolder> {

        private Transaction transactionHeaderUpload;
        private HashMap<String, List<BasketProduct>> product_list;
        private List<String> categories;

        public TransParentAdapter(@NotNull HashMap<String, List<BasketProduct>> product_list, Transaction transactionHeaderUpload) {
            this.transactionHeaderUpload = transactionHeaderUpload;
            this.product_list = product_list;
            this.categories = new ArrayList<>(product_list.keySet());
        }

        @NonNull
        @Override
        public TransParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.transaction_item_parent, parent, false);
            return new TransParentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransParentViewHolder holder, int position) {
            TransChildAdapter transChildAdapter = new TransChildAdapter(product_list.get(categories.get(position)));
            holder.trans_item_category.setText(categories.get(position));
            holder.trans_rv_prent_item.setLayoutManager(new LinearLayoutManager(getContext()));
            holder.trans_rv_prent_item.setAdapter(transChildAdapter);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class TransParentViewHolder extends RecyclerView.ViewHolder {
            private TextView trans_item_category;
            private RecyclerView trans_rv_prent_item;

            public TransParentViewHolder(@NonNull View itemView) {
                super(itemView);
                trans_item_category = itemView.findViewById(R.id.trans_item_category);
                trans_rv_prent_item = itemView.findViewById(R.id.trans_rv_prent_item);
            }
        }

    }

    public static class TransChildAdapter extends RecyclerView.Adapter<TransChildAdapter.TransItemViewHolder> {
        private List<BasketProduct> list;

        public TransChildAdapter(List<BasketProduct> list) {
            this.list = list;
        }

        public class TransItemViewHolder extends RecyclerView.ViewHolder {
            private TextView trans_product_name;
            private TextView trans_product_price;
            private TextView trans_product_qty;
            private View divider;

            public TransItemViewHolder(@NonNull View itemView) {
                super(itemView);
                trans_product_name = itemView.findViewById(R.id.trans_product_name);
                trans_product_price = itemView.findViewById(R.id.trans_product_price);
                trans_product_qty = itemView.findViewById(R.id.trans_product_qty);
                divider = itemView.findViewById(R.id.divider);
            }
        }

        @NonNull
        @Override
        public TransItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.transaction_item, parent, false);
            return new TransItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransItemViewHolder holder, int position) {
            holder.trans_product_name.setText(list.get(position).getProduct_name());
            holder.trans_product_price.setText(StringUtil.setString("%s %s", list.get(position).getCurrency_symbol(), list.get(position).getUnit_price()));
            holder.trans_product_qty.setText(StringUtil.concat(list.get(position).getQty(), " items"));

            if (position == (list.size() - 1))
                holder.divider.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


}