package com.dvishapps.yourshop.api.services;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.dvishapps.yourshop.api.ApiResponse;
import com.dvishapps.yourshop.api.httpClient.Http;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.ArrayParams;
import com.dvishapps.yourshop.models.ArrayParamsBitmap;
import com.dvishapps.yourshop.models.Category;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.models.Search;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.SubCategory;
import com.dvishapps.yourshop.models.TestImage;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Product service.
 */
public class ProductService {
    /**
     * Gets categories.
     *
     * @return the categories
     */
    @NotNull
    public static MutableLiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> caMutableLiveData = new MutableLiveData<>(null);
        Type collectionType = new TypeToken<Collection<Category>>() {
        }.getType();
        Http.<ArrayList<Category>>post(
//                Constants.CATEGORIES_URL,
                DynamicConstants.getInstance().CATEGORIES_URL,
                "",
                collectionType,
//                caMutableLiveData::postValue,
                response -> {
                    Console.logError("CATEGORIES_URL response : " + response);
                    caMutableLiveData.setValue(response);
                }
                ,
                error -> {
                    JSONObject jsonObject = null;
                    ArrayList<Category> list = new ArrayList<>();
                    Category category = new Category();
                    if (error.networkResponse != null) {
                        try {
                            jsonObject = new JSONObject(new String(error.networkResponse.data));
                            Console.logError("CATEGORIES_URL error : " + error.networkResponse.statusCode + " "
                                    + jsonObject.getString("message"));

                            category.setStatus("404 error" + jsonObject.getString("message"));
                            list.add(category);
                            caMutableLiveData.setValue(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (error.getMessage() == null) {
                            category.setStatus("404 error" + "UnknownHostException");
                        } else {
                            category.setStatus("404 error" + error.getMessage());
                        }
                        list.add(category);
                        caMutableLiveData.setValue(list);
                    }

                }
        );
        return caMutableLiveData;
    }


    /**
     * Gets sub categories.
     *
     * @param cate_id the cate id
     * @return the sub categories
     */
    @NotNull
    public static MutableLiveData<List<SubCategory>> getSubCategories(String cate_id) {
        MutableLiveData<List<SubCategory>> caMutableLiveData = new MutableLiveData<>(null);
//        String url = String.format(Constants.SUB_CATEGORIES_URL, Constants.USER_ID, cate_id, String.valueOf(Config.LIMIT_0), String.valueOf(Config.OFFSET));
        String url = String.format(/*Constants.SUB_CATEGORIES_URL*/ DynamicConstants.getInstance().SUB_CATEGORIES_URL, /*Constants.USER_ID,*/ cate_id, String.valueOf(Config.LIMIT_0), String.valueOf(Config.OFFSET));
        Type collectionType = new TypeToken<Collection<SubCategory>>() {
        }.getType();
//        Http.<List<SubCategory>>fetch(url, "", collectionType, caMutableLiveData::setValue, error -> {
        Http.<List<SubCategory>>post(url, "", collectionType,
//                caMutableLiveData::setValue
                response -> {
                    Console.logError("SUB CATEGORIES_URL response : " + response);
                    caMutableLiveData.setValue(response);
                }
                , error -> {
                    JSONObject jsonObject = null;
                    ArrayList<SubCategory> list = new ArrayList<>();
                    SubCategory category = new SubCategory();
                    if (error.networkResponse != null) {
                        try {
                            jsonObject = new JSONObject(new String(error.networkResponse.data));
                            Console.logError("SUB CATEGORIES_URL error : " + error.networkResponse.statusCode + " "
                                    + jsonObject.getString("message"));

                            category.setStatus("404 error" + jsonObject.getString("message"));
                            list.add(category);
                            caMutableLiveData.setValue(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (error.getMessage() == null) {
                            category.setStatus("404 error" + "UnknownHostException");
                        } else {
                            category.setStatus("404 error" + error.getMessage());
                        }
                        list.add(category);
                        caMutableLiveData.setValue(list);
                    }
                });
        return caMutableLiveData;
    }

    /**
     * Gets products.
     *
     * @param cat_id the cat id
     * @return the products
     */
    @NotNull
    public static MutableLiveData<List<Product>> getFeaturedProducts(String cat_id) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>(new ArrayList<>());
        Type collectionType = new TypeToken<ArrayList<Product>>() {
        }.getType();
        String url = String.format(Constants.FEATURED_PRODUCTS_URL, Constants.USER_ID, cat_id);
        Http.<List<Product>>fetch(url, "", collectionType, data::setValue, error -> {
            Log.e("getFeaturedProducts", "getFeaturedProducts: ", error);
        });

        return data;
    }

    @NotNull
    public static MutableLiveData<List<Product>> getFeaturedProducts2() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>(null);
        Type collectionType = new TypeToken<ArrayList<Product>>() {
        }.getType();
        String url = String.format(/*Constants.FEATURED_PRODUCTS_URL2*/DynamicConstants.getInstance().FEATURED_PRODUCTS_URL2, Constants.USER_ID);
        Http.<List<Product>>fetch(url, "", collectionType, data::setValue, error -> {

            JSONObject jsonObject = null;
            ArrayList<Product> list = new ArrayList<>();
            Product product = new Product();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError("Featured products error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    product.setStatus("404 error" + jsonObject.getString("message"));
                    list.add(product);
                    data.setValue(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (error.getMessage() == null) {
                    product.setStatus("404 error" + "UnknownHostException");
                } else {
                    product.setStatus("404 error" + error.getMessage());
                }
                list.add(product);
                data.setValue(list);
            }

            Log.e("getFeaturedProducts2", "getFeaturedProducts: ", error);
        });

        return data;
    }

    /**
     * Gets product.
     *
     * @param p_id the p id
     * @return the product
     */
    @NotNull
    public static MutableLiveData<Product> getProduct(String p_id) {
        MutableLiveData<Product> productMutableLiveData = new MutableLiveData<>(new Product());
        String url = String.format(/*Constants.PRODUCT_DETAILS_URL*/ DynamicConstants.getInstance().PRODUCT_DETAILS_URL, p_id/*, Constants.USER_ID*/);
        Http.<Product>fetch(url, "", Product.class, productMutableLiveData::setValue, error -> {
        });
        return productMutableLiveData;
    }


    /**
     * Gets product sub images.
     *
     * @param p_id the p d
     * @return the product sub images
     */
    @NotNull
    public static MutableLiveData<List<Photo>> getProductSubImages(String p_id) {
        MutableLiveData<List<Photo>> listMutableLiveData = new MutableLiveData<>(new ArrayList<>());
        Type collectionType = new TypeToken<Collection<Photo>>() {
        }.getType();
        String url = String.format(/*Constants.SUB_PRODUCT_IMAGE*/DynamicConstants.getInstance().SUB_PRODUCT_IMAGE, p_id);
        System.out.println("ok " + url);
        Http.<List<Photo>>fetch(url, "", collectionType, listMutableLiveData::setValue, error -> {
        });
        return listMutableLiveData;
    }

    /**
     * Gets searched category.
     *
     * @return the searched category
     */
    @NotNull
    public static LiveData<List<Category>> getSearchedCategory() {
        MutableLiveData<List<Category>> categoryMutableLiveData = new MutableLiveData<>();
        Type collectionType = new TypeToken<Collection<Category>>() {
        }.getType();
        Http.<ArrayList<Category>>post(Constants.PRODUCT_CATEGORY, "", collectionType, response -> {
            categoryMutableLiveData.setValue(response);
        }, error -> {
        });
        return categoryMutableLiveData;
    }

    /**
     * Search product by category live data.
     *
     * @param cate_id     the cate id
     * @param sub_cate_id the sub cate id
     * @param limitData   the limit data
     * @return the live data
     */
//    @NotNull
    public static MutableLiveData<List<Product>> searchProductByCategory(String cate_id, String sub_cate_id, @NotNull LimitData limitData) {

        MutableLiveData<List<Product>> productMutableLiveData = new MutableLiveData<>(null);
//        String url = String.format(Constants.PRODUCTS_URL, Constants.USER_ID, limitData.limit, limitData.offset, cate_id, sub_cate_id);
        String url = String.format(/*Constants.PRODUCTS_URL*/ DynamicConstants.getInstance().PRODUCTS_URL,/* Constants.USER_ID,*/ cate_id, sub_cate_id, limitData.limit, limitData.offset);
        System.out.println(url);
        Type collectionType = new TypeToken<Collection<Product>>() {
        }.getType();
        Http.<List<Product>>fetch(url, "", collectionType, productMutableLiveData::setValue, error -> {

            JSONObject jsonObject = null;
            ArrayList<Product> list = new ArrayList<>();
            Product product = new Product();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" products error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    product.setStatus("404 error" + jsonObject.getString("message"));
                    list.add(product);
                    productMutableLiveData.setValue(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (error.getMessage() == null) {
                    product.setStatus("404 error" + "UnknownHostException");
                } else {
                    product.setStatus("404 error" + error.getMessage());
                }
                list.add(product);
                productMutableLiveData.setValue(list);
            }

            Log.e("getProducts2", "getProducts: ", error);
        });
        return productMutableLiveData;
    }


    @NotNull
    public static MutableLiveData<Shop> postCategoryWithData(Bitmap cover, Bitmap icon, String name, String subcategory_need, String is_enable, String status, String added_user_id, String shopKeyValue, String category_bussiness_type, String category_open_time, String category_close_time) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/categories/add/api_key/raviteja";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));
        imgParams.add(new ArrayParamsBitmap("icon", icon));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("name", name));
        params.add(new ArrayParams("subcategory_need", subcategory_need));
        params.add(new ArrayParams("status", status));
//        params.add(new ArrayParams("is_enable", is_enable));
        params.add(new ArrayParams("added_user_id", added_user_id));
        params.add(new ArrayParams("shop_id", shopKeyValue));

        params.add(new ArrayParams("category_open_time", category_open_time));
        params.add(new ArrayParams("category_close_time", category_close_time));
        params.add(new ArrayParams("category_bussiness_type", category_bussiness_type));

        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
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
    public static MutableLiveData<Shop> postUpdateCategoryWithData(Bitmap cover, Bitmap icon, String cat_id, String name, String subcategory_need, String is_enable, String status, String added_user_id, String shopKeyValue, String category_bussiness_type, String category_open_time, String category_close_time) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/categories/add/api_key/raviteja";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));
        imgParams.add(new ArrayParamsBitmap("icon", icon));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("name", name));
        params.add(new ArrayParams("subcategory_need", subcategory_need));
        params.add(new ArrayParams("status", status));
//        params.add(new ArrayParams("is_enable", is_enable));
        params.add(new ArrayParams("added_user_id", added_user_id));
        params.add(new ArrayParams("shop_id", shopKeyValue));


        params.add(new ArrayParams("category_bussiness_type", category_bussiness_type));
        params.add(new ArrayParams("category_open_time", category_open_time));
        params.add(new ArrayParams("category_close_time", category_close_time));

        params.add(new ArrayParams("id", cat_id));

        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
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
    public static MutableLiveData<Shop> postDeleteCategoryWithData(Bitmap cover, Bitmap icon, String cat_id, String name, String status, String added_user_id, String shopKeyValue) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/categories/delete/api_key/raviteja";

        JsonObject body = new JsonObject();
        body.addProperty("category_id", cat_id);
        body.addProperty("shop_id", shopKeyValue);

        Http.<ApiResponse>post(url,
                JsonUtils.toJsonString(body),
                ApiResponse.class,
                shopResponse -> {
                    try {
//                JSONObject obj = new JSONObject(new String(shopResponse));
                        Shop shop = new Shop();
                        shop.setStatus("Successfully deleted");
                        response.setValue(shop);
//                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {

                    JSONObject jsonObject = null;
                    Shop shop = new Shop();
                    if (error.networkResponse != null) {
                        try {
                            jsonObject = new JSONObject(new String(error.networkResponse.data));

                            Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                                    + jsonObject.getString("message"));
                            Console.toast(jsonObject.getString("message"));
                            shop.setStatus(jsonObject.getString("message"));
                            response.setValue(shop);

                        } catch (Exception e) {
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
    public static MutableLiveData<Shop> postSubCategoryWithData(Bitmap cover, Bitmap icon, String name, String cat_id, String is_enable, String status, String added_user_id, String shopKeyValue, String sub_category_bussiness_type, String sub_category_open_time, String sub_category_close_time) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/Subcategories/add/api_key/raviteja/";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));
        imgParams.add(new ArrayParamsBitmap("icon", icon));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("name", name));
        params.add(new ArrayParams("added_user_id", added_user_id));
        params.add(new ArrayParams("cat_id", cat_id));
//        params.add(new ArrayParams("is_enable", is_enable));
        params.add(new ArrayParams("status", status));
        params.add(new ArrayParams("shop_id", shopKeyValue));

        params.add(new ArrayParams("sub_category_open_time", sub_category_open_time));
        params.add(new ArrayParams("sub_category_close_time", sub_category_close_time));
        params.add(new ArrayParams("sub_category_bussiness_type", sub_category_bussiness_type));

        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
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
    public static MutableLiveData<Shop> postUpdateSubCategoryWithData(Bitmap cover, Bitmap icon, String name, String sub_category_id, String cat_id, String is_enable, String status, String added_user_id, String shopKeyValue, String sub_category_bussiness_type, String sub_category_open_time, String sub_category_close_time) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/Subcategories/add/api_key/raviteja/";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));
        imgParams.add(new ArrayParamsBitmap("icon", icon));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("name", name));
        params.add(new ArrayParams("added_user_id", added_user_id));
        params.add(new ArrayParams("cat_id", cat_id));
//        params.add(new ArrayParams("is_enable", is_enable));
        params.add(new ArrayParams("status", status));
        params.add(new ArrayParams("shop_id", shopKeyValue));

        params.add(new ArrayParams("sub_cat_id", sub_category_id));


        params.add(new ArrayParams("sub_category_open_time", sub_category_open_time));
        params.add(new ArrayParams("sub_category_close_time", sub_category_close_time));
        params.add(new ArrayParams("sub_category_bussiness_type", sub_category_bussiness_type));

        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
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
    public static MutableLiveData<Shop> postDeleteSubCategoryWithData(Bitmap cover, Bitmap icon, String cat_id, String name, String status, String added_user_id, String shopKeyValue) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/Subcategories/delete/api_key/raviteja";

        JsonObject body = new JsonObject();
        body.addProperty("shop_sub_category", cat_id);
        body.addProperty("shop_id", shopKeyValue);

        Http.<ApiResponse>post(url,
                JsonUtils.toJsonString(body),
                ApiResponse.class,
                shopResponse -> {
                    try {
//                JSONObject obj = new JSONObject(new String(shopResponse));
                        Shop shop = new Shop();
                        shop.setStatus("Successfully deleted");
                        response.setValue(shop);
//                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {

                    JSONObject jsonObject = null;
                    Shop shop = new Shop();
                    if (error.networkResponse != null) {
                        try {
                            jsonObject = new JSONObject(new String(error.networkResponse.data));

                            Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                                    + jsonObject.getString("message"));
                            Console.toast(jsonObject.getString("message"));
                            shop.setStatus(jsonObject.getString("message"));
                            response.setValue(shop);

                        } catch (Exception e) {
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
    public static MutableLiveData<Shop> postProductWithData(Bitmap cover, Bitmap icon, String name, String product_measurement, String original_price,
                                                            String unit_price, String sub_cat_id, String cat_id, String is_enable, String status, String is_available,
                                                            String added_user_id, String shopKeyValue,
                                                            String product_bussiness_type, String product_open_time, String product_close_time, String offer_price

    ) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/products/add/api_key/raviteja";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));
        imgParams.add(new ArrayParamsBitmap("icon", icon));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("name", name));
        params.add(new ArrayParams("added_user_id", added_user_id));
        params.add(new ArrayParams("cat_id", cat_id));
//        params.add(new ArrayParams("is_enable", is_enable));
        params.add(new ArrayParams("status", status));
        params.add(new ArrayParams("shop_id", shopKeyValue));
        params.add(new ArrayParams("sub_cat_id", sub_cat_id));
        params.add(new ArrayParams("product_measurement", product_measurement));
        params.add(new ArrayParams("original_price", original_price));
        params.add(new ArrayParams("unit_price", unit_price));
        params.add(new ArrayParams("is_available", is_available));

        params.add(new ArrayParams("product_bussiness_type", product_bussiness_type));
        params.add(new ArrayParams("product_open_time", product_open_time));
        params.add(new ArrayParams("product_close_time", product_close_time));
        params.add(new ArrayParams("offer_price", offer_price));

//        added_user_id:c4ca4238a0b923820dcc509a6f75849b
//        cat_id:1260
//        sub_cat_id:9302

   /*     product_unit:
        product_measurement:
        discount_type_id:*/
//        name:test product test 31
//        description:Test Description
//        original_price:50
//        unit_price:50
       /* shipping_cost:
        minimum_order:
        search_tag:
        highlight_information:
        code:
        is_featured:
        is_featured_stage:*/
//        is_available:1
//        status:1
//        shop_id:shopd50aab2bddb710c6d23bc882c277cf58

        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
                    if (e.getMessage() == null) {
                        shop.setStatus("404 error" + e.getMessage());
                    } else {
                        shop.setStatus("404 error" + e.getMessage());
                    }
                    response.setValue(shop);
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
    public static MutableLiveData<Shop> postDeleteProduct(Bitmap cover, Bitmap icon, String product_id, String name, String status, String added_user_id, String shopKeyValue) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/products/delete/api_key/raviteja";

        JsonObject body = new JsonObject();
        body.addProperty("product_id", product_id);
        body.addProperty("shop_id", shopKeyValue);

        Http.<ApiResponse>post(url,
                JsonUtils.toJsonString(body),
                ApiResponse.class,
                shopResponse -> {
                    try {
//                JSONObject obj = new JSONObject(new String(shopResponse));
                        Shop shop = new Shop();
                        shop.setStatus("Successfully deleted");
                        response.setValue(shop);
//                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {

                    JSONObject jsonObject = null;
                    Shop shop = new Shop();
                    if (error.networkResponse != null) {
                        try {
                            jsonObject = new JSONObject(new String(error.networkResponse.data));

                            Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                                    + jsonObject.getString("message"));
                            Console.toast(jsonObject.getString("message"));
                            shop.setStatus(jsonObject.getString("message"));
                            response.setValue(shop);

                        } catch (Exception e) {
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
    public static MutableLiveData<Shop> updateProductWithData(Bitmap cover, Bitmap icon, String product_id, String name, String product_measurement, String original_price,
                                                              String unit_price, String sub_cat_id, String cat_id, String is_enable, String status, String is_available,
                                                              String added_user_id, String shopKeyValue,
                                                              String product_bussiness_type, String product_open_time, String product_close_time, String offer_price

    ) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = DynamicConstants.HTTP_SERVER_URL + "/index.php/rest/products/add/api_key/raviteja";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));
        imgParams.add(new ArrayParamsBitmap("icon", icon));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("name", name));
        params.add(new ArrayParams("added_user_id", added_user_id));
        params.add(new ArrayParams("cat_id", cat_id));
//        params.add(new ArrayParams("is_enable", is_enable));
        params.add(new ArrayParams("status", status));
        params.add(new ArrayParams("shop_id", shopKeyValue));
        params.add(new ArrayParams("sub_cat_id", sub_cat_id));
        params.add(new ArrayParams("product_measurement", product_measurement));
        params.add(new ArrayParams("original_price", original_price));
        params.add(new ArrayParams("unit_price", unit_price));
        params.add(new ArrayParams("is_available", is_available));
        params.add(new ArrayParams("product_id", product_id));


        params.add(new ArrayParams("product_bussiness_type", product_bussiness_type));
        params.add(new ArrayParams("product_open_time", product_open_time));
        params.add(new ArrayParams("product_close_time", product_close_time));
        params.add(new ArrayParams("offer_price", offer_price));

//        added_user_id:usrba820703adad7e42a1e97291ff4e40ab
//        cat_id:1505
//        sub_cat_id:
//        product_id:1380033
//        name:test product test
//        description:Test Description
//        original_price:50
//        unit_price:50
//        is_enable:1
//        is_available:1
//        product_bussiness_type:1
//        product_open_time:5:30
//        product_close_time:20:30
//        offer_price:6
//        shop_id:shop7877e758e5f291920d3871cffbb94b41

        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
                    if (e.getMessage() == null) {
                        shop.setStatus("404 error" + e.getMessage());
                    } else {
                        shop.setStatus("404 error" + e.getMessage());
                    }
                    response.setValue(shop);
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
    public static MutableLiveData<Shop> postAddMoreImagesWithData(Bitmap cover, String product_id) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = "http://dvishapps.com/bde/index.php/rest/products/mulitpleimage/api_key/raviteja";

        ArrayList<ArrayParamsBitmap> imgParams = new ArrayList<>();
        imgParams.add(new ArrayParamsBitmap("cover", cover));

        ArrayList<ArrayParams> params = new ArrayList<>();
        params.add(new ArrayParams("product_id", product_id));


        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, params, imgParams, /*null, testImage_type,*/ shopResponse -> {
            try {
                JSONObject obj = new JSONObject(new String(shopResponse.data));
                Shop shop = new Shop();
                shop.setStatus("Successfully added");
                response.setValue(shop);
                Console.logDebug(obj.getString("id"));
//                Console.toast("Successfully added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));

                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));
                    Console.toast(jsonObject.getString("message"));
                    shop.setStatus(jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (Exception e) {
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

    /**
     * Search product by category live data.
     *
     * @param cate_id   the cate id
     * @param limitData the limit data
     * @return the live data
     */
    @NotNull
    public static MutableLiveData<List<Product>> searchProductByCategoryWithNoSubCategory(String cate_id, @NotNull LimitData limitData) {
        MutableLiveData<List<Product>> productMutableLiveData = new MutableLiveData<>(null);

        String url = String.format(/*Constants.PRODUCTS_URL*/ DynamicConstants.getInstance().PRODUCTS_URL_NO_SUBCATEGORY,/* Constants.USER_ID,*/ cate_id, limitData.limit, limitData.offset);

        System.out.println(url);
        Type collectionType = new TypeToken<Collection<Product>>() {
        }.getType();
        Http.<List<Product>>fetch(url, "", collectionType, productMutableLiveData::setValue, error -> {

            JSONObject jsonObject = null;
            ArrayList<Product> list = new ArrayList<>();
            Product product = new Product();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" products error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    product.setStatus("404 error" + jsonObject.getString("message"));
                    list.add(product);
                    productMutableLiveData.setValue(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (error.getMessage() == null) {
                    product.setStatus("404 error" + "UnknownHostException");
                } else {
                    product.setStatus("404 error" + error.getMessage());
                }
                list.add(product);
                productMutableLiveData.setValue(list);
            }

            Log.e("getProducts2", "getProducts: ", error);
        });
        return productMutableLiveData;
    }

    /**
     * Filter products live data.
     *
     * @param searchData the search data
     * @param limitData  the limit data
     * @return the live data
     */
    @NotNull
    public static MutableLiveData<List<Product>> filterProducts(@NotNull Search searchData, @NotNull LimitData limitData) {
        MutableLiveData<List<Product>> productMutableLiveData = new MutableLiveData<>();
        String url = String.format(Constants.PRODUCT_FILTER_URL, Constants.USER_ID, limitData.limit, limitData.offset,
                searchData.getCat_id(), searchData.getSub_cat_id(), searchData.getMax_price(), searchData.getSearchTerm());
        System.out.println(url);
        Type collectionType = new TypeToken<Collection<Product>>() {
        }.getType();
        Http.<List<Product>>fetch(url, "", collectionType, productMutableLiveData::postValue, error -> {
            if (error.networkResponse.statusCode == Constants.ERROR_404) {
                productMutableLiveData.setValue(null);
            }
        });
        return productMutableLiveData;
    }


}
