package com.dvishapps.yourshop.utils;

import com.dvishapps.yourshop.R;

public class DynamicConstants {

    //        public static String HTTP_SERVER_URL="http://139.59.20.251";
//    public static String HTTP_SERVER_URL = "http://dvishapps.com/yourshop";
//    public static String HTTP_SERVER_URL = "http://dvishapps.com/yscomco";
    public static String HTTP_SERVER_URL = "http://yourshop.com.co";

    private static DynamicConstants instance;

    public static DynamicConstants getInstance() {
        if (instance == null) {
            instance = new DynamicConstants();
        }
        return instance;
    }


    public void clearAllData() {
        instance = new DynamicConstants();
    }

    public void setInstance(DynamicConstants instance) {
        DynamicConstants.instance = instance;
    }


    // api urls
    public String USER_ID = "usr220660e2d111529faa70d141610e20e7";

    public String SHOP_ID = "shop0b69bc5dbd68bbd57ea13dfc5488e20a";

    public String API_KEY = "raviteja";

//    public  String IMG_URL = "http://www.dvishapps.com/chendhur/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/chendhur/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/chendhur/index.php";

//
//    public  String IMG_URL = "http://www.dvishapps.com/saistores/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/saistores/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/saistores/index.php";


//    public  String IMG_URL = "http://www.dvishapps.com/bluewave/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/bluewave/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/bluewave/index.php";
//
//    public  String IMG_URL = "http://www.dvishapps.com/amutham_sm/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/amutham_sm/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/amutham_sm/index.php";

//    public  String IMG_URL = "http://www.dvishapps.com/velsm/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/velsm/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/velsm/index.php";
    // TODO: Current SHOP_KEY_NAME for URL

    public String SHOP_KEY_NAME = "velsm";
    //    public String shopKeyValue = "shop0c54d033ccebee59b6a1e7f0baa560b2";

    //    http://139.59.20.251/index.php
    public String IMG_URL = HTTP_SERVER_URL + "/uploads/shop_parent_id/" + SessionData.getInstance().getShopKeyValue();

    public String THUMB_IMG_URL = "http://www.dvishapps.com/" + SHOP_KEY_NAME + "/uploads/thumbnail/";

    public String HTTP = "http://www.dvishapps.com/" + SHOP_KEY_NAME + "/index.php";

//    public  String IMG_URL = "http://www.dvishapps.com/arabian/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/arabian/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/arabian/index.php";

//
//    public  String IMG_URL = "http://www.dvishapps.com/bawarchi/uploads/";
//
//    public  String THUMB_IMG_URL = "http://www.dvishapps.com/bawarchi/uploads/thumbnail/";
//
//    public  String HTTP = "http://www.dvishapps.com/bawarchi/index.php";


    public String FEATURED_PRODUCTS_URL = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/is_featured/1/limit/0/offset/10/cat_id/%s";


    //    public  String CATEGORIES_URL = HTTP + "/rest/categories/search/api_key/raviteja/limit/0/offset/10";
    public String CATEGORIES_URL = HTTP_SERVER_URL + "/index.php/rest/shopcategories/search/api_key/raviteja/shop_parent_id/" + SessionData.getInstance().getShopKeyValue() + "/limit/0/offset/10";
//    http://139.59.20.251/index.php/rest/shopcategories/search/api_key/raviteja/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/limit/0/offset/10

    //    public  String SUB_CATEGORIES_URL = HTTP + "/rest/subcategories/get/api_key/raviteja/login_user_id/%s/cat_id/%s/limit/%s/offset/%s";
    public String SUB_CATEGORIES_URL = HTTP_SERVER_URL + "/index.php/rest/shopsubcategories/search/api_key/raviteja/shop_parent_id/" + SessionData.getInstance().getShopKeyValue() + "/cat_id/%s/limit/%s/offset/%s";
//http://139.59.20.251/index.php/rest/shopsubcategories/search/api_key/raviteja/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/cat_id/cat300a2b76e37b9a6632e85e68d466f64d/limit/0/offset/10

    public String PRODUCT_CATEGORY = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/limit/%s/offset/%s/cat_id/%s";

    //    public  String PRODUCTS_URL = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/limit/%s/offset/%s/cat_id/%s/sub_cat_id/%s";
//    public String PRODUCTS_URL = HTTP_SERVER_URL+"/index.php/rest/shopproducts/get/api_key/raviteja/cat_id/catfec79815a9341e928700b175ccd43f91/sub_cat_id/subcat3e862d4c2eeb5deb5bb00ac5623b2227/shop_parent_id/" + SessionData.getInstance().getShopKeyValue() + "/cat_id/%s/limit/%s/offset/%s";
//    public String PRODUCTS_URL = HTTP_SERVER_URL+"/index.php/rest/shopproducts/get/api_key/raviteja/cat_id/%s/sub_cat_id/%s/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2/limit/%s/offset/%s";
    public String PRODUCTS_URL = HTTP_SERVER_URL + "/index.php/rest/shopproducts/get/api_key/raviteja/cat_id/%s/sub_cat_id/%s/shop_parent_id/" + SessionData.getInstance().getShopKeyValue() + "/limit/%s/offset/%s";

//    public String PRODUCTS_URL = HTTP_SERVER_URL+"/index.php/rest/shopproducts/get/api_key/raviteja/limit/0/offset/10/cat_id/cataa7220c9864e2d2f19a869caee1fc7ad/sub_cat_id/subcat53d08dfac9e7f4d226fb6d49c70e96b8/shop_parent_id/" + shopKeyValue;
//http://139.59.20.251/index.php/rest/shopproducts/get/api_key/raviteja/limit/0/offset/10/cat_id/cataa7220c9864e2d2f19a869caee1fc7ad/sub_cat_id/subcat53d08dfac9e7f4d226fb6d49c70e96b8   /shop_parent_id

    public String PRODUCTS_URL_NO_SUBCATEGORY = HTTP_SERVER_URL + "/index.php/rest/shopproducts/get/api_key/raviteja/cat_id/%s/shop_parent_id/" + SessionData.getInstance().getShopKeyValue() + "/limit/%s/offset/%s";


    //    public  String FEATURED_PRODUCTS_URL2 = HTTP + "/rest/products/get/api_key/raviteja/is_featured/1/limit/0/offset/10/";
    public String FEATURED_PRODUCTS_URL2 = HTTP_SERVER_URL + "/index.php/rest/shopproducts/get/api_key/raviteja/is_featured/1/shop_parent_id/" + SessionData.getInstance().getShopKeyValue() + "/limit/0/offset/10";


    public String COUNTRY_URL = HTTP + "/rest/shipping_zones/get_shipping_country/api_key/raviteja/";

    public String CITY_URL = HTTP + "/rest/shipping_zones/get_shipping_city/api_key/raviteja/";

    //    public String TRANSACTION_URL = HTTP + "/rest/transactionheaders/submit/api_key/raviteja";
//    http://139.59.20.251/index.php/rest/transactionheaders/submit/api_key/raviteja
    public String TRANSACTION_URL = HTTP_SERVER_URL + "/index.php/rest/transactionheaders/submit/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();

//    public String SUB_PRODUCT_IMAGE = HTTP + "/rest/images/get/api_key/raviteja/img_parent_id/%s/img_type/product";

//    public String PRODUCT_DETAILS_URL = HTTP + "/rest/products/get/api_key/raviteja/id/%s//login_user_id/%s";

    public String PRODUCT_FILTER_URL = HTTP + "/rest/products/get/api_key/raviteja/login_user_id/%s/limit/%s/offset/%s/cat_id/%s/sub_cat_id/%s/max_price/%s/searchterm/%s";

    //    public static String sign_up_url = StringUtil.setString("%s/rest/users/add/api_key/%s", Constants.HTTP, Constants.API_KEY);
    public String sign_up_url = HTTP_SERVER_URL + "/index.php/rest/users/add/api_key/raviteja";
    public String sign_up_url_new = HTTP_SERVER_URL + "/index.php/rest/users/add/api_key/raviteja";
//    public String sign_up_url = HTTP_SERVER_URL+"/index.php/rest/users/add/api_key/raviteja/shop_id/usr79e93f0dd3411cf1dc7291a5d08cef18";
//        http://www.dvishapps.com/arabian/index.php/rest/users/add/api_key/raviteja

    public String sign_up_verify_url = HTTP_SERVER_URL + "/index.php/rest/users/verify/api_key/raviteja/";
//        http://www.dvishapps.com/arabian/index.php/rest/users/verify/api_key/raviteja

    public String sign_in_url = HTTP_SERVER_URL + "/index.php/rest/users/login/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();
    public String sign_in_url_new = HTTP_SERVER_URL + "/index.php/rest/users/login/api_key/raviteja";
//    public static String sign_in_url = StringUtil.setString("%s/rest/users/login/api_key/%s", Constants.HTTP, Constants.API_KEY);


    public String user_info_url = HTTP_SERVER_URL + "/index.php/rest/users/get/api_key/raviteja/user_id/%s/shop_id/" + SessionData.getInstance().getShopKeyValue();
//    public static String user_info_url =  Constants.HTTP+"/rest/users/get/api_key/%s/user_id/%s";

//    http://139.59.20.251/index.php/rest/transactionheaders/submit/api_key/raviteja
//    www.dvishapps.com/bde/index.php/rest/transactiondetails/get/api_key/raviteja/shop_id/shop0c54d033ccebee59b6a1e7f0baa560b2

    public String edit_user_info_url = HTTP_SERVER_URL + "/index.php/rest/users/profile_update/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();
//    public static String edit_user_info_url = StringUtil.setString("%s/rest/users/profile_update/api_key/%s", Constants.HTTP, Constants.API_KEY);


    public String update_password_url = HTTP_SERVER_URL + "/index.php/rest/users/password_update/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();

    public String forget_password_url = HTTP_SERVER_URL + "/index.php/rest/users/reset/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();
//    public static String update_password_url = StringUtil.setString("%s/rest/users/password_update/api_key/%s", Constants.HTTP, Constants.API_KEY);

    public String upload_image_url = HTTP_SERVER_URL + "/index.php/rest/images/upload/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();
//    public static String upload_image_url = StringUtil.setString("%s/rest/images/upload/api_key/%s", Constants.HTTP, Constants.API_KEY);


    public String shop_details_url = HTTP_SERVER_URL + "/index.php/rest/shops/get/api_key/raviteja/shop_id/" + SessionData.getInstance().getShopKeyValue();
    public String shop_details_urlCustom = HTTP_SERVER_URL + "/index.php/rest/shops/get/api_key/raviteja/shop_id/%s";
    public String shop_details_urlCustomtest = HTTP_SERVER_URL + "/index.php/rest/shops/get/api_key/raviteja/shop_id/shop0c54d033ccebee59b6a1e7f0baa560b2";
//    public static String shop_details_url = StringUtil.setString("%s/rest/shops/get_shop_info/api_key/%s", Constants.HTTP, Constants.API_KEY);
//    http://139.59.20.251/index.php/rest/shops/get/api_key/raviteja/shop_id/shop01672de80f1b2f0f3fa803b58c23d11d

    //    public static String PRODUCT_DETAILS_URL = HTTP + "/rest/products/get/api_key/raviteja/id/%s//login_user_id/%s";
    public String PRODUCT_DETAILS_URL = HTTP_SERVER_URL + "/index.php/rest/shopproducts/get/api_key/raviteja/id/%s/shop_parent_id/" + SessionData.getInstance().getShopKeyValue();
//    http://139.59.20.251/index.php/rest/shopproducts/get/api_key/raviteja/id/2528/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2

    //    public String SUB_PRODUCT_IMAGE = HTTP + "/rest/images/get/api_key/raviteja/img_parent_id/%s/img_type/product";
//    http://139.59.20.251/index.php/rest/images/get/api_key/raviteja/id/2528/img_type/product
    public String SUB_PRODUCT_IMAGE = HTTP_SERVER_URL + "/index.php/rest/images/get/api_key/raviteja/img_parent_id/%s/img_type/product";
//    http://139.59.20.251/index.php/rest/shopproducts/get/api_key/raviteja/id/2528/shop_parent_id/shop0c54d033ccebee59b6a1e7f0baa560b2

    public String delivery_staff_url = HTTP_SERVER_URL + "/index.php/rest/Areamanagers/get_areamanager/api_key/raviteja";

    public String transaction_update_url = HTTP_SERVER_URL + "/index.php/rest/transactionheaders/updatetransaction/api_key/raviteja";
    public String shop_settings_update_url = HTTP_SERVER_URL + "/index.php/rest/shops/edit_shop/api_key/raviteja";

    public String google_live_distance_url = "https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&sensor=false&key=AIzaSyD-xCSyRy7_rkA5_pFwLoOubt5x7gXtIZA";
    public String google_address_conversion_url = "https://maps.google.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyDf3xZF2g6qV_udmlgkOzZR4ydD_CrtoRc&sensor=false";
//    public String google_address_conversion_url = "https://maps.google.com/maps/api/geocode/json?latlng=%s,%s&key="+ R.string.google_api_key+"&sensor=false";

    public String custom_shop_list_url = HTTP_SERVER_URL + "/index.php/rest/shops/shop_list/api_key/raviteja/";
//http://www.dvishapps.com/yourshop/index.php/rest/shops/shop_list/api_key/raviteja
}

