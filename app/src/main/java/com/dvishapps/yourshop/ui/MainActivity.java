package com.dvishapps.yourshop.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.utils.Console;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.ui.interfaces.OnToolbarChange;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutActivity;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.dvishapps.yourshop.app.Config.CALL_BACKGROUND_SETTINGS;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements OnToolbarChange {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private NavController navController;
    private MeowBottomNavigation bottomNavigation;

    private Intent checkout_intent;
    private int oldPage;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;

    private int orderCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*        String  value = getIntent().getStringExtra("shop_id");
        Config.editPreferences.putString(Constants.SHOP_KEY, value).apply();*/

//        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != null) {
//            SessionData.getInstance().setShopKeyValue(Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
//        } else Console.toast(" Fetching Failed");
//        checkShopUrlChange();

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
        bottomNavigation = findViewById(R.id.bottom);
        SessionData.getInstance().setBottomNavigation(bottomNavigation);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_category, R.id.nav_home, R.id.nav_profile, R.id.nav_shop, R.id.nav_order)
//                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        Config.cart.changeListener.observeForever(cart -> {
            invalidateOptionsMenu();
        });


        initBottomNavigation();
//        setCurrentAddress();

//        if (checkCameraPermission()) {
//
//        } else {
//            requestPermission();
//        }
    }


    private void initBottomNavigation() {
//        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_home, R.drawable.baseline_home_grey_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_home, R.drawable.ic_home_new));
        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_order, R.drawable.ic_list_black_40dp));
//        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_category, R.drawable.badge));
        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_category, R.drawable.ic_basket));
        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_shop, R.drawable.ic_shop));
//        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_profile, R.drawable.baseline_profile_grey_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(R.id.nav_profile, R.drawable.ic_user_new));
        initNavigationController();
    }

    private void initNavigationController() {

        bottomNavigation.setOnClickMenuListener(item -> {
            switch (item.getId()) {
                case R.id.nav_category:
                    if (checkout_intent == null)
                        checkout_intent = new Intent(this, CheckoutActivity.class);
                    startActivity(checkout_intent);
                    break;
                case R.id.nav_profile:
                case R.id.nav_home:
                case R.id.nav_order:
                case R.id.nav_shop:
                    oldPage = item.getId();
                    navController.navigate(item.getId());
                    break;
            }
        });

        bottomNavigation.setOnShowListener(item -> {

        });

        bottomNavigation.setOnReselectListener(item -> {
            switch (item.getId()) {
                case R.id.nav_category:
                    if (checkout_intent == null)
                        checkout_intent = new Intent(this, CheckoutActivity.class);
                    startActivity(checkout_intent);
                    break;
                case R.id.nav_profile:
                case R.id.nav_home:
                case R.id.nav_order:
                case R.id.nav_shop:
                    navController.navigate(item.getId());
                    break;
            }
        });
        bottomNavigation.setCount(R.id.nav_category, String.valueOf(Config.cart.getTotal_qty()));
        bottomNavigation.show(R.id.nav_home, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        menu.findItem(R.id.search_menu).setVisible(false);
        if (Config.cart.getTotal_qty() <= 0)
            menu.findItem(R.id.action_cart).setVisible(false);
        else {
            menu.findItem(R.id.action_cart).setVisible(true);
            Tools.setBadgeCount(this, menu.findItem(R.id.action_cart), String.valueOf(Config.cart.getTotal_qty()));
        }
        if (Config.cart.getTotal_qty() <= 0) {
            bottomNavigation.clearCount(R.id.nav_category);
        } else {
            bottomNavigation.setCount(R.id.nav_category, String.valueOf(Config.cart.getTotal_qty()));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent intent = new Intent(this, CheckoutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        bottomNavigation.show(R.id.nav_home, false);
//        if (oldPage != 0) {
//            bottomNavigation.show(oldPage, false);
//            navController.navigate(oldPage);
//        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void changeTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    public void setCurrentAddress() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                return;
            } else {
                try {
                    GPSTrackerService gpsTracker = new GPSTrackerService(MainActivity.this);
                    if (!gpsTracker.getIsGPSTrackingEnabled()) {
                        //                gpsTracker.showSettingsAlert();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                GPSTrackerService gpsTracker = new GPSTrackerService(MainActivity.this);
                if (!gpsTracker.getIsGPSTrackingEnabled()) {
                    //                gpsTracker.showSettingsAlert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void checkShopUrlChange() {
        if (Config.sharedPreferences.getString(Constants.SHOP, null) == null) {
            Config.editPreferences.clear().apply();
            Config.editPreferences.putString(Constants.SHOP, Constants.HTTP).apply();
        } else if (Config.sharedPreferences.getString(Constants.SHOP, null) != (Constants.HTTP)) {
            Config.editPreferences.clear().apply();
            Config.editPreferences.putString(Constants.SHOP, Constants.HTTP).apply();
        }
    }

    public void checkShopKeyChange() {

        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) == null) {
            Config.editPreferences.clear().apply();
            Config.editPreferences.putString(Constants.SHOP_KEY, Constants.HTTP).apply();
        } else if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != (Constants.HTTP)) {
            Config.editPreferences.clear().apply();
            Config.editPreferences.putString(Constants.SHOP_KEY, Constants.HTTP).apply();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_BACKGROUND_SETTINGS) {
            callSettingsCheck();
        }
    }

    private void callSettingsCheck() {
        Console.toast("Call setting Triggered");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callSettingsShow();
            }
        }, 1000);
    }

    private void callSettingsShow() {
        if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
            //TODO: 2nd Implementation for Call Settings
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
            WorkManager.getInstance(MainActivity.this).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
            if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
            power_management();
        }
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
