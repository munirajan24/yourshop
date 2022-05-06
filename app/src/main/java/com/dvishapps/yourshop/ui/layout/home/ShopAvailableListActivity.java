package com.dvishapps.yourshop.ui.layout.home;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.ActivityShopAvailableListBinding;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.adapters.ShopListAvailableVerticalAdapter;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.viewModel.ShopListViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;

import java.util.ArrayList;
import java.util.List;

public class ShopAvailableListActivity extends AppCompatActivity implements OnRecycleListener {

    private ActivityShopAvailableListBinding binding;
    private Toolbar toolbar;

    private ShopListViewModel shopListViewModel;
    ShopListDb shopListDb;

    List<RecycleItem> itemList = new ArrayList<>();

    ShopListAvailableVerticalAdapter shopListAvailableVerticalAdapter;
    List<Shop> shopDetailsListAvailableFiltered = new ArrayList<>();
    List<Shop> shopDetailsListAvailableFilteredSecondary = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_available_list);
//        toolbar = findViewById(R.id.header);
        getSupportActionBar().hide();
        shopListDb = new ShopListDb(ShopAvailableListActivity.this);

        shopDetailsListAvailableFiltered.clear();
        shopDetailsListAvailableFiltered.addAll(SessionData.getInstance().getShopDetailsListAvailableFiltered());
        shopDetailsListAvailableFilteredSecondary.addAll(shopDetailsListAvailableFiltered);
        binding.shopCount.setText(shopDetailsListAvailableFiltered.size() + "");

        shopListAvailableVerticalAdapter = new ShopListAvailableVerticalAdapter(shopDetailsListAvailableFilteredSecondary, shopKey -> {
            SessionData.getInstance().setShopKeyValue(shopKey);
            directShopIn(shopKey, false);
        });

        shopListViewModel = new ViewModelProvider(this).get(ShopListViewModel.class);

        RecyclerView recyclerView = binding.shopList;
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
//        shopUsersAdapterTopChoices = new ShopListTopChoiceAdapter(shopListViewModel.getRecycleItems().getValue(), this);
        recyclerView.setAdapter(shopListAvailableVerticalAdapter);

//        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
//        snapHelper.attachToRecyclerView(recyclerView);

//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;

//        recycleAdapter.setImageSize(getScreenWidth() * 35 / 100, getScreenWidth() * 35 / 100);
//        recycleAdapter.setItemSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        itemList = shopListDb.getAllData();
        shopListViewModel.recycleItemsList.clear();
        shopListViewModel.recycleItemsList.addAll(itemList);
        shopListViewModel.setRecycleItems();

        shopListViewModel.getRecycleItems().observeForever(recycleItems -> {
//            shopUsersAdapterTopChoices.setItems(recycleItems);
        });



        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shopListAvailableVerticalAdapter.getFilter().filter(s);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(RecycleItem recycleItem, int position) {
        Config.editPreferences.putString(Constants.SHOP_MAIN_BRANCH_KEY, recycleItem.getShopId()).apply();
        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(recycleItem.getShopId());
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, null));
        Intent intent = new Intent(ShopAvailableListActivity.this, BranchListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void directShopIn(String shopId, boolean noback) {
//        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != null) {
        Config.editPreferences.putString(Constants.SHOP_KEY, shopId).apply();
//        Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(shopId);
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
        Intent intent = new Intent(ShopAvailableListActivity.this, MainActivity.class);
        if (noback) {
            //single shop
            if (!SessionData.getInstance().isFromScan()) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        startActivity(intent);
        if (noback) {
            //single shop scan
            if (SessionData.getInstance().isFromScan()) {
                finish();
            }
        }
    }

}
