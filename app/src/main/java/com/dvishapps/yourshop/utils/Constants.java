package com.dvishapps.yourshop.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;

public class Constants {
    public static String CHANNEL_ID = "com.dvishapps.yourshop";
    public static String CHANNEL_NAME = "New Order";
    public static int NOTIFICATION_ID = 65;
    public static Ringtone ringtone = null;
    public static NotificationManager nm = null;

    public static final String ONE = "1";
    // api urls
    public static String USER_ID = "usr220660e2d111529faa70d141610e20e7";

    public static String SHOP_ID = "shop0b69bc5dbd68bbd57ea13dfc5488e20a";

    public static String API_KEY = "raviteja";

//    public static String IMG_URL = "http://www.dvishapps.com/chendhur/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/chendhur/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/chendhur/index.php";

//
//    public static String IMG_URL = "http://www.dvishapps.com/saistores/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/saistores/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/saistores/index.php";


//    public static String IMG_URL = "http://www.dvishapps.com/bluewave/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/bluewave/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/bluewave/index.php";
//
//    public static String IMG_URL = "http://www.dvishapps.com/amutham_sm/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/amutham_sm/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/amutham_sm/index.php";

//    public static String IMG_URL = "http://www.dvishapps.com/velsm/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/velsm/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/velsm/index.php";
    // TODO: Current SHOP_KEY_NAME for URL

    public static String SHOP_KEY_NAME = "velsm";


    //    public static String IMG_URL = "http://www.dvishapps.com/" + SHOP_KEY_NAME + "/uploads/";
//    public static String IMG_URL = "http://www.dvishapps.com/bde/uploads/";
    public static String IMG_URL = DynamicConstants.HTTP_SERVER_URL + "/uploads/";

    public static String THUMB_IMG_URL = "http://www.dvishapps.com/" + SHOP_KEY_NAME + "/uploads/thumbnail/";

    public static String HTTP = "http://www.dvishapps.com/" + SHOP_KEY_NAME + "/index.php";


//    public static String IMG_URL = "http://www.dvishapps.com/arabian/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/arabian/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/arabian/index.php";

//
//    public static String IMG_URL = "http://www.dvishapps.com/bawarchi/uploads/";
//
//    public static String THUMB_IMG_URL = "http://www.dvishapps.com/bawarchi/uploads/thumbnail/";
//
//    public static String HTTP = "http://www.dvishapps.com/bawarchi/index.php";


    public static String FEATURED_PRODUCTS_URL = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/is_featured/1/limit/0/offset/10/cat_id/%s";

    public static String FEATURED_PRODUCTS_URL2 = HTTP + "/rest/products/get/api_key/raviteja/is_featured/1/limit/0/offset/10/";

    //    public static String CATEGORIES_URL = HTTP + "/rest/categories/search/api_key/raviteja/limit/0/offset/10";
    public static String CATEGORIES_URL = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/shopcategories/search/api_key/raviteja/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/limit/0/offset/10";
//    http://139.59.20.251/index.php/rest/shopcategories/search/api_key/raviteja/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/limit/0/offset/10

    //    public static String SUB_CATEGORIES_URL = HTTP + "/rest/subcategories/get/api_key/raviteja/login_user_id/%s/cat_id/%s/limit/%s/offset/%s";
    public static String SUB_CATEGORIES_URL = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/shopsubcategories/search/api_key/raviteja/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/cat_id/%s/limit/%s/offset/%s";
//http://139.59.20.251/index.php/rest/shopsubcategories/search/api_key/raviteja/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/cat_id/cat300a2b76e37b9a6632e85e68d466f64d/limit/0/offset/10

    public static String PRODUCT_CATEGORY = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/limit/%s/offset/%s/cat_id/%s";

//    public static String PRODUCTS_URL = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/limit/%s/offset/%s/cat_id/%s/sub_cat_id/%s";

//    public static String PRODUCTS_URL = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/shopproducts/get/api_key/raviteja/limit/0/offset/10/cat_id/cataa7220c9864e2d2f19a869caee1fc7ad/sub_cat_id/subcat53d08dfac9e7f4d226fb6d49c70e96b8/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2";
//http://139.59.20.251/index.php/rest/shopproducts/get/api_key/raviteja/limit/0/offset/10/cat_id/cataa7220c9864e2d2f19a869caee1fc7ad/sub_cat_id/subcat53d08dfac9e7f4d226fb6d49c70e96b8   /shop_parent_id

    //--product Url
    public static String PRODUCTS_URL = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/shopproducts/get/api_key/raviteja/cat_id/catfec79815a9341e928700b175ccd43f91/sub_cat_id/subcat3e862d4c2eeb5deb5bb00ac5623b2227/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2";
//public static String PRODUCTS_URL = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/shopproducts/search/api_key/raviteja/";
//public static String PRODUCTS_URL = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/shopproducts/search/api_key/raviteja/limit/0/offset/10/cat_id/cataa7220c9864e2d2f19a869caee1fc7ad/sub_cat_id/subcat53d08dfac9e7f4d226fb6d49c70e96b8/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2";

    //            [{"key":"api_key","value":"raviteja","description":""},{"key":"limit","value":"0","description":""},{"key":"offset","value":"10","description":""},{"key":"order_by_field","value":"name","description":""},{"key":"order_by","value":"1","description":""},{"key":"order_by_type","value":"desc","description":""},{"key":"cat_id","value":"catfec79815a9341e928700b175ccd43f91","description":""},{"key":"sub_cat_id","value":"subcat3e862d4c2eeb5deb5bb00ac5623b2227","description":""},{"key":"shop_parent_id","value":"[{"key":"shop_parent_id","value":"shop0c54d033ccebee59b6a1e7f0baa560b2","description":""}]","description":""}]
    public static String PRODUCTS_URL_NO_SUBCATEGORY = HTTP + "/rest/products/get/api_key/raviteja/limit/%s/offset/%s/cat_id/%s/";

    public static String COUNTRY_URL = HTTP + "/rest/shipping_zones/get_shipping_country/api_key/raviteja/";

    public static String CITY_URL = HTTP + "/rest/shipping_zones/get_shipping_city/api_key/raviteja/";

    public static String TRANSACTION_URL = HTTP + "/rest/transactionheaders/submit/api_key/raviteja";

    public static String SUB_PRODUCT_IMAGE = HTTP + "/rest/images/get/api_key/raviteja/img_parent_id/%s/img_type/product";

    public static String PRODUCT_DETAILS_URL = HTTP + "/rest/products/get/api_key/raviteja/id/%s//login_user_id/%s";

    public static String PRODUCT_FILTER_URL = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/limit/%s/offset/%s/cat_id/%s/sub_cat_id/%s/max_price/%s/searchterm/%s";


    ///

    //product
    public static String PRODUCTS = "PRODUCTS";
    public static String PRODUCT = "PRODUCT";
    public static String PRODUCT_NAME = "PRODUCT_NAME";
    public static String PRODUCT_ID = "PRODUCT_ID";
    public static String CART = "CART";
    ///

    //SharedPreference
    public static String PREFERENCE = "PREFERENCE";
    ///

    //category
    public static String CATEGORIES = "CATEGORIES";
    public static String CATEGORY_ID = "CATEGORY_ID";
    public static String CATEGORY_NAME = "CATEGORY_NAME";
    public static String SUB_CATEGORIES = "SUBCATEGORIES";
    public static String SUB_CATEGORY_NAME = "SUB_CATEGORY_NAME";
    public static String SUB_CATEGORY_ID = "SUBCATEGORY_ID";
    ///

    // response error
    public static final int ERROR_404 = 404;
    ///

    //activity code
    public static final int REQUEST_CODE = 1;
    public static final int RESPONSE_CODE = 1;
    ///


    // Connection Error
    public static String INTERROR = "Please Check Your Connection";
    ///

    // Payment Method
    public static String PAYMENT_PAYPAL = "PAYMENT_PAYPAL";
    public static String PAYMENT_BANK = "PAYMENT_BANK";
    public static String PAYMENT_CASH_ON_DELIVERY = "PAYMENT_CASH_ON_DELIVERY";
    public static String PAYMENT_RAZOR = "PAYMENT_RAZOR";
    public static String PAYMENT_STRIPE = "PAYMENT_STRIPE";
    public static String PAYMENT_SHIPPING_TAX = "PAYMENT_SHIPPING_TAX";
    public static String PAYMENT_OVER_ALL_TAX = "PAYMENT_OVER_ALL_TAX";
    public static String PAYMENT_SHIPPING_TAX_LABEL = "PAYMENT_SHIPPING_TAX_LABEL";
    public static String PAYMENT_OVER_ALL_TAX_LABEL = "PAYMENT_OVER_ALL_TAX_LABEL";
    public static String PAYMENT_TOKEN = "TOKEN";
    public static String PAYMENT_CONDITION_ERROR = "Please Accept Conditions";
    public static String PAYMENT_METHOD_ERROR = "Please Choose Payment Method!";
    ///

    //Rating
    public static String RATING_ZERO = "0";
    public static String RATING_ONE = "1";
    public static String RATING_TWO = "2";
    public static String RATING_THREE = "3";
    public static String RATING_FOUR = "4";
    public static String RATING_FIVE = "5";
    ///

    //Country
    public static String COUNTRY_NAME = "COUNTRY_NAME";
    public static String COUNTRY_ID = "COUNTRY_ID";
    ///

    //Tags
    public static String SEARCH_TAG = "SEARCH_TAG";

    ///


    //User
    public static String USER = "USER";
    public static String USER_NEED_VERIFY = "USER_NEED_VERIFY";
    public static String LOGGED_USER = "LOGGED_USER";
    public static String USER_NAME = "USERNAME";
    public static String EMAIL = "EMAIL";
    public static String PASSWORD = "PASSWORD";
    public static String SHOP = "SHOP";
    public static String SHOP_KEY = "SHOP_KEY";
    public static String SHOP_NAME = "SHOP_NAME";
    public static String SHOP_MAIN_BRANCH_KEY = "SHOP_MAIN_BRANCH_KEY";
    public static String INDIVIDUAL_SHOP = "INDIVIDUAL_SHOP";
    public static String SHOP_OWNER = "SHOP_OWNER";
    public static String LAT = "LAT";
    public static String LNG = "LNG";
    public static String CURRENT_LAT = "CURRENT_LAT";
    public static String CURRENT_LNG = "CURRENT_LNG";
    public static String AVAILABLE_DISTANCE = "AVAILABLE_DISTANCE";
    public static String DELIVERY_CHARGE_CALCULATED = "DELIVERY_CHARGE_CALCULATED";
    public static String DELIVERY_CHARGE_CALCULATE_AFTER_LOGIN = "DELIVERY_CHARGE_CALCULATED";
    public static String COD_CHARGE_FROM = "cod_charge_from";
    public static String DELIVERY_CHARGE = "DELIVERY_CHARGE";
    //

    //Constants
    //TODO : mc: Merchant category code : 5411 for Grocery Stores, Supermarkets
    //TODO : mc: Merchant category code : 5812 for Eating Places and Restaurants
    //TODO : mc: Merchant category code : 5814 for Fast Food Restaurants

    public static String MC_GROCERY_STORES = "5144";
    public static String MC_EATING_PLACES_AND_RESTAURANTS = "5812";
    public static String MC_FAST_FOOD_RESTAURANTS = "5814";
    ///

    public static synchronized void play_ringtone(Context ctx) {
        try {
            if (ringtone == null) {
                ringtone = RingtoneManager.getRingtone(ctx, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone.setLooping(true);
                }
            }
            if (!ringtone.isPlaying())
                ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void stop_ringtone(Context ctx) {
        if (nm == null)
            nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
            ringtone = null;
        }
    }
}