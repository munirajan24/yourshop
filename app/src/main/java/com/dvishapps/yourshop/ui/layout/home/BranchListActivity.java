package com.dvishapps.yourshop.ui.layout.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.ActivityShopBranchListBinding;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.adapters.ShopUsersAdapter;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.dvishapps.yourshop.ui.layout.home.HomeActivity.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static com.dvishapps.yourshop.ui.layout.home.HomeActivity.PLACE_AUTOCOMPLETE_STRING_REQUEST_CODE;

public class BranchListActivity extends AppCompatActivity implements OnRecycleListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private static final int MY_PERMISSIONS_LOCATION_SETTING = 121;
    private static final int MY_PERMISSIONS_LOCATION_PERMISSION_SETTING = 221;
    private static final long DELAY_TIME = 3000;

    private ActivityShopBranchListBinding binding;
    private Toolbar toolbar;

    private ShopViewModel shopViewModel;
    private GPSTrackerService gpsTracker;

    List<RecycleItem> itemList = new ArrayList<>();
    ProgressDialog progressDialog;

    ShopUsersAdapter shopUsersAdapter;
    List<Shop> shopList = new ArrayList<>();
    private Dialog dialog;

    ShopListDb shopListDb;
    String latLng = "";
    private boolean permissionSettingsIntentCalled = true;

    int latLngCount = 0;
    int latLngCountCycle = 0;
    private String latitude = "";
    private String longitude = "";
    private String place = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_branch_list);
//        toolbar = findViewById(R.id.header);
//        getSupportActionBar().hide();
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopListDb = new ShopListDb(BranchListActivity.this);

        binding.parentLayout.setVisibility(View.GONE);
        binding.secondaryLayout.setVisibility(View.VISIBLE);


///TODO:Step1 - Individual shop setup

//        setIndividualShopPreference("shopa6f642973a95fb87aa74c333c484da2a");// Blue Wave
//        setIndividualShopPreference("shopa753df3600de2dac7ec25e7aa7228ee2");// premier
//        setIndividualShopPreference("shop88e9107210cd853467b062c92fe85ead");// Akshaya Park Restaurant
//        setIndividualShopPreference("shop9d9fa7b7510b4d5211f6ae1a41a51c9c");// rhr
//        setIndividualShopPreference("shopfbafffb733e5dc5093be448e989bb130");// A1 chips

        //un paid
//        setIndividualShopPreference("shopa8c28f7d4036638835753d95f042b32e");// Sivakannan Restaurant
//        setIndividualShopPreference("shop288210b2b1a545cd0b681dd43af1a2be");// Johan Sea Foods


//        setIndividualShopPreference("shop446e2e5099ec5a8b5c584ef2797ace0b");// Thandav's Multi Cuisine Restaurant

        //        setIndividualShopPreference("shop3732dc44ccf3c1f94885b3483fb14910");// thesquarekitchen

//        setIndividualShopPreference("shopa0d11f797dd7df368036373ca42af9bf");// A1  Restaurant
//        setIndividualShopPreference("shopc19a738cd483ec464a314bf22654c4c9");// M H R Biriyani house


//        setIndividualShopPreference("shop7e51daa6f2f0538fa45b18a92cd148dc");//Hotel Yours
//        setIndividualShopPreference("shop6bfcf13985e316e394b1f50d991edf1f");//Bright Multi Services`


//        setIndividualShopPreference("shop6ede4d023cb8028bfd39acce4b590c57");// Test shop

        //TODO: Latest
        setIndividualShopPreference("shopb07fea385b2dd886cd51006133ac8569");// //red bowl
//        setIndividualShopPreference("shop7877e758e5f291920d3871cffbb94b41");// Arusuvai Restaurant
//          setIndividualShopPreference("shopc7cd34bc9ff079aa96970146cbd76e0c");// Kootansoru
//        setIndividualShopPreference("shop8ccaa51728297b03d6ff966e30ad0f4a");// SRKP Chettinad Mess
//        setIndividualShopPreference("shopa6d2d8450dc057baeadfa4f842b39a78");// Lakshmi Mess
//        setIndividualShopPreference("shop22434955996c0228341f05de7643f712");// Sai Samboorna Hotel
//        setIndividualShopPreference("shop195ebafe01167cb4b1e1cce7ed6584b5");// Ifthar Multicuisine Restaurant
//        setIndividualShopPreference("shope13b6e4eac77ba98ef7b11cbc3311bc8");//  Burma bhai
        //     setIndividualShopPreference("shop096d3bd1222bf6774db756894ec25fb3");//  Chennais Biriyani House - R S Puram
//        setIndividualShopPreference("shopea967edfb367bc0a243cd1e62e748557");//  Kovai President Biriyani
        // setIndividualShopPreference("shop0a07f190766445d7facb7bd33eba77fb");//  Gandhipuram Original Lala Sweets
//        setIndividualShopPreference("shop26cfb9abb2bb510f455548920b38228d");//  burgersquare
//        setIndividualShopPreference("shop68c4ea7729b8cda04b29f1cbf1f3750a");//  SS Hotel
//        setIndividualShopPreference("shop3a0ac42b68b0ca5dea4cd184f4c35b30");//  Durai's Super Market
//        setIndividualShopPreference("shop3fc082058d262b3b6a648a2ea28a9c23");//  almaas biriyani house

//        setIndividualShopPreference("shop6833085a2747c9ea76496d2b03164f1e");// Ancient Village Restaurant
//        setIndividualShopPreference("shop18689e7fdc6ab30f69035fcac26cbda4");// shree aksshayam
//        setIndividualShopPreference("shop5d8c3468b4e124e978ac2d06fae14991");// hotel maharaja kidavirunthu
//        setIndividualShopPreference("shop832ea872b93353b6b1e29b11df924aff");// sri velan chettinad mess
//        setIndividualShopPreference("shop2ba87085a6c5cb2a4ebcc7b109930f79");// Haryana dhaba restaurant and bakes
//        setIndividualShopPreference("shop18ef628b19563742e485b02cfab9b579");// lunch box


        if (shopListDb.CheckIsShopExist(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""))) {

            binding.txtShopName.setText(shopListDb.getName(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")));
//            Tools.setShopImageOffline(binding.imgLogo, Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""), NewShopListHomeActivity.this);
            if (shopListDb.getImageWithShopId(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")) != null) {
                binding.imgLogo.setImageBitmap(shopListDb.getImageWithShopId(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")));
                binding.imgMainLogo.setImageBitmap(shopListDb.getImageWithShopId(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")));
                binding.imgMainLogo.setVisibility(View.VISIBLE);
            }

        } else {
            binding.txtShopName.setText("");
        }

        if (SessionData.getInstance().getSelectedShop() != null) {
            binding.txtShopName.setText(SessionData.getInstance().getSelectedShop().getName());
            SessionData.getInstance().setSelectedShop(null);
        }

        progressDialog = new ProgressDialog(this);
        gpsTracker = new GPSTrackerService(this);

        shopViewModel.fetchShopWithId(SessionData.getInstance().getShopKeyValue());
        SessionData.getInstance().setShopKeyValue(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""));
        shopViewModel.getShopDataTemp().observeForever(shop -> {
            if (shop != null) {

                if (shop.getStatus().contains("404 error")) {
                    if (shop.getStatus().contains("UnknownHostException")) {
                        internetSnack(binding.parentLayout);
                    }
                } else {
                    binding.txtShopName.setText(shop.getName());
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(Constants.IMG_URL + shop.getDefault_photo().getImg_path())
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    binding.imgLogo.setImageBitmap(resource);
                                    binding.imgMainLogo.setImageBitmap(resource);
                                    binding.imgMainLogo.setVisibility(View.VISIBLE);
                                    if (!shopListDb.CheckIsShopExist(shop.getId())) {
                                        shopListDb.insertRecords(shop.getId(), shop.getName(), shop.getDefault_photo().getUrl() + shop.getDefault_photo().getImg_path());
                                    }
                                    shopListDb.insertImage(shop.getId(), resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }
            }
        });

        binding.edtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getLocation();
//                fetchShopBranchesFromOldLocation();

                SessionData.getInstance().setLocationText(binding.edtLocation.getText().toString());
                Intent intent = new Intent(BranchListActivity.this, LocationSearchActivity.class);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
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

        shopUsersAdapter = new ShopUsersAdapter(shopList, shopKey -> {
            SessionData.getInstance().setShopKeyValue(shopKey);

            directShopIn(shopKey, false);
//            Intent intent = new Intent(getActivity(), EditShopForm.class);
//            startActivity(intent);
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.shopBranchList.setLayoutManager(llm);
        binding.shopBranchList.setNestedScrollingEnabled(false);
        binding.shopBranchList.setAdapter(shopUsersAdapter);
        if (SessionData.getInstance().getLatitude() == null) {
            getLocation();
        } else {
            if (SessionData.getInstance().getLatitude().length() == 0) {
                getLocation();
            } else {
                fetchShopBranchesFromSessionLocation();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onItemClick(RecycleItem recycleItem, int position) {
        Config.editPreferences.putString(Constants.SHOP_KEY, recycleItem.getShopId()).apply();
        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(recycleItem.getShopId());
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
        Intent intent = new Intent(BranchListActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
//        Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
//        Config.editPreferences.remove(Constants.CART).apply();
        SessionData.getInstance().setShopKeyValue(shopId);
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
        Intent intent = new Intent(BranchListActivity.this, MainActivity.class);
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
                getLocation();
            } else {
//                Console.toast("Location permission required to proceed!");
                latLngCount = 0;
                binding.lnrSetLocation.setVisibility(View.VISIBLE);
                binding.progressStart.setVisibility(View.GONE);

                binding.lnrSetLocation.setVisibility(View.VISIBLE);
                binding.lnrSetLocationOnSettings.setVisibility(View.VISIBLE);
//                binding.lnrSetLocation.setVisibility(View.VISIBLE);
//                binding.progressStart.setVisibility(View.GONE);
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
//                        }, 2000);
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

        if (SessionData.getInstance().getLatitude() == null||SessionData.getInstance().getLatitude().length()==0) {
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

                                if (gpsTracker.getSubLocality(BranchListActivity.this) != null) {
                                    loc = gpsTracker.getSubLocality(BranchListActivity.this) + ", " + gpsTracker.getLocality(BranchListActivity.this);
                                } else {
                                    loc = gpsTracker.getLocality(BranchListActivity.this);
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

                            if (gpsTracker.getSubLocality(BranchListActivity.this) != null) {
                                loc = gpsTracker.getSubLocality(BranchListActivity.this) + ", " + gpsTracker.getLocality(BranchListActivity.this);
                            } else {
                                loc = gpsTracker.getLocality(BranchListActivity.this);
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
        }else {
            fetchShopBranchesFromSessionLocation();
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

                            if (gpsTracker.getSubLocality(BranchListActivity.this) != null) {
                                loc = gpsTracker.getSubLocality(BranchListActivity.this) + ", " + gpsTracker.getLocality(BranchListActivity.this);
                            } else {
                                loc = gpsTracker.getLocality(BranchListActivity.this);
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

                        if (gpsTracker.getSubLocality(BranchListActivity.this) != null) {
                            loc = gpsTracker.getSubLocality(BranchListActivity.this) + ", " + gpsTracker.getLocality(BranchListActivity.this);
                        } else {
                            loc = gpsTracker.getLocality(BranchListActivity.this);
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


//    public void getLocation() {
//        if (Tools.isOnline()) {
//            binding.progressStart.setVisibility(View.VISIBLE);
//            binding.lnrSetLocation.setVisibility(View.GONE);
//            binding.lnrSetLocationOnSettings.setVisibility(View.GONE);
//            gpsTracker.getLocation();
//            if (gpsTracker.canGetGps()) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                MY_PERMISSIONS_REQUEST_LOCATION);
//                        Console.toast("Location permission required!");
//                        binding.lnrSetLocation.setVisibility(View.GONE);///
//                        binding.progressStart.setVisibility(View.GONE);
//                        return;
//                    } else {
//
//                        if (gpsTracker.getIsGPSTrackingEnabled()) {
////                                    SessionData.getInstance().setMapFrom("EditProfileActivity");
//                            String loc = "";
//
//                            if (gpsTracker.getSubLocality(NewShopListHomeActivity.this) != null) {
//                                loc = gpsTracker.getSubLocality(NewShopListHomeActivity.this) + ", " + gpsTracker.getLocality(NewShopListHomeActivity.this);
//                            } else {
//                                loc = gpsTracker.getLocality(NewShopListHomeActivity.this);
//                            }
//                            SessionData.getInstance().setLat(gpsTracker.getLatitude());
//                            SessionData.getInstance().setLng(gpsTracker.getLongitude());
//                            binding.edtLocation.setText(loc);
//
//                            if (gpsTracker.getLatitude() == 0) {
////                                Console.toast(" Latitude 0");
//                                getLocation();
//                            } else {
//                                fetchShopBranches();
//                            }
//                        } else {
//                            binding.lnrSetLocation.setVisibility(View.GONE);//
//                            binding.progressStart.setVisibility(View.GONE);
////                            gpsTracker.showSettingsAlert();
//                            customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
//
//                        }
//                    }
//                } else {
//
//                    if (gpsTracker.getIsGPSTrackingEnabled()) {
//                        String loc = "";
//
//                        if (gpsTracker.getSubLocality(NewShopListHomeActivity.this) != null) {
//                            loc = gpsTracker.getSubLocality(NewShopListHomeActivity.this) + ", " + gpsTracker.getLocality(NewShopListHomeActivity.this);
//                        } else {
//                            loc = gpsTracker.getLocality(NewShopListHomeActivity.this);
//                        }
//                        SessionData.getInstance().setLat(gpsTracker.getLatitude());
//                        SessionData.getInstance().setLng(gpsTracker.getLongitude());
//                        binding.edtLocation.setText(loc);
//                        if (gpsTracker.getLatitude() == 0) {
////                            Console.toast(" Latitude 0");
//                            getLocation();
//                        } else {
//                            fetchShopBranches();
//                        }
//                    } else {
//                        binding.lnrSetLocation.setVisibility(View.GONE);//
//                        binding.progressStart.setVisibility(View.GONE);
////                        gpsTracker.showSettingsAlert();
//                        customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
//                    }
//                }
//            } else {
//                binding.lnrSetLocation.setVisibility(View.GONE);//
//                binding.progressStart.setVisibility(View.GONE);
////                gpsTracker.showSettingsAlert();
//                customAlertNew("Please enable location on settings", "Location Permission", "Settings", "Cancel");
//            }
//        } else {
//            internetSnack(binding.parentLayout);
//
//        }
//    }

    public void fetchShopBranches() {


        Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
        Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();

//        progressDialog.startLoading();
        binding.progressLocation2.setVisibility(View.VISIBLE);
//        binding.imgQr.setVisibility(View.GONE);
        latLng = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();

        if (latLng.length() < 5) {
//            Console.toastLong("Can not fetch current location");
        }
        shopViewModel.fetchCustomShopBranchList(
                gpsTracker.getLatitude() + "",
                gpsTracker.getLongitude() + "",
                Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")).observeForever(shops -> {
            if (shops != null) {
                shopList = shops;
//                progressDialog.dismiss();
                binding.progressLocation2.setVisibility(View.GONE);
//                binding.imgQr.setVisibility(View.VISIBLE);

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
                        if (Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "").length() > 0) {
                            if (latLng.length() > 5) {
//                                Console.toastLong("Fetching shop branch details for your location");
                            }
                            directShopIn(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""), true);//  here enable
//                            binding.parentLayout.setVisibility(View.VISIBLE);
//                            binding.secondaryLayout.setVisibility(View.GONE);
                        } else {
                            Console.toastLong("can not load shop!");
                        }
                    }

                } else {
                    binding.parentLayout.setVisibility(View.VISIBLE);
                    binding.secondaryLayout.setVisibility(View.GONE);

                    for (int i = 0; i < shopList.size(); i++) {
                        try {
                            shopList.get(i).setDistance(Tools.distanceInKmsSimple(
                                    Double.parseDouble(shopList.get(i).getLat()),
                                    Double.parseDouble(shopList.get(i).getLng()),
                                    SessionData.getInstance().getLat(), SessionData.getInstance().getLng()));
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
                }
                shopUsersAdapter.setItems(shopList);
//                shopUsersAdapter.notifyDataSetChanged();
//                Console.toast(shops.size() + "");
            }
        });
    }

    public void fetchShopBranchesFromOldLocation() {


//        Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
//        Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();

//        progressDialog.startLoading();
        binding.progressLocation2.setVisibility(View.VISIBLE);
//        latLng = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
//        if (latLng.length() < 5) {
//            Console.toastLong("Can not fetch current location");
//        }
        shopViewModel.fetchCustomShopBranchList(
                Config.sharedPreferences.getString(Constants.LAT, "") + "",
                Config.sharedPreferences.getString(Constants.LNG, "") + "",
                Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")
        ).observeForever(shops -> {
            if (shops != null) {

                shopList.addAll(shops);

//                progressDialog.dismiss();
                binding.progressLocation2.setVisibility(View.GONE);

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
                        if (Config.sharedPreferences.getString(Constants.LAT, "").length() > 5) {
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

                //TODO: Handle filters
                //TODO: filter for  restaurant
//                shopUsersAdapter.notifyDataSetChanged();
//                Console.toast(shops.size() + "");
            }
        });
    }

    public void fetchShopBranchesFromSessionLocation() {

        binding.progressStart.setVisibility(View.VISIBLE);
        binding.lnrSetLocation.setVisibility(View.GONE);
        binding.lnrSetLocationOnSettings.setVisibility(View.GONE);
//        Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
//        Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();

//        progressDialog.startLoading();
        binding.progressLocation2.setVisibility(View.VISIBLE);
//        binding.imgQr.setVisibility(View.GONE);

        shopViewModel.fetchCustomShopBranchList(
                SessionData.getInstance().getLatitude() + "",
                SessionData.getInstance().getLongitude() + "",
                Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "")).observeForever(shops -> {
            if (shops != null) {
                shopList = shops;
//                progressDialog.dismiss();
                binding.progressLocation2.setVisibility(View.GONE);
//                binding.imgQr.setVisibility(View.VISIBLE);

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
                        if (Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, "").length() > 0) {
                            if (SessionData.getInstance().getLatitude().length() > 5) {
//                                Console.toastLong("Fetching shop branch details for your location");
                            }
                            directShopIn(Config.sharedPreferences.getString(Constants.SHOP_MAIN_BRANCH_KEY, ""), true); //here enable
//                            binding.parentLayout.setVisibility(View.VISIBLE);
//                            binding.secondaryLayout.setVisibility(View.GONE);
                        } else {
                            Console.toastLong("can not load shop!");
                        }
                    }

                } else {
                    binding.parentLayout.setVisibility(View.VISIBLE);
                    binding.secondaryLayout.setVisibility(View.GONE);

                    for (int i = 0; i < shopList.size(); i++) {
                        try {
                            shopList.get(i).setDistance(Tools.distanceInKmsSimple(
                                    Double.parseDouble(shopList.get(i).getLat()),
                                    Double.parseDouble(shopList.get(i).getLng()),
                                    SessionData.getInstance().getLat(), SessionData.getInstance().getLng()));
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
                }
                shopUsersAdapter.setItems(shopList);
//                shopUsersAdapter.notifyDataSetChanged();
//                Console.toast(shops.size() + "");
            }
        });
    }

    public void customAlertNew(String message, String title, String strOk, String strCancel) { // nORMAL SCAN

        dialog = new Dialog(BranchListActivity.this);

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

}
