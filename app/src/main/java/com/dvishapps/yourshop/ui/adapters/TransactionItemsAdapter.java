package com.dvishapps.yourshop.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.databinding.ProfileTransactionItemBinding;
import com.dvishapps.yourshop.models.TransactionDetail;
import com.dvishapps.yourshop.models.Transaction;

import java.util.List;

public class TransactionItemsAdapter extends RecyclerView.Adapter<TransactionItemsAdapter.TransactionItemViewHolder> {

    private List<TransactionDetail> transactionDetails;
    private Transaction transaction;

    public TransactionItemsAdapter(List<TransactionDetail> transactionDetails, Transaction transaction) {
        this.transactionDetails = transactionDetails;
        this.transaction = transaction;
    }

    public void setItems(List<TransactionDetail> transactionDetails, Transaction transaction) {
        this.transactionDetails = transactionDetails;
        this.transaction = transaction;
        notifyDataSetChanged();
    }

    public class TransactionItemViewHolder extends RecyclerView.ViewHolder {
        private ProfileTransactionItemBinding binding;

        public TransactionItemViewHolder(@NonNull ProfileTransactionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TransactionDetail detail, Transaction transaction) {
            binding.setTran(transaction);
            binding.setProd(detail);
            try {
                binding.valueSubTotal.setText(detail.getCurrency_symbol() + " "+
                        (Integer.parseInt(detail.getPrice())* Integer.parseInt(detail.getQty())));
            } catch (NumberFormatException e) {
                binding.valueSubTotal.setText("");
                        e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public TransactionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ProfileTransactionItemBinding binding = ProfileTransactionItemBinding.inflate(inflater, parent, false);
        return new TransactionItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionItemViewHolder holder, int position) {
        holder.bind(transactionDetails.get(position), transaction);
    }

    @Override
    public int getItemCount() {
        return transactionDetails != null ? transactionDetails.size() : 0;
    }
}
