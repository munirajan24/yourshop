package com.dvishapps.yourshop.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.OrderItemBinding;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

import java.util.List;
import java.util.Objects;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.OrderViewHolder> {

    private List<Transaction> transactions;
    private OnTransactionListener onOrderListener;
    private OnTransactionCancelListener onTransactionCancelListener;

    public TransactionAdapter(List<Transaction> transactions, OnTransactionListener onOrderListener, OnTransactionCancelListener onTransactionCancelListener) {
        this.transactions = transactions;
        this.onOrderListener = onOrderListener;
        this.onTransactionCancelListener = onTransactionCancelListener;
    }

    public TransactionAdapter(List<Transaction> transactions, OnTransactionListener onOrderListener) {
        this.transactions = transactions;
        this.onOrderListener = onOrderListener;
    }



    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private OrderItemBinding binding;
        private OnTransactionListener onOrderListener;
        private OnTransactionCancelListener onTransactionCancelListener;

        public OrderViewHolder(@NonNull OrderItemBinding binding, OnTransactionListener onOrderListener, OnTransactionCancelListener onTransactionCancelListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onOrderListener = onOrderListener;
            this.onTransactionCancelListener = onTransactionCancelListener;
        }

        public void bind(Transaction transaction) {
            binding.setTransaction(transaction);
            binding.details.setOnClickListener(v -> {
                onOrderListener.onViewDetail(transaction.getId());
            });
            if (transaction.getTrans_status_id().equals("9")) {
                binding.valueStatus.setText("Completed");
            } else {
                binding.valueStatus.setText(transaction.getTrans_status_title());
            }

            if (transaction.getTrans_status_id().length() > 0) {
                if (transaction.getTrans_status_id().equals("1")
//                        || transaction.getTrans_status_id().equals("4")
//                        || transaction.getTrans_status_id().equals("6")
                ) {
                    if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null) {
                        if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1")) {
                            binding.cancel.setVisibility(View.GONE);
                        } else {
                            binding.cancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.cancel.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.cancel.setVisibility(View.GONE);
                }
            }


            binding.cancel.setOnClickListener(v -> {
                onTransactionCancelListener.onCancel(transaction.getId());
            });
        }
    }



    public void disableCancelForItem(String  transactionId) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(transactionId)){
                transactions.get(i).setTrans_status_id("4");
                transactions.get(i).setTrans_status_title("Accepted");
            }
        }
        notifyDataSetChanged();
    }

    public void disableCancelForRejectedItem(String  transactionId) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(transactionId)){
                transactions.get(i).setTrans_status_id("5");
                transactions.get(i).setTrans_status_title("Rejected");
            }
        }
        notifyDataSetChanged();
    }


    public void setItems(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderItemBinding binding = OrderItemBinding.inflate(inflater, parent, false);
        return new OrderViewHolder(binding, onOrderListener, onTransactionCancelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (!Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {

            transactions.get(position).setTotal_item_amount(
                    (Double.parseDouble(transactions.get(position).getTotal_item_amount())
                            - Double.parseDouble(transactions.get(position).getShipping_amount())

                    ) + ""
            );
        }
        holder.bind(transactions.get(position));
        Console.logDebug("Added_date : " + transactions.get(position).getAdded_date());
        holder.binding.txtDate.setText(Tools.getDate(transactions.get(position).getAdded_date()));
        if (transactions.get(position).getTrans_status_title().equalsIgnoreCase("Pending")) {
            holder.binding.valueStatus.setTextColor(ContextCompat.getColor(holder.binding.valueStatus.getContext(), R.color.md_red_600));
        } else if (transactions.get(position).getTrans_status_title().equalsIgnoreCase("On Delivery")) {
            holder.binding.valueStatus.setTextColor(ContextCompat.getColor(holder.binding.valueStatus.getContext(), R.color.md_orange_300));
        } else if (transactions.get(position).getTrans_status_title().equalsIgnoreCase("Completed")) {
            holder.binding.valueStatus.setTextColor(ContextCompat.getColor(holder.binding.valueStatus.getContext(), R.color.md_green_500));
        }else {
            holder.binding.valueStatus.setTextColor(ContextCompat.getColor(holder.binding.valueStatus.getContext(), R.color.md_grey_500));
        }


    }

    @Override
    public int getItemCount() {
        return transactions != null ? transactions.size() : 0;
    }

    public interface OnTransactionListener {
        void onViewDetail(String tran_id);
    }


    public interface OnTransactionCancelListener {
        void onCancel(String tran_id);
    }

}
