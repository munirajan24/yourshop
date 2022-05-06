package com.dvishapps.yourshop.ui.layout.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.layout.home.HomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FProfileBinding;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.adapters.TransactionAdapter;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.POWER_SERVICE;
import static com.dvishapps.yourshop.app.Config.CALL_BACKGROUND_SETTINGS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends SFragment {

    private UserViewModel userViewModel;
    private TransactionViewModel orderViewModel;
    private FProfileBinding binding;
    private TransactionAdapter orderAdapter;

    private User currentUser;
    private int ordersCount = 0;
    private boolean sec = false;
    private List<Transaction> transactionListFiltered;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new UserViewModel(this.getActivity());
        orderViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        if (getArguments() != null) {
            currentUser = (User) getArguments().getSerializable(Constants.LOGGED_USER);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_profile, container, false);
        binding.setViewModel(orderViewModel);
        binding.setIsLoading(true);

        webCallFunctions();
//        userViewModel.getLoginUser().observe(this.getViewLifecycleOwner(), logged_user -> {
//            if (logged_user == null) {
//                SessionData.getInstance().setAuthFrom("ProfileFragment");
//                SessionData.getInstance().setProfileFragment(ProfileFragment.this);
//                Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
//                startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
//            } else {
//                setupUserDetails(logged_user);
//                Console.logError("profile fragment getLoginUser()");
//            }
//        });

        return binding.getRoot();
    }

    public void init() {
        SessionData.getInstance().setProfileFragment(ProfileFragment.this);
        changeShopVisibility();
        changeShopSettingVisibility();

        transactionListFiltered = new ArrayList<>();
        RecyclerView ordersList = binding.ordersList;
        ordersList.setLayoutManager(getLinearLayout());
        orderAdapter = new TransactionAdapter(orderViewModel.transactions_data.getValue(), tran_id -> {
            Bundle bundle = new Bundle();
            bundle.putString(orderViewModel.TRAN_ID, tran_id);
            bundle.putString(orderViewModel.USER_ID, orderViewModel.user.getUser_id());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_profile_to_transactionDetailsFragment, bundle);
        });
        ordersList.setAdapter(orderAdapter);

        binding.editProfile.setOnClickListener(v -> {
            if (Tools.isOnline()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.LOGGED_USER, orderViewModel.user);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_profile_to_editProfileFragment, bundle);
            }else {
                Console.toast("No internet connection");
            }
        });
        binding.btnChangePassword.setOnClickListener(v -> {
            if (Tools.isOnline()) {

                Bundle data = new Bundle();
                if (currentUser != null) {
                    data.putString(Constants.USER_ID, currentUser.getUser_id());
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_profile_to_changePwdFragment, data);
                }
            }else {
                Console.toast("No internet connection");
            }
        });

        binding.btnShopSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.isOnline()) {

                    Bundle data = new Bundle();
                    if (currentUser != null) {
                        data.putString(Constants.USER_ID, currentUser.getUser_id());
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_profile_to_shopSettingsFragment, data);
                    }
                }else {
                    Console.toast("No internet connection");
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.signOut.setOnClickListener(v -> {
            if (userViewModel != null) {
                userViewModel.progressDialog.startLoading();
                userViewModel.signOut().observe(this.getViewLifecycleOwner(), aBoolean -> {
                    userViewModel.progressDialog.dismiss();
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_home);
//                    if (aBoolean){
//                        Console.toast("Logged out");
//                    }
                });
            }
        });
        binding.exitShop.setOnClickListener(v -> {
            userViewModel.progressDialog.startLoading();
            userViewModel.removeShop().observe(this.getViewLifecycleOwner(), aBoolean -> {
                userViewModel.progressDialog.dismiss();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        });
    }

    public void setupUserDetails(User logged_user) {
        currentUser = (User) logged_user;
        init();

        if (Config.currentUser != null) {
            Console.logError("shop_log Config.currentUser id " + Config.currentUser.getUser_id());
            Console.logError("shop_log Config.logged_user id " + logged_user.getUser_id());
            userViewModel.getUserInfo(Config.currentUser.getUser_id()).observe(this.getViewLifecycleOwner(), user -> {
                if (user != null && user.size() > 0) {
                    if (user.get(0).getStatus().contains("404 error")) {
                        if (user.get(0).getStatus().contains("UnknownHostException")) {
                            internetSnack(binding.parentLayout);
                        }
                        binding.setIsLoading(false);

//                            nouserSetUp();
                    } else {
//                            setuserSetUp();
                        currentUser = (User) user.get(0);

                        orderViewModel.fetchOrders(user.get(0).getUser_id());
                        orderViewModel.user = user.get(0);
                        orderViewModel.getTransactions_data().observe(this.getViewLifecycleOwner(), transactions -> {
                            if (transactions != null) {
                                binding.setIsLoading(false);
                                if (transactions.size() > 0) {
                                    transactionListFiltered.clear();
                                    for (int i = 0; i < transactions.size(); i++) {
                                        if (transactions.get(i).getTrans_status_title().equalsIgnoreCase("Pending")) {
                                            transactionListFiltered.add(transactions.get(i));
                                        }
                                    }
                                }
                            }

                            ordersCount = transactionListFiltered.size();
                            SessionData.getInstance().setOrdersCount(ordersCount);
//                            if (ordersCount > 0) {
//                                SessionData.getInstance().getBottomNavigation().setCount(R.id.nav_order, ordersCount + "");
//                            } else {
//                                if (sec) {
//                                    SessionData.getInstance().getBottomNavigation().clearCount(R.id.nav_order);
//                                }
//                                sec = true;
//
//                            }
                        });

                        binding.setUser(user.get(0));
                        binding.setUserModel(userViewModel);
                    }

                }
            });
        } else {
            Console.logError("shop_log Config.currentUser null");
            Console.logError("shop_log Config.logged_user " + logged_user);

        }
    }

    public void changeShopVisibility() {
        if (Config.sharedPreferences.getString(Constants.INDIVIDUAL_SHOP, null) != null) {
            if (Objects.equals(Config.sharedPreferences.getString(Constants.INDIVIDUAL_SHOP, null), "1")) {
                binding.lnrExitShop.setVisibility(View.GONE);
            } else {
                binding.lnrExitShop.setVisibility(View.VISIBLE);
            }
        } else {
            binding.lnrExitShop.setVisibility(View.VISIBLE);
        }

    }

    public void changeShopSettingVisibility() {
        if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null) {
            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1")) {
                binding.lnrShopSettings.setVisibility(View.VISIBLE);
            } else {
                binding.lnrShopSettings.setVisibility(View.GONE);
            }
        } else {
            binding.lnrShopSettings.setVisibility(View.GONE);
        }
//        binding.lnrShopSettings.setVisibility(View.VISIBLE);

    }

    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
                            webCallFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void webCallFunctions() {
        try {
            userViewModel.getLoginUser().observe(this.getViewLifecycleOwner(), logged_user -> {
                if (logged_user == null) {
                    SessionData.getInstance().setAuthFrom("ProfileFragment");
                    SessionData.getInstance().setProfileFragment(ProfileFragment.this);
                    Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
                    startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
                } else {
                    setupUserDetails(logged_user);
                    Console.logError("profile fragment getLoginUser()");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_BACKGROUND_SETTINGS) {
            callSettingsCheck();
        }
    }

    private void callSettingsCheck() {
//        Console.toast("Call setting Triggered : ProfileFragment");
//        Console.logDebug("Call setting Triggered : ProfileFragment");
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
            WorkManager.getInstance(getActivity()).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
            if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(getActivity()))
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(getActivity());
            power_management();
        }
    }

    private void power_management() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getActivity().getPackageName();
            Intent in = new Intent();
            in.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            in.setData(Uri.parse("package:" + packageName));
            PowerManager pm = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
