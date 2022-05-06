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
import com.dvishapps.yourshop.databinding.ActivityShopFavListBinding;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.ui.adapters.ShopRecycleAdapter;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.viewModel.ShopListViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;

import java.util.ArrayList;
import java.util.List;

public class ShopFavoriteListActivity extends AppCompatActivity implements OnRecycleListener {

    private ActivityShopFavListBinding binding;
    private Toolbar toolbar;

    private ShopRecycleAdapter recycleAdapter;
    private ShopListViewModel shopListViewModel;
    ShopListDb shopListDb;

    List<RecycleItem> itemList = new ArrayList<>();
    List<RecycleItem> itemListFiltered = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_fav_list);
//        toolbar = findViewById(R.id.header);
        getSupportActionBar().hide();
        shopListDb = new ShopListDb(ShopFavoriteListActivity.this);
        itemList = shopListDb.getAllData();
        itemListFiltered.addAll(itemList);

        shopListViewModel = new ViewModelProvider(this).get(ShopListViewModel.class);
        shopListViewModel.recycleItemsList.clear();
        shopListViewModel.recycleItemsList.addAll(itemList);
//        binding.txtChooseShop.setText("Previous Shop list (" + itemList.size() + ")");
        shopListViewModel.setRecycleItems();

        shopListViewModel.getRecycleItems().observeForever(recycleItems -> {
            recycleAdapter.setItems(recycleItems);
        });

        RecyclerView recyclerView = binding.shopList;
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recycleAdapter = new ShopRecycleAdapter(itemListFiltered, this, true);
        recyclerView.setAdapter(recycleAdapter);
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;

//        recycleAdapter.setImageSize(getScreenWidth() * 35 / 100, getScreenWidth() * 35 / 100);
//        recycleAdapter.setItemSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recycleAdapter.getFilter().filter(s);
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
        Intent intent = new Intent(ShopFavoriteListActivity.this, BranchListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
