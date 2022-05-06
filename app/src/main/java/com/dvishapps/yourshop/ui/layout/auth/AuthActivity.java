package com.dvishapps.yourshop.ui.layout.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.common.SCompatActivity;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthActivity extends SCompatActivity implements AuthListener {
    private NavController navController;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_auth);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        navController = Navigation.findNavController(this, R.id.auth_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public void onSignInClick() {
        if (navController.getCurrentDestination().getId() == R.id.auth_signup)
            navController.navigate(R.id.action_auth_signup_to_auth_signin, null, new NavOptions.Builder().setPopUpTo(R.id.auth_signin, true).build());
        else
            navController.navigate(R.id.action_forgotPwdFragment_to_auth_signin, null, new NavOptions.Builder().setPopUpTo(R.id.auth_signin, true).build());
    }

    @Override
    public void onForgotPwd() {
        navController.navigate(R.id.action_auth_signin_to_forgotPwdFragment);
    }

    @Override
    public void onSignUpClick() {
        navController.navigate(R.id.action_auth_signin_to_auth_signup);
    }

    @Override
    public void onSuccess(User user) {
        Console.logError("AuthActivity OnSuccess User " + user);
        if (user != null) {
            if (SessionData.getInstance().isInitialSignUp()) {
                SessionData.getInstance().setInitialSignUp(false);
                Intent auth_intent = new Intent(AuthActivity.this, MainActivity.class);
//                auth_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(auth_intent);

                if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
                    //TODO: 2nd Implementation for Call Settings
                    PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
                    WorkManager.getInstance(AuthActivity.this).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
                    if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
                        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
                    power_management();
                }else {
                    //for test
                    PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
                    WorkManager.getInstance(AuthActivity.this).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
                    if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
                        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
                    power_management();
                }
            }else {
                if (SessionData.getInstance().getAuthFrom().equals("ProfileFragment")) {
                    SessionData.getInstance().getProfileFragment().setupUserDetails(user);
                } else if (SessionData.getInstance().getAuthFrom().equals("OrdersAFragment")) {
                    SessionData.getInstance().getPendingOrdersFragment().setUpUserOrders();
                }

                if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
                    //TODO: 2nd Implementation for Call Settings
                    PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
                    WorkManager.getInstance(AuthActivity.this).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
                    if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
                        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
                    power_management();
                }else {
                    //for test
                    PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
                    WorkManager.getInstance(AuthActivity.this).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
                    if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
                        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
                    power_management();
                }
            }
            this.finish();
        }

    }

    @Override
    public void onVerify(User user) {
        Bundle data = new Bundle();
        data.putSerializable(Constants.USER_NEED_VERIFY, user);
        navController.navigate(R.id.action_auth_signup_to_verifyUserFragment, data);
    }

    @Override
    public void onChangeEmail(User user) {
        Bundle data = new Bundle();
        data.putSerializable(Constants.USER_NEED_VERIFY, user);
        navController.navigate(R.id.action_auth_verify_to_auth_signup, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
