package com.dvishapps.yourshop.ui.layout.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.BuildConfig;
import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.ActivityNewShopListHomeBinding;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.adapters.RecycleAdapterFavoriteShops;
import com.dvishapps.yourshop.ui.adapters.ShopListAvailableAdapter;
import com.dvishapps.yourshop.ui.adapters.ShopListTopChoiceAdapter;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.layout.scanner.ZbarCustomScannerActivity;
import com.dvishapps.yourshop.ui.viewModel.ShopListViewModel;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity implements OnRecycleListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private static final int MY_PERMISSIONS_LOCATION_SETTING = 121;
    private static final int MY_PERMISSIONS_LOCATION_PERMISSION_SETTING = 221;
    private static final int DELAY_TIME = 3000;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 434;
    public static final String PLACE_AUTOCOMPLETE_STRING_REQUEST_CODE = "latlong";

    private ActivityNewShopListHomeBinding binding;
    private Toolbar toolbar;

    private ShopViewModel shopViewModel;
    private GPSTrackerService gpsTracker;

    List<RecycleItem> itemList = new ArrayList<>();

    ShopListTopChoiceAdapter shopUsersAdapterTopChoices;
    ShopListAvailableAdapter shopUsersAdapterAvailable;
    List<Shop> shopList = new ArrayList<>();
    List<Shop> shopDetailsListInitialFiltered = new ArrayList<>();
    List<Shop> shopDetailsListBusinessTypeFiltered = new ArrayList<>();
    List<Shop> shopDetailsListTopChoiceFiltered = new ArrayList<>();
    //    List<Shop> shopDetailsListAvailableFiltered = new ArrayList<>();
    private Dialog dialog;

    ShopListDb shopListDb;
    String latLng = "";

    private RecycleAdapterFavoriteShops recycleAdapter;
    private ShopListViewModel shopListViewModel;

    private boolean permissionSettingsIntentCalled = true;

    String shop_bussiness_type = "0";         //("0", "Restaurants")  ("1", "Grocery Stores") ("2", "Vegetable & Pazhamudhir") ("3", "Chicken/Meat,Fish")

    int latLngCount = 0;
    int latLngCountCycle = 0;
    private String latitude = "";
    private String longitude = "";
    private String place = "";

    boolean btnScanClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_shop_list_home);
//        toolbar = findViewById(R.id.header);
//        getSupportActionBar().hide();
//TODO: Call settings
//        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
//        WorkManager.getInstance(HomeActivity.this).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
//        if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
//            AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
//        power_management();

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopListViewModel = new ViewModelProvider(this).get(ShopListViewModel.class);
        shopListDb = new ShopListDb(HomeActivity.this);

        binding.parentLayout.setVisibility(View.GONE);
        binding.secondaryLayout.setVisibility(View.VISIBLE);

        gpsTracker = new GPSTrackerService(this);

        shopViewModel.fetchShopWithId("shop0b5f60e50f662d7e825cbd3d1ecdf186"); //   YourShop Id
        shopViewModel.getShopDataTemp().observeForever(shop -> {
            if (shop != null) {

                if (shop.getStatus().contains("404 error")) {
                    if (shop.getStatus().contains("UnknownHostException")) {
                        internetSnack(binding.parentLayout);
                    }
                } else {
                    if (shop.getShop_app_force_update().equals("1")) {
                        if (shop.getShop_current_version().length() > 0) {
                            if (!shop.getShop_current_version().equals(BuildConfig.VERSION_NAME)) {
                                customAlertForcedUpdate(HomeActivity.this, shop.getShop_current_version());
                            }
                        }
                    }
                }
            }
        });


        binding.edtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getLocation();

                SessionData.getInstance().setLocationText(binding.edtLocation.getText().toString());
                Intent intent = new Intent(HomeActivity.this, LocationSearchActivity.class);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                try {
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(NewShopListScanActivity.this);
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO: Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                }
            }
        });

        binding.lnrSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        binding.lnrSetLocationOnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, MY_PERMISSIONS_LOCATION_PERMISSION_SETTING);
                permissionSettingsIntentCalled = true;
            }
        });


        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnScanClickable) {
                    Intent intent = new Intent(HomeActivity.this, ZbarCustomScannerActivity.class);
                    startActivityForResult(intent, 101);
                }
            }
        });

        binding.txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ShopFavoriteListActivity.class);
                startActivity(intent);
            }
        });


        binding.topChoiceViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionData.getInstance().setShopDetailsListTopChoiceFiltered(shopDetailsListTopChoiceFiltered);
                Intent intent = new Intent(HomeActivity.this, ShopTopChoicesListActivity.class);
                startActivity(intent);
            }
        });

        binding.txtAvailableShopsViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionData.getInstance().setShopDetailsListAvailableFiltered(shopDetailsListBusinessTypeFiltered);
                Intent intent = new Intent(HomeActivity.this, ShopAvailableListActivity.class);
                startActivity(intent);
            }
        });


        shopUsersAdapterTopChoices = new ShopListTopChoiceAdapter(shopDetailsListTopChoiceFiltered, shopKey -> {
            SessionData.getInstance().setShopKeyValue(shopKey);

            directShopIn(shopKey, false);
//            Intent intent = new Intent(getActivity(), EditShopForm.class);
//            startActivity(intent);
        });

        shopUsersAdapterAvailable = new ShopListAvailableAdapter(shopDetailsListBusinessTypeFiltered, shopKey -> {
            SessionData.getInstance().setShopKeyValue(shopKey);

            directShopIn(shopKey, false);
//            Intent intent = new Intent(getActivity(), EditShopForm.class);
//            startActivity(intent);
        });

        RecyclerView recyclerView = binding.shopList;
        LinearLayoutManager llmHor = new LinearLayoutManager(this);
        llmHor.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llmHor);
        recycleAdapter = new RecycleAdapterFavoriteShops(shopListViewModel.getRecycleItems().getValue(), this, true);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setImageSize(150, 150);
        recycleAdapter.setItemSize(200, 200);


        itemList = shopListDb.getAllData();
        Collections.reverse(itemList);
        shopListViewModel.recycleItemsList.clear();
        shopListViewModel.recycleItemsList.addAll(itemList);
        shopListViewModel.setRecycleItems();

        if (itemList.size() > 0) {
            binding.lnrChoose.setVisibility(View.VISIBLE);
//            binding.mariginBottomView.setVisibility(View.GONE);
        } else {
            binding.lnrChoose.setVisibility(View.GONE);
//            binding.mariginBottomView.setVisibility(View.VISIBLE);
        }

        shopListViewModel.getRecycleItems().observeForever(recycleItems -> {
            recycleAdapter.setItems(recycleItems);
        });


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.shopListTopChoice.setLayoutManager(llm);
        binding.shopListTopChoice.setNestedScrollingEnabled(true);
        binding.shopListTopChoice.setAdapter(shopUsersAdapterTopChoices);

        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(binding.shopListTopChoice);


        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        binding.shopListAvailable.setLayoutManager(llm2);
        binding.shopListAvailable.setNestedScrollingEnabled(false);
        binding.shopListAvailable.setAdapter(shopUsersAdapterAvailable);

//        binding.shopListAvailable.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));


        SnapHelper snapHelper2 = new GravitySnapHelper(Gravity.START);
        snapHelper2.attachToRecyclerView(binding.shopListAvailable);


        binding.restaurantLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRestaurants();
            }
        });

        binding.restaurantSelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRestaurants();
            }
        });

        binding.groceryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGrocery();
            }
        });

        binding.grocerySelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGrocery();
            }
        });

        binding.vegetablesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVegetables();
            }
        });

        binding.vegetablesSelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVegetables();
            }
        });

        binding.meatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMeat();
            }
        });

        binding.meatSelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMeat();
            }
        });


        getLocation();

//        searchLoc();

//        testSort();
    }

    private void testSort() {
        ArrayList<Double> arraylist = new ArrayList<Double>();
        arraylist.add(1.0);
        arraylist.add(11.0);
        arraylist.add(12.0);
        arraylist.add(2.0);
        arraylist.add(23.0);
        arraylist.add(21.0);
        arraylist.add(7.0);
        arraylist.add(3.0);
        arraylist.add(32.0);
        /* ArrayList before the sorting*/
        System.out.println("zxc Before Sorting:");
        for (double counter : arraylist) {
            System.out.println("zxc" + counter);
        }

        /* Sorting of arraylist using Collections.sort*/
        Collections.sort(arraylist);

        /* ArrayList after sorting*/
        System.out.println("zxc After Sorting:");
        for (double counter : arraylist) {
            System.out.println("zxc" + counter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onItemClick(RecycleItem recycleItem, int position) {

        directShopIn(recycleItem.getShopId(), false);

//        Config.editPreferences.putString(Constants.SHOP_KEY, recycleItem.getShopId()).apply();
//        if (!Config.sharedPreferences.getString(Constants.SHOP_KEY, "") .equals(recycleItem.getShopId())) {
//            Config.editPreferences.remove(Constants.CART).apply();
//        }

//        SessionData.getInstance().setShopKeyValue(recycleItem.getShopId());
//        DynamicConstants.getInstance().clearAllData();
//        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
//        Intent intent = new Intent(NewShopListScanActivity.this, MainActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }


    public void directShopIn(String shopId, boolean noback) {
//        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != null) {
        if (!Config.sharedPreferences.getString(Constants.SHOP_KEY, "").equals(shopId)) {
            Config.editPreferences.remove(Constants.CART).apply();
            Config.cart = new Cart();
            Config.currentUser = null;
            Config.editPreferences.remove(Constants.LOGGED_USER);
            Config.editPreferences.remove(Constants.SHOP_OWNER);
            Config.editPreferences.putString(Constants.CART, JsonUtils.toJsonString(Config.cart));
            Config.editPreferences.apply();
        }
        Config.editPreferences.putString(Constants.SHOP_KEY, shopId).apply();
        Config.editPreferences.putString(Constants.SHOP_MAIN_BRANCH_KEY, shopId).apply();

//        Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
//        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(shopId);

        for (int i = 0; i < shopDetailsListInitialFiltered.size(); i++) {
            if (shopDetailsListInitialFiltered.get(i).getId().equals(shopId)) {
                SessionData.getInstance().setSelectedShop(shopDetailsListInitialFiltered.get(i));
                break;
            }
        }
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
        Intent intent = new Intent(HomeActivity.this, BranchListActivity.class);
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

    public void internetSnack(View parentLayout) {
        binding.progressStart.setVisibility(View.GONE);
        binding.lnrSetLocation.setVisibility(View.GONE);
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Try again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
//                            retryFunctions();
                            getLocation();
                        } else {
//                            internetSnack(parentLayout);
                        }

                    }
                })
                .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black))
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                latLngCount = 0;
                getLocation();
            } else {
//                Console.toast("Location permission required to proceed!");
                latLngCount = 0;
                binding.lnrSetLocation.setVisibility(View.VISIBLE);
                binding.progressStart.setVisibility(View.GONE);

                binding.lnrSetLocation.setVisibility(View.VISIBLE);
                binding.lnrSetLocationOnSettings.setVisibility(View.VISIBLE);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                        Console.toast("Location permission required to proceed!");
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // Do something after 5s = 5000ms
//                                getLocation();
//                            }
//                        }, DELAY_TIME);
//                    } else {
//                        binding.lnrSetLocation.setVisibility(View.VISIBLE);
//                        binding.lnrSetLocationOnSettings.setVisibility(View.VISIBLE);
//
////                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
////                                Uri.fromParts("package", getPackageName(), null));
////                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        startActivityForResult(intent,MY_PERMISSIONS_LOCATION_PERMISSION_SETTING);
////                        Console.toast("Location permission required to proceed!");
//                    }
//                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String latlng = data.getStringExtra(PLACE_AUTOCOMPLETE_STRING_REQUEST_CODE);
                latitude = "";
                longitude = "";
                if (latlng != null) {
                    if (latlng.length() > 0) {
                        try {
                            latitude = latlng.split(",")[0];
                            longitude = latlng.split(",")[1];
                            place = latlng.split(",")[2];


                            if (latitude != null) {
                                if (latitude.length() > 0) {
                                    Config.editPreferences.putString(Constants.LAT, latitude + "").apply();
                                    Config.editPreferences.putString(Constants.LNG, longitude + "").apply();
                                    SessionData.getInstance().setLat(Double.parseDouble(latitude));
                                    SessionData.getInstance().setLng(Double.parseDouble(longitude));
                                    fetchShopBranchesFromOldLocation();
                                }
                            }
                            if (place != null) {
                                if (place.length() > 0) {
                                    binding.edtLocation.setText(place);
                                }
                            }


//                            try {
//                                List<Address> addressList = gpsTracker.getGeocoderAddress(NewShopListScanActivity.this,
//                                        Long.parseLong(latitude), Long.parseLong(longitude));
//
//                                if (addressList != null) {
//                                    if (addressList.size() > 0) {
//                                        binding.edtLocation.setText("" + addressList.get(0).getLocality());
//                                    } else {
////                                        binding.edtLocation.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
//                                    }
//                                } else {
//                                    if (!Tools.isOnline()) {
//                                        Console.toast("No internet Connection");
//                                        finish();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

//                            binding.edtLocation.setText();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // TODO Update your TextView.
            }
//            getLocation();
        } else if (requestCode == MY_PERMISSIONS_LOCATION_SETTING) {
            getLocation();
        } else if (requestCode == MY_PERMISSIONS_LOCATION_PERMISSION_SETTING) {
//            if (!permissionSettingsIntentCalled) {
//                getLocation();
//            } else {
//                permissionSettingsIntentCalled = false;
//            }
        }


    }

    public void getLocation() {
        Console.logDebug("getLocation Count : " + latLngCount);
        if (Tools.isOnline()) {
            binding.progressStart.setVisibility(View.VISIBLE);
            binding.lnrSetLocation.setVisibility(View.GONE);
            binding.lnrSetLocationOnSettings.setVisibility(View.GONE);
            gpsTracker.getLocation();
            if (gpsTracker.canGetGps()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        Console.toast("Location permission required!");
                        binding.lnrSetLocation.setVisibility(View.GONE);///
                        binding.progressStart.setVisibility(View.GONE);
                        return;
                    } else {
                        latLngCount = latLngCount + 1;

                        if (gpsTracker.getIsGPSTrackingEnabled()) {
//                                    SessionData.getInstance().setMapFrom("EditProfileActivity");
                            String loc = "";

                            if (gpsTracker.getSubLocality(HomeActivity.this) != null) {
                                loc = gpsTracker.getSubLocality(HomeActivity.this) + ", " + gpsTracker.getLocality(HomeActivity.this);
                            } else {
                                loc = gpsTracker.getLocality(HomeActivity.this);
                            }
                            SessionData.getInstance().setLat(gpsTracker.getLatitude());
                            SessionData.getInstance().setLng(gpsTracker.getLongitude());
                            binding.edtLocation.setText(loc);

//                            if (gpsTracker.getLatitude() != 0) {
                            if (gpsTracker.getLatitude() == 0) {
//                                Console.toast(" Latitude 0");

                                if (latLngCount <= 1) {
                                    getLocation();
                                } else {
                                    final Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            if (latLngCount <= 2) {
                                                getLocation();
                                            } else if (latLngCount <= 4) {
                                                gpsTracker.getLocation(true);
                                                getLocation2();
                                            } else {
                                                gpsTracker.getLocation(true);
                                                getLocation2();
                                                //                                            Console.toast("Can not fetch location");
                                                //                                            binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                                                //                                            binding.progressStart.setVisibility(View.GONE);
                                            }
                                        }
                                    }, DELAY_TIME);
                                }
                            } else {
                                fetchShopBranches();
                            }
                        } else {
                            binding.lnrSetLocation.setVisibility(View.GONE);//
                            binding.progressStart.setVisibility(View.GONE);
//                            gpsTracker.showSettingsAlert();
                            customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");

                        }
                    }
                } else {
                    latLngCount = latLngCount + 1;

                    if (gpsTracker.getIsGPSTrackingEnabled()) {
                        String loc = "";

                        if (gpsTracker.getSubLocality(HomeActivity.this) != null) {
                            loc = gpsTracker.getSubLocality(HomeActivity.this) + ", " + gpsTracker.getLocality(HomeActivity.this);
                        } else {
                            loc = gpsTracker.getLocality(HomeActivity.this);
                        }
                        SessionData.getInstance().setLat(gpsTracker.getLatitude());
                        SessionData.getInstance().setLng(gpsTracker.getLongitude());
                        binding.edtLocation.setText(loc);

//                        if (gpsTracker.getLatitude() != 0) {
                        if (gpsTracker.getLatitude() == 0) {
//                            Console.toast(" Latitude 0");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    if (latLngCount <= 3) {
                                        getLocation();
                                    } else if (latLngCount <= 4) {
                                        gpsTracker.getLocation(true);
                                        getLocation2();
                                    } else {
                                        gpsTracker.getLocation(true);
                                        getLocation2();
//                                            Console.toast("Can not fetch location");
//                                            binding.lnrSetLocation.setVisibility(View.VISIBLE);//
//                                            binding.progressStart.setVisibility(View.GONE);
                                    }
                                }
                            }, DELAY_TIME);
                        } else {
                            fetchShopBranches();
                        }
                    } else {
                        binding.lnrSetLocation.setVisibility(View.GONE);//
                        binding.progressStart.setVisibility(View.GONE);
//                        gpsTracker.showSettingsAlert();
                        customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
                    }
                }
            } else {
                binding.lnrSetLocation.setVisibility(View.GONE);//
                binding.progressStart.setVisibility(View.GONE);
//                gpsTracker.showSettingsAlert();
                customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
            }
        } else {
            internetSnack(binding.parentLayout);

        }
    }

    public void getLocation2() {
        if (Tools.isOnline()) {
            Console.logDebug("getLocation Count : " + latLngCount);
            binding.progressStart.setVisibility(View.VISIBLE);
            binding.lnrSetLocation.setVisibility(View.GONE);
            binding.lnrSetLocationOnSettings.setVisibility(View.GONE);
            //            gpsTracker.getLocation();
            if (gpsTracker.canGetNetworkLocation()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        Console.toast("Location permission required!");
                        binding.lnrSetLocation.setVisibility(View.GONE);///
                        binding.progressStart.setVisibility(View.GONE);
                        return;
                    } else {
                        latLngCount = latLngCount + 1;

                        if (gpsTracker.canGetNetworkLocation()) {
                            String loc = "";

                            if (gpsTracker.getSubLocality(HomeActivity.this) != null) {
                                loc = gpsTracker.getSubLocality(HomeActivity.this) + ", " + gpsTracker.getLocality(HomeActivity.this);
                            } else {
                                loc = gpsTracker.getLocality(HomeActivity.this);
                            }
                            SessionData.getInstance().setLat(gpsTracker.getLatitude());
                            SessionData.getInstance().setLng(gpsTracker.getLongitude());
                            binding.edtLocation.setText(loc);

//                            if (gpsTracker.getLatitude() != 0) {
                            if (gpsTracker.getLatitude() == 0) {
                                //                                Console.toast(" Latitude 0");
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        gpsTracker.getLocation(true);
                                        if (latLngCount <= 6) {
                                            getLocation2();
                                        } else {
                                            if (latLngCount <= 7) {
                                                getLocation3();
                                            } else {
                                                Console.toast("Can not fetch location");
                                                binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                                                binding.progressStart.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }, DELAY_TIME);
                            } else {
                                fetchShopBranches();
                            }
                        } else {
                            binding.lnrSetLocation.setVisibility(View.GONE);//
                            binding.progressStart.setVisibility(View.GONE);
                            //                            gpsTracker.showSettingsAlert();
                            customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");

                        }
                    }
                } else {
                    latLngCount = latLngCount + 1;

                    if (gpsTracker.canGetNetworkLocation()) {
                        String loc = "";

                        if (gpsTracker.getSubLocality(HomeActivity.this) != null) {
                            loc = gpsTracker.getSubLocality(HomeActivity.this) + ", " + gpsTracker.getLocality(HomeActivity.this);
                        } else {
                            loc = gpsTracker.getLocality(HomeActivity.this);
                        }
                        SessionData.getInstance().setLat(gpsTracker.getLatitude());
                        SessionData.getInstance().setLng(gpsTracker.getLongitude());
                        binding.edtLocation.setText(loc);

//                        if (gpsTracker.getLatitude() != 0) {
                        if (gpsTracker.getLatitude() == 0) {
                            //                            Console.toast(" Latitude 0");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    gpsTracker.getLocation(true);
                                    if (latLngCount <= 6) {
                                        getLocation2();
                                    } else {
                                        if (latLngCount <= 7) {
                                            getLocation3();
                                        } else {
                                            Console.toast("Can not fetch location");
                                            binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                                            binding.progressStart.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }, DELAY_TIME);
                        } else {
                            fetchShopBranches();
                        }
                    } else {
                        //no Location issue
                        Console.toast("Can not fetch location");
                        binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                        binding.progressStart.setVisibility(View.GONE);
                        //                        binding.lnrSetLocation.setVisibility(View.GONE);//
                        //                        binding.progressStart.setVisibility(View.GONE);
                        ////                        gpsTracker.showSettingsAlert();
                        //                        customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
                    }
                }
            } else {
                //no Location issue
                Console.toast("Can not fetch location");
                binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                binding.progressStart.setVisibility(View.GONE);
                //                binding.lnrSetLocation.setVisibility(View.GONE);//
                //                binding.progressStart.setVisibility(View.GONE);
                ////                gpsTracker.showSettingsAlert();
                //                customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
            }
        } else {
            internetSnack(binding.parentLayout);

        }
    }

    public void getLocation3() {
        Console.logDebug("getLocation Count : " + latLngCount);
        if (Config.sharedPreferences.getString(Constants.LAT, "").length() > 0) {
            fetchShopBranchesFromOldLocation();
        } else {
            if (latLngCountCycle == 0) {
                Console.toast("Can not fetch location");
                binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                binding.progressStart.setVisibility(View.GONE);
            } else {
                latLngCountCycle = latLngCountCycle + 1;
                Console.toast("Can not fetch location. Try restarting your device");
                binding.lnrSetLocation.setVisibility(View.VISIBLE);//
                binding.progressStart.setVisibility(View.GONE);
            }
        }
    }

    public void fetchShopBranches() {

        Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
        Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
        SessionData.getInstance().setLatitude(gpsTracker.getLatitude() + "");
        SessionData.getInstance().setLongitude(gpsTracker.getLongitude() + "");
//        progressDialog.startLoading();
        binding.pb2.setVisibility(View.VISIBLE);
        btnScanClickable = false;
//        binding.btnScan.setVisibility(View.GONE);
//        progressDialog.startLoading();
        latLng = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
        if (latLng.length() < 5) {
//            Console.toastLong("Can not fetch current location");
        }
        shopViewModel.fetchCustomLocationShopList(
                gpsTracker.getLatitude() + "",
                gpsTracker.getLongitude() + ""
        ).observeForever(shops -> {
            if (shops != null) {

                try {
                    for (int i = 0; i < shops.size(); i++) {
                        shopList.add(shops.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                shopList.addAll(shops);

//                progressDialog.dismiss();
                binding.pb2.setVisibility(View.GONE);
                btnScanClickable = true;
//                binding.btnScan.setVisibility(View.VISIBLE);
//                progressDialog.dismiss();
                boolean internetError = false;
                if (shops.get(0).getStatus().contains("404 error")) {
                    if (shops.get(0).getStatus().contains("UnknownHostException")) {
                        internetSnack(binding.parentLayout);
                        internetError = true;
                    }
                    shops.clear();
                    shopList.clear();
                }
                if (shopList.size() == 0) {
                    if (!internetError) {
//                        if (Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "").length() > 0) {
                        if (latLng.length() > 5) {
//                                directShopIn(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""), true);
                            Console.toastLong("No shops Available here! Scan a shop.");
                        } else {
                            Console.toastLong("Can not fetch location");
                        }

//                        binding.parentLayout.setVisibility(View.VISIBLE);
//                        binding.secondaryLayout.setVisibility(View.GONE);
//                        } else {
//                            Console.toastLong("can not load shop!");
//                        }
                    }

                } else {
                    for (int i = 0; i < shopList.size(); i++) {
                        try {
                            shopList.get(i).setDistance(Double.parseDouble(Tools.round1(Tools.distanceInKmsSimple(
                                    Double.parseDouble(shopList.get(i).getLat()),
                                    Double.parseDouble(shopList.get(i).getLng()),
                                    SessionData.getInstance().getLat(), SessionData.getInstance().getLng()))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    user.setDistance(Tools.distance(user.getLat(), user.getLng()) + "");

//                    Collections.sort(shopList, new Comparator<Shop>() {
//                        @Override
//                        public int compare(Shop lhs, Shop rhs) {
//                            return lhs.getDistance().compareTo(rhs.getDistance());
//                        }
//                    });

                    //TODO: filter for premium shops - Top Choice
                    //TODO: filter for Classic shops - Available
//                    shopUsersAdapterTopChoices.setItems(shopList);

                }

                binding.parentLayout.setVisibility(View.VISIBLE);
                binding.secondaryLayout.setVisibility(View.GONE);

                shopDetailsListInitialFiltered.clear();
                shopDetailsListInitialFiltered.addAll(shopList);
                //TODO: Handle filters
                //TODO: filter for  restaurant
                setRestaurants();
//                shopUsersAdapter.notifyDataSetChanged();
//                Console.toast(shops.size() + "");
            }
        });
    }

    public void fetchShopBranchesFromOldLocation() {


//        Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
//        Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();

//        progressDialog.startLoading();
        SessionData.getInstance().setLatitude(Config.sharedPreferences.getString(Constants.LAT, "") + "");
        SessionData.getInstance().setLongitude(Config.sharedPreferences.getString(Constants.LNG, "") + "");

        binding.pb2.setVisibility(View.VISIBLE);
        btnScanClickable = false;
//        binding.btnScan.setVisibility(View.GONE);
//        progressDialog.startLoading();
//        latLng = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
//        if (latLng.length() < 5) {
//            Console.toastLong("Can not fetch current location");
//        }
        shopViewModel.fetchCustomLocationShopList(
                Config.sharedPreferences.getString(Constants.LAT, "") + "",
                Config.sharedPreferences.getString(Constants.LNG, "") + ""
        ).observeForever(shops -> {
            if (shops != null) {

                shopList.addAll(shops);

//                progressDialog.dismiss();
                binding.pb2.setVisibility(View.GONE);
                btnScanClickable = true;

//                binding.btnScan.setVisibility(View.VISIBLE);
//                progressDialog.dismiss();
                boolean internetError = false;
                if (shops.get(0).getStatus().contains("404 error")) {
                    if (shops.get(0).getStatus().contains("UnknownHostException")) {
                        internetSnack(binding.parentLayout);
                        internetError = true;
                    }
                    shops.clear();
                    shopList.clear();
                }

                if (shopList.size() == 0) {
                    if (!internetError) {
//                        if (Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "").length() > 0) {
                        if (latLng.length() > 5) {
//                                directShopIn(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""), true);
                            Console.toastLong("No shops Available here! Scan a shop.");
                        } else {
                            Console.toastLong("Can not fetch location");
                        }

//                        binding.parentLayout.setVisibility(View.VISIBLE);
//                        binding.secondaryLayout.setVisibility(View.GONE);
//                        } else {
//                            Console.toastLong("can not load shop!");
//                        }
                    }

                } else {
                    for (int i = 0; i < shopList.size(); i++) {
                        try {
                            shopList.get(i).setDistance(Double.parseDouble(Tools.round1(Tools.distanceInKmsSimple(
                                    Double.parseDouble(shopList.get(i).getLat()),
                                    Double.parseDouble(shopList.get(i).getLng()),
                                    SessionData.getInstance().getLat(), SessionData.getInstance().getLng()))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    user.setDistance(Tools.distance(user.getLat(), user.getLng()) + "");

//                    Collections.sort(shopList, new Comparator<Shop>() {
//                        @Override
//                        public int compare(Shop lhs, Shop rhs) {
//                            return lhs.getDistance().compareTo(rhs.getDistance());
//                        }
//                    });

                    //TODO: filter for premium shops - Top Choice
                    //TODO: filter for Classic shops - Available
//                    shopUsersAdapterTopChoices.setItems(shopList);

                }

                binding.parentLayout.setVisibility(View.VISIBLE);
                binding.secondaryLayout.setVisibility(View.GONE);

                shopDetailsListInitialFiltered.clear();
                shopDetailsListInitialFiltered.addAll(shopList);
                //TODO: Handle filters
                //TODO: filter for  restaurant
                setRestaurants();
//                shopUsersAdapter.notifyDataSetChanged();
//                Console.toast(shops.size() + "");
            }
        });
    }

    public void customAlertNew(String message, String title, String strOk, String strCancel) { // nORMAL SCAN

        dialog = new Dialog(HomeActivity.this);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);
        View view_cancel = (View) dialog.findViewById(R.id.view_cancel);

        ImageView closeImg = dialog.findViewById(R.id.close_img);

        txtTitle.setText(title);
        txtMessage.setText(message);
        ok.setText(strOk);
        cancel.setText(strCancel);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, MY_PERMISSIONS_LOCATION_SETTING);

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.lnrSetLocation.setVisibility(View.VISIBLE);
                dialog.dismiss();

            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.lnrSetLocation.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void setIndividualShopPreference(String shop_id) {

        Config.editPreferences.putString(Constants.SHOP_KEY, shop_id).apply();
        Config.editPreferences.putString(Constants.SHOP_MAIN_BRANCH_KEY, shop_id).apply();

        SessionData.getInstance().setShopKeyValue(shop_id);
        Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
    }

    public void setRestaurants() {
        binding.restaurantLayout.setVisibility(View.GONE);
        binding.restaurantSelectedLayout.setVisibility(View.VISIBLE);
        binding.groceryLayout.setVisibility(View.VISIBLE);
        binding.grocerySelectedLayout.setVisibility(View.GONE);
        binding.vegetablesLayout.setVisibility(View.VISIBLE);
        binding.vegetablesSelectedLayout.setVisibility(View.GONE);
        binding.meatLayout.setVisibility(View.VISIBLE);
        binding.meatSelectedLayout.setVisibility(View.GONE);

        shop_bussiness_type = "0";

        List<Shop> filteredList = new ArrayList<>();
        for (Shop item : shopDetailsListInitialFiltered) {
            if (item.getShop_bussiness_type().toLowerCase().trim().contains(shop_bussiness_type)) {
                filteredList.add(item);
            }
        }

        shopDetailsListBusinessTypeFiltered.clear();
        shopDetailsListBusinessTypeFiltered.addAll(filteredList);
        setTopChoicesAndAvailable();
    }

    public void setGrocery() {
        binding.restaurantLayout.setVisibility(View.VISIBLE);
        binding.restaurantSelectedLayout.setVisibility(View.GONE);
        binding.groceryLayout.setVisibility(View.GONE);
        binding.grocerySelectedLayout.setVisibility(View.VISIBLE);
        binding.vegetablesLayout.setVisibility(View.VISIBLE);
        binding.vegetablesSelectedLayout.setVisibility(View.GONE);
        binding.meatLayout.setVisibility(View.VISIBLE);
        binding.meatSelectedLayout.setVisibility(View.GONE);

        shop_bussiness_type = "1";

        List<Shop> filteredList = new ArrayList<>();
        for (Shop item : shopDetailsListInitialFiltered) {
            if (item.getShop_bussiness_type().toLowerCase().trim().contains(shop_bussiness_type)) {
                filteredList.add(item);
            }
        }
        shopDetailsListBusinessTypeFiltered.clear();
        shopDetailsListBusinessTypeFiltered.addAll(filteredList);
        setTopChoicesAndAvailable();
    }

    public void setVegetables() {
        binding.restaurantLayout.setVisibility(View.VISIBLE);
        binding.restaurantSelectedLayout.setVisibility(View.GONE);
        binding.groceryLayout.setVisibility(View.VISIBLE);
        binding.grocerySelectedLayout.setVisibility(View.GONE);
        binding.vegetablesLayout.setVisibility(View.GONE);
        binding.vegetablesSelectedLayout.setVisibility(View.VISIBLE);
        binding.meatLayout.setVisibility(View.VISIBLE);
        binding.meatSelectedLayout.setVisibility(View.GONE);

        shop_bussiness_type = "2";

        List<Shop> filteredList = new ArrayList<>();
        for (Shop item : shopDetailsListInitialFiltered) {
            if (item.getShop_bussiness_type().toLowerCase().trim().contains(shop_bussiness_type)) {
                filteredList.add(item);
            }
        }

        shopDetailsListBusinessTypeFiltered.clear();
        shopDetailsListBusinessTypeFiltered.addAll(filteredList);
        setTopChoicesAndAvailable();
    }

    public void setMeat() {
        binding.restaurantLayout.setVisibility(View.VISIBLE);
        binding.restaurantSelectedLayout.setVisibility(View.GONE);
        binding.groceryLayout.setVisibility(View.VISIBLE);
        binding.grocerySelectedLayout.setVisibility(View.GONE);
        binding.vegetablesLayout.setVisibility(View.VISIBLE);
        binding.vegetablesSelectedLayout.setVisibility(View.GONE);
        binding.meatLayout.setVisibility(View.GONE);
        binding.meatSelectedLayout.setVisibility(View.VISIBLE);

        shop_bussiness_type = "3";

        List<Shop> filteredList = new ArrayList<>();
        for (Shop item : shopDetailsListInitialFiltered) {
            if (item.getShop_bussiness_type().toLowerCase().trim().contains(shop_bussiness_type)) {
                filteredList.add(item);
            }
        }

        shopDetailsListBusinessTypeFiltered.clear();
        shopDetailsListBusinessTypeFiltered.addAll(filteredList);
        setTopChoicesAndAvailable();
    }

    public void setTopChoicesAndAvailable() {

        List<Shop> filteredListTop = new ArrayList<>();
        List<Shop> filteredListAvailable = new ArrayList<>();
        for (Shop item : shopDetailsListBusinessTypeFiltered) {
            if (item.getPlan_type().toLowerCase().trim().contains("0")) {
                filteredListTop.add(item);
            } else {
                filteredListAvailable.add(item);
            }
        }
//        Collections.sort(filteredListTop, new Comparator<Shop>() {
//            @Override
//            public int compare(Shop lhs, Shop rhs) {
//                return lhs.getDistance().compareTo(rhs.getDistance());
//            }
//        });
//        Collections.sort(filteredListAvailable, new Comparator<Shop>() {
//            @Override
//            public int compare(Shop lhs, Shop rhs) {
//                return lhs.getDistance().compareTo(rhs.getDistance());
//            }
//        });
        shopDetailsListTopChoiceFiltered.clear();
        shopDetailsListTopChoiceFiltered.addAll(filteredListTop);
        shopUsersAdapterTopChoices.setItems(shopDetailsListTopChoiceFiltered);

//        shopDetailsListAvailableFiltered.clear();
//        shopDetailsListAvailableFiltered.addAll(filteredListAvailable);
        shopUsersAdapterAvailable.setItems(shopDetailsListBusinessTypeFiltered);

        binding.shopCount.setText(shopDetailsListBusinessTypeFiltered.size() + "");
        checkNoShops();
    }

    private void checkNoShops() {
        if (shopDetailsListTopChoiceFiltered.size() == 0) {
            binding.topChoicesLayout.setVisibility(View.GONE);

        } else {
            binding.topChoicesLayout.setVisibility(View.VISIBLE);
        }

        if (shopDetailsListBusinessTypeFiltered.size() == 0) {
            binding.availableLayout.setVisibility(View.GONE);

            switch (shop_bussiness_type) {
                case "0":
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.emptyText.setText("Restaurants not available");
                    break;
                case "1":
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.emptyText.setText("Grocery shops not available");
                    break;
                case "2":
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.emptyText.setText("Vegetable shops not available");
                    break;
                case "3":
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.emptyText.setText("Chicken, meat / fish shops not available");
                    break;
            }
        } else {
            binding.availableLayout.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
        }
    }

    public void customAlertForcedUpdate(Context mContext, String version) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_forced_update);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);

        if (version.length() > 0) {
            txtMessage.setText("You need to update the latest version (" + version + ") to proceed.");
        } else {
            txtMessage.setText("You need to update the latest version to proceed.");
        }


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
//                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void power_management() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            Intent in = new Intent();
            in.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            in.setData(Uri.parse("package:" + packageName));
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your Shop").setMessage("Enable Battery Optimization")
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            startActivity(in);
                        })
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            }
        }
    }
}
