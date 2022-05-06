package com.dvishapps.yourshop.ui.viewModel;

import androidx.lifecycle.LiveData;

import com.dvishapps.yourshop.api.services.ShopService;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShopViewModel extends SViewModel {
    private LiveData<Shop> shopData;
    private LiveData<Shop> shopDetails;
    private LiveData<Shop> shopDataTemp;
    private LiveData<List<Shop>> shopBranchesData;

    public ShopViewModel() {
        shopData = ShopService.getShopDetails2();
    }

    public void fetchShop() {
//        if (shopData != null)
//            return;
        shopData = ShopService.getShopDetails2();
    }

    public void fetchShopWithId(String shop_id) {
        if (shopDataTemp != null)
            return;
        shopDataTemp = ShopService.getShopDetails3(shop_id);
    }

    public LiveData<Shop> getShopDataTemp() {
        return shopDataTemp;
    }

    public LiveData<Shop> getShopData() {
        return shopData;
    }

    public LiveData<Shop> setShopSettings(String shop_open_time, String shop_close_time,
                                          String shop_branch, String shop_main_branch_id,
                                          String shop_current_version, String shop_app_force_update,
                                          String shop_id, String package_charges,
                                          String address2, String lat, String lng, String postal_code,
                                          String shipping_tax_value, String overall_tax_value
    ) {
        shopDetails = ShopService.setShopSettings(shop_open_time, shop_close_time, shop_branch,
                shop_main_branch_id, shop_current_version, shop_app_force_update, shop_id,
                package_charges, address2, lat, lng, postal_code, shipping_tax_value, overall_tax_value);
        return shopDetails;
    }


    public LiveData<List<Shop>> fetchCustomShopBranchList(String lat, String lng, String shop_main_branch_id) {
//        if (shopData != null)
//            return;
        return ShopService.getCustomShopBranchList(lat, lng, shop_main_branch_id);
    }

    public LiveData<List<Shop>> fetchCustomLocationShopList(String lat, String lng) {
//        if (shopData != null)
//            return;
        return ShopService.getCustomLocationShopList(lat, lng);
    }


}
