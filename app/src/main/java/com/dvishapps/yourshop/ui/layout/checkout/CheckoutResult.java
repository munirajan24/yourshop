package com.dvishapps.yourshop.ui.layout.checkout;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;

import com.dvishapps.yourshop.models.BasketProduct;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.CallActivity;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutResult extends DialogFragment {
    private static String KEYUPLOAD = "transactionHeaderUpload";
    private static String PRODUCTSBYCATEGORY = "prod";

    private RecyclerView trans_items;
    public TextView total_item_text_view;
    private TextView trans_amount;
    public TextView success_text;
    private AppCompatButton trans_done_btn;
    public AppCompatButton order_cancel_btn;
    public Transaction transactionHeaderUpload;
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
    private TextView txt_payment_mode_value;
    private TextView txt_time_value;
    private TextView txt_distance_value;
    private TextView overAllTax;
    public ImageView img_success;
    public ImageView img_failure;

    private TransParentAdapter transaction_parent_adapter;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity(), RecyclerView.VERTICAL, false);


    TextView packageChargeValue;
    TextView packageChargeTitle;

    boolean cancelClickable = true;
    private ProgressDialog progressDialog;
    private TransactionViewModel transactionViewModel;

    public static CheckoutResult newInstance(Transaction transactionHeaderUpload, HashMap<String, List<BasketProduct>> prods) {
        Bundle args = new Bundle();
        args.putSerializable(KEYUPLOAD, transactionHeaderUpload);
        args.putSerializable(PRODUCTSBYCATEGORY, prods);
        CheckoutResult fragment = new CheckoutResult();
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
        progressDialog = new ProgressDialog(getActivity(), "Canceling order...");
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        SessionData.getInstance().setCheckoutResult(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.cart.clearCart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transation_details, container, false);
        trans_items = root.findViewById(R.id.trans_items);
        success_text = root.findViewById(R.id.success_text);
        total_item_text_view = root.findViewById(R.id.total_item_text_view);
        trans_amount = root.findViewById(R.id.trans_amount);
        trans_done_btn = root.findViewById(R.id.trans_done_btn);
        order_cancel_btn = root.findViewById(R.id.order_cancel_btn);

        img_success = root.findViewById(R.id.img_success);
        img_failure = root.findViewById(R.id.img_failure);
        totalItemCountValue = root.findViewById(R.id.totalItemCountValue);
        totalValue = root.findViewById(R.id.totalValue);
        subtotalValue = root.findViewById(R.id.subtotalValue);
        taxValue = root.findViewById(R.id.taxValue);
        deliveryChargeValue = root.findViewById(R.id.deliveryChargeValue);
        finalTotalValue = root.findViewById(R.id.finalTotalValue);
        txt_payment_mode_value = root.findViewById(R.id.txt_payment_mode_value);
        txt_time_value = root.findViewById(R.id.txt_time_value);
        txt_distance_value = root.findViewById(R.id.txt_distance_value);
        overAllTax = root.findViewById(R.id.overAllTax);
        packageChargeTitle = root.findViewById(R.id.packageChargeTitle);
        packageChargeValue = root.findViewById(R.id.packageChargeValue);

//        binding.setOverAllTaxValue(SessionData.getInstance().getOverAllTaxValue());
        if (SessionData.getInstance().getOverAllTaxValue() == 0) {
            overAllTax.setVisibility(View.GONE);
            taxValue.setVisibility(View.GONE);
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));

        SessionData.getInstance().setSuccess_text(success_text);
        success_text.setText(StringUtil.setString("Thank you for ordering with %s please wait till the hotel accept it", SessionData.getInstance().getShop().getName()));
        total_item_text_view.setText(StringUtil.setString(getString(R.string.transaction_success_for), transactionHeaderUpload.getDetails().size()));
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

        if (transactionHeaderUpload.getPackage_charges().length() > 0) {
            if (Float.parseFloat(transactionHeaderUpload.getPackage_charges()) > 0) {
                packageChargeValue.setText(transactionHeaderUpload.getCurrency_symbol() + " " + transactionHeaderUpload.getPackage_charges());
            } else {
                packageChargeValue.setVisibility(View.GONE);
                packageChargeTitle.setVisibility(View.GONE);
            }
        } else {
            packageChargeValue.setVisibility(View.GONE);
            packageChargeTitle.setVisibility(View.GONE);
        }
//        shippingTaxValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + shipping_tax_amount);

//        couponDiscountValue.setText("-");
//        couponDiscountValueInput.setText("");

        overAllTax.setText(String.format(getString(R.string.tax), SessionData.getInstance().getOverAllTaxValue() + ""));
//        shippingTax.setText(String.format(getString(R.string.shipping_tax), Config.TAX_SHIPPING_VALUE));


        trans_items.setLayoutManager(linearLayoutManager);
        trans_items.setAdapter(transaction_parent_adapter);

        if (transactionHeaderUpload.getAdded_date().length() != 0) {
            txt_time_value.setText(Tools.getDateAndTime(transactionHeaderUpload.getAdded_date()));
        }

        if (transactionHeaderUpload.getRazor_payment_status().length() == 0) {
            txt_payment_mode_value.setText("Cash On Delivery");
        } else {
            txt_payment_mode_value.setText("Online Payment");
        }

//        txt_distance_value.setText(transactionHeaderUpload.getDiscount_amount());

        trans_done_btn.setOnClickListener(v -> {
            this.dismiss();
        });

        order_cancel_btn.setOnClickListener(v -> {
//            this.dismiss();
            if (cancelClickable) {
                cancelClickable = false;
                cancelOrder(transactionHeaderUpload.getId());
            }
        });

        return root;
    }


    private void cancelOrder(String tran_id) {
        customAlertNewCancelOrder(getActivity(), "Are you sure to cancel your order?", "Cancel Order", "OK", "Close", tran_id);
    }

    public void customAlertNewCancelOrder(Context mContext, String message, String title, String strOk, String strCancel, String tran_id) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);
        View view_cancel = (View) dialog.findViewById(R.id.view_cancel);

        ImageView closeImg = dialog.findViewById(R.id.close_img);

        txtTitle.setText(title);
        txtMessage.setText(message);
        ok.setText(strOk);
        cancel.setText(strCancel);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStatus("7", tran_id);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickable = true;

                dialog.dismiss();

            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickable = true;

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void updateStatus(String status_id, String tran_id) {
        progressDialog.startLoading();

        transactionViewModel.updateTransactionStatus(tran_id, status_id)
                .observe(this.getViewLifecycleOwner(), apiResponse -> {
                    if (apiResponse != null) {
                        cancelClickable = true;
                        progressDialog.dismiss();
                        if (!Tools.isOnline()) {
                            //                    if (apiResponse.getStatus().contains("404 error")){
                            //                        internetSnack(binding.parentLayout);
                            Console.toast("No internet Connection");
                        } else {
                            //                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                            if (apiResponse != null) {
                                if (apiResponse.getMessage().toLowerCase().contains("success")) {
                                    Console.toast("Order cancelled");
//                                    setUpUserOrders();
                                    order_cancel_btn.setVisibility(View.GONE);

                                    success_text.setText("Your order cancelled");
                                    img_failure.setVisibility(View.VISIBLE);
                                    img_success.setVisibility(View.GONE);
                                    total_item_text_view.setVisibility(View.GONE);
                                    success_text.setTextColor(Color.parseColor("#FF1100"));
                                } else {
                                    Console.toast(apiResponse.getMessage());
                                }

                                //      TODO : move to selected fragment
//                                Bundle bundle = new Bundle();
//                                bundle.putString("ORDER_TAB", status_id);
//                                SessionData.getInstance().setOrderTabSelection(status_id);
//                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_transaction_detail_to_OrdersSFragment, bundle);

                            } else {
//                                dialog.dismiss();
                            }

                        }
                    }
                });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
        SessionData.getInstance().getBottomNavigation().setCount(R.id.nav_order, ((int) (SessionData.getInstance().getOrdersCount()) + 1) + "");
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getExtras().getString("textResponse") != null) {
                    if (intent.getExtras().getString("transactionId") != null) {
                        String id = intent.getExtras().getString("transactionId");

                        if (id.equals(transactionHeaderUpload.getId())) {
                            if (intent.getExtras().getString("textResponse").toLowerCase().contains("reject")) {
                                success_text.setText("Order rejected by the hotel");
                                img_failure.setVisibility(View.VISIBLE);
                                img_success.setVisibility(View.GONE);
                                total_item_text_view.setVisibility(View.GONE);
                                success_text.setTextColor(Color.parseColor("#FF1100"));
                            } else if (intent.getExtras().getString("textResponse").toLowerCase().contains("accept")) {
                                //                success_text.setTextColor(Color.parseColor("#2CF134"));
                                img_failure.setVisibility(View.GONE);
                                img_success.setVisibility(View.VISIBLE);
                                success_text.setText("Order accepted by the hotel");
                            }
                        }
                    }
                }
                if (intent.getExtras().getString("removeCancel") != null) {
                    if (intent.getExtras().getString("removeCancel").toLowerCase().contains("true")) {
                        String id = intent.getExtras().getString("transactionId");

                        if (CheckoutResult.this.transactionHeaderUpload.getId().equals(id)) {
                            order_cancel_btn.setVisibility(View.GONE);

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}