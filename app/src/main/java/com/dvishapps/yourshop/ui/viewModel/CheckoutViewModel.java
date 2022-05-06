package com.dvishapps.yourshop.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dvishapps.yourshop.api.services.CheckoutService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.Country;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;

import java.util.HashMap;
import java.util.List;

public class CheckoutViewModel extends SViewModel {
    public HashMap<String, List<Product>> products = Config.cart.getItems() != null ? Config.cart.getItems() : new HashMap<>();
    public MutableLiveData<List<Country>> countries_data;
    public static User currentUser = null;

    public void fetchBasketProducts() {

//        basketProds = Transformations.switchMap(Config.cart.prodCart, input -> Config.cart.getProdCart());
    }

    public LiveData<Cart> getBasketProds() {
        return Config.cart.changeListener;
    }

    public LiveData<Transaction> postTransaction(Transaction transaction) {
        return CheckoutService.postTransaction(transaction);
    }
    public LiveData<List<Country>> getZone(String country_id) {
        return CheckoutService.getZone(country_id);
    }
}
