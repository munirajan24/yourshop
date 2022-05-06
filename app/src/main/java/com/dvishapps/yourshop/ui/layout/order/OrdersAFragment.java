package com.dvishapps.yourshop.ui.layout.order;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FOrdersABinding;
import com.dvishapps.yourshop.models.Order;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.adapters.TransactionAdapter;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.viewModel.OrderViewModel;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
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
public class OrdersAFragment extends Fragment {
    private FOrdersABinding binding;
    private OrderViewModel orderViewModeNnew;
    private Order order;
    private UserViewModel userViewModel;
    private TransactionViewModel transactionViewModel;
    private TransactionAdapter orderAdapter;
    private int ordersCount = 0;
    boolean sec = false;

    private ShimmerFrameLayout mShimmerViewContainer;
    private List<Transaction> transactionListFiltered = new ArrayList<>();
    private List<Transaction> transactionListSecondaryFiltered = new ArrayList<>();
    private String phone = "";

    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressDialog progressDialog;

    private boolean shopStatusOpen = true;

    boolean cancelClickable = true;

    public OrdersAFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModeNnew = new ViewModelProvider(this).get(OrderViewModel.class);
        progressDialog = new ProgressDialog(getActivity(), "Canceling order...");

        userViewModel = new UserViewModel(this.getActivity());
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.f_orders_a, container, false);
        binding.setViewModel(transactionViewModel);
        binding.setIsLoading(true);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));

        if (SessionData.getInstance().getShop() != null) {
            if (SessionData.getInstance().getShop().getStatus().contains("404 errorNo more records")) {
                binding.lnrSubParent.setVisibility(View.GONE);
                binding.lnrPb.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                shopStatusOpen = false;
            }
        }

        mShimmerViewContainer = binding.shimmerViewContainer;
        mSwipeRefreshLayout = binding.swipelayout;
        if (shopStatusOpen) {
            userViewModel.getLoginUser().observe(this.getViewLifecycleOwner(), logged_user -> {
                if (logged_user == null) {
                    SessionData.getInstance().setAuthFrom("OrdersAFragment");
                    //                SessionData.getInstance().setOrdersSFragment(OrdersAFragment.this);
                    //                SessionData.getInstance().getBottomNavigation().show(R.id.nav_category,false);
                    //                onBackPressed();
                    Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
                    startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
                } else {
                    setUpUserOrders();
                }
            });
        }

        orderViewModeNnew.getOrderData().observe(this.getViewLifecycleOwner(), order -> {
            if (order != null) {
                binding.setOrders(order);
                this.order = order;
            }
        });


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
        setDeliveryStafDetails();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpUserOrders();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }

    public void init() {
        RecyclerView ordersList = binding.ordersList;
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        ordersList.setLayoutManager(manager);
        orderAdapter = new TransactionAdapter(transactionViewModel.transactions_data.getValue(), tran_id -> {
            Bundle bundle = new Bundle();
            bundle.putString(transactionViewModel.TRAN_ID, tran_id);
            bundle.putString(transactionViewModel.USER_ID, transactionViewModel.user.getUser_id());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_order_to_transactionDetailsFragment, bundle);
        }, tran_id -> {
            if (cancelClickable) {
                cancelClickable = false;
                cancelOrder(tran_id);
            }
        });
        ordersList.setAdapter(orderAdapter);
        transactionListFiltered.clear();
        orderAdapter.setItems(transactionListFiltered);
//        binding.editProfile.setOnClickListener(v -> {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Constants.LOGGED_USER, orderViewModel.user);
//            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_profile_to_editProfileFragment, bundle);
//        });

    }

    public void customAlertNewCancelOrder(Context mContext, String message, String title, String strOk, String strCancel, String tran_id) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

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

                updateStatus("7", tran_id);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickable = true;

                dialog.dismiss();

            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickable = true;

                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private void cancelOrder(String tran_id) {
        customAlertNewCancelOrder(getActivity(), "Are you sure to cancel your order?", "Cancel Order", "OK", "Close", tran_id);
    }

    public void updateStatus(String status_id, String tran_id) {
        progressDialog.startLoading();

        transactionViewModel.updateTransactionStatus(tran_id, status_id)
                .observe(this.getViewLifecycleOwner(), apiResponse -> {
                    if (apiResponse != null) {
                        cancelClickable = true;
                        progressDialog.dismiss();
                        if (!Tools.isOnline()) {
                            //                    if (apiResponse.getStatus().contains("404 error")){
                            //                        internetSnack(binding.parentLayout);
                            Console.toast("No internet Connection");
                        } else {
                            //                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                            if (apiResponse != null) {
                                if (apiResponse.getMessage().toLowerCase().contains("success")) {
                                    Console.toast("Order cancelled");
                                    setUpUserOrders();
                                } else {
                                    Console.toast(apiResponse.getMessage());
                                }

                                //      TODO : move to selected fragment
//                                Bundle bundle = new Bundle();
//                                bundle.putString("ORDER_TAB", status_id);
//                                SessionData.getInstance().setOrderTabSelection(status_id);
//                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_transaction_detail_to_OrdersSFragment, bundle);

                            } else {
//                                dialog.dismiss();
                            }

                        }
                    }
                });
    }


    public void setUpUserOrders() {
        if (shopStatusOpen) {
            binding.setIsLoading(true);

            init();

            sec = false;
            mShimmerViewContainer.startShimmer();
            mShimmerViewContainer.setVisibility(View.VISIBLE);

            if (Config.currentUser != null) {
                userViewModel.getUserInfo(Config.currentUser.getUser_id()).observe(this.getViewLifecycleOwner(), user -> {
                    if (user != null && user.size() > 0) {
                        binding.setIsLoading(false);
                        if (user.get(0).getStatus().contains("404 error")) {
                            if (user.get(0).getStatus().contains("UnknownHostException")) {
                                internetSnack(binding.parentLayout);
                            } else {
//                            SessionData.getInstance().getBottomNavigation().clearCount(R.id.nav_order);
                            }
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            binding.setIsLoading(false);
//                            nouserSetUp();
                        } else {

                            if (user.size() > 0) {
                                transactionViewModel.fetchOrders(user.get(0).getUser_id());
                                transactionViewModel.user = user.get(0);
                                transactionViewModel.getTransactions_data().observe(this.getViewLifecycleOwner(), transactions -> {

                                    if (transactions != null) {
                                        if (transactions.size() > 0) {

                                            if (transactions.get(0).getTrans_status_title().contains("404 error")) {
                                                if (transactions.get(0).getTrans_status_title().contains("UnknownHostException")) {
                                                    internetSnack(binding.parentLayout);
                                                }
                                                transactions.clear();
                                            }
                                            transactionListFiltered.clear();
                                            transactionListFiltered.addAll(transactions);

//                                        for (int i = 0; i < transactions.size(); i++) {
//                                            if (transactions.get(i).getTrans_status_title().equalsIgnoreCase("Pending")) {
//                                                transactionListFiltered.add(transactions.get(i));
//                                            }
//                                        }
                                        }
                                        mShimmerViewContainer.stopShimmer();
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        binding.setIsLoading(false);


//                                        orderAdapter.setItems(transactionListFiltered);
                                        binding.radioCod.setChecked(true);
                                        setCodOrders();
                                        binding.radioCod.setText("CASH ON DELIVERY (" + transactionListSecondaryFiltered.size() + ")");

                                        List<Transaction> filteredList = new ArrayList<>();
                                        for (Transaction item : transactionListFiltered) {
                                            if (item.getRazor_payment_status().length() > 0) {
                                                filteredList.add(item);
                                            }
                                        }
                                        binding.radioOnlinePayment.setText("ONLINE PAYMENT (" + filteredList.size() + ")");

                                        if (transactionListFiltered.size() == 0) {
//                                        binding.lnrSubParent.setVisibility(View.GONE);
                                            binding.ordersList.setVisibility(View.GONE);
                                            binding.noOrders.setVisibility(View.VISIBLE);
                                            binding.noOrders.setText("There are no orders");
                                        } else {
//                                        binding.lnrSubParent.setVisibility(View.VISIBLE);
                                            binding.ordersList.setVisibility(View.VISIBLE);
                                            binding.noOrders.setVisibility(View.GONE);
                                        }


                                        List<Transaction> pendingList = new ArrayList<>();
                                        for (Transaction item : transactionListFiltered) {
                                            if (item.getTrans_status_title().equalsIgnoreCase("Pending")) {
                                                pendingList.add(item);
                                            }
                                        }

                                        if (pendingList.size() > 0) {
                                            if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null) {
                                                if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1")) {
                                                    showPendingOrdersDialog(pendingList);
                                                }
                                            }
                                        }
                                        SessionData.getInstance().getBottomNavigation().clearCount(R.id.nav_order);

                                        //Todo: orders count hided here
//                                    int x = 0;
//                                    for (int i = 0; i < transactions.size(); i++) {
//                                        if (transactions.get(i).getTrans_status_title().equalsIgnoreCase("Pending")) {
//                                            x++;
//                                        }
//                                    }
//                                    ordersCount = x;
//                                    SessionData.getInstance().setOrdersCount(ordersCount);
//                                    if (ordersCount > 0) {
//                                        SessionData.getInstance().getBottomNavigation().setCount(R.id.nav_order, ordersCount + "");
//                                    } else {
//                                        if (sec) {
//                                            SessionData.getInstance().getBottomNavigation().clearCount(R.id.nav_order);
//                                        }
//                                        sec = true;
//                                    }
                                    }

                                });

                                binding.setUser(user.get(0));
                                binding.setUserModel(userViewModel);
                            }
                        }
                    }
                });
            }
        }
    }

    private void showPendingOrdersDialog(List<Transaction> transactionList) {
        Dialog dialog;
        dialog = new Dialog(getActivity());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pending_orders_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView closeImg = dialog.findViewById(R.id.close_img);
        RecyclerView recycle_orders_list = dialog.findViewById(R.id.orders_list);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        recycle_orders_list.setLayoutManager(manager);
        TransactionAdapter pendingOrderAdapter;
        pendingOrderAdapter = new TransactionAdapter(transactionList, tran_id -> {
//            SessionData.getInstance().get
            gotoTransactionDetails(tran_id);
            dialog.dismiss();
        });
        recycle_orders_list.setAdapter(pendingOrderAdapter);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void gotoTransactionDetails(String tran_id) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("from_pending", true);
        bundle.putString(transactionViewModel.TRAN_ID, tran_id);
        bundle.putString(transactionViewModel.USER_ID, transactionViewModel.user.getUser_id());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_order_to_transactionDetailsFragment, bundle);
    }


//    public void setUpUserOrders() {
//        binding.setIsLoading(true);
//        init();
//        sec = false;
//
//        if (Config.currentUser != null) {
//            userViewModel.getUserInfo(Config.currentUser.getUser_id()).observe(this.getViewLifecycleOwner(), user -> {
//                if (user != null && user.size() > 0) {
//                    orderViewModel.fetchOrders(user.get(0).getUser_id());
//                    orderViewModel.user = user.get(0);
//                    orderViewModel.getTransactions_data().observe(this.getViewLifecycleOwner(), transactions -> {
//                        if (transactions.size() > 0) {
//                            orderAdapter.setItems(transactions);
//                        }
//
//                        binding.setIsLoading(false);
//                        ordersCount = transactions.size();
//                        SessionData.getInstance().setOrdersCount(ordersCount);
//                        if (ordersCount > 0) {
//                            SessionData.getInstance().getBottomNavigation().setCount(R.id.nav_order, ordersCount + "");
//                        } else {
//                            if (sec) {
//                                SessionData.getInstance().getBottomNavigation().clearCount(R.id.nav_order);
//                            }
//                            sec = true;
//
//                        }
//                    });
//
//                    binding.setUser(user.get(0));
//                    binding.setUserModel(userViewModel);
//
//                }
//            });
//        }
//    }
//    @Override
//    public boolean onBackPressed() {
//        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            //action not popBackStack
//            return true;
//        } else {
//            return false;
//        }
//    }


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
                SessionData.getInstance().setOrdersAFragment(OrdersAFragment.this);
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
            if (item.getRazor_payment_status().length() == 0) {
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
            if (item.getRazor_payment_status().length() > 0) {
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
            if (binding.ordersList.getVisibility() == View.VISIBLE) {
                binding.noOrders.setText("There are no orders in filtered results");
            } else {
                binding.noOrders.setText("There are no orders");
            }
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

    public void setDeliveryStafDetails() {
//        progressDialog.setVisibility(View.VISIBLE);
        binding.shimmerViewContainerDeliveryStaff.startShimmer();
        binding.shimmerViewContainerDeliveryStaff.setVisibility(View.VISIBLE);
        userViewModel.getDeliveryStaffInfo().observe(this.getViewLifecycleOwner(), user -> {
            if (user != null) {
//                progressDialog.setVisibility(View.GONE);
                binding.shimmerViewContainerDeliveryStaff.stopShimmer();
                binding.shimmerViewContainerDeliveryStaff.setVisibility(View.GONE);
                if (user.getStatus().contains("404 error")) {
                    if (user.getStatus().contains("UnknownHostException")) {
//                            internetSnack(binding.parentLayout);
                    } else {
                        //TODO : set delivery staff not available status
                    }

//                        binding.setIsLoading(false);
//                            nouserSetUp();
                    binding.lnrDeliveryStaffNotAvailableLayout.setVisibility(View.VISIBLE);
                    binding.lnrDeliveryStaffLayout.setVisibility(View.GONE);
                } else {
                    if (user.getUser_phone() != null) {
                        if (user.getUser_phone().length() > 0) {
                            binding.lnrDeliveryStaffNotAvailableLayout.setVisibility(View.GONE);
                            binding.lnrDeliveryStaffLayout.setVisibility(View.VISIBLE);
                            binding.txtName.setText(user.getUser_name());
                            binding.txtPhoneNumber.setText(user.getUser_phone());
                            phone = user.getUser_phone();

                            SessionData.getInstance().setDeliveryStaffId(user.getUser_id());
                        } else {
                            binding.lnrDeliveryStaffNotAvailableLayout.setVisibility(View.VISIBLE);
                            binding.lnrDeliveryStaffLayout.setVisibility(View.GONE);
                        }
                    } else {
                        binding.lnrDeliveryStaffNotAvailableLayout.setVisibility(View.VISIBLE);
                        binding.lnrDeliveryStaffLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                if (intent.getExtras().getString("removeCancel") != null) {
                    if (intent.getExtras().getString("removeCancel").toLowerCase().contains("true")) {
                        String id = intent.getExtras().getString("transactionId");
                        orderAdapter.disableCancelForItem(id);
                        //                setUpUserOrders();
                        //                orderAdapter
                    }
                }
                if (intent.getExtras().getString("orderRejected") != null) {
                    if (intent.getExtras().getString("transactionId") != null) {
                        if (intent.getExtras().getString("orderRejected").toLowerCase().contains("true")) {
                            String id = intent.getExtras().getString("transactionId");
                            orderAdapter.disableCancelForRejectedItem(id);
                            //                setUpUserOrders();
                            //                orderAdapter
                        }
                    } else {
                        setUpUserOrders();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


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
