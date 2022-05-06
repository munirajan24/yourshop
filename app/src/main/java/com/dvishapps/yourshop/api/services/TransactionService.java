package com.dvishapps.yourshop.api.services;

import androidx.lifecycle.MutableLiveData;

import com.dvishapps.yourshop.api.ApiResponse;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.dvishapps.yourshop.api.httpClient.Http;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.models.TransactionDetail;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TransactionService {
    private static final Type transaction_type = Transaction.class;
    private static final Type transaction_product_list_type = new TypeToken<Collection<TransactionDetail>>() {
    }.getType();
    private static final Type type_list_transaction = new TypeToken<Collection<Transaction>>() {
    }.getType();
    private static final Type api_response_type = ApiResponse.class;

    @NotNull
    public static MutableLiveData<List<Transaction>> getUserOrders(String user_id, @NotNull LimitData limitData) {
        MutableLiveData<List<Transaction>> liveData = new MutableLiveData<>(null);
        String tran_url="";
//        String url = String.format(ConstantsDynamic.getInstance().ORDER_TRANSACTION_URL, Constants.API_KEY, user_id, limitData.limit, limitData.offset);
        if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null) {
            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1")){
                 tran_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/limit/%s/offset/%s";
            }else {
                 tran_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/user_id/%s/limit/%s/offset/%s";
            }
        }else {
             tran_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/user_id/%s/limit/%s/offset/%s";
        }

//             tran_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/user_id/%s/limit/%s/offset/%s";

        String url = String.format(tran_url,/* Constants.API_KEY,*/ user_id, limitData.limit, limitData.offset);
        System.out.println(url);
        Http.<List<Transaction>>fetch(url, null, type_list_transaction, liveData::setValue, error -> {

            ArrayList<Transaction> transactionArrayList = new ArrayList<>();
            Transaction transaction = new Transaction();
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {

                    transaction.setTrans_status_title("404 error" + new String(error.networkResponse.data));
                    transactionArrayList.add(transaction);
                    liveData.setValue(transactionArrayList);

                }
            } else {
                if (error.getMessage()==null){
                    transaction.setTrans_status_title("404 error"+"UnknownHostException");
                }else {
                    transaction.setTrans_status_title("404 error" + error.getMessage());
                }
                transactionArrayList.add(transaction);
                liveData.setValue(transactionArrayList);
            }
        });

        return liveData;
    }

    @NotNull
    public static MutableLiveData<Transaction> getTransaction(String user_id, String tran_id) {
        MutableLiveData<Transaction> tranData = new MutableLiveData<>(null);
        String tran_detail_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/id/%s";
        String url = String.format(tran_detail_url/*, Constants.API_KEY*//*, user_id*/, tran_id);
        System.out.println(url);
        Http.<Transaction>fetch(url, null, transaction_type, tranData::setValue, error -> {
            if (error.networkResponse.statusCode == Constants.ERROR_404) {
                tranData.setValue(null);
            }
        });
        return tranData;
    }

    @NotNull
    public static MutableLiveData<List<TransactionDetail>> getTransactionDetail(String tran_header_id, LimitData limitData) {
        MutableLiveData<List<TransactionDetail>> tranProductsData = new MutableLiveData<>(null);
//        String tran_product_list_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactiondetails/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/transactions_header_id/%s/limit/%s/offset/%s";
        String tran_product_list_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactiondetails/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/transactions_header_id/%s";
        String url = String.format(tran_product_list_url/*, Constants.API_KEY*/, tran_header_id/*, limitData.limit, limitData.offset*/);
        System.out.println(url);
        Http.<List<TransactionDetail>>fetch(url, null, transaction_product_list_type, tranProductsData::setValue, error -> {
            if (error.networkResponse.statusCode == Constants.ERROR_404) {
                tranProductsData.setValue(null);
            }
        });
        return tranProductsData;
    }


    @NotNull
    public static MutableLiveData<ApiResponse> transactionUpdate(String id, String trans_status_id) {
        MutableLiveData<ApiResponse> tranData = new MutableLiveData<>(null);

        JsonArray requestArray = new JsonArray();

        JsonObject objectId = new JsonObject();

        objectId.addProperty("key", "id");
        objectId.addProperty("value", id);

        JsonObject objectStatus = new JsonObject();

        objectStatus.addProperty("key", "trans_status_id");
        objectStatus.addProperty("value", trans_status_id);


        requestArray.add(objectId);
        requestArray.add(objectStatus);

        DynamicConstants.getInstance().clearAllData();
        String url = String.format(DynamicConstants.getInstance().transaction_update_url/*, Constants.API_KEY*//*, user_id*//*, tran_id*/);
        System.out.println(url);

        Http.<ApiResponse>post(url,  JsonUtils.toJsonString(requestArray), api_response_type, response -> {
            response.setMessage("Successfully Updated");
            tranData.setValue(response);
        }, error -> {

            ApiResponse apiResponse = new ApiResponse("","");
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
//                        tranData.setValue(new ApiResponse("404 error"+jsonObject.getString("status"), jsonObject.getString("message")));
                        tranData.setValue(new ApiResponse(jsonObject.getString("status"), jsonObject.getString("message")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    apiResponse.setMessage("404 error" + new String(error.networkResponse.data));
//                    apiResponse.setMessage( new String(error.networkResponse.data));
                    tranData.setValue(apiResponse);

                }
            } else {
                if (error.getMessage()==null){
//                    apiResponse.setMessage("404 error"+"UnknownHostException");
                    apiResponse.setMessage("No internet Connection");

                }else {
//                    apiResponse.setMessage("404 error " + error.getMessage());
                    apiResponse.setMessage( error.getMessage());
                }
                tranData.setValue(apiResponse);
            }
        });
        return tranData;
    }


    static class TranApis {
        //        public static String tran_url = Constants.HTTP + "/rest/transactionheaders/get/api_key/%s/user_id/%s/limit/%s/offset/%s";
        public static  String tran_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/user_id/%s/limit/%s/offset/%s";
        //        public String tran_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactiondetails/get/api_key/raviteja/shop_id/shop0c54d033ccebee59b6a1e7f0baa560b2";

        //        public static String tran_detail_url = Constants.HTTP + "/rest/transactionheaders/get/api_key/%s/user_id/%s/id/%s";
        public static String tran_detail_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/id/%s";
//        http://139.59.20.251/index.php/rest/transactionheaders/get/api_key/raviteja/shop_id/shop0c54d033ccebee59b6a1e7f0baa560b2/id/trans_hdr_d37a152aafdbcbd84961d349d77c79f4


        //        public static String tran_product_list_url = Constants.HTTP + "/rest/transactiondetails/get/api_key/%s/transactions_header_id/%s/limit/%s/offset/%s";
//        public static String tran_product_list_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactiondetails/get/api_key/raviteja/shop_id/"+ SessionData.getInstance().getShopKeyValue();
        public static String tran_product_list_url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/transactiondetails/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue() + "/transactions_header_id/%s/limit/%s/offset/%s";
    }

}
