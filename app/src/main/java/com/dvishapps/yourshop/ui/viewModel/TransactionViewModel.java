package com.dvishapps.yourshop.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dvishapps.yourshop.api.ApiResponse;
import com.dvishapps.yourshop.api.services.TransactionService;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.models.TransactionDetail;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionViewModel extends SViewModel {
    public User user;
    public String TRAN_ID = "TRAN_ID";
    public String USER_ID = "USER_ID";
    public LiveData<List<Transaction>> transactions_data = new MutableLiveData<>(new ArrayList<>());
    public LiveData<Transaction> transaction_data = new MutableLiveData<>(null);
    public LiveData<List<TransactionDetail>> transaction_product_list_data = new MutableLiveData<>(new ArrayList<>());

    public int transactionCount = 0;

    public TransactionViewModel() {
    }

    public void fetchOrders(String user_id) {
        transactions_data = TransactionService.getUserOrders(user_id, new LimitData(0, 10));
        List<Transaction> transactions = transactions_data.getValue();
        if (transactions != null) {
            transactionCount = transactions.size();
        }
    }

    public LiveData<List<Transaction>> getTransactions_data() {
        return transactions_data;
    }

    public void fetchTransactionDetail(String user_id, String tran_id) {
        transaction_data = TransactionService.getTransaction(user_id, tran_id);
        transaction_product_list_data = TransactionService.getTransactionDetail(tran_id, new LimitData(0, 50));
    }

    public void fetchTransactionProducts(String tran_header_id) {

    }

    public LiveData<Transaction> getTransaction_data() {
        return transaction_data;
    }

    public LiveData<List<TransactionDetail>> getTransaction_product_list_data() {
        return transaction_product_list_data;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }


    public MutableLiveData<ApiResponse> updateTransactionStatus(String id, String trans_status_id){
//        transaction_update_data=TransactionService.transactionUpdate(id,trans_status_id);
        return TransactionService.transactionUpdate(id,trans_status_id);

    }

}
