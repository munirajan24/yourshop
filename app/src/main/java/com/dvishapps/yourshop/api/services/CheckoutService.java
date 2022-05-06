package com.dvishapps.yourshop.api.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.dvishapps.yourshop.api.httpClient.Http;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.BasketProduct;
import com.dvishapps.yourshop.models.Country;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutService {
    /**
     * Post transaction live data.
     *
     * @param transaction Transaction
     * @return LiveData<Transaction>   live data
     */
    @Nullable
    public static MutableLiveData<Transaction> postTransaction(Transaction transaction) {
        System.out.println(JsonUtils.toJsonString(transaction));
        MutableLiveData<Transaction> result = new MutableLiveData<>(null);
        Http.<Transaction>post(DynamicConstants.getInstance().TRANSACTION_URL,
                JsonUtils.toJsonString(transaction),
                Transaction.class,
                result::setValue,
                error -> {

                    JSONObject jsonObject = null;
                    Transaction transactionObject = new Transaction();
                    if (error.networkResponse != null) {
                        try {
                            jsonObject = new JSONObject(new String(error.networkResponse.data));
                            Console.logError(" postTransaction error : " + error.networkResponse.statusCode + " "
                                    + jsonObject.getString("message"));

                            transactionObject.setTrans_status_title("404 error" + jsonObject.getString("message"));
                            result.setValue(transactionObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (error.getMessage()==null){
                            transactionObject.setTrans_status_title("404 error"+"UnknownHostException");
                        }else {
                            transactionObject.setTrans_status_title("404 error" + error.getMessage());
                        }
                        result.setValue(transactionObject);
                    }

                    Log.e("postTransaction", "postTransaction: ", error);
                });
        return result;
    }

    /**
     * Gets zone.
     *
     * @param country_id String
     * @return LiveData<List < Country>   >
     */
    @NotNull
    public static MutableLiveData<List<Country>> getZone(@androidx.annotation.Nullable String
                                                                 country_id) {
        MutableLiveData<List<Country>> countries = new MutableLiveData<>();
        Type collectionType = new TypeToken<ArrayList<Country>>() {
        }.getType();
        JsonObject body = new JsonObject();
        body.addProperty("shop_id", Constants.SHOP_ID);
        if (country_id != null)
            body.addProperty("country_id", country_id);
        Http.<ArrayList<Country>>post(country_id != null ? Constants.CITY_URL : Constants.COUNTRY_URL,
                JsonUtils.toJsonString(body),
                collectionType,
                countries::postValue,
                error -> {

                });
        return countries;
    }

    /**
     * Generate basket products list.
     *
     * @return List<BasketProduct>   list
     */
    @NotNull
    public static List<BasketProduct> generateBasketProducts() {
        List<BasketProduct> basketProducts = new ArrayList<>();
        for (Product p : Config.cart.getProductsAsList()) {
            BasketProduct bpd = new BasketProduct(
                    p.getAttributes_header().get(0).getShop_id() != null ? p.getAttributes_header().get(0).getShop_id() : Constants.SHOP_ID,
                    p.getId(),
                    p.getName(),
                    p.getAttributes_header().get(0).getId(),
                    p.getAttributes_header().get(0).getName(),
                    p.getAttributes_header().get(0).getAttributes_detail().get(0).getAdditional_price(),
                    p.getColors().get(0).getId(),
                    p.getColors().get(0).getColor_value(),
                    String.valueOf(p.getProduct_unit()),
                    p.getProduct_measurement(),
                    p.getShipping_cost(),
                    String.valueOf(p.getUnit_price()),
                    String.valueOf(p.getOriginal_price()),
                    String.valueOf(p.getDiscount_value()),
                    String.valueOf(p.getDiscount_amount()),
                    String.valueOf(Config.cart.getProductQty().get(p.getId())),
                    String.valueOf(p.getDiscount_value()),
                    String.valueOf(p.getDiscount_percent()),
                    p.getCurrency_short_form(),
                    p.getCurrency_symbol()
            );
            basketProducts.add(bpd);
        }
        return basketProducts;
    }


    /**
     * Gets basket products.
     *
     * @return the basket products
     */
    @NotNull
    public static HashMap<String, List<BasketProduct>> getBasketProducts() {
        HashMap<String, List<BasketProduct>> basketProducts = new HashMap<>();
        for (Map.Entry<String, List<Product>> entry : Config.cart.getItems().entrySet()) {
            String s = entry.getKey();
            List<Product> prods = entry.getValue();
            List<BasketProduct> list = new ArrayList<>();
            for (Product p : prods) {
                BasketProduct bpd = new BasketProduct(
                        p.getAttributes_header().get(0).getShop_id() != null ? p.getAttributes_header().get(0).getShop_id() : Constants.SHOP_ID,
                        p.getId(),
                        p.getName(),
                        p.getAttributes_header().get(0).getId(),
                        p.getAttributes_header().get(0).getName(),
                        p.getAttributes_header().get(0).getAttributes_detail().get(0).getAdditional_price(),
                        p.getColors().get(0).getId(),
                        p.getColors().get(0).getColor_value(),
                        String.valueOf(p.getProduct_unit()),
                        p.getProduct_measurement(),
                        p.getShipping_cost(),
                        String.valueOf(p.getUnit_price()),
                        String.valueOf(p.getOriginal_price()),
                        String.valueOf(p.getDiscount_value()),
                        String.valueOf(p.getDiscount_amount()),
                        String.valueOf(Config.cart.getProductQty().get(p.getId())),
                        String.valueOf(p.getDiscount_value()),
                        String.valueOf(p.getDiscount_percent()),
                        p.getCurrency_short_form(),
                        p.getCurrency_symbol()
                );
                list.add(bpd);
            }
            basketProducts.put(prods.get(0).getCategory().getName(), list);
        }
        return basketProducts;
    }
}
