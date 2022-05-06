package com.dvishapps.yourshop.api.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.dvishapps.yourshop.api.httpClient.Http;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private static Type shop_response_type = new TypeToken<List<Shop>>() {
    }.getType();
    private static Type user_type = User.class;

    private static final Type shop_details_response_type = Shop.class;
    private static final Type distance_response_type = JSONObject.class;

    @NotNull
    public static MutableLiveData<Shop> getShopDetails() {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        Http.<Shop>fetch(/*ShopApis.shop_details_url*/DynamicConstants.getInstance().shop_details_url, null, shop_response_type, response::setValue, error -> {
            try {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    response.setValue(null);
                }
            } catch (Exception e) {
                response.setValue(null);
                e.printStackTrace();
            }
        });
        return response;
    }

    @NotNull
    public static MutableLiveData<Shop> getShopDetails2() {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = String.format(DynamicConstants.getInstance().shop_details_urlCustom, SessionData.getInstance().getShopKeyValue()
        );
        Http.<List<Shop>>fetch(/*ShopApis.shop_details_url*/ url, null, shop_response_type, shopResponse -> {
//            int distance = 4;//temp
            if (shopResponse != null) {
                if (shopResponse.size() > 0) {
                    response.setValue(shopResponse.get(0));
                    SessionData.getInstance().setShop(shopResponse.get(0));
                    ShopService.setLatLongAndCalculateDeliveryCharge(SessionData.getInstance().getShop());
                }
            }
//            if (shopResponse.get(0).getShipping_tax_value() < 5) {
//                shopResponse.get(0).setShipping_tax_value(5);
//            }
//            shopResponse.get(0).setCod_charge_from("0");//temp
//            float deliveryCharge = 0;
//            boolean twoWayDelivery = false;
//
//            SessionData.getInstance().setShopShippingTaxValue(shopResponse.get(0).getShipping_tax_value());
//            SessionData.getInstance().setShopLat(shopResponse.get(0).getLat());
//            SessionData.getInstance().setShopLng(shopResponse.get(0).getLng());
//
//            if (!Config.sharedPreferences.getString(Constants.LNG, "0").equals("0")) {
//                Config.editPreferences.putString(Constants.DELIVERY_CHARGE_CALCULATED, "TRUE").apply();
//            }
//            float distance = Tools.distanceInKms(
//                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
//                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),
//
//                    Double.parseDouble(shopResponse.get(0).getLat()),
//                    Double.parseDouble(shopResponse.get(0).getLng())
//            );
//
//            if (shopResponse != null) {
//                if (shopResponse.size() > 0) {
//                    response.setValue(shopResponse.get(0));
//                    SessionData.getInstance().setOverAllTaxValue(shopResponse.get(0).getOverall_tax_value());
//
//                    //TODO: cod_charge_from : 0 for from customer, 1 for from owner
////                    if (shopResponse.get(0).getCod_charge_from().equals("0")) {
//                    if (!Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals(shopResponse.get(0).getCod_charge_from())) {
//                        Config.editPreferences.putString(Constants.COD_CHARGE_FROM, shopResponse.get(0).getCod_charge_from()).apply();
//                    }
////                    }
//
//                    //TODO: cod_needed_type : 0 for  END_OF_THE_DAY, 1 for END_OF_DELIVERY
//                    if (shopResponse.get(0).getCod_needed_type().equals("0")) {
//                        twoWayDelivery = false;
//                    } else {
//                        twoWayDelivery = true;
//                    }
//                    twoWayDelivery = false;//Hard coded
//                    SessionData.getInstance().setTwoWayDelivery(twoWayDelivery);
//                    //TODO : calculate distance
//                    //todo: if distance is more than 2kms ,  calculate with formula
//                    if (distance > 2) {
//                        if (twoWayDelivery) {
//                            //TODO : calculate with formula : distance * shipping tax * 2       ---for two way delivery      (or)
//                            deliveryCharge = 20 + shopResponse.get(0).getShipping_tax_value() * (int) (distance * 2 - 2);
////                                Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value() * (distance - 2) * 2);
//                        } else {
//                            //TODO : calculate with formula : distance * shipping tax      ---for one way delivery
////                                Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value() * distance);
//                            deliveryCharge = 20 + shopResponse.get(0).getShipping_tax_value() * (int) (distance - 2);
//                        }
//                    } else {
//                        deliveryCharge = 20;
////                            Config.cart.setDelivery_charge(deliveryCharge);
//                    }
//
////                        SessionData.getInstance().setShippingTaxValue(deliveryCharge);
////                        Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value());
////                    Config.editPreferences.putString(Constants.DELIVERY_CHARGE, shopResponse.get(0).getCod_charge_from()).apply();
//                    SessionData.getInstance().setShippingTaxValue(deliveryCharge);
//                    Config.cart.setDelivery_charge(deliveryCharge);
//
//                    SessionData.getInstance().setShop(shopResponse.get(0));
//                }
//            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    shop.setStatus("404 error" + jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (error.getMessage() == null) {
                    shop.setStatus("404 error" + "UnknownHostException");
                } else {
                    shop.setStatus("404 error" + error.getMessage());
                }
                response.setValue(shop);
            }

            Log.e("getShopDetails2", "getShopDetails2: ", error);
        });
        return response;
    }

    @NotNull
    public static MutableLiveData<Shop> getShopDetails3(String shop_id) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = String.format(DynamicConstants.getInstance().shop_details_urlCustom, shop_id);
        Http.<List<Shop>>fetch(/*ShopApis.shop_details_url*/ url, null, shop_response_type, shopResponse -> {
            if (shopResponse != null) {
                if (shopResponse.size() > 0) {
                    response.setValue(shopResponse.get(0));
                }
            }
        }, error -> {
            try {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    response.setValue(null);
                }
            } catch (Exception e) {
                response.setValue(null);
                e.printStackTrace();
            }
        });
        return response;
    }


    @NotNull
    public static MutableLiveData<Shop> setShopSettings(String shop_open_time, String shop_close_time,
                                                        String shop_branch, String shop_main_branch_id,
                                                        String shop_current_version, String shop_app_force_update,
                                                        String shop_id, String package_charges,
                                                        String address2, String lat, String lng, String postal_code,
                                                        String shipping_tax_value, String overall_tax_value

    ) {
        MutableLiveData<Shop> response = new MutableLiveData<>();

        JsonArray requestArray = new JsonArray();

        JsonObject objectShop_open_time = new JsonObject();

        objectShop_open_time.addProperty("key", "shop_open_time");
        objectShop_open_time.addProperty("value", shop_open_time);

        JsonObject objectShop_close_time = new JsonObject();

        objectShop_close_time.addProperty("key", "shop_close_time");
        objectShop_close_time.addProperty("value", shop_close_time);


        JsonObject objectShop_branch = new JsonObject();

        objectShop_branch.addProperty("key", "shop_branch");
        objectShop_branch.addProperty("value", shop_branch);


        JsonObject objectShop_main_branch_id = new JsonObject();

        objectShop_main_branch_id.addProperty("key", "shop_main_branch_id");
        objectShop_main_branch_id.addProperty("value", shop_main_branch_id);


        JsonObject objectShop_current_version = new JsonObject();

        objectShop_current_version.addProperty("key", "shop_current_version");
        objectShop_current_version.addProperty("value", shop_current_version);


        JsonObject objectShop_app_force_update = new JsonObject();

        objectShop_app_force_update.addProperty("key", "shop_app_force_update");
        objectShop_app_force_update.addProperty("value", shop_app_force_update);


        JsonObject objectShop_id = new JsonObject();

        objectShop_id.addProperty("key", "shop_id");
        objectShop_id.addProperty("value", shop_id);

        JsonObject objectPackage_charges = new JsonObject();

        objectPackage_charges.addProperty("key", "package_charges");
        objectPackage_charges.addProperty("value", package_charges);

        JsonObject objectShipping_tax_value = new JsonObject();

        objectShipping_tax_value.addProperty("key", "shipping_tax_value");
        objectShipping_tax_value.addProperty("value", shipping_tax_value);

        JsonObject objectOverall_tax_value = new JsonObject();

        objectOverall_tax_value.addProperty("key", "overall_tax_value");
        objectOverall_tax_value.addProperty("value", overall_tax_value);

//        String address2,String lat,String lng, String postal_code

        JsonObject objectAddress2 = new JsonObject();

        objectAddress2.addProperty("key", "address2");
        objectAddress2.addProperty("value", address2);

        JsonObject objectLat = new JsonObject();

        objectLat.addProperty("key", "lat");
        objectLat.addProperty("value", lat);

        JsonObject objectLng = new JsonObject();

        objectLng.addProperty("key", "lng");
        objectLng.addProperty("value", lng);

        JsonObject objectPostal_code = new JsonObject();

        objectPostal_code.addProperty("key", "postal_code");
        objectPostal_code.addProperty("value", postal_code);


        requestArray.add(objectShop_open_time);
        requestArray.add(objectShop_close_time);
        requestArray.add(objectShop_branch);
        requestArray.add(objectShop_main_branch_id);
        requestArray.add(objectShop_current_version);
        requestArray.add(objectShop_app_force_update);
        requestArray.add(objectShop_id);
        requestArray.add(objectPackage_charges);
        requestArray.add(objectShipping_tax_value);
        requestArray.add(objectOverall_tax_value);
        requestArray.add(objectAddress2);
        requestArray.add(objectLat);
        requestArray.add(objectLng);
        requestArray.add(objectPostal_code);

        String url = String.format(DynamicConstants.getInstance().shop_settings_update_url, SessionData.getInstance().getShopKeyValue()
        );
        Http.<Shop>post(/*ShopApis.shop_details_url*/ url, JsonUtils.toJsonString(requestArray), shop_details_response_type, shopResponse -> {
//            int distance = 4;//temp
            response.setValue(shopResponse);

        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    shop.setStatus("404 error" + jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (error.getMessage() == null) {
                    shop.setStatus("404 error" + "UnknownHostException");
                } else {
                    shop.setStatus("404 error" + error.getMessage());
                }
                response.setValue(shop);
            }

            Log.e("getShopDetails2", "getShopDetails2: ", error);
        });
        return response;
    }


    private static class ShopApis {
        public static String shop_details_url = StringUtil.setString("%s/rest/shops/get_shop_info/api_key/%s", Constants.HTTP, Constants.API_KEY);
    }

    public static void setLatLongAndCalculateDeliveryCharge(Shop shopResponse) {
//            int distance = 4;//temp
        if (shopResponse.getShipping_tax_value() < 5) {
            shopResponse.setShipping_tax_value(5);
        }
        shopResponse.setCod_charge_from("0");//temp
        float deliveryCharge = 0;
        boolean twoWayDelivery = false;

        SessionData.getInstance().setShopShippingTaxValue(shopResponse.getShipping_tax_value());
        SessionData.getInstance().setShopLat(shopResponse.getLat());
        SessionData.getInstance().setShopLng(shopResponse.getLng());

        if (!Config.sharedPreferences.getString(Constants.LNG, "0").equals("0")) {
            Config.editPreferences.putString(Constants.DELIVERY_CHARGE_CALCULATED, "TRUE").apply();
        }
        Config.editPreferences.putString(Constants.COD_CHARGE_FROM, shopResponse.getCod_charge_from()).apply();

//        double distance = Tools.distanceInKms(
//                Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
//                Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),
//
//                Double.parseDouble(shopResponse.getLat()),
//                Double.parseDouble(shopResponse.getLng())
//        );
//
//        Console.toast("Distance shop : " + distance);

        getLiveDistance(Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
                Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),

                Double.parseDouble(shopResponse.getLat()),
                Double.parseDouble(shopResponse.getLng())
                , shopResponse
        );

//        {
//            SessionData.getInstance().setOverAllTaxValue(shopResponse.getOverall_tax_value());
//
//            //TODO: cod_charge_from : 0 for from customer, 1 for from owner
////                    if (shopResponse.getCod_charge_from().equals("0")) {
//            if (!Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals(shopResponse.getCod_charge_from())) {
//                Config.editPreferences.putString(Constants.COD_CHARGE_FROM, shopResponse.getCod_charge_from()).apply();
//            }
////                    }
//
//            //TODO: cod_needed_type : 0 for  END_OF_THE_DAY, 1 for END_OF_DELIVERY
//            if (shopResponse.getCod_needed_type().equals("0")) {
//                twoWayDelivery = false;
//            } else {
//                twoWayDelivery = true;
//            }
//            twoWayDelivery = false;//Hard coded
//            SessionData.getInstance().setTwoWayDelivery(twoWayDelivery);
//            //TODO : calculate distance
//            //todo: if distance is more than 2kms ,  calculate with formula
//            if (distance > 2) {
//                if (twoWayDelivery) {
//                    //TODO : calculate with formula : distance * shipping tax * 2       ---for two way delivery      (or)
//                    deliveryCharge = 20 + shopResponse.getShipping_tax_value() * (int) (distance * 2 - 2);
////                                Config.cart.setDelivery_charge(20 + shopResponse.getShipping_tax_value() * (distance - 2) * 2);
//                } else {
//                    //TODO : calculate with formula : distance * shipping tax      ---for one way delivery
////                                Config.cart.setDelivery_charge(20 + shopResponse.getShipping_tax_value() * distance);
//                    deliveryCharge = 20 + shopResponse.getShipping_tax_value() * (int) (distance - 2);
//                }
//            } else {
//                deliveryCharge = 20;
////                            Config.cart.setDelivery_charge(deliveryCharge);
//            }
//
////                        SessionData.getInstance().setShippingTaxValue(deliveryCharge);
////                        Config.cart.setDelivery_charge(20 + shopResponse.getShipping_tax_value());
////                    Config.editPreferences.putString(Constants.DELIVERY_CHARGE, shopResponse.getCod_charge_from()).apply();
//            SessionData.getInstance().setShippingTaxValue(deliveryCharge);
//            Config.cart.setDelivery_charge(deliveryCharge);
//
//            SessionData.getInstance().setShop(shopResponse);
//        }
    }


    @NotNull
    public static MutableLiveData<List<Shop>> getCustomShopBranchList(String lat, String lng, String shop_main_branch_id) {
        MutableLiveData<List<Shop>> liveData = new MutableLiveData<>(null);

        JsonArray requestArray = new JsonArray();

        JsonObject objectId = new JsonObject();

        objectId.addProperty("key", "shop_main_branch_id");
        objectId.addProperty("value", shop_main_branch_id);

        JsonObject objectLat = new JsonObject();

        objectLat.addProperty("key", "lat");
        objectLat.addProperty("value", lat);

        JsonObject objectLng = new JsonObject();

        objectLng.addProperty("key", "lng");
        objectLng.addProperty("value", lng);

        requestArray.add(objectId);
        requestArray.add(objectLat);
        requestArray.add(objectLng);
        DynamicConstants.getInstance().clearAllData();
        String url = DynamicConstants.getInstance().custom_shop_list_url;
        System.out.println(url);
        Http.<List<Shop>>post(url, JsonUtils.toJsonString(requestArray), shop_response_type, response -> {

            if (response.size() != 0) {
                liveData.setValue(response);
            } else {
                List<Shop> shopDetail = new ArrayList<>();
                Shop shopDetail1 = new Shop();
                shopDetail.add(shopDetail1);
                shopDetail.get(0).setId("404 error" + "No records found");
                liveData.setValue(shopDetail);
            }

        }, error -> {

            List<Shop> shopDetail = new ArrayList<>();
            Shop shopDetail1 = new Shop();
            shopDetail.add(shopDetail1);

            if (error.networkResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    shopDetail.get(0).setStatus("404 error" + jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                liveData.setValue(shopDetail);

//                }
            } else {
                if (error.getMessage() == null) {
                    shopDetail.get(0).setStatus("404 error" + "UnknownHostException");
                } else {
                    shopDetail.get(0).setStatus("404 error" + error.getMessage());
                }
                liveData.setValue(shopDetail);
            }
        });

        return liveData;
    }


    @NotNull
    public static MutableLiveData<List<Shop>> getCustomLocationShopList(String lat, String lng) {
        MutableLiveData<List<Shop>> liveData = new MutableLiveData<>(null);

        JsonArray requestArray = new JsonArray();


        JsonObject objectLat = new JsonObject();

        objectLat.addProperty("key", "lat");
        objectLat.addProperty("value", lat);

        JsonObject objectLng = new JsonObject();

        objectLng.addProperty("key", "lng");
        objectLng.addProperty("value", lng);

        requestArray.add(objectLat);
        requestArray.add(objectLng);
        DynamicConstants.getInstance().clearAllData();
        String url = DynamicConstants.getInstance().custom_shop_list_url;
        System.out.println(url);
        Http.<List<Shop>>post(url, JsonUtils.toJsonString(requestArray), shop_response_type, response -> {

            if (response.size() != 0) {
                liveData.setValue(response);
            } else {
                List<Shop> shopDetail = new ArrayList<>();
                Shop shopDetail1 = new Shop();
                shopDetail.add(shopDetail1);
                shopDetail.get(0).setId("404 error" + "No records found");
                liveData.setValue(shopDetail);
            }

        }, error -> {

            List<Shop> shopDetail = new ArrayList<>();
            Shop shopDetail1 = new Shop();
            shopDetail.add(shopDetail1);

            if (error.networkResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    shopDetail.get(0).setStatus("404 error" + jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                liveData.setValue(shopDetail);

//                }
            } else {
                if (error.getMessage() == null) {
                    shopDetail.get(0).setStatus("404 error" + "UnknownHostException");
                } else {
                    shopDetail.get(0).setStatus("404 error" + error.getMessage());
                }
                liveData.setValue(shopDetail);
            }
        });

        return liveData;
    }


    @NotNull
    public static MutableLiveData<JsonObject> getLiveDistance(double lat1, double lon1, double lat2, double lon2, Shop shopResponse) {
        MutableLiveData<JsonObject> response = new MutableLiveData<>();
        String myurl = String.format(DynamicConstants.getInstance().google_live_distance_url, lat1, lon1, lat2, lon2);

        StringRequest stringRequest = new StringRequest(myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distanceJson = steps.getJSONObject("distance");
//                    String parsedDistance = distanceJson.getString("text");
                    String parsedDistance = distanceJson.getString("value");

                    {
                        double distance = 0;
                        try {
                            distance = Double.parseDouble(parsedDistance) / 1000;
                            Console.logDebug("distance : " + distance);
                        } catch (Exception e) {
                            e.printStackTrace();
                            distance = Tools.distanceInKms(
                                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
                                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),

                                    Double.parseDouble(SessionData.getInstance().getShopLat()),
                                    Double.parseDouble(SessionData.getInstance().getShopLng())
                            );
                        }
                        distance = Math.ceil(distance);
                        if (shopResponse.getShipping_tax_value() < 5) {
                            shopResponse.setShipping_tax_value(5);
                        }
                        shopResponse.setCod_charge_from("0");//temp
                        float deliveryCharge = 0;
                        boolean twoWayDelivery = false;

                        SessionData.getInstance().setShopShippingTaxValue(shopResponse.getShipping_tax_value());
                        SessionData.getInstance().setShopLat(shopResponse.getLat());
                        SessionData.getInstance().setShopLng(shopResponse.getLng());

                        if (!Config.sharedPreferences.getString(Constants.LNG, "0").equals("0")) {
                            Config.editPreferences.putString(Constants.DELIVERY_CHARGE_CALCULATED, "TRUE").apply();
                        }

                        {
                            SessionData.getInstance().setOverAllTaxValue(shopResponse.getOverall_tax_value());

                            //TODO: cod_charge_from : 0 for from customer, 1 for from owner
//                    if (shopResponse.getCod_charge_from().equals("0")) {
//                            if (!Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals(shopResponse.getCod_charge_from())) {
                            Config.editPreferences.putString(Constants.COD_CHARGE_FROM, shopResponse.getCod_charge_from()).apply();
//                            }
//                    }

                            //TODO: cod_needed_type : 0 for  END_OF_THE_DAY, 1 for END_OF_DELIVERY
                            if (shopResponse.getCod_needed_type().equals("0")) {
                                twoWayDelivery = false;
                            } else {
                                twoWayDelivery = true;
                            }
                            twoWayDelivery = false;//Hard coded
                            SessionData.getInstance().setTwoWayDelivery(twoWayDelivery);
                            //TODO : calculate distance
                            //todo: if distance is more than 2kms ,  calculate with formula
                            if (distance > 2) {
                                if (twoWayDelivery) {
                                    //TODO : calculate with formula : distance * shipping tax * 2       ---for two way delivery      (or)
                                    deliveryCharge = 20 + shopResponse.getShipping_tax_value() * (int) (distance * 2 - 2);
//                                Config.cart.setDelivery_charge(20 + shopResponse.getShipping_tax_value() * (distance - 2) * 2);
                                } else {
                                    //TODO : calculate with formula : distance * shipping tax      ---for one way delivery
//                                Config.cart.setDelivery_charge(20 + shopResponse.getShipping_tax_value() * distance);
                                    deliveryCharge = 20 + shopResponse.getShipping_tax_value() * (int) (distance - 2);
                                }
                            } else {
                                deliveryCharge = 20;
//                            Config.cart.setDelivery_charge(deliveryCharge);
                            }

//                        SessionData.getInstance().setShippingTaxValue(deliveryCharge);
//                        Config.cart.setDelivery_charge(20 + shopResponse.getShipping_tax_value());
//                    Config.editPreferences.putString(Constants.DELIVERY_CHARGE, shopResponse.getCod_charge_from()).apply();
                            SessionData.getInstance().setShippingTaxValue(deliveryCharge);
                            Config.cart.setDelivery_charge(deliveryCharge);

                            SessionData.getInstance().setShop(shopResponse);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Console.error("error distance : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Config.context);
        requestQueue.add(stringRequest);

        return response;
    }


}
