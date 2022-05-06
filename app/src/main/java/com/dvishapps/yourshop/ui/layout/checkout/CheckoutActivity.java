package com.dvishapps.yourshop.ui.layout.checkout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.utils.Tools;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.ACheckoutBinding;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.adapters.CheckoutParentAdapter;
import com.dvishapps.yourshop.ui.common.SCompatActivity;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.viewModel.CheckoutViewModel;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;

public class CheckoutActivity extends SCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 89;
    private ACheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private UserViewModel userViewModel;
    private ShopViewModel shopViewModel;
    private CheckoutParentAdapter checkoutParentAdapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.a_checkout);
        toolbar = findViewById(R.id.header);

        binding.setOverAllTax(SessionData.getInstance().getOverAllTaxValue());
        if (SessionData.getInstance().getOverAllTaxValue() == 0) {
            binding.taxLabel.setVisibility(View.GONE);
            binding.taxValue.setVisibility(View.GONE);
        }

        setCustomToolbar(toolbar, StringUtil.setString(getString(R.string.my_cart), Config.cart.getTotal_qty()), getString(R.string.basket__list));
        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        checkoutViewModel.fetchBasketProducts();
        binding.setLifecycleOwner(this);
        binding.setCart(Config.cart);
        binding.setDeliveryCharge(Config.cart.getDelivery_charge() + "");
        if (Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {
            binding.setFinalPrice(Tools.round(Double.parseDouble(Config.cart.getFinalPrice()) - Config.cart.getDelivery_charge() - Config.cart.getPackageCharge()) + "");
            binding.txtDeliveryCharge.setVisibility(View.GONE);
            binding.txtDeliveryChargeFree.setVisibility(View.GONE);
            binding.labelDeliveryCharges.setVisibility(View.GONE);
            binding.txtDeliveryChargeIncludedText.setVisibility(View.VISIBLE);
        } else {
            //Free
            binding.setFinalPrice(Tools.round(Double.parseDouble(Config.cart.getFinalPrice()) - Config.cart.getDelivery_charge() - Config.cart.getPackageCharge()) + "");
            binding.txtDeliveryCharge.setVisibility(View.GONE);
            binding.txtDeliveryChargeFree.setVisibility(View.VISIBLE);
            binding.txtDeliveryChargeIncludedText.setVisibility(View.GONE);
        }

        binding.setTotalTax(Config.cart.getTotalTax());
        if (Config.cart.getProductsAsList().size() > 0)
            binding.setCurrencySymbol(Config.cart.getProductsAsList().get(0).getCurrency_symbol());

        bottomSheetBehavior = BottomSheetBehavior.from(binding.moreDetail);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.checkoutBtn.setOnClickListener(view -> {
//            checkoutClick();
            setLatLng();
        });

        binding.checkoutTotalPrice.setOnClickListener(view -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                binding.checkoutTotalPrice.setSelected(false);
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                binding.btm.setBackgroundResource(R.color.md_grey_50);
            } else {
                binding.btm.setBackgroundResource(R.color.green_transparent_2);
                binding.checkoutTotalPrice.setSelected(true);
                bottomSheetBehavior.setPeekHeight(600);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu:
                navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        RecyclerView checkout_super_list = binding.checkoutSuperList;
        checkoutParentAdapter = new CheckoutParentAdapter(checkoutViewModel.products, (action, prod, parentPosition, childPosition) -> {
            try {
                Config.cart.updateCart(action, prod, 1);
                binding.setCart(Config.cart);
                try {
                    binding.setCurrencySymbol(Config.cart.getProductsAsList().get(0).getCurrency_symbol());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.setDeliveryCharge(Config.cart.getDelivery_charge() + "");
                binding.setFinalPrice(Tools.round(Double.parseDouble(Config.cart.getFinalPrice()) - Config.cart.getDelivery_charge() - Config.cart.getPackageCharge()) + "");
                binding.setTotalTax(Config.cart.getTotalTax());
                setCustomToolbar(toolbar, StringUtil.setString(getString(R.string.my_cart), Config.cart.getTotal_qty()), getString(R.string.basket__list));
                checkoutParentAdapter.notifyChangedItems(parentPosition, childPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        checkout_super_list.setLayoutManager(getLinearLayout());
        checkout_super_list.setAdapter(checkoutParentAdapter);
//        setLatLng();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.REQUEST_CODE) {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    public void setLatLng() {
        if (Config.sharedPreferences.getString(Constants.LAT, "0").equals("0")) {
            GPSTrackerService gpsTracker = new GPSTrackerService(CheckoutActivity.this);

            gpsTracker.getLocation();
            if (gpsTracker.canGetGps()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CheckoutActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        Console.toast("You need to allow location access permission to proceed");
                        return;
                    } else {

                        if (gpsTracker.getIsGPSTrackingEnabled()) {
                            if (gpsTracker.getLatitude() > 0) {
                                Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
                                Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
                                Console.logDebug("got lat lng");
                                resetShopDetails();
                            } else {
                                Console.logDebug("lat lng not available");
                            }
                        } else {
                            gpsTracker.showSettingsAlert();
                        }
                    }
                } else {

                    if (gpsTracker.getIsGPSTrackingEnabled()) {
                        if (gpsTracker.getLatitude() > 0) {
                            Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
                            Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
                            Console.logDebug("got lat lng");
                            resetShopDetails();
                        } else {
                            Console.logDebug("lat lng not available");
                        }
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
        } else {
            resetShopDetails();
        }
    }


    public void resetShopDetails() {
        if (Config.sharedPreferences.getString(Constants.DELIVERY_CHARGE_CALCULATE_AFTER_LOGIN, "FALSE").equals("TRUE")) {
            checkoutClick();
        } else if (Config.sharedPreferences.getString(Constants.DELIVERY_CHARGE_CALCULATED, "FALSE").equals("TRUE")) {
            checkoutClick();
        } else {
            shopViewModel.fetchShop();
            shopViewModel.getShopData().observeForever(shop -> {
                if (shop != null) {
                    checkoutClick();
                }
            });
        }

    }

    public void checkoutClick() {
        if (checkoutViewModel.products != null && checkoutViewModel.products.size() > 0) {
            userViewModel.getLoginUser().observe(this, user -> {
                if (user != null) {
                    SessionData.getInstance().setCurrentUser(user);
                    Intent intent = new Intent(this, CheckoutForm.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                } else {
                    SessionData.getInstance().setAuthFrom("CheckoutActivity");

                    Intent login_intent = new Intent(this, AuthActivity.class);
                    startActivity(login_intent);
                }
            });
        }
    }
}
