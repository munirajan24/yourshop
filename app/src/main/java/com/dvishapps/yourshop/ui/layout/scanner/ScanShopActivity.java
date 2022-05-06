package com.dvishapps.yourshop.ui.layout.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.ActivityScanShopBinding;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.adapters.RecycleAdapter;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.layout.home.BranchListActivity;
import com.dvishapps.yourshop.ui.layout.home.ShopFavoriteListActivity;
import com.dvishapps.yourshop.ui.viewModel.ShopListViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;

import java.util.ArrayList;
import java.util.List;

public class ScanShopActivity extends AppCompatActivity implements OnRecycleListener {

    private ActivityScanShopBinding binding;
    private Toolbar toolbar;

    private RecycleAdapter recycleAdapter;
    private ShopListViewModel shopListViewModel;
    ShopListDb shopListDb;

    List<RecycleItem> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_shop);
//        toolbar = findViewById(R.id.header);
//        getSupportActionBar().hide();

        if (Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, null) != null) {
            SessionData.getInstance().setShopKeyValue(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, null));
            SessionData.getInstance().setFromScan(false);
            Intent intent = new Intent(ScanShopActivity.this, BranchListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        shopListDb = new ShopListDb(ScanShopActivity.this);

        testShops();

        shopListViewModel = new ViewModelProvider(this).get(ShopListViewModel.class);

        RecyclerView recyclerView = binding.shopList;
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recycleAdapter = new RecycleAdapter(shopListViewModel.getRecycleItems().getValue(), this, true);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setImageSize(150, 150);
        recycleAdapter.setItemSize(200, 200);

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanShopActivity.this, ZbarCustomScannerActivity.class);
                startActivityForResult(intent,101);
            }
        });
        binding.txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanShopActivity.this, ShopFavoriteListActivity.class);
                startActivity(intent);
            }
        });


        itemList = shopListDb.getAllData();
        shopListViewModel.recycleItemsList.clear();
        shopListViewModel.recycleItemsList.addAll(itemList);
        shopListViewModel.setRecycleItems();

        if (itemList.size() > 0) {
            binding.lnrChoose.setVisibility(View.VISIBLE);
            binding.mariginBottomView.setVisibility(View.GONE);
        } else {
            binding.lnrChoose.setVisibility(View.GONE);
            binding.mariginBottomView.setVisibility(View.VISIBLE);
        }

        shopListViewModel.getRecycleItems().observeForever(recycleItems -> {
            recycleAdapter.setItems(recycleItems);
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101){

            itemList = shopListDb.getAllData();
            shopListViewModel.recycleItemsList.clear();
            shopListViewModel.recycleItemsList.addAll(itemList);
            shopListViewModel.setRecycleItems();

            if (itemList.size() > 0) {
                binding.lnrChoose.setVisibility(View.VISIBLE);
                binding.mariginBottomView.setVisibility(View.GONE);
            } else {
                binding.lnrChoose.setVisibility(View.GONE);
                binding.mariginBottomView.setVisibility(View.VISIBLE);
            }

            shopListViewModel.getRecycleItems().observeForever(recycleItems -> {
                recycleAdapter.setItems(recycleItems);
            });
        }
    }

    @Override
    public void onItemClick(RecycleItem recycleItem, int position) {
        Config.editPreferences.putString(Constants.SHOP_MAIN_BRANCH_KEY, recycleItem.getShopId()).apply();
        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(recycleItem.getShopId());
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, null));
        Intent intent = new Intent(ScanShopActivity.this, BranchListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void insertShop(String id, String name) {
        Shop shop = new Shop(id, name);

        if (!shopListDb.CheckIsShopExist(shop.getId())) {
//            shopListDb.insertRecords(shop.getId(), shop.getName(), "https://www.clipartmax.com/png/middle/291-2911410_business-store-front-shop-vector-icon-stock-vector-storefront-icon.png");
            shopListDb.insertRecords(shop.getId(), shop.getName(), shop.getDefault_photo().getUrl() + shop.getDefault_photo().getImg_path());
        }
    }

    public void testShops() {

//        scanWithShopId2("shop0c54d033ccebee59b6a1e7f0baa560b2");
//        scanWithShopId2("shop1f2a35ea2530a0472cdc40a9502c63fc"); //hai bro
//        scanWithShopId2("shop120b0176818c07999ed4ce18ea4e3902"); //veni
//        scanWithShopId2("shop671b23c780d73a75e5a2131068505ef9"); //selvam chetinad
//        scanWithShopId2("shop01672de80f1b2f0f3fa803b58c23d11d"); //teen dharia
//        scanWithShopId2("shop896978b3878e172c4a7822998d203065"); //form
//        scanWithShopId2("shopf8ebedb1868bee63301c8b844a5c907d"); //rusi
//        scanWithShopId2("shop14957a69f18e0633e70fc9de0b5add9f"); //kangeyam
//        scanWithShopId2("shopc8b38c534b085af1871328c63609940e"); //vkk rice
//        scanWithShopId2("shop832ea872b93353b6b1e29b11df924aff"); //sri velan chettinad

//        insertShop("shop671b23c780d73a75e5a2131068505ef9", "selvam chettinad hotel");
//        insertShop("shopf8ebedb1868bee63301c8b844a5c907d", "Rusi Nellai lala");
//        insertShop("shop14957a69f18e0633e70fc9de0b5add9f", "Kangeyam rice");
//        insertShop("shop896978b3878e172c4a7822998d203065", "Form carner");
//        insertShop("shop832ea872b93353b6b1e29b11df924aff", "sri velan chettinad");
//        insertShop("shopae0b1358059e084ae8883cd9e75eb200", "sri ram mess");

        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) == null) {

//        directShopIn("shopc8b38c534b085af1871328c63609940e");/// vkk
//        directShopIn("shop896978b3878e172c4a7822998d203065");/// form
//            directShopIn("shop14957a69f18e0633e70fc9de0b5add9f");/// kangeyam
//            directShopIn("shopa753df3600de2dac7ec25e7aa7228ee2");/// premiere
        }
    }

    public void directShopIn(String shopId) {
//        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != null) {
        Config.editPreferences.putString(Constants.SHOP_KEY, shopId).apply();
        Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(shopId);
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
        Intent intent = new Intent(ScanShopActivity.this, BranchListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        }
    }

}
