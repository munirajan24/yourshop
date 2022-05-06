package com.dvishapps.yourshop.ui.layout.auth.signin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.FForgotPasswordBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.layout.auth.AuthListener;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;

public class ForgotPwdFragment extends SFragment {

    private FForgotPasswordBinding binding;
    private AuthListener authListener;
    private UserViewModel userViewModel;

    private ShopViewModel shopViewModel;
    private Shop shop;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new UserViewModel(this.getActivity());
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_forgot_password, container, false);
        binding.setViewModel(userViewModel);
        shopViewModel.fetchShop();
        binding.loginBack.setOnClickListener(v -> {
            authListener.onSignInClick();
        });
        if (SessionData.getInstance().getShop()!=null) {
            binding.appNameText.setText(SessionData.getInstance().getShop().getName());
        }
        userViewModel.error.observe(this.getViewLifecycleOwner(), s -> {
            if (s.contains("UnknownHostException")) {
                internetSnack(binding.parentLayout);
            }else {
                binding.forgotPwd.setError(s);
            }
        });

        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                this.shop = shop;
                binding.appNameText.setText(shop.getName());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            authListener = (AuthListener) context;
        } catch (ClassCastException cast) {

        }
    }

    public void internetSnack(View parentLayout){
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Tools.isOnline()) {
                            retryFunctions();
                        }else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black ))
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    private void retryFunctions() {
        userViewModel.forgotPassword();
    }

}
