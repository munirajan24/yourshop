package com.dvishapps.yourshop.ui.layout.shop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dvishapps.yourshop.BuildConfig;
import com.dvishapps.yourshop.utils.SessionData;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.FShopBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
    private FShopBinding binding;
    private ShopViewModel shopViewModel;
    private Shop shop;

    private UserViewModel userViewModel;
    TextView txt_name;
    TextView txt_phone_number;
    ImageView img_call;
    LinearLayout lnr_delivery_staff_layout;
    LinearLayout lnr_delivery_staff_not_available_layout;
    private String phone = "";
    private boolean shopStatusOpen = true;

    public ShopFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        userViewModel = new UserViewModel(this.getActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_shop, container, false);
//        shopViewModel.fetchShop();
//        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
//            if (shop != null) {
//                if (shop.getStatus().contains("404 error")) {
//                    if (shop.getStatus().contains("UnknownHostException")) {
//                        internetSnack(binding.parentLayout);
//                    }
//                }
//                binding.setShop(shop);
//                this.shop = shop;
//                setUpVisibility();
//            }
//        });
        if (SessionData.getInstance().getShop().getStatus().contains("404 errorNo more records")) {
            binding.parentLayout.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
            shopStatusOpen = false;
        }

        webCallFunctions();
        txt_name = binding.txtName;
        txt_phone_number = binding.txtPhoneNumber;
        img_call = binding.imgCall;
        lnr_delivery_staff_layout = binding.lnrDeliveryStaffLayout;
        lnr_delivery_staff_not_available_layout = binding.lnrDeliveryStaffNotAvailableLayout;

        binding.txtAppVersion.setText("App version : " + BuildConfig.VERSION_NAME);


        setDeliveryStafDetails();

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getContext().checkSelfPermission(Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    112);

                            return;
                        } else {

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phone));
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                } else {
                    Console.toast("Phone number not available");
                }
            }
        });

        binding.emailTextView.setOnClickListener(v -> {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
            } catch (Exception e) {

            }
        });

        binding.shopPhoneNumber.setOnClickListener(v -> {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + shop.getAbout_phone1()));
                startActivity(callIntent);
            } catch (Exception e) {
                // no activity to handle intent. show error dialog/toast whatever
                e.printStackTrace();
            }
        });
        return binding.getRoot();
    }

    public void setUpVisibility() {

        if (shop.getAbout_phone2() == null || shop.getAbout_phone2() == "") {
            binding.phone2TextView.setVisibility(View.GONE);
        } else {
            if (shop.getAbout_phone2().length() == 0) {
                binding.phone2TextView.setVisibility(View.GONE);
            } else
                binding.phone2TextView.setVisibility(View.VISIBLE);
        }
        if (shop.getAbout_phone3() == null || shop.getAbout_phone3() == "") {
            binding.phone3TextView.setVisibility(View.GONE);
        } else {
            if (shop.getAbout_phone3().length() == 0) {
                binding.phone3TextView.setVisibility(View.GONE);
            } else
                binding.phone3TextView.setVisibility(View.VISIBLE);
        }
        if (shop.getCity() == null || shop.getCity() == "") {
            binding.txtTitleCity.setVisibility(View.GONE);
            binding.lnrCity.setVisibility(View.GONE);
        } else {
            binding.txtTitleCity.setVisibility(View.VISIBLE);
            binding.lnrCity.setVisibility(View.VISIBLE);
        }
        if (shop.getArea() == null || shop.getArea() == "") {
            binding.txtTitleArea.setVisibility(View.GONE);
            binding.lnrArea.setVisibility(View.GONE);
        } else {
            binding.txtTitleArea.setVisibility(View.VISIBLE);
            binding.lnrArea.setVisibility(View.VISIBLE);
        }
        if (shop.getPostal_code() == null || shop.getPostal_code() == "") {
            binding.txtTitlePostal.setVisibility(View.GONE);
            binding.lnrPostal.setVisibility(View.GONE);
        } else {
            binding.txtTitlePostal.setVisibility(View.VISIBLE);
            binding.lnrPostal.setVisibility(View.VISIBLE);
        }

        if (shop.getShop_current_version().length() > 0) {
            if (!shop.getShop_current_version().equals(BuildConfig.VERSION_NAME)) {

                binding.btnUpdate.setVisibility(View.VISIBLE);

                binding.btnUpdate.setOnClickListener(view -> {
                    final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                });
            }
        }

    }

    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
//                            setUpUserOrders();
                            webCallFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void webCallFunctions() {
        shopViewModel.fetchShop();
        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                if (shop.getStatus().contains("404 error")) {
                    if (shop.getStatus().contains("UnknownHostException")) {
                        internetSnack(binding.parentLayout);
                    }
                }
                binding.setShop(shop);
                this.shop = shop;
                setUpVisibility();


            }
        });
    }

    public void setDeliveryStafDetails() {
        userViewModel.getDeliveryStaffInfo().observe(this.getViewLifecycleOwner(), user -> {
            if (user != null) {
//                    binding.setIsLoading(false);
                if (user.getStatus().contains("404 error")) {
                    if (user.getStatus().contains("UnknownHostException")) {
//                            internetSnack(binding.parentLayout);
                    } else {
                        //TODO : set delivery staff not available status
                    }
//                        mShimmerViewContainer.stopShimmer();
//                        mShimmerViewContainer.setVisibility(View.GONE);
//                        binding.setIsLoading(false);
//                            nouserSetUp();
                    lnr_delivery_staff_not_available_layout.setVisibility(View.VISIBLE);
                    lnr_delivery_staff_layout.setVisibility(View.GONE);
                } else {
                    if (user.getUser_phone() != null) {
                        if (user.getUser_phone().length() > 0) {
                            binding.lnrDeliveryStaffNotAvailableLayout.setVisibility(View.GONE);
                            binding.lnrDeliveryStaffLayout.setVisibility(View.VISIBLE);
                            binding.txtName.setText(user.getUser_name());
                            binding.txtPhoneNumber.setText(user.getUser_phone());
                            phone = user.getUser_phone();

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

}
