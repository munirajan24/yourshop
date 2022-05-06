package com.dvishapps.yourshop.ui.layout.order;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FOrdersABinding;
import com.dvishapps.yourshop.models.Order;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.adapters.TransactionAdapter;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.viewModel.OrderViewModel;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.POWER_SERVICE;
import static com.dvishapps.yourshop.app.Config.CALL_BACKGROUND_SETTINGS;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveredOrdersFragment extends Fragment {

    private FOrdersABinding binding;
    private OrderViewModel orderViewModeNnew;
    private Order order;
    private UserViewModel userViewModel;
    private TransactionViewModel orderViewModel;
    private TransactionAdapter orderAdapter;
    private int ordersCount = 0;
    boolean sec = false;
    private List<Transaction> transactionListFiltered = new ArrayList<>();
    private List<Transaction> transactionListSecondaryFiltered = new ArrayList<>();

    private ShimmerFrameLayout mShimmerViewContainer;
    private boolean order2ndTimeCheck = false;

    public DeliveredOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModeNnew = new ViewModelProvider(this).get(OrderViewModel.class);

        userViewModel = new UserViewModel(this.getActivity());
        orderViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.f_orders_a, container, false);
        binding.setViewModel(orderViewModel);
        binding.setIsLoading(true);

        mShimmerViewContainer = binding.shimmerViewContainer;
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        userViewModel.getLoginUser().observe(this.getViewLifecycleOwner(), logged_user -> {
            if (logged_user == null) {
                SessionData.getInstance().setAuthFrom("OrdersAFragment");
//                SessionData.getInstance().setPendingOrdersFragment(PendingOrdersFragment.this);
//                SessionData.getInstance().getBottomNavigation().show(R.id.nav_category,false);
//                onBackPressed();
                Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
                startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
            } else {
                setUpUserOrders();
            }
        });

        orderViewModeNnew.getOrderData().observe(this.getViewLifecycleOwner(), order -> {
            if (order != null) {
                binding.setOrders(order);
                this.order = order;
            }
        });
        return binding.getRoot();
    }

    public void init() {
        RecyclerView ordersList = binding.ordersList;
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        ordersList.setLayoutManager(manager);
        orderAdapter = new TransactionAdapter(orderViewModel.transactions_data.getValue(), tran_id -> {
            Bundle bundle = new Bundle();
            bundle.putString(orderViewModel.TRAN_ID, tran_id);
            bundle.putString(orderViewModel.USER_ID, orderViewModel.user.getUser_id());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_order_to_transactionDetailsFragment, bundle);
        });
        ordersList.setAdapter(orderAdapter);

        binding.radioCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCodOrders();
            }
        });

        binding.radioOnlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnlinePaymentOrders();
            }
        });

//        binding.rlFilterHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeFilterLayout();
//            }
//        });
    }

    public void setUpUserOrders() {
        binding.setIsLoading(true);
        init();
        sec = false;

        if (Config.currentUser != null) {
            userViewModel.getUserInfo(Config.currentUser.getUser_id()).observe(this.getViewLifecycleOwner(), user -> {
                if (user != null && user.size() > 0) {
                    if (user != null && user.size() > 0) {
                        binding.setIsLoading(false);
                        if (user.get(0).getStatus().contains("404 error")) {
                            if (user.get(0).getStatus().contains("UnknownHostException")) {
                                internetSnack(binding.parentLayout);
                            }
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        } else {

                            if (user.size() > 0) {
                                orderViewModel.fetchOrders(user.get(0).getUser_id());
                                orderViewModel.user = user.get(0);
                                orderViewModel.getTransactions_data().observe(this.getViewLifecycleOwner(), transactions -> {

                                    if (transactions != null) {
                                        if (transactions.size() > 0) {

                                            if (user.get(0).getStatus().contains("404 error")) {
                                                if (user.get(0).getStatus().contains("UnknownHostException")) {
                                                    internetSnack(binding.parentLayout);
                                                }
                                                transactions.clear();
                                            }
                                            transactionListFiltered.clear();
                                            for (int i = 0; i < transactions.size(); i++) {
                                                if (transactions.get(i).getTrans_status_title().equalsIgnoreCase("Completed")) {
                                                    transactionListFiltered.add(transactions.get(i));
                                                }
                                            }
                                        }
                                        mShimmerViewContainer.stopShimmer();
                                        mShimmerViewContainer.setVisibility(View.GONE);

//                                        orderAdapter.setItems(transactionListFiltered);
                                        setCodOrders();
                                        binding.radioCod.setChecked(true);
                                        binding.radioCod.setText("CASH ON DELIVERY ("+transactionListSecondaryFiltered.size()+")");

                                        List<Transaction> filteredList = new ArrayList<>();
                                        for (Transaction item : transactionListFiltered) {
                                            if (item.getRazor_payment_status().length()>0) {
                                                filteredList.add(item);
                                            }
                                        }
                                        binding.radioOnlinePayment.setText("ONLINE PAYMENT ("+filteredList.size()+")");

                                        binding.setIsLoading(false);

                                        if (transactionListFiltered.size() == 0) {
                                            binding.lnrSubParent.setVisibility(View.GONE);
                                            binding.ordersList.setVisibility(View.GONE);
                                            binding.noOrders.setVisibility(View.VISIBLE);
                                            binding.noOrders.setText("There are no delivered orders");
                                        } else {
                                            binding.ordersList.setVisibility(View.VISIBLE);
                                            binding.lnrSubParent.setVisibility(View.VISIBLE);
                                            binding.noOrders.setVisibility(View.GONE);
                                        }
                                    }

                                });

                                binding.setUser(user.get(0));
                                binding.setUserModel(userViewModel);
                            }
                        }
                    }
                }
            });
        }
    }

    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
//                            setUpUserOrders();
                            retryFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void retryFunctions() {
        userViewModel.getLoginUser().observe(this.getViewLifecycleOwner(), logged_user -> {
            if (logged_user == null) {
                SessionData.getInstance().setAuthFrom("OrdersAFragment");
//                SessionData.getInstance().setPendingOrdersFragment(OnProcessOrdersFragment.this);
//                SessionData.getInstance().getBottomNavigation().show(R.id.nav_category,false);
//                onBackPressed();
                Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
                startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
            } else {
                setUpUserOrders();
            }
        });
    }



    public void setCodOrders() {
        List<Transaction> filteredList = new ArrayList<>();
        for (Transaction item : transactionListFiltered) {
            if (item.getRazor_payment_status().length()==0) {
                filteredList.add(item);
            }
        }
        transactionListSecondaryFiltered.clear();
        transactionListSecondaryFiltered.addAll(filteredList);
        orderAdapter.setItems(transactionListSecondaryFiltered);
        checkNoOrders();
//        hideFilterLayout();
    }


    public void setOnlinePaymentOrders() {
        List<Transaction> filteredList = new ArrayList<>();
        for (Transaction item : transactionListFiltered) {
            if (item.getRazor_payment_status().length()>0) {
                filteredList.add(item);
            }
        }
        transactionListSecondaryFiltered.clear();
        transactionListSecondaryFiltered.addAll(filteredList);
        orderAdapter.setItems(transactionListSecondaryFiltered);
        checkNoOrders();
//        hideFilterLayout();
    }


    private void checkNoOrders() {
        if (transactionListSecondaryFiltered.size() == 0) {
            binding.noOrders.setVisibility(View.VISIBLE);
            binding.noOrders.setText("There are no orders in filtered results");
        } else {
            binding.noOrders.setVisibility(View.GONE);
        }
    }

    public void hideFilterLayout() {
        binding.icRight.setVisibility(View.VISIBLE);
        binding.icDown.setVisibility(View.GONE);
        binding.lnrMoreFilters.setVisibility(View.GONE);
    }
    public void changeFilterLayout() {
        if (binding.lnrMoreFilters.getVisibility() == View.VISIBLE) {
            binding.icRight.setVisibility(View.VISIBLE);
            binding.icDown.setVisibility(View.GONE);
            binding.lnrMoreFilters.setVisibility(View.GONE);

        } else {
            binding.icRight.setVisibility(View.GONE);
            binding.icDown.setVisibility(View.VISIBLE);
            binding.lnrMoreFilters.setVisibility(View.VISIBLE);
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
