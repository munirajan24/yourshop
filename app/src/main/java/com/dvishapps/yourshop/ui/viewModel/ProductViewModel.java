package com.dvishapps.yourshop.ui.viewModel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dvishapps.yourshop.api.services.ProductService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.Category;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Search;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.SubCategory;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;
import com.dvishapps.yourshop.utils.Constants;

import java.util.List;

public class ProductViewModel extends SViewModel {

    private MutableLiveData<LimitData> productLimitData = new MutableLiveData<>();
    private MutableLiveData<Search> searchData = new MutableLiveData<>();
    private MutableLiveData<List<Photo>> featuredProductsPhoto = new MutableLiveData<>();

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<SubCategory>> subCategories;
    private LiveData<List<Product>> products;
    private LiveData<List<Product>> featuredProducts;
    private LiveData<List<Product>> filteredProducts;
    private MutableLiveData<Product> productDetails;
    private MutableLiveData<List<Photo>> productSubPhotos;


    private LiveData<String> productSubCategoryTitle;
    private LiveData<String> productCategoryTitle;

    public void fetchProductDetail(String product_id) {
        productDetails = ProductService.getProduct(product_id);
    }


    public void fetchProductDetailPhotos(String product_id) {
        productSubPhotos = ProductService.getProductSubImages(product_id);
    }


    public void fetchFeaturedProducts() {
        if (featuredProducts != null)
            return;
        featuredProducts = ProductService.getFeaturedProducts2();
    }

    public void fetchFeaturedProductsRetry() {
        featuredProducts = ProductService.getFeaturedProducts2();
    }

    public LiveData<Product> getProductDetails() {
        return productDetails;
    }

    public LiveData<List<Photo>> getProductSubPhotos() {
        if (featuredProducts == null) {
//            fetchProductDetailPhotos();
        }

        return productSubPhotos;
    }

    public void fetchProducts(String cat_id, String sub_cate_id) {
//        products = Transformations.switchMap(productLimitData, input -> {
//            if (input == null) {
//                return new MutableLiveData<>(new ArrayList<>());
//            }
//            return ProductService.searchProductByCategory(cat_id, sub_cate_id, input);
//        });
        setProductLimitData(new LimitData(Config.LIMIT_0, Config.OFFSET));
        products = ProductService.searchProductByCategory(cat_id, sub_cate_id, getProductLimitData());
    }

    public void fetchProductsWithNoSubcategory(String cat_id) {
//        products = Transformations.switchMap(productLimitData, input -> {
//            if (input == null) {
//                return new MutableLiveData<>(new ArrayList<>());
//            }
//            return ProductService.searchProductByCategoryWithNoSubCategory(cat_id, input);
//        });
        setProductLimitData(new LimitData(Config.LIMIT_0, Config.OFFSET));

        products = ProductService.searchProductByCategoryWithNoSubCategory(cat_id, getProductLimitData());
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void fetchCategories() {
        if (categories != null)
            return;
        categories = ProductService.getCategories();
    }

    public void fetchCategoriesRetry() {
        categories = ProductService.getCategories();
    }

    public LiveData<Shop> createCategory(Bitmap cover, Bitmap icon, String name, String subcategory_need,String is_enable, String status, String added_user_id, String shopKeyValue, String category_bussiness_type, String category_open_time, String category_close_time) {
        return ProductService.postCategoryWithData(cover, icon, name, subcategory_need,is_enable, status, added_user_id, shopKeyValue,category_bussiness_type, category_open_time, category_close_time);
    }

    public LiveData<Shop> updateCategory(Bitmap cover, Bitmap icon, String name, String subcategory_need, String cat_id, String is_enable,String status, String added_user_id, String shopKeyValue, String category_bussiness_type, String category_open_time, String category_close_time) {
        return ProductService.postUpdateCategoryWithData(cover, icon, cat_id, name, subcategory_need,is_enable, status, added_user_id, shopKeyValue,category_bussiness_type, category_open_time, category_close_time);
    }

    public LiveData<Shop> deleteCategory(Bitmap cover, Bitmap icon, String name, String cat_id, String status, String added_user_id, String shopKeyValue) {
        return ProductService.postDeleteCategoryWithData(cover, icon, cat_id, name, status, added_user_id, shopKeyValue);
    }



    public LiveData<Shop> createSubCategory(Bitmap cover, Bitmap icon, String name, String cat_id, String is_enable, String status, String added_user_id, String shopKeyValue, String bussiness_type, String open_time, String close_time) {
        return ProductService.postSubCategoryWithData(cover, icon, name, cat_id,is_enable, status, added_user_id, shopKeyValue,bussiness_type,open_time,close_time);
    }

    public LiveData<Shop> updateSubCategory(Bitmap cover, Bitmap icon, String name, String sub_category_id, String cat_id,String is_enable, String status, String added_user_id, String shopKeyValue, String bussiness_type, String open_time, String close_time) {
        return ProductService.postUpdateSubCategoryWithData(cover, icon, name, sub_category_id, cat_id,is_enable, status, added_user_id, shopKeyValue,bussiness_type,open_time,close_time);
    }

    public LiveData<Shop> deleteSubCategory(Bitmap cover, Bitmap icon, String name, String cat_id, String status, String added_user_id, String shopKeyValue) {
        return ProductService.postDeleteSubCategoryWithData(cover, icon, cat_id, name, status, added_user_id, shopKeyValue);
    }



    public LiveData<Shop> createProduct(Bitmap cover, Bitmap icon, String name, String product_measurement, String original_price,
                                        String unit_price, String sub_cat_id, String cat_id, String is_enable, String status, String is_available,
                                        String added_user_id, String shopKeyValue, String bussiness_type, String open_time, String close_time, String offer_price) {
        return ProductService.postProductWithData(cover, icon, name, product_measurement, original_price, unit_price, sub_cat_id, cat_id,is_enable, status, is_available, added_user_id, shopKeyValue,bussiness_type,open_time,close_time,offer_price);
    }

    public LiveData<Shop> updateProduct(Bitmap cover, Bitmap icon, String product_id, String name, String product_measurement, String original_price,
                                        String unit_price, String sub_cat_id, String cat_id, String is_enable,  String status, String is_available,
                                        String added_user_id, String shopKeyValue, String bussiness_type, String open_time, String close_time, String offer_price) {
        return ProductService.updateProductWithData(cover, icon, product_id, name, product_measurement, original_price, unit_price, sub_cat_id, cat_id, is_enable, status, is_available, added_user_id, shopKeyValue,bussiness_type,open_time,close_time,offer_price);
    }


    public LiveData<Shop> deleteProduct(Bitmap cover, Bitmap icon, String name, String cat_id, String status, String added_user_id, String shopKeyValue) {
        return ProductService.postDeleteProduct(cover, icon, cat_id, name, status, added_user_id, shopKeyValue);
    }


    public LiveData<Shop> addMoreImagesWithData(Bitmap cover, String product_id) {
        return ProductService.postAddMoreImagesWithData(cover, product_id);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void fetchSubCategories(String cat_id) {
        subCategories = ProductService.getSubCategories(cat_id);
//        ProductService.getFeaturedProducts(cat_id).observeForever(products -> {
//            if (products != null) {
//                List<Photo> list = new ArrayList<>();
//                for (Product p : products) {
//                    list.add(p.getDefault_photo());
//                }
//                featuredProductsPhoto.setValue(list);
//            }
//        });
    }

    public LiveData<List<SubCategory>> getSubCategories() {
        return subCategories;
    }

    public MutableLiveData<List<Photo>> getFeaturedProductsPhoto() {
        return featuredProductsPhoto;
    }

    public void filterProducts() {
        filteredProducts = Transformations.switchMap(searchData, input -> {
            if (input == null) {
                return new MutableLiveData<>();
            }
            return ProductService.filterProducts(input, new LimitData(Config.LIMIT_0, Config.OFFSET));
        });

    }


    public LiveData<List<Product>> getFilteredProducts() {
        return filteredProducts != null ? filteredProducts : new MutableLiveData<>();
    }

    public MutableLiveData<List<RecycleItem>> getRecycleItems() {
        return recycleItems;
    }

    public void setRecycleItems() {
        recycleItems.postValue(recycleItemsList);
    }

    public void setProductLimitData(LimitData productLimitData) {
        this.productLimitData.setValue(productLimitData);
    }

    public void setSearchData(Search searchData) {
        this.searchData.setValue(searchData);
    }

    public LimitData getProductLimitData() {
        return productLimitData.getValue();
    }

    public MutableLiveData<Search> getSearchLimitData() {
        return searchData;
    }

    public LiveData<List<Product>> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(LiveData<List<Product>> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    public LiveData<String> getProductSubCategoryTitle() {
        return productSubCategoryTitle;
    }

    public void fetchProductSubCategoryTitle() {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>(new String());
        mutableLiveData.setValue(Config.sharedPreferences.getString(Constants.SUB_CATEGORY_NAME, "null"));
        this.productSubCategoryTitle = mutableLiveData;
    }

    public LiveData<String> getProductCategoryTitle() {
        return productCategoryTitle;
    }

    public void fetchProductCategoryTitle() {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>(new String());
        mutableLiveData.setValue(Config.sharedPreferences.getString(Constants.CATEGORY_NAME, "null"));
        this.productCategoryTitle = mutableLiveData;
    }
}


